package techreborn.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

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
     * This gets the output form the array list
     *
     * @return the output
     */
    public ItemStack getOutput(int i);

	/**
	 *
	 * @return The ammount of outputs
	 */
	public int getOutputsSize();

    /**
     * This is the name to check that the recipe is the one that should be used in
     * the tile entity that is set up to process this recipe.
     *
     * @return The recipeName
     */
    public String getRecipeName();

    /**
     * This is how long the recipe needs to tick for the crafting operation to complete
     *
     * @return tick length
     */
    public int tickTime();

    /**
     * This is how much eu Per tick the machine should use
     *
     * @return the amount of eu to be used per tick.
     */
    public int euPerTick();

    /**
     * @param tile the tile that is doing the crafting
     * @return if true the recipe will craft, if false it will not
     */
    public boolean canCraft(TileEntity tile);

    /**
     * @param tile the tile that is doing the crafting
     * @return return true if fluid was taken and should craft
     */
    public boolean onCraft(TileEntity tile);

    public Object clone()throws CloneNotSupportedException;

	public boolean useOreDic();
}
