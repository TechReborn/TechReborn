package techreborn.api.recipe;

import ic2.api.energy.prefab.BasicSink;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
	 *
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


	public IBaseRecipeType currentRecipe;
	public int currentTickTime = 0;

	/**
	 * Call this on the tile tick
	 */
	public void updateEntity() {
		if (currentRecipe == null) {//It will now look for new recipes.
			for (IBaseRecipeType recipe : RecipeHanderer.getRecipeClassFromName(recipeName)) {
				if(recipe.canCraft(parentTile) && hasAllInputs(recipe)){//This checks to see if it has all of the inputs
					boolean canGiveInvAll = true;
					for (int i = 0; i < recipe.getOutputs().size(); i++) {//This checks to see if it can fit all of the outputs
						if (!canFitStack(recipe.getOutputs().get(i), outputSlots[i])) {
							canGiveInvAll = false;
						}
					}
					if(canGiveInvAll){
						currentRecipe = recipe;//Sets the current recipe then syncs
						parentTile.syncWithAll();
						return;
					}
				}
			}
		} else {
			if(!hasAllInputs()){//If it doesn't have all the inputs reset
				currentRecipe = null;
				currentTickTime = 0;
				parentTile.syncWithAll();
				return;
			}
			if (currentTickTime >= currentRecipe.tickTime()) {//If it has reached the recipe tick time
				boolean canGiveInvAll = true;
				for (int i = 0; i < currentRecipe.getOutputs().size(); i++) {//Checks to see if it can fit the output
					if (!canFitStack(currentRecipe.getOutputs().get(i), outputSlots[i])) {
						canGiveInvAll = false;
					}
				}
				ArrayList<Integer> filledSlots = new ArrayList<Integer>();//The slots that have been filled
				if (canGiveInvAll && currentRecipe.onCraft(parentTile)) {
					for (int i = 0; i < currentRecipe.getOutputs().size(); i++) {
						if (!filledSlots.contains(outputSlots[i])) {//checks it has not been filled
							fitStack(currentRecipe.getOutputs().get(i), outputSlots[i]);//fills the slot with the output stack
							filledSlots.add(outputSlots[i]);
						}
					}
					useAllInputs();//this uses all the inputs
					currentRecipe = null;//resets
					currentTickTime = 0;
					parentTile.syncWithAll();
				}
			} else if (currentTickTime < currentRecipe.tickTime()) {
				if (energy.canUseEnergy(currentRecipe.euPerTick())) {//This checks to see if it can use the power
					if(!parentTile.getWorldObj().isRemote){//remove the power on the server side only
						this.energy.setEnergyStored(this.energy.getEnergyStored() - currentRecipe.euPerTick());
					}
					currentTickTime++;//increase the ticktime
					parentTile.syncWithAll();
				}
			}
		}
	}

	public boolean hasAllInputs(){
		if(currentRecipe == null){
			return false;
		}
		for(ItemStack input : currentRecipe.getInputs()){
			Boolean hasItem = false;
			for(int inputSlot : inputSlots){//Checks to see if it can find the input
				if(ItemUtils.isItemEqual(input, inventory.getStackInSlot(inputSlot), true, true, true) && inventory.getStackInSlot(inputSlot).stackSize >= input.stackSize){
					hasItem = true;
				}
			}
			if(!hasItem)
				return false;
		}
		return true;
	}

	public boolean hasAllInputs(IBaseRecipeType recipeType){
		if(recipeType == null){
			return false;
		}
		for(ItemStack input : recipeType.getInputs()){
			Boolean hasItem = false;
			for(int inputslot : inputSlots){
				if(ItemUtils.isItemEqual(input, inventory.getStackInSlot(inputslot), true, true, true)){
					hasItem = true;
				}
			}
			if(!hasItem)
				return false;
		}
		return true;
	}

	public void useAllInputs(){
		if(currentRecipe == null){
			return;
		}
		for(ItemStack input : currentRecipe.getInputs()){
			for(int inputSlot : inputSlots){//Uses all of the inputs
				if(ItemUtils.isItemEqual(input, inventory.getStackInSlot(inputSlot), true, true, true)){
					inventory.decrStackSize(inputSlot, input.stackSize);
				}
			}
		}
	}

	public boolean canFitStack(ItemStack stack, int slot) {//Checks to see if it can fit the stack
		if (stack == null) {
			return true;
		}
		if (inventory.getStackInSlot(slot) == null) {
			return true;
		}
		if (ItemUtils.isItemEqual(inventory.getStackInSlot(slot), stack, true, true, true)) {
			if (stack.stackSize + inventory.getStackInSlot(slot).stackSize <= stack.getMaxStackSize()) {
				return true;
			}
		}
		return false;
	}

	public void fitStack(ItemStack stack, int slot) {//This fits a stack into a slot
		if (stack == null) {
			return;
		}
		if (inventory.getStackInSlot(slot) == null) {//If the slot is empty set the contents
			inventory.setInventorySlotContents(slot, stack);
			return;
		}
		if (ItemUtils.isItemEqual(inventory.getStackInSlot(slot), stack, true, true, true)) {//If the slot has stuff in
			if (stack.stackSize + inventory.getStackInSlot(slot).stackSize <= stack.getMaxStackSize()) {//Check to see if it fits
				ItemStack newStack = stack.copy();
				newStack.stackSize = inventory.getStackInSlot(slot).stackSize + stack.stackSize;//Sets the new stack size
				inventory.setInventorySlotContents(slot, newStack);
			}
		}
	}

	public void readFromNBT(NBTTagCompound tag) {
		NBTTagCompound data = tag.getCompoundTag("Crater");

		currentTickTime = data.getInteger("currentTickTime");
	}

	public void writeToNBT(NBTTagCompound tag) {

		NBTTagCompound data = new NBTTagCompound();

		data.setDouble("currentTickTime", currentTickTime);

		tag.setTag("Crater", data);
	}

	public boolean isActive(){
		return currentRecipe != null && currentTickTime != 0;
	}
}
