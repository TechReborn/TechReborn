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

package techreborn.tiles.machine.tier1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.api.IToolDrop;
import reborncore.api.tile.ItemHandlerProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.IInventoryAccess;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.TechReborn;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.init.ModSounds;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 20/06/2017.
 */
@RebornRegister(modID = TechReborn.MOD_ID)
public class TileAutoCraftingTable extends TilePowerAcceptor
		implements IToolDrop, ItemHandlerProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "autocrafter", key = "AutoCrafterInput", comment = "AutoCrafting Table Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "autocrafter", key = "AutoCrafterMaxEnergy", comment = "AutoCrafting Table Max Energy (Value in EU)")
	public static int maxEnergy = 10_000;

	public Inventory<TileAutoCraftingTable> inventory = new Inventory<>(11, "TileAutoCraftingTable", 64, this, getInventoryAccess());
	public int progress;
	public int maxProgress = 120;
	public int euTick = 10;

	InventoryCrafting inventoryCrafting = null;
	IRecipe lastCustomRecipe = null;
	IRecipe lastRecipe = null;

	public boolean locked = true;

	public TileAutoCraftingTable() {
		super();
	}

	@Nullable
	public IRecipe getIRecipe() {
		InventoryCrafting crafting = getCraftingInventory();
		if (!crafting.isEmpty()) {
			if (lastRecipe != null) {
				if (lastRecipe.matches(crafting, world)) {
					return lastRecipe;
				}
			}
			for (IRecipe testRecipe : CraftingManager.REGISTRY) {
				if (testRecipe.matches(crafting, world)) {
					lastRecipe = testRecipe;
					return testRecipe;
				}
			}
		}
		return null;
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

	public boolean canMake(IRecipe recipe) {
		if (recipe != null && recipe.canFit(3, 3)) {
			boolean missingOutput = false;
			int[] stacksInSlots = new int[9];
			for (int i = 0; i < 9; i++) {
				stacksInSlots[i] = inventory.getStackInSlot(i).getCount();
			}
			for (Ingredient ingredient : recipe.getIngredients()) {
				if (ingredient != Ingredient.EMPTY) {
					boolean foundIngredient = false;
					for (int i = 0; i < 9; i++) {
						ItemStack stack = inventory.getStackInSlot(i);
						int requiredSize = locked ? 1 : 0;
						if (stack.getMaxStackSize() == 1) {
							requiredSize = 0;
						}
						if (stacksInSlots[i] > requiredSize) {
							if (ingredient.apply(stack)) {
								if (stack.getItem().getContainerItem() != null) {
									if (!hasRoomForExtraItem(stack.getItem().getContainerItem(stack))) {
										continue;
									}
								}
								foundIngredient = true;
								stacksInSlots[i]--;
								break;
							}
						}
					}
					if (!foundIngredient) {
						missingOutput = true;
					}
				}
			}
			if (!missingOutput) {
				if (hasOutputSpace(recipe.getRecipeOutput(), 9)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	boolean hasRoomForExtraItem(ItemStack stack) {
		ItemStack extraOutputSlot = inventory.getStackInSlot(10);
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

	public boolean make(IRecipe recipe) {
		if (recipe == null || !canMake(recipe)) {
			return false;
		}
		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			Ingredient ingredient = recipe.getIngredients().get(i);
			// Looks for the best slot to take it from
			ItemStack bestSlot = inventory.getStackInSlot(i);
			if (ingredient.apply(bestSlot)) {
				handleContainerItem(bestSlot);
				bestSlot.shrink(1);
			} else {
				for (int j = 0; j < 9; j++) {
					ItemStack stack = inventory.getStackInSlot(j);
					if (ingredient.apply(stack)) {
						handleContainerItem(stack);
						stack.shrink(1); // TODO is this right? or do I need
											// to use it as an actull
											// crafting grid
						break;
					}
				}
			}
		}
		ItemStack output = inventory.getStackInSlot(9);
		// TODO fire forge recipe event
		ItemStack ouputStack = recipe.getCraftingResult(getCraftingInventory());
		if (output.isEmpty()) {
			inventory.setStackInSlot(9, ouputStack.copy());
		} else {
			// TODO use ouputStack in someway?
			output.grow(recipe.getRecipeOutput().getCount());
		}
		return true;
	}

	private void handleContainerItem(ItemStack stack) {
		if (stack.getItem().hasContainerItem(stack)) {
			ItemStack containerItem = stack.getItem().getContainerItem(stack);
			ItemStack extraOutputSlot = inventory.getStackInSlot(10);
			if (hasOutputSpace(containerItem, 10)) {
				if (extraOutputSlot.isEmpty()) {
					inventory.setStackInSlot(10, containerItem.copy());
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

	public boolean isItemValidForRecipeSlot(IRecipe recipe, ItemStack stack, int slotID) {
		if (recipe == null) {
			return true;
		}
		int bestSlot = findBestSlotForStack(recipe, stack);
		if (bestSlot != -1) {
			return bestSlot == slotID;
		}
		return true;
	}

	public int findBestSlotForStack(IRecipe recipe, ItemStack stack) {
		if (recipe == null) {
			return -1;
		}
		List<Integer> possibleSlots = new ArrayList<>();
		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			ItemStack stackInSlot = inventory.getStackInSlot(i);
			Ingredient ingredient = recipe.getIngredients().get(i);
			if (ingredient != Ingredient.EMPTY && ingredient.apply(stack)) {
				if (stackInSlot.isEmpty()) {
					possibleSlots.add(i);
				} else if (stackInSlot.getItem() == stack.getItem()
						&& stackInSlot.getItemDamage() == stack.getItemDamage()) {
					if (stackInSlot.getMaxStackSize() >= stackInSlot.getCount() + stack.getCount()) {
						possibleSlots.add(i);
					}
				}
			}
		}
		// Slot, count
		Pair<Integer, Integer> smallestCount = null;
		for (Integer slot : possibleSlots) {
			ItemStack slotStack = inventory.getStackInSlot(slot);
			if (slotStack.isEmpty()) {
				return slot;
			}
			if (smallestCount == null) {
				smallestCount = Pair.of(slot, slotStack.getCount());
			} else if (smallestCount.getRight() >= slotStack.getCount()) {
				smallestCount = Pair.of(slot, slotStack.getCount());
			}
		}
		if (smallestCount != null) {
			return smallestCount.getLeft();
		}
		return -1;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
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

	// TilePowerAcceptor
	@Override
	public void update() {
		super.update();
		if (world.isRemote) {
			return;
		}
		IRecipe recipe = getIRecipe();
		if (recipe != null) {
			if (progress >= maxProgress) {
				if (make(recipe)) {
					progress = 0;
				}
			} else {
				if (canMake(recipe)) {
					if (canUseEnergy(euTick)) {
						progress++;
						if (progress == 1) {
							world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.AUTO_CRAFTING,
									SoundCategory.BLOCKS, 0.3F, 0.8F);
						}
						useEnergy(euTick);
					}
				} else {
					progress = 0;
				}
			}
		}
		if (recipe == null) {
			progress = 0;
		}
	}

	// Easyest way to sync back to the client
	public int getLockedInt() {
		return locked ? 1 : 0;
	}

	public void setLockedInt(int lockedInt) {
		locked = lockedInt == 1;
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
		super.readFromNBT(tag);
	}

	// TileMachineBase
	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	private static IInventoryAccess<TileAutoCraftingTable> getInventoryAccess(){
		return (slotID, stack, facing, direction, tile) -> {
			switch (direction){
				case INSERT:
					if (slotID > 8) {
						return false;
					}
					int bestSlot = tile.findBestSlotForStack(tile.getIRecipe(), stack);
					if (bestSlot != -1) {
						return slotID == bestSlot;
					}
					return true;
				case EXTRACT:
					return slotID > 8;
			}
			return true;
		};
	}

	// This machine doesnt have a facing
	@Override
	public EnumFacing getFacingEnum() {
		return EnumFacing.NORTH;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(EntityPlayer playerIn) {
		return new ItemStack(ModBlocks.AUTO_CRAFTING_TABLE, 1);
	}

	// ItemHandlerProvider
	@Override
	public IItemHandler getInventory() {
		return inventory;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(EntityPlayer player) {
		return new ContainerBuilder("autocraftingtable").player(player.inventory).inventory().hotbar().addInventory()
				.tile(this).slot(0, 28, 25).slot(1, 46, 25).slot(2, 64, 25).slot(3, 28, 43).slot(4, 46, 43)
				.slot(5, 64, 43).slot(6, 28, 61).slot(7, 46, 61).slot(8, 64, 61).outputSlot(9, 145, 42)
				.outputSlot(10, 145, 70).syncEnergyValue().syncIntegerValue(this::getProgress, this::setProgress)
				.syncIntegerValue(this::getMaxProgress, this::setMaxProgress)
				.syncIntegerValue(this::getLockedInt, this::setLockedInt).addInventory().create(this);
	}

	@Override
	public boolean hasSlotConfig() {
		return false;
	}
}
