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

import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.categories.crafting.DefaultCraftingCategory;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import techreborn.recipe.recipes.RollingMachineRecipe;
import techreborn.client.compat.rei.ReiPlugin;

public class RollingMachineCategory extends DefaultCraftingCategory {

	private final RecipeType<RollingMachineRecipe> recipeType;

	public RollingMachineCategory(RecipeType<RollingMachineRecipe> recipeType) {
		this.recipeType = recipeType;
	}

	private Identifier id() {
		return Registries.RECIPE_TYPE.getId(recipeType);
	}


	@Override
	public CategoryIdentifier<? extends DefaultCraftingDisplay<?>> getCategoryIdentifier() {
		return CategoryIdentifier.of(id());
	}

	@Override
	public Text getTitle() {
		return Text.translatable(id().toString());
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(ReiPlugin.iconMap.getOrDefault(recipeType, () -> Items.DIAMOND_SHOVEL));
	}

}
