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

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import reborncore.common.crafting.SizedIngredient;
import reborncore.common.crafting.RebornRecipe;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class RollingMachineRecipe extends RebornRecipe {
	public static Function<RecipeType<RollingMachineRecipe>, MapCodec<RollingMachineRecipe>> CODEC = type -> RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.list(SizedIngredient.CODEC.codec()).fieldOf("ingredients").forGetter(RebornRecipe::getSizedIngredients),
		Codec.list(ItemStack.CODEC).fieldOf("outputs").forGetter(RebornRecipe::getOutputs),
		Codecs.POSITIVE_INT.fieldOf("power").forGetter(RebornRecipe::getPower),
		Codecs.POSITIVE_INT.fieldOf("time").forGetter(RebornRecipe::getTime),
		RecipeSerializer.SHAPED.codec().forGetter(RollingMachineRecipe::getShapedRecipe)
	).apply(instance, (ingredients, outputs, power, time, shaped) -> new RollingMachineRecipe(type, ingredients, outputs, power, time, shaped)));
	public static Function<RecipeType<RollingMachineRecipe>, PacketCodec<RegistryByteBuf, RollingMachineRecipe>> PACKET_CODEC = type -> PacketCodec.tuple(
		SizedIngredient.PACKET_CODEC.collect(PacketCodecs.toList()), RebornRecipe::getSizedIngredients,
		ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()), RebornRecipe::getOutputs,
		PacketCodecs.INTEGER, RebornRecipe::getPower,
		PacketCodecs.INTEGER, RebornRecipe::getTime,
		RecipeSerializer.SHAPED.packetCodec(), RollingMachineRecipe::getShapedRecipe,
		(ingredients, outputs, power, time, shaped) -> new RollingMachineRecipe(type, ingredients, outputs, power, time, shaped)
	);

	private final ShapedRecipe shapedRecipe;

	public RollingMachineRecipe(RecipeType<?> type, List<SizedIngredient> ingredients, List<ItemStack> outputs, int power, int time, ShapedRecipe shapedRecipe) {
		super(type, ingredients, outputs, power, time);
		this.shapedRecipe = shapedRecipe;
	}

	@Override
	public DefaultedList<SizedIngredient> getSizedIngredients() {
		return DefaultedList.of();
	}

	@Override
	public List<ItemStack> getOutputs(DynamicRegistryManager registryManager) {
		return Collections.singletonList(shapedRecipe.getResult(registryManager));
	}

	@Override
	public ItemStack getResult(RegistryWrapper.WrapperLookup lookup) {
		return shapedRecipe.getResult(lookup);
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return shapedRecipe.getIngredients();
	}

	@Override
	public boolean fits(int width, int height) {
		return shapedRecipe.fits(width, height);
	}

	public ShapedRecipe getShapedRecipe() {
		return shapedRecipe;
	}
}
