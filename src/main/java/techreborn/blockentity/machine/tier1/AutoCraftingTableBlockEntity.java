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

package techreborn.blockentity.machine.tier1;

import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.IInventoryAccess;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class AutoCraftingTableBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider, IContainerProvider {

	public RebornInventory<AutoCraftingTableBlockEntity> inventory = new RebornInventory<>(11, "AutoCraftingTableBlockEntity", 64, this, getInventoryAccess());
	public int progress;
	public int maxProgress = 120;
	public int euTick = 10;

	CraftingInventory inventoryCrafting = null;
	CraftingRecipe lastCustomRecipe = null;
	CraftingRecipe lastRecipe = null;

	public boolean locked = true;

	public AutoCraftingTableBlockEntity() {
		super(TRBlockEntities.AUTO_CRAFTING_TABLE);
	}

	@Nullable
	public CraftingRecipe getIRecipe() {
		CraftingInventory crafting = getCraftingInventory();
		if (!crafting.isInvEmpty()) {
			if (lastRecipe != null) {
				if (lastRecipe.matches(crafting, world)) {
					return lastRecipe;
				}
			}
			Optional<CraftingRecipe> testRecipe = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, crafting, world);
			if (testRecipe.isPresent()) {
				lastRecipe = testRecipe.get();
				return lastRecipe;
			}
		}
		return null;
	}

	public CraftingInventory getCraftingInventory() {
		if (inventoryCrafting == null) {
			inventoryCrafting = new CraftingInventory(new Container(null, -1) {
				@Override
				public boolean canUse(PlayerEntity playerIn) {
					return false;
				}
			}, 3, 3);
		}
		for (int i = 0; i < 9; i++) {
			inventoryCrafting.setInvStack(i, inventory.getInvStack(i));
		}
		return inventoryCrafting;
	}

	public boolean canMake(CraftingRecipe recipe) {
		if (recipe != null) {
			boolean missingOutput = false;
			int[] stacksInSlots = new int[9];
			for (int i = 0; i < 9; i++) {
				stacksInSlots[i] = inventory.getInvStack(i).getCount();
			}

			DefaultedList<Ingredient> ingredients = recipe.getPreviewInputs();
			for (Ingredient ingredient : ingredients) {
				if (ingredient != Ingredient.EMPTY) {
					boolean foundIngredient = false;
					for (int i = 0; i < 9; i++) {
						ItemStack stack = inventory.getInvStack(i);
						int requiredSize = locked ? 1 : 0;
						if (stack.getMaxCount() == 1) {
							requiredSize = 0;
						}
						if (stacksInSlots[i] > requiredSize) {
							if (ingredient.method_8093(stack)) {
								if (stack.getItem().getRecipeRemainder() != null) {
									if (!hasRoomForExtraItem(new ItemStack(stack.getItem().getRecipeRemainder()))) {
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
				if (hasOutputSpace(recipe.getOutput(), 9)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	boolean hasRoomForExtraItem(ItemStack stack) {
		ItemStack extraOutputSlot = inventory.getInvStack(10);
		if (extraOutputSlot.isEmpty()) {
			return true;
		}
		return hasOutputSpace(stack, 10);
	}

	public boolean hasOutputSpace(ItemStack output, int slot) {
		ItemStack stack = inventory.getInvStack(slot);
		if (stack.isEmpty()) {
			return true;
		}
		if (ItemUtils.isItemEqual(stack, output, true, true)) {
			if (stack.getMaxCount() > stack.getCount() + output.getCount()) {
				return true;
			}
		}
		return false;
	}

	public boolean make(CraftingRecipe recipe) {
		if (recipe == null || !canMake(recipe)) {
			return false;
		}
		for (int i = 0; i < recipe.getPreviewInputs().size(); i++) {
			DefaultedList<Ingredient> ingredients = recipe.getPreviewInputs();
			Ingredient ingredient = ingredients.get(i);
			// Looks for the best slot to take it from
			ItemStack bestSlot = inventory.getInvStack(i);
			if (ingredient.method_8093(bestSlot)) {
				handleContainerItem(bestSlot);
				bestSlot.decrement(1);
			} else {
				for (int j = 0; j < 9; j++) {
					ItemStack stack = inventory.getInvStack(j);
					if (ingredient.method_8093(stack)) {
						handleContainerItem(stack);
						stack.decrement(1); // TODO is this right? or do I need
											// to use it as an actull
											// crafting grid
						break;
					}
				}
			}
		}
		ItemStack output = inventory.getInvStack(9);
		ItemStack outputStack = recipe.craft(getCraftingInventory());
		if (output.isEmpty()) {
			inventory.setInvStack(9, outputStack.copy());
		} else {
			// TODO use ouputStack in someway?
			output.increment(recipe.getOutput().getCount());
		}
		return true;
	}

	private void handleContainerItem(ItemStack stack) {
		if (stack.getItem().hasRecipeRemainder()) {
			ItemStack containerItem = new ItemStack(stack.getItem().getRecipeRemainder());
			ItemStack extraOutputSlot = inventory.getInvStack(10);
			if (hasOutputSpace(containerItem, 10)) {
				if (extraOutputSlot.isEmpty()) {
					inventory.setInvStack(10, containerItem.copy());
				} else if (ItemUtils.isItemEqual(extraOutputSlot, containerItem, true, true)
						&& extraOutputSlot.getMaxCount() < extraOutputSlot.getCount() + containerItem.getCount()) {
					extraOutputSlot.increment(1);
				}
			}
		}
	}

	public boolean hasIngredient(Ingredient ingredient) {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = inventory.getInvStack(i);
			if (ingredient.method_8093(stack)) {
				return true;
			}
		}
		return false;
	}

	public boolean isItemValidForRecipeSlot(Recipe<CraftingInventory> recipe, ItemStack stack, int slotID) {
		if (recipe == null) {
			return true;
		}
		int bestSlot = findBestSlotForStack(recipe, stack);
		if (bestSlot != -1) {
			return bestSlot == slotID;
		}
		return true;
	}

	public int findBestSlotForStack(Recipe<CraftingInventory> recipe, ItemStack stack) {
		if (recipe == null) {
			return -1;
		}
		DefaultedList<Ingredient> ingredients = recipe.getPreviewInputs();
		List<Integer> possibleSlots = new ArrayList<>();
		for (int i = 0; i < recipe.getPreviewInputs().size(); i++) {
			ItemStack stackInSlot = inventory.getInvStack(i);
			Ingredient ingredient = ingredients.get(i);
			if (ingredient != Ingredient.EMPTY && ingredient.method_8093(stack)) {
				if (stackInSlot.isEmpty()) {
					possibleSlots.add(i);
				} else if (stackInSlot.getItem() == stack.getItem()) {
					if (stackInSlot.getMaxCount() >= stackInSlot.getCount() + stack.getCount()) {
						possibleSlots.add(i);
					}
				}
			}
		}
		// Slot, count
		Pair<Integer, Integer> smallestCount = null;
		for (Integer slot : possibleSlots) {
			ItemStack slotStack = inventory.getInvStack(slot);
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
	public void tick() {
		super.tick();
		if (world.isClient) {
			return;
		}
		CraftingRecipe recipe = getIRecipe();
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
		return TechRebornConfig.autoCraftingTableMaxEnergy;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return TechRebornConfig.autoCraftingTableMaxInput;
	}

	@Override
	public boolean canAcceptEnergy(Direction enumFacing) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(Direction enumFacing) {
		return false;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putBoolean("locked", locked);
		return super.toTag(tag);
	}

	@Override
	public void fromTag(CompoundTag tag) {
		if (tag.containsKey("locked")) {
			locked = tag.getBoolean("locked");
		}
		super.fromTag(tag);
	}

	// TileMachineBase
	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	private static IInventoryAccess<AutoCraftingTableBlockEntity> getInventoryAccess(){
		return (slotID, stack, facing, direction, blockEntity) -> {
			switch (direction){
				case INSERT:
					if (slotID > 8) {
						return false;
					}
					int bestSlot = blockEntity.findBestSlotForStack(blockEntity.getIRecipe(), stack);
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
	public Direction getFacingEnum() {
		return Direction.NORTH;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity playerIn) {
		return TRContent.Machine.AUTO_CRAFTING_TABLE.getStack();
	}

	// ItemHandlerProvider
	@Override
	public RebornInventory<AutoCraftingTableBlockEntity> getInventory() {
		return inventory;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, PlayerEntity player) {
		return new ContainerBuilder("autocraftingtable").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).slot(0, 28, 25).slot(1, 46, 25).slot(2, 64, 25).slot(3, 28, 43).slot(4, 46, 43)
				.slot(5, 64, 43).slot(6, 28, 61).slot(7, 46, 61).slot(8, 64, 61).outputSlot(9, 145, 42)
				.outputSlot(10, 145, 70).syncEnergyValue().syncIntegerValue(this::getProgress, this::setProgress)
				.syncIntegerValue(this::getMaxProgress, this::setMaxProgress)
				.syncIntegerValue(this::getLockedInt, this::setLockedInt).addInventory().create(this, syncID);
	}

	@Override
	public boolean hasSlotConfig() {
		return false;
	}
}
