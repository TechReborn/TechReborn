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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.fluid.FluidValue;
import reborncore.common.network.NetworkManager;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.items.DrillHeadItem;
import techreborn.packets.ClientboundPackets;
import techreborn.utils.WorldHelper;
import techreborn.utils.enums.RigStatus;

import java.util.*;

public class MiningRigBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider, InventoryProvider {
	// CONSTANTS
	private final int DRILL_OFFSET = 3; // Position from just inside block at bottom
	private final int DRILL_MINE_OFFSET = TechRebornConfig.miningRigDrillOffset; // How far down to begin mining (from drill offset)
	private final boolean DEBUG_MODE = false;
	// Inventory slot constants
	private final int ENERGY_SLOT = 0;
	private final int TANK_SLOT = 1;
	private final int DRILL_HEAD_SLOT = 2;
	private final int OUTPUT_SLOT = 3;
	// Inventory and storage
	private final RebornInventory<MiningRigBlockEntity> inventory = new RebornInventory<>(4, "MiningRigBlockEntity", 64, this);
	private final Tank tank = new Tank("MiningRigBlockEntity", FluidValue.BUCKET.multiply(TechRebornConfig.miningRigTankSize), this);
	// State control
	public Set<RigStatus> status = new HashSet<>(); // Hashset of all issues currently present, that need to be addressed
	public int statusNumber; // Client-side sync variable (Can't send hashset, will use int instead)
	// Pipe reserve related variables
	private int pipeReserveCount; // How many pipes are currently above rig
	private BlockPos reserveHead = null; // The top of the stack
	// Drill-related variables
	private ItemStack drillItemStack; // Drill head itemstack in machine inventory
	private DrillHeadItem drillItem; // Drill head item in machine inventory
	private int drillDepth; // How far down relative to machine (including drill head)
	private BlockPos drillHead = null; // Position of drill head, null if not in world
	private boolean activeMining = false; // True when nothing is wrong.
	// Mining cursor (Position currently being mined on Y-Level
	private int curX;
	private int curZ;
	// Frequently accessed variables, set in the onLoad
	private ServerWorld serverWorld;
	private LootContext.Builder builder; // Used to get drops for mining
	private int spawnRadius = 0; // Radius of spawn protection
	private BlockPos spawnPos;


	public MiningRigBlockEntity() {
		// TODO config these values
		super(TRBlockEntities.MINING_RIG, "MiningRig", TechRebornConfig.miningRigMaxInput, TechRebornConfig.miningRigMaxEnergy, TRContent.Machine.MINING_RIG.block, 0);
	}

	// Set thing sup
	@Override
	public void onLoad() {
		super.onLoad();

		if (world == null || world.isClient) return;

		// Check structure
		updatePipeReserve();
		updatePipeDrillDepth();
		updateDrillHead();

		// Ugly, probably better way to do this
		MinecraftServer server = world.getServer();
		assert server != null;
		serverWorld = world.getServer().getWorld(world.getRegistryKey());
		assert serverWorld != null; // TODO what if I am in the nether? TEST

		spawnPos = serverWorld.getSpawnPos();
		spawnRadius = server.getSpawnRadius(serverWorld);

		// Loot context for getting drops
		builder = getDefaultLootContext();
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !isMultiblockValid()) return;


		int amountToRun = 1;
		if (drillItem != null) {
			amountToRun = (int) drillItem.speedFactor;
		}


