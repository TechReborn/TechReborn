package techreborn.compat.nei.recipes;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.client.gui.GuiCentrifuge;
import techreborn.util.ItemUtils;
import codechicken.nei.PositionedStack;

public class CentrifugeRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {
	@Override
	public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
		int offset = 4;
		PositionedStack pStack = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(0)), 80 - offset, 35 - offset);
		input.add(pStack);

		PositionedStack pStack2 = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(1)), 50 - offset, 5 - offset);
		input.add(pStack2);

		PositionedStack pStack3 = new PositionedStack(recipeType.getOutputs().get(0), 80 - offset, 5 - offset);
		outputs.add(pStack3);
		
		PositionedStack pStack4 = new PositionedStack(recipeType.getOutputs().get(1), 110 - offset, 35 - offset);
		outputs.add(pStack4);
		
		PositionedStack pStack5 = new PositionedStack(recipeType.getOutputs().get(2), 80 - offset, 65 - offset);
		outputs.add(pStack5);
		
		PositionedStack pStack6 = new PositionedStack(recipeType.getOutputs().get(3), 50 - offset, 35 - offset);
		outputs.add(pStack6);
	}

	@Override
	public String getRecipeName() {
		return "centrifugeRecipe";
	}

	@Override
	public String getGuiTexture() {
		return "techreborn:textures/gui/industrial_centrifuge.png";
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiCentrifuge.class;
	}

	@Override
	public INeiBaseRecipe getNeiBaseRecipe() {
		return this;
	}
}
