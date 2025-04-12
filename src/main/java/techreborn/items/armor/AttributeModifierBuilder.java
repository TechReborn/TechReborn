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

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.UUID;

public class AttributeModifierBuilder {
	private final ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder;
	private final EquipmentSlot target;

	public AttributeModifierBuilder() {
		this(null);
	}

	public AttributeModifierBuilder(ArmorItem.Type type) {
		builder = ImmutableMultimap.builder();
		target = type == null ? null : type.getEquipmentSlot();
	}

	private EntityAttributeModifier modifier(String path, double value) {
		UUID id = TRArmourItem.MODIFIERS[target == null ? 0 : target.getEntitySlotId()];
		return new EntityAttributeModifier(id, path, value, EntityAttributeModifier.Operation.ADDITION);
	}

	public AttributeModifierBuilder armor(int i) {
		builder.put(EntityAttributes.GENERIC_ARMOR, modifier("Armor modifier", i));
		return this;
	}

	public AttributeModifierBuilder toughness(int i) {
		builder.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, modifier("Toughness modifier", i));
		return this;
	}

	public AttributeModifierBuilder knockback(double i) {
		builder.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, modifier("Knockback modifier", i / 10));
		return this;
	}

	public Multimap<EntityAttribute, EntityAttributeModifier> build() {
		return builder.build();
	}

	public static void appendText(List<Text> tooltip, Multimap<EntityAttribute, EntityAttributeModifier> attributes, Formatting formatting) {
		attributes.forEach((attribute, modifier) -> {
			tooltip.add(AttributeModifierBuilder.text(attribute, modifier, modifier.getValue()).formatted(formatting));
		});
	}

	public static void appendDiffText(
		List<Text> tooltip,
		Multimap<EntityAttribute, EntityAttributeModifier> attributes,
		Multimap<EntityAttribute, EntityAttributeModifier> target,
		Formatting formatting
	) {
		target.forEach((attribute, modifier) -> {
			double source = attributes.get(attribute).stream().findFirst().map(EntityAttributeModifier::getValue).orElse(0d);
			double value = modifier.getValue() - source;
			if (value != 0) {
				tooltip.add(AttributeModifierBuilder.text(attribute, modifier, value).formatted(formatting));
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

	private static MutableText text(EntityAttribute attribute, EntityAttributeModifier modifier, double value) {
		if (attribute.equals(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) {
			value *= 10;
		}
		return Text.translatable(
			"attribute.modifier.plus." + modifier.getOperation().getId(),
			ItemStack.MODIFIER_FORMAT.format(value),
			Text.translatable(attribute.getTranslationKey())
		);
	}

	public static MutableText text(EquipmentSlot slotType) {
		return Text.translatable("item.modifiers." + slotType.getName());
	}
}
