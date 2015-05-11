package techreborn.api.recipe;

import ic2.api.energy.prefab.BasicSink;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import techreborn.tiles.TileMachineBase;
import techreborn.util.Inventory;
import techreborn.util.ItemUtils;

import java.util.ArrayList;

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
		/*if (parentTile.getWorldObj().isRemote) {
			return;
		}*/
		if (currentRecipe == null) {
			for (IBaseRecipeType recipe : RecipeHanderer.getRecipeClassFromName(recipeName)) {
				boolean isFullRecipe = false;
				for (int i = 0; i < inputs; i++) {
					if (ItemUtils.isItemEqual(inventory.getStackInSlot(inputSlots[i]), recipe.getInputs().get(i), true, true, true) && inventory.getStackInSlot(inputSlots[i]).stackSize >= recipe.getInputs().get(i).stackSize) {
						isFullRecipe = true;
					} else {
						isFullRecipe = false;
					}
				}
				if (isFullRecipe) {
					currentRecipe = recipe;
					return;
				}
			}
		} else {
			for (int i = 0; i < inputs; i++) {
				if (!ItemUtils.isItemEqual(inventory.getStackInSlot(inputSlots[i]), currentRecipe.getInputs().get(i), true, true, true) || (inventory.getStackInSlot(inputSlots[i]) == null || (inventory.getStackInSlot(inputSlots[i]).stackSize < currentRecipe.getInputs().get(i).stackSize))) {
					currentRecipe = null;
					currentTickTime = 0;
					return;
				}
			}
			if (currentTickTime >= currentRecipe.tickTime()) {
				boolean canGiveInvAll = true;
				for (int i = 0; i < currentRecipe.getOutputs().size(); i++) {
					if (!canFitStack(currentRecipe.getOutputs().get(i), outputSlots[i])) {
						canGiveInvAll = false;
					}
				}
				ArrayList<Integer> filledSlots = new ArrayList<Integer>();
				if (canGiveInvAll) {
					for (int i = 0; i < currentRecipe.getOutputs().size(); i++) {
						if (!filledSlots.contains(outputSlots[i])) {
							fitStack(currentRecipe.getOutputs().get(i), outputSlots[i]);
							filledSlots.add(outputSlots[i]);
						}
					}
					for (int i = 0; i < inputs; i++) {
						if (!filledSlots.contains(inputSlots[i])) {
							inventory.decrStackSize(inputSlots[i], currentRecipe.getInputs().get(i).stackSize);
						}
					}
					currentRecipe = null;
					currentTickTime = 0;
					parentTile.syncWithAll();
				}
			} else if (currentTickTime < currentRecipe.tickTime()) {
				if (energy.useEnergy(currentRecipe.euPerTick())) {
					currentTickTime++;
					parentTile.syncWithAll();
				}
			}
		}
	}

	public boolean canFitStack(ItemStack stack, int slot) {
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

	public void fitStack(ItemStack stack, int slot) {
		if (stack == null) {
			return;
		}
		if (inventory.getStackInSlot(slot) == null) {
			inventory.setInventorySlotContents(slot, stack);
			return;
		}
		if (ItemUtils.isItemEqual(inventory.getStackInSlot(slot), stack, true, true, true)) {
			if (stack.stackSize + inventory.getStackInSlot(slot).stackSize <= stack.getMaxStackSize()) {
				ItemStack newStack = stack.copy();
				newStack.stackSize = inventory.getStackInSlot(slot).stackSize + stack.stackSize;
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
}
