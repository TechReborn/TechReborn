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
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import reborncore.api.items.ArmorBlockEntityTicker;
import reborncore.api.items.ArmorRemoveHandler;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.config.TechRebornConfig;
import techreborn.utils.TRItemUtils;

import java.util.ArrayList;
import java.util.List;

public class NanoSuitItem extends TREnergyArmourItem implements ArmorBlockEntityTicker, ArmorRemoveHandler {
	private static final AttributeModifiersComponent FULL_SUIT = new AttributeModifierBuilder().armor(10).toughness(4).build();
	private final AttributeModifiersComponent noPowerAttributes;
	private final AttributeModifiersComponent hasPowerAttributes;
	private final AttributeModifiersComponent fullSuitAttributes;

	public NanoSuitItem(RegistryEntry<ArmorMaterial> material, Type slot) {
		super(material, slot, TechRebornConfig.nanoSuitCapacity, RcEnergyTier.HIGH);
		switch (slot) {
			case HELMET, BOOTS:
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(1).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(3).toughness(2).knockback(1).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(5).toughness(3).knockback(1).tooltip(false).build();
				break;
			case CHESTPLATE:
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(2).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(6).toughness(2).knockback(1).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(10).toughness(3).knockback(1).tooltip(false).build();
				break;
			case LEGGINGS:
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(3).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(8).toughness(2).knockback(1).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(10).toughness(3).knockback(1).tooltip(false).build();
				break;
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
		// Night Vision
		if (this.getSlotType() == EquipmentSlot.HEAD) {
			if (TRItemUtils.isActive(stack) && tryUseEnergy(stack, TechRebornConfig.suitNightVisionCost)) {
				playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 220, 1, false, false));
			} else {
				playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
			}
		}
		applyModifier(stack, hasFullSuit);
	}

	private void applyModifier(ItemStack stack, AttributeModifiersComponent attributes, AttributeModifiersComponent target) {
		if (attributes != target) {
			stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, target);
		}
	}

	public void applyModifier(ItemStack stack, boolean hasFullSuit) {
		AttributeModifiersComponent attributes = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
		if (getStoredEnergy(stack) > 0) {
			if (hasFullSuit) {
				applyModifier(stack, attributes, fullSuitAttributes);
			} else {
				applyModifier(stack, attributes, hasPowerAttributes);
			}
		} else {
			applyModifier(stack, attributes, noPowerAttributes);
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack thisStack = user.getStackInHand(hand);
		EquipmentSlot slotType = this.getSlotType();
		if (user.isSneaking() && slotType == EquipmentSlot.HEAD) {
			TRItemUtils.switchActive(thisStack, 1, user);
			return TypedActionResult.success(thisStack);
		}
		return super.use(world, user, hand);
	}

	@Override
	public void onRemoved(PlayerEntity playerEntity) {
		playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
		ItemStack stack = playerEntity.playerScreenHandler.getCursorStack();
		if (stack.getItem() instanceof NanoSuitItem nanoSuitItem) {
			nanoSuitItem.applyModifier(stack, false);
			stack.remove(DataComponentTypes.CUSTOM_DATA);
		} else {
			playerEntity.getInventory().main.forEach(itemStack -> {
				if (itemStack.getItem() instanceof NanoSuitItem nanoSuitItem) {
					nanoSuitItem.applyModifier(itemStack, false);
					itemStack.remove(DataComponentTypes.CUSTOM_DATA);
				}
			});
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		if (this.type == Type.HELMET) {
			TRItemUtils.buildActiveTooltip(stack, tooltip);
		}
	}

	public void appendArmorTooltip(ItemStack stack, List<Text> tooltip, boolean shift) {
		List<Text> buffer = new ArrayList<>();
		AttributeModifiersComponent attributes = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
		if (AttributeModifierBuilder.equals(attributes, hasPowerAttributes)) {
			if (shift) {
				buffer.add(Text.translatable("item.modifiers.all_equipment").formatted(Formatting.GRAY));
				AttributeModifierBuilder.appendText(buffer, FULL_SUIT, Formatting.BLUE);
			}
		} else if (AttributeModifierBuilder.equals(attributes, fullSuitAttributes)) {
			buffer.add(ScreenTexts.EMPTY);
			EquipmentSlot slotType = getSlotType();
			buffer.add(AttributeModifierBuilder.text(slotType).formatted(Formatting.GRAY));
			AttributeModifierBuilder.appendText(buffer, shift ? attributes : hasPowerAttributes, Formatting.BLUE);
			AttributeModifierBuilder.appendEnchantmentText(buffer, stack, slotType, Formatting.BLUE);
			AttributeModifierBuilder.appendArmorEnchantmentText(buffer, stack, Formatting.BLUE);
			if (!shift) {
				buffer.add(ScreenTexts.EMPTY);
				buffer.add(Text.translatable("item.modifiers.full_suit").formatted(Formatting.YELLOW));
				AttributeModifierBuilder.appendText(buffer, FULL_SUIT, Formatting.YELLOW);
			}
		} else {
			if (!shift && stack.contains(DataComponentTypes.CUSTOM_DATA)) {
				return;
			}
			buffer.add(Text.translatable("item.modifiers.power").formatted(Formatting.GRAY));
			AttributeModifierBuilder.appendDiffText(buffer, attributes, hasPowerAttributes, Formatting.BLUE);
			buffer.add(Text.translatable("item.modifiers.all_equipment").formatted(Formatting.GRAY));
			AttributeModifierBuilder.appendText(buffer, FULL_SUIT, Formatting.BLUE);
		}
		AttributeModifierBuilder.appendEnd(tooltip, buffer);
	}
}
