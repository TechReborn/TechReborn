/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.tiles.processing;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import reborncore.api.IToolDrop;
import reborncore.api.praescriptum.ingredients.output.ItemStackOutputIngredient;
import reborncore.api.praescriptum.ingredients.output.OutputIngredient;
import reborncore.api.praescriptum.recipes.Recipe;
import reborncore.api.praescriptum.recipes.RecipeHandler;
import reborncore.api.tile.IInventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;

/**
 * @author estebes
 */
public abstract class TileMachine extends TilePowerAcceptor implements IToolDrop, IInventoryProvider, IContainerProvider {
    // Constructors >>
    public TileMachine(String name, int maxInput, int maxEnergy, int energySlot, int slots, RecipeHandler recipeHandler, Block drop) {
        this(name, maxInput, maxEnergy, energySlot, slots, 64, recipeHandler, drop);
    }

    public TileMachine(String name, int maxInput, int maxEnergy, int energySlot, int slots, int slotSize, RecipeHandler recipeHandler, Block drop) {
        this(name, maxInput, maxEnergy, energySlot, slots, slotSize, new int[]{0}, new int[]{1}, recipeHandler, drop);
    }

    public TileMachine(String name, int maxInput, int maxEnergy, int energySlot, int slots, int slotSize, int[] inputSlots, int[] outputSlots, RecipeHandler recipeHandler, Block drop) {
        this.name = "Tile" + name;
        this.maxInput = maxInput;
        this.maxEnergy = maxEnergy;
        this.energySlot = energySlot;
        this.inventory = new Inventory(slots, name, slotSize, this);
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.recipeHandler = recipeHandler;
        this.drop = drop;

        checkTeir();
    }
    // << Constructors

    // TileEntity >>
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagCompound data = tag.getCompoundTag("TileMachine");
        progress = data.hasKey("progress") ? data.getInteger("progress") : 0;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("progress", this.progress);
        tag.setTag("TileMachine", data);

