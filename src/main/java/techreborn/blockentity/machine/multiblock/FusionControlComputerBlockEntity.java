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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Torus;
import techreborn.api.recipe.recipes.FusionReactorRecipe;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.List;

public class FusionControlComputerBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider, IContainerProvider {

	public RebornInventory<FusionControlComputerBlockEntity> inventory;

	public int coilCount = 0;
	public int crafingTickTime = 0;
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

	public FusionControlComputerBlockEntity() {
		super(TRBlockEntities.FUSION_CONTROL_COMPUTER);
		checkOverfill = false;
		this.inventory = new RebornInventory<>(3, "FusionControlComputerBlockEntity", 64, this);
	}

	/**
	 * Check that reactor has all necessary coils in place
	 * 
	 * @return boolean Return true if coils are present
	 */
	public boolean checkCoils() {
		List<BlockPos> coils = Torus.generate(pos, size);
		for(BlockPos coilPos : coils){
			if (!isCoil(coilPos)) {
				coilCount = 0;
				return false;
			}
		}
		coilCount = coils.size();
		return true;
	}

	/**
	 * Checks if block is fusion coil
	 * 
	 * @param pos coordinate for block
	 * @return boolean Returns true if block is fusion coil
	 */
	public boolean isCoil(BlockPos pos) {
		return world.getBlockState(pos).getBlock() == TRContent.Machine.FUSION_COIL.block;
	}

	/**
	 * Resets crafter progress and recipe
	 */
	private void resetCrafter() {
		currentRecipe = null;
		crafingTickTime = 0;
		neededPower = 0;
		hasStartedCrafting = false;
	}

	/**
	 * Checks that ItemStack could be inserted into slot provided, including check
	 * for existing item in slot and maximum stack size
	 * 
	 * @param stack ItemStack ItemStack to insert
	 * @param slot int Slot ID to check
	 * @param tags boolean Should we use tags
	 * @return boolean Returns true if ItemStack will fit into slot
	 */
	public boolean canFitStack(ItemStack stack, int slot, boolean tags) {// Checks to see if it can
																								// fit the stack
		if (stack.isEmpty()) {
			return true;
		}
		if (inventory.getInvStack(slot).isEmpty()) {
			return true;
		}
		if (ItemUtils.isItemEqual(inventory.getInvStack(slot), stack, true, tags)) {
			return stack.getCount() + inventory.getInvStack(slot).getCount() <= stack.getMaxCount();
		}
		return false;
	}

	/**
	 * Returns progress scaled to input value
	 * 
	 * @param scale int Maximum value for progress
	 * @return int Scale of progress
	 */
	public int getProgressScaled(int scale) {
		FusionReactorRecipe reactorRecipe = getCurrentRecipeFromID();
		if (crafingTickTime != 0 && reactorRecipe != null && reactorRecipe.getTime() != 0) {
			return crafingTickTime * scale / reactorRecipe.getTime();
		}
		return 0;
	}

	/**
	 * Tries to set current recipe based in inputs in reactor
	 */
	private void updateCurrentRecipe() {
		for (RebornRecipe recipe : ModRecipes.FUSION_REACTOR.getRecipes(getWorld())) {
			if (validateRecipe((FusionReactorRecipe) recipe)) {
				currentRecipe = (FusionReactorRecipe) recipe;
				crafingTickTime = 0;
				neededPower = currentRecipe.getStartEnergy();
				hasStartedCrafting = false;
				break;
			}
		}
	}
	
	/**
	 * Validates if reactor has all inputs and can output result
	 * 
	 * @param recipe FusionReactorRecipe Recipe to validate
	 * @return Boolean True if we have all inputs and can fit output
	 */
	private boolean validateRecipe(FusionReactorRecipe recipe) {
		return hasAllInputs(recipe) && canFitStack(recipe.getOutputs().get(0), outputStackSlot, true);	
	}
	
