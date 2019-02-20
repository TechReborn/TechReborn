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

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityFurnace;
import reborncore.api.IToolDrop;
import reborncore.api.recipe.IBaseRecipeType;
import reborncore.api.recipe.RecipeHandler;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.recipes.RecipeTranslator;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.tile.TileLegacyMachineBase;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.Reference;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileIronAlloyFurnace extends TileLegacyMachineBase
	implements IToolDrop, IInventoryProvider, IContainerProvider {

	public int tickTime;
	public Inventory inventory = new Inventory(4, "TileIronAlloyFurnace", 64, this);
	public int burnTime;
	public int currentItemBurnTime;
	public int cookTime;
	int input1 = 0;
	int input2 = 1;
	int outputSlot = 2;
	int fuel = 3;

	public TileIronAlloyFurnace() {

	}

	/**
	 * Returns the number of ticks that the supplied fuel item will keep the
	 * alloy furnace burning, or 0 if the item isn't fuel
	 * @param stack Itemstack of fuel
	 * @return Integer Number of ticks
	 */
	public static int getItemBurnTime(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}
		return (int) (TileEntityFurnace.getItemBurnTime(stack) * 1.25);
	}
	
	/**
	 * Checks if alloy furnace has all inputs for recipe 
	 * @param recipeType IBaseRecipeType Alloy Smelter Recipe
	 * @return boolean True if we have all inputs necessery for recipe
	 */
	public boolean hasAllInputs(final IBaseRecipeType recipeType) {
		if (recipeType == null) {
			return false;
		}
		for (final Object input : recipeType.getInputs()) {
			boolean hasItem = false;
			boolean useOreDict = input instanceof String || recipeType.useOreDic();
			boolean checkSize = input instanceof ItemStack;
			for (int inputslot = 0; inputslot < 2; inputslot++) {
				if (ItemUtils.isInputEqual(input, inventory.getStackInSlot(inputslot), true, true, useOreDict)) {
					ItemStack inputStack = RecipeTranslator.getStackFromObject(input);
					if (!checkSize || inventory.getStackInSlot(inputslot).getCount() >= inputStack.getCount()) {
						hasItem = true;
					}
				}
			}
			if (!hasItem) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if it has inputs and can fit recipe outputSlot to outputSlot slot
	 * @return boolean True if it can fit outputSlot itemstack into outputSlot slot
	 */
	private boolean canSmelt() {
		if (getStackInSlot(input1).isEmpty() || getStackInSlot(input2).isEmpty()) {
			return false;
		} 
		ItemStack outputStack = null;
		for (final IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(Reference.ALLOY_SMELTER_RECIPE)) {
			if (hasAllInputs(recipeType)) {
				outputStack = recipeType.getOutput(0);
				break;
			}
		}

		if (outputStack == null) {
			return false;
		}
		ItemStack outputSlotStack = getStackInSlot(outputSlot);
		if (outputSlotStack.isEmpty()) {
			return true;
		}
		if (!outputSlotStack.isItemEqual(outputStack)) {
			return false;
		}
			
		final int result = outputSlotStack.getCount() + outputStack.getCount();
		return result <= getInventoryStackLimit() && result <= outputSlotStack.getMaxStackSize(); 	
	}
	
	/**
	 * Alloy Furnace isBurning
	 * @return Boolean True if alloy furnace is burning
	 */
	public boolean isBurning() {
		return burnTime > 0;
	}

	public int getBurnTimeRemainingScaled(final int scale) {
		if (currentItemBurnTime == 0) {
			currentItemBurnTime = 200;
		}

		return burnTime * scale / currentItemBurnTime;
	}

	public int getCookProgressScaled(final int scale) {
		return cookTime * scale / 200;
	}
	
	/**
	 * Turn inputs into the appropriate smelted item in the alloy furnace result stack
	 */
	public void smeltItem() {
		if (!canSmelt()) {
			return;
		}
		ItemStack outputStack = ItemStack.EMPTY;
		IBaseRecipeType alloySmelterRecipe = null;

		for (final IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(Reference.ALLOY_SMELTER_RECIPE)) {
			if (hasAllInputs(recipeType)) {
				alloySmelterRecipe = recipeType;
				outputStack = recipeType.getOutput(0);
				break;
			}
		}

		if (alloySmelterRecipe == null || outputStack.isEmpty()) {
			return;
		}

		// Add recipe results
		ItemStack outputSlotStack = getStackInSlot(outputSlot);
		if (outputSlotStack.isEmpty()) {
			setInventorySlotContents(outputSlot, outputStack.copy());
		} else if (getStackInSlot(outputSlot).getItem() == outputStack.getItem()) {
			decrStackSize(outputSlot, -outputStack.getCount());
		}

		// Remove recipe ingredients
		for (Object input : alloySmelterRecipe.getInputs()) {
			boolean useOreDict = input instanceof String || alloySmelterRecipe.useOreDic();
			for (int inputSlot = 0; inputSlot < 2; inputSlot++) {
				if (ItemUtils.isInputEqual(input, inventory.getStackInSlot(inputSlot), true, true, useOreDict)) {
					int count = RecipeTranslator.getStackFromObject(input).getCount();
					inventory.decrStackSize(inputSlot, count);
				}
			}
		}
	}

	// TileLegacyMachineBase
	@Override
	public void update() {
		super.update();
		final boolean burning = isBurning();
		boolean updateInventory = false;
		if (burning) {
			--burnTime;
		}
		if (world.isRemote) {
			return;
		}
		ItemStack fuelStack = getStackInSlot(fuel);
		if (burnTime != 0 || !getStackInSlot(input1).isEmpty() && !fuelStack.isEmpty()) {
			if (burnTime == 0 && canSmelt()) {
				currentItemBurnTime = burnTime = TileIronAlloyFurnace.getItemBurnTime(fuelStack);
				if (burnTime > 0) {
					updateInventory = true;
					if (!fuelStack.isEmpty()) {
						decrStackSize(fuel, 1);
					}
				}
			}
			if (isBurning() && canSmelt()) {
				if(!isActive()){
					setActive(true);
				}
				++cookTime;
				if (cookTime == 200) {
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

	public void setActive(boolean active) {
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockMachineBase) {
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockMachineBase.ACTIVE, active));
		}
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
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
			.filterSlot(0, 47, 17,
				stack -> RecipeHandler.recipeList.stream()
					.anyMatch(recipe -> recipe instanceof AlloySmelterRecipe
						&& ItemUtils.isInputEqual(recipe.getInputs().get(0), stack, true, true, true)))
			.filterSlot(1, 65, 17,
				stack -> RecipeHandler.recipeList.stream()
					.anyMatch(recipe -> recipe instanceof AlloySmelterRecipe
						&& ItemUtils.isInputEqual(recipe.getInputs().get(1), stack, true, true, true)))
			.outputSlot(2, 116, 35).fuelSlot(3, 56, 53).syncIntegerValue(this::getBurnTime, this::setBurnTime)
			.syncIntegerValue(this::getCookTime, this::setCookTime)
			.syncIntegerValue(this::getCurrentItemBurnTime, this::setCurrentItemBurnTime).addInventory().create(this);
	}
	
	public int getBurnTime() {
		return this.burnTime;
	}

	public void setBurnTime(final int burnTime) {
		this.burnTime = burnTime;
	}

	public int getCurrentItemBurnTime() {
		return this.currentItemBurnTime;
	}

	public void setCurrentItemBurnTime(final int currentItemBurnTime) {
		this.currentItemBurnTime = currentItemBurnTime;
	}

	public int getCookTime() {
		return this.cookTime;
	}

	public void setCookTime(final int cookTime) {
		this.cookTime = cookTime;
	}
}
