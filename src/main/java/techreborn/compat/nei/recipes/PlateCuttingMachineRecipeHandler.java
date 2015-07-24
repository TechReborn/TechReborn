package techreborn.compat.nei.recipes;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.client.gui.GuiPlateCuttingMachine;
import techreborn.lib.Reference;
import techreborn.util.ItemUtils;

import java.awt.*;
import java.util.List;

public class PlateCuttingMachineRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {
	@Override
	public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
		int offset = 4;
		if (recipeType.getInputs().size() > 0) {
			PositionedStack pStack = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(0)), 56 - offset, 17 - offset);
			input.add(pStack);
		}
		if (recipeType.getOutputsSize() > 0) {
			PositionedStack pStack3 = new PositionedStack(recipeType.getOutput(0), 116 - offset, 35 - offset);
			outputs.add(pStack3);
		}
	}

	@Override
	public String getRecipeName() {
		return Reference.plateCuttingMachineRecipe;
	}

	@Override
	public String getGuiTexture() {
		return "techreborn:textures/gui/plate_cutting_machine.png";
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiPlateCuttingMachine.class;
	}

	@Override
	public INeiBaseRecipe getNeiBaseRecipe() {
		return this;
	}
	
	@Override
	public void loadTransferRects() {
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(
				new Rectangle(75, 20, 20, 20), getNeiBaseRecipe().getRecipeName(), new Object[0]));
	}
}
