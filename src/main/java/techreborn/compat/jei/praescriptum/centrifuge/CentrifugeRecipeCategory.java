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

package techreborn.compat.jei.praescriptum.centrifuge;

import net.minecraft.util.ResourceLocation;

import reborncore.common.util.StringUtils;

import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.compat.jei.RecipeUtil;
import techreborn.lib.ModInfo;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;

import javax.annotation.Nonnull;

/**
 * @author estebes
 */
public class CentrifugeRecipeCategory implements IRecipeCategory<CentrifugeRecipeWrapper> {
    public CentrifugeRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(texture, 0, 308, 116, 88)
                .setTextureSize(256, 512)
                .build();
        title = StringUtils.t("tile.techreborn.industrial_centrifuge.name");
    }

    @Override
    public String getModName() {
        return ModInfo.MOD_NAME;
    }

    @Nonnull
    @Override
    public String getUid() {
        return RecipeCategoryUids.CENTRIFUGE;
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
    public void setRecipe(IRecipeLayout recipeLayout, CentrifugeRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(INPUT_SLOTS[0], true, 9, 12);
        guiItemStacks.init(INPUT_SLOTS[1], true, 9, 32);
        guiItemStacks.init(OUTPUT_SLOTS[0], false, 51, 22);
        guiItemStacks.init(OUTPUT_SLOTS[1], false, 70, 3);
        guiItemStacks.init(OUTPUT_SLOTS[2], false, 89, 22);
        guiItemStacks.init(OUTPUT_SLOTS[3], false, 70, 41);

        RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
    }

    // Fields >>
    public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/jei3.png");

    private static final int[] INPUT_SLOTS = {0, 1};
    private static final int[] OUTPUT_SLOTS = {2, 3, 4, 5};

    private final IDrawable background;
    private final String title;
    // << Fields
}
