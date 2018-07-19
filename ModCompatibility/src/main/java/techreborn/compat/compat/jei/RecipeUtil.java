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

package techreborn.compat.compat.jei;

import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;


public class RecipeUtil {
	private static final int color = Color.darkGray.getRGB();

	private RecipeUtil() {
	}

	public static void drawInfo(
		@Nonnull
			Minecraft minecraft, int x, int y, final double startCost,
		final double euPerTick, final int tickTime) {
		FontRenderer fontRenderer = minecraft.fontRenderer;
		int lineSpacing = fontRenderer.FONT_HEIGHT + 1;

		String startCostEU = PowerSystem.getLocaliszedPower(startCost);
		//String startCostString = I18n.translateToLocalFormatted("techreborn.jei.recipe.start.cost", startCostEU);
		String startCostString = StringUtils.t("techreborn.jei.recipe.start.cost", startCostEU);
		fontRenderer.drawString(startCostString, x, y, color);
		y += lineSpacing;

		drawInfo(minecraft, x, y, euPerTick, tickTime);
	}

	public static void drawInfo(
		@Nonnull
			Minecraft minecraft, int x, int y, final double euPerTick, final int tickTime) {
		FontRenderer fontRenderer = minecraft.fontRenderer;
		int lineSpacing = fontRenderer.FONT_HEIGHT + 1;

		String runningCostString = StringUtils.t("techreborn.jei.recipe.running.cost", PowerSystem.getDisplayPower().abbreviation.toUpperCase(),  PowerSystem.getLocaliszedPowerFormattedNoSuffix(euPerTick));
		//String runningCostString = I18n.translateToLocalFormatted("techreborn.jei.recipe.running.cost",PowerSystem.getDisplayPower().abbreviation.toUpperCase(),  PowerSystem.getLocaliszedPowerFormattedNoSuffix(euPerTick));
		fontRenderer.drawString(runningCostString, x, y, color);
		y += lineSpacing;

		// String processingTimeString1 = I18n.translateToLocalFormatted("techreborn.jei.recipe.processing.time.1", tickTime);
		String processingTimeString1 = StringUtils.t("techreborn.jei.recipe.processing.time.1", tickTime);
		fontRenderer.drawString(processingTimeString1, x, y, color);
		y += lineSpacing;

		int seconds = tickTime / 20;
		//String processingTimeString2 = I18n.translateToLocalFormatted("techreborn.jei.recipe.processing.time.2", seconds);
		String processingTimeString2 = StringUtils.t("techreborn.jei.recipe.processing.time.2", seconds);
		fontRenderer.drawString(processingTimeString2, x + 10, y, color);
	}

	@Deprecated
	public static void setRecipeItems(
		@Nonnull
			IRecipeLayout recipeLayout,
		@Nonnull
			BaseRecipeWrapper<?> recipe,
		@Nullable
			int[] itemInputSlots,
		@Nullable
			int[] itemOutputSlots,
		@Nullable
			int[] fluidInputSlots,
		@Nullable
			int[] fluidOutputSlots) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		if (itemInputSlots != null) {
			List<List<ItemStack>> inputs = recipe.getInputs();
			for (int i = 0; i < inputs.size() && i < itemInputSlots.length; i++) {
				int inputSlot = itemInputSlots[i];
				guiItemStacks.set(inputSlot, inputs.get(i));
			}
		}

		if (itemOutputSlots != null) {
			List<ItemStack> outputs = recipe.getOutputs();
			for (int i = 0; i < outputs.size() && i < itemOutputSlots.length; i++) {
				int outputSlot = itemOutputSlots[i];
				guiItemStacks.set(outputSlot, outputs.get(i));
			}
		}

		if (fluidInputSlots != null) {
			List<FluidStack> fluidInputs = recipe.getFluidInputs();
			for (int i = 0; i < fluidInputs.size() && i < fluidInputSlots.length; i++) {
				int inputTank = fluidInputSlots[i];
				guiFluidStacks.set(inputTank, fluidInputs.get(i));
			}
		}

		//TODO 1.11
		//		if (fluidOutputSlots != null) {
		//			List<FluidStack> fluidOutputs = recipe.getFluidOutputs();
		//			for (int i = 0; i < fluidOutputs.size() && i < fluidOutputSlots.length; i++) {
		//				int outputTank = fluidOutputSlots[i];
		//				guiFluidStacks.set(outputTank, fluidOutputs.get(i));
		//			}
		//		}
	}

	public static void setRecipeItems(
		@Nonnull
			IRecipeLayout recipeLayout,
		@Nonnull
			IIngredients ingredients,
		@Nullable
			int[] itemInputSlots,
		@Nullable
			int[] itemOutputSlots,
		@Nullable
			int[] fluidInputSlots,
		@Nullable
			int[] fluidOutputSlots) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		if (itemInputSlots != null) {
			List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
			for (int i = 0; i < inputs.size() && i < itemInputSlots.length; i++) {
				int inputSlot = itemInputSlots[i];
				guiItemStacks.set(inputSlot, inputs.get(i));
			}
		}

		if (itemOutputSlots != null) {
			List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
			for (int i = 0; i < outputs.size() && i < itemOutputSlots.length; i++) {
				int outputSlot = itemOutputSlots[i];
				guiItemStacks.set(outputSlot, outputs.get(i));
			}
		}

		if (fluidInputSlots != null) {
			List<List<FluidStack>> fluidInputs = ingredients.getInputs(FluidStack.class);
			for (int i = 0; i < fluidInputs.size() && i < fluidInputSlots.length; i++) {
				int inputTank = fluidInputSlots[i];
				guiFluidStacks.set(inputTank, fluidInputs.get(i));
			}
		}

		if (fluidOutputSlots != null) {
			List<List<FluidStack>> fluidOutputs = ingredients.getOutputs(FluidStack.class);
			for (int i = 0; i < fluidOutputs.size() && i < fluidOutputSlots.length; i++) {
				int outputTank = fluidOutputSlots[i];
				guiFluidStacks.set(outputTank, fluidOutputs.get(i));
			}
		}
	}
}
