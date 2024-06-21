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
import reborncore.common.crafting.RebornIngredient;
import reborncore.common.crafting.RebornRecipe;
import techreborn.blockentity.machine.multiblock.FusionControlComputerBlockEntity;
import techreborn.init.TRContent;

import java.util.List;
import java.util.function.Function;

/**
 * @author drcrazy
 */
public class FusionReactorRecipe extends RebornRecipe {
	public static Function<RecipeType<FusionReactorRecipe>, MapCodec<FusionReactorRecipe>> CODEC = type -> RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.list(RebornIngredient.CODEC.codec()).fieldOf("ingredients").forGetter(RebornRecipe::getRebornIngredients),
		Codec.list(ItemStack.CODEC).fieldOf("outputs").forGetter(RebornRecipe::getOutputs),
		Codecs.POSITIVE_INT.fieldOf("power").forGetter(RebornRecipe::getPower),
		Codecs.POSITIVE_INT.fieldOf("time").forGetter(RebornRecipe::getTime),
		Codec.INT.fieldOf("startEnergy").forGetter(FusionReactorRecipe::getStartEnergy),
		Codecs.POSITIVE_INT.fieldOf("minSize").forGetter(FusionReactorRecipe::getMinSize)
	).apply(instance, (ingredients, outputs, power, time, startE, minSize) -> new FusionReactorRecipe(type, ingredients, outputs, power, time, startE, minSize)));
	public static Function<RecipeType<FusionReactorRecipe>, PacketCodec<RegistryByteBuf, FusionReactorRecipe>> PACKET_CODEC = type -> PacketCodec.tuple(
		RebornIngredient.PACKET_CODEC.collect(PacketCodecs.toList()), RebornRecipe::getRebornIngredients,
		ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()), RebornRecipe::getOutputs,
		PacketCodecs.INTEGER, RebornRecipe::getPower,
		PacketCodecs.INTEGER, RebornRecipe::getTime,
		PacketCodecs.INTEGER, FusionReactorRecipe::getStartEnergy,
		PacketCodecs.INTEGER, FusionReactorRecipe::getMinSize,
		(ingredients, outputs, power, time, startE, minSize) -> new FusionReactorRecipe(type, ingredients, outputs, power, time, startE, minSize)
	);
	
	private final int startE;
	private final int minSize;

	public FusionReactorRecipe(RecipeType<?> type, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time, int startE, int minSize) {
		super(type, ingredients, outputs, power, time);
		this.startE = startE;
		this.minSize = minSize;
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(TRContent.Machine.FUSION_CONTROL_COMPUTER);
	}

	public int getStartEnergy() {
		return startE;
	}

	public int getMinSize() {
		return minSize;
	}

	@Override
	public boolean canCraft(final BlockEntity blockEntity) {
		if (blockEntity instanceof final FusionControlComputerBlockEntity reactor) {
			return reactor.getSize() >= minSize;
		}
		return false;
	}

}
