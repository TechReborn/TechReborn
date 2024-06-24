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

package techreborn.api.recipe.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.dynamic.Codecs;
import reborncore.common.crafting.RebornFluidRecipe;
import reborncore.common.crafting.SizedIngredient;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.multiblock.IndustrialSawmillBlockEntity;

import java.util.List;
import java.util.function.Function;

public class IndustrialSawmillRecipe extends RebornFluidRecipe {
	public static final Function<RecipeType<IndustrialSawmillRecipe>, MapCodec<IndustrialSawmillRecipe>> CODEC = type -> RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.list(SizedIngredient.CODEC.codec()).fieldOf("ingredients").forGetter(RebornRecipe::getSizedIngredients),
		Codec.list(ItemStack.CODEC).fieldOf("outputs").forGetter(RebornRecipe::getOutputs),
		Codecs.POSITIVE_INT.fieldOf("power").forGetter(RebornRecipe::getPower),
		Codecs.POSITIVE_INT.fieldOf("time").forGetter(RebornRecipe::getTime),
		FluidInstance.CODEC.fieldOf("fluid").forGetter(RebornFluidRecipe::getFluidInstance)
	).apply(instance, (ingredients, outputs, power, time, fluid) -> new IndustrialSawmillRecipe(type, ingredients, outputs, power, time, fluid)));
	public static final Function<RecipeType<IndustrialSawmillRecipe>, PacketCodec<RegistryByteBuf, IndustrialSawmillRecipe>> PACKET_CODEC = type -> PacketCodec.tuple(
		SizedIngredient.PACKET_CODEC.collect(PacketCodecs.toList()), RebornRecipe::getSizedIngredients,
		ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()), RebornRecipe::getOutputs,
		PacketCodecs.INTEGER, RebornRecipe::getPower,
		PacketCodecs.INTEGER, RebornRecipe::getTime,
		FluidInstance.PACKET_CODEC, RebornFluidRecipe::getFluidInstance,
		(ingredients, outputs, power, time, fluid) -> new IndustrialSawmillRecipe(type, ingredients, outputs, power, time, fluid)
	);
	
	public IndustrialSawmillRecipe(RecipeType<?> type, List<SizedIngredient> ingredients, List<ItemStack> outputs, int power, int time, FluidInstance fluid) {
		super(type, ingredients, outputs, power, time, fluid);
	}

	@Override
	public Tank getTank(BlockEntity be) {
		IndustrialSawmillBlockEntity blockEntity = (IndustrialSawmillBlockEntity) be;
		return blockEntity.getTank();
	}

	@Override
	public boolean canCraft(BlockEntity be) {
		IndustrialSawmillBlockEntity blockEntity = (IndustrialSawmillBlockEntity) be;
		if (!blockEntity.isMultiblockValid()) {
			return false;
		}
		return super.canCraft(be);
	}

}
