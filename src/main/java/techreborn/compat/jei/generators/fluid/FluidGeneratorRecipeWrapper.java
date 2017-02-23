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

package techreborn.compat.jei.generators.fluid;

import javax.annotation.Nonnull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.generator.FluidGeneratorRecipe;

public class FluidGeneratorRecipeWrapper extends BlankRecipeWrapper {

	private static final DecimalFormat formatter;

	static {
		formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);
	}

	private static final int FLUID_GENERATOR_STORAGE = 100_000;

	private final FluidGeneratorRecipe baseRecipe;
	private final IDrawable energyProduced;

	public FluidGeneratorRecipeWrapper(@Nonnull IJeiHelpers jeiHelpers, @Nonnull FluidGeneratorRecipe recipe) {

		this.baseRecipe = recipe;

		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		energyProduced = guiHelper.createDrawable(FluidGeneratorRecipeCategory.texture, 176, 3,
				(int) (25 * ((recipe.getEnergyPerMb() * 1000.0) / FLUID_GENERATOR_STORAGE)), 14);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);

		energyProduced.draw(minecraft, 73, 26);

		minecraft.fontRendererObj.drawString(formatter.format(baseRecipe.getEnergyPerMb() * 1000) + " FE", 70, 13,
				0x444444);
	}

	@Override
	public void getIngredients(final IIngredients ingredients) {
		ingredients.setInput(FluidStack.class, new FluidStack(this.baseRecipe.getFluid(), Fluid.BUCKET_VOLUME));
	}
}
