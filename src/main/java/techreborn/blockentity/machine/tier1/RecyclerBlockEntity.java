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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import reborncore.api.blockentity.IUpgrade;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.api.recipe.RecyclerRecipeCrafter;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class RecyclerBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {

	public RecyclerBlockEntity() {
		super(TRBlockEntities.RECYCLER, "Recycler", TechRebornConfig.recyclerMaxInput, TechRebornConfig.recyclerMaxEnergy, TRContent.Machine.RECYCLER.block, 2);
		final int[] inputs = new int[]{0};
		final int[] outputs = new int[]{1};
		this.inventory = new RebornInventory<>(3, "RecyclerBlockEntity", 64, this);
		this.crafter = new RecyclerRecipeCrafter(this, this.inventory, inputs, outputs);
	}

	public static boolean canRecycle(ItemStack stack) {
		Item item = stack.getItem();
		if ((item instanceof IUpgrade)) {
			return false;
		}
		return !TechRebornConfig.recyclerBlackList.contains(Registry.ITEM.getId(item).toString());
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("recycler").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).slot(0, 55, 45, RecyclerBlockEntity::canRecycle)
				.outputSlot(1, 101, 45).energySlot(2, 8, 72).syncEnergyValue()
				.syncCrafterValue().addInventory().create(this, syncID);
	}
}
