package techreborn.blockentity.machine.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.util.ChatUtils;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.items.DrillHeadItem;
import techreborn.utils.WorldHelper;
import techreborn.utils.enums.RigStatus;

import java.util.*;

public class MiningRigBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider, InventoryProvider {
	private boolean hasInit;
	private boolean prevMultiblockState = false;

	private int pipeReserveCount;
	private BlockPos reserveHead = null;

	private DrillHeadItem drillItem;
	private int drillDepth;
	private BlockPos drillHead =  null;

	// Position from just inside block at bottom
	private final int DRILL_OFFSET = 3;

	// Inventory slot constants
	private final int ENERGY_SLOT = 0;
	private final int TANK_SLOT = 1;
	private final int DRILL_HEAD_SLOT = 2;
	private final int OUTPUT_SLOT = 3;

	// Mining cursor
	private  int curX;
	private int curZ;

	// State control
	private final Set<RigStatus> status = new HashSet<>();

	// Frequently accessed
	private ServerWorld serverWorld;
	private LootContext.Builder builder;
	private int spawnRadius = 0;
	private BlockPos spawnPos;

	// Consume constants TODO move to config
	private double ENERGY_PER_BLOCK = 150;

	// Inventory and storage
	private final RebornInventory<MiningRigBlockEntity> inventory = new RebornInventory<>(4, "MiningRigBlockEntity", 64, this);
	private final Tank tank = null;


	public MiningRigBlockEntity() {
		// TODO config these values
		super(TRBlockEntities.MINING_RIG, "MiningRig", 512, 50000, TRContent.Machine.MINING_RIG.block, 0);
	}

	// Called on first tick on server when world isn't null
	private void init(){
		// Check structure
		updatePipeReserve();
		updatePipeDrillDepth();
		updateDrillHead();

		// Ugly, probably better way to do this
		assert world != null;
		MinecraftServer server = world.getServer();
		assert server != null;
		serverWorld = world.getServer().getWorld(world.getRegistryKey());
		assert serverWorld != null;

		spawnPos = serverWorld.getSpawnPos();
		spawnRadius = server.getSpawnRadius(serverWorld);

		// Loot context for getting drops
		builder = (new LootContext.Builder(Objects.requireNonNull(world.getServer()).getOverworld()))
				.random(world.random)
				.parameter(LootContextParameters.POSITION, pos)
				.parameter(LootContextParameters.TOOL, new ItemStack(Items.DIAMOND_PICKAXE));

		// Flip the flipflop :3
		hasInit = true;
	}

	@Override
	public void tick() {
		super.tick();

		if(world == null || world.isClient){
			return;
		}

		// Need to clear info if multiblock becomes invalid whilst it has already been valid
		if(!isMultiblockValid()){
			if(prevMultiblockState) {
				reserveHead = null;
				drillItem = null;
				drillHead = null;
				hasInit = false;

				prevMultiblockState = false;
			}

			return;
		}

		if(!hasInit){
			init();
			prevMultiblockState = true;
		}

		// Only proceed if there's no problems
		if(status.isEmpty()) {

			if (world.getTime() % 50 == 0) {
				verifyIntegrity();
			}else{
				// Logic starts here, assume everything is correct in terms of state
				drill();
			}

		}else{
			ChatUtils.sendNoSpamMessages(0, new LiteralText(status.iterator().next().name() + " of " + status.size() + " issues"));
			handleStatus();
		}
	}

	private void drill(){
		boolean validMine = false;

		do {
			if (curX > drillHead.getX() + drillItem.range) {
				curZ++;
				curX = drillHead.getX() - drillItem.range;
			}

			// Reached end of this Y-level
			if (curZ > drillHead.getZ() + drillItem.range) {
				status.add(RigStatus.FINISHED_Y);
				return;
			}

			// Mine down one from drill's Y-Level
			BlockPos minePosition = new BlockPos(curX, drillHead.getY() - 1, curZ);

			if (canMine(minePosition)) {
				BlockState minedState = world.getBlockState(minePosition);

				// TODO debug remove
				serverWorld.spawnParticles(ParticleTypes.BARRIER, curX, drillHead.getY(), curZ, 1, 0, 0, 0, 1);

				if (!minedState.isAir() && minedState.getHardness(world,minePosition) != -1) {
					if(!this.canUseEnergy(ENERGY_PER_BLOCK)){
						status.add(RigStatus.NO_ENERGY);
						return;
					}

					this.useEnergy(ENERGY_PER_BLOCK);
					world.setBlockState(minePosition, Blocks.AIR.getDefaultState());
					validMine = true;

					// INVENTORY, DISABLED FOR TESTING
//				if (inventory.getStack(OUTPUT_SLOT).isEmpty()) {
//					// TODO add to existing slot instead of waiting for it to be empty
//					world.setBlockState(minePosition, Blocks.AIR.getDefaultState());
//
//					List<ItemStack> stacks = minedBlock.getDroppedStacks(minedState, builder);
//
//
//					if(stacks.size() == 1){
//						inventory.setStack(OUTPUT_SLOT, stacks.get(0));
//					}
//
//					// Instantly dump contents to neighbours, can't wait
//					super.getSlotConfiguration().update(this);
//				}else{
//					status.add(RigStatus.FULL);
//				}

				}
			}

			curX++;
		}while (!validMine);
	}

