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

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.api.EnergyStorage;
import techreborn.TechReborn;
import techreborn.utils.InitUtils;

public class BatpackItem extends ArmorItem implements RcEnergyItem, Trinket {

	public final int maxCharge;
	public final RcEnergyTier tier;

	public BatpackItem(int maxCharge, ArmorMaterial material, RcEnergyTier tier) {
		super(material, EquipmentSlot.CHEST, new Settings().group(TechReborn.ITEMGROUP).maxCount(1).maxDamage(-1));
		this.maxCharge = maxCharge;
		this.tier = tier;
		registerTrinket();
	}

	//Trinket
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (entity instanceof ServerPlayerEntity) {

			EnergyStorage sourceStorage = ContainerItemContext.withInitial(stack).find(EnergyStorage.ITEM);

			ServerPlayerEntity player = (ServerPlayerEntity) entity;

			for (int i = 0; i < player.getInventory().size(); i++) {

				long amountBefore = sourceStorage.getAmount();

				long accepted = ItemUtils.transferPower(player, i, sourceStorage, tier.getMaxOutput(), (itemStack) -> true );

				long expectedAfter = amountBefore - accepted;

				if (amountBefore != expectedAfter) {
					((BatpackItem) stack.getItem()).setStoredEnergy(stack, expectedAfter);
				}
			}

		}
	}

	// Item
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (worldIn.isClient) {
			return;
		}

		// vanilla default for chest inventory slot
		if (itemSlot != 2) {
			return;
		}

		if (entityIn instanceof PlayerEntity) {
			ItemUtils.distributePowerToInventory((PlayerEntity) entityIn, stack, tier.getMaxOutput());
		}
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> itemList) {
		if (!isIn(group)) {
			return;
		}
		InitUtils.initPoweredItems(this, itemList);
	}

	// EnergyHolder
	@Override
	public long getEnergyCapacity() {
		return maxCharge;
	}

	@Override
	public RcEnergyTier getTier() {
		return tier;
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		return ItemUtils.getColorForDurabilityBar(stack);
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return ItemUtils.getPowerForDurabilityBar(stack);
	}

	private void registerTrinket() {
		// Ensures that trinkets are only registered if the mod is installed
		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			TrinketsApi.registerTrinket(this, this);
		}
	}
}
