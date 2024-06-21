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

public record RebornIngredient(int count, Ingredient ingredient) implements Predicate<ItemStack> {
	public static MapCodec<RebornIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.INT.optionalFieldOf("count", 1).forGetter(RebornIngredient::count),
		MapCodec.assumeMapUnsafe(Ingredient.DISALLOW_EMPTY_CODEC).forGetter(RebornIngredient::ingredient)
	).apply(instance, RebornIngredient::new));
	public static PacketCodec<RegistryByteBuf, RebornIngredient> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.INTEGER, RebornIngredient::count,
		Ingredient.PACKET_CODEC, RebornIngredient::ingredient,
		RebornIngredient::new
	);

	@Override
	public boolean test(ItemStack itemStack) {
		return ingredient.test(itemStack);
	}

	public Ingredient getPreview() {
		return ingredient;
	}

	public List<ItemStack> getPreviewStacks() {
		return Arrays.asList(ingredient.getMatchingStacks());
	}
}
