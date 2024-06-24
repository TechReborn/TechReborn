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
import io.github.cottonmc.libcd.api.CustomOutputRecipe;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.RebornCore;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.common.util.DefaultedListCollector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class RebornRecipe implements Recipe<RebornRecipeInput>, CustomOutputRecipe {
	public static Function<RecipeType<RebornRecipe>, MapCodec<RebornRecipe>> CODEC = type -> RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.list(SizedIngredient.CODEC.codec()).fieldOf("ingredients").forGetter(RebornRecipe::getSizedIngredients),
		Codec.list(ItemStack.CODEC).fieldOf("outputs").forGetter(RebornRecipe::getOutputs),
		Codecs.POSITIVE_INT.fieldOf("power").forGetter(RebornRecipe::getPower),
		Codecs.POSITIVE_INT.fieldOf("time").forGetter(RebornRecipe::getTime)
	).apply(instance, (ingredients, outputs, power, time) -> new RebornRecipe(type, ingredients, outputs, power, time)));
	public static Function<RecipeType<RebornRecipe>, PacketCodec<RegistryByteBuf, RebornRecipe>> PACKET_CODEC = type -> PacketCodec.tuple(
		SizedIngredient.PACKET_CODEC.collect(PacketCodecs.toList()), RebornRecipe::getSizedIngredients,
		ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()), RebornRecipe::getOutputs,
		PacketCodecs.INTEGER, RebornRecipe::getPower,
		PacketCodecs.INTEGER, RebornRecipe::getTime,
		(ingredients, outputs, power, time) -> new RebornRecipe(type, ingredients, outputs, power, time)
	);

	private final RecipeType<?> type;

	private final List<SizedIngredient> ingredients;
	private final List<ItemStack> outputs;
	protected final int power;
	protected final int time;

	private final DefaultedList<Ingredient> baseIngredients;

	public RebornRecipe(RecipeType<?> type, List<SizedIngredient> ingredients, List<ItemStack> outputs, int power, int time) {
		this.type = type;
		this.ingredients = ingredients;
		this.outputs = outputs;
		this.power = power;
		this.time = time;
		this.baseIngredients = ingredients.stream().map(SizedIngredient::ingredient).collect(DefaultedListCollector.toList());
	}

	@Override
	public ItemStack createIcon() {
		Identifier typeId = Registries.RECIPE_TYPE.getId(this.type);
		Optional<Item> catalyst = Registries.ITEM.getOrEmpty(typeId);

		if (catalyst.isPresent()) {
			return new ItemStack(catalyst.get());
		}

		RebornCore.LOGGER.warn("Missing toast icon for {}!", typeId);
		return Recipe.super.createIcon();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Registries.RECIPE_SERIALIZER.get(Registries.RECIPE_TYPE.getId(getType()));
	}

	@Override
	public RecipeType<?> getType() {
		return type;
	}

	/**
	 * use the {@link SizedIngredient} version to ensure stack sizes are checked
	 */
	@Deprecated
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return baseIngredients;
	}

	public List<SizedIngredient> getSizedIngredients() {
		return ingredients;
	}

	public List<ItemStack> getOutputs(@Nullable DynamicRegistryManager registryManager) {
		return Collections.unmodifiableList(outputs);
	}

	public List<ItemStack> getOutputs() {
		return Collections.unmodifiableList(outputs);
	}

	public int getPower() {
		return power;
	}

	public int getTime() {
		return time;
	}

	/**
	 * @param blockEntity {@link BlockEntity} The blockEntity that is doing the crafting
	 * @return {@code boolean} If true, the recipe will craft, if false it will not
	 */
	public boolean canCraft(BlockEntity blockEntity) {
		if (blockEntity instanceof IRecipeCrafterProvider) {
			return ((IRecipeCrafterProvider) blockEntity).canCraft(this);
		}
		return true;
	}

	/**
	 * @param blockEntity {@link BlockEntity} The blockEntity that is doing the crafting
	 * @return {@code boolean} Returns true if fluid was taken and should craft
	 */
	public boolean onCraft(BlockEntity blockEntity) {
		return true;
	}

	// Done as our recipes do not support these functions, hopefully nothing blindly calls them
	@Deprecated
	@Override
	public boolean matches(RebornRecipeInput inv, World worldIn) {
		throw new UnsupportedOperationException();
	}


	@Override
	public ItemStack craft(RebornRecipeInput inventory, RegistryWrapper.WrapperLookup lookup) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public boolean fits(int width, int height) {
		throw new UnsupportedOperationException();
	}


	/**
	 * Do not call directly, this is implemented only as a fallback.
	 * {@link RebornRecipe#getOutputs(DynamicRegistryManager)} will return all the outputs
	 */
	@Deprecated
	@Override
	public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
		if (outputs.isEmpty()) {
			return ItemStack.EMPTY;
		}
		return outputs.get(0);
	}

	@Override
	public DefaultedList<ItemStack> getRemainder(RebornRecipeInput input) {
		throw new UnsupportedOperationException();
	}

	// Done to try and stop the table from loading it
	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}

	@Override
	public Collection<Item> getOutputItems() {
		List<Item> items = new ArrayList<>();
		for (ItemStack stack : outputs) {
			items.add(stack.getItem());
		}
		return items;
	}
}
