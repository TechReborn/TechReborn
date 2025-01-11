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

package techreborn.recipe.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.dynamic.Codecs;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeInput;
import reborncore.common.crafting.SizedIngredient;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public record RollingMachineRecipe(RecipeType<? extends RollingMachineRecipe> type, int power, int time, ShapedRecipe shapedRecipe) implements RebornRecipe {
	public static Function<RecipeType<RollingMachineRecipe>, MapCodec<RollingMachineRecipe>> CODEC = type -> RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codecs.POSITIVE_INT.fieldOf("power").forGetter(RebornRecipe::power),
		Codecs.POSITIVE_INT.fieldOf("time").forGetter(RebornRecipe::time),
		RecipeSerializer.SHAPED.codec().forGetter(RollingMachineRecipe::getShapedRecipe)
	).apply(instance, (power, time, shaped) -> new RollingMachineRecipe(type, power, time, shaped)));
	public static Function<RecipeType<RollingMachineRecipe>, PacketCodec<RegistryByteBuf, RollingMachineRecipe>> PACKET_CODEC = type -> PacketCodec.tuple(
		SizedIngredient.PACKET_CODEC.collect(PacketCodecs.toList()), RebornRecipe::ingredients,
		ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()), RebornRecipe::outputs,
		PacketCodecs.INTEGER, RebornRecipe::power,
		PacketCodecs.INTEGER, RebornRecipe::time,
		RecipeSerializer.SHAPED.packetCodec(), RollingMachineRecipe::getShapedRecipe,
		(ingredients, outputs, power, time, shaped) -> new RollingMachineRecipe(type, power, time, shaped)
	);

	@Override
	public List<ItemStack> outputs() {
		// Input does not affect the result
		return Collections.singletonList(shapedRecipe.craft(null, null));
	}

	@Override
	public ItemStack craft(RebornRecipeInput inventory, RegistryWrapper.WrapperLookup lookup) {
		// Input does not affect the result
		return shapedRecipe.craft(null, lookup);
	}

	@Override
	public List<SizedIngredient> ingredients() {
		return List.of();
	}

	@Override
	public IngredientPlacement getIngredientPlacement() {
		return shapedRecipe.getIngredientPlacement();
	}

	public ShapedRecipe getShapedRecipe() {
		return shapedRecipe;
	}
}
