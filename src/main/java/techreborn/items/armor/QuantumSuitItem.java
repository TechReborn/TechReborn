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

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import reborncore.api.items.ArmorBlockEntityTicker;
import reborncore.api.items.ArmorRemoveHandler;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.config.TechRebornConfig;
import techreborn.utils.TRItemUtils;

import java.util.List;

public class QuantumSuitItem extends TREnergyArmourItem implements ArmorBlockEntityTicker, ArmorRemoveHandler {
	public QuantumSuitItem(RegistryEntry<ArmorMaterial> material, Type slot) {
		super(material, slot, TechRebornConfig.quantumSuitCapacity, RcEnergyTier.INSANE);
	}

	// TREnergyArmourItem
	@Override
	public long getEnergyMaxOutput(ItemStack stack) { return 0; }


	// FabricItem
	@Override
	public AttributeModifiersComponent getAttributeModifiers(ItemStack stack) {
		AttributeModifiersComponent attributes = super.getAttributeModifiers(stack);

		if (TRItemUtils.isActive(stack) && TechRebornConfig.quantumSuitEnableSprint) {
			if (getStoredEnergy(stack) > TechRebornConfig.quantumSuitSprintingCost) {
				attributes = attributes.with(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier("Movement Speed", 0.15, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.LEGS);
			}
		}

		AttributeModifierSlot modifierSlot = AttributeModifierSlot.forEquipmentSlot(this.getSlotType());

		if (getStoredEnergy(stack) > 0) {
			attributes = attributes.with(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier("Armor modifier", 20, EntityAttributeModifier.Operation.ADD_VALUE), modifierSlot);
			attributes = attributes.with(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier("Knockback modifier", 2, EntityAttributeModifier.Operation.ADD_VALUE), modifierSlot);
		}

		return attributes;
	}

	// ArmorBlockEntityTicker
	@Override
	public void tickArmor(ItemStack stack, PlayerEntity playerEntity) {
		switch (this.getSlotType()) {
			case HEAD -> {
				// Water Breathing
				if (playerEntity.isSubmergedInWater() && tryUseEnergy(stack, TechRebornConfig.quantumSuitBreathingCost)) {
					playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 5, 1));
				}

				// Night Vision
				if (TRItemUtils.isActive(stack) && tryUseEnergy(stack, TechRebornConfig.suitNightVisionCost)) {
					playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 220, 1, false, false));
				} else {
					playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
				}
			}
			case CHEST -> {
				if (TechRebornConfig.quantumSuitEnableFlight) {
					if (getStoredEnergy(stack) > TechRebornConfig.quantumSuitFlyingCost) {
						playerEntity.getAbilities().allowFlying = true;
						playerEntity.sendAbilitiesUpdate();

						if (playerEntity.getAbilities().flying) {
							tryUseEnergy(stack, TechRebornConfig.quantumSuitFlyingCost);
						}
						playerEntity.setOnGround(true);
					} else {
						playerEntity.getAbilities().allowFlying = false;
						playerEntity.getAbilities().flying = false;
						playerEntity.sendAbilitiesUpdate();
					}
				}
				if (playerEntity.isOnFire() && tryUseEnergy(stack, TechRebornConfig.fireExtinguishCost)) {
					playerEntity.extinguish();
				}
			}
			case LEGS -> {
				if (playerEntity.isSprinting() && TRItemUtils.isActive(stack) && TechRebornConfig.quantumSuitEnableSprint) {
					tryUseEnergy(stack, TechRebornConfig.quantumSuitSprintingCost);
				}
			}
			case FEET -> {
				if (playerEntity.isSwimming() && tryUseEnergy(stack, TechRebornConfig.quantumSuitSwimmingCost)) {
					playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 5, 1, true, false));
				}
			}
		}
	}

	// ArmorRemoveHandler
	@Override
	public void onRemoved(PlayerEntity playerEntity) {
		if (this.getSlotType() == EquipmentSlot.CHEST && TechRebornConfig.quantumSuitEnableFlight) {
			if (!playerEntity.isCreative() && !playerEntity.isSpectator()) {
				playerEntity.getAbilities().allowFlying = false;
				playerEntity.getAbilities().flying = false;
				playerEntity.sendAbilitiesUpdate();
			}
		} else if (this.getSlotType() == EquipmentSlot.HEAD) {
			playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		if (this.getSlotType() == EquipmentSlot.HEAD) {
			TRItemUtils.buildActiveTooltip(stack, tooltip);
		}

		// Will only add Inactive/Active tooltip if sprint is enabled
		if (this.getSlotType() == EquipmentSlot.LEGS && TechRebornConfig.quantumSuitEnableSprint) {
			TRItemUtils.buildActiveTooltip(stack, tooltip);
		}
	}
}
