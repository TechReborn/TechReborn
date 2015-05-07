package techreborn.api.recipe;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Extend this to add a recipe
 */
public abstract class BaseRecipe implements IBaseRecipeType {

	public ArrayList<ItemStack> inputs;

	public ArrayList<ItemStack> outputs;

	public String name;

	public int tickTime;

	public int euPerTick;

	public BaseRecipe(String name, int tickTime, int euPerTick) {
		inputs = new ArrayList<ItemStack>();
		outputs = new ArrayList<ItemStack>();
		this.name = name;
		//This adds all new recipes
		this.tickTime = tickTime;
		this.euPerTick = euPerTick;
	}

	@Override
	public List<ItemStack> getOutputs() {
		return outputs;
	}

	@Override
	public List<ItemStack> getInputs() {
		return inputs;
	}

	@Override
	public String getRecipeName() {
		return name;
	}

	@Override
	public int tickTime() {
		return tickTime;
	}

	@Override
	public int euPerTick() {
		return euPerTick;
	}
}
