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
import org.jetbrains.annotations.ApiStatus;
import reborncore.RebornCore;
import reborncore.api.recipe.IRecipeCrafterProvider;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface RebornRecipe extends Recipe<RebornRecipeInput> {
	Function<RecipeType<RebornRecipe>, MapCodec<RebornRecipe>> CODEC = type -> RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.list(SizedIngredient.CODEC.codec()).fieldOf("ingredients").forGetter(RebornRecipe::ingredients),
		Codec.list(ItemStack.CODEC).fieldOf("outputs").forGetter(RebornRecipe::outputs),
		ExtraCodecs.POSITIVE_INT.fieldOf("power").forGetter(RebornRecipe::power),
		ExtraCodecs.POSITIVE_INT.fieldOf("time").forGetter(RebornRecipe::time)
	).apply(instance, (ingredients, outputs, power, time) -> new Default(type, ingredients, outputs, power, time)));
	Function<RecipeType<RebornRecipe>, StreamCodec<RegistryFriendlyByteBuf, RebornRecipe>> PACKET_CODEC = type -> StreamCodec.composite(
		SizedIngredient.PACKET_CODEC.apply(ByteBufCodecs.list()), RebornRecipe::ingredients,
		ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), RebornRecipe::outputs,
		ByteBufCodecs.INT, RebornRecipe::power,
		ByteBufCodecs.INT, RebornRecipe::time,
		(ingredients, outputs, power, time) -> new Default(type, ingredients, outputs, power, time)
	);

	@ApiStatus.Internal
	record Default(RecipeType<? extends RebornRecipe> type, List<SizedIngredient> ingredients, List<ItemStack> outputs, int power, int time) implements RebornRecipe {
	}

	RecipeType<? extends RebornRecipe> type();
	List<SizedIngredient> ingredients();
	List<ItemStack> outputs();
	int power();
	int time();

	@Override
	default List<RecipeDisplay> display() {
		Identifier typeId = BuiltInRegistries.RECIPE_TYPE.getKey(type());
		Optional<Item> catalyst = BuiltInRegistries.ITEM.getOptional(typeId);

		if (catalyst.isPresent()) {
			ItemStack stack = new ItemStack(catalyst.get());
			return List.of(new RebornRecipeDisplay(new SlotDisplay.ItemStackSlotDisplay(stack)));
		}

		RebornCore.LOGGER.warn("Missing toast icon for {}!", typeId);
		return Recipe.super.display();
	}

	@Override
	default RecipeSerializer<? extends RebornRecipe> getSerializer() {
		return (RecipeSerializer<? extends RebornRecipe>) BuiltInRegistries.RECIPE_SERIALIZER.getValue(BuiltInRegistries.RECIPE_TYPE.getKey(getType()));
	}

	@Override
	default RecipeType<? extends RebornRecipe> getType() {
		return type();
	}

	/**
	 * @param blockEntity {@link BlockEntity} The blockEntity that is doing the crafting
	 * @return {@code boolean} If true, the recipe will craft, if false it will not
	 */
	default boolean canCraft(BlockEntity blockEntity) {
		if (blockEntity instanceof IRecipeCrafterProvider) {
			return ((IRecipeCrafterProvider) blockEntity).canCraft(this);
		}
		return true;
	}

	/**
	 * @param blockEntity {@link BlockEntity} The blockEntity that is doing the crafting
	 * @return {@code boolean} Returns true if fluid was taken and should craft
	 */
	default boolean onCraft(BlockEntity blockEntity) {
		return true;
	}

	@Override
	default PlacementInfo placementInfo() {
		return PlacementInfo.NOT_PLACEABLE;
	}

	// Done as our recipes do not support these functions, hopefully nothing blindly calls them
	@Deprecated
	@Override
	default boolean matches(RebornRecipeInput inv, Level worldIn) {
		throw new UnsupportedOperationException();
	}

	@Override
	default ItemStack assemble(RebornRecipeInput inventory, HolderLookup.Provider lookup) {
		throw new UnsupportedOperationException();
	}

	@Override
	default RecipeBookCategory recipeBookCategory() {
		return null;
	}

	// Done to try and stop the table from loading it
	@Override
	default boolean isSpecial() {
		return true;
	}
}
