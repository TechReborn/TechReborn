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

package techreborn.tiles.tier0;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;

import reborncore.api.IToolDrop;
import reborncore.api.praescriptum.Utils.IngredientUtils;
import reborncore.api.praescriptum.recipes.Recipe;
import reborncore.api.praescriptum.recipes.RecipeHandler;
import reborncore.api.tile.IInventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.tile.RebornMachineTile;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;

import techreborn.api.recipe.Recipes;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

import java.util.Arrays;

import com.google.common.collect.ImmutableList;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileIronAlloyFurnace extends RebornMachineTile
        implements IToolDrop, IInventoryProvider, IContainerProvider {

    public int tickTime;
    public Inventory inventory = new Inventory(4, "TileIronAlloyFurnace", 64, this);
    public int burnTime;
    public int burnTimeTotal;
    public int cookTime;
    private int cookTimeTotal = 200;
    protected final int[] inputSlots;
    int outputSlot = 2;
    int fuel = 3;
    protected Recipe recipe = null;
    public final RecipeHandler recipeHandler;


    public TileIronAlloyFurnace() {
        this.recipeHandler = Recipes.alloySmelter;
        this.inputSlots = new int[]{0, 1};

    }

    private void reset() {
        cookTime = 0; // reset progress
        recipe = null; // set current recipe to null
    }

    /**
     * Checks if it has inputs and can fit recipe outputSlot to outputSlot slot
     *
     * @return boolean True if it can craft recipe
     */
    private boolean canWork() {
        ImmutableList<ItemStack> inputs = Arrays.stream(inputSlots)
                .mapToObj(inventory::getStackInSlot)
                .filter(contents -> !contents.isEmpty())
                .collect(ImmutableList.toImmutableList());

        if (recipe != null) {
            boolean canUse = recipeHandler.apply(recipe, inputs, true);
            if (!canUse) reset(); // we cannot use the current recipe so reset
        }

        Recipe temp = recipe;

        // current recipe is still usable
        if (temp != null) {
            // we need space for the outputs
            return Arrays.stream(recipe.getItemOutputs())
                    .allMatch(output -> addToOutputs(output.copy(), true) == output.getCount());
        }

        // The current recipe is not usable anymore so we need to find a new one
        temp = recipeHandler.findRecipe(inputs); // try to find a matching recipe

        if (temp == null) return false; // could not find a recipe

        // if a matching recipe exists update parameters
        updateRecipe(temp);

        // we need space for the outputs
        return Arrays.stream(recipe.getItemOutputs())
                .allMatch(output -> addToOutputs(output.copy(), true) == output.getCount());
    }

    /**
     * Turn inputs into the appropriate smelted item in the alloy furnace result stack
     */
    private void smeltItem() {
        // if the input is empty the operation cannot be completed
        // if there are no inputs the machine cannot operate
        ImmutableList<ItemStack> inputs = Arrays.stream(inputSlots)
                .mapToObj(inventory::getStackInSlot)
                .filter(contents -> !contents.isEmpty())
                .collect(ImmutableList.toImmutableList());

        if (inputs.isEmpty()) {
            return;
        }

        // adjust input
        recipeHandler.apply(recipe, inputs, false);

        // adjust output
        Arrays.stream(recipe.getItemOutputs())
                .forEach(output -> addToOutputs(output.copy(), false));

        cookTime = 0;
    }

    private int addToOutputs(ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return 0;
        }

        int remaining = stack.getCount();

        if (remaining <= 0) {
            return 0;
        }

        ItemStack contents = inventory.getStackInSlot(outputSlot);
        int transfered = Math.min(remaining, Math.min(inventory.getInventoryStackLimit(), stack.getMaxStackSize()) - contents.getCount());

        if (!contents.isEmpty() && ItemUtils.isItemEqual(contents, stack, true, true)) {
            if (!simulate) {
                inventory.setInventorySlotContents(outputSlot, ItemUtils.increaseSize(contents, transfered));
            }
            remaining -= transfered;
        } else if (contents.isEmpty()) {
            if (!simulate) {
                ItemStack temp = ItemUtils.setSize(stack.copy(), transfered);
                inventory.setInventorySlotContents(outputSlot, temp);
            }
            remaining -= transfered;
        }

        return stack.getCount() - remaining;
    }

    private void updateRecipe(Recipe recipe) {
        reset();
        this.recipe = recipe;
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the
     * alloy furnace burning, or 0 if the item isn't fuel
     *
     * @param stack Itemstack of fuel
     * @return Integer Number of ticks
     */
    public int getItemBurnTime(ItemStack stack) {
        return stack.isEmpty() ? 0 :
                (int) (TileEntityFurnace.getItemBurnTime(stack) * 1.25);
    }

    /**
     * Alloy Furnace isBurning
     *
     * @return Boolean True if alloy furnace is burning
     */
    public boolean isBurning() {
        return burnTime > 0;
    }

    public int getBurnTimeRemainingScaled(int scale) {
        if (burnTimeTotal == 0) {
            burnTimeTotal = getItemBurnTime(getStackInSlot(fuel));
        }

        return burnTime * scale / burnTimeTotal;
    }

    public int getCookProgressScaled(final int scale) {
        return cookTime * scale / cookTimeTotal;
    }

    // RebornMachineTile
    @Override
    public void update() {
        super.update();
        boolean burning = isBurning();
        boolean updateInventory = false;
        if (burning) {
            --burnTime;
        }
        if (world.isRemote) {
            return;
        }
        ItemStack fuelStack = getStackInSlot(fuel);
        if (burnTime != 0 || !getStackInSlot(inputSlots[0]).isEmpty() && !fuelStack.isEmpty()) {
            if (burnTime == 0 && canWork()) {
                burnTimeTotal = burnTime = getItemBurnTime(fuelStack);
                if (burnTime > 0) {
                    updateInventory = true;
                    if (!fuelStack.isEmpty()) {
                        decrStackSize(fuel, 1);
                    }
                }
            }
            if (isBurning() && canWork()) {
                if (!isActive()) {
                    setActive(true);
                }
                ++cookTime;
                if (cookTime == cookTimeTotal) {
                    cookTime = 0;
                    smeltItem();
                    updateInventory = true;
                    setActive(false);
                }
            } else {
                cookTime = 0;
                setActive(false);
            }
        }
        if (burning != isBurning()) {
            updateInventory = true;
        }
        if (updateInventory) {
            markDirty();
        }
    }

    @Override
    public boolean canBeUpgraded() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagCompound data = tag.getCompoundTag("TileIronAlloyFurnace");
        cookTime = data.hasKey("cooktime") ? data.getInteger("cooktime") : 0;
        burnTime = data.hasKey("burntime") ? data.getInteger("burntime") : 0;
        burnTimeTotal = data.hasKey("burntimetotal") ? data.getInteger("burntimetotal") : 0;

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("cooktime", cookTime);
        data.setInteger("burntime", burnTime);
        data.setInteger("burntimetotal", burnTimeTotal);
        tag.setTag("TileIronAlloyFurnace", data);

        return tag;
    }

    // IToolDrop
    @Override
    public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
        return new ItemStack(ModBlocks.IRON_ALLOY_FURNACE, 1);
    }

    // IInventoryProvider
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    // IContainerProvider
    @Override
    public BuiltContainer createContainer(final EntityPlayer player) {
        return new ContainerBuilder("alloyfurnace").player(player.inventory).inventory(8, 84).hotbar(8, 142)
                .addInventory().tile(this)
                .filterSlot(0, 47, 17, IngredientUtils.isPartOfRecipe(recipeHandler))
                .filterSlot(1, 65, 17, IngredientUtils.isPartOfRecipe(recipeHandler))
                .outputSlot(2, 116, 35)
                .fuelSlot(3, 56, 53)
                .syncIntegerValue(this::getBurnTime, this::setBurnTime)
                .syncIntegerValue(this::getCookTime, this::setCookTime)
                .syncIntegerValue(this::getCurrentItemBurnTime, this::setCurrentItemBurnTime)
                .addInventory()
                .create(this);
    }

    public int getBurnTime() {
        return this.burnTime;
    }

    public void setBurnTime(final int burnTime) {
        this.burnTime = burnTime;
    }

    public int getCurrentItemBurnTime() {
        return this.burnTimeTotal;
    }

    public void setCurrentItemBurnTime(final int currentItemBurnTime) {
        this.burnTimeTotal = currentItemBurnTime;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    public void setCookTime(final int cookTime) {
        this.cookTime = cookTime;
    }
}
