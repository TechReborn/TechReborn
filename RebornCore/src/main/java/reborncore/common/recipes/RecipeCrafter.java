/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.recipes;

import net.minecraft.world.item.ItemStackTemplate;
import org.jspecify.annotations.Nullable;
import reborncore.RebornCore;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.SizedIngredient;
import reborncore.common.crafting.RecipeUtils;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * Use this in your blockEntity entity to craft things
 */
public class RecipeCrafter implements IUpgradeHandler {

	/**
	 * This is the recipe type to use
	 */
	public RecipeType<? extends RebornRecipe> recipeType;

	/**
	 * This is the parent blockEntity
	 */
	public BlockEntity blockEntity;

	/**
	 * This is the place to use the power from
	 */
	public PowerAcceptorBlockEntity poweredMachine;

	public Optional<IUpgradeHandler> parentUpgradeHandler = Optional.empty();

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
	public RebornInventory<?> inventory;

	/**
	 * This is the list of the slots that the crafting logic should look for the
	 * input item stacks.
	 */
	public int[] inputSlots;

	/**
	 * This is the list for the slots that the crafting logic should look fot
	 * the output item stacks.
	 */
	public int[] outputSlots;
	public RebornRecipe currentRecipe;
	/**
	 *  Current progress of crafting
	 */
	public int currentTickTime = 0;
	/**
	 *  Recipe crafting time modified by upgrades
	 */
	public int currentNeededTicks = 1;// Set to 1 to stop rare crashes

	int ticksSinceLastChange = 0;

	@Nullable
	public static ICrafterSoundHandler soundHandler = (firstRun, blockEntity) -> {};

	public RecipeCrafter(RecipeType<? extends RebornRecipe> recipeType, BlockEntity blockEntity, int inputs, int outputs, RebornInventory<?> inventory,
						int[] inputSlots, int[] outputSlots) {
		this.recipeType = recipeType;
		this.blockEntity = blockEntity;
		if (blockEntity instanceof PowerAcceptorBlockEntity powerAcceptor) {
			poweredMachine = powerAcceptor;
		}
		if (blockEntity instanceof IUpgradeHandler) {
			parentUpgradeHandler = Optional.of((IUpgradeHandler) blockEntity);
		}
		this.inputs = inputs;
		this.outputs = outputs;
		this.inventory = inventory;
		this.inputSlots = inputSlots;
		this.outputSlots = outputSlots;
		if (!(blockEntity instanceof IRecipeCrafterProvider)) {
			RebornCore.LOGGER.error("{} does not use IRecipeCrafterProvider report this to the issue tracker!", blockEntity.getClass().getName());
		}
	}

	/**
	 * Call this on the blockEntity tick
	 */
	public void updateEntity() {
		if (blockEntity.getLevel() == null || blockEntity.getLevel().isClientSide()) {
			return;
		}
		// Force a has changed every second
		ticksSinceLastChange++;
		if (ticksSinceLastChange == 200) {
			setInvDirty(true);
			ticksSinceLastChange = 0;
		}

		// Force check recipe
		if (isInvDirty()) {
			updateCurrentRecipe();
		}

		// No recipe - nothing to do
		if (currentRecipe == null) {
			setInvDirty(false);
			setIsActive();
			return;
		}

		// We have recipe beyond this point
		// Check multiblock
		if (poweredMachine.hasMultiblock() && !poweredMachine.isShapeValid()) {
			setInvDirty(false);
			resetCrafter();
			setIsActive();
			return;
		}

		// Update blockState
		setIsActive();

		// Do the work
		if (currentNeededTicks > 0 && currentTickTime >= currentNeededTicks) {
			completeCraft();
		} else {
			progressCraft();
		}
	}

	/**
	 * Checks that we can craft recipe and update recipe, max tick time and current tick time
	 */
	public void updateCurrentRecipe() {
		for (RebornRecipe recipe : RecipeUtils.getRecipes(blockEntity.getLevel(), recipeType)) {
			if (!isValidRecipe(recipe)) continue;

			// Reset progress if recipe changed
			if (currentRecipe != recipe) {
				currentTickTime = 0;
			}
			// Sets the current recipe
			setCurrentRecipe(recipe);
			this.currentNeededTicks = Math.max((int) (currentRecipe.time() * (1.0 - getSpeedMultiplier())), 1);
			return;
		}

		// No matching recipe. Reset.
		resetCrafter();
	}

