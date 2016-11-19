package techreborn.compat.jei;

import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;

public class RecipeUtil {
	private static final int color = Color.darkGray.getRGB();

	private RecipeUtil() {
	}

	public static void drawInfo(
		@Nonnull
			Minecraft minecraft, int x, int y, final double startCost,
		final double euPerTick, final int tickTime) {
		FontRenderer fontRendererObj = minecraft.fontRendererObj;
		int lineSpacing = fontRendererObj.FONT_HEIGHT + 1;

		NumberFormat formatter = NumberFormat.getInstance();
		String startCostEU = formatter.format(startCost);
		String startCostString = I18n.translateToLocalFormatted("techreborn.jei.recipe.start.cost", startCostEU);
		fontRendererObj.drawString(startCostString, x, y, color);
		y += lineSpacing;

		drawInfo(minecraft, x, y, euPerTick, tickTime);
	}

	public static void drawInfo(
		@Nonnull
			Minecraft minecraft, int x, int y, final double euPerTick, final int tickTime) {
		FontRenderer fontRendererObj = minecraft.fontRendererObj;
		int lineSpacing = fontRendererObj.FONT_HEIGHT + 1;

		String runningCostString = I18n.translateToLocalFormatted("techreborn.jei.recipe.running.cost", euPerTick);
		fontRendererObj.drawString(runningCostString, x, y, color);
		y += lineSpacing;

		String processingTimeString1 = I18n.translateToLocalFormatted("techreborn.jei.recipe.processing.time.1",
			tickTime);
		fontRendererObj.drawString(processingTimeString1, x, y, color);
		y += lineSpacing;

		int seconds = tickTime / 20;
		String processingTimeString2 = I18n.translateToLocalFormatted("techreborn.jei.recipe.processing.time.2",
			seconds);
		fontRendererObj.drawString(processingTimeString2, x + 10, y, color);
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

		if (fluidOutputSlots != null) {
			List<FluidStack> fluidOutputs = recipe.getFluidOutputs();
			for (int i = 0; i < fluidOutputs.size() && i < fluidOutputSlots.length; i++) {
				int outputTank = fluidOutputSlots[i];
				guiFluidStacks.set(outputTank, fluidOutputs.get(i));
			}
		}
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
			List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class);
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
			List<FluidStack> fluidOutputs = ingredients.getOutputs(FluidStack.class);
			for (int i = 0; i < fluidOutputs.size() && i < fluidOutputSlots.length; i++) {
				int outputTank = fluidOutputSlots[i];
				guiFluidStacks.set(outputTank, fluidOutputs.get(i));
			}
		}
	}
}
