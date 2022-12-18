package techreborn.blockentity.machine.misc;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class PumpBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {

	public static final FluidValue TANK_CAPACITY = FluidValue.BUCKET.multiply(16);
	public static final int DEFAULT_RANGE = 10;
	public static final int DEFAULT_DEPTH = 10;
	protected Tank tank;
	boolean exhausted;
	BlockPos pumpedTargetBlockPos;
	long timePumpingStarted;
	long timeLastScanned;
	int range = 10;

	public int getRange() {
		return range;
	}

	public void setRange(int newRange) {
		range = newRange;
		if (range < 1) range = 1;
		if (range > 50) range = 50;
	}

	int depth = 10;

	public int getDepth() {
		return depth;
	}

	public void setDepth(int newDepth) {
		depth = newDepth;
		if (depth < 1) depth = 1;
		if (depth > 50) depth = 50;
	}

	public PumpBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.PUMP, pos, state, "Pump", TechRebornConfig.pumpMaxInput, TechRebornConfig.pumpMaxEnergy, TRContent.Machine.PUMP.block, 0);
		this.inventory = new RebornInventory<>(1, "PumpBlockEntity", 64, this);
		this.tank = new Tank("PumpBlockEntity", TANK_CAPACITY, this);
		this.exhausted = false;
	}

	@Override
	public Tank getTank() {
		return tank;
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity p0) {
		return TRContent.Machine.PUMP.getStack();
	}

	@Override
	public void readNbt(final NbtCompound tagCompound) {
		super.readNbt(tagCompound);
		tank.read(tagCompound);
		if (tagCompound.contains("rangeConfig")) {
			NbtCompound rangeConfig = tagCompound.getCompound("rangeConfig");
			this.range = rangeConfig.getInt("range");
			this.depth = rangeConfig.getInt("depth");
		} else {
			this.range = DEFAULT_RANGE;
			this.depth = DEFAULT_DEPTH;
		}
	}

	@Override
	public void writeNbt(final NbtCompound tagCompound) {
		super.writeNbt(tagCompound);
		tank.write(tagCompound);
		NbtCompound rangeConfig = new NbtCompound();
		rangeConfig.putInt("range", range);
		rangeConfig.putInt("depth", depth);
		tagCompound.put("rangeConfig", rangeConfig);
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("pump")
			.player(player.getInventory()).inventory().hotbar().addInventory()
			.blockEntity(this)
			.energySlot(0, 8, 72)
			.sync(tank)
			.syncEnergyValue()
			.sync(this::getDepth, this::setDepth)
			.sync(this::getRange, this::setRange)
			.addInventory()
			.create(this, syncID);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);

		if (world == null || world.isClient) return;

		//do nothing if all liquids have been exhausted
		if (this.exhausted) return;

		//if pumping time completed
		if (pumpedTargetBlockPos != null) {
			if ((world.getTime() - timePumpingStarted >= (long) (TechRebornConfig.pumpTicksToComplete * (1 - getSpeedMultiplier())))
				&& getEnergy() >= (long) (TechRebornConfig.pumpEnergyToCollect * getPowerMultiplier())) {
				//collect fluid
				Fluid fluid;
				FluidValue fluidValue = FluidValue.BUCKET;
				SoundEvent soundEvent;
				BlockState blockState = world.getBlockState(pumpedTargetBlockPos);
				if (blockState.getBlock() == Blocks.WATER) {
					fluid = Fluids.WATER;
					soundEvent = SoundEvents.ITEM_BUCKET_EMPTY;
				} else if (blockState.getBlock() == Blocks.LAVA) {
					fluid = Fluids.LAVA;
					soundEvent = SoundEvents.ITEM_BUCKET_EMPTY_LAVA;
				} else {
					fluid = Fluids.EMPTY;
					soundEvent = SoundEvents.BLOCK_DISPENSER_FAIL;
				}
				if (fluid == Fluids.EMPTY || !tank.canFit(fluid, fluidValue)) {
					//play oops
					world.playSound(null, this.pos, soundEvent, SoundCategory.BLOCKS, 1.0f, 1.0f);
					//drop target
					pumpedTargetBlockPos = null;
					return;
				}
				//fill tank
				if (tank.getFluidInstance().isEmpty()) {
					tank.setFluidInstance(new FluidInstance(fluid, fluidValue));
				} else {
					tank.getFluidInstance().addAmount(fluidValue);
				}
				//play sound
				world.playSound(null, this.pos, soundEvent, SoundCategory.BLOCKS, isMuffled() ? 0.1f : 1.0f, 1.0f);
				//consume energy
				this.useEnergy((long) (TechRebornConfig.pumpEnergyToCollect * getPowerMultiplier()));
				//replace target with solid
				world.setBlockState(pumpedTargetBlockPos, Blocks.COBBLESTONE.getDefaultState());
				pumpedTargetBlockPos = null;
				return;
			}
		} else if (world.getTime() - timeLastScanned >= TechRebornConfig.pumpTicksUntilNextAttempt) {
			timeLastScanned = world.getTime();
			if (!this.exhausted && !tank.isFull()) {
				//find next target
				findNextToPump(world);
				if (pumpedTargetBlockPos != null) {
					timePumpingStarted = world.getTime();
					return;
				}
				//else - consider exhausted
				this.exhausted = true;
			}
		}
	}

	private void findNextToPump(World world) {
		found:
		for (int y = this.pos.getY() - 1; y >= this.pos.getY() - 1 - depth; y--) {
			for (int x = this.pos.getX() - range; x <= this.pos.getX() + range; x++) {
				for (int z = this.pos.getZ() - range; z <= this.pos.getZ() + range; z++) {
					BlockPos blockPos = new BlockPos(x, y, z);
					BlockState blockState = world.getBlockState(blockPos);
					Block block = blockState.getBlock();

					if (block == Blocks.LAVA && blockState.getFluidState().getLevel() == 8 && (tank.getFluid() == Fluids.LAVA || tank.getFluid() == Fluids.EMPTY)
						|| block == Blocks.WATER && blockState.getFluidState().getLevel() == 8 && (tank.getFluid() == Fluids.WATER || tank.getFluid() == Fluids.EMPTY)) {
						//if any found - start pumping
						pumpedTargetBlockPos = new BlockPos(x, y, z);
						break found;
					}
				}

			}
		}
	}

	public void handleDepthGuiInputFromClient(int buttonAmount) {
		setDepth(depth + buttonAmount);
	}

	public void handleRangeGuiInputFromClient(int buttonAmount) {
		setRange(range + buttonAmount);
	}
}