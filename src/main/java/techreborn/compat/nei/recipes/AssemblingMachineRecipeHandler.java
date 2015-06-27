package techreborn.compat.nei.recipes;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.client.gui.GuiAssemblingMachine;
import techreborn.util.ItemUtils;

import java.awt.Rectangle;
import java.util.List;

public class AssemblingMachineRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {
	@Override
	public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
		int offset = 4;
		if (recipeType.getInputs().size() > 0) {
			PositionedStack pStack = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(0)), 47 - offset, 17 - offset);
			input.add(pStack);
		}
		if (recipeType.getInputs().size() > 1) {
			PositionedStack pStack2 = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(1)), 65 - offset, 17 - offset);
			input.add(pStack2);
		}

		if (recipeType.getOutputsSize() > 0) {
			PositionedStack pStack3 = new PositionedStack(recipeType.getOutput(0), 116 - offset, 35 - offset);
			outputs.add(pStack3);
		}

	}

	@Override
	public String getRecipeName() {
		return "assemblingMachineRecipe";
	}

	@Override
	public String getGuiTexture() {
		return "techreborn:textures/gui/assembling_machine.png";
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiAssemblingMachine.class;
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
