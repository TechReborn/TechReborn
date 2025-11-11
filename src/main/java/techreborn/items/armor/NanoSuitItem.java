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

import reborncore.api.items.ArmorBlockEntityTicker;
import reborncore.api.items.ArmorRemoveHandler;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRItemSettings;
import techreborn.utils.TRItemUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;

public class NanoSuitItem extends TREnergyArmourItem implements ArmorBlockEntityTicker, ArmorRemoveHandler {
	private static final ItemAttributeModifiers FULL_SUIT = new AttributeModifierBuilder().armor(10).toughness(4).build();
	private final ItemAttributeModifiers noPowerAttributes;
	private final ItemAttributeModifiers hasPowerAttributes;
	private final ItemAttributeModifiers fullSuitAttributes;

	public NanoSuitItem(ArmorMaterial material, ArmorType slot, String name) {
		super(material, slot, TechRebornConfig.nanoSuitCapacity, RcEnergyTier.HIGH, name);
		switch (slot) {
			case HELMET, BOOTS:
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(1).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(3).toughness(2).knockback(1).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(5).toughness(3).knockback(1).build();
				break;
			case CHESTPLATE:
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(2).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(6).toughness(2).knockback(1).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(10).toughness(3).knockback(1).build();
				break;
			case LEGGINGS:
				noPowerAttributes = new AttributeModifierBuilder(slot).armor(3).build();
				hasPowerAttributes = new AttributeModifierBuilder(slot).armor(8).toughness(2).knockback(1).build();
				fullSuitAttributes = new AttributeModifierBuilder(slot).armor(10).toughness(3).knockback(1).build();
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
	public void tickArmor(ItemStack stack, boolean hasFullSuit, Player playerEntity) {
		// Night Vision
		if (this.getSlotType() == EquipmentSlot.HEAD) {
			if (TRItemUtils.isActive(stack) && tryUseEnergy(stack, TechRebornConfig.suitNightVisionCost)) {
				playerEntity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 1, false, false));
			} else {
				playerEntity.removeEffect(MobEffects.NIGHT_VISION);
			}
		}
		applyModifier(stack, hasFullSuit);
	}

	public void applyModifier(ItemStack stack, boolean hasFullSuit) {
		ItemAttributeModifiers attributes = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
		if (getStoredEnergy(stack) > 0) {
			if (hasFullSuit) {
				if (attributes != fullSuitAttributes) {
					stack.set(DataComponents.ATTRIBUTE_MODIFIERS, fullSuitAttributes);
					stack.set(DataComponents.TOOLTIP_DISPLAY, AttributeModifierBuilder.ATTRIBUTE_HIDE);
				}
			} else if (attributes != hasPowerAttributes) {
				stack.set(DataComponents.ATTRIBUTE_MODIFIERS, hasPowerAttributes);
				stack.set(DataComponents.TOOLTIP_DISPLAY, TRItemSettings.UNBREAKABLE_HIDE);
			}
		} else if (attributes != noPowerAttributes) {
			stack.set(DataComponents.ATTRIBUTE_MODIFIERS, noPowerAttributes);
			stack.set(DataComponents.TOOLTIP_DISPLAY, TRItemSettings.UNBREAKABLE_HIDE);
		}
	}

	@Override
	public InteractionResult use(Level world, Player user, InteractionHand hand) {
		ItemStack thisStack = user.getItemInHand(hand);
		EquipmentSlot slotType = this.getSlotType();
		if (user.isShiftKeyDown() && slotType == EquipmentSlot.HEAD) {
			TRItemUtils.switchActive(thisStack, 1, user);
			return InteractionResult.SUCCESS;
		}
		return super.use(world, user, hand);
	}

	@Override
	public void onRemoved(Player playerEntity) {
		playerEntity.removeEffect(MobEffects.NIGHT_VISION);
		ItemStack stack = playerEntity.inventoryMenu.getCarried();
		if (stack.getItem() instanceof NanoSuitItem nanoSuitItem) {
			nanoSuitItem.applyModifier(stack, false);
			stack.remove(DataComponents.CUSTOM_DATA);
		} else {
			playerEntity.getInventory().getNonEquipmentItems().forEach(itemStack -> {
				if (itemStack.getItem() instanceof NanoSuitItem nanoSuitItem) {
					nanoSuitItem.applyModifier(itemStack, false);
					itemStack.remove(DataComponents.CUSTOM_DATA);
				}
			});
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> tooltip, TooltipFlag type) {
		if (this.getSlotType() == EquipmentSlot.HEAD) {
			TRItemUtils.buildActiveTooltip(stack, tooltip);
		}
	}

	public void appendArmorTooltip(ItemStack stack, List<Component> tooltip, boolean shift) {
		List<Component> buffer = new ArrayList<>();
		ItemAttributeModifiers attributes = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
		if (AttributeModifierBuilder.equals(attributes, hasPowerAttributes)) {
			if (shift) {
				buffer.add(Component.translatable("item.modifiers.all_equipment").withStyle(ChatFormatting.GRAY));
				AttributeModifierBuilder.appendText(buffer, FULL_SUIT, ChatFormatting.BLUE);
			}
		} else if (AttributeModifierBuilder.equals(attributes, fullSuitAttributes)) {
			buffer.add(CommonComponents.EMPTY);
			EquipmentSlot slotType = getSlotType();
			buffer.add(AttributeModifierBuilder.text(slotType).withStyle(ChatFormatting.GRAY));
			AttributeModifierBuilder.appendText(buffer, shift ? attributes : hasPowerAttributes, ChatFormatting.BLUE);
			AttributeModifierBuilder.appendEnchantmentText(buffer, stack, slotType, ChatFormatting.BLUE);
			AttributeModifierBuilder.appendArmorEnchantmentText(buffer, stack, ChatFormatting.BLUE);
			if (!shift) {
				buffer.add(CommonComponents.EMPTY);
				buffer.add(Component.translatable("item.modifiers.full_suit").withStyle(ChatFormatting.YELLOW));
				AttributeModifierBuilder.appendText(buffer, FULL_SUIT, ChatFormatting.YELLOW);
			}
		} else {
			if (!shift && stack.has(DataComponents.CUSTOM_DATA)) {
				return;
			}
			buffer.add(Component.translatable("item.modifiers.power").withStyle(ChatFormatting.GRAY));
			AttributeModifierBuilder.appendDiffText(buffer, noPowerAttributes, hasPowerAttributes, ChatFormatting.BLUE);
			buffer.add(Component.translatable("item.modifiers.all_equipment").withStyle(ChatFormatting.GRAY));
			AttributeModifierBuilder.appendText(buffer, FULL_SUIT, ChatFormatting.BLUE);
		}
		AttributeModifierBuilder.appendEnd(tooltip, buffer);
	}
}
