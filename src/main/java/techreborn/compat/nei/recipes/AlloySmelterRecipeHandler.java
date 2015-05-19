package techreborn.compat.nei.recipes;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.client.gui.GuiAlloySmelter;
import techreborn.util.ItemUtils;
import codechicken.nei.PositionedStack;

public class AlloySmelterRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {
	@Override
	public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
		int offset = 4;
		PositionedStack pStack = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(0)), 47 - offset, 17 - offset);
		input.add(pStack);

		PositionedStack pStack2 = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(1)), 65 - offset, 17 - offset);
		input.add(pStack2);

		PositionedStack pStack3 = new PositionedStack(recipeType.getOutputs().get(0), 116 - offset, 35 - offset);
		outputs.add(pStack3);
	}

	@Override
	public String getRecipeName() {
		return "alloySmelterRecipe";
	}

	@Override
	public String getGuiTexture() {
		return "techreborn:textures/gui/electric_alloy_furnace.png";
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiAlloySmelter.class;
	}

	@Override
	public INeiBaseRecipe getNeiBaseRecipe() {
		return this;
	}
}
