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

import com.google.common.collect.*;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.items.ArmorBlockEntityTicker;
import reborncore.api.items.ArmorRemoveHandler;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.config.TechRebornConfig;

import java.util.ArrayList;
import java.util.List;

public class QuantumSuitItem extends TREnergyArmourItem implements ArmorBlockEntityTicker, ArmorRemoveHandler {
	private static final Multimap<EntityAttribute, EntityAttributeModifier> FULL_SUIT = new AttributeModifierBuilder().armor(10).toughness(8).knockback(6).build();
	private final Multimap<EntityAttribute, EntityAttributeModifier> noPowerAttributes;
	private final Multimap<EntityAttribute, EntityAttributeModifier> hasPowerAttributes;
	private final Multimap<EntityAttribute, EntityAttributeModifier> fullSuitAttributes;
	private final Multimap<EntityAttribute, EntityAttributeModifier> hasPowerSprintAttributes;
	private final Multimap<EntityAttribute, EntityAttributeModifier> fullSuitSprintAttributes;

	public QuantumSuitItem(ArmorMaterial material, Type slot) {
		super(material, slot, TechRebornConfig.quantumSuitCapacity, RcEnergyTier.INSANE);
		switch (slot) {
			case HELMET, BOOTS:
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(3).toughness(2).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(3).toughness(3).knockback(1).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(5).toughness(5).knockback(2).build();
				hasPowerSprintAttributes = fullSuitSprintAttributes = null;
				break;
			case CHESTPLATE:
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(6).toughness(2).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(6).toughness(3).knockback(1).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(10).toughness(5).knockback(3).build();
				hasPowerSprintAttributes = fullSuitSprintAttributes = null;
				break;
			case LEGGINGS: {
				EntityAttributeModifier modifier = new EntityAttributeModifier(
					TRArmourItem.MODIFIERS[1],
					"Movement Speed",
					0.15,
					EntityAttributeModifier.Operation.ADDITION
				);
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(8).toughness(2).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(8).toughness(3).knockback(1).build();
				hasPowerSprintAttributes = ImmutableListMultimap.<EntityAttribute, EntityAttributeModifier>builder()
					.putAll(hasPowerAttributes).put(EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(10).toughness(5).knockback(3).build();
				fullSuitSprintAttributes = ImmutableListMultimap.<EntityAttribute, EntityAttributeModifier>builder()
					.putAll(fullSuitAttributes).put(EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier).build();
				break;
			}
			default:
				throw new IllegalArgumentException("Invalid slot type");
		}
	}

	// TREnergyArmourItem
	@Override
	public long getEnergyMaxOutput() { return 0; }

	// ArmorItem
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return HashMultimap.create();
	}

	// FabricItem
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot equipmentSlot) {
		if (equipmentSlot != this.getSlotType()) {
			return ImmutableMultimap.of();
		}
		long energy = getStoredEnergy(stack);
		if (energy > 0) {
			NbtCompound nbt = stack.getOrCreateNbt();
			if (equipmentSlot == EquipmentSlot.LEGS && TechRebornConfig.quantumSuitEnableSprint && nbt.getBoolean("isActive") && energy >= TechRebornConfig.quantumSuitSprintingCost) {
				if (nbt.contains("HideFlags")) {
					return fullSuitSprintAttributes;
				} else {
					return hasPowerSprintAttributes;
				}
			} else if (nbt.contains("HideFlags")) {
				return fullSuitAttributes;
			} else {
				return hasPowerAttributes;
			}
		} else {
			return noPowerAttributes;
		}
	}

	// ArmorBlockEntityTicker
	@Override
	public void tickArmor(ItemStack stack, boolean hasFullSuit, PlayerEntity playerEntity) {
		final NbtCompound nbt = stack.getOrCreateNbt();
		switch (this.getSlotType()) {
			case HEAD -> {
				// Water Breathing
				if (playerEntity.isSubmergedInWater() && tryUseEnergy(stack, TechRebornConfig.quantumSuitBreathingCost)) {
					playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 5, 1));
				}

				// Night Vision
				if (nbt.getBoolean("isActive") && tryUseEnergy(stack, TechRebornConfig.suitNightVisionCost)) {
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
				if (playerEntity.isSprinting() && nbt.getBoolean("isActive") && TechRebornConfig.quantumSuitEnableSprint) {
					tryUseEnergy(stack, TechRebornConfig.quantumSuitSprintingCost);
				}
			}
			case FEET -> {
				if (playerEntity.isSwimming() && tryUseEnergy(stack, TechRebornConfig.quantumSuitSwimmingCost)) {
					playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 5, 1, true, false));
				}
			}
		}
		if (nbt.contains("HideFlags")) {
			if (!hasFullSuit) {
				nbt.remove("HideFlags");
			}
		} else if (hasFullSuit) {
			nbt.putInt("HideFlags", ItemStack.TooltipSection.MODIFIERS.getFlag());
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
		ItemStack stack = playerEntity.playerScreenHandler.getCursorStack();
		if (stack.getItem() instanceof QuantumSuitItem) {
			NbtCompound nbt = stack.getOrCreateNbt();
			nbt.remove("HideFlags");
			nbt.remove("isTicking");
		} else {
			playerEntity.getInventory().main.forEach(itemStack -> {
				if (itemStack.getItem() instanceof QuantumSuitItem) {
					NbtCompound nbt = itemStack.getOrCreateNbt();
					nbt.remove("HideFlags");
					nbt.remove("isTicking");
				}
			});
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

	public void appendArmorTooltip(ItemStack stack, List<Text> tooltip, boolean shift) {
		List<Text> buffer = new ArrayList<>();
		NbtCompound nbt = stack.getOrCreateNbt();
		if (getStoredEnergy(stack) > 0) {
			if (nbt.contains("HideFlags")) {
				buffer.add(Text.empty());
				buffer.add(AttributeModifierBuilder.text(getSlotType()).formatted(Formatting.GRAY));
				if (shift) {
					AttributeModifierBuilder.appendText(buffer, fullSuitAttributes, Formatting.BLUE);
				} else {
					AttributeModifierBuilder.appendText(
						buffer,
						this.getSlotType() == EquipmentSlot.LEGS && TechRebornConfig.quantumSuitEnableSprint && nbt.getBoolean("isActive") ?
							hasPowerSprintAttributes : hasPowerAttributes,
						Formatting.BLUE
					);
					buffer.add(Text.empty());
					buffer.add(Text.translatable("item.modifiers.full_suit").formatted(Formatting.YELLOW));
					AttributeModifierBuilder.appendText(buffer, FULL_SUIT, Formatting.YELLOW);
				}
			} else if (shift) {
				buffer.add(Text.translatable("item.modifiers.all_equipment").formatted(Formatting.GRAY));
				AttributeModifierBuilder.appendText(buffer, FULL_SUIT, Formatting.BLUE);
			}
		} else {
			if (!shift && nbt.contains("isTicking")) {
				return;
			}
			buffer.add(Text.translatable("item.modifiers.power").formatted(Formatting.GRAY));
			AttributeModifierBuilder.appendDiffText(buffer, noPowerAttributes, hasPowerAttributes, Formatting.BLUE);
			buffer.add(Text.translatable("item.modifiers.all_equipment").formatted(Formatting.GRAY));
			AttributeModifierBuilder.appendText(buffer, FULL_SUIT, Formatting.BLUE);
		}
		AttributeModifierBuilder.appendEnd(tooltip, buffer);
	}
}
