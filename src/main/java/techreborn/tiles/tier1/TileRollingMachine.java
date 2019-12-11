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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.api.IToolDrop;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.RebornMachineBlock;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.RollingMachineRecipe;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

//TODO add tick and power bars.

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileRollingMachine extends TilePowerAcceptor
	implements IToolDrop, IInventoryProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "rolling_machine", key = "RollingMachineMaxInput", comment = "Rolling Machine Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "rolling_machine", key = "RollingMachineEnergyPerTick", comment = "Rolling Machine Energy Per Tick (Value in EU)")
	public static int energyPerTick = 5;
	@ConfigRegistry(config = "machines", category = "rolling_machine", key = "RollingMachineEnergyRunTime", comment = "Rolling Machine Run Time")
	public static int runTime = 250;
	@ConfigRegistry(config = "machines", category = "rolling_machine", key = "RollingMachineMaxEnergy", comment = "Rolling Machine Max Energy (Value in EU)")
	public static int maxEnergy = 10000;

	public int[] craftingSlots = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	private InventoryCrafting craftCache;
	public Inventory inventory = new Inventory(12, "TileRollingMachine", 64, this);
	public boolean isRunning;
	public int tickTime;
	@Nonnull
	public ItemStack currentRecipeOutput;
	public IRecipe currentRecipe;
	private int outputSlot;
	public boolean locked = false;
	public int balanceSlot = 0;

	public TileRollingMachine() {
		super();
		outputSlot = 9;
	}

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

	@Override
	public void update() {
		super.update();
		if (world.isRemote) {
			return;
		}
		charge(10);

		InventoryCrafting craftMatrix = getCraftingMatrix();
		currentRecipe = RollingMachineRecipe.instance.findMatchingRecipe(craftMatrix, world);
		if (currentRecipe != null) {
			setIsActive(true);
			if (world.getTotalWorldTime() % 2 == 0) {
				Optional<InventoryCrafting> balanceResult = balanceRecipe(craftMatrix);
				if (balanceResult.isPresent()) {
					craftMatrix = balanceResult.get();
				}
			}
			currentRecipeOutput = currentRecipe.getCraftingResult(craftMatrix);
		} else {
			currentRecipeOutput = ItemStack.EMPTY;
		}

		if (!currentRecipeOutput.isEmpty() && canMake(craftMatrix)) {
			if (tickTime >= Math.max((int) (runTime * (1.0 - getSpeedMultiplier())), 1)) {
				currentRecipeOutput = RollingMachineRecipe.instance.findMatchingRecipeOutput(craftMatrix, world);
				if (!currentRecipeOutput.isEmpty()) {
					boolean hasCrafted = false;
					if (inventory.getStackInSlot(outputSlot).isEmpty()) {
						inventory.setInventorySlotContents(outputSlot, currentRecipeOutput);
						tickTime = 0;
						hasCrafted = true;
					} else {
						if (inventory.getStackInSlot(outputSlot).getCount()
								+ currentRecipeOutput.getCount() <= currentRecipeOutput.getMaxStackSize()) {
							final ItemStack stack = inventory.getStackInSlot(outputSlot);
							stack.setCount(stack.getCount() + currentRecipeOutput.getCount());
							inventory.setInventorySlotContents(outputSlot, stack);
							tickTime = 0;
							hasCrafted = true;
						} else {
							setIsActive(false);
						}
					}
					if (hasCrafted) {
						for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
							inventory.decrStackSize(i, 1);
						}
						currentRecipeOutput = ItemStack.EMPTY;
						currentRecipe = null;
					}
				}
			}
		} else {
			tickTime = 0;
		}
		if (!currentRecipeOutput.isEmpty()) {
			if (canUseEnergy(getEuPerTick(energyPerTick))
					&& tickTime < Math.max((int) (runTime * (1.0 - getSpeedMultiplier())), 1)
					&& canMake(craftMatrix)) {
				useEnergy(getEuPerTick(energyPerTick));
				tickTime++;
			} else {
				setIsActive(false);
			}
		}
		if (currentRecipeOutput.isEmpty()) {
			tickTime = 0;
			currentRecipe = null;
			setIsActive(canMake(getCraftingMatrix()));
		}
	}

	public void setIsActive(boolean active) {
		if (active == isRunning){
			return;
		}
		isRunning = active;
		if (this.getWorld().getBlockState(this.getPos()).getBlock() instanceof RebornMachineBlock) {
			setActive(active);
		}
		this.getWorld().notifyBlockUpdate(this.getPos(), this.getWorld().getBlockState(this.getPos()), this.getWorld().getBlockState(this.getPos()), 3);
	}

	public Optional<InventoryCrafting> balanceRecipe(InventoryCrafting craftCache) {
		if (currentRecipe == null) {
			return Optional.empty();
		}
		if (world.isRemote) {
			return Optional.empty();
		}
		if (!locked) {
			return Optional.empty();
		}
		if (craftCache.isEmpty()) {
			return Optional.empty();
		}
		balanceSlot++;
		if (balanceSlot > craftCache.getSizeInventory()) {
			balanceSlot = 0;
		}
		//Find the best slot for each item in a recipe, and move it if needed
		ItemStack sourceStack = inventory.getStackInSlot(balanceSlot);
		if (sourceStack.isEmpty()) {
			return Optional.empty();
		}
		List<Integer> possibleSlots = new ArrayList<>();
		for (int s = 0; s < currentRecipe.getIngredients().size(); s++) {
			ItemStack stackInSlot = inventory.getStackInSlot(s);
			Ingredient ingredient = currentRecipe.getIngredients().get(s);
			if (ingredient != Ingredient.EMPTY && ingredient.apply(sourceStack)) {
				if (stackInSlot.isEmpty()) {
					possibleSlots.add(s);
				} else if (stackInSlot.getItem() == sourceStack.getItem() && stackInSlot.getItemDamage() == sourceStack.getItemDamage()) {
					possibleSlots.add(s);
				}
			}
		}

		if(!possibleSlots.isEmpty()){
			int totalItems =  possibleSlots.stream()
				.mapToInt(value -> inventory.getStackInSlot(value).getCount()).sum();
			int slots = possibleSlots.size();

			//This makes an array of ints with the best possible slot distribution
			int[] split = new int[slots];
			int remainder = totalItems % slots;
			Arrays.fill(split, totalItems / slots);
			while (remainder > 0){
				for (int i = 0; i < split.length; i++) {
					if(remainder > 0){
						split[i] +=1;
						remainder --;
					}
				}
			}

			List<Integer> slotDistrubution = possibleSlots.stream()
				.mapToInt(value -> inventory.getStackInSlot(value).getCount())
				.boxed().collect(Collectors.toList());

			boolean needsBalance = false;
			for (int i = 0; i < split.length; i++) {
				int required = split[i];
				if(slotDistrubution.contains(required)){
					//We need to remove the int, not at the int, this seems to work around that
					slotDistrubution.remove(new Integer(required));
				} else {
					needsBalance = true;
				}
			}
			if (!needsBalance) {
				return Optional.empty();
			}
		} else {
			return Optional.empty();
		}

		//Slot, count
		Pair<Integer, Integer> bestSlot = null;
		for (Integer slot : possibleSlots) {
			ItemStack slotStack = inventory.getStackInSlot(slot);
			if (slotStack.isEmpty()) {
				bestSlot = Pair.of(slot, 0);
			}
			if (bestSlot == null) {
				bestSlot = Pair.of(slot, slotStack.getCount());
			} else if (bestSlot.getRight() >= slotStack.getCount()) {
				bestSlot = Pair.of(slot, slotStack.getCount());
			}
		}
		if (bestSlot == null
			|| bestSlot.getLeft() == balanceSlot
			|| bestSlot.getRight() == sourceStack.getCount()
			|| inventory.getStackInSlot(bestSlot.getLeft()).isEmpty()
			|| !ItemUtils.isItemEqual(sourceStack, inventory.getStackInSlot(bestSlot.getLeft()), true, true, true)) {
			return Optional.empty();
		}
		sourceStack.shrink(1);
		inventory.getStackInSlot(bestSlot.getLeft()).grow(1);
		inventory.hasChanged = true;

		return Optional.of(getCraftingMatrix());
	}

	private InventoryCrafting getCraftingMatrix() {
		if (craftCache == null) {
			craftCache = new InventoryCrafting(new RollingTileContainer(), 3, 3);
		}
		if (inventory.hasChanged) {
			for (int i = 0; i < 9; i++) {
				craftCache.setInventorySlotContents(i, inventory.getStackInSlot(i).copy());
			}
			inventory.hasChanged = false;
		}
		return craftCache;
	}

	public boolean canMake(InventoryCrafting craftMatrix) {
		ItemStack stack = RollingMachineRecipe.instance.findMatchingRecipeOutput(craftMatrix, this.world);
		if (locked) {
			for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
				ItemStack stack1 = craftMatrix.getStackInSlot(i);
				if (!stack1.isEmpty() && stack1.getCount() < 2) {
					return false;
				}
			}
		}
		if (stack.isEmpty()) {
			return false;
		}
		ItemStack output = getStackInSlot(outputSlot);
		if (output.isEmpty()) {
			return true;
		}
		return ItemUtils.isItemEqual(stack, output, true, true);
	}

	@Override
	public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.ROLLING_MACHINE, 1);
	}

	@Override
	public void readFromNBT(final NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		this.isRunning = tagCompound.getBoolean("isRunning");
		this.tickTime = tagCompound.getInteger("tickTime");
		this.locked = tagCompound.getBoolean("locked");
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setBoolean("isRunning", this.isRunning);
		tagCompound.setInteger("tickTime", this.tickTime);
		tagCompound.setBoolean("locked", locked);
		return tagCompound;
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	public int getBurnTime() {
		return tickTime;
	}

	public void setBurnTime(final int burnTime) {
		this.tickTime = burnTime;
	}

	public int getBurnTimeRemainingScaled(final int scale) {
		if (tickTime == 0 || Math.max((int) (runTime* (1.0 - getSpeedMultiplier())), 1) == 0) {
			return 0;
		}
		return tickTime * scale / Math.max((int) (runTime* (1.0 - getSpeedMultiplier())), 1);
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("rollingmachine").player(player.inventory)
			.inventory().hotbar()
			.addInventory().tile(this)
			.slot(0, 30, 22).slot(1, 48, 22).slot(2, 66, 22)
			.slot(3, 30, 40).slot(4, 48, 40).slot(5, 66, 40)
			.slot(6, 30, 58).slot(7, 48, 58).slot(8, 66, 58)
			.onCraft(inv -> this.inventory.setInventorySlotContents(1, RollingMachineRecipe.instance.findMatchingRecipeOutput(getCraftingMatrix(), this.world)))
			.outputSlot(9, 124, 40)
			.energySlot(10, 8, 70)
			.syncEnergyValue().syncIntegerValue(this::getBurnTime, this::setBurnTime).syncIntegerValue(this::getLockedInt, this::setLockedInt).addInventory().create(this);
	}

	//Easyest way to sync back to the client
	public int getLockedInt() {
		return locked ? 1 : 0;
	}

	public void setLockedInt(int lockedInt) {
		locked = lockedInt == 1;
	}

	public int getProgressScaled(final int scale) {
		if (tickTime != 0 && Math.max((int) (runTime* (1.0 - getSpeedMultiplier())), 1) != 0) {
			return tickTime * scale / Math.max((int) (runTime* (1.0 - getSpeedMultiplier())), 1);
		}
		return 0;
	}

	private static class RollingTileContainer extends Container {

		@Override
		public boolean canInteractWith(final EntityPlayer entityplayer) {
			return true;
		}

	}

	@Override
	public boolean canBeUpgraded() {
		return true;
	}
}
