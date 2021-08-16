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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import reborncore.api.items.ArmorBlockEntityTicker;
import reborncore.api.items.ArmorRemoveHandler;
import reborncore.api.items.ItemStackModifiers;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.TechReborn;
import techreborn.config.TechRebornConfig;
import techreborn.utils.InitUtils;

public class QuantumSuitItem extends TRArmourItem implements ItemStackModifiers, ArmorBlockEntityTicker, ArmorRemoveHandler, RcEnergyItem {

	public final long flyCost = TechRebornConfig.quantumSuitFlyingCost;
	public final long swimCost = TechRebornConfig.quantumSuitSwimmingCost;
	public final long breathingCost = TechRebornConfig.quantumSuitBreathingCost;
	public final long sprintingCost = TechRebornConfig.quantumSuitSprintingCost;
	public final long fireExtinguishCost = TechRebornConfig.fireExtinguishCost;

	public final boolean enableSprint = TechRebornConfig.quantumSuitEnableSprint;
	public final boolean enableFlight = TechRebornConfig.quantumSuitEnableFlight;


	public QuantumSuitItem(ArmorMaterial material, EquipmentSlot slot) {
		super(material, slot, new Item.Settings().group(TechReborn.ITEMGROUP).maxDamage(-1).maxCount(1));
	}

	@Override
	public void getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack, Multimap<EntityAttribute, EntityAttributeModifier> attributes) {
		attributes.removeAll(EntityAttributes.GENERIC_MOVEMENT_SPEED);

		if (this.slot == EquipmentSlot.LEGS && equipmentSlot == EquipmentSlot.LEGS && enableSprint) {
			if (getStoredEnergy(stack) > sprintingCost) {
				attributes.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(MODIFIERS[equipmentSlot.getEntitySlotId()], "Movement Speed", 0.15, EntityAttributeModifier.Operation.ADDITION));
			}
		}

		if (equipmentSlot == this.slot && getStoredEnergy(stack) > 0) {
			attributes.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(MODIFIERS[slot.getEntitySlotId()], "Armor modifier", 20, EntityAttributeModifier.Operation.ADDITION));
			attributes.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(MODIFIERS[slot.getEntitySlotId()], "Knockback modifier", 2, EntityAttributeModifier.Operation.ADDITION));
		}
	}

	@Override
	public void tickArmor(ItemStack stack, PlayerEntity playerEntity) {
		switch (this.slot) {
			case HEAD:
				if (playerEntity.isSubmergedInWater()) {
					if (tryUseEnergy(stack, breathingCost)) {
						playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 5, 1));
					}
				}
				break;
			case CHEST:
				if (enableFlight){
					if (getStoredEnergy(stack) > flyCost && !TechReborn.elytraPredicate.test(playerEntity)) {
						playerEntity.getAbilities().allowFlying = true;
						if (playerEntity.getAbilities().flying) {
							tryUseEnergy(stack, flyCost);
						}
						playerEntity.setOnGround(true);
					} else {
						playerEntity.getAbilities().allowFlying = false;
						playerEntity.getAbilities().flying = false;
					}
				}
				if (playerEntity.isOnFire() && getStoredEnergy(stack) > fireExtinguishCost) {
					playerEntity.extinguish();
				}
				break;
			case LEGS:
				if (playerEntity.isSprinting() && enableSprint) {
					tryUseEnergy(stack, sprintingCost);
				}
				break;
			case FEET:
				if (playerEntity.isSwimming()) {
					if (tryUseEnergy(stack, swimCost)) {
						playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 5, 1));
					}
				}
				break;
			default:

		}
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return HashMultimap.create();
	}

	@Override
	public void onRemoved(PlayerEntity playerEntity) {
		if (this.slot == EquipmentSlot.CHEST && enableFlight) {
			if (!playerEntity.isCreative() && !playerEntity.isSpectator()) {
				playerEntity.getAbilities().allowFlying = false;
				playerEntity.getAbilities().flying = false;
			}
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
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public int getDurabilityColor(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}

	@Override
	public boolean canRepair(ItemStack itemStack_1, ItemStack itemStack_2) {
		return false;
	}

	@Override
	public long getEnergyCapacity() {
		return TechRebornConfig.quantumSuitCapacity;
	}

	@Override
	public RcEnergyTier getTier() {
		return RcEnergyTier.EXTREME;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> itemList) {
		if (!isIn(group)) {
			return;
		}
		InitUtils.initPoweredItems(this, itemList);
	}
}
