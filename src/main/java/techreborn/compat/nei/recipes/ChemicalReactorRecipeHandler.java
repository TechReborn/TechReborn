package techreborn.compat.nei.recipes;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.client.gui.GuiChemicalReactor;
import techreborn.util.ItemUtils;

import java.awt.Rectangle;
import java.util.List;

public class ChemicalReactorRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {
	@Override
	public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
		int offset = 4;
		if (recipeType.getInputs().size() > 0) {
			PositionedStack pStack = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(0)), 70 - offset, 21 - offset);
			input.add(pStack);
		}

		if (recipeType.getInputs().size() > 1) {
			PositionedStack pStack2 = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(1)), 90 - offset, 21 - offset);
			input.add(pStack2);
		}

		if (recipeType.getOutputsSize() > 0) {
			PositionedStack pStack3 = new PositionedStack(recipeType.getOutput(0), 80 - offset, 51 - offset);
			outputs.add(pStack3);
		}
	}

	@Override
	public String getRecipeName() {
		return "chemicalReactorRecipe";
	}

	@Override
	public String getGuiTexture() {
		return "techreborn:textures/gui/chemical_reactor.png";
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiChemicalReactor.class;
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
}
