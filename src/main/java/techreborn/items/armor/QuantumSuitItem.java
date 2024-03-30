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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.ladysnake.pal.AbilitySource;
import io.github.ladysnake.pal.Pal;
import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.items.ArmorBlockEntityTicker;
import reborncore.api.items.ArmorRemoveHandler;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.TechReborn;
import techreborn.config.TechRebornConfig;

import java.util.List;

public class QuantumSuitItem extends TREnergyArmourItem implements ArmorBlockEntityTicker, ArmorRemoveHandler {
	private static final AbilitySource QUANTUM_CHESTPLATE_FLIGHT = Pal.getAbilitySource(new Identifier(TechReborn.MOD_ID, "quantum_chestplate_flight"), AbilitySource.CONSUMABLE);

	public QuantumSuitItem(ArmorMaterial material, Type slot) {
		super(material, slot, TechRebornConfig.quantumSuitCapacity, RcEnergyTier.INSANE);
	}

	// TREnergyArmourItem
	@Override
	public long getEnergyMaxOutput(ItemStack stack) { return 0; }

	// ArmorItem
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return HashMultimap.create();
	}

	// FabricItem
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot equipmentSlot) {
		var attributes = ArrayListMultimap.create(super.getAttributeModifiers(stack, getSlotType()));

		attributes.removeAll(EntityAttributes.GENERIC_MOVEMENT_SPEED);

		if (this.getSlotType() == EquipmentSlot.LEGS && equipmentSlot == EquipmentSlot.LEGS && stack.getOrCreateNbt().getBoolean("isActive") && TechRebornConfig.quantumSuitEnableSprint) {
			if (getStoredEnergy(stack) > TechRebornConfig.quantumSuitSprintingCost) {
				attributes.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(MODIFIERS[equipmentSlot.getEntitySlotId()], "Movement Speed", 0.15, EntityAttributeModifier.Operation.ADDITION));
			}
		}

		if (equipmentSlot == this.getSlotType() && getStoredEnergy(stack) > 0) {
			attributes.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(MODIFIERS[getSlotType().getEntitySlotId()], "Armor modifier", 20, EntityAttributeModifier.Operation.ADDITION));
			attributes.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(MODIFIERS[getSlotType().getEntitySlotId()], "Knockback modifier", 2, EntityAttributeModifier.Operation.ADDITION));
		}

		return ImmutableMultimap.copyOf(attributes);
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
				if (stack.getOrCreateNbt().getBoolean("isActive") && tryUseEnergy(stack, TechRebornConfig.suitNightVisionCost)) {
					playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 220, 1, false, false));
				} else {
					playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
				}
			}
			case CHEST -> {
				if (TechRebornConfig.quantumSuitEnableFlight && !playerEntity.getWorld().isClient) {
					if (getStoredEnergy(stack) > TechRebornConfig.quantumSuitFlyingCost) {
						if (!QUANTUM_CHESTPLATE_FLIGHT.grants(playerEntity, VanillaAbilities.ALLOW_FLYING))
							QUANTUM_CHESTPLATE_FLIGHT.grantTo(playerEntity, VanillaAbilities.ALLOW_FLYING);

						if (playerEntity.getAbilities().flying && QUANTUM_CHESTPLATE_FLIGHT.isActivelyGranting(playerEntity, VanillaAbilities.ALLOW_FLYING)) {
							tryUseEnergy(stack, TechRebornConfig.quantumSuitFlyingCost);
						}
						playerEntity.setOnGround(true);
					} else {
						if (QUANTUM_CHESTPLATE_FLIGHT.grants(playerEntity, VanillaAbilities.ALLOW_FLYING))
							QUANTUM_CHESTPLATE_FLIGHT.revokeFrom(playerEntity, VanillaAbilities.ALLOW_FLYING);
					}
				}
				if (playerEntity.isOnFire() && tryUseEnergy(stack, TechRebornConfig.fireExtinguishCost)) {
					playerEntity.extinguish();
				}
			}
			case LEGS -> {
				if (playerEntity.isSprinting() && stack.getOrCreateNbt().getBoolean("isActive") && TechRebornConfig.quantumSuitEnableSprint) {
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
			if (QUANTUM_CHESTPLATE_FLIGHT.grants(playerEntity, VanillaAbilities.ALLOW_FLYING))
				QUANTUM_CHESTPLATE_FLIGHT.revokeFrom(playerEntity, VanillaAbilities.ALLOW_FLYING);
		} else if (this.getSlotType() == EquipmentSlot.HEAD) {
			playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (this.getSlotType() == EquipmentSlot.HEAD) {
			ItemUtils.buildActiveTooltip(stack, tooltip);
		}

		// Will only add Inactive/Active tooltip if sprint is enabled
		if (this.getSlotType() == EquipmentSlot.LEGS && TechRebornConfig.quantumSuitEnableSprint) {
			ItemUtils.buildActiveTooltip(stack, tooltip);
		}
	}
}
