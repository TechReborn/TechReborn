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

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItemContainerProvider;
import reborncore.common.powerSystem.forge.ForgePowerItemManager;
import reborncore.common.registration.RebornRegister;
import reborncore.common.util.ItemUtils;
import techreborn.TechReborn;
import techreborn.config.ConfigTechReborn;
import techreborn.init.TRArmorMaterial;
import techreborn.init.TRContent;

import javax.annotation.Nullable;

@RebornRegister(TechReborn.MOD_ID)
public class ItemCloakingDevice extends ItemTRArmour implements IEnergyItemInfo {

	public static int maxCharge = ConfigTechReborn.CloakingDeviceCharge;
	public static int usage = ConfigTechReborn.CloackingDeviceUsage;
	public static int transferLimit = 10_000;
	public static boolean isActive;

	// 40M FE capacity with 10k FE\t charge rate
	public ItemCloakingDevice() {
		super(TRArmorMaterial.CLOAKING, EntityEquipmentSlot.CHEST);
	}

	// Item
	@Override
	@OnlyIn(Dist.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return "techreborn:" + "textures/models/cloaking.png";
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityIn;
			IEnergyStorage capEnergy = new ForgePowerItemManager(stack);
			if (capEnergy != null && capEnergy.getEnergyStored() >= usage) {
				capEnergy.extractEnergy(usage, false);
				player.setInvisible(true);
			} else {
				if (!player.isPotionActive(MobEffects.INVISIBILITY)) {
					player.setInvisible(false);
				}
			}
		}
	}
	
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {

	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (!isInGroup(group)) {
			return;
		}
		ItemStack uncharged = new ItemStack(TRContent.CLOAKING_DEVICE);
		ItemStack charged = new ItemStack(TRContent.CLOAKING_DEVICE);
		ForgePowerItemManager capEnergy = new ForgePowerItemManager(charged);
		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());
		items.add(uncharged);
		items.add(charged);
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
		return new PoweredItemContainerProvider(stack);
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
