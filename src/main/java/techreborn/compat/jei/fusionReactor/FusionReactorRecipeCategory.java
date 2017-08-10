/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.compat.jei.fusionReactor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import techreborn.client.gui.GuiFusionReactor;
import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.lib.ModInfo;

import javax.annotation.Nonnull;

public class FusionReactorRecipeCategory extends BlankRecipeCategory<FusionReactorRecipeWrapper> {

	private static final int inputSlotTop = 0;
	private static final int inputSlotBottom = 1;
	private static final int outputSlot = 2;

	@Nonnull
	private final IDrawable background;
	@Nonnull
	private final String title;

	public FusionReactorRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiFusionReactor.texture, 86, 16, 85, 64, 0, 40, 20, 20);
		title = I18n.translateToLocal("tile.techreborn.fusioncontrolcomputer.name");
	}

	@Override
	public String getModName() {
		return ModInfo.MOD_NAME;
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.FUSION_REACTOR;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return title;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(
		@Nonnull
			IRecipeLayout recipeLayout,
		@Nonnull
			FusionReactorRecipeWrapper recipeWrapper,
		@Nonnull
			IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		itemStacks.init(inputSlotTop, true, 21, 0);
		itemStacks.init(inputSlotBottom, true, 21, 36);
		itemStacks.init(outputSlot, false, 81, 18);

		itemStacks.set(inputSlotTop, recipeWrapper.getTopInput());
		itemStacks.set(inputSlotBottom, recipeWrapper.getBottomInput());
		itemStacks.set(outputSlot, ingredients.getOutputs(ItemStack.class).get(0));
	}

}
