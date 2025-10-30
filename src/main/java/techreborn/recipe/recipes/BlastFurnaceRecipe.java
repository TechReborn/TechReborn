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
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeDisplay;
import reborncore.common.crafting.SizedIngredient;
import techreborn.blockentity.machine.multiblock.IndustrialBlastFurnaceBlockEntity;
import techreborn.init.TRContent;

import java.util.List;
import java.util.function.Function;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.block.entity.BlockEntity;

public record BlastFurnaceRecipe(RecipeType<? extends BlastFurnaceRecipe> type, List<SizedIngredient> ingredients, List<ItemStack> outputs, int power, int time, int heat) implements RebornRecipe {
	public static Function<RecipeType<BlastFurnaceRecipe>, MapCodec<BlastFurnaceRecipe>> CODEC = type -> RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.list(SizedIngredient.CODEC.codec()).fieldOf("ingredients").forGetter(RebornRecipe::ingredients),
		Codec.list(ItemStack.CODEC).fieldOf("outputs").forGetter(RebornRecipe::outputs),
		ExtraCodecs.POSITIVE_INT.fieldOf("power").forGetter(RebornRecipe::power),
		ExtraCodecs.POSITIVE_INT.fieldOf("time").forGetter(RebornRecipe::time),
		ExtraCodecs.POSITIVE_INT.fieldOf("heat").forGetter(BlastFurnaceRecipe::getHeat)
	).apply(instance, (ingredients, outputs, power, time, heat) -> new BlastFurnaceRecipe(type, ingredients, outputs, power, time, heat)));
	public static Function<RecipeType<BlastFurnaceRecipe>, StreamCodec<RegistryFriendlyByteBuf, BlastFurnaceRecipe>> PACKET_CODEC = type -> StreamCodec.composite(
		SizedIngredient.PACKET_CODEC.apply(ByteBufCodecs.list()), RebornRecipe::ingredients,
		ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), RebornRecipe::outputs,
		ByteBufCodecs.INT, RebornRecipe::power,
		ByteBufCodecs.INT, RebornRecipe::time,
		ByteBufCodecs.INT, BlastFurnaceRecipe::getHeat,
		(ingredients, outputs, power, time, heat) -> new BlastFurnaceRecipe(type, ingredients, outputs, power, time, heat)
	);

	@Override
	public List<RecipeDisplay> display() {
		ItemStack stack = new ItemStack(TRContent.Machine.INDUSTRIAL_BLAST_FURNACE);
		return List.of(new RebornRecipeDisplay(new SlotDisplay.ItemStackSlotDisplay(stack)));
	}

	public int getHeat() {
		return heat;
	}

	@Override
	public boolean canCraft(final BlockEntity blockEntity) {
		if (blockEntity instanceof final IndustrialBlastFurnaceBlockEntity blastFurnace) {
			return blastFurnace.getHeat() >= heat;
		}
		return false;
	}

}
