package techreborn.api.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * Extend this to add a recipe
 */
public abstract class BaseRecipe implements IBaseRecipeType, Cloneable
{

	public ArrayList<ItemStack> inputs;
	public String name;
	public int tickTime;
	public int euPerTick;
	private ArrayList<ItemStack> outputs;

	public BaseRecipe(String name, int tickTime, int euPerTick)
	{
		inputs = new ArrayList<ItemStack>();
		outputs = new ArrayList<ItemStack>();
		this.name = name;
		// This adds all new recipes
		this.tickTime = tickTime;
		this.euPerTick = euPerTick;
	}

	@Override
	public ItemStack getOutput(int i)
	{
		return outputs.get(i).copy();
	}

	@Override
	public int getOutputsSize()
	{
		return outputs.size();
	}

	public void addOutput(ItemStack stack)
	{
		outputs.add(stack);
	}

	@Override
	public List<ItemStack> getInputs()
	{
		return inputs;
	}

	@Override
	public String getRecipeName()
	{
		return name;
	}

	@Override
	public int tickTime()
	{
		return tickTime;
	}

	@Override
	public int euPerTick()
	{
		return euPerTick;
	}

	@Override
	public boolean canCraft(TileEntity tile)
	{
		return true;
	}

	@Override
	public boolean onCraft(TileEntity tile)
	{
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	@Override
	public boolean useOreDic()
	{
		return true;
	}

	@Override
	public List<ItemStack> getOutputs()
	{
		return outputs;
	}
}
