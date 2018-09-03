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
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItemCapabilityProvider;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.ItemUtils;
import techreborn.api.Reference;
import techreborn.config.ConfigTechReborn;
import techreborn.init.TRItems;
import techreborn.items.armor.ItemTRArmour;
import techreborn.lib.ModInfo;
import techreborn.utils.TechRebornCreativeTab;

import javax.annotation.Nullable;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class ItemCloakingDevice extends ItemTRArmour implements IEnergyItemInfo {

	@ConfigRegistry(config = "items", category = "cloacking_device", key = "ClockingDeviceEnergyUsage", comment = "Cloacking device energy usesage (Value in FE)")
	public static int usage = 10;

	public static int maxCharge = ConfigTechReborn.CloakingDeviceCharge;
	public static int transferLimit = 10_000;
	public static boolean isActive;

	// 40M FE capacity with 10k FE\t charge rate
	public ItemCloakingDevice() {
		super(Reference.CLOAKING_ARMOR, EntityEquipmentSlot.CHEST);
		setTranslationKey("techreborn.cloakingdevice");
		setMaxStackSize(1);
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	// Item
	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return "techreborn:" + "textures/models/cloaking.png";
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		IEnergyStorage capEnergy = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (capEnergy != null && capEnergy.getEnergyStored() >= usage) {
			capEnergy.extractEnergy(usage, false);
			player.setInvisible(true);
		} else {
			if (!player.isPotionActive(MobEffects.INVISIBILITY)) {
				player.setInvisible(false);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> itemList) {
		if (!isInCreativeTab(par2CreativeTabs)) {
			return;
		}
		ItemStack uncharged = new ItemStack(TRItems.CLOAKING_DEVICE);
//		ItemStack charged = new ItemStack(ModItems.CLOAKING_DEVICE);
//		ForgePowerItemManager capEnergy = (ForgePowerItemManager) charged.getCapability(CapabilityEnergy.ENERGY, null);
//		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());
		itemList.add(uncharged);
//		itemList.add(charged);
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
		return 0;
	}
}
