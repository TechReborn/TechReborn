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
import net.minecraft.world.item.ItemStackTemplate;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeInput;
import reborncore.common.crafting.SizedIngredient;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;

public record RollingMachineRecipe(RecipeType<? extends RollingMachineRecipe> type, int power, int time, ShapedRecipe shapedRecipe) implements RebornRecipe {
	public static Function<RecipeType<RollingMachineRecipe>, MapCodec<RollingMachineRecipe>> CODEC = type -> RecordCodecBuilder.mapCodec(instance -> instance.group(
		ExtraCodecs.POSITIVE_INT.fieldOf("power").forGetter(RebornRecipe::power),
		ExtraCodecs.POSITIVE_INT.fieldOf("time").forGetter(RebornRecipe::time),
		ShapedRecipe.SERIALIZER.codec().forGetter(RollingMachineRecipe::getShapedRecipe)
	).apply(instance, (power, time, shaped) -> new RollingMachineRecipe(type, power, time, shaped)));
	public static Function<RecipeType<RollingMachineRecipe>, StreamCodec<RegistryFriendlyByteBuf, RollingMachineRecipe>> PACKET_CODEC = type -> StreamCodec.composite(
		SizedIngredient.PACKET_CODEC.apply(ByteBufCodecs.list()), RebornRecipe::ingredients,
		ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list()), RebornRecipe::outputs,
		ByteBufCodecs.INT, RebornRecipe::power,
		ByteBufCodecs.INT, RebornRecipe::time,
		ShapedRecipe.SERIALIZER.streamCodec(), RollingMachineRecipe::getShapedRecipe,
		(ingredients, outputs, power, time, shaped) -> new RollingMachineRecipe(type, power, time, shaped)
	);

	@Override
	public List<ItemStackTemplate> outputs() {
		// Input does not affect the result
		ItemStack stack = shapedRecipe.assemble(null);
		return Collections.singletonList(new ItemStackTemplate(stack.getItem().builtInRegistryHolder(), stack.getCount(), stack.getComponentsPatch()));
	}

	@Override
	public ItemStack assemble(RebornRecipeInput inventory) {
		// Input does not affect the result
		return shapedRecipe.assemble(null);
	}

	@Override
	public List<SizedIngredient> ingredients() {
		return List.of();
	}

	@Override
	public PlacementInfo placementInfo() {
		return shapedRecipe.placementInfo();
	}

	public ShapedRecipe getShapedRecipe() {
		return shapedRecipe;
	}
}
