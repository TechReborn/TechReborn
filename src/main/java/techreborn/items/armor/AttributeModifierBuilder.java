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

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static techreborn.TechReborn.MOD_ID;

public class AttributeModifierBuilder {
	private final AttributeModifiersComponent.Builder builder;
	private final AttributeModifierSlot target;
	private boolean tooltip = true;

	public AttributeModifierBuilder() {
		this(null);
	}

	public AttributeModifierBuilder(@Nullable ArmorItem.Type slot) {
		builder = AttributeModifiersComponent.builder();
		target = slot == null ? null : AttributeModifierSlot.forEquipmentSlot(slot.getEquipmentSlot());
	}

	private EntityAttributeModifier modifier(String path, double value) {
		Identifier id = Identifier.of(MOD_ID, (target == null ? path : path + "/" + target.asString()));
		return new EntityAttributeModifier(id, value, EntityAttributeModifier.Operation.ADD_VALUE);
	}

	public AttributeModifierBuilder armor(int i) {
		builder.add(EntityAttributes.GENERIC_ARMOR, modifier("nano_suit_armor", i), target);
		return this;
	}

	public AttributeModifierBuilder toughness(int i) {
		builder.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, modifier("nano_suit_armor_toughness", i), target);
		return this;
	}

	public AttributeModifierBuilder knockback(double i) {
		builder.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, modifier("nano_suit_knockback_resistance", i / 10), target);
		return this;
	}

	public AttributeModifierBuilder tooltip(boolean show) {
		tooltip = show;
		return this;
	}

	public AttributeModifiersComponent build() {
		AttributeModifiersComponent component = builder.build();
		return tooltip ? component : component.withShowInTooltip(false);
	}

	public static boolean equals(@Nullable AttributeModifiersComponent attributes, AttributeModifiersComponent target) {
		if (attributes == null) {
			return false;
		}
		List<AttributeModifiersComponent.Entry> m1 = attributes.modifiers();
		List<AttributeModifiersComponent.Entry> m2 = target.modifiers();
		if (m1.size() < m2.size()) {
			return false;
		}
		Map<Identifier, Double> map = new HashMap<>();
		m1.forEach(entry -> map.put(entry.modifier().id(), entry.modifier().value()));
		for (AttributeModifiersComponent.Entry entry : m2) {
			if (map.get(entry.modifier().id()) != entry.modifier().value()) {
				return false;
			}
		}
		return true;
	}

	public static void appendText(List<Text> tooltip, AttributeModifiersComponent attributes, Formatting formatting) {
		attributes.modifiers().forEach(entry -> {
			tooltip.add(AttributeModifierBuilder.text(entry.attribute(), entry.modifier(), entry.modifier().value()).formatted(formatting));
		});
	}

	public static void appendEnchantmentText(List<Text> tooltip, ItemStack stack, EquipmentSlot slotType, Formatting formatting) {
		EnchantmentHelper.applyAttributeModifiers(
			stack, AttributeModifierSlot.forEquipmentSlot(slotType), (entry, modifier) -> {
				tooltip.add(AttributeModifierBuilder.text(entry, modifier, modifier.value()).formatted(formatting));
			}
		);
	}

	public static void appendArmorEnchantmentText(List<Text> tooltip, ItemStack stack, Formatting formatting) {
		MutableBoolean mutableBoolean = new MutableBoolean(true);
		EnchantmentHelper.applyAttributeModifiers(
			stack, AttributeModifierSlot.ARMOR, (entry, modifier) -> {
				if (mutableBoolean.isTrue()) {
					tooltip.add(ScreenTexts.EMPTY);
					tooltip.add(Text.translatable("item.modifiers.armor").formatted(Formatting.GRAY));
					mutableBoolean.setFalse();
				}
				tooltip.add(AttributeModifierBuilder.text(entry, modifier, modifier.value()).formatted(formatting));
			}
		);
	}

	public static void appendDiffText(
		List<Text> tooltip,
		@Nullable AttributeModifiersComponent attributes,
		AttributeModifiersComponent target,
		Formatting formatting
	) {
		Map<Identifier, Double> map = new HashMap<>();
		if (attributes != null) {
			attributes.modifiers().forEach(entry -> map.put(entry.modifier().id(), entry.modifier().value()));
		}
		target.modifiers().forEach(entry -> {
			double value = entry.modifier().value() - map.getOrDefault(entry.modifier().id(), 0d);
			if (value != 0) {
				tooltip.add(AttributeModifierBuilder.text(entry.attribute(), entry.modifier(), value).formatted(formatting));
			}
		});
	}

	public static void appendEnd(List<Text> tooltip, List<Text> buffer) {
		TextColor skip = TextColor.fromFormatting(Formatting.DARK_GRAY);
		for (int i = tooltip.size() - 1; i >= 0; i--) {
			if (tooltip.get(i).getStyle().getColor() != skip) {
				tooltip.addAll(i + 1, buffer);
				break;
			}
		}
	}

	private static MutableText text(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, double value) {
		EntityAttributeModifier.Operation operation = modifier.operation();
		if (operation == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE || operation == EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
			value = value * 100.0;
		} else if (EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE.matchesKey(attribute.getKey().orElseThrow())) {
			value *= 10;
		}
		return Text.translatable(
			(value > 0 ? "attribute.modifier.plus." : "attribute.modifier.take.") + operation.getId(),
			AttributeModifiersComponent.DECIMAL_FORMAT.format(value > 0 ? value : -value),
			Text.translatable(attribute.value().getTranslationKey())
		);
	}

	public static MutableText text(EquipmentSlot slotType) {
		return Text.translatable("item.modifiers." + AttributeModifierSlot.forEquipmentSlot(slotType).asString());
	}
}
