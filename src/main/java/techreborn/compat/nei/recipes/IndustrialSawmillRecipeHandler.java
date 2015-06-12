package techreborn.compat.nei.recipes;

import codechicken.nei.PositionedStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.client.gui.GuiIndustrialSawmill;

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
}
