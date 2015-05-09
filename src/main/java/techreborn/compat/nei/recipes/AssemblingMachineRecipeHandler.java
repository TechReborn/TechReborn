package techreborn.compat.nei.recipes;

import codechicken.nei.PositionedStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.client.gui.GuiAssemblingMachine;
import techreborn.client.gui.GuiImplosionCompressor;

import java.util.List;

public class AssemblingMachineRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {
	@Override
	public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
		int offset = 4;
		PositionedStack pStack = new PositionedStack(recipeType.getInputs().get(0), 47 - offset, 17 - offset);
		input.add(pStack);

		PositionedStack pStack2 = new PositionedStack(recipeType.getInputs().get(1), 65 - offset, 17 - offset);
		input.add(pStack2);

		PositionedStack pStack3 = new PositionedStack(recipeType.getOutputs().get(0), 116 - offset, 35 - offset);
		outputs.add(pStack3);
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
}
