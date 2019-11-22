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

package techreborn.api.fluidreplicator;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import reborncore.common.fluids.RebornFluidTank;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.Inventory;

import techreborn.api.Reference;
import techreborn.init.ModItems;
import techreborn.tiles.multiblock.TileFluidReplicator;

/**
 * @author drcrazy
 *
 */
public class FluidReplicatorRecipeCrafter extends RecipeCrafter {
	
	public FluidReplicatorRecipe currentRecipe;
	int ticksSinceLastChange;

	/**
	 * RecipeCrafter for Fluid Replicator
	 * 
	 * @param parentTile TileEntity Reference to the tile having this crafter
	 * @param inventory Inventory reference to inventory used for crafting
	 * @param inputSlots This is the list of the slots that the crafting logic should look for the input UU-Matter.
	 * @param outputSlots This is the list of slots that the crafting logic should look for output fluid
	 */
	public FluidReplicatorRecipeCrafter(TileEntity parentTile, Inventory inventory, int[] inputSlots, int[] outputSlots) {
		super(Reference.FLUID_REPLICATOR_RECIPE, parentTile, 1, 1, inventory, inputSlots, outputSlots);
	}
	
	/**
	 * FluidReplicatorRecipe version of hasAllInputs
	 */
	private boolean hasAllInputs(FluidReplicatorRecipe recipe) {
		if (recipe == null) {
			return false;
		}
		ItemStack inputStack = inventory.getStackInSlot(inputSlots[0]);
		if (!inputStack.isItemEqual(new ItemStack(ModItems.UU_MATTER))) {
			return false;
		}
		if (inputStack.getCount() < recipe.getInput()) {
			return false;
		}

		return true;
	}
	
	private boolean canFit(Fluid fluid, RebornFluidTank tank) {
		if (tank.fill(new FluidStack(fluid, Fluid.BUCKET_VOLUME), false) != Fluid.BUCKET_VOLUME) {
			return false;
		}
		return true;
	}
	
	public void setCurrentRecipe(FluidReplicatorRecipe recipe) {
		try {
			this.currentRecipe = (FluidReplicatorRecipe) recipe.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	// RecipeCrafter
	@Override
	public void updateEntity() {
		if (parentTile.getWorld().isRemote) {
			return;
		}
		ticksSinceLastChange++;
		// Force a has changed every second
		if (ticksSinceLastChange >= 20) {
			setInvDirty(true);
			ticksSinceLastChange = 0;
		}
		// It will now look for new recipes.
		if (currentRecipe == null && isInvDirty()) {
			updateCurrentRecipe();
		}
		if(currentRecipe != null) {
			// If it doesn't have all the inputs reset
			if (isInvDirty() && !hasAllInputs()) {
				currentRecipe = null;
				currentTickTime = 0;
				setIsActive();
			}
			// If it has reached the recipe tick time
			if (currentRecipe != null && currentTickTime >= currentNeededTicks && hasAllInputs()) {
				TileFluidReplicator tileFluidReplicator =  (TileFluidReplicator) parentTile;
				// Checks to see if it can fit the output
				// And fill tank with replicated fluid
				if (canFit(currentRecipe.getFluid(), tileFluidReplicator.tank) && currentRecipe.onCraft(tileFluidReplicator)) {
					tileFluidReplicator.tank.fill(new FluidStack(currentRecipe.getFluid(), Fluid.BUCKET_VOLUME), true);
					// This uses all the inputs
					useAllInputs();
					// Reset
					currentRecipe = null;
					currentTickTime = 0;
					updateCurrentRecipe();
					//Update active state if the tile isnt going to start crafting again
					if(currentRecipe == null){
						setIsActive();
					}
				}
			} else if (currentRecipe != null && currentTickTime < currentNeededTicks) {
				// This uses the power
				if (energy.canUseEnergy(getEuPerTick(currentRecipe.getEuTick()))) {
					energy.useEnergy(getEuPerTick(currentRecipe.getEuTick()));
					// Increase the ticktime
					currentTickTime ++;
					if(currentTickTime == 1 || currentTickTime % 20 == 0 && soundHanlder != null){
						soundHanlder.playSound(false, parentTile);
					}
				}
			}
		}
		setInvDirty(false);
	}
	
	@Override
	public void updateCurrentRecipe() {
		TileFluidReplicator tileFluidReplicator =  (TileFluidReplicator) parentTile;
		for (FluidReplicatorRecipe recipe : FluidReplicatorRecipeList.recipes) {
			if (recipe.canCraft(tileFluidReplicator) && hasAllInputs(recipe)) {
				if (!canFit(recipe.getFluid(), tileFluidReplicator.tank)) {
					this.currentRecipe = null;
					currentTickTime = 0;
					setIsActive();
					return;					
				}
				setCurrentRecipe(recipe);
				currentNeededTicks = Math.max((int) (currentRecipe.getTickTime()* (1.0 - getSpeedMultiplier())), 1);
				currentTickTime = 0;
				setIsActive();
				return;
			}
		}
	}
	
	@Override
	public boolean hasAllInputs() {
		if (this.currentRecipe == null) {
			return false;
		}
		else {
			return hasAllInputs(this.currentRecipe);
		}
	}
	
	@Override
	public void useAllInputs() {
		if (currentRecipe == null) {
			return;
		}
		if (hasAllInputs(currentRecipe)) {
			inventory.decrStackSize(inputSlots[0], currentRecipe.getInput());	
		}
	}
	
	@Override
	public boolean canCraftAgain() {
		TileFluidReplicator tileFluidReplicator =  (TileFluidReplicator) parentTile;
		for (FluidReplicatorRecipe recipe : FluidReplicatorRecipeList.recipes) {
			if (recipe.canCraft(tileFluidReplicator) && hasAllInputs(recipe)) {
				if (!canFit(recipe.getFluid(), tileFluidReplicator.tank)) {
					return false;
				}
				if (energy.getEnergy() < recipe.getEuTick()) {
					return false;
				}
				return true;
			}
		}
		return false;
	}	
}