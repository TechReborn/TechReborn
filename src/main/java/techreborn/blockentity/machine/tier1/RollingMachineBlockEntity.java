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

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import team.reborn.energy.EnergySide;
import techreborn.api.recipe.recipes.RollingMachineRecipe;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//TODO add tick and power bars.

public class RollingMachineBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

	public int[] craftingSlots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
	private CraftingInventory craftCache;
	public RebornInventory<RollingMachineBlockEntity> inventory = new RebornInventory<>(12, "RollingMachineBlockEntity", 64, this);
	public boolean isRunning;
	public int tickTime;
	@NotNull
	public ItemStack currentRecipeOutput;
	public RollingMachineRecipe currentRecipe;
	private final int outputSlot;
	public boolean locked = false;
	public int balanceSlot = 0;

	public RollingMachineBlockEntity() {
		super(TRBlockEntities.ROLLING_MACHINE);
		outputSlot = 9;
	}

	@Override
	public double getBaseMaxPower() {
		return TechRebornConfig.rollingMachineMaxEnergy;
	}

	@Override
	public boolean canProvideEnergy(EnergySide side) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return TechRebornConfig.rollingMachineMaxInput;
	}

	@Override
	public void tick() {
		super.tick();
		if (world.isClient) {
			return;
		}
		charge(10);

		CraftingInventory craftMatrix = getCraftingMatrix();
		currentRecipe = findMatchingRecipe(craftMatrix, world);
		if (currentRecipe != null) {
			setIsActive(true);
			if (world.getTime() % 2 == 0) {
				Optional<CraftingInventory> balanceResult = balanceRecipe(craftMatrix);
				if (balanceResult.isPresent()) {
					craftMatrix = balanceResult.get();
				}
			}
			currentRecipeOutput = currentRecipe.craft(craftMatrix);
		} else {
			currentRecipeOutput = ItemStack.EMPTY;
		}

		if (!currentRecipeOutput.isEmpty() && canMake(craftMatrix)) {
			if (tickTime >= Math.max((int) (TechRebornConfig.rollingMachineRunTime * (1.0 - getSpeedMultiplier())), 1)) {
				currentRecipeOutput = findMatchingRecipeOutput(craftMatrix, world);
				if (!currentRecipeOutput.isEmpty()) {
					boolean hasCrafted = false;
					if (inventory.getStack(outputSlot).isEmpty()) {
						inventory.setStack(outputSlot, currentRecipeOutput.copy());
						tickTime = 0;
						hasCrafted = true;
					} else {
						if (inventory.getStack(outputSlot).getCount()
								+ currentRecipeOutput.getCount() <= currentRecipeOutput.getMaxCount()) {
							final ItemStack stack = inventory.getStack(outputSlot);
							stack.setCount(stack.getCount() + currentRecipeOutput.getCount());
							inventory.setStack(outputSlot, stack);
							tickTime = 0;
							hasCrafted = true;
						} else {
							setIsActive(false);
						}
					}
					if (hasCrafted) {
						for (int i = 0; i < craftMatrix.size(); i++) {
							inventory.shrinkSlot(i, 1);
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
			if (getStored(EnergySide.UNKNOWN) > getEuPerTick(TechRebornConfig.rollingMachineEnergyPerTick)
					&& tickTime < Math.max((int) (TechRebornConfig.rollingMachineRunTime * (1.0 - getSpeedMultiplier())), 1)
					&& canMake(craftMatrix)) {
				useEnergy(getEuPerTick(TechRebornConfig.rollingMachineEnergyPerTick));
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
		if (active == isRunning) {
			return;
		}
		isRunning = active;
		if (this.getWorld().getBlockState(this.getPos()).getBlock() instanceof BlockMachineBase) {
			BlockMachineBase blockMachineBase = (BlockMachineBase) this.getWorld().getBlockState(this.getPos()).getBlock();
			blockMachineBase.setActive(active, this.getWorld(), this.getPos());
		}
		this.getWorld().updateListeners(this.getPos(), this.getWorld().getBlockState(this.getPos()), this.getWorld().getBlockState(this.getPos()), 3);
	}

	public Optional<CraftingInventory> balanceRecipe(CraftingInventory craftCache) {
		if (currentRecipe == null) {
			return Optional.empty();
		}
		if (world.isClient) {
			return Optional.empty();
		}
		if (!locked) {
			return Optional.empty();
		}
		if (craftCache.isEmpty()) {
			return Optional.empty();
		}
		balanceSlot++;
		if (balanceSlot > craftCache.size()) {
			balanceSlot = 0;
		}
		//Find the best slot for each item in a recipe, and move it if needed
		ItemStack sourceStack = inventory.getStack(balanceSlot);
		if (sourceStack.isEmpty()) {
			return Optional.empty();
		}
		List<Integer> possibleSlots = new ArrayList<>();
		for (int s = 0; s < currentRecipe.getPreviewInputs().size(); s++) {
			ItemStack stackInSlot = inventory.getStack(s);
			Ingredient ingredient = currentRecipe.getPreviewInputs().get(s);
			if (ingredient != Ingredient.EMPTY && ingredient.test(sourceStack)) {
				if (stackInSlot.isEmpty()) {
					possibleSlots.add(s);
				} else if (stackInSlot.getItem() == sourceStack.getItem()) {
					possibleSlots.add(s);
				}
			}
		}

		if (!possibleSlots.isEmpty()) {
			int totalItems = possibleSlots.stream()
					.mapToInt(value -> inventory.getStack(value).getCount()).sum();
			int slots = possibleSlots.size();

			//This makes an array of ints with the best possible slot EnvTyperibution
			int[] split = new int[slots];
			int remainder = totalItems % slots;
			Arrays.fill(split, totalItems / slots);
			while (remainder > 0) {
				for (int i = 0; i < split.length; i++) {
					if (remainder > 0) {
						split[i] += 1;
						remainder--;
					}
				}
			}

			List<Integer> slotEnvTyperubution = possibleSlots.stream()
					.mapToInt(value -> inventory.getStack(value).getCount())
					.boxed().collect(Collectors.toList());

			boolean needsBalance = false;
			for (int required : split) {
				if (slotEnvTyperubution.contains(required)) {
					//We need to remove the int, not at the int, this seems to work around that
					slotEnvTyperubution.remove(Integer.valueOf(required));
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
			ItemStack slotStack = inventory.getStack(slot);
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
				|| inventory.getStack(bestSlot.getLeft()).isEmpty()
				|| !ItemUtils.isItemEqual(sourceStack, inventory.getStack(bestSlot.getLeft()), true, true)) {
			return Optional.empty();
		}
		sourceStack.decrement(1);
		inventory.getStack(bestSlot.getLeft()).increment(1);
		inventory.setChanged();

		return Optional.of(getCraftingMatrix());
	}

	private CraftingInventory getCraftingMatrix() {
		if (craftCache == null) {
			craftCache = new CraftingInventory(new RollingBEContainer(), 3, 3);
		}
		if (inventory.hasChanged()) {
			for (int i = 0; i < 9; i++) {
				craftCache.setStack(i, inventory.getStack(i).copy());
			}
			inventory.resetChanged();
		}
		return craftCache;
	}

	public boolean canMake(CraftingInventory craftMatrix) {
		ItemStack stack = findMatchingRecipeOutput(craftMatrix, this.world);
		if (locked) {
			for (int i = 0; i < craftMatrix.size(); i++) {
				ItemStack stack1 = craftMatrix.getStack(i);
				if (!stack1.isEmpty() && stack1.getCount() < 2) {
					return false;
				}
			}
		}
		if (stack.isEmpty()) {
			return false;
		}
		ItemStack output = inventory.getStack(outputSlot);
		if (output.isEmpty()) {
			return true;
		}
		return ItemUtils.isItemEqual(stack, output, true, true);
	}

	public List<RollingMachineRecipe> getAllRecipe(World world) {
		return ModRecipes.ROLLING_MACHINE.getRecipes(world);
	}

	public ItemStack findMatchingRecipeOutput(CraftingInventory inv, World world) {
		RollingMachineRecipe recipe = findMatchingRecipe(inv, world);
		if (recipe == null) {
			return ItemStack.EMPTY;
		}
		return recipe.getOutput();
	}

	public RollingMachineRecipe findMatchingRecipe(CraftingInventory inv, World world) {
		for (RollingMachineRecipe recipe : getAllRecipe(world)) {
			if (recipe.matches(inv, world)) {
				return recipe;
			}
		}
		return null;
	}

	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return TRContent.Machine.ROLLING_MACHINE.getStack();
	}

	@Override
	public void fromTag(BlockState blockState, final CompoundTag tagCompound) {
		super.fromTag(blockState, tagCompound);
		this.isRunning = tagCompound.getBoolean("isRunning");
		this.tickTime = tagCompound.getInt("tickTime");
		this.locked = tagCompound.getBoolean("locked");
	}

	@Override
	public CompoundTag toTag(final CompoundTag tagCompound) {
		super.toTag(tagCompound);
		tagCompound.putBoolean("isRunning", this.isRunning);
		tagCompound.putInt("tickTime", this.tickTime);
		tagCompound.putBoolean("locked", locked);
		return tagCompound;
	}

	@Override
	public RebornInventory<RollingMachineBlockEntity> getInventory() {
		return inventory;
	}

	public int getBurnTime() {
		return tickTime;
	}

	public void setBurnTime(final int burnTime) {
		this.tickTime = burnTime;
	}

	public int getBurnTimeRemainingScaled(final int scale) {
		if (tickTime == 0 || Math.max((int) (TechRebornConfig.rollingMachineRunTime * (1.0 - getSpeedMultiplier())), 1) == 0) {
			return 0;
		}
		return tickTime * scale / Math.max((int) (TechRebornConfig.rollingMachineRunTime * (1.0 - getSpeedMultiplier())), 1);
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("rollingmachine").player(player.inventory)
				.inventory().hotbar()
				.addInventory().blockEntity(this)
				.slot(0, 30, 22).slot(1, 48, 22).slot(2, 66, 22)
				.slot(3, 30, 40).slot(4, 48, 40).slot(5, 66, 40)
				.slot(6, 30, 58).slot(7, 48, 58).slot(8, 66, 58)
				.onCraft(inv -> this.inventory.setStack(1, findMatchingRecipeOutput(getCraftingMatrix(), this.world)))
				.outputSlot(9, 124, 40)
				.energySlot(10, 8, 70)
				.syncEnergyValue().sync(this::getBurnTime, this::setBurnTime).sync(this::getLockedInt, this::setLockedInt).addInventory().create(this, syncID);
	}

	//Easyest way to sync back to the client
	public int getLockedInt() {
		return locked ? 1 : 0;
	}

	public void setLockedInt(int lockedInt) {
		locked = lockedInt == 1;
	}

	public int getProgressScaled(final int scale) {
		if (tickTime != 0 && Math.max((int) (TechRebornConfig.rollingMachineRunTime * (1.0 - getSpeedMultiplier())), 1) != 0) {
			return tickTime * scale / Math.max((int) (TechRebornConfig.rollingMachineRunTime * (1.0 - getSpeedMultiplier())), 1);
		}
		return 0;
	}

	private static class RollingBEContainer extends ScreenHandler {

		protected RollingBEContainer() {
			super(null, 0);
		}

		@Override
		public boolean canUse(final PlayerEntity entityplayer) {
			return true;
		}

	}

	@Override
	public boolean canBeUpgraded() {
		return true;
	}
}