	/**
	 * Checks that we have all inputs, can fit output and has energy
	 */
	protected boolean isValidRecipe(RebornRecipe recipe) {
		// Check input
		if (!hasAllInputs()) return false;

		// Machine specific checks, like multiblock
		if (!recipe.canCraft(blockEntity)) return false;

		// Check output slot
		final List<ItemStack> outputs = recipe.outputs().stream().map(ItemStackTemplate::create).toList();
		for (int i = 0; i < outputs.size(); i++) {
			if (!canFitOutput(outputs.get(i), outputSlots[i])) {
				return false;
			}
		}

		//check power
		return (recipe.power() <= poweredMachine.getEnergy());
	}

	/**
	 * Wrapper for an actual hasAllInputs()
	 */
	public boolean hasAllInputs() {
		return hasAllInputs(currentRecipe);
	}

	/**
	 * Checks if machine has all input ingredients
	 *
	 * @param recipe Recipe to check for, if null returns false
	 * @return Returns true if all inputs are there
	 */
	public boolean hasAllInputs(RebornRecipe recipe) {
		if (recipe == null) {
			return false;
		}
		for (SizedIngredient ingredient : recipe.ingredients()) {
			boolean hasItem = false;
			for (int slot : inputSlots) {
				if (ingredient.test(inventory.getItem(slot))) {
					hasItem = true;
				}
			}
			if (!hasItem) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks to see if it can fit the stack into the output slot, used to check if we can craft the recipe
	 *
	 * @param stack ItemStack to check
	 * @param slot Slot to check
	 * @return Returns true if the stack can fit in the output slot, false if it can't
	 */
	public boolean canFitOutput(ItemStack stack, int slot) {
		if (stack.isEmpty()) {
			return true;
		}
		if (inventory.getItem(slot).isEmpty()) {
			return true;
		}
		if (ItemUtils.isItemEqual(inventory.getItem(slot), stack, true, true)) {
			return stack.getCount() + inventory.getItem(slot).getCount() <= stack.getMaxStackSize();
		}
		return false;
	}

	/**
	 * Consumes energy and makes some noise.
	 */
	private void progressCraft() {
		long useRequirement = getEuPerTick(currentRecipe.power());
		if (poweredMachine.tryUseExact(useRequirement)) {
			currentTickTime++;
			if ((currentTickTime == 1 || currentTickTime % 10 == 0) && soundHandler != null && !isMuffled()) {
				soundHandler.playSound(false, blockEntity);
			}
		}
	}

	/**
	 * An actual craft. Consumes ingredients and produces recipe outputs.
	 */
	protected void completeCraft() {
		final List<ItemStack> outputs = currentRecipe.outputs().stream().map(ItemStackTemplate::create).toList();
		ArrayList<Integer> filledSlots = new ArrayList<>();

		// Check machine-specific on craft logic
		if (!currentRecipe.onCraft(blockEntity)) {
			return;
		}

		// Avoid writing the same output slot multiple times for recipes that share slot targets.
		for (int i = 0; i < outputs.size(); i++) {
			if (!filledSlots.contains(outputSlots[i])) {
				fitStack(outputs.get(i).copy(), outputSlots[i]);
				filledSlots.add(outputSlots[i]);
			}
		}

		useAllInputs();
		currentTickTime = 0;
	}

	/**
	 * Resets recipe and progress
	 */
	protected void resetCrafter(){
		currentTickTime = 0;
		currentNeededTicks = 0;
		setCurrentRecipe(null);
	}

	/**
	 * Puts recipe output to the output slot.
	 *
	 * @param stack ItemStack to put
	 * @param slot Slot to put
	 */
	public void fitStack(ItemStack stack, int slot) {// This fits a stack into a slot
		if (stack.isEmpty()) {
			return;
		}
		if (inventory.getItem(slot).isEmpty()) {// If the slot is empty set the contents
			inventory.setItem(slot, stack);
			return;
		}
		if (ItemStack.isSameItemSameComponents(inventory.getItem(slot), stack)) {// If the slot has stuff in
			if (stack.getCount() + inventory.getItem(slot).getCount() <= stack.getMaxStackSize()) {// Check to see if it fits
				ItemStack newStack = stack.copy();
				// Sets the new stack size
				newStack.setCount(inventory.getItem(slot).getCount() + stack.getCount());
				inventory.setItem(slot, newStack);
			}
		}
	}

	/**
	 * Consumes the recipe ingredients, should be called after crafting is complete
	 */
	public void useAllInputs() {
		for (SizedIngredient ingredient : currentRecipe.ingredients()) {
			for (int inputSlot : inputSlots) {// Uses all the inputs
				if (ingredient.test(inventory.getItem(inputSlot))) {
					inventory.shrinkSlot(inputSlot, ingredient.count());
					break;
				}
			}
		}
	}

	/**
	 * NBT Stuff
	 */
	public void read(ValueInput view) {
		if (view.child("Crafter").isPresent()) {
			view.child("Crafter").ifPresent(data -> currentTickTime = data.getIntOr("currentTickTime", 0));
		} else {
			// Fix for typo
			view.child("Crater").ifPresent(data -> currentTickTime = data.getIntOr("currentTickTime", 0));
		}

		if (blockEntity != null && blockEntity.getLevel() != null && blockEntity.getLevel().isClientSide()) {
			blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(),
				blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()),
				blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()), 3);
		}
	}

	/**
	 * NBT Stuff
	 */
	public void write(ValueOutput view) {
		view.child("Crafter").putDouble("currentTickTime", currentTickTime);
	}

	/**
	 * @return Returns true if the crafter has a valid recipe and is currently crafting, false otherwise
	 */
	private boolean isActive() {
		if (currentRecipe == null) return false;
		if (getEuPerTick(currentRecipe.power()) > poweredMachine.getStored()) return false;
		//return isValidRecipe(currentRecipe) &&  currentTickTime > 0;
		return currentTickTime > 0;
	}

	/**
	 * Sets the block state active property based on if the crafter is active or not
	 */
	public void setIsActive() {
		BlockPos pos = blockEntity.getBlockPos();
		if (blockEntity.getLevel() == null) return;
		BlockState oldState = blockEntity.getLevel().getBlockState(pos);
		if (oldState.getBlock() instanceof BlockMachineBase blockMachineBase) {
			boolean isActive = isActive();

			if (isActive == oldState.getValue(BlockMachineBase.ACTIVE)) {
				return;
			}

			blockMachineBase.setActive(isActive, blockEntity.getLevel(), pos);
			blockEntity.getLevel().sendBlockUpdated(pos, oldState, blockEntity.getLevel().getBlockState(pos), 3);
		}
	}

	public void setCurrentRecipe(RebornRecipe recipe) {
		this.currentRecipe = recipe;
	}

	public boolean isInvDirty() {
		return inventory.hasChanged();
	}

	public void setInvDirty(boolean isDirty) {
		inventory.setHashChanged(isDirty);
	}

	/**
	 * Slot I\O stuff
	 */
	public boolean isStackValidInput(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		// Test with a stack with the max stack size as some independents will check the stack size.
		// A bit of a hack but should work.
		ItemStack largeStack = stack.copy();
		largeStack.setCount(largeStack.getMaxStackSize());
		for (RebornRecipe recipe : RecipeUtils.getRecipes(blockEntity.getLevel(), recipeType)) {
			for (SizedIngredient ingredient : recipe.ingredients()) {
				if (ingredient.test(largeStack)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void resetSpeedMultiplier() {
		parentUpgradeHandler.ifPresent(IUpgradeHandler::resetSpeedMultiplier);
	}

	@Override
	public double getSpeedMultiplier() {
		return Math.min(parentUpgradeHandler.map(IUpgradeHandler::getSpeedMultiplier).orElse(0D), 0.99);
	}

	@Override
	public void addPowerMultiplier(double amount) {
		parentUpgradeHandler.ifPresent(iUpgradeHandler -> iUpgradeHandler.addPowerMultiplier(amount));
	}

	@Override
	public void resetPowerMultiplier() {
		parentUpgradeHandler.ifPresent(IUpgradeHandler::resetPowerMultiplier);
	}

	@Override
	public double getPowerMultiplier() {
		return parentUpgradeHandler.map(IUpgradeHandler::getPowerMultiplier).orElse(1D);
	}

	@Override
	public long getEuPerTick(long baseEu) {
		long power = parentUpgradeHandler.map(iUpgradeHandler -> iUpgradeHandler.getEuPerTick(baseEu)).orElse(1L);
		return Math.min(power, poweredMachine.getMaxStoredPower());
	}

	@Override
	public void addSpeedMultiplier(double amount) {
		parentUpgradeHandler.ifPresent(iUpgradeHandler -> iUpgradeHandler.addSpeedMultiplier(amount));
	}

	@Override
	public void muffle() {
		parentUpgradeHandler.ifPresent(IUpgradeHandler::muffle);
	}

	@Override
	public void resetMuffler() {
		parentUpgradeHandler.ifPresent(IUpgradeHandler::resetMuffler);
	}

	@Override
	public boolean isMuffled() {
		return parentUpgradeHandler.map(IUpgradeHandler::isMuffled).orElse(false);
	}

	@Nullable
	private RegistryAccess getDynamicRegistryManager() {
		return blockEntity.getLevel().registryAccess();
	}
}
