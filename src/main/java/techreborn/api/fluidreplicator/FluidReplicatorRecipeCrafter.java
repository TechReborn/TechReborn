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

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import net.minecraft.fluid.Fluid;
import io.github.prospector.silk.fluid.FluidInstance;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.blockentity.machine.multiblock.FluidReplicatorBlockEntity;

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
	 * @param parent BlockEntity Reference to the blockEntity having this crafter
	 * @param inventory Inventory reference to inventory used for crafting
	 * @param inputSlots This is the list of the slots that the crafting logic should look for the input UU-Matter.
	 * @param outputSlots This is the list of slots that the crafting logic should look for output fluid
	 */
	public FluidReplicatorRecipeCrafter(BlockEntity parent, RebornInventory<?> inventory, int[] inputSlots, int[] outputSlots) {
		super(ModRecipes.FLUID_REPLICATOR, parent, 1, 1, inventory, inputSlots, outputSlots);
	}
	
	/**
	 * FluidReplicatorRecipe version of hasAllInputs
	 */
	private boolean hasAllInputs(FluidReplicatorRecipe recipe) {
		if (recipe == null) {
			return false;
		}
		ItemStack inputStack = inventory.getInvStack(inputSlots[0]);
		if (!inputStack.isItemEqualIgnoreDamage(TRContent.Parts.UU_MATTER.getStack())) {
			return false;
		}
		if (inputStack.getCount() < recipe.getInput()) {
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
		if (blockEntity.getWorld().isClient) {
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
				FluidReplicatorBlockEntity blockEntityFluidReplicator =  (FluidReplicatorBlockEntity) blockEntity;
				// Checks to see if it can fit the output
				// And fill tank with replicated fluid
				if (blockEntityFluidReplicator.tank.canFit(currentRecipe.getFluid(), 1000) && currentRecipe.onCraft(blockEntityFluidReplicator)) {
					blockEntityFluidReplicator.tank.getFluidInstance().setFluid(currentRecipe.getFluid());
					blockEntityFluidReplicator.tank.getFluidInstance().addAmount(1000);
					// This uses all the inputs
					useAllInputs();
					// Reset
					currentRecipe = null;
					currentTickTime = 0;
					updateCurrentRecipe();
					//Update active state if the blockEntity isnt going to start crafting again
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
						soundHanlder.playSound(false, blockEntity);
					}
				}
			}
		}
		setInvDirty(false);
	}
	
	@Override
	public void updateCurrentRecipe() {
		FluidReplicatorBlockEntity blockEntityFluidReplicator =  (FluidReplicatorBlockEntity) blockEntity;
		for (FluidReplicatorRecipe recipe : FluidReplicatorRecipeList.recipes) {
			if (recipe.canCraft(blockEntityFluidReplicator) && hasAllInputs(recipe)) {
				if (!blockEntityFluidReplicator.tank.canFit(recipe.getFluid(), 1000)) {
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
			inventory.shrinkSlot(inputSlots[0], currentRecipe.getInput());
		}
	}
	
	@Override
	public boolean canCraftAgain() {
		FluidReplicatorBlockEntity blockEntityFluidReplicator =  (FluidReplicatorBlockEntity) blockEntity;
		for (FluidReplicatorRecipe recipe : FluidReplicatorRecipeList.recipes) {
			if (recipe.canCraft(blockEntityFluidReplicator) && hasAllInputs(recipe)) {
				if (!blockEntityFluidReplicator.tank.canFit(recipe.getFluid(), 1000)) {
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