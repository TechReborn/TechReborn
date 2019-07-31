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

package techreborn.tiles.tier1;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import com.google.common.collect.ImmutableList;
import reborncore.api.IToolDrop;
import reborncore.api.praescriptum.ingredients.output.ItemStackOutputIngredient;
import reborncore.api.praescriptum.ingredients.output.OutputIngredient;
import reborncore.api.praescriptum.recipes.Recipe;
import reborncore.api.praescriptum.recipes.RecipeHandler;
import reborncore.api.tile.IInventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class TileMachine extends TilePowerAcceptor implements IToolDrop, IInventoryProvider, IContainerProvider {
	// Constructors >>
	public TileMachine(String name, int maxInput, int maxEnergy, int slots, RecipeHandler recipeHandler) {
		this(name, maxInput, maxEnergy, slots, 64, recipeHandler);
	}

	public TileMachine(String name, int maxInput, int maxEnergy, int slots, int slotSize, RecipeHandler recipeHandler) {
		this(name, maxInput, maxEnergy, slots, 64, new int[] { 0 }, new int[] { 1 }, recipeHandler);
	}

	public TileMachine(String name, int maxInput, int maxEnergy, int slots, int slotSize, int[] inputSlots, int[] outputSlots, RecipeHandler recipeHandler) {
		this.name = "Tile" + name;
		this.maxInput = maxInput;
		this.maxEnergy = maxEnergy;
		this.inventory = new Inventory(slots, name, slotSize, this);
		this.inputSlots = inputSlots;
		this.outputSlots = outputSlots;
		this.recipeHandler = recipeHandler;
	}
	// << Constructors

	// TileEntity >>
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);

		NBTTagCompound data = tag.getCompoundTag("TileMachine");
		progress = data.hasKey("progress") ? data.getInteger("progress") : 0;
		isActive = data.hasKey("isActive") && data.getBoolean("isActive");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);

		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("progress", this.progress);
		data.setBoolean("isActive", this.isActive);
		tag.setTag("TileMachine", data);

		return tag;
	}

	@Override
	public void update() {
		if (world.isRemote) return;

		boolean needsInventoryUpdate = false; // To reduce update frequency of inventories, only call onInventory changed if this was set to true

		// operation
		if (canWork()) { // operation conditions satisfied
			if (!isActive) {
				isActive = true;
				setActive(true);
			}

			// process start
			if (progress == 0) needsInventoryUpdate = true;

			int progressNeeded = Math.max((int) ((double) recipe.getOperationDuration() * (1.0D - this.getSpeedMultiplier())), 1);
			if (progress < progressNeeded) {
				useEnergy(getEuPerTick(recipe.getEnergyCostPerTick())); // use energy
				progress += 1; // update progress
			}

			if (progress >= progressNeeded) { // process end
				finishWork();

				needsInventoryUpdate = true;
			}
		} else { // operation conditions not satisfied
			reset();

			if (isActive) {
				isActive = false;
				setActive(false);
			}
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
		ImmutableList<ItemStack> inputs = Arrays.stream(inputSlots)
			.mapToObj(inventory::getStackInSlot)
			.filter(contents -> !contents.isEmpty())
			.collect(ImmutableList.toImmutableList());

		if (inputs.isEmpty()) return false;

		if (recipe != null) {
			boolean canUse = recipeHandler.apply(recipe, inputs, ImmutableList.of(), true);
			if (!canUse) reset(); // we cannot use the current recipe so reset
		}

		Optional<Recipe> maybeRecipe = Optional.ofNullable(recipe);

		// current recipe is still usable
		if (maybeRecipe.isPresent()) {
			if (!canUseEnergy(getEuPerTick(recipe.getEnergyCostPerTick()))) return false;

			return recipe.getOutputIngredients()
				.stream()
				.filter(output -> output instanceof ItemStackOutputIngredient)
				.allMatch(hasSpaceForItemOutputs(this));
		}

		// The current recipe is not usable anymore so we need to find a new one
		maybeRecipe = recipeHandler.findRecipe(inputs, ImmutableList.of()); // try to find a matching recipe

		if (!maybeRecipe.isPresent()) return false; // could not find a recipe

		// if a matching recipe exists update parameters
		updateRecipe(maybeRecipe.get());

		// we need space for the outputs
		return recipe.getOutputIngredients()
			.stream()
			.filter(output -> output instanceof ItemStackOutputIngredient)
			.allMatch(hasSpaceForItemOutputs(this));
	}

	protected void finishWork() {
		// if the input is empty the operation cannot be completed
		// if there are no inputs the machine cannot operate
		ImmutableList<ItemStack> inputs = Arrays.stream(inputSlots)
			.mapToObj(inventory::getStackInSlot)
			.filter(contents -> !contents.isEmpty())
			.collect(ImmutableList.toImmutableList());

		if (inputs.isEmpty()) return;

		// adjust input
		recipeHandler.apply(recipe, inputs, ImmutableList.of(), false);

		// adjust output
		recipe.getOutputIngredients()
			.stream()
			.filter(entry -> entry instanceof ItemStackOutputIngredient)
			.forEach(entry -> addOutput(((ItemStack) entry.ingredient).copy(), 1));

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

	protected void setActive(boolean value) {
		Block block = world.getBlockState(pos).getBlock();

		if (block instanceof BlockMachineBase)
			((BlockMachineBase) block).setActive(value, world, pos);
	}
	// << TileMachine

	// IInventoryProvider >>
	@Override
	public Inventory getInventory() {
		return inventory;
	}
	// << IInventoryProvider

	// Getters && Setters >>
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
	// << Getters && Setters

	// Helpers >>
	public static Predicate<OutputIngredient> hasSpaceForItemOutputs(TileMachine tileMachine) {
		return ingredient -> {
			ItemStack stack = (ItemStack) ingredient.ingredient;
			return !ItemUtils.isEmpty(stack) && Arrays.stream(tileMachine.outputSlots)
				.anyMatch(slot -> {
					ItemStack contents = tileMachine.inventory.getStackInSlot(slot);
					if (contents.isEmpty())
						return true;

					return ItemUtils.isItemEqual(contents, stack, true, true) &&
						stack.getCount() + contents.getCount() <= stack.getMaxStackSize();
				});
		};
	}

	public void addOutput(ItemStack stack, int slot) {// This fits a stack into a slot
		if (stack.isEmpty()) return;

		ItemStack contents = inventory.getStackInSlot(slot);

		if (contents.isEmpty()) {
			inventory.setInventorySlotContents(slot, stack);
			return;
		}

		if (ItemUtils.isItemEqual(contents, stack, true, true)) {
			if (contents.getCount() + stack.getCount() <= contents.getMaxStackSize())
				ItemUtils.increaseSize(contents, stack.getCount());
		}
	}
	// << Helpers

	// Fields >>
	public final String name;
	public final int maxInput;
	public final int maxEnergy;
	public final Inventory inventory;
	public final RecipeHandler recipeHandler;

	// Slots
	protected final int[] inputSlots;
	protected final int[] outputSlots;

	protected int progress = 0;
	protected int operationLength = 0;
	protected Recipe recipe = null;
	protected boolean isActive = false;
	// << Fields
}
