package techreborn.api.recipe;

import ic2.api.energy.prefab.BasicSink;
import techreborn.tiles.TileMachineBase;
import techreborn.util.Inventory;
import techreborn.util.ItemUtils;

/**
 * Use this in your tile entity to craft things
 */
public class RecipeCrafter {

	/**
	 * This is the recipe type to use
	 */
	public String recipeName;

	/**
	 * This is the parent tile
	 */
	public TileMachineBase parentTile;

	/**
	 * This is the place to use the power from
	 */
	public BasicSink energy;

	/**
	 * This is the amount of inputs that the setRecipe has
	 */
	public int inputs;

	/**
	 * This is the amount of outputs that the recipe has
	 */
	public int outputs;

	/**
	 * This is the inventory to use for the crafting
	 */
	public Inventory inventory;

	/**
	 * This is the list of the slots that the crafting logic should look for the input item stacks.
	 */
	public int[] inputSlots;

	/**
	 * This is the list for the slots that the crafting logic should look fot the output item stacks.
	 */
	public int[] outputSlots;

	/**
	 * This is the constructor, not a lot to say here :P
	 * @param recipeName
	 * @param parentTile
	 * @param energy
	 * @param inputs
	 * @param outputs
	 * @param inventory
	 * @param inputSlots
	 * @param outputSlots
	 */
	public RecipeCrafter(String recipeName, TileMachineBase parentTile, BasicSink energy, int inputs, int outputs, Inventory inventory, int[] inputSlots, int[] outputSlots) {
		this.recipeName = recipeName;
		this.parentTile = parentTile;
		this.energy = energy;
		this.inputs = inputs;
		this.outputs = outputs;
		this.inventory = inventory;
		this.inputSlots = inputSlots;
		this.outputSlots = outputSlots;
	}


	IBaseRecipeType currentRecipe;
	int currentTickTime = 0;

	/**
	 * Call this on the tile tick
	 */
	public void updateEntity(){
		if(currentRecipe == null){
			for(IBaseRecipeType recipe : RecipeHanderer.getRecipeClassFromName(recipeName)){
				boolean isFullRecipe = false;
				for (int i = 0; i < inputs; i++) {
					if(ItemUtils.isItemEqual(inventory.getStackInSlot(inputSlots[i]), recipe.getInputs().get(i), true, true)){
						isFullRecipe = true;
					} else {
						isFullRecipe = false;
					}
				}
				if(isFullRecipe){
					currentRecipe = recipe;
					return;
				}
			}
		} else {
			for (int i = 0; i < inputs; i++) {
				if(!ItemUtils.isItemEqual(inventory.getStackInSlot(inputSlots[i]), currentRecipe.getInputs().get(i), true, true)){
					currentRecipe = null;
					currentTickTime = 0;
					return;
				}
			}
			if(currentTickTime >= currentRecipe.tickTime()){
				//TODO give the player the goodies :)
				//Need some nicer way of added the crafting things.
			} else {
				if(energy.useEnergy(currentRecipe.euPerTick())){
					currentTickTime ++;
				}
			}
		}
	}
}
