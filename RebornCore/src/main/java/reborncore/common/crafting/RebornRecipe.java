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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import reborncore.RebornCore;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.common.util.DefaultedListCollector;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface RebornRecipe extends Recipe<RebornRecipeInput> {
	Function<RecipeType<RebornRecipe>, MapCodec<RebornRecipe>> CODEC = type -> RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.list(SizedIngredient.CODEC.codec()).fieldOf("ingredients").forGetter(RebornRecipe::ingredients),
		Codec.list(ItemStack.CODEC).fieldOf("outputs").forGetter(RebornRecipe::outputs),
		Codecs.POSITIVE_INT.fieldOf("power").forGetter(RebornRecipe::power),
		Codecs.POSITIVE_INT.fieldOf("time").forGetter(RebornRecipe::time)
	).apply(instance, (ingredients, outputs, power, time) -> new Default(type, ingredients, outputs, power, time)));
	Function<RecipeType<RebornRecipe>, PacketCodec<RegistryByteBuf, RebornRecipe>> PACKET_CODEC = type -> PacketCodec.tuple(
		SizedIngredient.PACKET_CODEC.collect(PacketCodecs.toList()), RebornRecipe::ingredients,
		ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()), RebornRecipe::outputs,
		PacketCodecs.INTEGER, RebornRecipe::power,
		PacketCodecs.INTEGER, RebornRecipe::time,
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
	default List<RecipeDisplay> getDisplays() {
		Identifier typeId = Registries.RECIPE_TYPE.getId(type());
		Optional<Item> catalyst = Registries.ITEM.getOptionalValue(typeId);

		if (catalyst.isPresent()) {
			ItemStack stack = new ItemStack(catalyst.get());
			return List.of(new RebornRecipeDisplay(new SlotDisplay.StackSlotDisplay(stack)));
		}

		RebornCore.LOGGER.warn("Missing toast icon for {}!", typeId);
		return Recipe.super.getDisplays();
	}

	@Override
	default RecipeSerializer<? extends RebornRecipe> getSerializer() {
		return (RecipeSerializer<? extends RebornRecipe>) Registries.RECIPE_SERIALIZER.get(Registries.RECIPE_TYPE.getId(getType()));
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
	default IngredientPlacement getIngredientPlacement() {
		return IngredientPlacement.NONE;
	}

	// Done as our recipes do not support these functions, hopefully nothing blindly calls them
	@Deprecated
	@Override
	default boolean matches(RebornRecipeInput inv, World worldIn) {
		throw new UnsupportedOperationException();
	}

	@Override
	default ItemStack craft(RebornRecipeInput inventory, RegistryWrapper.WrapperLookup lookup) {
		throw new UnsupportedOperationException();
	}

	@Override
	default RecipeBookCategory getRecipeBookCategory() {
		throw new UnsupportedOperationException();
	}

	// Done to try and stop the table from loading it
	@Override
	default boolean isIgnoredInRecipeBook() {
		return true;
	}
}
