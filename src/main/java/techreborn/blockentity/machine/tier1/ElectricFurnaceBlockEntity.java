/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.blockentity.machine.tier1;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import team.reborn.energy.EnergySide;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.Optional;

public class ElectricFurnaceBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

	public RebornInventory<ElectricFurnaceBlockEntity> inventory = new RebornInventory<>(3, "ElectricFurnaceBlockEntity", 64, this);
	int inputSlot = 0;
	int outputSlot = 1;
	int ticksSinceLastChange;
	private SmeltingRecipe currentRecipe;
	private int cookTime;
	private int cookTimeTotal;
	// Energy cost per tick of cooking
	final int EnergyPerTick = 1;

	public ElectricFurnaceBlockEntity() {
		super(TRBlockEntities.ELECTRIC_FURNACE);
	}

	private void setInvDirty(boolean isDirty) {
		inventory.setChanged(isDirty);
	}

	private boolean isInvDirty() {
		return inventory.hasChanged();
	}

	private void updateCurrentRecipe() {
		if (inventory.getStack(inputSlot).isEmpty()) {
			resetCrafter();
			return;
		}
		Optional<SmeltingRecipe> testRecipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, inventory, world);
		if (!testRecipe.isPresent()) {
			resetCrafter();
			return;
		}
		if (!canAcceptOutput(testRecipe.get(), outputSlot)) {
			resetCrafter();
		}
		currentRecipe = testRecipe.get();
		cookTime = 0;
		cookTimeTotal = Math.max((int) (currentRecipe.getCookTime() * (1.0 - getSpeedMultiplier())), 1);
		updateState();
	}

	private boolean canAcceptOutput(SmeltingRecipe recipe, int slot) {
		ItemStack recipeOutput = recipe.getOutput();
		if (recipeOutput.isEmpty()) {
			return false;
		}
		if (inventory.getStack(slot).isEmpty()) {
			return true;
		}
		if (ItemUtils.isItemEqual(inventory.getStack(slot), recipeOutput, true, true)) {
			return recipeOutput.getCount() + inventory.getStack(slot).getCount() <= recipeOutput.getMaxCount();
		}
		return false;
	}

	public boolean canCraftAgain() {
		if (inventory.getStack(inputSlot).isEmpty()) {
			return false;
		}
		if (currentRecipe == null) {
			return false;
		}
		if (!canAcceptOutput(currentRecipe, outputSlot)) {
			return false;
		}
		return !(getEnergy() < currentRecipe.getCookTime() * getEuPerTick(EnergyPerTick));
	}

	private void resetCrafter() {
		currentRecipe = null;
		cookTime = 0;
		cookTimeTotal = 0;
		updateState();
	}

	private void updateState() {
		Block furnaceBlock = getWorld().getBlockState(pos).getBlock();

		if (furnaceBlock instanceof BlockMachineBase) {
			BlockMachineBase blockMachineBase = (BlockMachineBase) furnaceBlock;
			boolean isActive = currentRecipe != null || canCraftAgain();
			blockMachineBase.setActive(isActive, world, pos);
		}
		world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	}

	private boolean hasAllInputs(SmeltingRecipe recipe) {
		if (recipe == null) {
			return false;
		}
		if (inventory.getStack(inputSlot).isEmpty()) {
			return false;
		}

		return recipe.matches(inventory, world);
	}

	private void craftRecipe(SmeltingRecipe recipe) {
		if (recipe == null) {
			return;
		}
		if (!canAcceptOutput(recipe, outputSlot)) {
			return;
		}
		ItemStack outputStack = inventory.getStack(outputSlot);
		if (outputStack.isEmpty()) {
			inventory.setStack(outputSlot, recipe.getOutput().copy());
		} else {
			// Just increment. We already checked stack match and stack size
			outputStack.increment(1);
		}

		inventory.getStack(inputSlot).decrement(1);
	}

	public int getProgressScaled(int scale) {
		if (cookTimeTotal != 0) {
			return cookTime * scale / cookTimeTotal;
		}
		return 0;
	}

	public int getCookTime() {
		return cookTime;
	}

	public void setCookTime(int cookTime) {
		this.cookTime = cookTime;
	}

	public int getCookTimeTotal() {
		return cookTimeTotal;
	}

	public void setCookTimeTotal(int cookTimeTotal) {
		this.cookTimeTotal = cookTimeTotal;
	}

	// TilePowerAcceptor
	@Override
	public void tick() {
		super.tick();
		charge(2);

		if (world == null || world.isClient) {
			return;
		}

		ticksSinceLastChange++;
		// Force a has chanced every second
		if (ticksSinceLastChange == 20) {
			setInvDirty(true);
			ticksSinceLastChange = 0;
		}

		if (isInvDirty()) {
			if (currentRecipe == null) {
				updateCurrentRecipe();
			}
			if (currentRecipe != null && (!hasAllInputs(currentRecipe) || !canAcceptOutput(currentRecipe, outputSlot))) {
				resetCrafter();
			}
		}

		if (currentRecipe != null) {
			if (cookTime >= cookTimeTotal && hasAllInputs(currentRecipe)) {
				craftRecipe(currentRecipe);
				updateCurrentRecipe();
			} else if (cookTime < cookTimeTotal) {
				if (getStored(EnergySide.UNKNOWN) > getEuPerTick(EnergyPerTick)) {
					useEnergy(getEuPerTick(EnergyPerTick));
					cookTime++;
					if (cookTime == 1 || cookTime % 20 == 0 && RecipeCrafter.soundHanlder != null) {
						RecipeCrafter.soundHanlder.playSound(false, this);
					}
				}
			}
		}
		setInvDirty(false);
	}

	@Override
	protected boolean canProvideEnergy(EnergySide side) {
		return false;
	}

	@Override
	public double getBaseMaxPower() {
		return TechRebornConfig.electricFurnaceMaxEnergy;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return TechRebornConfig.electricFurnaceMaxInput;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return TRContent.Machine.ELECTRIC_FURNACE.getStack();
	}

	// InventoryProvider
	@Override
	public RebornInventory<ElectricFurnaceBlockEntity> getInventory() {
		return inventory;
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("electricfurnace").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).slot(0, 55, 45).outputSlot(1, 101, 45).energySlot(2, 8, 72).syncEnergyValue()
				.sync(this::getCookTime, this::setCookTime).sync(this::getCookTimeTotal, this::setCookTimeTotal).addInventory().create(this, syncID);
	}
}
