package techreborn.blockentity.machine.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import reborncore.RebornCore;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.GenericMachineBlockEntity;

import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.utils.WorldHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MiningRigBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider, InventoryProvider {
	private boolean hasInit;

	private int pipeReserveCount;
	private BlockPos reserveHead = null;

	private int drillDepth;
	private BlockPos drillHead =  null;

	// Position from just inside block at bottom
	private final int DRILL_OFFSET = 3;
	private final int ENERGY_SLOT = 0;
	private final int TANK_SLOT = 1;
	private final int DRILL_HEAD_SLOT = 2;
	private final int OUTPUT_SLOT = 3;

	// Might be better to move these to an ENUM
	private final int OK = 0;
	private final int FULL = 1;
	private final int NO_PIPE = 2;
	private final int FINISHED = 3;


	private int mineRadius = 16;
	private  int curX;
	private int curZ;


	// Mining Rig state control
	private boolean finishedY;

	private int status = OK;


	// Frequently accessed
	private ServerWorld serverWorld;
	private LootContext.Builder builder;
	private int spawnRadius = 0;
	private BlockPos spawnPos;


	private final RebornInventory<MiningRigBlockEntity> inventory = new RebornInventory<>(4, "MiningRigBlockEntity", 64, this);
	private final Tank tank = null;


	public MiningRigBlockEntity() {
		// TODO config these values
		super(TRBlockEntities.MINING_RIG, "MiningRig", 512, 50000, TRContent.Machine.MINING_RIG.block, 0);
	}

	// Called on first tick on server when world isn't null
	private void init(){
		verifyIntegrity();

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
	}


	@Override
	public void tick() {
		super.tick();
		if(world == null || world.isClient || !isMultiblockValid()){
			return;
		}

		if(!hasInit){
			init();
		}


		// Ensure no one's gon doof our pipes and drill
		if (world.getTime() % 60 == 0) {
			verifyIntegrity();
			hasInit = true;
		}

		// Start rig logic

		if(!finishedY){
			for(int i = 0; i < 520; i++) {
				Drill();
			}
		}else{
			if(advanceDrill()){
				finishedY = false;
			}
		}
	}

	private void Drill(){
		if(curX > drillHead.getX() + mineRadius){
			curZ++;
			curX = drillHead.getX() - mineRadius;
		}

		// Reached end of this Y-level
		if(curZ > drillHead.getZ() + mineRadius){
			finishedY = true;
			return;
		}

		BlockPos minePosition = new BlockPos(curX, drillHead.getY(), curZ);

		// For the love of god, don't mine the drill head
		if(!minePosition.equals(drillHead) && canMine(minePosition)){
			BlockState minedState = world.getBlockState(minePosition);
			Block minedBlock = minedState.getBlock();


			serverWorld.spawnParticles(ParticleTypes.BARRIER, curX,drillHead.getY(), curZ,1,0,0,0,1); // TODO debug remove

			if(!minedBlock.is(Blocks.AIR)) {
				ItemStack mineStack = new ItemStack(minedBlock.asItem());

				if (inventory.getStack(OUTPUT_SLOT).isEmpty()) {
					// TODO add to existing slot instead of waiting for it to be empty
					world.setBlockState(minePosition, Blocks.AIR.getDefaultState());

					List<ItemStack> stacks = minedBlock.getDroppedStacks(minedState, builder);


					if(stacks.size() == 1){
						inventory.setStack(OUTPUT_SLOT, stacks.get(0));
					}else{
						System.out.println("WHAT?");
					}


					// Instantly dump contents to neighbours, can't wait
					super.getSlotConfiguration().update(this);
					if(status == FULL){
						status = OK;
					}
				}else{
					status = FULL;
				}
			}
		}


		// Progress if not full
		if(status != FULL) {
			curX++;
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
		curX = drillHead.getX() - mineRadius;
		curZ = drillHead.getZ() - mineRadius;
	}

	private void verifyIntegrity(){
		updatePipeReserve();
		updatePipeDrillDepth();
		rebuildDrillHead();
	}

	// Update pipe reserve count and reserveHead
	private void updatePipeReserve() {
		pipeReserveCount = WorldHelper.getBlockCountAlongY(this.pos,1, TRContent.DRILL_PIPE, world);

		// Reserve position is just machine offset by count
		reserveHead = this.pos.offset(Direction.UP, pipeReserveCount);
	}

	// Updates drilldepth (Includes drillhead)
	private void updatePipeDrillDepth(){
		Block[] blocks = {TRContent.DRILL_PIPE, TRContent.DRILL_HEAD};
		drillDepth = WorldHelper.getBlockCountAlongY(this.pos.offset(Direction.DOWN, DRILL_OFFSET),-1, Arrays.asList(blocks), world);
	}

	// Check if there's pipe directly above machine
	private boolean hasPipeAbove(){
		return world.getBlockState(this.pos.offset(Direction.UP)).getBlock() == TRContent.DRILL_PIPE;
	}

	// Take a pipe from reserve off
	private boolean consumePipe(){
		// If reserve is empty but there's a pipe above, update count and head position.
		if(pipeReserveCount == 0 && hasPipeAbove()){
			updatePipeReserve();
		}

		// No pipe, can't consume
		if(pipeReserveCount == 0){
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

		return blockHead.is(TRContent.DRILL_HEAD) && blockAbove.is(TRContent.DRILL_PIPE);
	}

	private boolean addDrillHead(){
		updatePipeDrillDepth();

		// Drill head should be at the end of pipe (ASSUMING no drill)
		drillHead = this.pos.offset(Direction.DOWN,   drillDepth + 1 + DRILL_OFFSET);

 		if(world.getBlockState(drillHead).getBlock() != TRContent.DRILL_HEAD) {
			world.setBlockState(drillHead, TRContent.DRILL_HEAD.getDefaultState());
			drillDepth++;
		}

 		// Recalculate cursor if cursors are 0 (I know this is an actual position but shouldn't matter)
		if(curX == 0 && curZ == 0){
			resetMiningCursor();
		}

		return true;
	}
	private void removeDrillHead(){
		// Could improve this to be more performant, but this is safer TODO
		List<BlockPos> drillHeads = WorldHelper.getBlocksAlongY(this.pos,-1,TRContent.DRILL_HEAD,world,true, null);

		for(BlockPos pos : drillHeads){
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}
	private void rebuildDrillHead(){
		this.removeDrillHead();
		this.addDrillHead();
	}


	// Move the drill down one, consuming pipe
	private boolean advanceDrill(){
		if(!validDrillHead()){
			rebuildDrillHead();
		}

		if(!validReserveHead()){
			updatePipeReserve();

			if(pipeReserveCount == 0){
				status = NO_PIPE;
				return false;
			}else{
				status = OK;
			}
		}

		BlockPos nextDrillPosition = drillHead.offset(Direction.DOWN);
		Block nextBlock = world.getBlockState(nextDrillPosition).getBlock();
		if(!nextBlock.is(Blocks.BEDROCK)) {
			status = OK;
			world.setBlockState(drillHead, TRContent.DRILL_PIPE.getDefaultState());

			// Move drill to new position and update depth
			drillHead = nextDrillPosition;
			world.setBlockState(drillHead, TRContent.DRILL_HEAD.getDefaultState());
			drillDepth++;

			consumePipe();

			resetMiningCursor();

			return true;
		}else{
			status = FINISHED;
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
				.slot(2,140, 30, (itemStack -> {

					// Ensure is drillhead
					Item item = itemStack.getItem();
					for (TRContent.DrillHeads head : TRContent.DrillHeads.values()) {
						if(head.item.equals(item)){
							return true;
						}
					}
					return false;
				})).outputSlot(3,140,70)
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
