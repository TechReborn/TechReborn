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

package techreborn.items.tools;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.config.ConfigTechReborn;
import techreborn.init.TRItems;

public class ItemAdvancedJackhammer extends ItemJackhammer {

	// 4M FE max charge with 1k charge rate
	public ItemAdvancedJackhammer() {
		super(ToolMaterial.IRON, ConfigTechReborn.AdvancedJackhammerCharge);
		this.cost = 250;
		this.efficiency = 60F;
		this.transferLimit = 1000;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(
		CreativeTabs par2CreativeTabs, NonNullList<ItemStack> itemList) {
		if (!isInCreativeTab(par2CreativeTabs)) {
			return;
		}
		ItemStack stack = new ItemStack(TRItems.ADVANCED_JACKHAMMER);
//		ItemStack charged = stack.copy();
//		ForgePowerItemManager capEnergy = (ForgePowerItemManager) charged.getCapability(CapabilityEnergy.ENERGY, null);
//		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());

		itemList.add(stack);
//		itemList.add(charged);
	}
}
