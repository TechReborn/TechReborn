package techreborn.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import reborncore.api.recipe.IBaseRecipeType;
import reborncore.common.recipes.RecipeTranslator;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extend this to add a recipe
 */
public abstract class BaseRecipe implements IBaseRecipeType, Cloneable {

	public String name;
	public int tickTime;
	public int euPerTick;
	public ArrayList<Object> inputs;
	private ArrayList<ItemStack> outputs;
	private boolean oreDict = true;

	public BaseRecipe(String name, int tickTime, int euPerTick) {
		inputs = new ArrayList<>();
		outputs = new ArrayList<>();
		this.name = name;
		// This adds all new recipes
		this.tickTime = tickTime;
		this.euPerTick = euPerTick;
	}

	@Override
	public ItemStack getOutput(int i) {
		return outputs.get(i).copy();
	}

	@Override
	public int getOutputsSize() {
		return outputs.size();
	}

	public void addOutput(ItemStack stack) {
		if (stack == null || stack.getItem() == null) {
			throw new InvalidParameterException("Output stack is null or empty");
		}
		outputs.add(stack);
	}

	@Override
	public List<Object> getInputs() {
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

	@Override
	public boolean canCraft(TileEntity tile) {
		if (tile instanceof ITileRecipeHandler) {
			return ((ITileRecipeHandler) tile).canCraft(tile, this);
		}
		return true;
	}

	@Override
	public boolean onCraft(TileEntity tile) {
		if (tile instanceof ITileRecipeHandler) {
			return ((ITileRecipeHandler) tile).onCraft(tile, this);
		}
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void setOreDict(boolean oreDict) {
		this.oreDict = oreDict;
	}

	@Override
	public boolean useOreDic() {
		return oreDict;
	}

	@Override
	public List<ItemStack> getOutputs() {
		return outputs;
	}

	public void addInput(Object inuput) {
		if (inuput == null) {
			throw new InvalidParameterException("input is invalid!");
		}
		if(inuput instanceof ItemStack){
			if(((ItemStack) inuput).getItem() == null){
				throw new InvalidParameterException("input is invalid!");
			}
		}
		if(RecipeTranslator.getStackFromObject(inuput) == null){
			throw new InvalidParameterException("Could not determin recipe input for " + inuput);
		}
		inputs.add(inuput);
	}
}
