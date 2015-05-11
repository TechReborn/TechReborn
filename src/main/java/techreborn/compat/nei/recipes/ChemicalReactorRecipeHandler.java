package techreborn.compat.nei.recipes;

import codechicken.nei.PositionedStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.client.gui.GuiAlloySmelter;
import techreborn.client.gui.GuiChemicalReactor;
import techreborn.client.gui.GuiImplosionCompressor;
import techreborn.util.ItemUtils;

import java.util.List;

public class ChemicalReactorRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {
	@Override
	public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
		int offset = 4;
		PositionedStack pStack = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(0)), 70 - offset, 21 - offset);
		input.add(pStack);

		PositionedStack pStack2 = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(1)), 90 - offset, 21 - offset);
		input.add(pStack2);

		PositionedStack pStack3 = new PositionedStack(recipeType.getOutputs().get(0), 80 - offset, 51 - offset);
		outputs.add(pStack3);
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
}
