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

package techreborn.items.armor;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PoweredItem;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;

public class ItemLithiumBatpack extends ItemArmor implements IEnergyItemInfo {

	public static final int maxCharge = ConfigTechReborn.LithiumBatpackCharge;
	public static final int tier = ConfigTechReborn.LithiumBatpackTier;
	public double transferLimit = 10000;

	public ItemLithiumBatpack() {
		super(ItemArmor.ArmorMaterial.DIAMOND, 7, EntityEquipmentSlot.CHEST);
		setMaxStackSize(1);
		setUnlocalizedName("techreborn.lithiumbatpack");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
				ItemStack item = player.inventory.getStackInSlot(i);
				if (item.getItem() instanceof IEnergyItemInfo) {
					IEnergyItemInfo energyItemInfo = (IEnergyItemInfo) item.getItem();
					if (energyItemInfo.getMaxPower(item) != PoweredItem.getEnergy(item)) {
						if (PoweredItem.canUseEnergy(energyItemInfo.getMaxPower(item), itemStack)) {
							PoweredItem.useEnergy(energyItemInfo.getMaxTransfer(item), itemStack);
							PoweredItem.setEnergy(PoweredItem.getEnergy(item) + energyItemInfo.getMaxTransfer(item), item);
						}
					}
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return "techreborn:" + "textures/models/lithiumbatpack.png";
	}

	@Override
	public double getMaxPower(ItemStack stack) {
		return maxCharge;
	}

	@Override
	public boolean canAcceptEnergy(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(ItemStack stack) {
		return true;
	}

	@Override
	public double getMaxTransfer(ItemStack stack) {
		return transferLimit;
	}

	@Override
	public int getStackTier(ItemStack stack) {
		return tier;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(
		CreativeTabs par2CreativeTabs, NonNullList itemList) {
		if(!func_194125_a(par2CreativeTabs)){
			return;
		}
		ItemStack uncharged = new ItemStack(ModItems.LITHIUM_BATTERY_PACK);
		ItemStack charged = new ItemStack(ModItems.LITHIUM_BATTERY_PACK);
		PoweredItem.setEnergy(getMaxPower(charged), charged);

		itemList.add(uncharged);
		itemList.add(charged);
	}

}
