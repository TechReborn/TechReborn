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
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class AutoCraftingTableBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

	public static final int CRAFTING_HEIGHT = 3;
	public static final int CRAFTING_WIDTH = 3;
	public static final int CRAFTING_AREA = CRAFTING_HEIGHT*CRAFTING_WIDTH;

	public RebornInventory<AutoCraftingTableBlockEntity> inventory = new RebornInventory<>(CRAFTING_AREA+2, "AutoCraftingTableBlockEntity", 64, this);
	private final int OUTPUT_SLOT = CRAFTING_AREA; // first slot is indexed by 0, so this is the last non crafting slot
	private final int EXTRA_OUTPUT_SLOT = CRAFTING_AREA + 1;

	public int progress;
	public int maxProgress = 120;
	public int euTick = 10;
	public int balanceSlot = 0;

	private List<Item> cachedInventoryStructure = null;
	CraftingInventory inventoryCrafting = null;
	CraftingRecipe lastRecipe = null;


	public boolean locked = false;

	public AutoCraftingTableBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.AUTO_CRAFTING_TABLE, pos, state);
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
	private List<Item> fastIntlayout(){
		if (this.inventory == null) return null;
		ArrayList<Item> arrayList = new ArrayList<>(9);
		for (int i = 0; i < 9; i++){
			arrayList.add(this.inventory.getStack(i).getItem());
		}
		return arrayList;
	}

	@Nullable
	// it should return any recipe that matches inventory, even if it does not have enough amount
	public CraftingRecipe getCurrentRecipe() {
		if (world == null) return null;
		if (isCorrectCachedInventory()){
			return lastRecipe;
		}
		CraftingInventory crafting = getCraftingInventory();
		if (crafting.isEmpty()) return null;

		Optional<CraftingRecipe> testRecipe = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, crafting, world);
		cachedInventoryStructure = fastIntlayout();
		if (testRecipe.isPresent()) {
			lastRecipe = testRecipe.get();
			return lastRecipe;
		}
		lastRecipe = null;
		return null;
	}

	private CraftingInventory getCraftingInventory() {
		if (inventoryCrafting == null) {
			inventoryCrafting = new CraftingInventory(new ScreenHandler(null, -1) {
				@Override
				public boolean canUse(PlayerEntity playerIn) {
					return false;
				}
			}, CRAFTING_WIDTH, CRAFTING_HEIGHT);
		}
		for (int i = 0; i < CRAFTING_AREA; i++) {
			inventoryCrafting.setStack(i, inventory.getStack(i));
		}
		return inventoryCrafting;
	}

	// Check if we have recipe, inputs and space for outputs
	private boolean canMake(CraftingRecipe recipe) {
		if (world == null) return false;
		if (recipe == null) return false;

		CraftingInventory crafting = getCraftingInventory();
		if (crafting.isEmpty()) return false;

		// Don't allow recipe to change (Keep at least one of each slot stocked, assuming it's actually a recipe)
		if (locked) {
			for(int i = 0; i < CRAFTING_AREA; i++){
				if(crafting.getStack(i).getCount() == 1){
					return false;
				}
			}
		}
		if (!hasOutputSpace(recipe.getOutput(), OUTPUT_SLOT)) return false;
		if (!recipe.matches(crafting, world)) return false;


		DefaultedList<ItemStack> remainingStacks = recipe.getRemainder(crafting);
		for (ItemStack stack : remainingStacks) {
			if (!stack.isEmpty() && !hasRoomForExtraItem(stack)) return false;
		}

		return true;
	}

	private boolean hasRoomForExtraItem(ItemStack stack) {
		ItemStack extraOutputSlot = inventory.getStack(EXTRA_OUTPUT_SLOT);
		if (extraOutputSlot.isEmpty()) {
			return true;
		}
		return hasOutputSpace(stack, EXTRA_OUTPUT_SLOT);
	}

	private boolean hasOutputSpace(ItemStack output, int slot) {
		ItemStack stack = inventory.getStack(slot);
		if (stack.isEmpty()) {
			return true;
		}
		if(stack.getMaxCount() == stack.getCount()){
			return false;
		}
		if (ItemUtils.isItemEqual(stack, output, true, true)) {
			return stack.getMaxCount() >= stack.getCount() + output.getCount();
		}
		return false;
	}

	private boolean hasOutputSpaceAny(int slot) {
		ItemStack stack = inventory.getStack(slot);
		if (stack.isEmpty()) {
			return true;
		}
		return stack.getMaxCount() != stack.getCount();
	}
	private boolean make(CraftingRecipe recipe) {
		if (recipe == null || !canMake(recipe)) {
			return false;
		}
		DefaultedList<Ingredient> ingredients = recipe.getIngredients();
		// each slot can only be used once because in canMake we only checked if decrement by 1 still retains the recipe
		// otherwise recipes can break when an ingredient is used multiple times
		boolean[] slotUsed = new boolean[CRAFTING_AREA];
		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			Ingredient ingredient = ingredients.get(i);
			// Looks for the best slot to take it from
			ItemStack bestSlot = inventory.getStack(i);
			if (ingredient.test(bestSlot) && !slotUsed[i]) {
				slotUsed[i] = true;
				ItemStack remainderStack = getRemainderItem(bestSlot);
				bestSlot.decrement(1);
				if (!remainderStack.isEmpty()) {
					moveExtraOutput(remainderStack);
				}

			} else {
				// check all slots in search of the ingredient
				for (int j = 0; j < CRAFTING_AREA; j++) {
					ItemStack stack = inventory.getStack(j);
					if (ingredient.test(stack) && !slotUsed[j]) {
						slotUsed[j] = true;
						ItemStack remainderStack = getRemainderItem(stack);
						stack.decrement(1);
						if (!remainderStack.isEmpty()) {
							moveExtraOutput(remainderStack);
						}
					}
				}
			}
		}
		ItemStack output = inventory.getStack(OUTPUT_SLOT);
		ItemStack outputStack = recipe.craft(getCraftingInventory());
		if (output.isEmpty()) {
			inventory.setStack(OUTPUT_SLOT, outputStack.copy());
		} else {
			output.increment(recipe.getOutput().getCount());
		}
		return true;
	}

	private void moveExtraOutput(ItemStack stack){
		ItemStack currentExtraOutput = inventory.getStack(EXTRA_OUTPUT_SLOT);
		if (currentExtraOutput.isEmpty()){
			inventory.setStack(EXTRA_OUTPUT_SLOT, stack.copy());
		} else {
			currentExtraOutput.increment(stack.getCount());
		}
	}

	private ItemStack getRemainderItem(ItemStack stack) {
		if (stack.getItem().hasRecipeRemainder()) {
			return new ItemStack(stack.getItem().getRecipeRemainder());
		}
		return ItemStack.EMPTY;
	}

	private Optional<CraftingInventory> balanceRecipe(CraftingInventory craftCache) {
		if (world == null || world.isClient) return Optional.empty();
		if (craftCache.isEmpty()) return Optional.empty();

		CraftingRecipe currentRecipe = lastRecipe;
		if (currentRecipe == null) {
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
		HashSet<Integer> possibleSlots = new LinkedHashSet<>(16);
		List<Integer> slotDistribution = new ArrayList<>(9);
		int totalItems = 0;
		for (int s = 0; s < currentRecipe.getIngredients().size(); s++) {
			for (int i = 0; i < CRAFTING_AREA; i++) {
				if (possibleSlots.contains(i)) {
					continue;
				}
				ItemStack stackInSlot = inventory.getStack(i);
				Ingredient ingredient = currentRecipe.getIngredients().get(s);
				if (ingredient != Ingredient.EMPTY && ingredient.test(sourceStack)) {
					if (stackInSlot.getItem() == sourceStack.getItem()) {
						possibleSlots.add(i);
						int count = stackInSlot.getCount();
						totalItems += count;
						slotDistribution.add(count);
						break;
					}
				}
			}

		}

		if (!possibleSlots.isEmpty()) {
			int slots = possibleSlots.size();

			//This makes an array of ints with the best possible slot distribution
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


			boolean needsBalance = false;
			for (int required : split) {
				if (slotDistribution.contains(required)) {
					//We need to remove the int, not at the int, this seems to work around that
					slotDistribution.remove(Integer.valueOf(required));
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
		if (bestSlot.getLeft() == balanceSlot
				|| bestSlot.getRight() == sourceStack.getCount()
				|| inventory.getStack(bestSlot.getLeft()).isEmpty()
				|| !ItemUtils.isItemEqual(sourceStack, inventory.getStack(bestSlot.getLeft()), true, true)) {
			return Optional.empty();
		}
		sourceStack.decrement(1);
		inventory.getStack(bestSlot.getLeft()).increment(1);
		inventory.setHashChanged();

		return Optional.of(getCraftingInventory());
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClient) {
			return;
		}
		if (getStored() < (long) euTick * maxProgress || !hasOutputSpaceAny(OUTPUT_SLOT)){
			return;
		}
		CraftingRecipe recipe = getCurrentRecipe();
		if (recipe != null) {
			Optional<CraftingInventory> balanceResult = balanceRecipe(getCraftingInventory());
			balanceResult.ifPresent(craftingInventory -> inventoryCrafting = craftingInventory);
		}
		if (recipe == null || !canMake(recipe)) {
			progress = 0;
			return;
		}

		if (progress >= maxProgress) {
			if (make(recipe)) {
				progress = 0;
				useEnergy((long) maxProgress * euTick);
			}
		} else {
			progress++;
			if (progress == 1) {
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.AUTO_CRAFTING,
					SoundCategory.BLOCKS, 0.3F, 0.8F);
			}
			//useEnergy(euTick);
		}
	}

	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.autoCraftingTableMaxEnergy;
	}

	@Override
	public long getBaseMaxOutput() {
		return 0;
	}

	@Override
	public long getBaseMaxInput() {
		return TechRebornConfig.autoCraftingTableMaxInput;
	}

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		return false;
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		tag.putBoolean("locked", locked);
		super.writeNbt(tag);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		if (tag.contains("locked")) {
			locked = tag.getBoolean("locked");
		}
		super.readNbt(tag);
	}

	// MachineBaseBlockEntity
	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	@Override
	public boolean hasSlotConfig() {
		return true;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity playerIn) {
		return TRContent.Machine.AUTO_CRAFTING_TABLE.getStack();
	}

	// InventoryProvider
	@Override
	public RebornInventory<AutoCraftingTableBlockEntity> getInventory() {
		return inventory;
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("autocraftingtable").player(player.getInventory()).inventory().hotbar().addInventory()
				.blockEntity(this)
				.slot(0, 28, 25).slot(1, 46, 25).slot(2, 64, 25)
				.slot(3, 28, 43).slot(4, 46, 43).slot(5, 64, 43)
				.slot(6, 28, 61).slot(7, 46, 61).slot(8, 64, 61)
				.outputSlot(OUTPUT_SLOT, 145, 42)
				.outputSlot(EXTRA_OUTPUT_SLOT, 145, 70)
				.syncEnergyValue().sync(this::getProgress, this::setProgress)
				.sync(this::getMaxProgress, this::setMaxProgress)
				.sync(this::getLockedInt, this::setLockedInt).addInventory().create(this, syncID);
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

	public int getLockedInt() {
		return locked ? 1 : 0;
	}

	public void setLockedInt(int lockedInt) {
		locked = lockedInt == 1;
	}

}
