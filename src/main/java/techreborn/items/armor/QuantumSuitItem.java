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

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import reborncore.api.items.ArmorBlockEntityTicker;
import reborncore.api.items.ArmorRemoveHandler;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.config.TechRebornConfig;
import techreborn.utils.TRItemUtils;

import java.util.List;

public class QuantumSuitItem extends TREnergyArmourItem implements ArmorBlockEntityTicker, ArmorRemoveHandler {
	private static final AttributeModifiersComponent FULL_SUIT = new AttributeModifierBuilder().armor(10).toughness(8).knockback(6).build();
	private final AttributeModifiersComponent noPowerAttributes;
	private final AttributeModifiersComponent hasPowerAttributes;
	private final AttributeModifiersComponent fullSuitAttributes;
	private final AttributeModifiersComponent hasPowerSprintAttributes;
	private final AttributeModifiersComponent fullSuitSprintAttributes;

	public QuantumSuitItem(ArmorMaterial material, EquipmentType slot, String name) {
		super(material, slot, TechRebornConfig.quantumSuitCapacity, RcEnergyTier.INSANE, name);
		switch (slot) {
			case HELMET, BOOTS:
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(3).toughness(2).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(3).toughness(2).knockback(1).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(5).toughness(5).knockback(2).build();
				hasPowerSprintAttributes = fullSuitSprintAttributes = null;
				break;
			case CHESTPLATE:
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(6).toughness(2).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(6).toughness(2).knockback(1).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(10).toughness(5).knockback(3).build();
				hasPowerSprintAttributes = fullSuitSprintAttributes = null;
				break;
			case LEGGINGS: {
				EntityAttributeModifier modifier = new EntityAttributeModifier(
					Identifier.of("techreborn", "quantum_movement_speed"),
					0.15,
					EntityAttributeModifier.Operation.ADD_VALUE
				);
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(8).toughness(2).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(8).toughness(2).knockback(1).build();
				hasPowerSprintAttributes = hasPowerAttributes.with(EntityAttributes.MOVEMENT_SPEED, modifier, AttributeModifierSlot.LEGS);
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(10).toughness(5).knockback(3).build();
				fullSuitSprintAttributes = fullSuitAttributes.with(EntityAttributes.MOVEMENT_SPEED, modifier, AttributeModifierSlot.LEGS);
				break;
			}
			default:
				throw new IllegalArgumentException("Invalid slot type");
		}
	}

	// TREnergyArmourItem
	@Override
	public long getEnergyMaxOutput(ItemStack stack) { return 0; }

