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
import org.jetbrains.annotations.NotNull;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.TechReborn;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.Iterator;

public class PumpBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {

	public static final FluidValue TANK_CAPACITY = FluidValue.BUCKET.multiply(16);
	public static final int DEFAULT_RANGE = 10;
	public static final int DEFAULT_DEPTH = 10;
	private Iterator<BlockPos> finder;
	protected final Tank tank;
	boolean exhausted;
	BlockPos pumpedTargetBlockPos;
	long timePumpingStarted;
	long timeLastScanned;
	int range;

	public int getRange() {
		return range;
	}

	public void setRange(int newRange) {
		TechReborn.LOGGER.info("setRange {} → {}", range, newRange);
		if (newRange != range) {
			range = newRange;
			if (range < 1) range = 1;
			if (range > 50) range = 50;
			finder = null;
			exhausted = false;
		}
	}

	int depth;

	public int getDepth() {
		return depth;
	}

	public void setDepth(int newDepth) {
		TechReborn.LOGGER.info("setDepth {} → {}", depth, newDepth);
		if (newDepth != depth) {
			depth = newDepth;
			if (depth < 1) depth = 1;
			if (depth > 50) depth = 50;
			finder = null;
			exhausted = false;
		}
	}

	public PumpBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.PUMP, pos, state, "Pump", TechRebornConfig.pumpMaxInput, TechRebornConfig.pumpMaxEnergy, TRContent.Machine.PUMP.block, 0);
		TechReborn.LOGGER.debug("constructing pump at {} with state {}...", pos, state);
		this.inventory = new RebornInventory<>(1, "PumpBlockEntity", 64, this);
		this.tank = new Tank("PumpBlockEntity", TANK_CAPACITY, this);
		this.exhausted = false;
		this.range = DEFAULT_RANGE;
		this.depth = DEFAULT_DEPTH;
		TechReborn.LOGGER.debug("pump created");
	}

	private void setupFinder() {
		this.finder = new BlockPosIterable(pos, range, depth).iterator();
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
		this.range = tagCompound.getInt("range");
		this.depth = tagCompound.getInt("depth");
		TechReborn.LOGGER.debug("readNbt, contains range {} depth {}", range, depth);
		finder = null;
	}

	@Override
	public void writeNbt(final NbtCompound tagCompound) {
		super.writeNbt(tagCompound);
		tank.write(tagCompound);
		tagCompound.putInt("range", range);
		tagCompound.putInt("depth", depth);
		TechReborn.LOGGER.debug("writeNbt, range {} depth {}", range, depth);
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("pump").player(player.getInventory()).inventory().hotbar().addInventory().blockEntity(this).energySlot(0, 8, 72).sync(tank).syncEnergyValue().sync(this::getDepth, this::setDepth).sync(this::getRange, this::setRange).addInventory().create(this, syncID);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);

		if (world == null || world.isClient) return;

		//do nothing if all liquids have been exhausted
		if (this.exhausted) return;

		//if pumping time completed
		if (pumpedTargetBlockPos != null) {
			if ((world.getTime() - timePumpingStarted >= (long) (TechRebornConfig.pumpTicksToComplete * (1 - getSpeedMultiplier()))) && getEnergy() >= (long) (TechRebornConfig.pumpEnergyToCollect * getPowerMultiplier())) {
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
				TechReborn.LOGGER.debug("pump {} exhausted", pos);
			}
		}
	}

	private void findNextToPump(World world) {
		if (finder == null) {
			setupFinder();
		}
		while (finder.hasNext()) {
			BlockPos blockPos = finder.next();

			BlockState blockState = world.getBlockState(blockPos);
			Block block = blockState.getBlock();

			if (block == Blocks.LAVA && blockState.getFluidState().getLevel() == 8 && (tank.getFluid() == Fluids.LAVA || tank.getFluid() == Fluids.EMPTY) || block == Blocks.WATER && blockState.getFluidState().getLevel() == 8 && (tank.getFluid() == Fluids.WATER || tank.getFluid() == Fluids.EMPTY)) {
				//if any found - start pumping
				pumpedTargetBlockPos = blockPos;
				return;
			}
		}
	}

	public void handleDepthGuiInputFromClient(int buttonAmount) {
		setDepth(depth + buttonAmount);
	}

	public void handleRangeGuiInputFromClient(int buttonAmount) {
		setRange(range + buttonAmount);
	}

	static class BlockPosIterable implements Iterable<BlockPos> {

		static class MeasuredPos extends BlockPos {
			private final double weight;

			public MeasuredPos(int x, int y, int z, double weight) {
				super(x, y, z);
				this.weight = weight;
			}
		}

		int index;
		final int layerSize;
		final int m;

		final ArrayList<MeasuredPos> layer;

		public BlockPosIterable(BlockPos centerTop, int range, int depth) {
			this.index = 0;
			this.layerSize = (range * 2 + 1) * (range * 2 + 1);
			this.m = layerSize * depth;
			layer = new ArrayList<>(layerSize);
			for (int i = centerTop.getX() - range; i <= centerTop.getX() + range; i++)
				for (int j = centerTop.getZ() - range; j <= centerTop.getZ() + range; j++)
					layer.add(new MeasuredPos(i, centerTop.getY(), j, centerTop.getSquaredDistance(i, centerTop.getY(), j)));
			layer.sort((o1, o2) -> (int) (o1.weight - o2.weight));
			TechReborn.LOGGER.info("prepared layer info for pos {} with range {} and depth {}", centerTop, range, depth);
			TechReborn.LOGGER.info("layer size = {} and m = {}", layerSize, m);
		}

		/**
		 * Returns an iterator over elements of type {@code T}.
		 *
		 * @return an Iterator.
		 */
		@NotNull
		@Override
		public Iterator<BlockPos> iterator() {
			return new Iterator<>() {
				@Override
				public boolean hasNext() {
					return index < m;
				}

				@Override
				public BlockPos next() {
					BlockPos pos = layer.get(index % layerSize).down(1 + index / layerSize);
					index++;
					return pos;
				}
			};
		}
	}
}