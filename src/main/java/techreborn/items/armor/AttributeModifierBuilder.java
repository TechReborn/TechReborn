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
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static techreborn.TechReborn.MOD_ID;

public class AttributeModifierBuilder {
	private final AttributeModifiersComponent.Builder builder;
	private final AttributeModifierSlot target;

	public AttributeModifierBuilder() {
		this(null);
	}

	public AttributeModifierBuilder(@Nullable EquipmentType slot) {
		builder = AttributeModifiersComponent.builder();
		target = slot == null ? null : AttributeModifierSlot.forEquipmentSlot(slot.getEquipmentSlot());
	}

	private EntityAttributeModifier modifier(String path, double value) {
		Identifier id = Identifier.of(MOD_ID, (target == null ? path : path + "/" + target.asString()));
		return new EntityAttributeModifier(id, value, EntityAttributeModifier.Operation.ADD_VALUE);
	}

	public AttributeModifierBuilder armor(int i) {
		builder.add(EntityAttributes.ARMOR, modifier("nano_suit_armor", i), target);
		return this;
	}

	public AttributeModifierBuilder toughness(int i) {
		builder.add(EntityAttributes.ARMOR_TOUGHNESS, modifier("nano_suit_armor_toughness", i), target);
		return this;
	}

	public AttributeModifierBuilder knockback(double i) {
		builder.add(EntityAttributes.KNOCKBACK_RESISTANCE, modifier("nano_suit_knockback_resistance", i / 10), target);
		return this;
	}

	public AttributeModifiersComponent build() {
		return builder.build().withShowInTooltip(false);
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

	public static MutableText text(AttributeModifiersComponent.Entry entry) {
		RegistryEntry<EntityAttribute> attribute = entry.attribute();
		double value = entry.modifier().value();
		if (EntityAttributes.KNOCKBACK_RESISTANCE.matchesKey(attribute.getKey().orElseThrow())) {
			value *= 10;
		}
		return Text.translatable(
			"attribute.modifier.plus." + entry.modifier().operation().getId(),
			AttributeModifiersComponent.DECIMAL_FORMAT.format(value),
			Text.translatable(attribute.value().getTranslationKey())
		);
	}

	public static MutableText text(EquipmentSlot slotType) {
		return Text.translatable("item.modifiers." + AttributeModifierSlot.forEquipmentSlot(slotType).asString()).formatted(Formatting.GRAY);
	}
}
