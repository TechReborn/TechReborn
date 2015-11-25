package techreborn.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.power.IEnergyInterfaceTile;
import techreborn.blocks.BlockMachineBase;
import techreborn.tiles.TileMachineBase;

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
    public IEnergyInterfaceTile energy;

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
     * @param recipeName The recipe name that should be crafted
     * @param parentTile The tile that wil be using this recipe crafter
     * @param inputs The amount of input slots
     * @param outputs The amount of output slots
     * @param inventory The inventory of the machine
     * @param inputSlots A list of the input slot ids
     * @param outputSlots A list of output slot ids
     */
    public RecipeCrafter(String recipeName, TileMachineBase parentTile, int inputs, int outputs, Inventory inventory, int[] inputSlots, int[] outputSlots) {
        this.recipeName = recipeName;
        this.parentTile = parentTile;
        if (parentTile instanceof IEnergyInterfaceTile) {
            energy = (IEnergyInterfaceTile) parentTile;
        }
        this.inputs = inputs;
        this.outputs = outputs;
        this.inventory = inventory;
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
    }


    public IBaseRecipeType currentRecipe;
    public int currentTickTime = 0;
    public int currentNeededTicks = 1;//Set to 1 to stop rare crashes
    double lastEnergy;

    /**
     * This is used to change the speed of the crafting operation.
     * <p/>
     * 0 = none;
     * 0.2 = 20% speed increase
     * 0.75 = 75% increase
     */
    double speedMultiplier = 0;

    /**
     * This is used to change the power of the crafting operation.
     * <p/>
     * 1 = none;
     * 1.2 = 20% speed increase
     * 1.75 = 75% increase
     * 5 = uses 5 times more power
     */
    double powerMultiplier = 1;

    int ticksSinceLastChange;

    /**
     * Call this on the tile tick
     */
    public void updateEntity() {
        if (parentTile.getWorld().isRemote) {
            return;
        }
        ticksSinceLastChange++;
        if (ticksSinceLastChange == 20) {//Force a has chanced every second
            inventory.hasChanged = true;
            ticksSinceLastChange = 0;
        }
        if (currentRecipe == null && inventory.hasChanged) {//It will now look for new recipes.
            currentTickTime = 0;
            for (IBaseRecipeType recipe : RecipeHandler.getRecipeClassFromName(recipeName)) {
                if (recipe.canCraft(parentTile) && hasAllInputs(recipe)) {//This checks to see if it has all of the inputs
                    boolean canGiveInvAll = true;
                    for (int i = 0; i < recipe.getOutputsSize(); i++) {//This checks to see if it can fit all of the outputs
                        if (!canFitStack(recipe.getOutput(i), outputSlots[i], recipe.useOreDic())) {
                            canGiveInvAll = false;
                            return;
                        }
                    }
                    if (canGiveInvAll) {
                        setCurrentRecipe(recipe);//Sets the current recipe then syncs
                        this.currentNeededTicks = (int) (currentRecipe.tickTime() * (1.0 - speedMultiplier));
                        this.currentTickTime = -1;
                        setIsActive();
                    } else {
                        this.currentTickTime = -1;
                    }
                }
            }
        } else {
            if (inventory.hasChanged && !hasAllInputs()) {//If it doesn't have all the inputs reset
                currentRecipe = null;
                currentTickTime = -1;
                setIsActive();
            }
            if (currentRecipe != null && currentTickTime >= currentNeededTicks) {//If it has reached the recipe tick time
                boolean canGiveInvAll = true;
                for (int i = 0; i < currentRecipe.getOutputsSize(); i++) {//Checks to see if it can fit the output
                    if (!canFitStack(currentRecipe.getOutput(i), outputSlots[i], currentRecipe.useOreDic())) {
                        canGiveInvAll = false;
                    }
                }
                ArrayList<Integer> filledSlots = new ArrayList<Integer>();//The slots that have been filled
                if (canGiveInvAll && currentRecipe.onCraft(parentTile)) {
                    for (int i = 0; i < currentRecipe.getOutputsSize(); i++) {
                        if (!filledSlots.contains(outputSlots[i])) {//checks it has not been filled
                            fitStack(currentRecipe.getOutput(i).copy(), outputSlots[i]);//fills the slot with the output stack
                            filledSlots.add(outputSlots[i]);
                        }
                    }
                    useAllInputs();//this uses all the inputs
                    currentRecipe = null;//resets
                    currentTickTime = -1;
                    setIsActive();
                }
            } else if (currentRecipe != null && currentTickTime < currentNeededTicks) {
                if (energy.canUseEnergy(getEuPerTick())) {//This uses the power
                    energy.useEnergy(getEuPerTick());
                    currentTickTime++;//increase the ticktime
                }
            }
        }
        if (inventory.hasChanged) {
            inventory.hasChanged = false;
        }
    }

    public boolean hasAllInputs() {
        if (currentRecipe == null) {
            return false;
        }
        for (ItemStack input : currentRecipe.getInputs()) {
            Boolean hasItem = false;
            for (int inputSlot : inputSlots) {//Checks to see if it can find the input
                if (ItemUtils.isItemEqual(input, inventory.getStackInSlot(inputSlot), true, true, currentRecipe.useOreDic()) && inventory.getStackInSlot(inputSlot).stackSize >= input.stackSize) {
                    hasItem = true;
                }
            }
            if (!hasItem)
                return false;
        }
        return true;
    }

    public boolean hasAllInputs(IBaseRecipeType recipeType) {
        if (recipeType == null) {
            return false;
        }
        for (ItemStack input : recipeType.getInputs()) {
            Boolean hasItem = false;
            for (int inputslot : inputSlots) {
                if (ItemUtils.isItemEqual(input, inventory.getStackInSlot(inputslot), true, true, recipeType.useOreDic()) && inventory.getStackInSlot(inputslot).stackSize >= input.stackSize) {
                    hasItem = true;
                }
            }
            if (!hasItem)
                return false;
        }
        return true;
    }

    public void useAllInputs() {
        if (currentRecipe == null) {
            return;
        }
        for (ItemStack input : currentRecipe.getInputs()) {
            for (int inputSlot : inputSlots) {//Uses all of the inputs
                if (ItemUtils.isItemEqual(input, inventory.getStackInSlot(inputSlot), true, true, currentRecipe.useOreDic())) {
                    inventory.decrStackSize(inputSlot, input.stackSize);
                    break;
                }
            }
        }
    }

    public boolean canFitStack(ItemStack stack, int slot, boolean oreDic) {//Checks to see if it can fit the stack
        if (stack == null) {
            return true;
        }
        if (inventory.getStackInSlot(slot) == null) {
            return true;
        }
        if (ItemUtils.isItemEqual(inventory.getStackInSlot(slot), stack, true, true, oreDic)) {
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
        if (ItemUtils.isItemEqual(inventory.getStackInSlot(slot), stack, true, true, currentRecipe.useOreDic())) {//If the slot has stuff in
            if (stack.stackSize + inventory.getStackInSlot(slot).stackSize <= stack.getMaxStackSize()) {//Check to see if it fits
                ItemStack newStack = stack.copy();
                newStack.stackSize = inventory.getStackInSlot(slot).stackSize + stack.stackSize;//Sets the new stack size
                inventory.setInventorySlotContents(slot, newStack);
            }
        }
    }

    public void readFromNBT(NBTTagCompound tag) {
        NBTTagCompound data = tag.getCompoundTag("Crater");

        if (data.hasKey("currentTickTime"))
            currentTickTime = data.getInteger("currentTickTime");

        if (parentTile != null && parentTile.getWorld() != null && parentTile.getWorld().isRemote) {
            parentTile.getWorld().markBlockForUpdate(parentTile.getPos());
            parentTile.getWorld().markBlockRangeForRenderUpdate(parentTile.getPos().getX(), parentTile.getPos().getY(), parentTile.getPos().getZ(), parentTile.getPos().getX(), parentTile.getPos().getY(), parentTile.getPos().getZ());
        }
    }

    public void writeToNBT(NBTTagCompound tag) {

        NBTTagCompound data = new NBTTagCompound();

        data.setDouble("currentTickTime", currentTickTime);

        tag.setTag("Crater", data);
    }


    private boolean isActive() {
        return currentRecipe != null && energy.getEnergy() >= currentRecipe.euPerTick();
    }

    public void addSpeedMulti(double amount) {
        if (speedMultiplier + amount <= 0.99) {
            speedMultiplier += amount;
        } else {
            speedMultiplier = 0.99;
        }
    }

    public void resetSpeedMulti() {
        speedMultiplier = 0;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void addPowerMulti(double amount) {
        powerMultiplier += amount;
    }

    public void resetPowerMulti() {
        powerMultiplier = 1;
    }

    public double getPowerMultiplier() {
        return powerMultiplier;
    }

    public double getEuPerTick() {
        return currentRecipe.euPerTick() * powerMultiplier;
    }


    public void setIsActive() {
        if(parentTile.getWorld().getBlockState(parentTile.getPos()).getBlock() instanceof BlockMachineBase){
            BlockMachineBase blockMachineBase = (BlockMachineBase) parentTile.getWorld().getBlockState(parentTile.getPos()).getBlock();
            blockMachineBase.setActive(isActive(), parentTile.getWorld(), parentTile.getPos());
        }
        parentTile.getWorld().markBlockForUpdate(parentTile.getPos());
    }

    public void setCurrentRecipe(IBaseRecipeType recipe) {
        try {
            this.currentRecipe = (IBaseRecipeType) recipe.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
