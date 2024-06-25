/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * An ingredient with a specific item stack count
 * @param count The count of the ingredient
 * @param ingredient The ingredient
 */
public record SizedIngredient(int count, Ingredient ingredient) implements Predicate<ItemStack> {
	public static MapCodec<SizedIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.INT.optionalFieldOf("count", 1).forGetter(SizedIngredient::count),
		MapCodec.assumeMapUnsafe(Ingredient.DISALLOW_EMPTY_CODEC).forGetter(SizedIngredient::ingredient)
	).apply(instance, SizedIngredient::new));
	public static PacketCodec<RegistryByteBuf, SizedIngredient> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.INTEGER, SizedIngredient::count,
		Ingredient.PACKET_CODEC, SizedIngredient::ingredient,
		SizedIngredient::new
	);

	@Override
	public boolean test(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			// Never match empty stacks.
			return false;
		}

		return ingredient.test(itemStack);
	}

	public List<ItemStack> getPreviewStacks() {
		return Arrays.asList(ingredient.getMatchingStacks());
	}
}
