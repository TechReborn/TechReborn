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

package techreborn.compat.jei.praescriptum.gasturbine;

import net.minecraft.util.ResourceLocation;

import reborncore.common.util.StringUtils;

import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.compat.jei.RecipeUtil;
import techreborn.lib.ModInfo;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;

import javax.annotation.Nonnull;

/**
 * @author estebes
 */
public class GasTurbineFuelCategory implements IRecipeCategory<GasTurbineFuelWrapper> {
    public GasTurbineFuelCategory(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(texture, 0, 225, 116, 82)
                .setTextureSize(256, 512)
                .build();
        tankOverlay = guiHelper.createDrawable(texture, 176, 72, 12, 47);
        title = StringUtils.t("techreborn.jei.category.generator.gas");
    }

    @Override
    public String getModName() {
        return ModInfo.MOD_NAME;
    }

    @Nonnull
    @Override
    public String getUid() {
        return RecipeCategoryUids.GAS_TURBINE;
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
    public void setRecipe(IRecipeLayout recipeLayout, GasTurbineFuelWrapper fuelWrapper, IIngredients ingredients) {
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        guiFluidStacks.init(INPUT_TANKS[0], true, 25, 20, 16, 16, 1, false, tankOverlay);

        RecipeUtil.setRecipeItems(recipeLayout, ingredients, null, null, INPUT_TANKS, null);
    }

    // Fields >>
    public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/jei3.png");

    private static final int[] INPUT_TANKS = {0};

    private final IDrawable background;
    private final IDrawable tankOverlay;
    private final String title;
    // << Fields
}
