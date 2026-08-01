/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2025 TechReborn
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

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jspecify.annotations.Nullable;

import java.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.equipment.ArmorType;

import static techreborn.TechReborn.MOD_ID;

public class AttributeModifierBuilder {
	public static final TooltipDisplay ATTRIBUTE_HIDE = new TooltipDisplay(
		false,
		new LinkedHashSet<>(Set.of(DataComponents.UNBREAKABLE, DataComponents.ATTRIBUTE_MODIFIERS))
	);
	private final ItemAttributeModifiers.Builder builder;
	private final EquipmentSlotGroup target;

	public AttributeModifierBuilder() {
		this(null);
	}

	public AttributeModifierBuilder(@Nullable ArmorType slot) {
		builder = ItemAttributeModifiers.builder();
		target = slot == null ? null : EquipmentSlotGroup.bySlot(slot.getSlot());
	}

	private AttributeModifier modifier(String path, double value) {
		Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, (target == null ? path : path + "/" + target.getSerializedName()));
		return new AttributeModifier(id, value, AttributeModifier.Operation.ADD_VALUE);
	}

	public AttributeModifierBuilder armor(int i) {
		builder.add(Attributes.ARMOR, modifier("suit_armor", i), target);
		return this;
	}

	public AttributeModifierBuilder toughness(int i) {
		builder.add(Attributes.ARMOR_TOUGHNESS, modifier("suit_armor_toughness", i), target);
		return this;
	}

	public AttributeModifierBuilder knockback(double i) {
		builder.add(Attributes.KNOCKBACK_RESISTANCE, modifier("suit_knockback_resistance", i / 10), target);
		return this;
	}

	public ItemAttributeModifiers build() {
		return builder.build();
	}

	public static boolean equals(@Nullable ItemAttributeModifiers attributes, ItemAttributeModifiers target) {
		if (attributes == null) {
			return false;
		}
		List<ItemAttributeModifiers.Entry> m1 = attributes.modifiers();
		List<ItemAttributeModifiers.Entry> m2 = target.modifiers();
		if (m1.size() < m2.size()) {
			return false;
		}
		Map<Identifier, Double> map = new HashMap<>();
		m1.forEach(entry -> map.put(entry.modifier().id(), entry.modifier().amount()));
		for (ItemAttributeModifiers.Entry entry : m2) {
			Double amount = map.get(entry.modifier().id());
			if (amount == null || amount != entry.modifier().amount()) {
				return false;
			}
		}
		return true;
	}

	public static void appendText(List<Component> tooltip, ItemAttributeModifiers attributes, ChatFormatting formatting) {
		attributes.modifiers().forEach(entry -> {
			tooltip.add(AttributeModifierBuilder.text(entry.attribute(), entry.modifier(), entry.modifier().amount()).withStyle(formatting));
		});
	}

	public static void appendEnchantmentText(List<Component> tooltip, ItemStack stack, EquipmentSlot slotType, ChatFormatting formatting) {
		EnchantmentHelper.forEachModifier(
			stack, EquipmentSlotGroup.bySlot(slotType), (entry, modifier) -> {
				tooltip.add(AttributeModifierBuilder.text(entry, modifier, modifier.amount()).withStyle(formatting));
			}
		);
	}

	public static void appendArmorEnchantmentText(List<Component> tooltip, ItemStack stack, ChatFormatting formatting) {
		MutableBoolean mutableBoolean = new MutableBoolean(true);
		EnchantmentHelper.forEachModifier(
			stack, EquipmentSlotGroup.ARMOR, (entry, modifier) -> {
				if (mutableBoolean.isTrue()) {
					tooltip.add(CommonComponents.EMPTY);
					tooltip.add(Component.translatable("item.modifiers.armor").withStyle(ChatFormatting.GRAY));
					mutableBoolean.setFalse();
				}
				tooltip.add(AttributeModifierBuilder.text(entry, modifier, modifier.amount()).withStyle(formatting));
			}
		);
	}

	public static void appendDiffText(
		List<Component> tooltip,
		@Nullable ItemAttributeModifiers attributes,
		ItemAttributeModifiers target,
		ChatFormatting formatting
	) {
		Map<Identifier, Double> map = new HashMap<>();
		if (attributes != null) {
			attributes.modifiers().forEach(entry -> map.put(entry.modifier().id(), entry.modifier().amount()));
		}
		target.modifiers().forEach(entry -> {
			double value = entry.modifier().amount() - map.getOrDefault(entry.modifier().id(), 0d);
			if (value != 0) {
				tooltip.add(AttributeModifierBuilder.text(entry.attribute(), entry.modifier(), value).withStyle(formatting));
			}
		});
	}

	public static void appendEnd(List<Component> tooltip, List<Component> buffer) {
		TextColor skip = TextColor.fromLegacyFormat(ChatFormatting.DARK_GRAY);
		for (int i = tooltip.size() - 1; i >= 0; i--) {
			if (tooltip.get(i).getStyle().getColor() != skip) {
				tooltip.addAll(i + 1, buffer);
				break;
			}
		}
	}

	private static MutableComponent text(Holder<Attribute> attribute, AttributeModifier modifier, double value) {
		AttributeModifier.Operation operation = modifier.operation();
		if (operation == AttributeModifier.Operation.ADD_MULTIPLIED_BASE || operation == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
			value = value * 100.0;
		} else if (Attributes.KNOCKBACK_RESISTANCE.is(attribute.unwrapKey().orElseThrow())) {
			value *= 10;
		}
		return Component.translatable(
			(value > 0 ? "attribute.modifier.plus." : "attribute.modifier.take.") + operation.id(),
			ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(value > 0 ? value : -value),
			Component.translatable(attribute.value().getDescriptionId())
		);
	}

	public static MutableComponent text(EquipmentSlot slotType) {
		return Component.translatable("item.modifiers." + EquipmentSlotGroup.bySlot(slotType).getSerializedName());
	}
}
