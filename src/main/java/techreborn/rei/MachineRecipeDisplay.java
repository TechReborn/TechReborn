package techreborn.rei;

import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornIngredient;
import reborncore.common.crafting.RebornRecipe;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MachineRecipeDisplay<R extends RebornRecipe> implements RecipeDisplay<R> {

	private final R recipe;
	private List<List<ItemStack>> inputs;
	private List<ItemStack> outputs;

	public MachineRecipeDisplay(R recipe) {
		this.recipe = recipe;
		this.inputs = recipe.getRebornIngredients().stream().map(RebornIngredient::getStacks).collect(Collectors.toList());
		this.outputs = recipe.getOutputs();
	}

	@Override
	public Optional<R> getRecipe() {
		return Optional.ofNullable(recipe);
	}

	@Override
	public List<List<ItemStack>> getInput() {
		return inputs;
	}

	@Override
	public List<List<ItemStack>> getRequiredItems() {
		return inputs;
	}

	@Override
	public List<ItemStack> getOutput() {
		return outputs;
	}

	@Override
	public Identifier getRecipeCategory() {
		return recipe.getRebornRecipeType().getName();
	}
}
