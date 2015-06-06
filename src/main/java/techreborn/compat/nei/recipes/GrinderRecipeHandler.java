package techreborn.compat.nei.recipes;

import codechicken.nei.PositionedStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.client.gui.GuiAlloySmelter;
import techreborn.client.gui.GuiGrinder;
import techreborn.util.ItemUtils;

import java.util.List;

public class GrinderRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {
	@Override
	public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
		int offset = 4;
		PositionedStack pStack = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(0)), 0 - offset, 32 - offset);
		input.add(pStack);

		PositionedStack pStack2 = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(1)), 32 - offset, 44 - offset);
		input.add(pStack2);

		PositionedStack pStack3 = new PositionedStack(recipeType.getOutputs().get(0), 2 - offset, 77 - offset);
		outputs.add(pStack3);

		PositionedStack pStack4 = new PositionedStack(recipeType.getOutputs().get(1), 2 - offset, 95 - offset);
		outputs.add(pStack4);

		PositionedStack pStack5 = new PositionedStack(recipeType.getOutputs().get(2), 4 - offset, 113 - offset);
		outputs.add(pStack5);

		PositionedStack pStack6 = new PositionedStack(recipeType.getOutputs().get(3), 5 - offset, 131 - offset);
		outputs.add(pStack6);
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
}
