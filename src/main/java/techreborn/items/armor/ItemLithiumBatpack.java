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
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItemContainerProvider;
import reborncore.common.powerSystem.forge.ForgePowerItemManager;
import reborncore.common.util.ItemUtils;
import techreborn.utils.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;

import javax.annotation.Nullable;

public class ItemLithiumBatpack extends ItemArmor implements IEnergyItemInfo {

	public static final int maxCharge = ConfigTechReborn.LithiumBatpackCharge;
	public double transferLimit = 10_000;

	public ItemLithiumBatpack() {
		super(ItemArmor.ArmorMaterial.DIAMOND, 7, EntityEquipmentSlot.CHEST);
		setMaxStackSize(1);
		setUnlocalizedName("techreborn.lithiumbatpack");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	public static void distributePowerToInventory(World world, EntityPlayer player, ItemStack itemStack, int maxSend) {
		if (world.isRemote) {
			return;
		}

		// TODO: Find out how to become compatible with IC2 items
		// IC2 handles battery pack charging in a very peculiar way. Battery packs do not actually distribute power on their own,
		// rather items explicitly request charging from armor slots. The default ElectricItemManager implementation will only
		// draw power from an IElectricItem, so items utilizing ISpecialElectricItem or IBackupElectricItemManager will not work.
		// This means that the only way for battery armor to be 100% compatible with IC2 is to be completely managed by the
		// ElectricItemManager. This will obviously not work. Instead, we should automatically distribute power to IC2 electric items.
		// However, this brings up a somewhat unrelated problem: TechReborn items do not know how to explicitly pull power from IC2
		// battery packs. Perhaps we need to add a function to ExternalPowerManager to hint that an ItemStack has discharged,
		// which will appropriately ask IC2 battery packs for energy.

		IEnergyStorage capEnergy = new ForgePowerItemManager(itemStack);

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if (!player.inventory.getStackInSlot(i).isEmpty()) {
				ItemStack item = player.inventory.getStackInSlot(i);
				if (!item.hasCapability(CapabilityEnergy.ENERGY, null)) {
					continue;
				}
				IEnergyStorage itemPower = item.getCapability(CapabilityEnergy.ENERGY, null);
				capEnergy.extractEnergy(itemPower.receiveEnergy(Math.min(capEnergy.getEnergyStored(), maxSend), false), false);
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
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new PoweredItemContainerProvider(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return "techreborn:" + "textures/models/lithiumbatpack.png";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> itemList) {
		if (!isInCreativeTab(par2CreativeTabs)) {
			return;
		}
		ItemStack uncharged = new ItemStack(ModItems.LITHIUM_BATTERY_PACK);
		ItemStack charged = new ItemStack(ModItems.LITHIUM_BATTERY_PACK);
		ForgePowerItemManager capEnergy = new ForgePowerItemManager(charged);
		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());
		itemList.add(uncharged);
		itemList.add(charged);
	}

	// IEnergyItemInfo
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
}
