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

package techreborn.compat.compat.jei.industrialSawmill;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.compat.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * @author drcrazy
 */
public class IndustrialSawmillRecipeWrapper extends BaseRecipeWrapper<IndustrialSawmillRecipe> {
	public static final ResourceLocation texture = new ResourceLocation("techreborn",
		"textures/gui/industrial_sawmill.png");
	private final IDrawableAnimated progress;

	public IndustrialSawmillRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			IndustrialSawmillRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(texture, 176, 14, 20, 13);
		int ticksPerCycle = baseRecipe.tickTime();
		this.progress = guiHelper.createAnimatedDrawable(progressStatic, ticksPerCycle,
			IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void getIngredients(
		@Nonnull
		final IIngredients ingredients) {
		ingredients.setInput(FluidStack.class, this.baseRecipe.fluidStack);
		super.getIngredients(ingredients);
	}

	@Override
	@Nonnull
	public List<FluidStack> getFluidInputs() {
		if (baseRecipe.fluidStack != null) {
			return Collections.singletonList(baseRecipe.fluidStack);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
		progress.draw(minecraft, 48, 23);

		if (minecraft.fontRenderer != null) {
			int x = 70;
			int y = 40;
			int lineHeight = minecraft.fontRenderer.FONT_HEIGHT;

			minecraft.fontRenderer.drawString("Time: " + baseRecipe.tickTime / 20 + " s", x, y, 0x444444);
			minecraft.fontRenderer.drawString("FE: " + baseRecipe.euPerTick + " FE/t", x, y += lineHeight, 0x444444);
		}
	}

}
