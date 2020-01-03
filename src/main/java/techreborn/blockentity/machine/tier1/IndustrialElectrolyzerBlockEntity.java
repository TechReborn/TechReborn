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

package techreborn.blockentity.machine.tier1;

import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.ItemUtils;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.init.TRBlockEntities;
import techreborn.items.ItemDynamicCell;
import techreborn.blockentity.machine.GenericMachineBlockEntity;

public class IndustrialElectrolyzerBlockEntity extends GenericMachineBlockEntity implements IContainerProvider {

	public IndustrialElectrolyzerBlockEntity() {
		super(TRBlockEntities.INDUSTRIAL_ELECTROLYZER, "IndustrialElectrolyzer", TechRebornConfig.industrialElectrolyzerMaxInput, TechRebornConfig.industrialElectrolyzerMaxEnergy, TRContent.Machine.INDUSTRIAL_ELECTROLYZER.block, 6);
		final int[] inputs = new int[] { 0, 1 };
		final int[] outputs = new int[] { 2, 3, 4, 5 };
		this.inventory = new RebornInventory<>(7, "IndustrialElectrolyzerBlockEntity", 64, this);
		this.crafter = new RecipeCrafter(ModRecipes.INDUSTRIAL_ELECTROLYZER, this, 2, 4, this.inventory, inputs, outputs);
	}
	
	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("industrialelectrolyzer").player(player.inventory).inventory().hotbar()
			.addInventory().blockEntity(this)
			.filterSlot(1, 47, 72, stack -> ItemUtils.isItemEqual(stack, ItemDynamicCell.getEmptyCell(1), true, true))
			.filterSlot(0, 81, 72, stack -> !ItemUtils.isItemEqual(stack, ItemDynamicCell.getEmptyCell(1), true, true))
			.outputSlot(2, 51, 24).outputSlot(3, 71, 24).outputSlot(4, 91, 24).outputSlot(5, 111, 24)
			.energySlot(6, 8, 72).syncEnergyValue().syncCrafterValue().addInventory().create(this, syncID);
	}
}