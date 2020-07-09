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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class DistillationTowerBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {

	public DistillationTowerBlockEntity() {
		super(TRBlockEntities.DISTILLATION_TOWER, "DistillationTower", TechRebornConfig.distillationTowerMaxInput, TechRebornConfig.distillationTowerMaxEnergy, TRContent.Machine.DISTILLATION_TOWER.block, 6);
		final int[] inputs = new int[]{0, 1};
		final int[] outputs = new int[]{2, 3, 4, 5};
		this.inventory = new RebornInventory<>(7, "DistillationTowerBlockEntity", 64, this);
		this.crafter = new RecipeCrafter(ModRecipes.DISTILLATION_TOWER, this, 2, 4, this.inventory, inputs, outputs);
	}

	@Override
	public void writeMultiblock(MultiblockWriter writer) {
		writer.translate(1, 0, -1)
				.fill(0, 0, 0, 3, 1, 3, TRContent.MachineBlocks.BASIC.getCasing().getDefaultState())
				.ringWithAir(Direction.Axis.Y, 3, 1, 3, TRContent.MachineBlocks.INDUSTRIAL.getCasing().getDefaultState())
				.ringWithAir(Direction.Axis.Y, 3, 2, 3, TRContent.MachineBlocks.BASIC.getCasing().getDefaultState())
				.fill(0, 3, 0, 3, 4, 3, TRContent.MachineBlocks.INDUSTRIAL.getCasing().getDefaultState());
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("Distillationtower").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).slot(0, 35, 27).slot(1, 35, 47).outputSlot(2, 79, 37).outputSlot(3, 99, 37)
				.outputSlot(4, 119, 37).outputSlot(5, 139, 37).energySlot(6, 8, 72).syncEnergyValue().syncCrafterValue()
				.addInventory().create(this, syncID);
	}

	@Override
	public boolean canCraft(RebornRecipe rebornRecipe) {
		return isMultiblockValid();
	}
}