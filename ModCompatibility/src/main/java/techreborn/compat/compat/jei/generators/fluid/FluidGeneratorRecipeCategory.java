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

package techreborn.compat.compat.jei.generators.fluid;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import reborncore.common.util.StringUtils;
import techreborn.api.generator.EFluidGenerator;
import techreborn.compat.compat.jei.RecipeUtil;
import techreborn.lib.ModInfo;

public class FluidGeneratorRecipeCategory implements IRecipeCategory<FluidGeneratorRecipeWrapper> {
	public static ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/jei_fluid_generator.png");

	private static final int[] INPUT_TANKS = { 0 };
	private static final int[] INPUT_SLOTS = { 0 };
	private static final int[] OUTPUT_SLOTS = { 1 };

	private final IDrawable background;
	private final String title;
	private final IDrawable tankOverlay;
	private final EFluidGenerator generatorType;

	public FluidGeneratorRecipeCategory(EFluidGenerator generatorType, IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(texture, 42, 16, 102, 60);
		tankOverlay = guiHelper.createDrawable(texture, 176, 72, 12, 47);
		title = StringUtils.t("techreborn.jei.category.generator." + generatorType.name().toLowerCase());
		this.generatorType = generatorType;
	}

	@Override
	public String getModName() {
		return ModInfo.MOD_NAME;
	}

	@Override
	public String getUid() {
		return this.generatorType.getRecipeID();
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, FluidGeneratorRecipeWrapper recipeWrapper,
	                      IIngredients ingredients) {

		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		guiFluidStacks.init(INPUT_TANKS[0], true, 4, 8, 12, 47, 10000, true, tankOverlay);

		RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, OUTPUT_SLOTS, INPUT_TANKS, null);
	}
}
