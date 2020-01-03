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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.world.World;
import reborncore.api.items.ArmorRemoveHandler;
import reborncore.api.items.ArmorTickable;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyTier;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRArmorMaterial;
import techreborn.init.TRContent;
import techreborn.utils.InitUtils;

public class ItemCloakingDevice extends ItemTRArmour implements EnergyHolder, ArmorTickable, ArmorRemoveHandler {

	public static int maxCharge = TechRebornConfig.cloakingDeviceCharge;
	public static int usage = TechRebornConfig.cloackingDeviceUsage;
	public static int transferLimit = 10_000;
	public static boolean isActive;

	// 40M FE capacity with 10k FE\t charge rate
	public ItemCloakingDevice() {
		super(TRArmorMaterial.CLOAKING, EquipmentSlot.CHEST);
	}

	// ItemTRArmour
	@Override
	public double getDurability(ItemStack stack) {
		return 1 - ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean showDurability(ItemStack stack) {
		return true;
	}

	@Override
	public int getDurabilityColor(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}

	// Item
	@Environment(EnvType.CLIENT)
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> itemList) {
		if (!isIn(group)) {
			return;
		}
		InitUtils.initPoweredItems(TRContent.CLOAKING_DEVICE, itemList);
	}

	// EnergyHolder
	@Override
	public double getMaxStoredPower() {
		return maxCharge;
	}

	@Override
	public EnergyTier getTier() {
		return EnergyTier.HIGH;
	}

	@Override
	public double getMaxInput(EnergySide side) {
		return transferLimit;
	}

	@Override
	public double getMaxOutput(EnergySide side) {
		return 0;
	}

	// ArmorTickable
	@Override
	public void tickArmor(ItemStack stack, PlayerEntity playerEntity) {
		if (Energy.of(stack).use(usage)) {
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
