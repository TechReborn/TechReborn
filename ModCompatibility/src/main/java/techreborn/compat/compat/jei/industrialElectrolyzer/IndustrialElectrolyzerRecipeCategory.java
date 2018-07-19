/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.compat.compat.jei.industrialElectrolyzer;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.util.ResourceLocation;
import reborncore.common.util.StringUtils;
import techreborn.compat.compat.jei.RecipeCategoryUids;
import techreborn.compat.compat.jei.RecipeUtil;
import techreborn.lib.ModInfo;

import javax.annotation.Nonnull;

public class IndustrialElectrolyzerRecipeCategory implements IRecipeCategory<IndustrialElectrolyzerRecipeWrapper> {
	private static final int[] INPUT_SLOTS = { 0, 1 };
	private static final int[] OUTPUT_SLOTS = { 2, 3, 4, 5 };

	private final IDrawable background;
	private final String title;
	
	private final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/industrial_electrolyzer.png");

	public IndustrialElectrolyzerRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(texture, 49, 18, 78, 50);
		title = StringUtils.t("tile.techreborn:industrial_electrolyzer.name");
	}

	@Override
	public String getModName() {
		return ModInfo.MOD_NAME;
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER;
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
			IndustrialElectrolyzerRecipeWrapper recipeWrapper,
		@Nonnull
			IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 30, 32);
		guiItemStacks.init(INPUT_SLOTS[1], true, 0, 32);
		guiItemStacks.init(OUTPUT_SLOTS[0], false, 0, 0);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 20, 0);
		guiItemStacks.init(OUTPUT_SLOTS[2], false, 40, 0);
		guiItemStacks.init(OUTPUT_SLOTS[3], false, 60, 0);

		RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
	}
}
