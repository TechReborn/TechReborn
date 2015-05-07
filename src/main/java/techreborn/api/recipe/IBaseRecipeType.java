package techreborn.api.recipe;

import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * This is the base recipe class implement this to make a recipe handler
 */
public interface IBaseRecipeType {

	/**
	 * Use this to get all of the inputs
	 *
	 * @return the List of inputs
	 */
	public List<ItemStack> getInputs();

	/**
	 * Use this to get all of the outputs
	 *
	 * @return the List of outputs
	 */
	public List<ItemStack> getOutputs();

	/**
	 * This is the name to check that the recipe is the one that should be used in
	 * the tile entity that is set up to process this recipe.
	 *
	 * @return The recipeName
	 */
	public String getRecipeName();
}
