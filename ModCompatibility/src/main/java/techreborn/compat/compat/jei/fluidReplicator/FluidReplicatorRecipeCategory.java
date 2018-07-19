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

package techreborn.compat.compat.jei.fluidReplicator;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
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

/**
 * @author drcrazy
 *
 */
public class FluidReplicatorRecipeCategory implements IRecipeCategory<FluidReplicatorRecipeWrapper> {
	public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/jei.png");
	private static final int[] OUTPUT_TANKS = { 0 };
	private static final int[] INPUT_SLOTS = { 0 };
	private final IDrawable background;
	private final IDrawable tankOverlay;
	private final String title;

	public FluidReplicatorRecipeCategory(@Nonnull IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(texture, 125, 0, 72, 60);
		this.tankOverlay = guiHelper.createDrawable(texture, 196, 0, 12, 47);
		this.title = StringUtils.t("tile.techreborn:fluid_replicator.name");
	}

	@Override
	public String getUid() {
		return RecipeCategoryUids.FLUID_REPLICATOR;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getModName() {
		return ModInfo.MOD_NAME;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, FluidReplicatorRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 2, 21);
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		guiFluidStacks.init(OUTPUT_TANKS[0], false, 52, 6, 12, 47, 10_000, true, tankOverlay);
		RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, null, null, OUTPUT_TANKS);
	}

}
