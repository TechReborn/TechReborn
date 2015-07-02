package techreborn.compat.nei.recipes;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import ic2.core.util.DrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.IndustrialElectrolyzerRecipe;
import techreborn.client.gui.GuiIndustrialElectrolyzer;
import techreborn.util.ItemUtils;

import java.util.List;

public class IndustrialElectrolyzerRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {
	@Override
	public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
		int offset = 4;
		if (recipeType.getInputs().size() > 0) {
			PositionedStack pStack = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(0)), 80 - offset, 51 - offset);
			input.add(pStack);
		}

		if (recipeType.getInputs().size() > 1) {
			PositionedStack pStack2 = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(1)), 50 - offset, 51 - offset);
			input.add(pStack2);
		}

		if (recipeType.getOutputsSize() > 0) {
			PositionedStack pStack3 = new PositionedStack(recipeType.getOutput(0), 50 - offset, 19 - offset);
			outputs.add(pStack3);
		}

		if (recipeType.getOutputsSize() > 1) {
			PositionedStack pStack4 = new PositionedStack(recipeType.getOutput(1), 70 - offset, 19 - offset);
			outputs.add(pStack4);
		}
		if (recipeType.getOutputsSize() > 2) {
			PositionedStack pStack5 = new PositionedStack(recipeType.getOutput(2), 90 - offset, 19 - offset);
			outputs.add(pStack5);
		}
		if (recipeType.getOutputsSize() > 3) {
			PositionedStack pStack6 = new PositionedStack(recipeType.getOutput(3), 110 - offset, 19 - offset);
			outputs.add(pStack6);
		}
	}

	@Override
	public String getRecipeName() {
		return "industrialElectrolyzerRecipe";
	}

	@Override
	public String getGuiTexture() {
		return "techreborn:textures/gui/industrial_electrolyzer.png";
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiIndustrialElectrolyzer.class;
	}

	@Override
	public INeiBaseRecipe getNeiBaseRecipe() {
		return this;
	}
	
	@Override
	public void drawBackground(int recipeIndex) {
		super.drawBackground(recipeIndex);
		CachedRecipe recipe = arecipes.get(recipeIndex);
		if (recipe instanceof CachedGenericRecipe) {
			if (((CachedGenericRecipe) recipe).recipie instanceof IndustrialElectrolyzerRecipe) {
				IndustrialElectrolyzerRecipe grinderRecipe = (IndustrialElectrolyzerRecipe) ((CachedGenericRecipe) recipe).recipie;
				if (grinderRecipe.fluidStack != null) {
					IIcon fluidIcon = grinderRecipe.fluidStack.getFluid().getIcon();
					if (fluidIcon != null) {

						Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
						int liquidHeight = grinderRecipe.fluidStack.amount * 100 / 16000;
						DrawUtil.drawRepeated(fluidIcon, 7, 22 + 47 - liquidHeight, 14.0D, liquidHeight, GuiDraw.gui.getZLevel());

					}
					GuiDraw.drawString(grinderRecipe.fluidStack.amount + "mb of " + grinderRecipe.fluidStack.getLocalizedName(), 14, 135, -1);
				}
			}
		}

	}
}
