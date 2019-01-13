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

package techreborn.items.armor;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItemContainerProvider;
import reborncore.common.powerSystem.forge.ForgePowerItemManager;
import reborncore.common.util.ItemUtils;
import techreborn.config.ConfigTechReborn;
import techreborn.init.TRContent;

import javax.annotation.Nullable;

public class ItemLithiumIonBatpack extends ItemArmor implements IEnergyItemInfo {

	// 8M FE maxCharge and 2k FE\t charge rate. Fully charged in 3 mins.
	public static final int maxCharge = ConfigTechReborn.LithiumBatpackCharge;
	public int transferLimit = 2_000;

	public ItemLithiumIonBatpack() {
		super(ItemArmor.ArmorMaterial.DIAMOND, 7, EntityEquipmentSlot.CHEST);
		setMaxStackSize(1);
	}

	public static void distributePowerToInventory(World world, EntityPlayer player, ItemStack itemStack, int maxSend) {
		if (world.isRemote) {
			return;
		}

		ForgePowerItemManager capEnergy = new ForgePowerItemManager(itemStack);

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if (!player.inventory.getStackInSlot(i).isEmpty()) {
				ExternalPowerSystems.chargeItem(capEnergy, player.inventory.getStackInSlot(i));
			}
		}
	}

	// Item
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		distributePowerToInventory(world, player, itemStack, (int) transferLimit);
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
	public ICapabilityProvider initCapabilities(ItemStack stack,
	                                            @Nullable
		                                            NBTTagCompound nbt) {
		return new PoweredItemContainerProvider(stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return "techreborn:" + "textures/models/lithiumbatpack.png";
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> itemList) {
		if (!isInCreativeTab(par2CreativeTabs)) {
			return;
		}
		ItemStack uncharged = new ItemStack(TRContent.LITHIUM_ION_BATTERY);
		ItemStack charged = new ItemStack(TRContent.LITHIUM_ION_BATTERY);
		ForgePowerItemManager capEnergy = new ForgePowerItemManager(charged);
		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());
		itemList.add(uncharged);
		//	itemList.add(charged);
	}

	// IEnergyItemInfo
	@Override
	public int getCapacity() {
		return maxCharge;
	}

	@Override
	public int getMaxInput() {
		return transferLimit;
	}

	@Override
	public int getMaxOutput() {
		return transferLimit;
	}
}