	private void handleStatus(){
		for(RigStatus rigStatus : new ArrayList<>(status)){
			switch (rigStatus){
				case FULL:
					if(inventory.getStack(OUTPUT_SLOT).isEmpty()){
						status.remove(RigStatus.FULL);
					}
					break;
				case NO_PIPE:
					updatePipeReserve();
					if(pipeReserveCount > 0){
						status.remove(RigStatus.NO_PIPE);
					}
					break;
				case NO_HEAD:
					updateDrillHead();
					if(drillHead != null){
						status.remove(RigStatus.NO_HEAD);
					}
				case NO_ENERGY:
					if(this.canUseEnergy(ENERGY_PER_BLOCK)){
						status.remove(RigStatus.NO_ENERGY);
					}
					break;
				case FINISHED_Y:
					if(!status.contains(RigStatus.NO_HEAD) && advanceDrill()){
						status.remove(RigStatus.FINISHED_Y);
					}
					break;
				case FINISHED:
					break;
			}
		}
	}

	// Assume that world isn't null when this is called nor is it a client
	private boolean canMine(BlockPos pos) {
		// TODO make more performant

		if (spawnRadius <= 0) {
			return true;
		} else {
			int i = MathHelper.abs(pos.getX() - spawnPos.getX());
			int j = MathHelper.abs(pos.getZ() - spawnPos.getZ());
			int k = Math.max(i, j);
 			return k > spawnRadius;
		}
	}

	private void resetMiningCursor(){
		// Reset mining cursor
		curX = drillHead.getX() - drillItem.range;
		curZ = drillHead.getZ() - drillItem.range;
	}

	private void verifyIntegrity(){
		if(!validReserveHead()) {
			updatePipeReserve();
		}
		if(!validDrillHead()) {
			updatePipeDrillDepth();
		}

		updateDrillHead();
	}

	// Update pipe reserve count and reserveHead
	private void updatePipeReserve() {
		pipeReserveCount = WorldHelper.getBlockCountAlongY(this.pos,1, TRContent.DRILL_PIPE, world);

		// Reserve position is just machine offset by count
		reserveHead = this.pos.offset(Direction.UP, pipeReserveCount);
	}

	// Updates drilldepth (Includes drillhead)
	private void updatePipeDrillDepth(){
		Block[] blocks = {TRContent.DRILL_PIPE, TRContent.Machine.DRILL_HEAD.block};
		drillDepth = WorldHelper.getBlockCountAlongY(this.pos.offset(Direction.DOWN, DRILL_OFFSET),-1, Arrays.asList(blocks), world);
	}

	// Check if there's pipe directly above machine
	private boolean hasPipeAbove(){
		return world.getBlockState(this.pos.offset(Direction.UP)).getBlock() == TRContent.DRILL_PIPE;
	}

	// Take a pipe from reserve off
	private boolean consumePipe(){
		// If reserve is empty but there's a pipe above, update count and head position.
		if(pipeReserveCount == 0 && hasPipeAbove() ||
				world.getBlockState(reserveHead.offset(Direction.UP, 1)).getBlock().is(TRContent.DRILL_PIPE)){
			updatePipeReserve();
		}

		// No pipe, can't consume
		if(pipeReserveCount == 0){
			status.add(RigStatus.NO_PIPE);
			return false;
		}

		world.setBlockState(reserveHead, Blocks.AIR.getDefaultState());
		pipeReserveCount--;
		reserveHead = reserveHead.offset(Direction.DOWN);

		return true;
	}

	// Ensure the head of the reserve stack has air above it, pipe or machine below
	private boolean validReserveHead(){
		if(reserveHead == null){
			return false;
		}

		Block blockHead = world.getBlockState(reserveHead).getBlock();
		Block blockAbove = world.getBlockState(reserveHead.offset(Direction.UP)).getBlock();
		Block blockBelow = world.getBlockState(reserveHead.offset(Direction.DOWN)).getBlock();

		Block pipe = TRContent.DRILL_PIPE;
		return blockHead.is(pipe) && blockAbove.is(Blocks.AIR) &&
				blockBelow.is(pipe) || blockBelow.is(this.getBlockType());
	}

	private boolean validDrillHead(){
		if(drillHead == null){
			return false;
		}

		Block blockHead = world.getBlockState(drillHead).getBlock();
		Block blockAbove = world.getBlockState(drillHead.offset(Direction.UP)).getBlock();

		return blockHead.is(TRContent.Machine.DRILL_HEAD.block) && blockAbove.is(TRContent.DRILL_PIPE);
	}

