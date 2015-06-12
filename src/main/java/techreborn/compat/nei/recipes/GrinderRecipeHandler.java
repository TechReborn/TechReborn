package techreborn.compat.nei.recipes;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import ic2.core.util.DrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.client.gui.GuiGrinder;
import techreborn.util.ItemUtils;

import java.util.List;

public class GrinderRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {
	@Override
	public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
		int offset = 4;
		if (recipeType.getInputs().size() > 0) {
			PositionedStack pStack = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(0)), 32 - offset, 26 - offset);
			input.add(pStack);
		}

		if (recipeType.getInputs().size() > 1) {
			PositionedStack pStack2 = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(1)), 32 - offset, 44 - offset);
			input.add(pStack2);
		}

		if (recipeType.getOutputsSize() > 0) {
			PositionedStack pStack3 = new PositionedStack(recipeType.getOutput(0), 77 - offset, 35 - offset);
			outputs.add(pStack3);
		}

		if (recipeType.getOutputsSize() > 1) {
			PositionedStack pStack4 = new PositionedStack(recipeType.getOutput(1), 95 - offset, 35 - offset);
			outputs.add(pStack4);
		}

		if (recipeType.getOutputsSize() > 2) {
			PositionedStack pStack5 = new PositionedStack(recipeType.getOutput(2), 113 - offset, 35 - offset);
			outputs.add(pStack5);
		}

		if (recipeType.getOutputsSize() > 3) {
			PositionedStack pStack6 = new PositionedStack(recipeType.getOutput(3), 131 - offset, 35 - offset);
			outputs.add(pStack6);
		}

	}

	@Override
	public String getRecipeName() {
		return "grinderRecipe";
	}

	@Override
	public String getGuiTexture() {
		return "techreborn:textures/gui/industrial_grinder.png";
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiGrinder.class;
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
			if (((CachedGenericRecipe) recipe).recipie instanceof GrinderRecipe) {
				GrinderRecipe grinderRecipe = (GrinderRecipe) ((CachedGenericRecipe) recipe).recipie;
				if (grinderRecipe.fluidStack != null) {
					IIcon fluidIcon = grinderRecipe.fluidStack.getFluid().getIcon();
					if (fluidIcon != null) {
//					GuiDraw.drawRect(7, 16, 176, 31, 0);
//					drawTexturedModalRect(k + 7, l + 15, 176, 31, 20, 55);

						Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
						int liquidHeight = grinderRecipe.fluidStack.amount * 47 / 16000;
						DrawUtil.drawRepeated(fluidIcon, 11, 19 + 47 - liquidHeight, 12.0D, liquidHeight, GuiDraw.gui.getZLevel());


//					this.mc.renderEngine.bindTexture(texture);
//					drawTexturedModalRect(k + 11, l + 19, 176, 86, 12, 47);
					}
					GuiDraw.drawString(grinderRecipe.fluidStack.amount + "mb of " + grinderRecipe.fluidStack.getLocalizedName(), 14, 124, -1);
				}
			}
		}

	}
}
