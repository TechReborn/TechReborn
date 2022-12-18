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

package techreborn.items.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import reborncore.api.items.ArmorBlockEntityTicker;
import reborncore.api.items.ArmorRemoveHandler;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRArmorMaterials;

public class CloakingDeviceItem extends TRArmourItem implements RcEnergyItem, ArmorBlockEntityTicker, ArmorRemoveHandler {

	public static int maxCharge = TechRebornConfig.cloakingDeviceCharge;
	public static int cost = TechRebornConfig.cloakingDeviceCost;
	public static boolean isActive;

	// 40M FE capacity with 10k FE\t charge rate
	public CloakingDeviceItem() {
		super(TRArmorMaterials.CLOAKING_DEVICE, EquipmentSlot.CHEST, new Item.Settings().maxDamage(-1).maxCount(1));
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return false;
	}

	// ItemTRArmour
	@Override
	public int getItemBarStep(ItemStack stack) {
		return ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		return ItemUtils.getColorForDurabilityBar(stack);
	}

	// EnergyHolder
	@Override
	public long getEnergyCapacity() {
		return maxCharge;
	}

	@Override
	public RcEnergyTier getTier() {
		return RcEnergyTier.INSANE;
	}

	@Override
	public long getEnergyMaxOutput() {
		return 0;
	}

	// ArmorTickable
	@Override
	public void tickArmor(ItemStack stack, PlayerEntity playerEntity) {
		if (tryUseEnergy(stack, cost)) {
			playerEntity.setInvisible(true);
		} else {
			if (playerEntity.isInvisible()) {
				playerEntity.setInvisible(false);
			}
		}
	}

	// ArmorRemoveHandler
	@Override
	public void onRemoved(PlayerEntity playerEntity) {
		if (playerEntity.isInvisible()) {
			playerEntity.setInvisible(false);
		}
	}
}
