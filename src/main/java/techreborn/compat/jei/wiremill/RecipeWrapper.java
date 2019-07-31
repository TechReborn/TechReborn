package techreborn.compat.jei.wiremill;

import net.minecraft.item.ItemStack;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.api.praescriptum.ingredients.input.FluidStackInputIngredient;
import reborncore.api.praescriptum.ingredients.input.ItemStackInputIngredient;
import reborncore.api.praescriptum.ingredients.input.OreDictionaryInputIngredient;
import reborncore.api.praescriptum.ingredients.output.FluidStackOutputIngredient;
import reborncore.api.praescriptum.ingredients.output.ItemStackOutputIngredient;
import reborncore.api.praescriptum.recipes.Recipe;

import java.util.List;
import java.util.stream.Collectors;

public abstract class RecipeWrapper implements IRecipeWrapper {
	public RecipeWrapper(Recipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		// inputs
		List<ItemStack> itemInputs = recipe.getInputIngredients().stream()
			.filter(entry -> entry instanceof ItemStackInputIngredient)
			.map(entry -> (ItemStack) entry.ingredient)
			.collect(Collectors.toList()); // map ItemStacks

		recipe.getInputIngredients().stream()
			.filter(entry -> entry instanceof OreDictionaryInputIngredient)
			.flatMap(entry -> OreDictionary.getOres((String) entry.ingredient).stream())
			.collect(Collectors.toCollection(() -> itemInputs)); // map OreDictionary entries

		List<FluidStack> fluidInputs = recipe.getInputIngredients().stream()
			.filter(entry -> entry instanceof FluidStackInputIngredient)
			.map(entry -> (FluidStack) entry.ingredient)
			.collect(Collectors.toList()); // map ItemStacks

		ingredients.setInputs(VanillaTypes.ITEM, itemInputs);
		ingredients.setInputs(VanillaTypes.FLUID, fluidInputs);

		// outputs
		List<ItemStack> itemOutputs = recipe.getOutputIngredients().stream()
			.filter(entry -> entry instanceof ItemStackOutputIngredient)
			.map(entry -> (ItemStack) entry.ingredient)
			.collect(Collectors.toList()); // map ItemStacks

		List<FluidStack> fluidOutputs = recipe.getOutputIngredients().stream()
			.filter(entry -> entry instanceof FluidStackOutputIngredient)
			.map(entry -> (FluidStack) entry.ingredient)
			.collect(Collectors.toList()); // map ItemStacks

		ingredients.setOutputs(VanillaTypes.ITEM, itemOutputs);
		ingredients.setOutputs(VanillaTypes.FLUID, fluidOutputs);
	}

	// Fields >>
	protected final Recipe recipe;
	// << Fields
}
