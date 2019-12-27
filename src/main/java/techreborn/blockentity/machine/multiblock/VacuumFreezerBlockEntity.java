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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.init.TRBlockEntities;
import techreborn.blockentity.bases.GenericMachineBlockEntity;

public class VacuumFreezerBlockEntity extends GenericMachineBlockEntity implements IContainerProvider {

	public MultiblockChecker multiblockChecker;

	public VacuumFreezerBlockEntity() {
		super(TRBlockEntities.VACUUM_FREEZER, "VacuumFreezer", TechRebornConfig.vacuumFreezerMaxInput, TechRebornConfig.vacuumFreezerMaxEnergy, TRContent.Machine.VACUUM_FREEZER.block, 2);
		final int[] inputs = new int[] { 0 };
		final int[] outputs = new int[] { 1 };
		this.inventory = new RebornInventory<>(3, "VacuumFreezerBlockEntity", 64, this);
		this.crafter = new RecipeCrafter(ModRecipes.VACUUM_FREEZER, this, 2, 1, this.inventory, inputs, outputs);
	}
	
	public boolean getMultiBlock() {
		if (multiblockChecker == null) {
			return false;
		}
		final boolean up = multiblockChecker.checkRectY(1, 1, MultiblockChecker.REINFORCED_CASING, MultiblockChecker.ZERO_OFFSET);
		final boolean down = multiblockChecker.checkRectY(1, 1, MultiblockChecker.REINFORCED_CASING, new BlockPos(0, -2, 0));
		final boolean chamber = multiblockChecker.checkRingYHollow(1, 1, MultiblockChecker.ADVANCED_CASING, new BlockPos(0, -1, 0));
		return down && chamber && up;
	}
	
	// TileGenericMachine
	@Override
	public void tick() {
		if (!world.isClient && getMultiBlock()) {
			super.tick();
		}
	}

	// BlockEntity
	@Override
	public void cancelRemoval() {
		super.cancelRemoval();
		multiblockChecker = new MultiblockChecker(world, pos.offset(Direction.DOWN, 1));
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("vacuumfreezer").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).slot(0, 55, 45).outputSlot(1, 101, 45).energySlot(2, 8, 72).syncEnergyValue()
				.syncCrafterValue().addInventory().create(this, syncID);
	}
}