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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.world.World;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.ItemDurabilityExtensions;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergyTier;
import techreborn.TechReborn;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRArmorMaterial;
import techreborn.init.TRContent;
import techreborn.utils.InitUtils;

public class ItemLithiumIonBatpack extends ArmorItem implements EnergyHolder, ItemDurabilityExtensions {

	// 8M FE maxCharge and 2k FE\t charge rate. Fully charged in 3 mins.
	public static final int maxCharge = TechRebornConfig.lithiumBatpackCharge;
	public int transferLimit = 2_000;

	public ItemLithiumIonBatpack() {
		super(TRArmorMaterial.LITHIUMBATPACK, EquipmentSlot.CHEST, new Item.Settings().group(TechReborn.ITEMGROUP).maxCount(1));
	}

	public static void distributePowerToInventory(World world, PlayerEntity player, ItemStack itemStack, int maxSend) {
		if (world.isClient) {
			return;
		}

		if(!Energy.valid(itemStack)){
			return;
		}

		for (int i = 0; i < player.inventory.getInvSize(); i++) {
			if (Energy.valid(player.inventory.getInvStack(i))) {
				Energy.of(itemStack)
					.into(
						Energy
							.of(player.inventory.getInvStack(i))
					)
					.move();
			}
		}
	}

	// Item
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof PlayerEntity) {
			distributePowerToInventory(worldIn, (PlayerEntity) entityIn, stack, (int) transferLimit);
		}
	}

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

	@Environment(EnvType.CLIENT)
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> itemList) {
		if (!isIn(group)) {
			return;
		}
		InitUtils.initPoweredItems(TRContent.LITHIUM_ION_BATPACK, itemList);
	}
	
	// IEnergyItemInfo
	@Override
	public double getMaxStoredPower() {
		return maxCharge;
	}

	@Override
	public EnergyTier getTier() {
		return EnergyTier.HIGH;
	}

}
