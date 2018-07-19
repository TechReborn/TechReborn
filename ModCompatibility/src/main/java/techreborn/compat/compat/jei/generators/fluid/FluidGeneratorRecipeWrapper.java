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
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.api.generator.FluidGeneratorRecipe;

import javax.annotation.Nonnull;

public class FluidGeneratorRecipeWrapper implements IRecipeWrapper {

	private final FluidGeneratorRecipe baseRecipe;
	private final IDrawableAnimated progress;

	public FluidGeneratorRecipeWrapper(@Nonnull IJeiHelpers jeiHelpers, @Nonnull FluidGeneratorRecipe recipe) {
		this.baseRecipe = recipe;
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(FluidGeneratorRecipeCategory.texture, 176, 3, 25, 14);
		this.progress = guiHelper.createAnimatedDrawable(progressStatic, 200, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		progress.draw(minecraft, 73, 26);
		minecraft.fontRenderer
				.drawString(PowerSystem.getLocaliszedPowerFormattedNoSuffix(baseRecipe.getEnergyPerMb() * 1000) + " "
						+ PowerSystem.getDisplayPower().abbreviation, 70, 13, 0x444444);
	}

	@Override
	public void getIngredients(final IIngredients ingredients) {
		ingredients.setInput(FluidStack.class, new FluidStack(this.baseRecipe.getFluid(), Fluid.BUCKET_VOLUME));
	}
}
