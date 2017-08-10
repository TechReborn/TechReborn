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

package techreborn.compat.jei.blastFurnace;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.compat.jei.RecipeUtil;
import techreborn.lib.ModInfo;

import javax.annotation.Nonnull;

public class BlastFurnaceRecipeCategory extends BlankRecipeCategory<BlastFurnaceRecipeWrapper> {
	public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/jei.png");
	private static final int[] INPUT_SLOTS = { 0, 1 };
	private static final int[] OUTPUT_SLOTS = { 2, 3 };

	private final IDrawable background;
	private final String title;

	public BlastFurnaceRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(texture, 0, 94, 120, 78);
		title = I18n.translateToLocal("tile.techreborn.blastfurnace.name");
	}

	@Override
	public String getModName() {
		return ModInfo.MOD_NAME;
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.BLAST_FURNACE;
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
			BlastFurnaceRecipeWrapper recipeWrapper,
		@Nonnull
			IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 21, 3);
		guiItemStacks.init(INPUT_SLOTS[1], true, 21, 23);
		guiItemStacks.init(OUTPUT_SLOTS[0], false, 63, 12);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 81, 12);

		RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
	}
}
