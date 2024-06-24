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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.crafting.RecipeUtils;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import techreborn.recipe.recipes.RollingMachineRecipe;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO add tick and power bars.

public class RollingMachineBlockEntity extends PowerAcceptorBlockEntity
	implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

	public int[] craftingSlots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
	private CraftingInventory craftCache;
	public final RebornInventory<RollingMachineBlockEntity> inventory = new RebornInventory<>(12, "RollingMachineBlockEntity", 64, this);
	public boolean isRunning;
	public int tickTime;
	// Only synced to the client
	public int currentRecipeTime;
	public ItemStack currentRecipeOutput = ItemStack.EMPTY;
	public RollingMachineRecipe currentRecipe;
	private final int outputSlot;
	public boolean locked = false;
	public int balanceSlot = 0;
	RollingMachineRecipe lastRecipe = null;
	private List<Item> cachedInventoryStructure = null;
	public RollingMachineBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.ROLLING_MACHINE, pos, state);
		outputSlot = 9;
	}

	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.rollingMachineMaxEnergy;
	}

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		return false;
	}

	@Override
	public long getBaseMaxOutput() {
		return 0;
	}

	@Override
	public long getBaseMaxInput() {
		return TechRebornConfig.rollingMachineMaxInput;
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClient) {
			return;
		}
		charge(10);

		CraftingInventory craftMatrix = getCraftingMatrix(true);
		currentRecipe = findMatchingRecipe(craftMatrix, world);
		if (currentRecipe != null) {
			if (world.getTime() % 2 == 0) {
				balanceRecipe(craftMatrix);
			}
			currentRecipeOutput = currentRecipe.getShapedRecipe().craft(recipeInput(craftMatrix), getWorld().getRegistryManager());
		} else {
			currentRecipeOutput = ItemStack.EMPTY;
		}
		craftMatrix = getCraftingMatrix();

		if (currentRecipeOutput.isEmpty() || !checkNotEmpty(craftMatrix)){
			// can't make anyway, reject.
			tickTime = 0;
			setIsActive(false);
			return;
		}
		// Now we ensured we can make something. Check energy state.
		if (getStored() > getEuPerTick(currentRecipe.getPower())
			&& tickTime < Math.max((int) (currentRecipe.getTime() * (1.0 - getSpeedMultiplier())), 1)
			&& canMake(craftMatrix)) {
			setIsActive(true);
			useEnergy(getEuPerTick(currentRecipe.getPower()));
			tickTime++;
		} else {
			setIsActive(false);
			return;
		}
		// Cached recipe or valid recipe exists.
		// checked if we can make at least one.
		if (tickTime >= Math.max((int) (currentRecipe.getTime() * (1.0 - getSpeedMultiplier())), 1)) {
			//craft one
			if (inventory.getStack(outputSlot).isEmpty()) {
				inventory.setStack(outputSlot, currentRecipeOutput.copy());
			}
			else {
				// we checked stack can fit in output slot in canMake()
				inventory.getStack(outputSlot).increment(currentRecipeOutput.getCount());
			}
			tickTime = 0;
			for (int i = 0; i < craftMatrix.size(); i++) {
				inventory.shrinkSlot(i, 1);
			}
			if (!locked) {
				currentRecipeOutput = ItemStack.EMPTY;
				currentRecipe = null;
			}
		}
	}

	public void setIsActive(boolean active) {
		if (active == isRunning) {
			return;
		}
		isRunning = active;
		if (this.getWorld().getBlockState(this.getPos()).getBlock() instanceof BlockMachineBase blockMachineBase) {
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
		// Find the best slot for each item in a recipe, and move it if needed
		ItemStack sourceStack = inventory.getStack(balanceSlot);
		if (sourceStack.isEmpty()) {
			return Optional.empty();
		}
		List<Integer> possibleSlots = new ArrayList<>();
		for (int s = 0; s < currentRecipe.getIngredients().size(); s++) {
			ItemStack stackInSlot = inventory.getStack(s);
			Ingredient ingredient = currentRecipe.getIngredients().get(s);
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

			// This makes an array of ints with the best possible slot distribution
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
					// We need to remove the int, not at the int, this seems to work around that
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

		// Slot, count
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
		inventory.setHashChanged();

		return Optional.of(getCraftingMatrix());
	}

	private CraftingInventory getCraftingMatrix() {
		return getCraftingMatrix(false);
	}

	private CraftingInventory getCraftingMatrix(boolean forceRefresh) {
		if (craftCache == null) {
			craftCache = new CraftingInventory(new RollingBEContainer(), 3, 3);
		}
		if (forceRefresh || inventory.hasChanged()) {
			for (int i = 0; i < 9; i++) {
				craftCache.setStack(i, inventory.getStack(i).copy());
			}
			inventory.resetHasChanged();
		}
		return craftCache;
	}
	private List<Item> fastIntlayout(){
		if (this.inventory == null) return null;
		ArrayList<Item> arrayList = new ArrayList<>(9);
		for (int i = 0; i < 9; i++){
			arrayList.add(this.inventory.getStack(i).getItem());
		}
		return arrayList;
	}

	private boolean checkNotEmpty(CraftingInventory craftMatrix) {
		//checks if inventory is empty or considered quasi-empty.
		if (locked) {
			boolean returnValue = false;
			// for locked condition, we need to check if inventory contains item and all slots are empty or has more than one item.
			for (int i = 0; i < craftMatrix.size(); i++) {
				ItemStack stack1 = craftMatrix.getStack(i);
				if (stack1.getCount() == 1) {
					return false;
				}
				if (stack1.getCount() > 1) {
					returnValue = true;
				}
			}
			return returnValue;
		}
		else {
			for (int i = 0; i < craftMatrix.size(); i++) {
				ItemStack stack1 = craftMatrix.getStack(i);
				if (!stack1.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean canMake(CraftingInventory craftMatrix) {
		ItemStack stack = findMatchingRecipeOutput(craftMatrix, this.world);
		if (stack.isEmpty()) {
			return false;
		}
		ItemStack output = inventory.getStack(outputSlot);
		if (output.isEmpty()) {
			return true;
		}
		return ItemUtils.isItemEqual(stack, output, true, true) && output.getCount() + stack.getCount() <= output.getMaxCount();
	}

	public List<RollingMachineRecipe> getAllRecipe(World world) {
		return RecipeUtils.getRecipes(world, ModRecipes.ROLLING_MACHINE);
	}

	public ItemStack findMatchingRecipeOutput(CraftingInventory inv, World world) {
		RollingMachineRecipe recipe = findMatchingRecipe(inv, world);
		if (recipe == null) {
			return ItemStack.EMPTY;
		}
		return recipe.getResult(getWorld().getRegistryManager());
	}

	public RollingMachineRecipe findMatchingRecipe(CraftingInventory inv, World world) {
		if (isCorrectCachedInventory()){
			return lastRecipe;
		}
		cachedInventoryStructure = fastIntlayout();
		CraftingRecipeInput input = recipeInput(inv);
		for (RollingMachineRecipe recipe : getAllRecipe(world)) {
			if (recipe.getShapedRecipe().matches(input, world)) {
				lastRecipe = recipe;
				return recipe;
			}
		}
		lastRecipe = null;
		return null;
	}

	private boolean isCorrectCachedInventory(){
		if (cachedInventoryStructure == null){
			return false;
		}
		List<Item> current = fastIntlayout();
		if (current == null || current.size() != this.cachedInventoryStructure.size()){
			return false;
		}
		for (int i = 0; i < current.size(); i++ ){
			if (current.get(i) != this.cachedInventoryStructure.get(i)){
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return TRContent.Machine.ROLLING_MACHINE.getStack();
	}

	@Override
	public void readNbt(final NbtCompound tagCompound, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(tagCompound, registryLookup);
		this.isRunning = tagCompound.getBoolean("isRunning");
		this.tickTime = tagCompound.getInt("tickTime");
		this.locked = tagCompound.getBoolean("locked");
	}

	@Override
	public void writeNbt(final NbtCompound tagCompound, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(tagCompound, registryLookup);
		tagCompound.putBoolean("isRunning", this.isRunning);
		tagCompound.putInt("tickTime", this.tickTime);
		tagCompound.putBoolean("locked", locked);
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
		if (tickTime == 0 || Math.max((int) (currentRecipe.getTime() * (1.0 - getSpeedMultiplier())), 1) == 0) {
			return 0;
		}
		return tickTime * scale / Math.max((int) (currentRecipe.getTime() * (1.0 - getSpeedMultiplier())), 1);
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("rollingmachine").player(player.getInventory())
			.inventory().hotbar()
			.addInventory().blockEntity(this)
			.slot(0, 30, 22).slot(1, 48, 22).slot(2, 66, 22)
			.slot(3, 30, 40).slot(4, 48, 40).slot(5, 66, 40)
			.slot(6, 30, 58).slot(7, 48, 58).slot(8, 66, 58)
			.onCraft(inv -> this.inventory.setStack(1, findMatchingRecipeOutput(getCraftingMatrix(), this.world)))
			.outputSlot(9, 124, 40)
			.energySlot(10, 8, 70)
			.syncEnergyValue().sync(PacketCodecs.INTEGER, this::getBurnTime, this::setBurnTime).sync(PacketCodecs.INTEGER, this::getLockedInt, this::setLockedInt)
			.sync(PacketCodecs.INTEGER, this::getCurrentRecipeTime, this::setCurrentRecipeTime).addInventory().create(this, syncID);
	}

	public int getCurrentRecipeTime() {
		if (currentRecipe != null) {
			return currentRecipe.getTime();
		}

		return 0;
	}

	public RollingMachineBlockEntity setCurrentRecipeTime(int currentRecipeTime) {
		this.currentRecipeTime = currentRecipeTime;
		return this;
	}

	// Easiest way to sync back to the client
	public int getLockedInt() {
		return locked ? 1 : 0;
	}

	public void setLockedInt(int lockedInt) {
		locked = lockedInt == 1;
	}

	public int getProgressScaled(final int scale) {
		if (tickTime != 0 && Math.max((int) (currentRecipeTime * (1.0 - getSpeedMultiplier())), 1) != 0) {
			return tickTime * scale / Math.max((int) (currentRecipeTime * (1.0 - getSpeedMultiplier())), 1);
		}
		return 0;
	}

	private static class RollingBEContainer extends ScreenHandler {

		protected RollingBEContainer() {
			super(null, 0);
		}

		@Override
		public ItemStack quickMove(PlayerEntity player, int slot) {
			return ItemStack.EMPTY;
		}

		@Override
		public boolean canUse(final PlayerEntity playerEntity) {
			return true;
		}

	}

	@Override
	public boolean canBeUpgraded() {
		return true;
	}

	private static CraftingRecipeInput recipeInput(CraftingInventory inventory) {
		List<ItemStack> stacks = new ArrayList<>(inventory.size());
		for (int i = 0; i < inventory.size(); i++) {
			stacks.add(inventory.getStack(i));
		}
		return CraftingRecipeInput.create(inventory.getWidth(), inventory.getHeight(), stacks);
	}
}
