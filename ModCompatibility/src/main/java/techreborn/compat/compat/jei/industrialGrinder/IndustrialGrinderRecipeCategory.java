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

package techreborn.compat.compat.jei.industrialGrinder;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import reborncore.common.util.StringUtils;
import techreborn.compat.compat.jei.RecipeCategoryUids;
import techreborn.compat.compat.jei.RecipeUtil;
import techreborn.lib.ModInfo;
import techreborn.tiles.multiblock.TileIndustrialGrinder;

import javax.annotation.Nonnull;

public class IndustrialGrinderRecipeCategory implements IRecipeCategory<IndustrialGrinderRecipeWrapper> {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
		"textures/gui/industrial_grinder.png");
	private static final int[] INPUT_SLOTS = { 0, 1 };
	private static final int[] OUTPUT_SLOTS = { 2, 3, 4, 5 };
	private static final int[] INPUT_TANKS = { 0 };
	private final IDrawable background;
	// for covering the lightning power symbol
	private final IDrawable blankArea; 
	private final IDrawable tankOverlay;
	private final String title;

	public IndustrialGrinderRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(texture, 7, 15, 141, 55);
		blankArea = guiHelper.createDrawable(texture, 50, 45, 6, 6);
		tankOverlay = guiHelper.createDrawable(texture, 176, 86, 12, 47);
		title = StringUtils.t("tile.techreborn:industrial_grinder.name");
	}

	@Override
	public String getModName() {
		return ModInfo.MOD_NAME;
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.INDUSTRIAL_GRINDER;
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
	public void drawExtras(
		@Nonnull
			Minecraft minecraft) {
		blankArea.draw(minecraft, 129, 49);
	}

	@Override
	public void setRecipe(
		@Nonnull
			IRecipeLayout recipeLayout,
		@Nonnull
			IndustrialGrinderRecipeWrapper recipeWrapper,
		@Nonnull
			IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 24, 10);
		guiItemStacks.init(INPUT_SLOTS[1], true, 24, 28);

		guiItemStacks.init(OUTPUT_SLOTS[0], false, 69, 19);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 87, 19);
		guiItemStacks.init(OUTPUT_SLOTS[2], false, 105, 19);
		guiItemStacks.init(OUTPUT_SLOTS[3], false, 123, 19);

		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		guiFluidStacks.init(INPUT_TANKS[0], true, 4, 4, 12, 47, TileIndustrialGrinder.TANK_CAPACITY, true, tankOverlay);

		RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, OUTPUT_SLOTS, INPUT_TANKS, null);
	}
}
