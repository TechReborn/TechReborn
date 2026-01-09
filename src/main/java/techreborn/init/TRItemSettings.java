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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TooltipDisplay;
import techreborn.TechReborn;

import java.util.LinkedHashSet;
import java.util.Set;

public class TRItemSettings {
	public static TooltipDisplay UNBREAKABLE_HIDE = new TooltipDisplay(
		false, new LinkedHashSet<>(Set.of(DataComponents.UNBREAKABLE))
	);

	public static Item.Properties item(String name) {
		return new Item.Properties().setId(key(name));
	}

	public static Item.Properties unbreakable(String name) {
		return item(name).component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
			.component(DataComponents.TOOLTIP_DISPLAY, UNBREAKABLE_HIDE);
	}

	public static ResourceKey<Item> key(String name) {
		return ResourceKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, name));
	}
}