	/**
	 * Check if BlockEntity has all necessary inputs for recipe provided
	 * 
	 * @param recipeType RebornRecipe Recipe to check inputs
	 * @return Boolean True if reactor has all inputs for recipe
	 */
	private boolean hasAllInputs(RebornRecipe recipeType) {
		if (recipeType == null) {
			return false;
		}
		for (RebornIngredient ingredient : recipeType.getRebornIngredients()) {
			boolean hasItem = false;
			if (ingredient.test(inventory.getInvStack(topStackSlot))
					|| ingredient.test(inventory.getInvStack(bottomStackSlot))) {
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
	 * @param slot int Slot number
	 */
	private void useInput(int slot) {
		if (currentRecipe == null) {
			return;
		}
		for (RebornIngredient ingredient : currentRecipe.getRebornIngredients()) {
			if (ingredient.test(inventory.getInvStack(slot))) {
				inventory.shrinkSlot(slot, ingredient.getCount());
				break;
			}
		}
	}


	// TilePowerAcceptor
	@Override
	public void tick() {
		super.tick();

		if (world.isClient) {
			return;
		}

		//Move this to here from the nbt read method, as it now requires the world as of 1.14
		if(checkNBTRecipe) {
			checkNBTRecipe = false;
			for (final RebornRecipe reactorRecipe : ModRecipes.FUSION_REACTOR.getRecipes(getWorld())) {
				if (validateRecipe((FusionReactorRecipe) reactorRecipe)) {
					this.currentRecipe = (FusionReactorRecipe) reactorRecipe;
				}
			}
		}

		if(lastTick == world.getTime()){
			//Prevent tick accerators, blame obstinate for this.
			return;
		}
		lastTick = world.getTime();

		// Force check every second
		if (world.getTime() % 20 == 0) {
			checkCoils();
			inventory.setChanged();
		}

		if (coilCount == 0) {
			resetCrafter();
			return;
		}

		if (currentRecipe == null && inventory.hasChanged()) {
			updateCurrentRecipe();
		}

		if (currentRecipe != null) {
			if (!hasStartedCrafting && !validateRecipe(currentRecipe)) {
				resetCrafter();
				inventory.resetChanged();
				return;
			}

			if (!hasStartedCrafting) {
				// Ignition!
				if (canUseEnergy(currentRecipe.getStartEnergy())) {
					useEnergy(currentRecipe.getStartEnergy());
					hasStartedCrafting = true;
					useInput(topStackSlot);
					useInput(bottomStackSlot);
				}
			}
			if (hasStartedCrafting && crafingTickTime < currentRecipe.getTime()) {
				// Power gen
				if (currentRecipe.getPower() > 0) {
					// Waste power if it has no where to go
					double power = Math.abs(currentRecipe.getPower()) * getPowerMultiplier();
					addEnergy(power);
					powerChange = (power);
					crafingTickTime++;
				} else { // Power user
					if (canUseEnergy(currentRecipe.getPower())) {
						setEnergy(getEnergy() - currentRecipe.getPower());
						crafingTickTime++;
					}
				}
			} else if (crafingTickTime >= currentRecipe.getTime()) {
				ItemStack result = currentRecipe.getOutputs().get(0);
				if (canFitStack(result, outputStackSlot, true)) {
					if (inventory.getInvStack(outputStackSlot).isEmpty()) {
						inventory.setInvStack(outputStackSlot, result.copy());
					} else {
						inventory.shrinkSlot(outputStackSlot, -result.getCount());
					}
					if (validateRecipe(this.currentRecipe)) {
						crafingTickTime = 0;
						useInput(topStackSlot);
						useInput(bottomStackSlot);
					} else {
						resetCrafter();
					}
				}
			}
			markDirty();
		}

		inventory.resetChanged();
	}


	@Override
	public double getPowerMultiplier() {
		double calc = (1F/2F) * Math.pow(size -5, 1.8);
		return Math.max(Math.round(calc * 100D) / 100D, 1D);
	}

	@Override
	public double getBaseMaxPower() {
		return Math.min(TechRebornConfig.fusionControlComputerMaxEnergy * getPowerMultiplier(), Integer.MAX_VALUE);
	}

	@Override
	public boolean canAcceptEnergy(Direction direction) {
		return !(direction == Direction.DOWN || direction == Direction.UP);
	}

	@Override
	public boolean canProvideEnergy(Direction direction) {
		return direction == Direction.DOWN || direction == Direction.UP;
	}

	@Override
	public double getBaseMaxOutput() {
		if (!hasStartedCrafting) {
			return 0;
		}
		return TechRebornConfig.fusionControlComputerMaxOutput;
	}

	@Override
	public double getBaseMaxInput() {
		if (hasStartedCrafting) {
			return 0;
		}
		return TechRebornConfig.fusionControlComputerMaxInput;
	}

	@Override
	public void fromTag(final CompoundTag tagCompound) {
		super.fromTag(tagCompound);
		this.crafingTickTime = tagCompound.getInt("crafingTickTime");
		this.neededPower = tagCompound.getInt("neededPower");
		this.hasStartedCrafting = tagCompound.getBoolean("hasStartedCrafting");
		if(tagCompound.contains("hasActiveRecipe") && tagCompound.getBoolean("hasActiveRecipe") && this.currentRecipe == null){
			checkNBTRecipe = true;
		}
		if(tagCompound.contains("size")){
			this.size = tagCompound.getInt("size");
		}
		this.size = Math.min(size, TechRebornConfig.fusionControlComputerMaxCoilSize);//Done here to force the samller size, will be useful if people lag out on a large one.
	}

	@Override
	public CompoundTag toTag(final CompoundTag tagCompound) {
		super.toTag(tagCompound);
		tagCompound.putInt("crafingTickTime", this.crafingTickTime);
		tagCompound.putInt("neededPower", this.neededPower);
		tagCompound.putBoolean("hasStartedCrafting", this.hasStartedCrafting);
		tagCompound.putBoolean("hasActiveRecipe", this.currentRecipe != null);
		tagCompound.putInt("size", size);
		return tagCompound;
	}

	// TileLegacyMachineBase
	@Override
	public void onLoad() {
		super.onLoad();
		this.checkCoils();
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity playerIn) {
		return TRContent.Machine.FUSION_CONTROL_COMPUTER.getStack();
	}

	// ItemHandlerProvider
	@Override
	public RebornInventory<FusionControlComputerBlockEntity> getInventory() {
		return inventory;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("fusionreactor").player(player.inventory).inventory().hotbar()
				.addInventory().blockEntity(this).slot(0, 34, 47).slot(1, 126, 47).outputSlot(2, 80, 47).syncEnergyValue()
				.sync(this::getCoilStatus, this::setCoilStatus)
				.sync(this::getCrafingTickTime, this::setCrafingTickTime)
				.sync(this::getSize, this::setSize)
				.sync(this::getState, this::setState)
				.sync(this::getNeededPower, this::setNeededPower)
				.sync(this::getCurrentRecipeID, this::setCurrentRecipeID)
				.addInventory()
				.create(this, syncID);
	}

	public int getCoilStatus() {
		return coilCount;
	}

	public void setCoilStatus(int coilStatus) {
		this.coilCount = coilStatus;
	}

	public int getCrafingTickTime() {
		return crafingTickTime;
	}

	public void setCrafingTickTime(int crafingTickTime) {
		this.crafingTickTime = crafingTickTime;
	}

	public int getNeededPower() {
		return neededPower;
	}

	public void setNeededPower(int neededPower) {
		this.neededPower = neededPower;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void changeSize(int sizeDelta){
		int newSize = size + sizeDelta;
		this.size = Math.max(6, Math.min(TechRebornConfig.fusionControlComputerMaxCoilSize, newSize));
	}

	public int getState(){
		if(currentRecipe == null ){
			return 0; //No Recipe
		}
		if(!hasStartedCrafting){
			return 1; //Waiting on power
		}
		return 2; //Crafting
	}

	public void setState(int state){
		this.state = state;
	}

	public Identifier getCurrentRecipeID() {
		if(currentRecipe == null) {
			return new Identifier("null", "null");
		}
		return currentRecipe.getId();
	}

	public void setCurrentRecipeID(Identifier currentRecipeID) {
		if(currentRecipeID.getPath().equals("null")) {
			currentRecipeID = null;
		}
		this.currentRecipeID = currentRecipeID;
	}

	public FusionReactorRecipe getCurrentRecipeFromID() {
		if(currentRecipeID == null) return null;
		return ModRecipes.FUSION_REACTOR.getRecipes(world).stream()
				.filter(recipe -> recipe.getId().equals(currentRecipeID))
				.findFirst()
				.orElse(null);
	}

	public String getStateString(){
		if(state == -1){
			return "";
		} else if (state == 0){
			return "No recipe";
		} else if (state == 1){
			FusionReactorRecipe r = getCurrentRecipeFromID();
			if(r == null) {
				return "Charging";
			}
			int percentage = percentage(r.getStartEnergy(), getEnergy());
			return "Charging (" + percentage + "%)";
		} else if (state == 2){
			return "Crafting";
		}
		return "";
	}

	private int percentage(double MaxValue, double CurrentValue) {
		if (CurrentValue == 0) {
			return 0;
		}
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}
}
