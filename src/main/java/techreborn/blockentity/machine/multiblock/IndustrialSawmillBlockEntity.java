/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.fluid.FluidValue;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.init.TRBlockEntities;
import techreborn.blockentity.bases.GenericMachineBlockEntity;
import techreborn.utils.FluidUtils;

import javax.annotation.Nullable;

public class IndustrialSawmillBlockEntity extends GenericMachineBlockEntity implements IContainerProvider {

	public static final FluidValue TANK_CAPACITY = FluidValue.BUCKET.multiply(16);
	public Tank tank;
	public MultiblockChecker multiblockChecker;
	int ticksSinceLastChange;

	public IndustrialSawmillBlockEntity() {
		super(TRBlockEntities.INDUSTRIAL_SAWMILL, "IndustrialSawmill", TechRebornConfig.industrialSawmillMaxInput, TechRebornConfig.industrialSawmillMaxEnergy, TRContent.Machine.INDUSTRIAL_SAWMILL.block, 6);
		final int[] inputs = new int[] { 0, 1 };
		final int[] outputs = new int[] { 2, 3, 4 };
		this.inventory = new RebornInventory<>(7, "SawmillBlockEntity", 64, this);
		this.crafter = new RecipeCrafter(ModRecipes.INDUSTRIAL_SAWMILL, this, 1, 3, this.inventory, inputs, outputs);
		this.tank = new Tank("SawmillBlockEntity", IndustrialSawmillBlockEntity.TANK_CAPACITY, this);
		this.ticksSinceLastChange = 0;
	}

	public boolean getMutliBlock() {
		if (multiblockChecker == null) {
			return false;
		}
		final boolean down = multiblockChecker.checkRectY(1, 1, MultiblockChecker.STANDARD_CASING, MultiblockChecker.ZERO_OFFSET);
		final boolean up = multiblockChecker.checkRectY(1, 1, MultiblockChecker.STANDARD_CASING, new BlockPos(0, 2, 0));
		final boolean blade = multiblockChecker.checkRingY(1, 1, MultiblockChecker.REINFORCED_CASING, new BlockPos(0, 1, 0));
		final BlockState centerBlock = multiblockChecker.getBlock(0, 1, 0);
		final boolean center = ((centerBlock.getBlock() instanceof FluidBlock
				|| centerBlock.getBlock() instanceof FluidBlock)
				&& centerBlock.getMaterial() == Material.WATER);
		return down && center && blade && up;
	}

	// TileGenericMachine
	@Override
	public void tick() {
		if (multiblockChecker == null) {
			final BlockPos downCenter = pos.offset(getFacing().getOpposite(), 2).offset(Direction.DOWN, 1);
			multiblockChecker = new MultiblockChecker(world, downCenter);
		}

		ticksSinceLastChange++;
		// Check cells input slot 2 time per second
		if (!world.isClient && ticksSinceLastChange >= 10) {
			if (!inventory.getInvStack(1).isEmpty()) {
				FluidUtils.drainContainers(tank, inventory, 1, 5);
				FluidUtils.fillContainers(tank, inventory, 1, 5, tank.getFluidInstance().getFluid());
			}
			ticksSinceLastChange = 0;
		}

		super.tick();

	}
	
	// TilePowerAcceptor
	@Override
	public void fromTag(final CompoundTag tagCompound) {
		super.fromTag(tagCompound);
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
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("industrialsawmill").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).fluidSlot(1, 34, 35).slot(0, 84, 43).outputSlot(2, 126, 25).outputSlot(3, 126, 43)
				.outputSlot(4, 126, 61).outputSlot(5, 34, 55).energySlot(6, 8, 72).sync(tank).syncEnergyValue().syncCrafterValue()
				.addInventory().create(this, syncID);
	}

}