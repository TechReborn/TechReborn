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

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.api.IToolDrop;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by modmuss50 on 20/06/2017.
 */
@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileAutoCraftingTable extends TilePowerAcceptor
		implements IToolDrop, IInventoryProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "autocrafter", key = "AutoCrafterInput", comment = "AutoCrafting Table Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "autocrafter", key = "AutoCrafterMaxEnergy", comment = "AutoCrafting Table Max Energy (Value in EU)")
	public static int maxEnergy = 10_000;

	public int[] craftingSlots = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	private int outputSlot = 9;
	public Inventory inventory = new Inventory(11, "TileAutoCraftingTable", 64, this);
	public IRecipe currentRecipe;
	public ItemStack currentRecipeOutput;
	public int tickTime;
	public int maxProgress = 120;
	public int euTick = 10;
	public int balanceSlot = 0;

	InventoryCrafting inventoryCrafting = null;

	public boolean locked = true;

	public TileAutoCraftingTable() {
		super();
	}

	public InventoryCrafting getCraftingInventory() {
		if (inventoryCrafting == null) {
			inventoryCrafting = new InventoryCrafting(new Container() {
				@Override
				public boolean canInteractWith(EntityPlayer playerIn) {
					return false;
				}
			}, 3, 3);
		}
		for (int i = 0; i < 9; i++) {
			inventoryCrafting.setInventorySlotContents(i, inventory.getStackInSlot(i));
		}
		return inventoryCrafting;
	}

	public boolean canMake(InventoryCrafting craftMatrix) {
		ItemStack stack = findMatchingRecipeOutput(craftMatrix);
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

	boolean hasRoomForExtraItem(ItemStack stack) {
		ItemStack extraOutputSlot = getStackInSlot(10);
		if (extraOutputSlot.isEmpty()) {
			return true;
		}
		return hasOutputSpace(stack, 10);
	}

	public boolean hasOutputSpace(ItemStack output, int slot) {
		ItemStack stack = inventory.getStackInSlot(slot);
		if (stack.isEmpty()) {
			return true;
		}
		if (ItemUtils.isItemEqual(stack, output, true, true)) {
			if (stack.getMaxStackSize() > stack.getCount() + output.getCount()) {
				return true;
			}
		}
		return false;
	}


	@SuppressWarnings("unused")
	private void handleContainerItem(ItemStack stack) {
		if (stack.getItem().hasContainerItem(stack)) {
			ItemStack containerItem = stack.getItem().getContainerItem(stack);
			ItemStack extraOutputSlot = getStackInSlot(10);
			if (hasOutputSpace(containerItem, 10)) {
				if (extraOutputSlot.isEmpty()) {
					setInventorySlotContents(10, containerItem.copy());
				} else if (ItemUtils.isItemEqual(extraOutputSlot, containerItem, true, true)
						&& extraOutputSlot.getMaxStackSize() < extraOutputSlot.getCount() + containerItem.getCount()) {
					extraOutputSlot.grow(1);
				}
			}
		}
	}

	public boolean hasIngredient(Ingredient ingredient) {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (ingredient.apply(stack)) {
				return true;
			}
		}
		return false;
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

		return Optional.of(getCraftingInventory());
	}

	public int getProgress() {
		return tickTime;
	}

	public void setProgress(int progress) {
		this.tickTime = progress;
	}

	public int getMaxProgress() {
		if (maxProgress == 0) {
			maxProgress = 1;
		}
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	public IRecipe findMatchingRecipe(InventoryCrafting crafting){
		if(crafting.isEmpty()){
			return null;
		}
		if(currentRecipe != null){
			if(currentRecipe.matches(crafting, world)){
				return currentRecipe;
			}
		}
		return CraftingManager.findMatchingRecipe(crafting, world);
	}

	public ItemStack findMatchingRecipeOutput(InventoryCrafting crafting){
		return findMatchingRecipe(crafting).getRecipeOutput().copy();
	}

	// TilePowerAcceptor
	@Override
	public void update() {
		super.update();
		if (world.isRemote) {
			return;
		}
		charge(10);

		InventoryCrafting craftMatrix = getCraftingInventory();
		//Only search the recipe tree when the inv changes
		if(inventory.hasChanged){
			currentRecipe = findMatchingRecipe(craftMatrix);
		}

		//Checks that the current recipe is still valid, if not it will check for a valid recipe next tick.
		if(currentRecipe != null){
			if(!currentRecipe.matches(craftMatrix, world)){
				currentRecipe = null;
				inventory.hasChanged = true;
			}
		}

		if (currentRecipe != null) {
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
			if (tickTime >= Math.max((int) (maxProgress * (1.0 - getSpeedMultiplier())), 1)) {
				currentRecipeOutput = findMatchingRecipeOutput(craftMatrix);
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
			if (canUseEnergy(getEuPerTick(euTick))
				&& tickTime < Math.max((int) (maxProgress * (1.0 - getSpeedMultiplier())), 1)
				&& canMake(craftMatrix)) {
				useEnergy(getEuPerTick(euTick));
				tickTime++;
			}
		}
		if (currentRecipeOutput.isEmpty()) {
			tickTime = 0;
			currentRecipe = null;
		}
	}

	// Easyest way to sync back to the client
	public int getLockedInt() {
		return locked ? 1 : 0;
	}

	public void setLockedInt(int lockedInt) {
		locked = lockedInt == 1;
		inventory.hasChanged = true;
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
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
	public boolean canAcceptEnergy(EnumFacing enumFacing) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing enumFacing) {
		return false;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setBoolean("locked", locked);
		return super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey("locked")) {
			locked = tag.getBoolean("locked");
		}
		inventory.hasChanged = true;
		super.readFromNBT(tag);
	}

	// RebornMachineTile
	@Override
	public boolean canBeUpgraded() {
		return true;
	}


	// This machine doesnt have a facing
	@Override
	public EnumFacing getFacingEnum() {
		return EnumFacing.NORTH;
	}
	
	@Override
    public void onPlaced(EntityLivingBase placer, ItemStack stack) {
       return;
    }

	// IToolDrop
	@Override
	public ItemStack getToolDrop(EntityPlayer playerIn) {
		return new ItemStack(ModBlocks.AUTO_CRAFTING_TABLE, 1);
	}

	// IInventoryProvider
	@Override
	public IInventory getInventory() {
		return inventory;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(EntityPlayer player) {
		return new ContainerBuilder("autocraftingtable").player(player.inventory).inventory().hotbar().addInventory()
				.tile(this).slot(0, 28, 25).slot(1, 46, 25).slot(2, 64, 25).slot(3, 28, 43).slot(4, 46, 43)
				.slot(5, 64, 43).slot(6, 28, 61).slot(7, 46, 61).slot(8, 64, 61).outputSlot(9, 145, 42)
				.onCraft(inv -> this.inventory.setInventorySlotContents(1, findMatchingRecipeOutput(getCraftingInventory())))
				.outputSlot(10, 145, 70).syncEnergyValue().syncIntegerValue(this::getProgress, this::setProgress)
				.syncIntegerValue(this::getMaxProgress, this::setMaxProgress)
				.syncIntegerValue(this::getLockedInt, this::setLockedInt).addInventory().create(this);
	}

	@Override
	public boolean hasSlotConfig() {
		return true;
	}
}