        return tag;
    }

    @Override
    public void update() {
        super.update();

        if (world.isRemote) return;

        charge(energySlot);

        boolean needsInventoryUpdate = false; // To reduce update frequency of inventories, only call onInventory changed if this was set to true

        // operation
        if (canWork()) { // operation conditions satisfied
            if (!active) setActive(true);

            // process start
            if (progress == 0) needsInventoryUpdate = true;

            int progressNeeded = Math.max((int) ((double) recipe.getOperationDuration() * (1.0D - this.getSpeedMultiplier())), 1);
            if (progress < progressNeeded) {
                useEnergy(getEuPerTick(recipe.getEnergyCostPerTick())); // use energy
                progress += 1; // update progress
            }

            if (progress >= progressNeeded) { // process end
                work();

                needsInventoryUpdate = true;
            }
        } else { // operation conditions not satisfied
            if (active) setActive(false);
        }

        if (needsInventoryUpdate) super.markDirty();
    }
    // << TileEntity

    // TilePowerAcceptor >>
    @Override
    public double getBaseMaxPower() {
        return maxEnergy;
    }

    @Override
    public boolean canAcceptEnergy(final EnumFacing direction) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(final EnumFacing direction) {
        return false;
    }

    @Override
    public double getBaseMaxOutput() {
        return 0;
    }

    @Override
    public double getBaseMaxInput() {
        return maxInput;
    }
    // << TilePowerAcceptor

    // TileMachine >>
    protected boolean canWork() {
        // if there are no inputs the machine cannot operate
        Queue<ItemStack> inputs = new ArrayDeque<>();
        for (int index : inputSlots) {
            ItemStack input = inventory.getStackInSlot(index);
            if (!ItemUtils.isEmpty(input)) inputs.add(input);
        }

        if (inputs.isEmpty()) {
            reset();
            return false;
        }

        if (recipe != null) {
            boolean canUse = recipeHandler.apply(recipe, inputs, true);
            if (!canUse) reset(); // we cannot use the current recipe so reset
        }

        Recipe temp = recipe;

        // current recipe is still usable
        if (temp != null) {
            if (!canUseEnergy(getEuPerTick(recipe.getEnergyCostPerTick()))) return false;

            // we need space for the outputs
            Queue<ItemStack> itemOutputs = new ArrayDeque<>();
            for (OutputIngredient<?> outputIngredient : recipe.getOutputIngredients()) {
                if (outputIngredient instanceof ItemStackOutputIngredient)
                    itemOutputs.add((ItemStack) outputIngredient.ingredient);
            }

            return addToOutputs(itemOutputs, true) == 0;
        }

        // The current recipe is not usable anymore so we need to find a new one
        temp = recipeHandler.findRecipe(inputs); // try to find a matching recipe

        if (temp == null) return false; // could not find a recipe

        // if a matching recipe exists update parameters
        updateRecipe(temp);

        // we need to have enough energy
        if (!canUseEnergy(getEuPerTick(recipe.getEnergyCostPerTick()))) return false;

        // we need space for the outputs
        Queue<ItemStack> itemOutputs = new ArrayDeque<>();
        for (OutputIngredient<?> outputIngredient : recipe.getOutputIngredients()) {
            if (outputIngredient instanceof ItemStackOutputIngredient)
                itemOutputs.add((ItemStack) outputIngredient.ingredient);
        }

        return addToOutputs(itemOutputs, true) == 0;
    }

    protected void work() {
        // if the input is empty the operation cannot be completed
        // if there are no inputs the machine cannot operate
        Queue<ItemStack> inputs = new ArrayDeque<>();
        for (int index : inputSlots) {
            ItemStack input = inventory.getStackInSlot(index);
            if (!ItemUtils.isEmpty(input)) inputs.add(input);
        }

        if (inputs.isEmpty()) {
            reset();
            return;
        }

        // adjust input
        recipeHandler.apply(recipe, inputs, false);

        // adjust output
        Queue<ItemStack> itemOutputs = new ArrayDeque<>();
        for (OutputIngredient<?> outputIngredient : recipe.getOutputIngredients()) {
            if (outputIngredient instanceof ItemStackOutputIngredient)
                itemOutputs.add((ItemStack) outputIngredient.ingredient);
        }

        addToOutputs(itemOutputs, false);

        progress = 0;
    }

    protected void updateRecipe(Recipe recipe) {
        operationLength = recipe.getOperationDuration(); // set operation length
        this.recipe = recipe; // set recipe
    }

    protected void reset() {
        progress = 0; // reset progress
        operationLength = 0; // reset operation length
        recipe = null; // set current recipe to null
    }
    // << TileMachine

    // IInventoryProvider >>
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    // << IInventoryProvider

    // IToolDrop >>
    @Override
    public ItemStack getToolDrop(EntityPlayer player) {
        return new ItemStack(drop, 1);
    }
    // << IToolDrop

    // Getters & Setters >>
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getOperationLength() {
        return operationLength;
    }

    public void setOperationLength(int operationLength) {
        this.operationLength = operationLength;
    }

    public int getProgressScaled(int scale) {
        return progress == 0 || operationLength == 0 ? 0 : progress * scale / operationLength;
    }
    // << Getters & Setters

    // Helpers >>
    // Distribute the provided stack across the available output slots
    protected int addToOutputs(ItemStack stack, boolean simulate) {
        return addToOutputs(Collections.singleton(stack), simulate);
    }

    protected int addToOutputs(Collection<ItemStack> stacks, boolean simulate) {
        if (stacks == null || stacks.isEmpty()) return 0;

        ItemStack[] copy = simulate ? createCopyOfSlots(outputSlots) : null;

        int totalAmount = 0;
        for (ItemStack stack : stacks) {
            int amount = ItemUtils.getSize(stack);
            if (amount <= 0) continue;

            // 2 types -> 0: increase existing stacks, 1: fill empty slots
            typeLoop:
            for (int type = 0; type < 2; type++) {
                for (int outputSlot : outputSlots) {
                    ItemStack existingStack = inventory.getStackInSlot(outputSlot);
                    int space = inventory.getInventoryStackLimit();

                    if (!ItemUtils.isEmpty(existingStack))
                        space = Math.min(space, existingStack.getMaxStackSize()) - ItemUtils.getSize(existingStack);

                    if (space <= 0) continue;

                    if (type == 0 && !ItemUtils.isEmpty(existingStack)
                            && ItemUtils.isItemEqual(stack, existingStack, true, true)) {
                        if (space >= amount) {
                            inventory.setInventorySlotContents(outputSlot, ItemUtils.increaseSize(existingStack, amount));
                            amount = 0;
                            break typeLoop;
                        } else {
                            inventory.setInventorySlotContents(outputSlot, ItemUtils.increaseSize(existingStack, space));
                            amount -= space;
                        }
                    } else if (type == 1 && ItemUtils.isEmpty(existingStack)) {
                        if (space >= amount) {
                            inventory.setInventorySlotContents(outputSlot, ItemUtils.copyWithSize(stack, amount));
                            amount = 0;
                            break typeLoop;
                        } else {
                            inventory.setInventorySlotContents(outputSlot, ItemUtils.copyWithSize(stack, space));
                            amount -= space;
                        }
                    }
                }
            }

            totalAmount += amount;
        }

        if (simulate) loadCopyOfSlots(outputSlots, copy);

        return totalAmount;
    }

    protected ItemStack[] createCopyOfSlots(int[] slots) {
        ItemStack[] ret = new ItemStack[slots.length];

        for (int index = 0; index < slots.length; index++) {
            ItemStack contents = inventory.getStackInSlot(slots[index]);
            ret[index] = ItemUtils.isEmpty(contents) ? ItemStack.EMPTY : contents.copy();
        }

        return ret;
    }

    protected void loadCopyOfSlots(int[] slots, ItemStack[] copy) {
        if (slots.length != copy.length)
            throw new IllegalArgumentException("The number of slots does not match the size of the copy");

        for (int index = 0; index < slots.length; index++)
            inventory.setInventorySlotContents(slots[index], copy[index]);
    }
    // << Helpers

    // Fields >>
    public final String name;
    public final int maxInput;
    public final int maxEnergy;
    public final Inventory inventory;
    public final RecipeHandler recipeHandler;
    public final Block drop;

    // Slots
    protected final int energySlot;
    protected final int[] inputSlots;
    protected final int[] outputSlots;

    protected int progress = 0;
    protected int operationLength = 0;
    protected Recipe recipe = null;
    // << Fields
}
