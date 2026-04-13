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
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * An ingredient with a specific item stack count
 * @param count The count of the ingredient
 * @param ingredient The ingredient
 */
public record SizedIngredient(int count, Ingredient ingredient) implements Predicate<ItemStack> {
	public static MapCodec<SizedIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.INT.optionalFieldOf("count", 1).forGetter(SizedIngredient::count),
		Ingredient.CODEC.fieldOf("ingredient").forGetter(SizedIngredient::ingredient)
	).apply(instance, SizedIngredient::new));
	public static StreamCodec<RegistryFriendlyByteBuf, SizedIngredient> PACKET_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, SizedIngredient::count,
		Ingredient.CONTENTS_STREAM_CODEC, SizedIngredient::ingredient,
		SizedIngredient::new
	);

	@Override
	public boolean test(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			// Never match empty stacks.
			return false;
		}

		if (itemStack.getCount() < count) {
			return false;
		}

		return ingredient.test(itemStack);
	}

	public List<ItemStackTemplate> getPreviewStacks() {
		CustomIngredient customIngredient = ingredient.getCustomIngredient();
		Stream<ItemStackTemplate> stacks;
		if (customIngredient != null) {
			stacks = ((SlotDisplay.Composite) customIngredient.display()).contents().stream()
				.map(display -> ((SlotDisplay.ItemStackSlotDisplay) display).stack());
		} else {
			stacks = ingredient.values.stream().map(entry -> new ItemStackTemplate(entry.value()));
		}
		return stacks.map(item -> new ItemStackTemplate(item.item().value(), count)).toList();
	}
}
