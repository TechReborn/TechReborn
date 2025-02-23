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
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.*;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class AutoCraftingTableBlockEntity extends PowerAcceptorBlockEntity
	implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

	public static final int CRAFTING_HEIGHT = 3;
	public static final int CRAFTING_WIDTH = 3;
	public static final int CRAFTING_AREA = CRAFTING_HEIGHT * CRAFTING_WIDTH;

	public final RebornInventory<AutoCraftingTableBlockEntity> inventory;
	private final BalanceTable balanceTable = new BalanceTable();
	private final int OUTPUT_SLOT = CRAFTING_AREA; // first slot is indexed by 0, so this is the last non crafting slot
	private final int EXTRA_OUTPUT_SLOT = CRAFTING_AREA + 1;

	public int progress = 0;
	public final int defaultMaxProgress = 120;
	public int maxProgress = defaultMaxProgress; // changes based on speed upgrades
	public final int euTick = 10;

	CraftingInventory inventoryCrafting;
	CraftingRecipe lastRecipe = null;
	ItemStack outputPreview = ItemStack.EMPTY;

	public boolean locked = false;

	public AutoCraftingTableBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.AUTO_CRAFTING_TABLE, pos, state);
		inventoryCrafting = new CraftingInventory(new ScreenHandler(null, -1) {
			@Override
			public ItemStack quickMove(PlayerEntity player, int index) {
				return ItemStack.EMPTY;
			}

			@Override
			public boolean canUse(PlayerEntity playerIn) {
				return false;
			}
		}, CRAFTING_WIDTH, CRAFTING_HEIGHT);
		inventory = new RebornInventory<>(CRAFTING_AREA + 2, "AutoCraftingTableBlockEntity", 64, this) {
			private void syncStack(int slot, ItemStack stack) {
				if (slot < CRAFTING_AREA) {
					inventoryCrafting.setStack(slot, stack);
				}
			}

			@Override
			public void deserializeNBT(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
				super.deserializeNBT(tag, registryLookup);
				for (int i = 0; i < CRAFTING_AREA; i++) {
					inventoryCrafting.setStack(i, inventory.getStack(i));
				}
			}

			@Override
			public void setStack(int slot, @NotNull ItemStack stack) {
				super.setStack(slot, stack);
				syncStack(slot, stack);
			}

			@Override
			public ItemStack removeStack(int i) {
				syncStack(i, ItemStack.EMPTY);
				return super.removeStack(i);
			}

			@Override
			public ItemStack removeStack(int i, int i1) {
				ItemStack stack = super.removeStack(i, i1);
				if (this.getStack(i).isEmpty()) {
					syncStack(i, ItemStack.EMPTY);
				}
				return stack;
			}

			@Override
			public ItemStack shrinkSlot(int slot, int count) {
				ItemStack stack = super.shrinkSlot(slot, count);
				if (this.getStack(slot).isEmpty()) {
					syncStack(slot, ItemStack.EMPTY);
				}
				return stack;
			}
		};
	}

	@Nullable
	public boolean updateCurrentRecipe(CraftingRecipeInput input) {
		if (!(world instanceof ServerWorld serverWorld)) return false;

		if (lastRecipe != null && lastRecipe.matches(input, world)) {
			if (outputPreview == ItemStack.EMPTY) {
				balanceTable.updateLayout(input);
				outputPreview = lastRecipe.craft(input, world.getRegistryManager());
			} else if (lastRecipe instanceof SpecialCraftingRecipe && balanceTable.updateLayout(input)) {
				outputPreview = lastRecipe.craft(input, world.getRegistryManager());
			}
			return true;
		}

		if (balanceTable.updateLayout(input)) {
			Optional<CraftingRecipe> testRecipe = serverWorld.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, input, world).map(RecipeEntry::value);
			if (testRecipe.isPresent()) {
				lastRecipe = testRecipe.get();
				outputPreview = lastRecipe.craft(input, world.getRegistryManager());
				return true;
			} else {
				outputPreview = ItemStack.EMPTY;
			}
		}
		return false;
	}

	@Nullable
	private ItemStack getRecipeReminder(CraftingRecipeInput input) {
		DefaultedList<ItemStack> remainingStacks = lastRecipe.getRecipeRemainders(input);
		ItemStack reminderStack, recipeReminder = ItemStack.EMPTY;
		for (int slot = 0, size = remainingStacks.size(); slot < size; slot++) {
			reminderStack = remainingStacks.get(slot);
			if (!reminderStack.isEmpty()) {
				recipeReminder = reminderStack.copy();
				for (slot = slot + 1; slot < size; slot++) {
					reminderStack = remainingStacks.get(slot);
					if (!reminderStack.isEmpty()) {
						if (ItemStack.areItemsAndComponentsEqual(recipeReminder, reminderStack)) {
							recipeReminder.increment(reminderStack.getCount());
						} else {
							return null;
						}
					}
				}
				break;
			}
		}
		return recipeReminder;
	}

	private void make(CraftingRecipeInput.Positioned positioned, ItemStack resultStack, ItemStack remainderStack) {
		CraftingRecipeInput input = positioned.input();
		int width = input.getWidth();
		int max = (positioned.top() + input.getHeight()) * CRAFTING_WIDTH;
		int space = CRAFTING_WIDTH - width;
		for (int slot = positioned.top() * CRAFTING_WIDTH + positioned.left(); slot < max; slot += space) {
			for (int end = slot + width; slot < end; slot++) {
				if (!this.inventory.getStack(slot).isEmpty()) {
					this.inventory.shrinkSlot(slot, 1);
				}
			}
		}
		moveOutput(resultStack, OUTPUT_SLOT);
		if (!remainderStack.isEmpty()) {
			moveOutput(remainderStack, EXTRA_OUTPUT_SLOT);
		}
		inventory.resetHasChanged();
		if (inventoryCrafting.isEmpty()) {
			outputPreview = ItemStack.EMPTY;
		}
	}

	private boolean hasOutputSpace(ItemStack output, int slot) {
		ItemStack stack = inventory.getStack(slot);
		if (stack.isEmpty()) {
			return true;
		}
		if (ItemUtils.isItemEqual(stack, output, true, true)) {
			return stack.getMaxCount() >= stack.getCount() + output.getCount();
		}
		return false;
	}

	private void moveOutput(ItemStack stack, int slot) {
		ItemStack currentOutput = inventory.getStack(slot);
		if (currentOutput.isEmpty()) {
			inventory.setStack(slot, stack.copy());
		} else {
			currentOutput.increment(stack.getCount());
		}
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClient || getStored() < getEuPerTick(euTick)) {
			return;
		}
		if (inventoryCrafting.isEmpty()) {
			if (progress == 0) return;
			progress = 0;
			maxProgress = Math.max((int) (defaultMaxProgress * (1.0 - getSpeedMultiplier())), 1);
			outputPreview = ItemStack.EMPTY;
			return;
		}
		CraftingRecipeInput.Positioned positioned = inventoryCrafting.createPositionedRecipeInput();
		CraftingRecipeInput input = positioned.input();
		if (!updateCurrentRecipe(input) || !hasOutputSpace(outputPreview, OUTPUT_SLOT)) {
			progress = 0;
			maxProgress = Math.max((int) (defaultMaxProgress * (1.0 - getSpeedMultiplier())), 1);
			return;
		}

		balanceTable.balance(inventory, input);

		// Don't allow recipe to change (Keep at least one of each slot stocked, assuming it's actually a recipe)
		if (locked) {
			for (ItemStack stack : input.getStacks()) {
				if (stack.getCount() == 1) {
					return;
				}
			}
		}

		ItemStack recipeReminder = getRecipeReminder(input);
		if (recipeReminder == null || !hasOutputSpace(recipeReminder, EXTRA_OUTPUT_SLOT)) {
			return;
		}

		if (progress >= maxProgress) {
			progress = 0;
			maxProgress = Math.max((int) (defaultMaxProgress * (1.0 - getSpeedMultiplier())), 1);
			make(positioned, outputPreview, recipeReminder);
		} else {
			progress++;
			if (progress == 1 && !isMuffled()) {
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.AUTO_CRAFTING,
					SoundCategory.BLOCKS, 0.3F, 0.8F);
			}
			useEnergy(getEuPerTick(euTick));
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
	public void writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("locked", locked);
		super.writeNbt(tag, registryLookup);
	}

	@Override
	public void readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		if (tag.contains("locked")) {
			locked = tag.getBoolean("locked");
		}
		super.readNbt(tag, registryLookup);
	}

	// MachineBaseBlockEntity
	@Override
	public boolean canBeUpgraded() {
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
			.syncEnergyValue().sync(PacketCodecs.INTEGER, this::getProgress, this::setProgress)
			.sync(PacketCodecs.INTEGER, this::getMaxProgress, this::setMaxProgress)
			.sync(PacketCodecs.INTEGER, this::getLockedInt, this::setLockedInt)
			.sync(ItemStack.OPTIONAL_PACKET_CODEC, this::getOutputPreview, this::setOutputPreview)
			.addInventory().create(this, syncID);
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

	public ItemStack getOutputPreview() {
		return outputPreview;
	}

	public void setOutputPreview(ItemStack stack) {
		outputPreview = stack;
	}

	static class BalanceTable {
		private Item[] layout = new Item[0];
		private BalanceEntry entry = new BalanceEntry();
		private final Map<BalanceEntry, ArrayList<Integer>> ingredients = new HashMap<>();
		private Iterator<BalanceEntry> iterator = null;
		private boolean empty = true;
		private boolean needsMatch = false;

		public boolean updateLayout(CraftingRecipeInput input) {
			int size = input.size();
			ItemStack[] stacks = new ItemStack[size];
			Item[] items = new Item[size];

			boolean same = size == layout.length;
			for (int i = 0; i < size; i++) {
				stacks[i] = input.getStackInSlot(i);
				items[i] = stacks[i].getItem();
				if (same && layout[i] != items[i]) {
					same = false;
				}
			}

			if (same) {
				return false;
			}

			layout = items;
			ingredients.clear();
			entry.resetMatch();
			for (int i = 0; i < size; i++) {
				if (items[i] != Items.AIR) {
					entry.setItem(items[i]);
					entry.setComponents(stacks[i].getComponents());
					ingredients.computeIfAbsent(entry, this::createSlots).add(i);
				}
			}
			ingredients.values().removeIf(this::checkInvalid);
			empty = ingredients.isEmpty();
			if (!empty) {
				iterator = ingredients.keySet().stream().toList().iterator();
				entry = iterator.next();
				needsMatch = true;
			}

			return true;
		}

		private ArrayList<Integer> createSlots(BalanceEntry _entry) {
			entry = new BalanceEntry();
			return new ArrayList<>(layout.length);
		}

		private boolean checkInvalid(ArrayList<Integer> list) {
			return list.size() == 1;
		}

		public void balance(RebornInventory<AutoCraftingTableBlockEntity> inventory, CraftingRecipeInput input) {
			if (empty) return;
			if (!needsMatch) {
				if (!inventory.hasChanged()) return;
				inventory.resetHasChanged();
				needsMatch = true;
			}
			List<Integer> list = ingredients.get(entry);
			int min = Integer.MAX_VALUE, max = 0, count;
			ItemStack minStack = null, maxStack = null, itemStack;
			for (Integer slot : list) {
				itemStack = input.getStackInSlot(slot);
				count = itemStack.getCount();
				if (min > count) {
					min = count;
					minStack = itemStack;
				}
				if (max < count) {
					max = count;
					maxStack = itemStack;
				}
			}
			if (max > min + 1) {
				assert minStack != null && maxStack != null;
				maxStack.decrement(1);
				minStack.increment(1);
				inventory.resetHasChanged();
				inventory.markDirty();
			} else {
				entry.stopMatch();
			}

			checkState(entryNext());
		}

		private boolean entryNext() {
			if (!iterator.hasNext()) {
				iterator = ingredients.keySet().stream().toList().iterator();
			}
			entry = iterator.next();
			return entry.needsMatch;
		}

		private void checkState(boolean needsMatch) {
			if (!needsMatch) {
				for (int i = ingredients.size() - 1; i > 0; i--) {
					if (entryNext()) return;
				}
				this.needsMatch = false;
				ingredients.keySet().forEach(BalanceEntry::resetMatch);
			}
		}
	}
	static class BalanceEntry {
		public Item item = null;
		public ComponentMap components = null;
		public boolean needsMatch = true;

		public void setItem(Item item) {
			this.item = item;
		}

		public void setComponents(ComponentMap components) {
			this.components = components;
		}

		public void stopMatch() {
			this.needsMatch = false;
		}

		public void resetMatch() {
			this.needsMatch = true;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof BalanceEntry entry) {
				return item == entry.item && Objects.equals(components, entry.components);
			} else if (o instanceof ItemStack stack) {
				return item == stack.getItem() && Objects.equals(components, stack.getComponents());
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(item, components);
		}
	}
}
