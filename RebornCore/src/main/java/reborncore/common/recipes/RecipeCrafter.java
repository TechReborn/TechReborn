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
	public PowerAcceptorBlockEntity energy;

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
	public long lastSoundTime = 0;
	private long cachedWorldTime = 0;
	public int currentTickTime = 0;
	public int currentNeededTicks = 1;// Set to 1 to stop rare crashes

	int ticksSinceLastChange;

	@Nullable
	public static ICrafterSoundHandler soundHandler = (firstRun, blockEntity) -> {
	};

	public RecipeCrafter(RecipeType<? extends RebornRecipe> recipeType, BlockEntity blockEntity, int inputs, int outputs, RebornInventory<?> inventory,
						int[] inputSlots, int[] outputSlots) {
		this.recipeType = recipeType;
		this.blockEntity = blockEntity;
		if (blockEntity instanceof PowerAcceptorBlockEntity powerAcceptor) {
			energy = powerAcceptor;
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
			RebornCore.LOGGER.error(blockEntity.getClass().getName() + " does not use IRecipeCrafterProvider report this to the issue tracker!");
		}
	}

	/**
	 * Call this on the blockEntity tick
	 */
	public void updateEntity() {
		if (blockEntity.getLevel() == null || blockEntity.getLevel().isClientSide()) {
			return;
		}
		ticksSinceLastChange++;
		if (cachedWorldTime == 0){
			cachedWorldTime = blockEntity.getLevel().getGameTime();
		}
		cachedWorldTime++;
		// Force a has chanced every second
		if (ticksSinceLastChange == 20) {
			setInvDirty(true);
			ticksSinceLastChange = 0;
			setIsActive();
		}
		// It will now look for new recipes.
		if (currentRecipe == null && isInvDirty()) {
			updateCurrentRecipe();
		}
		if (currentRecipe != null) {
			// If it doesn't have all the inputs reset
			if (isInvDirty() && !hasAllInputs() || energy.hasMultiblock() && !energy.isShapeValid()) {
				currentRecipe = null;
				currentTickTime = 0;
				setIsActive();
			}
			// If it has reached the recipe tick time
			if (currentRecipe != null && currentTickTime >= currentNeededTicks && hasAllInputs()) {
				final List<ItemStack> outputs = currentRecipe.outputs().stream().map(ItemStackTemplate::create).toList();

				boolean canGiveInvAll = true;
				// Checks to see if it can fit the output
				for (int i = 0; i < outputs.size(); i++) {
					if (!canFitOutput(outputs.get(i), outputSlots[i])) {
						canGiveInvAll = false;
					}
				}
				// The slots that have been filled
				ArrayList<Integer> filledSlots = new ArrayList<>();
				if (canGiveInvAll && currentRecipe.onCraft(blockEntity)) {
					for (int i = 0; i < outputs.size(); i++) {
						// Checks it has not been filled
						if (!filledSlots.contains(outputSlots[i])) {
							// Fills the slot with the output stack
							fitStack(outputs.get(i).copy(), outputSlots[i]);
							filledSlots.add(outputSlots[i]);
						}
					}
					// This uses all the inputs
					useAllInputs();
					// Reset
					currentRecipe = null;
					currentTickTime = 0;
					updateCurrentRecipe();
					// Update active state if the blockEntity isn't going to start crafting again
					if (currentRecipe == null) {
						setIsActive();
					}
				}
			} else if (currentRecipe != null && currentTickTime < currentNeededTicks) {
				long useRequirement = getEuPerTick(currentRecipe.power());
				if (energy.tryUseExact(useRequirement)) {
					currentTickTime++;
					if ((currentTickTime == 1 || currentTickTime % 20 == 0 && cachedWorldTime > lastSoundTime+ 10) && soundHandler != null && !isMuffled()) {
						lastSoundTime = cachedWorldTime;
						soundHandler.playSound(false, blockEntity);
					}
				}
			}
		}
		setInvDirty(false);
	}

	/**
	 * Checks that we have all inputs, can fit output and update max tick time and current tick time
	 */
	public void updateCurrentRecipe() {
		currentTickTime = 0;
		for (RebornRecipe recipe : RecipeUtils.getRecipes(blockEntity.getLevel(), recipeType)) {
			// This checks to see if it has all the inputs
			if (!hasAllInputs(recipe)) continue;
			if (!recipe.canCraft(blockEntity)) continue;

			final List<ItemStack> outputs = recipe.outputs().stream().map(ItemStackTemplate::create).toList();

			// This checks to see if it can fit all the outputs
			boolean hasOutputSpace = true;
			for (int i = 0; i < outputs.size(); i++) {
				if (!canFitOutput(outputs.get(i), outputSlots[i])) {
					hasOutputSpace = false;
				}
			}
			if (!hasOutputSpace) continue;
			// Sets the current recipe then syncs
			setCurrentRecipe(recipe);
			this.currentNeededTicks = Math.max((int) (currentRecipe.time() * (1.0 - getSpeedMultiplier())), 1);
			setIsActive();
			return;
		}
		setCurrentRecipe(null);
		currentNeededTicks = 0;
		setIsActive();
	}

	public boolean hasAllInputs() {
		return hasAllInputs(currentRecipe);
	}

	public boolean hasAllInputs(RebornRecipe recipeType) {
		if (recipeType == null) {
			return false;
		}
		for (SizedIngredient ingredient : recipeType.ingredients()) {
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

	public void useAllInputs() {
		if (currentRecipe == null) {
			return;
		}
		for (SizedIngredient ingredient : currentRecipe.ingredients()) {
			for (int inputSlot : inputSlots) {// Uses all the inputs
				if (ingredient.test(inventory.getItem(inputSlot))) {
					inventory.shrinkSlot(inputSlot, ingredient.count());
					break;
				}
			}
		}
	}

	public boolean canFitOutput(ItemStack stack, int slot) {// Checks to see if it can fit the stack
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

	public void read(ValueInput view) {
		view.child("Crater").ifPresent(data -> {
			currentTickTime = data.getIntOr("currentTickTime", 0);
		});

		if (blockEntity != null && blockEntity.getLevel() != null && blockEntity.getLevel().isClientSide()) {
			blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(),
					blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()),
					blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()), 3);
		}
	}

	public void write(ValueOutput view) {
		view.child("Crater").putDouble("currentTickTime", currentTickTime);
	}

	private boolean isActive() {
		return currentRecipe != null && energy.getEnergy() >= currentRecipe.power();
	}

	public boolean canCraftAgain() {
		for (RebornRecipe recipe : RecipeUtils.getRecipes(blockEntity.getLevel(), recipeType)) {
			if (recipe.canCraft(blockEntity) && hasAllInputs(recipe)) {
				final List<ItemStack> outputs = recipe.outputs().stream().map(ItemStackTemplate::create).toList();

				for (int i = 0; i < outputs.size(); i++) {
					if (!canFitOutput(outputs.get(i), outputSlots[i])) {
						return false;
					}
				}
				return !(energy.getEnergy() < recipe.power());
			}
		}
		return false;
	}

	public void setIsActive() {
		BlockPos pos = blockEntity.getBlockPos();
		if (blockEntity.getLevel() == null) return;
		BlockState oldState  = blockEntity.getLevel().getBlockState(pos);
		if (oldState.getBlock() instanceof BlockMachineBase blockMachineBase) {
			boolean isActive = isActive() || canCraftAgain();

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
		return Math.min(power, energy.getMaxStoredPower());
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
