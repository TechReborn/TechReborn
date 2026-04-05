/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.client.compat.jei.recipe.category;

import java.util.List;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import reborncore.common.crafting.RebornFluidRecipe;
import techreborn.client.compat.jei.gui.render.EnergyDisplayDrawable;
import techreborn.client.compat.jei.gui.render.ProgressBarDrawable;

public class ItemFluidToFourItemRecipeCategory<R extends RebornFluidRecipe> extends AbstractRebornRecipeCategory<R> {

	public ItemFluidToFourItemRecipeCategory(IRecipeHolderType<R> recipeType) {
		super(recipeType);
	}

	public ItemFluidToFourItemRecipeCategory(IRecipeHolderType<R> recipeType, Component title) {
		super(recipeType, title);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, R recipe, IFocusGroup focuses) {
		addItem(builder, RecipeIngredientRole.INPUT, 50, 28, getInput(recipe, 0), SLOT);
		addFluid(builder, RecipeIngredientRole.INPUT, 24, 11, recipe.fluid());
		addItem(builder, RecipeIngredientRole.OUTPUT, 96, 1, getOutput(recipe, 0), SLOT);
		addItem(builder, RecipeIngredientRole.OUTPUT, 96, 19, getOutput(recipe, 1), SLOT);
		addItem(builder, RecipeIngredientRole.OUTPUT, 96, 37, getOutput(recipe, 2), SLOT);
		addItem(builder, RecipeIngredientRole.OUTPUT, 96, 55, getOutput(recipe, 3), SLOT);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, R recipe, IFocusGroup focuses) {
		builder.addDrawable(new EnergyDisplayDrawable(recipe), 3, 11);
		builder.addDrawable(ProgressBarDrawable.right(recipe), 73, 31);
	}

	@Override
	public void draw(R recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
		Font font = font();
		Component component = getTimeComponent(recipe);
		guiGraphics.text(font, component, 46, 0, 0xFF808080, false);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, R recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if(EnergyDisplayDrawable.isMouseOver(3, 11, mouseX, mouseY)) {
			tooltip.addAll(List.of(
					Component.translatable("techreborn.jei.recipe.energy"),
					Component.translatable("techreborn.jei.recipe.running.cost", "E", recipe.power()).withStyle(ChatFormatting.GRAY),
					Component.translatable("techreborn.jei.recipe.generator.total", recipe.power() * recipe.time()).withStyle(ChatFormatting.GRAY),
					Component.literal(jeiHelpers().getModIdHelper().getFormattedModNameForModId("techreborn"))));
		}
	}

	@Override
	public int getHeight() {
		return 72;
	}
}
