/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.client.container.builder.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import reborncore.api.tile.IUpgrade;
import reborncore.api.tile.IUpgradeable;
import reborncore.common.tile.TileBase;
import reborncore.common.util.Inventory;
import techreborn.client.container.IRightClickHandler;
import techreborn.client.container.builder.BuiltContainer;

import java.util.function.Predicate;

public class UpgradeSlot extends Slot implements IRightClickHandler {

	private Predicate<ItemStack> filter;

	public UpgradeSlot(final IInventory inventory, final int index, final int xPosition, final int yPosition) {
		super(inventory, index, xPosition, yPosition);
	}


	@Override
	public boolean isItemValid(final ItemStack stack) {
		return stack.getItem() instanceof IUpgrade;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public boolean handleRightClick(int slotID, EntityPlayer player, BuiltContainer container) {
		if(inventory instanceof Inventory){
			Inventory inv = (Inventory) inventory;
			TileEntity tileEntity = inv.getTileBase();
			if(tileEntity instanceof IUpgradeable){
				IUpgradeable upgradeable = (IUpgradeable) tileEntity;
				if(upgradeable.canBeUpgraded()){
					ItemStack stack = upgradeable.getUpgradeInvetory().getStackInSlot(slotID);
					if(!stack.isEmpty() && stack.getItem() instanceof IUpgrade){
						if(player.world.isRemote){
							((IUpgrade) stack.getItem()).handleRightClick(tileEntity, stack, container, slotID);
						}
					}
				}
			}
		}

		return true;
	}
}
