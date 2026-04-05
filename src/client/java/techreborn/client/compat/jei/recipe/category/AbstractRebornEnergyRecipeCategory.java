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

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import net.minecraft.network.chat.Component;
import reborncore.common.crafting.RebornRecipe;
import techreborn.client.compat.jei.gui.render.EnergyDisplayDrawable;

public abstract class AbstractRebornEnergyRecipeCategory<R extends RebornRecipe> extends AbstractRebornRecipeCategory<R> {

	public AbstractRebornEnergyRecipeCategory(IRecipeHolderType<R> recipeType) {
		super(recipeType);
	}

	public AbstractRebornEnergyRecipeCategory(IRecipeHolderType<R> recipeType, Component title) {
		super(recipeType, title);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, R recipe, IFocusGroup focuses) {
		builder.addDrawable(new EnergyDisplayDrawable(recipe), 3, 3);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, R recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		// TODO 26.1 can we just use drawMultiEnergyBar in EnergyDisplayDrawable?
//		if(EnergyDisplayDrawable.isMouseOver(3, 3, mouseX, mouseY)) {
//			tooltip.addAll(List.of(
//					Component.translatable("techreborn.jei.recipe.energy"),
//					Component.translatable("techreborn.jei.recipe.running.cost", "E", recipe.power()).withStyle(ChatFormatting.GRAY),
//					Component.translatable("techreborn.jei.recipe.generator.total", recipe.power() * recipe.time()).withStyle(ChatFormatting.GRAY),
//					Component.literal(jeiHelpers().getModIdHelper().getFormattedModNameForModId("techreborn"))));
//		}
	}
}
