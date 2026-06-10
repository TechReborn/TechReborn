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

import org.apache.commons.lang3.tuple.Pair;
import org.jspecify.annotations.Nullable;
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
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.recipe.recipes.RollingMachineRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

// TODO add tick and power bars.

public class RollingMachineBlockEntity extends PowerAcceptorBlockEntity
	implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

	public int[] craftingSlots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
	private TransientCraftingContainer craftCache;
	public final RebornInventory<RollingMachineBlockEntity> inventory = new RebornInventory<>(12, "RollingMachineBlockEntity", 64, this);
	public boolean isRunning;
	public int tickTime = 0;
	// Only synced to the client
	public int currentRecipeTime = 0;
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
		return TechRebornConfig.rollingMachineMaxEnergy.get();
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
		return TechRebornConfig.rollingMachineMaxInput.get();
	}

	@Override
	public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClientSide()) {
			return;
		}
		charge(10);

		TransientCraftingContainer craftMatrix = getCraftingMatrix(true);
		currentRecipe = findMatchingRecipe(craftMatrix, world);
		if (currentRecipe != null) {
			if (world.getGameTime() % 2 == 0) {
				balanceRecipe(craftMatrix);
			}
			currentRecipeOutput = currentRecipe.getShapedRecipe().assemble(recipeInput(craftMatrix));
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
		if (getStored() > getEuPerTick(currentRecipe.power())
			&& canMake(craftMatrix)) {
			setIsActive(true);
			useEnergy(getEuPerTick(currentRecipe.power()));
			tickTime++;
		} else {
			setIsActive(false);
			return;
		}
		// Cached recipe or valid recipe exists.
		// checked if we can make at least one.
		if (tickTime >= currentRecipeTime) {
			//craft one
			if (inventory.getItem(outputSlot).isEmpty()) {
				inventory.setItem(outputSlot, currentRecipeOutput.copy());
			}
			else {
				// we checked stack can fit in output slot in canMake()
				inventory.getItem(outputSlot).grow(currentRecipeOutput.getCount());
			}
			tickTime = 0;
			currentRecipeTime = Math.max((int) (currentRecipe.time() * (1.0 - getSpeedMultiplier())), 1);
			for (int i = 0; i < craftMatrix.getContainerSize(); i++) {
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
		if (this.getLevel().getBlockState(this.getBlockPos()).getBlock() instanceof BlockMachineBase blockMachineBase) {
			blockMachineBase.setActive(active, this.getLevel(), this.getBlockPos());
		}
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getLevel().getBlockState(this.getBlockPos()), this.getLevel().getBlockState(this.getBlockPos()), 3);
	}

	public Optional<TransientCraftingContainer> balanceRecipe(TransientCraftingContainer craftCache) {
		if (currentRecipe == null) {
			return Optional.empty();
		}
		if (level.isClientSide()) {
			return Optional.empty();
		}
		if (!locked) {
			return Optional.empty();
		}
		if (craftCache.isEmpty()) {
			return Optional.empty();
		}
		balanceSlot++;
		if (balanceSlot > craftCache.getContainerSize()) {
			balanceSlot = 0;
		}
		// Find the best slot for each item in a recipe, and move it if needed
		ItemStack sourceStack = inventory.getItem(balanceSlot);
		if (sourceStack.isEmpty()) {
			return Optional.empty();
		}
		List<Integer> possibleSlots = new ArrayList<>();
		for (int s = 0; s < currentRecipe.placementInfo().ingredients().size(); s++) {
			ItemStack stackInSlot = inventory.getItem(s);
			Ingredient ingredient = currentRecipe.placementInfo().ingredients().get(s);
			if (ingredient != null && ingredient.test(sourceStack)) {
				if (stackInSlot.isEmpty()) {
					possibleSlots.add(s);
				} else if (stackInSlot.getItem() == sourceStack.getItem()) {
					possibleSlots.add(s);
				}
			}
		}

		if (!possibleSlots.isEmpty()) {
			int totalItems = possibleSlots.stream()
				.mapToInt(value -> inventory.getItem(value).getCount()).sum();
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
				.mapToInt(value -> inventory.getItem(value).getCount())
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
			ItemStack slotStack = inventory.getItem(slot);
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
			|| inventory.getItem(bestSlot.getLeft()).isEmpty()
			|| !ItemUtils.isItemEqual(sourceStack, inventory.getItem(bestSlot.getLeft()), true, true)) {
			return Optional.empty();
		}
		sourceStack.shrink(1);
		inventory.getItem(bestSlot.getLeft()).grow(1);
		inventory.setHasChanged();

		return Optional.of(getCraftingMatrix());
	}

	private TransientCraftingContainer getCraftingMatrix() {
		return getCraftingMatrix(false);
	}

	private TransientCraftingContainer getCraftingMatrix(boolean forceRefresh) {
		if (craftCache == null) {
			craftCache = new TransientCraftingContainer(new RollingBEContainer(), 3, 3);
		}
		if (forceRefresh || inventory.hasChanged()) {
			for (int i = 0; i < 9; i++) {
				craftCache.setItem(i, inventory.getItem(i).copy());
			}
			inventory.resetHasChanged();
		}
		return craftCache;
	}
	private List<Item> fastIntlayout(){
		if (this.inventory == null) return null;
		ArrayList<Item> arrayList = new ArrayList<>(9);
		for (int i = 0; i < 9; i++){
			arrayList.add(this.inventory.getItem(i).getItem());
		}
		return arrayList;
	}

	private boolean checkNotEmpty(TransientCraftingContainer craftMatrix) {
		//checks if inventory is empty or considered quasi-empty.
		if (locked) {
			boolean returnValue = false;
			// for locked condition, we need to check if inventory contains item and all slots are empty or has more than one item.
			for (int i = 0; i < craftMatrix.getContainerSize(); i++) {
				ItemStack stack1 = craftMatrix.getItem(i);
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
			for (int i = 0; i < craftMatrix.getContainerSize(); i++) {
				ItemStack stack1 = craftMatrix.getItem(i);
				if (!stack1.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean canMake(TransientCraftingContainer craftMatrix) {
		ItemStack stack = findMatchingRecipeOutput(craftMatrix, this.level);
		if (stack.isEmpty()) {
			return false;
		}
		ItemStack output = inventory.getItem(outputSlot);
		if (output.isEmpty()) {
			return true;
		}
		return ItemUtils.isItemEqual(stack, output, true, true) && output.getCount() + stack.getCount() <= output.getMaxStackSize();
	}

	public List<RollingMachineRecipe> getAllRecipe(Level world) {
		return RecipeUtils.getRecipes(world, ModRecipes.ROLLING_MACHINE);
	}

	public ItemStack findMatchingRecipeOutput(TransientCraftingContainer inv, Level world) {
		RollingMachineRecipe recipe = findMatchingRecipe(inv, world);
		if (recipe == null) {
			return ItemStack.EMPTY;
		}
		return recipe.assemble(null);
	}

	public RollingMachineRecipe findMatchingRecipe(TransientCraftingContainer inv, Level world) {
		if (isCorrectCachedInventory()){
			return lastRecipe;
		}
		cachedInventoryStructure = fastIntlayout();
		CraftingInput input = recipeInput(inv);
		for (RollingMachineRecipe recipe : getAllRecipe(world)) {
			if (recipe.getShapedRecipe().matches(input, world)) {
				lastRecipe = recipe;
				currentRecipeTime = Math.max((int) (recipe.time() * (1.0 - getSpeedMultiplier())), 1);
				return recipe;
			}
		}
		lastRecipe = null;
		currentRecipeTime = 0;
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
	public ItemStack getToolDrop(final Player entityPlayer) {
		return TRContent.Machine.ROLLING_MACHINE.getStack();
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		this.isRunning = view.getBooleanOr("isRunning", false);
		this.tickTime = view.getIntOr("tickTime", 0);
		this.locked = view.getBooleanOr("locked", false);
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		view.putBoolean("isRunning", this.isRunning);
		view.putInt("tickTime", this.tickTime);
		view.putBoolean("locked", locked);
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
		if (tickTime == 0 || Math.max((int) (currentRecipe.time() * (1.0 - getSpeedMultiplier())), 1) == 0) {
			return 0;
		}
		return tickTime * scale / Math.max((int) (currentRecipe.time() * (1.0 - getSpeedMultiplier())), 1);
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final Player player) {
		return new ScreenHandlerBuilder("rollingmachine").player(player.getInventory())
			.inventory().hotbar()
			.addInventory().blockEntity(this)
			.slot(0, 30, 22).slot(1, 48, 22).slot(2, 66, 22)
			.slot(3, 30, 40).slot(4, 48, 40).slot(5, 66, 40)
			.slot(6, 30, 58).slot(7, 48, 58).slot(8, 66, 58)
			.onCraft(inv -> this.inventory.setItem(1, findMatchingRecipeOutput(getCraftingMatrix(), this.level)))
			.outputSlot(9, 124, 40)
			.energySlot(10, 8, 70)
			.syncEnergyValue().sync(ByteBufCodecs.INT, this::getBurnTime, this::setBurnTime).sync(ByteBufCodecs.INT, this::getLockedInt, this::setLockedInt)
			.sync(ByteBufCodecs.INT, this::getCurrentRecipeTime, this::setCurrentRecipeTime).addInventory().create(this, syncID);
	}

	public int getCurrentRecipeTime() {
		return currentRecipeTime;
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
		if (tickTime != 0 && currentRecipeTime != 0) {
			return tickTime * scale / currentRecipeTime;
		}
		return 0;
	}

	private static class RollingBEContainer extends AbstractContainerMenu {

		protected RollingBEContainer() {
			super(null, 0);
		}

		@Override
		public ItemStack quickMoveStack(Player player, int slot) {
			return ItemStack.EMPTY;
		}

		@Override
		public boolean stillValid(final Player playerEntity) {
			return true;
		}

	}

	@Override
	public boolean canBeUpgraded() {
		return true;
	}

	private static CraftingInput recipeInput(TransientCraftingContainer inventory) {
		List<ItemStack> stacks = new ArrayList<>(inventory.getContainerSize());
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			stacks.add(inventory.getItem(i));
		}
		return CraftingInput.of(inventory.getWidth(), inventory.getHeight(), stacks);
	}
}
