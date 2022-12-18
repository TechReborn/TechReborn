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

import io.github.cottonmc.libcd.api.CustomOutputRecipe;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import reborncore.RebornCore;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.util.DefaultedListCollector;

import java.util.*;

public class RebornRecipe implements Recipe<Inventory>, CustomOutputRecipe {
	private final RebornRecipeType<?> type;
	private final Identifier name;

	private final List<RebornIngredient> ingredients;
	private final List<ItemStack> outputs;
	protected final int power;
	protected final int time;

	public RebornRecipe(RebornRecipeType<?> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time) {
		this.type = type;
		this.name = name;
		this.ingredients = ingredients;
		this.outputs = outputs;
		this.power = power;
		this.time = time;
	}

	@Override
	public ItemStack createIcon() {
		Optional<Item> catalyst = Registries.ITEM.getOrEmpty(type.name());
		if (catalyst.isPresent())
			return new ItemStack(catalyst.get());
		else
			RebornCore.LOGGER.warn("Missing toast icon for {}!", type.name());
		return Recipe.super.createIcon();
	}

	@Override
	public Identifier getId() {
		return name;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return type;
	}

	@Override
	public net.minecraft.recipe.RecipeType<?> getType() {
		return type;
	}

	public RebornRecipeType<?> getRebornRecipeType() {
		return type;
	}

	/**
	 * use the {@link RebornIngredient} version to ensure stack sizes are checked
	 */
	@Deprecated
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return ingredients.stream().map(RebornIngredient::getPreview).collect(DefaultedListCollector.toList());
	}

	public List<RebornIngredient> getRebornIngredients() {
		return ingredients;
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
	public boolean matches(Inventory inv, World worldIn) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public ItemStack craft(Inventory inv) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public boolean fits(int width, int height) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Do not call directly, this is implemented only as a fallback.
	 * {@link RebornRecipe#getOutputs()} will return all the outputs
	 */
	@Deprecated
	@Override
	public ItemStack getOutput() {
		if (outputs.isEmpty()) {
			return ItemStack.EMPTY;
		}
		return outputs.get(0);
	}

	@Override
	public DefaultedList<ItemStack> getRemainder(Inventory p_179532_1_) {
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