		for (int i = 0; i < amountToRun; i++) {
			// Only proceed if there's no problems
			if (status.isEmpty()) {
				this.setActiveMining(true);

				if (world.getTime() % 50 == 0) {
					verifyIntegrity();
				} else {
					// Logic starts here, assume everything is correct in terms of state
					drill();
				}
			} else {
				//ChatUtils.sendNoSpamMessages(0, new LiteralText(status.iterator().next().name() + " of " + status.size() + " issues")); // DEBUG
				handleStatus();
				return;
			}
		}
	}

	// Core mining logic, will drill out a radius and add item to inventory.
	private void drill() {
		boolean validMine = false;

		do {
			if (curX > drillHead.getX() + drillItem.range) {
				curZ++;
				curX = drillHead.getX() - drillItem.range;
			}

			// Reached end of this Y-level OR still in buffer-zone
			if (curZ > drillHead.getZ() + drillItem.range || drillHead.getY() >= pos.getY() - DRILL_OFFSET - DRILL_MINE_OFFSET) {
				status.add(RigStatus.FINISHED_Y);
				return;
			}

			// Mine
			BlockPos minePosition = new BlockPos(curX, drillHead.getY(), curZ);

			// Don't mine the drillhead
			if (minePosition.equals(drillHead)) {
				curX++;
				return;
			}

			if (canMine(minePosition)) {
				BlockState minedState = world.getBlockState(minePosition);
				float minedHardness = minedState.getHardness(world, minePosition);

				// Show currently mined blocks
				if (DEBUG_MODE) {
					serverWorld.spawnParticles(ParticleTypes.BARRIER, curX, drillHead.getY(), curZ, 1, 0, 0, 0, 1);
				}

				// Conditions to mine and consume, must not be air and not unbreakable
				if (!minedState.isAir() && minedHardness != -1) {
					// Checking consume status (Need enough energy, liquid and drill durability
					if (!this.canUseEnergy()) {
						status.add(RigStatus.NO_ENERGY);
						return;
					}
					if (!this.canUseDurability(drillItem.durabilityPerBlock)) {
						return;
					}

					if (!this.canUseFluid(drillItem.fluidPerBlock)) {
						status.add(RigStatus.NO_FLUID);
						return;
					}

					// Consume
					this.useEnergy(drillItem.energyPerBlock);
					this.useDurability(drillItem.durabilityPerBlock);
					this.useFluid(drillItem.fluidPerBlock);

					// Mine
					world.setBlockState(minePosition, Blocks.AIR.getDefaultState());
					validMine = true;

					// Deposit
					if (inventory.getStack(OUTPUT_SLOT).isEmpty()) {
						if (!drillItem.voidAll) {
							// TODO add to existing slot instead of waiting for it to be empty
							world.setBlockState(minePosition, Blocks.AIR.getDefaultState());

							List<ItemStack> stacks = minedState.getBlock().getDroppedStacks(minedState, builder);

							if (stacks.size() == 1) {
								inventory.setStack(OUTPUT_SLOT, stacks.get(0));
							} else {
								// TODO handle stuff that drops multiple items
							}
							super.getSlotConfiguration().update(this);
						}
					} else {
						status.add(RigStatus.FULL);
					}
				}
			}

			// Progress
			curX++;
		} while (!validMine);
	}

	// Attempt to fix and issues and resolve them as a status
	private void handleStatus() {
		// Don't need to handle if finished
		if (status.contains(RigStatus.FINISHED)) {
			this.setActiveMining(false);
			return;
		}

		for (RigStatus rigStatus : new ArrayList<>(status)) {
			switch (rigStatus) {
				case FULL:
					this.setActiveMining(false);
					if (inventory.getStack(OUTPUT_SLOT).isEmpty()) {
						status.remove(RigStatus.FULL);
					}
					break;
				case NO_PIPE:
					if (status.contains(RigStatus.FINISHED_Y)) {
						this.setActiveMining(false);
						updatePipeReserve();
						if (pipeReserveCount > 0) {
							status.remove(RigStatus.NO_PIPE);
						}
					} else {
						// Don't worry about the pipe if haven't finished Y
						status.remove(RigStatus.NO_PIPE);
					}
					break;
				case NO_HEAD:
					this.setActiveMining(false);
					updateDrillHead();
					if (drillHead != null) {
						status.remove(RigStatus.NO_HEAD);
					}
					break;
				case NO_FLUID:
					this.setActiveMining(false);
					if (this.canUseFluid(drillItem.fluidPerBlock)) {
						status.remove(RigStatus.NO_FLUID);
					}
					break;
				case NO_ENERGY:
					this.setActiveMining(false);
					if (this.canUseEnergy()) {
						status.remove(RigStatus.NO_ENERGY);
					}
					break;
				case FINISHED_Y:
					if (!status.contains(RigStatus.NO_HEAD) && advanceDrill()) {
						status.remove(RigStatus.FINISHED_Y);
					} else {
						this.setActiveMining(false);
					}
					break;
			}
		}
	}

	// Assume that world isn't null when this is called nor is it a client
	private boolean canMine(BlockPos pos) {
		if (spawnRadius <= 0) {
			return true;
		} else {
			int i = MathHelper.abs(pos.getX() - spawnPos.getX());
			int j = MathHelper.abs(pos.getZ() - spawnPos.getZ());
			int k = Math.max(i, j);
			return k > spawnRadius;
		}
	}

	// Resets X and Z to the starting position (currentpos - radius for both x and z) called for new y-levels
	private void resetMiningCursor() {
		// Reset mining cursor
		curX = drillHead.getX() - drillItem.range;
		curZ = drillHead.getZ() - drillItem.range;
	}

	// Check's overall structure (reserve, digging pipe and the drillhead)
	private void verifyIntegrity() {
		if (!validReserveHead()) {
			updatePipeReserve();
		}
		if (!validDrillHead()) {
			updatePipeDrillDepth();
		}

		updateDrillHead();
	}

	// Update pipe reserve count and reserveHead
	private void updatePipeReserve() {
		pipeReserveCount = WorldHelper.getBlockCountAlongY(this.pos, 1, TRContent.DRILL_PIPE, world);

		// Reserve position is just machine offset by count
		reserveHead = this.pos.offset(Direction.UP, pipeReserveCount);
	}

	// Updates drilldepth (Includes drillhead)
	private void updatePipeDrillDepth() {
		Block[] blocks = {TRContent.DRILL_PIPE, TRContent.Machine.DRILL_HEAD.block};
		drillDepth = WorldHelper.getBlockCountAlongY(this.pos.offset(Direction.DOWN, DRILL_OFFSET), -1, Arrays.asList(blocks), world);
	}

	// Check if there's pipe directly above machine
	private boolean hasPipeAbove() {
		return world.getBlockState(this.pos.offset(Direction.UP)).getBlock() == TRContent.DRILL_PIPE;
	}

	// Take a pipe from reserve off
	private boolean consumePipe() {
		// If reserve is empty but there's a pipe above, update count and head position.
		if (pipeReserveCount == 0 && hasPipeAbove() ||
				world.getBlockState(reserveHead.offset(Direction.UP, 1)).getBlock().is(TRContent.DRILL_PIPE)) {
			updatePipeReserve();
		}

		// No pipe, can't consume
		if (pipeReserveCount == 0) {
			status.add(RigStatus.NO_PIPE);
			return false;
		}

		world.setBlockState(reserveHead, Blocks.AIR.getDefaultState());
		pipeReserveCount--;
		reserveHead = reserveHead.offset(Direction.DOWN);

		return true;
	}

	// Ensure the head of the reserve stack has air above it, pipe or machine below
	private boolean validReserveHead() {
		if (reserveHead == null) {
			return false;
		}

		Block blockHead = world.getBlockState(reserveHead).getBlock();
		Block blockAbove = world.getBlockState(reserveHead.offset(Direction.UP)).getBlock();
		Block blockBelow = world.getBlockState(reserveHead.offset(Direction.DOWN)).getBlock();

		Block pipe = TRContent.DRILL_PIPE;
		return blockHead.is(pipe) && blockAbove.is(Blocks.AIR) &&
				blockBelow.is(pipe) || blockBelow.is(this.getBlockType());
	}

	// Ensure drillhead is a valid drill (drill with pipe directly above it)
	private boolean validDrillHead() {
		if (drillHead == null) {
			return false;
		}

		Block blockHead = world.getBlockState(drillHead).getBlock();
		Block blockAbove = world.getBlockState(drillHead.offset(Direction.UP)).getBlock();

		return blockHead.is(TRContent.Machine.DRILL_HEAD.block) && blockAbove.is(TRContent.DRILL_PIPE);
	}

	// Add drill head to world at end of pipe
	private void addDrillHead() {
		updatePipeDrillDepth();

		// Drill head should be at the end of pipe (ASSUMING no drill)
		drillHead = this.pos.offset(Direction.DOWN, drillDepth + 1 + DRILL_OFFSET);

		if (world.getBlockState(drillHead).getBlock() != TRContent.Machine.DRILL_HEAD.block) {
			world.setBlockState(drillHead, TRContent.Machine.DRILL_HEAD.block.getDefaultState());
			drillDepth++;
		}
	}

	// Remove drill head from the world (ALL vertically)
	private void removeDrillHead() {
		// Could improve this to be more performant, but this is safer TODO
		List<BlockPos> drillHeads = WorldHelper.getBlocksAlongY(this.pos, -1, TRContent.Machine.DRILL_HEAD.block, world, true, null);

		for (BlockPos pos : drillHeads) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}

		this.drillHead = null;
	}

	// Check that drill has drill head item and change if it doesn't or does
	private void updateDrillHead() {
		boolean validDrillItem = validDrillHeadInInventory();
		boolean validDrillHead = validDrillHead();

		if (!validDrillItem || !validDrillHead) {
			this.removeDrillHead();
			status.add(RigStatus.NO_HEAD);
		}

		if (validDrillItem) {
			this.drillItemStack = inventory.getStack(DRILL_HEAD_SLOT);
			this.drillItem = ((DrillHeadItem) drillItemStack.getItem());

			if (drillItem.fortune != 0) {
				builder = getDefaultLootContext().luck(drillItem.fortune);
			} else {
				builder = getDefaultLootContext(); //TODO improve perf
			}
		}

		// Only add if no drillhead and have validItem
		if (drillHead == null && validDrillItem) {
			addDrillHead();

			// Recalculate cursor if cursors are 0 (I know this is an actual position but shouldn't matter)
			if (curX == 0 && curZ == 0) {
				resetMiningCursor();
			}
		}
	}

	// Animation helper fuction, is called by packet to change aniamtion on clients, depending on rig state
	public void setActiveMining(boolean value, boolean force) {
		if (((value == this.activeMining && !force)) || drillHead == null || world == null) {
			// Don't need to do anything if already state or blockhead isn't in world
			return;
		}

		// Notify client for animation
		if (value) {
			NetworkManager.sendToWorld(ClientboundPackets.createPacketMiningRigSync(true, drillHead, force), this.serverWorld);
		} else {
			NetworkManager.sendToWorld(ClientboundPackets.createPacketMiningRigSync(false, drillHead, force), this.serverWorld);
		}

		world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, value));
		this.activeMining = value;
	}

	public void setActiveMining(boolean value) {
		setActiveMining(value, false);
	}

	// Helper function that checks for valid drillhead in rig's inventory.
	private boolean validDrillHeadInInventory() {
		return this.inventory.getStack(DRILL_HEAD_SLOT).getItem() instanceof DrillHeadItem;
	}

	// Move the drill down one, consuming pipe
	private boolean advanceDrill() {
		if (consumePipe()) {
			BlockPos nextDrillPosition = drillHead.offset(Direction.DOWN);
			Block nextBlock = world.getBlockState(nextDrillPosition).getBlock();

			if (!nextBlock.is(Blocks.BEDROCK)) {
				// Move drill to new position and update depth
				world.setBlockState(drillHead, TRContent.DRILL_PIPE.getDefaultState());
				world.setBlockState(nextDrillPosition, TRContent.Machine.DRILL_HEAD.block.getDefaultState());

				drillHead = nextDrillPosition;
				drillDepth++;

				setActiveMining(false);
				resetMiningCursor();
				return true;
			} else {
				status.add(RigStatus.FINISHED);
			}
		}

		return false;
	}

	private LootContext.Builder getDefaultLootContext() {
		return (new LootContext.Builder(Objects.requireNonNull(world.getServer()).getOverworld()))
				.random(world.random)w
				.parameter(LootContextParameters.ORIGIN, new Vec3d(pos.getX(), pos.getY(), pos.getZ()))
				.parameter(LootContextParameters.TOOL, new ItemStack(Items.DIAMOND_PICKAXE));
	}

	private boolean canUseFluid(int amount) {
		return this.tank.getFluidAmount().equalOrMoreThan(FluidValue.fromRaw(amount));
	}

	private void useFluid(int amount) {
		this.tank.setFluidAmount(this.tank.getFluidAmount().subtract(FluidValue.fromRaw(amount)));
	}

	private boolean canUseEnergy() {
		return drillItem != null || this.getEnergy() >= drillItem.energyPerBlock;
	}

	private boolean canUseDurability(double durability) {
		return true;
	}

	private void useDurability(int durability) {
		this.drillItemStack.damage(durability, world.random, null);

		// Check if broken
		if (this.drillItemStack.getDamage() >= drillItemStack.getMaxDamage()) {
			world.createExplosion(null, drillHead.getX(), drillHead.getY(), drillHead.getZ(), 10, Explosion.DestructionType.NONE);

			inventory.setStack(DRILL_HEAD_SLOT, ItemStack.EMPTY);
			status.add(RigStatus.NO_HEAD);
		}

	}


	// Setter/Getters for client/server syncs

	public BlockPos getDrillHead() {
		if (drillHead == null) {
			return new BlockPos(0, -1, 0);
		}
		return drillHead;
	}

	public void setDrillHead(BlockPos drillHead) {
		this.drillHead = drillHead;
	}

	// Get most important status from hashset and return ordinal
	public int getStatusNumber() {
		if (status.size() == 0) {
			return -1;
		}

		// TODO: quite ugly
		int ordinal = -1;
		for (RigStatus stat : status) {
			int statOrdinal = stat.ordinal();
			if (statOrdinal > ordinal) {
				ordinal = statOrdinal;
			}
		}

		return ordinal;
	}

	public void setStatusNumber(int statusNumber) {
		this.statusNumber = statusNumber;
	}

	public int getPipeReserveCount() {
		return pipeReserveCount;
	}

	public void setPipeReserveCount(int pipeReserveCount) {
		this.pipeReserveCount = pipeReserveCount;
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
				.add(0, -2, 1, reinGlass) // Glass 1
				.add(1, -2, 0, reinGlass) // Glass 2
				.add(-1, -2, 0, reinGlass) // Glass 3
				.add(0, -2, -1, reinGlass) // Glass 4
				.add(0, -2, 0, pipe); // Pipe in middle
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		ScreenHandlerBuilder builder = new ScreenHandlerBuilder("miningrig").player(player.inventory).inventory().hotbar().addInventory().blockEntity(this)
				.syncEnergyValue()
				.sync(this::getPipeReserveCount, this::setPipeReserveCount)
				.sync(this::getDrillHead, this::setDrillHead)
				.sync(this::getStatusNumber, this::setStatusNumber)
				.sync(tank)
				.addInventory();

		// Only add slots when valid
		if (isMultiblockValid()) {
			builder.blockEntity(this).energySlot(0, 8, 72)
					.fluidSlot(1, 8, 72)
					.slot(2, 140, 30, (itemStack -> itemStack.getItem() instanceof DrillHeadItem))
					.outputSlot(3, 140, 70);
		}

		return builder.create(this, syncID);

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
	public int slotTransferSpeed() {
		return 1;
	}

	@Override
	public RebornInventory<MiningRigBlockEntity> getInventory() {
		return inventory;
	}

	@Override
	public Tank getTank() {
		return tank;
	}

	@Override
	public boolean hasSlotConfig() {
		return true;
	}
}
