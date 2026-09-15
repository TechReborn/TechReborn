/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TechReborn
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

package techreborn.init;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import techreborn.TechReborn;

import java.util.LinkedHashSet;
import java.util.Set;

public class TRItemSettings {
	private static final ResourceKey<ContextIntProvider> COMPOSTABLE_VERY_LOW = ResourceKey.create(
		Registries.CONTEXT_INT_PROVIDER,
		Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "compostable/very_low")
	);
	public static TooltipDisplay UNBREAKABLE_HIDE = new TooltipDisplay(
		false, new LinkedHashSet<>(Set.of(DataComponents.UNBREAKABLE))
	);

	public static Item.Properties item(String name) {
		Item.Properties properties = FuelRecipes.apply(name, new Item.Properties().setId(key(name)));
		return switch (name) {
			case "rubber_sapling", "rubber_leaves", "saw_dust" -> properties.compostable(ContextIntProviders.COMPOSTABLE_LOW);
			case "saw_small_dust" -> properties.compostable(COMPOSTABLE_VERY_LOW);
			case "plantball", "compressed_plantball" -> properties.compostable(ContextIntProviders.COMPOSTABLE_ALWAYS_ADD_ONE);
			default -> properties;
		};
	}

	public static Item.Properties unbreakable(String name) {
		return item(name).component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
			.component(DataComponents.TOOLTIP_DISPLAY, UNBREAKABLE_HIDE);
	}

	public static Item.Properties reactorComponent(String name) {
		return item(name);
	}

	public static ResourceKey<Item> key(String name) {
		return ResourceKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, name));
	}
}
