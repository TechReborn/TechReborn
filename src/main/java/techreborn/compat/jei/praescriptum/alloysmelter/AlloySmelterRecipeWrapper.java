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

package techreborn.compat.jei.praescriptum.alloysmelter;

import net.minecraft.client.Minecraft;

import reborncore.api.praescriptum.recipes.Recipe;
import reborncore.client.guibuilder.GuiBuilder;
import reborncore.client.guibuilder.GuiBuilder.ProgressDirection;
import reborncore.common.powerSystem.PowerSystem;

import techreborn.compat.jei.RecipeWrapper;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;

public class AlloySmelterRecipeWrapper extends RecipeWrapper {
    public AlloySmelterRecipeWrapper(IJeiHelpers jeiHelpers, Recipe recipe) {
        super(recipe);

        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        ProgressDirection right = ProgressDirection.RIGHT;
        ProgressDirection left = ProgressDirection.LEFT;

        IDrawableStatic progressrightStatic = guiHelper.createDrawable(GuiBuilder.defaultTextureSheet, right.xActive,
                right.yActive, right.width, right.height);
        IDrawableStatic progressleftStatic = guiHelper.createDrawable(GuiBuilder.defaultTextureSheet, left.xActive,
                left.yActive, left.width, left.height);

        int ticksPerCycle = recipe.getOperationDuration(); // speed up the animation
        this.progressright = guiHelper.createAnimatedDrawable(progressrightStatic, ticksPerCycle,
                IDrawableAnimated.StartDirection.LEFT, false);
        this.progressleft = guiHelper.createAnimatedDrawable(progressleftStatic, ticksPerCycle,
                IDrawableAnimated.StartDirection.RIGHT, false);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);

        progressright.draw(minecraft, 25, 12);
        progressleft.draw(minecraft, 75, 12);

        int y = 31;
        int lineHeight = minecraft.fontRenderer.FONT_HEIGHT;

        minecraft.fontRenderer.drawString(recipe.getOperationDuration() / 20 + " seconds",
                (recipeWidth / 2 - minecraft.fontRenderer.getStringWidth(recipe.getOperationDuration() / 20 + " seconds") / 2), y, 0x444444);
        minecraft.fontRenderer.drawString(PowerSystem.getLocaliszedPowerFormatted(recipe.getEnergyCostPerTick() * recipe.getOperationDuration()),
                (recipeWidth / 2 - minecraft.fontRenderer.getStringWidth(PowerSystem.getLocaliszedPowerFormatted(recipe.getEnergyCostPerTick() * recipe.getOperationDuration())) / 2),
                y + lineHeight + 1, 0x444444);
    }

    // Fields >>
    private final IDrawableAnimated progressright;
    private final IDrawableAnimated progressleft;
    //  << Fields
}
