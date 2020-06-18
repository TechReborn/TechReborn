/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.blockentity.machine.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.fluid.FluidValue;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.utils.FluidUtils;

import javax.annotation.Nullable;

public class IndustrialGrinderBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {

	public static final FluidValue TANK_CAPACITY = FluidValue.BUCKET.multiply(16);
	public Tank tank;
	public MultiblockChecker multiblockChecker;
	int ticksSinceLastChange;

	public IndustrialGrinderBlockEntity() {
		super(TRBlockEntities.INDUSTRIAL_GRINDER, "IndustrialGrinder", TechRebornConfig.industrialGrinderMaxInput, TechRebornConfig.industrialGrinderMaxEnergy, TRContent.Machine.INDUSTRIAL_GRINDER.block, 7);
		final int[] inputs = new int[] { 0, 1 };
		final int[] outputs = new int[] {2, 3, 4, 5};
		this.inventory = new RebornInventory<>(8, "IndustrialGrinderBlockEntity", 64, this);
		this.crafter = new RecipeCrafter(ModRecipes.INDUSTRIAL_GRINDER, this, 1, 4, this.inventory, inputs, outputs);
		this.tank = new Tank("IndustrialGrinderBlockEntity", IndustrialGrinderBlockEntity.TANK_CAPACITY, this);
		this.ticksSinceLastChange = 0;
	}

	public boolean getMultiBlock() {
		if (multiblockChecker == null) {
			return false;
		}
		final boolean down = multiblockChecker.checkRectY(1, 1, MultiblockChecker.STANDARD_CASING, MultiblockChecker.ZERO_OFFSET);
		final boolean up = multiblockChecker.checkRectY(1, 1, MultiblockChecker.STANDARD_CASING, new BlockPos(0, 2, 0));
		final boolean blade = multiblockChecker.checkRingY(1, 1, MultiblockChecker.ADVANCED_CASING, new BlockPos(0, 1, 0));
		final BlockState centerBlock = multiblockChecker.getBlock(0, 1, 0);
		final boolean center = ((centerBlock.getBlock() instanceof FluidBlock
				|| centerBlock.getBlock() instanceof FluidBlock)
				&& centerBlock.getMaterial() == Material.WATER);
		return down && center && blade && up;
	}

	// TilePowerAcceptor
	@Override
	public void tick() {
		if (multiblockChecker == null) {
			final BlockPos downCenter = pos.offset(getFacing().getOpposite(), 2).offset(Direction.DOWN, 1);
			multiblockChecker = new MultiblockChecker(world, downCenter);
		}

		ticksSinceLastChange++;
		// Check cells input slot 2 time per second
		if (!world.isClient && ticksSinceLastChange >= 10) {
			if (!inventory.getStack(1).isEmpty()) {
				FluidUtils.drainContainers(tank, inventory, 1, 6);
				FluidUtils.fillContainers(tank, inventory, 1, 6, tank.getFluid());
			}
			ticksSinceLastChange = 0;
		}

		super.tick();
	}

	@Override
	public void fromTag(BlockState blockState, final CompoundTag tagCompound) {
		super.fromTag(blockState, tagCompound);
		tank.read(tagCompound);
	}

	@Override
	public CompoundTag toTag(final CompoundTag tagCompound) {
		super.toTag(tagCompound);
		tank.write(tagCompound);
		return tagCompound;
	}

	// TileMachineBase
	@Nullable
	@Override
	public Tank getTank() {
		return tank;
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		// fluidSlot first to support automation and shift-click
		return new ScreenHandlerBuilder("industrialgrinder").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).fluidSlot(1, 34, 35).slot(0, 84, 43).outputSlot(2, 126, 18).outputSlot(3, 126, 36)
				.outputSlot(4, 126, 54).outputSlot(5, 126, 72).outputSlot(6, 34, 55).energySlot(7, 8, 72)
				.sync(tank).syncEnergyValue().syncCrafterValue().addInventory().create(this, syncID);
	}

}