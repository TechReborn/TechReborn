package techreborn.compat.nei.recipes;

import codechicken.nei.PositionedStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.client.gui.GuiAlloySmelter;
import techreborn.client.gui.GuiImplosionCompressor;
import techreborn.client.gui.GuiIndustrialSawmill;
import techreborn.util.ItemUtils;

import java.util.List;

public class IndustrialSawmillRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {
	@Override
	public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
		int offset = 4;
		PositionedStack pStack = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(0)), 32 - offset, 26 - offset);
		input.add(pStack);

		PositionedStack pStack2 = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getInputs().get(1)), 32 - offset, 44 - offset);
		input.add(pStack2);

		PositionedStack pStack3 = new PositionedStack(recipeType.getOutputs().get(0), 84 - offset, 35 - offset);
		outputs.add(pStack3);
		
		PositionedStack pStack4 = new PositionedStack(recipeType.getOutputs().get(1), 102 - offset, 35 - offset);
		outputs.add(pStack4);
		
		PositionedStack pStack5 = new PositionedStack(recipeType.getOutputs().get(2), 120 - offset, 35 - offset);
		outputs.add(pStack5);
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
