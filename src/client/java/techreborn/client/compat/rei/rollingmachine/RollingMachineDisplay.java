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

package techreborn.client.compat.rei.rollingmachine;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.Registries;
import reborncore.common.crafting.RebornRecipe;
import techreborn.init.ModRecipes;
import techreborn.recipe.recipes.RollingMachineRecipe;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

public class RollingMachineDisplay extends DefaultCraftingDisplay<RebornRecipe> {

	private final int width;
	private final int height;
	private final int energy;
	private final int time;

	public RollingMachineDisplay(RecipeEntry<RebornRecipe> entry) {
		super(
			EntryIngredients.ofIngredients(entry.value().getIngredients()),
			Collections.singletonList(EntryIngredients.of(entry.value().getResult(BasicDisplay.registryAccess()))),
			Optional.of(entry)
		);
		RollingMachineRecipe recipe = (RollingMachineRecipe) entry.value();
		ShapedRecipe shapedRecipe = recipe.getShapedRecipe();
		this.energy = recipe.power();
		this.time = recipe.time();
		this.width = shapedRecipe.getWidth();
		this.height = shapedRecipe.getHeight();
	}

	public int getEnergy() {
		return energy;
	}

	public int getTime() {
		return time;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return CategoryIdentifier.of(Objects.requireNonNull(Registries.RECIPE_TYPE.getId(ModRecipes.ROLLING_MACHINE)));
	}
}
