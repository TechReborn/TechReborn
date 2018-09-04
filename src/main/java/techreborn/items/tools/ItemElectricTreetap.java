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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItemCapabilityProvider;
import reborncore.common.util.ItemUtils;
import techreborn.init.TRItems;
import techreborn.items.ItemTR;
import javax.annotation.Nullable;

/**
 * Created by modmuss50 on 05/11/2016.
 */
public class ItemElectricTreetap extends ItemTR implements IEnergyItemInfo {

	public static final int maxCharge = 10_000;
	public int cost = 20;

	public ItemElectricTreetap() {
		setMaxStackSize(1);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1 - ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}

	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new PoweredItemCapabilityProvider(stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> itemList) {
		if (!isInCreativeTab(par2CreativeTabs)) {
			return;
		}
		ItemStack uncharged = new ItemStack(TRItems.ELECTRIC_TREE_TAP);
//		ItemStack charged = new ItemStack(ModItems.ELECTRIC_TREE_TAP);
//		ForgePowerItemManager capEnergy = (ForgePowerItemManager) charged.getCapability(CapabilityEnergy.ENERGY, null);
//		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());

		itemList.add(uncharged);
//		itemList.add(charged);
	}

	// IEnergyItemInfo
	@Override
	public int getCapacity() {
		return maxCharge;
	}

	@Override
	public int getMaxInput() {
		return 200;
	}

	@Override
	public int getMaxOutput() {
		return 0;
	}
}
