/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.blockentity.machine.tier2;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blocks.BlockMachineBase;
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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author maxvar (coding), ashendi (textures)
 */
public class PumpBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {

	public static final FluidValue TANK_CAPACITY = FluidValue.BUCKET.multiply(16);
	public static final int DEFAULT_RANGE = 10;
	public static final int DEFAULT_DEPTH = 10;
	public static final int MIN_RANGE = 0;
	public static final int MIN_DEPTH = 1;
	public static final int MAX_RANGE = 50;
	public static final int MAX_DEPTH = 50;
	private Iterator<BlockPos> finder;
	@Nullable
	private Tank tank;
	private boolean exhausted;
	private BlockPos pumpedTargetBlockPos;
	private long timeToPump;
	private int range;
	private int depth;

	public PumpBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.PUMP, pos, state, "Pump", TechRebornConfig.pumpMaxInput, TechRebornConfig.pumpMaxEnergy, TRContent.Machine.PUMP.block, 0);
		this.inventory = new RebornInventory<>(1, "PumpBlockEntity", 64, this);
		this.exhausted = false;
		this.range = DEFAULT_RANGE;
		this.depth = DEFAULT_DEPTH;
	}

	public boolean getExhausted() {
		return exhausted;
	}

	public void setExhausted(boolean exhausted) {
		this.exhausted = exhausted;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int newRange) {
		if (newRange < MIN_RANGE) newRange = MIN_RANGE;
		if (newRange > MAX_RANGE) newRange = MAX_RANGE;
		if (newRange != range) {
			range = newRange;
			reset();
		}
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int newDepth) {
		if (newDepth < MIN_DEPTH) newDepth = MIN_DEPTH;
		if (newDepth > MAX_DEPTH) newDepth = MAX_DEPTH;
		if (newDepth != depth) {
			depth = newDepth;
			reset();
		}
	}

	private void reset() {
		finder = null;
		exhausted = false;
		pumpedTargetBlockPos = null;
		world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, false));
	}

	private void setupFinder() {
		this.finder = new BlockPosIterable(pos, range, depth).iterator();
	}

	@Override
	@NotNull
	public Tank getTank() {
		if (this.tank == null) {
			this.tank = createTank();
		}

		return tank;
	}

	private Tank createTank() {
		return new Tank("PumpBlockEntity", TANK_CAPACITY, this);
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity p0) {
		return TRContent.Machine.PUMP.getStack();
	}

	@Override
	public void readNbt(final NbtCompound tagCompound) {
		super.readNbt(tagCompound);
		getTank().read(tagCompound);
		this.range = tagCompound.getInt("range");
		this.depth = tagCompound.getInt("depth");
		finder = null;
	}

	@Override
	public void writeNbt(final NbtCompound tagCompound) {
		super.writeNbt(tagCompound);
		getTank().write(tagCompound);
		tagCompound.putInt("range", range);
		tagCompound.putInt("depth", depth);
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("pump")
			.player(player.getInventory()).inventory().hotbar().addInventory().blockEntity(this)
			.energySlot(0, 8, 72)
			.sync(getTank())
			.syncEnergyValue()
			.sync(this::getDepth, this::setDepth)
			.sync(this::getRange, this::setRange)
			.sync(this::getExhausted, this::setExhausted)
			.addInventory()
			.create(this, syncID);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);

		if (world == null || world.isClient) return;

		//do nothing if all liquids have been exhausted
		if (this.exhausted) return;

		//has a target block to pump?
		if (pumpedTargetBlockPos != null) {
			//pumping time completed?
			//has space to store?
			//has enough energy to pump?
			if ((world.getTime() >= timeToPump)) {
				//not enough energy to pump?
				if (getEnergy() < (long) (TechRebornConfig.pumpEnergyToCollect * getPowerMultiplier())) {
					//don't drop target, retry it again later
					timeToPump = world.getTime() + (long) (TechRebornConfig.pumpTicksToComplete * (1 - getSpeedMultiplier()));
					return;
				}
				//recheck the target
				BlockState blockState = world.getBlockState(pumpedTargetBlockPos);
				Fluid fluid = getFluid(blockState);
				//no longer fluid there?
				if (fluid == Fluids.EMPTY) {
					//play oops
					if (!isMuffled()) {
						world.playSound(null, this.pos, SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS, 1.0f, 1.0f);
					}
					//drop target (and find the next)
					pumpedTargetBlockPos = null;
					world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, false));
					return;
				}
				//cannot fit fluid into the tank?
				if (!getTank().canFit(fluid, FluidValue.BUCKET)) {
					//don't drop target, retry it again later
					timeToPump = world.getTime() + (long) (TechRebornConfig.pumpTicksToComplete * (1 - getSpeedMultiplier()));
					return;
				}
				//fill tank
				if (getTank().getFluidInstance().isEmpty()) {
					getTank().setFluidInstance(new FluidInstance(fluid, FluidValue.BUCKET));
				} else {
					getTank().getFluidInstance().addAmount(FluidValue.BUCKET);
				}
				//play sound
				if (!isMuffled()) {
					world.playSound(null, this.pos, getTank().getFluid().getBucketFillSound().orElse(SoundEvents.ITEM_BUCKET_FILL), SoundCategory.BLOCKS, 1.0f, 1.0f);
				}
				//consume energy
				this.useEnergy((long) (TechRebornConfig.pumpEnergyToCollect * getPowerMultiplier()));
				//extract drops
				DefaultedList<ItemStack> drops = getDrops(blockState);
				if (!drops.isEmpty()) ItemScatterer.spawn(world, pumpedTargetBlockPos, drops);
				//replace target with solid based on dimension
				final Block replacementBlock;
				final RegistryKey<World> worldRegistryKey = world.getRegistryKey();
				if (worldRegistryKey == World.NETHER) replacementBlock = Blocks.BLACKSTONE;
				else if (worldRegistryKey == World.END) replacementBlock = Blocks.END_STONE;
				else replacementBlock = Blocks.COBBLESTONE;
				world.setBlockState(pumpedTargetBlockPos, replacementBlock.getDefaultState());
				pumpedTargetBlockPos = null;
			}
		} else if (!getTank().isFull()) {
			//find next target
			findNextToPump(world);
			if (pumpedTargetBlockPos != null) {
				timeToPump = world.getTime() + (long) (TechRebornConfig.pumpTicksToComplete * (1 - getSpeedMultiplier()));
			} else {
				//else - consider exhausted
				world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, false));
				this.exhausted = true;
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
			Fluid fluid = getFluid(blockState);
			if (fluid != Fluids.EMPTY && (fluid == getTank().getFluid() || getTank().getFluid() == Fluids.EMPTY)) {
				//if any found - start pumping
				world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, true));
				pumpedTargetBlockPos = blockPos;
				return;
			}
		}
	}

	@NotNull
	private Fluid getFluid(BlockState blockState) {
		FluidState fluidState = blockState.getFluidState();
		Fluid fluid = fluidState.getFluid();
		if (fluidState.getLevel() == 8) return fluid;
		else return Fluids.EMPTY;

	}

	@NotNull
	private DefaultedList<ItemStack> getDrops(BlockState blockState) {
		Block block = blockState.getBlock();
		Item item = block.asItem();
		if (item == Items.AIR) {
			return DefaultedList.ofSize(0);
		} else {
			ItemStack itemStack = item.getDefaultStack();
			return DefaultedList.ofSize(1, itemStack);
		}
	}

	public void handleDepthGuiInputFromClient(int buttonAmount) {
		setDepth(depth + buttonAmount);
	}

	public void handleRangeGuiInputFromClient(int buttonAmount) {
		setRange(range + buttonAmount);
	}

	static class BlockPosIterable implements Iterable<BlockPos> {
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
					final BlockPos pos;
					if (TechRebornConfig.pumpIterateOutwards) {
						pos = layer.get(index % layerSize).down(1 + index / layerSize);
					} else {
						pos = layer.get((m - index - 1) % layerSize).down(1 + (m - index - 1) / layerSize);
					}
					index++;
					return pos;
				}
			};
		}

		static class MeasuredPos extends BlockPos {
			private final double weight;

			public MeasuredPos(int x, int y, int z, double weight) {
				super(x, y, z);
				this.weight = weight;
			}
		}
	}
}
