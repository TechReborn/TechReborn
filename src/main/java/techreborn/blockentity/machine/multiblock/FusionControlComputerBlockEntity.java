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

package techreborn.blockentity.machine.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.StringUtils;
import reborncore.common.util.Torus;
import techreborn.api.recipe.recipes.FusionReactorRecipe;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class FusionControlComputerBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {

	public int craftingTickTime = 0;
	public int neededPower = 0;
	public int size = 6;
	public int state = -1;
	int topStackSlot = 0;
	int bottomStackSlot = 1;
	int outputStackSlot = 2;
	FusionReactorRecipe currentRecipe = null;
	Identifier currentRecipeID = null;
	boolean hasStartedCrafting = false;
	boolean checkNBTRecipe = false;
	long lastTick = -1;

	public FusionControlComputerBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.FUSION_CONTROL_COMPUTER, pos, state, "FusionControlComputer", -1, -1, TRContent.Machine.FUSION_CONTROL_COMPUTER.block, -1);
		this.inventory = new RebornInventory<>(3, "FusionControlComputerBlockEntity", 64, this);
	}



	public FusionReactorRecipe getCurrentRecipeFromID() {
		if (currentRecipeID == null) return null;
		return ModRecipes.FUSION_REACTOR.getRecipes(world).stream()
				.filter(recipe -> recipe.getId().equals(currentRecipeID))
				.findFirst()
				.orElse(null);
	}

	public Text getStateText() {
		if (state == -1) {
			return Text.empty();
		} else if (state == 0) {
			return Text.translatable("gui.techreborn.fusion.norecipe");
		} else if (state == 1) {
			FusionReactorRecipe r = getCurrentRecipeFromID();
			if (r == null) {
				return Text.translatable("gui.techreborn.fusion.charging");
			}
			int percentage = percentage(r.getStartEnergy(), getEnergy());
			return Text.translatable("gui.techreborn.fusion.chargingdetailed",
				new Object[]{StringUtils.getPercentageText(percentage)}
			);
		} else if (state == 2) {
			return Text.translatable("gui.techreborn.fusion.crafting");
		}
		return Text.empty();
	}

	/**
	 * Changes size of fusion reactor ring after GUI button click
	 *
	 * @param sizeDelta {@code int} Size increment
	 */
	public void changeSize(int sizeDelta) {
		int newSize = size + sizeDelta;
		this.size = Math.max(6, Math.min(TechRebornConfig.fusionControlComputerMaxCoilSize, newSize));
	}

	/**
	 * Resets crafter progress and recipe
	 */
	private void resetCrafter() {
		currentRecipe = null;
		craftingTickTime = 0;
		neededPower = 0;
		hasStartedCrafting = false;
	}

	/**
	 * Checks that {@link ItemStack} could be inserted into slot provided, including check
	 * for existing item in slot and maximum stack size
	 *
	 * @param stack {@link ItemStack} Stack to insert
	 * @param slot  {@code int} Slot ID to check
	 * @param tags  {@code boolean} Should we use tags
	 * @return {@code boolean} Returns true if ItemStack will fit into slot
	 */
	public boolean canFitStack(ItemStack stack, int slot, boolean tags) {// Checks to see if it can
		// fit the stack
		if (stack.isEmpty()) {
			return true;
		}
		if (inventory.getStack(slot).isEmpty()) {
			return true;
		}
		if (ItemUtils.isItemEqual(inventory.getStack(slot), stack, true, tags)) {
			return stack.getCount() + inventory.getStack(slot).getCount() <= stack.getMaxCount();
		}
		return false;
	}

	/**
	 * Tries to set current recipe based in inputs in reactor
	 */
	private void updateCurrentRecipe() {
		for (RebornRecipe recipe : ModRecipes.FUSION_REACTOR.getRecipes(getWorld())) {
			if (validateRecipe((FusionReactorRecipe) recipe)) {
				currentRecipe = (FusionReactorRecipe) recipe;
				craftingTickTime = 0;
				neededPower = currentRecipe.getStartEnergy();
				hasStartedCrafting = false;
				break;
			}
		}
	}

	/**
	 * Validates if reactor has all inputs and can output result
	 *
	 * @param recipe {@link FusionReactorRecipe} Recipe to validate
	 * @return {@code boolean} True if we have all inputs and can fit output
	 */
	private boolean validateRecipe(FusionReactorRecipe recipe) {
		return hasAllInputs(recipe) && canFitStack(recipe.getOutputs().get(0), outputStackSlot, true);
	}

	/**
	 * Check if {@link BlockEntity} has all necessary inputs for recipe provided
	 *
	 * @param recipeType {@link RebornRecipe} Recipe to check inputs
	 * @return {@code boolean} True if reactor has all inputs for recipe
	 */
	private boolean hasAllInputs(RebornRecipe recipeType) {
		if (recipeType == null) {
			return false;
		}
		for (RebornIngredient ingredient : recipeType.getRebornIngredients()) {
			boolean hasItem = false;
			if (ingredient.test(inventory.getStack(topStackSlot))
					|| ingredient.test(inventory.getStack(bottomStackSlot))) {
				hasItem = true;
			}
			if (!hasItem) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Decrease stack size on the given slot according to recipe input
	 *
	 * @param slot {@code int} Slot number
	 */
	private void useInput(int slot) {
		if (currentRecipe == null) {
			return;
		}
		for (RebornIngredient ingredient : currentRecipe.getRebornIngredients()) {
			if (ingredient.test(inventory.getStack(slot))) {
				inventory.shrinkSlot(slot, ingredient.getCount());
				break;
			}
		}
	}

	private int percentage(double MaxValue, double CurrentValue) {
		if (CurrentValue == 0) {
			return 0;
		}
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}

	// GenericMachineBlockEntity
	@Override
	public int getProgressScaled(int scale) {
		FusionReactorRecipe reactorRecipe = getCurrentRecipeFromID();
		if (craftingTickTime != 0 && reactorRecipe != null && reactorRecipe.getTime() != 0) {
			return craftingTickTime * scale / reactorRecipe.getTime();
		}
		return 0;
	}

	@Override
	public long getBaseMaxPower() {
		return Math.min((long) (TechRebornConfig.fusionControlComputerMaxEnergy * getPowerMultiplier()), Long.MAX_VALUE);
	}

	@Override
	public long getBaseMaxOutput() {
		if (!hasStartedCrafting) {
			return 0;
		}
		return TechRebornConfig.fusionControlComputerMaxOutput;
	}

	@Override
	public long getBaseMaxInput() {
		if (hasStartedCrafting) {
			return 0;
		}
		return TechRebornConfig.fusionControlComputerMaxInput;
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);

		if (world == null || world.isClient) {
			return;
		}

		// Move this to here from the nbt read method, as it now requires the world as of 1.14
		if (checkNBTRecipe) {
			checkNBTRecipe = false;
			for (final RebornRecipe reactorRecipe : ModRecipes.FUSION_REACTOR.getRecipes(getWorld())) {
				if (validateRecipe((FusionReactorRecipe) reactorRecipe)) {
					this.currentRecipe = (FusionReactorRecipe) reactorRecipe;
				}
			}
		}

		if (lastTick == world.getTime()) {
			// Prevent tick accelerators, blame obstinate for this.
			return;
		}
		lastTick = world.getTime();

		// Force check every second
		if (world.getTime() % 20 == 0) {
			inventory.setHashChanged();
		}

		if (!isMultiblockValid()) {
			resetCrafter();
			return;
		}

		if (currentRecipe == null && inventory.hasChanged()) {
			updateCurrentRecipe();
		}

		if (currentRecipe != null) {
			if (!hasStartedCrafting && !validateRecipe(currentRecipe)) {
				resetCrafter();
				inventory.resetHasChanged();
				return;
			}

			if (!hasStartedCrafting) {
				// Ignition!
				if (getStored() > currentRecipe.getStartEnergy()) {
					useEnergy(currentRecipe.getStartEnergy());
					hasStartedCrafting = true;
					useInput(topStackSlot);
					useInput(bottomStackSlot);
				}
			}
			if (hasStartedCrafting && craftingTickTime < currentRecipe.getTime()) {
				// Power gen
				if (currentRecipe.getPower() > 0) {
					// Waste power if it has nowhere to go
					long power = (long) (Math.abs(currentRecipe.getPower()) * getPowerMultiplier());
					addEnergy(power);
					powerChange = (power);
					craftingTickTime++;
				} else { // Power user
					if (getStored() > currentRecipe.getPower()) {
						setEnergy(getEnergy() - currentRecipe.getPower());
						craftingTickTime++;
					}
				}
			} else if (craftingTickTime >= currentRecipe.getTime()) {
				ItemStack result = currentRecipe.getOutputs().get(0);
				if (canFitStack(result, outputStackSlot, true)) {
					if (inventory.getStack(outputStackSlot).isEmpty()) {
						inventory.setStack(outputStackSlot, result.copy());
					} else {
						inventory.shrinkSlot(outputStackSlot, -result.getCount());
					}
					if (validateRecipe(this.currentRecipe)) {
						craftingTickTime = 0;
						useInput(topStackSlot);
						useInput(bottomStackSlot);
					} else {
						resetCrafter();
					}
				}
			}
			markDirty();
		}

		inventory.resetHasChanged();
	}

	@Override
	protected boolean canAcceptEnergy(@Nullable Direction side) {
		// Accept from sides
		return !(side == Direction.DOWN || side == Direction.UP);
	}

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		// Provide from top and bottom
		return side == Direction.DOWN || side == Direction.UP;
	}

	@Override
	public void readNbt(final NbtCompound tagCompound) {
		super.readNbt(tagCompound);
		this.craftingTickTime = tagCompound.getInt("craftingTickTime");
		this.neededPower = tagCompound.getInt("neededPower");
		this.hasStartedCrafting = tagCompound.getBoolean("hasStartedCrafting");
		if (tagCompound.contains("hasActiveRecipe") && tagCompound.getBoolean("hasActiveRecipe") && this.currentRecipe == null) {
			checkNBTRecipe = true;
		}
		if (tagCompound.contains("size")) {
			this.size = tagCompound.getInt("size");
		}
		//Done here to force the smaller size, will be useful if people lag out on a large one.
		this.size = Math.min(size, TechRebornConfig.fusionControlComputerMaxCoilSize);
	}

	@Override
	public void writeNbt(final NbtCompound tagCompound) {
		super.writeNbt(tagCompound);
		tagCompound.putInt("craftingTickTime", this.craftingTickTime);
		tagCompound.putInt("neededPower", this.neededPower);
		tagCompound.putBoolean("hasStartedCrafting", this.hasStartedCrafting);
		tagCompound.putBoolean("hasActiveRecipe", this.currentRecipe != null);
		tagCompound.putInt("size", size);
	}

	// MachineBaseBlockEntity
	@Override
	public double getPowerMultiplier() {
		double calc = (1F / 2F) * Math.pow(size - 5, 1.8);
		return Math.max(Math.round(calc * 100D) / 100D, 1D);
	}

	@Override
	public void writeMultiblock(MultiblockWriter writer) {
		BlockState coil = TRContent.Machine.FUSION_COIL.block.getDefaultState();
		Torus.getOriginPositions(size).forEach(pos -> writer.add(pos.getX(), pos.getY(), pos.getZ(), coil));
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("fusionreactor").player(player.getInventory()).inventory().hotbar()
				.addInventory().blockEntity(this).slot(0, 34, 47).slot(1, 126, 47).outputSlot(2, 80, 47).syncEnergyValue()
				.sync(this::getCraftingTickTime, this::setCraftingTickTime)
				.sync(this::getSize, this::setSize)
				.sync(this::getState, this::setState)
				.sync(this::getNeededPower, this::setNeededPower)
				.sync(this::getCurrentRecipeID, this::setCurrentRecipeID)
				.addInventory()
				.create(this, syncID);
	}

	public int getCraftingTickTime() {
		return craftingTickTime;
	}

	public void setCraftingTickTime(int craftingTickTime) {
		this.craftingTickTime = craftingTickTime;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getState() {
		if (currentRecipe == null) {
			return 0; //No Recipe
		}
		if (!hasStartedCrafting) {
			return 1; //Waiting on power
		}
		return 2; //Crafting
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getNeededPower() {
		return neededPower;
	}

	public void setNeededPower(int neededPower) {
		this.neededPower = neededPower;
	}

	public Identifier getCurrentRecipeID() {
		if (currentRecipe == null) {
			return new Identifier("null", "null");
		}
		return currentRecipe.getId();
	}

	public void setCurrentRecipeID(Identifier currentRecipeID) {
		if (currentRecipeID.getPath().equals("null")) {
			currentRecipeID = null;
		}
		this.currentRecipeID = currentRecipeID;
	}
}
