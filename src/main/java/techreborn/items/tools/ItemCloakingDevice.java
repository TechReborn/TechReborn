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

package techreborn.items.tools;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;
import techreborn.items.ItemTR;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class ItemCloakingDevice extends ItemTR implements IEnergyItemInfo {

	@ConfigRegistry(config = "items", category = "cloacking_device", key = "ClockingDeviceEnergyUsage", comment = "Cloacking device energy usesage (Value in EU)")
	public static int usage = 10;

	public static int MaxCharge = ConfigTechReborn.CloakingDeviceCharge;
	public static int Limit = 100;
	public static boolean isActive;
	private int armorType = 1;

	public ItemCloakingDevice() {
		setUnlocalizedName("techreborn.cloakingdevice");
		setMaxStackSize(1);
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (PoweredItem.canUseEnergy(usage, itemStack)) {
			PoweredItem.useEnergy(usage, itemStack);
			player.setInvisible(true);
		} else {
			if (!player.isPotionActive(MobEffects.INVISIBILITY)) {
				player.setInvisible(false);
			}
		}
	}

	@Override
	public double getMaxPower(ItemStack stack) {
		return MaxCharge;
	}

	@Override
	public boolean canAcceptEnergy(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(ItemStack itemStack) {
		return false;
	}

	@Override
	public double getMaxTransfer(ItemStack stack) {
		return Limit;
	}

	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		ItemStack itemstack1 = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

		if (itemstack1 == ItemStack.EMPTY) {
			player.setItemStackToSlot(EntityEquipmentSlot.CHEST, itemStack.copy());
			itemStack.setCount(0);
		}

		return itemStack;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(
		CreativeTabs par2CreativeTabs, NonNullList itemList) {
		if (!isInCreativeTab(par2CreativeTabs)) {
			return;
		}
		ItemStack uncharged = new ItemStack(ModItems.CLOAKING_DEVICE);
		ItemStack charged = new ItemStack(ModItems.CLOAKING_DEVICE);
		PoweredItem.setEnergy(getMaxPower(charged), charged);

		itemList.add(uncharged);
		itemList.add(charged);
	}
}
