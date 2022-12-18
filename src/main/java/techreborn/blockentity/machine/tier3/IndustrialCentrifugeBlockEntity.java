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

package techreborn.blockentity.machine.tier3;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.items.DynamicCellItem;

public class IndustrialCentrifugeBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {
	public IndustrialCentrifugeBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.INDUSTRIAL_CENTRIFUGE, pos, state, "IndustrialCentrifuge", TechRebornConfig.industrialCentrifugeMaxInput, TechRebornConfig.industrialCentrifugeMaxEnergy, TRContent.Machine.INDUSTRIAL_CENTRIFUGE.block, 6);
		final int[] inputs = new int[]{0, 1};
		final int[] outputs = new int[]{2, 3, 4, 5};
		this.inventory = new RebornInventory<>(7, "IndustrialCentrifugeBlockEntity", 64, this);
		this.crafter = new RecipeCrafter(ModRecipes.CENTRIFUGE, this, 2, 4, this.inventory, inputs, outputs);
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("centrifuge").player(player.getInventory()).inventory().hotbar()
				.addInventory().blockEntity(this)
				.filterSlot(1, 40, 54, stack -> ItemUtils.isItemEqual(stack, DynamicCellItem.getEmptyCell(1), true, true))
				.filterSlot(0, 40, 34, stack -> !ItemUtils.isItemEqual(stack, DynamicCellItem.getEmptyCell(1), true, true))
				.outputSlot(2, 82, 44).outputSlot(3, 101, 25)
				.outputSlot(4, 120, 44).outputSlot(5, 101, 63).energySlot(6, 8, 72).syncEnergyValue()
				.syncCrafterValue().addInventory().create(this, syncID);
	}
}