	// ArmorBlockEntityTicker
	@Override
	public void tickArmor(ItemStack stack, boolean hasFullSuit, PlayerEntity playerEntity) {
		// mark tick
		if (!stack.contains(DataComponentTypes.CUSTOM_DATA)) {
			stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
		}

		switch (getSlotType()) {
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
				boolean sprint = TechRebornConfig.quantumSuitEnableSprint && TRItemUtils.isActive(stack);
				if (sprint && playerEntity.isSprinting()) {
					tryUseEnergy(stack, TechRebornConfig.quantumSuitSprintingCost);
				}
				applyModifier(stack, hasFullSuit, sprint);
				return;
			}
			case FEET -> {
				if (playerEntity.isSwimming() && tryUseEnergy(stack, TechRebornConfig.quantumSuitSwimmingCost)) {
					playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 5, 1, true, false));
				}
			}
		}
		applyModifier(stack, hasFullSuit, false);
	}

	private void applyModifier(ItemStack stack, AttributeModifiersComponent attributes, AttributeModifiersComponent target) {
		if (attributes != target) {
			stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, target);
		}
	}

	public void applyModifier(ItemStack stack, boolean hasFullSuit, boolean sprintEnable) {
		AttributeModifiersComponent attributes = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
		long energy = getStoredEnergy(stack);
		if (energy > 0) {
			if (sprintEnable && energy >= TechRebornConfig.quantumSuitSprintingCost) {
				if (hasFullSuit) {
					applyModifier(stack, attributes, fullSuitSprintAttributes);
				} else {
					applyModifier(stack, attributes, hasPowerSprintAttributes);
				}
			} else if (hasFullSuit) {
				applyModifier(stack, attributes, fullSuitAttributes);
			} else {
				applyModifier(stack, attributes, hasPowerAttributes);
			}
		} else {
			applyModifier(stack, attributes, noPowerAttributes);
		}
	}

	// ArmorRemoveHandler
	@Override
	public void onRemoved(PlayerEntity playerEntity) {
		EquipmentSlot slotType = this.getSlotType();
		if (slotType == EquipmentSlot.CHEST && TechRebornConfig.quantumSuitEnableFlight) {
			if (!playerEntity.isCreative() && !playerEntity.isSpectator()) {
				playerEntity.getAbilities().allowFlying = false;
				playerEntity.getAbilities().flying = false;
				playerEntity.sendAbilitiesUpdate();
			}
		} else if (slotType == EquipmentSlot.HEAD) {
			playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
		}
		ItemStack stack = playerEntity.playerScreenHandler.getCursorStack();
		if (stack.getItem() instanceof QuantumSuitItem quantumSuitItem) {
			quantumSuitItem.applyModifier(stack, false, false);
			stack.remove(DataComponentTypes.CUSTOM_DATA);
		} else {
			playerEntity.getInventory().main.forEach(itemStack -> {
				if (itemStack.getItem() instanceof QuantumSuitItem quantumSuitItem) {
					quantumSuitItem.applyModifier(itemStack, false, false);
					itemStack.remove(DataComponentTypes.CUSTOM_DATA);
				}
			});
		}
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		ItemStack thisStack = user.getStackInHand(hand);
		EquipmentSlot slotType = this.getSlotType();
		if (user.isSneaking() && (slotType == EquipmentSlot.HEAD || slotType == EquipmentSlot.LEGS)) {
			TRItemUtils.switchActive(thisStack, 1, user);
			return ActionResult.SUCCESS;
		}
		return super.use(world, user, hand);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		if (this.getSlotType() == EquipmentSlot.HEAD) {
			TRItemUtils.buildActiveTooltip(stack, tooltip);
		}

		// Will only add Inactive/Active tooltip if sprint is enabled
		boolean sprintEnable = this.getSlotType() == EquipmentSlot.LEGS && TechRebornConfig.quantumSuitEnableSprint;
		if (sprintEnable) {
			TRItemUtils.buildActiveTooltip(stack, tooltip);
		}

		AttributeModifiersComponent attributes = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
		tooltip.add(Text.empty());
		tooltip.add(AttributeModifierBuilder.text(getSlotType()));
		if (AttributeModifierBuilder.equals(attributes, hasPowerAttributes)) {
			(sprintEnable && TRItemUtils.isActive(stack) ? hasPowerSprintAttributes : hasPowerAttributes).modifiers().forEach(entry -> {
				tooltip.add(AttributeModifierBuilder.text(entry).formatted(Formatting.BLUE));
			});
		} else if (AttributeModifierBuilder.equals(attributes, fullSuitAttributes)) {
			(sprintEnable && TRItemUtils.isActive(stack) ? hasPowerSprintAttributes : hasPowerAttributes).modifiers().forEach(entry -> {
				tooltip.add(AttributeModifierBuilder.text(entry).formatted(Formatting.BLUE));
			});
			tooltip.add(Text.empty());
			tooltip.add(Text.translatable("item.modifiers.full_suit").formatted(Formatting.YELLOW));
			FULL_SUIT.modifiers().forEach(entry -> {
				tooltip.add(AttributeModifierBuilder.text(entry).formatted(Formatting.YELLOW));
			});
		} else {
			noPowerAttributes.modifiers().forEach(entry -> {
				tooltip.add(AttributeModifierBuilder.text(entry).formatted(Formatting.BLUE));
			});
			if (stack.contains(DataComponentTypes.CUSTOM_DATA)) {
				return;
			}
			tooltip.add(Text.translatable("item.modifiers.power").formatted(Formatting.GRAY));
			hasPowerAttributes.modifiers().forEach(entry -> {
				tooltip.add(AttributeModifierBuilder.text(entry).formatted(Formatting.BLUE));
			});
			tooltip.add(Text.translatable("item.modifiers.all_equipment").formatted(Formatting.GRAY));
			FULL_SUIT.modifiers().forEach(entry -> {
				tooltip.add(AttributeModifierBuilder.text(entry).formatted(Formatting.BLUE));
			});
		}
	}
}
