package techreborn.compat.jei;

import javax.annotation.Nonnull;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import techreborn.api.recipe.BaseRecipe;

public abstract class BaseRecipeWrapper extends BlankRecipeWrapper {
	protected final BaseRecipe baseRecipe;

	public BaseRecipeWrapper(BaseRecipe baseRecipe) {
		this.baseRecipe = baseRecipe;
	}

	@Override
	public List<ItemStack> getInputs() {
		return baseRecipe.getInputs();
	}

	@Override
	public List<ItemStack> getOutputs() {
		return baseRecipe.getOutputs();
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
		// TODO: make right location for each recipe
		//		RecipeInfoUtil.drawInfo(minecraft, 0, 0, baseRecipe.euPerTick(), baseRecipe.tickTime());
	}
}
