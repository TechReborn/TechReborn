package techreborn.compat.nei.recipes;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import ic2.core.util.DrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.client.gui.GuiIndustrialSawmill;

import java.awt.Rectangle;
import java.util.List;

public class IndustrialSawmillRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {
	@Override
	public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
		int offset = 4;
		if (recipeType.getInputs().size() > 0) {
			PositionedStack pStack = new PositionedStack(recipeType.getInputs().get(0), 32 - offset, 26 - offset);
			input.add(pStack);
		}

		if (recipeType.getInputs().size() > 1) {
			PositionedStack pStack2 = new PositionedStack(recipeType.getInputs().get(1), 32 - offset, 44 - offset);
			input.add(pStack2);
		}

		if (recipeType.getOutputsSize() > 0) {
			PositionedStack pStack3 = new PositionedStack(recipeType.getOutput(0), 84 - offset, 35 - offset);
			outputs.add(pStack3);
		}

		if (recipeType.getOutputsSize() > 1) {
			PositionedStack pStack4 = new PositionedStack(recipeType.getOutput(1), 102 - offset, 35 - offset);
			outputs.add(pStack4);
		}

		if (recipeType.getOutputsSize() > 2) {
			PositionedStack pStack5 = new PositionedStack(recipeType.getOutput(2), 120 - offset, 35 - offset);
			outputs.add(pStack5);
		}
	}

	@Override
	public String getRecipeName() {
		return "industrialSawmillRecipe";
	}

	@Override
	public String getGuiTexture() {
		return "techreborn:textures/gui/industrial_sawmill.png";
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiIndustrialSawmill.class;
	}

	@Override
	public INeiBaseRecipe getNeiBaseRecipe() {
		return this;
	}
	
	@Override
	public void loadTransferRects() {
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(
				new Rectangle(80, 20, 20, 20), getNeiBaseRecipe().getRecipeName(), new Object[0]));
	}

	@Override
	public void drawBackground(int recipeIndex) {
		super.drawBackground(recipeIndex);
		CachedRecipe recipe = arecipes.get(recipeIndex);
		if (recipe instanceof CachedGenericRecipe) {
			if (((CachedGenericRecipe) recipe).recipie instanceof IndustrialSawmillRecipe) {
				IndustrialSawmillRecipe sawmillRecipe = (IndustrialSawmillRecipe) ((CachedGenericRecipe) recipe).recipie;
				if (sawmillRecipe.fluidStack != null) {
					IIcon fluidIcon = sawmillRecipe.fluidStack.getFluid().getIcon();
					if (fluidIcon != null) {
//					GuiDraw.drawRect(7, 16, 176, 31, 0);
//					drawTexturedModalRect(k + 7, l + 15, 176, 31, 20, 55);

						Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
						int liquidHeight = sawmillRecipe.fluidStack.amount * 47 / 16000;
						DrawUtil.drawRepeated(fluidIcon, 11, 19 + 47 - liquidHeight, 12.0D, liquidHeight, GuiDraw.gui.getZLevel());


//					this.mc.renderEngine.bindTexture(texture);
//					drawTexturedModalRect(k + 11, l + 19, 176, 86, 12, 47);
					}
					GuiDraw.drawString(sawmillRecipe.fluidStack.amount + "mb of " + sawmillRecipe.fluidStack.getLocalizedName(), 14, 124, -1);
				}
			}
		}

	}
}
