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

package techreborn.client.compat.rei.machine;

import me.shedaniel.rei.api.client.gui.DisplayRenderer;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.SimpleDisplayRenderer;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import techreborn.client.compat.rei.MachineRecipeDisplay;
import techreborn.client.compat.rei.ReiPlugin;

import java.util.Collections;
import java.util.List;

public abstract class AbstractMachineCategory<R extends RebornRecipe> implements DisplayCategory<MachineRecipeDisplay<R>> {
	private final RecipeType<R> recipeType;

	public AbstractMachineCategory(RecipeType<R> rebornRecipeType) {
		this.recipeType = rebornRecipeType;
	}

	private Identifier id() {
		return Registries.RECIPE_TYPE.getId(recipeType);
	}


	@Override
	public CategoryIdentifier<? extends MachineRecipeDisplay<R>> getCategoryIdentifier() {
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

	@Override
	public DisplayRenderer getDisplayRenderer(MachineRecipeDisplay<R> recipe) {
		return SimpleDisplayRenderer.from(Collections.singletonList(recipe.getInputEntries().get(0)), recipe.getOutputEntries());
	}

	public EntryIngredient getInput(MachineRecipeDisplay<R> recipeDisplay, int index) {
		List<EntryIngredient> inputs = recipeDisplay.getInputEntries();
		return inputs.size() > index ? inputs.get(index) : EntryIngredient.empty();
	}

	public EntryIngredient getOutput(MachineRecipeDisplay<R> recipeDisplay, int index) {
		List<EntryIngredient> outputs = recipeDisplay.getOutputEntries();
		return outputs.size() > index ? outputs.get(index) : EntryIngredient.empty();
	}
}