	private void addDrillHead(){
		updatePipeDrillDepth();

		// Drill head should be at the end of pipe (ASSUMING no drill)
		drillHead = this.pos.offset(Direction.DOWN,   drillDepth + 1 + DRILL_OFFSET);

 		if(world.getBlockState(drillHead).getBlock() != TRContent.Machine.DRILL_HEAD.block) {
			world.setBlockState(drillHead,TRContent.Machine.DRILL_HEAD.block.getDefaultState());
			drillDepth++;
		}
	}

	private void removeDrillHead(){
		// Could improve this to be more performant, but this is safer TODO
		List<BlockPos> drillHeads = WorldHelper.getBlocksAlongY(this.pos,-1, TRContent.Machine.DRILL_HEAD.block, world,true, null);

		for(BlockPos pos : drillHeads){
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}

		this.drillHead = null;
	}

	private void updateDrillHead(){
		boolean validDrillItem = validDrillHeadInInventory();
		boolean validDrillHead = validDrillHead();

		if(!validDrillItem || !validDrillHead){
			this.removeDrillHead();
			status.add(RigStatus.NO_HEAD);
		}

		if(validDrillItem){
			this.drillItem = ((DrillHeadItem)inventory.getStack(DRILL_HEAD_SLOT).getItem());
		}

		// Only add if no drillhead and have validItem
		if(drillHead == null && validDrillItem){
			addDrillHead();

			// Recalculate cursor if cursors are 0 (I know this is an actual position but shouldn't matter)
			if(curX == 0 && curZ == 0) {
				resetMiningCursor();
			}
		}
	}

	private boolean validDrillHeadInInventory(){
		return this.inventory.getStack(DRILL_HEAD_SLOT).getItem() instanceof DrillHeadItem;
	}

	// Move the drill down one, consuming pipe
	private boolean advanceDrill(){
		BlockPos nextDrillPosition = drillHead.offset(Direction.DOWN);
		Block nextBlock = world.getBlockState(nextDrillPosition).getBlock();

		if(!nextBlock.is(Blocks.BEDROCK)) {
			if(consumePipe()) {

				world.setBlockState(drillHead, TRContent.DRILL_PIPE.getDefaultState());

				// Move drill to new position and update depth
				drillHead = nextDrillPosition;
				world.setBlockState(drillHead, TRContent.Machine.DRILL_HEAD.block.getDefaultState());
				drillDepth++;

				resetMiningCursor();

				return true;
			}
		}else{
			status.add(RigStatus.FINISHED);
		}

		return false;
	}

	// Overrides

	@Override
	public void writeMultiblock(MultiblockWriter writer) {
		BlockState basic = TRContent.MachineBlocks.BASIC.getCasing().getDefaultState();
		BlockState advanced = TRContent.MachineBlocks.ADVANCED.getCasing().getDefaultState();
		BlockState reinGlass = TRContent.REINFORCED_GLASS.getDefaultState();
		BlockState pipe = TRContent.DRILL_PIPE.getDefaultState();

		// TODO use predicate for manual blocks
		writer.translate(-1, -4, -1)
				.ring(Direction.Axis.Y, 3, 1, 3, (view, pos) -> view.getBlockState(pos) == basic, basic, (view, pos) -> view.getBlockState(pos) == pipe, pipe)
				.ring(Direction.Axis.Y, 3, 3, 3, (view, pos) -> view.getBlockState(pos) == basic, basic, (view, pos) -> view.getBlockState(pos) == pipe, pipe)
				.add(2, 2, 2, advanced) // Corner 1
				.add(1, -2, 1, advanced) // Corner 2
				.add(1, -2, -1, advanced) // Corner 3
				.add(-1, -2, 1, advanced) // Corner 4
				.add(0,-2, 1, reinGlass) // Glass 1
				.add(1,-2, 0, reinGlass) // Glass 2
				.add(-1,-2, 0, reinGlass) // Glass 3
				.add(0,-2, -1, reinGlass) // Glass 4
				.add(0, -2, 0, pipe); // Pipe in middle
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("miningrig").player(player.inventory).inventory().hotbar().addInventory().blockEntity(this)
				.energySlot(0,8,72)
				.fluidSlot(1,8,72)
				.slot(2,140, 30, (itemStack -> itemStack.getItem() instanceof DrillHeadItem))
				.outputSlot(3,140,70)
				.syncEnergyValue()
//				.sync(tank)
				.addInventory().create(this, syncID);
	}

	@Override
	public boolean canExtract(int index, ItemStack stack, Direction direction) {
		return true;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	@Override
	public RebornInventory<MiningRigBlockEntity> getInventory() {
		return inventory;
	}

	//	@Nullable
//	@Override
//	public Tank getTank() {
//		return tank;
//	}

	@Override
	public boolean hasSlotConfig() {
		return true;
	}
}
