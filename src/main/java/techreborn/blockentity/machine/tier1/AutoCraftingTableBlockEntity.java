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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
	public static final int RECIPE_TIME = 120;
	public static final int EU_TICK = 10;

	public final RebornInventory<AutoCraftingTableBlockEntity> inventory;
	private final BalanceTable balanceTable = new BalanceTable();
	private final int OUTPUT_SLOT = CRAFTING_AREA; // first slot is indexed by 0, so this is the last non crafting slot
	private final int EXTRA_OUTPUT_SLOT = CRAFTING_AREA + 1;

	public int progress = 0;
	public int maxProgress = RECIPE_TIME;
	public long euTick = EU_TICK;
	public long lastSoundTime = 0;

	TransientCraftingContainer inventoryCrafting;
	CraftingRecipe lastRecipe = null;
	ItemStack outputPreview = ItemStack.EMPTY;

	public boolean locked = false;

	public AutoCraftingTableBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.AUTO_CRAFTING_TABLE, pos, state);
		inventoryCrafting = new TransientCraftingContainer(new AbstractContainerMenu(null, -1) {
			@Override
			public ItemStack quickMoveStack(Player player, int index) {
				return ItemStack.EMPTY;
			}

			@Override
			public boolean stillValid(Player playerIn) {
				return false;
			}
		}, CRAFTING_WIDTH, CRAFTING_HEIGHT);
		inventory = new RebornInventory<>(CRAFTING_AREA + 2, "AutoCraftingTableBlockEntity", 64, this) {
			private void syncStack(int slot, ItemStack stack) {
				if (slot < CRAFTING_AREA) {
					inventoryCrafting.setItem(slot, stack);
				}
			}

			@Override
			public void readData(ValueInput view) {
				super.readData(view);
				for (int i = 0; i < CRAFTING_AREA; i++) {
					inventoryCrafting.setItem(i, inventory.getItem(i));
				}
			}

			@Override
			public void setItem(int slot, @NotNull ItemStack stack) {
				super.setItem(slot, stack);
				syncStack(slot, stack);
			}

			@Override
			public ItemStack removeItemNoUpdate(int i) {
				syncStack(i, ItemStack.EMPTY);
				return super.removeItemNoUpdate(i);
			}

			@Override
			public ItemStack removeItem(int i, int i1) {
				ItemStack stack = super.removeItem(i, i1);
				if (this.getItem(i).isEmpty()) {
					syncStack(i, ItemStack.EMPTY);
				}
				return stack;
			}

			@Override
			public ItemStack shrinkSlot(int slot, int count) {
				ItemStack stack = super.shrinkSlot(slot, count);
				if (this.getItem(slot).isEmpty()) {
					syncStack(slot, ItemStack.EMPTY);
				}
				return stack;
			}
		};
	}

	public boolean updateCurrentRecipe(ServerLevel world, CraftingInput input) {
		if (lastRecipe != null && lastRecipe.matches(input, world)) {
			if (outputPreview == ItemStack.EMPTY) {
				balanceTable.updateLayout(input);
				outputPreview = lastRecipe.assemble(input, world.registryAccess());
			} else if (lastRecipe instanceof CustomRecipe && balanceTable.updateLayout(input)) {
				outputPreview = lastRecipe.assemble(input, world.registryAccess());
			}
			return true;
		}

		if (balanceTable.updateLayout(input)) {
			Optional<CraftingRecipe> testRecipe = world.recipeAccess()
				.getRecipeFor(RecipeType.CRAFTING, input, world).map(RecipeHolder::value);
			if (testRecipe.isPresent()) {
				lastRecipe = testRecipe.get();
				outputPreview = lastRecipe.assemble(input, world.registryAccess());
				return true;
			} else {
				outputPreview = ItemStack.EMPTY;
			}
		}
		return false;
	}

	@Nullable
	private ItemStack getRecipeReminder(CraftingInput input) {
		NonNullList<ItemStack> remainingStacks = lastRecipe.getRemainingItems(input);
		ItemStack reminderStack, recipeReminder = ItemStack.EMPTY;
		for (int slot = 0, size = remainingStacks.size(); slot < size; slot++) {
			reminderStack = remainingStacks.get(slot);
			if (!reminderStack.isEmpty()) {
				recipeReminder = reminderStack.copy();
				for (slot = slot + 1; slot < size; slot++) {
					reminderStack = remainingStacks.get(slot);
					if (!reminderStack.isEmpty()) {
						if (ItemStack.isSameItemSameComponents(recipeReminder, reminderStack)) {
							recipeReminder.grow(reminderStack.getCount());
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

	private void make(CraftingInput.Positioned positioned, ItemStack resultStack, ItemStack remainderStack) {
		CraftingInput input = positioned.input();
		int width = input.width();
		int max = (positioned.top() + input.height()) * CRAFTING_WIDTH;
		int space = CRAFTING_WIDTH - width;
		for (int slot = positioned.top() * CRAFTING_WIDTH + positioned.left(); slot < max; slot += space) {
			for (int end = slot + width; slot < end; slot++) {
				if (!this.inventory.getItem(slot).isEmpty()) {
					this.inventory.shrinkSlot(slot, 1);
				}
			}
		}
		moveOutput(resultStack, OUTPUT_SLOT);
		if (!remainderStack.isEmpty()) {
			moveOutput(remainderStack, EXTRA_OUTPUT_SLOT);
		}
		inventory.resetHasChanged();
	}

	private boolean hasOutputSpace(ItemStack output, int slot) {
		ItemStack stack = inventory.getItem(slot);
		if (stack.isEmpty()) {
			return true;
		}
		if (ItemUtils.isItemEqual(stack, output, true, true)) {
			return stack.getMaxStackSize() >= stack.getCount() + output.getCount();
		}
		return false;
	}

	private void moveOutput(ItemStack stack, int slot) {
		ItemStack currentOutput = inventory.getItem(slot);
		if (currentOutput.isEmpty()) {
			inventory.setItem(slot, stack.copy());
		} else {
			currentOutput.grow(stack.getCount());
		}
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClientSide || getStored() < euTick) {
			return;
		}
		if (inventoryCrafting.isEmpty()) {
			progress = 0;
			outputPreview = ItemStack.EMPTY;
			return;
		}
		CraftingInput.Positioned positioned = inventoryCrafting.asPositionedCraftInput();
		CraftingInput input = positioned.input();
		if (!updateCurrentRecipe((ServerLevel) world, input)) {
			progress = 0;
			return;
		}
		if (!hasOutputSpace(outputPreview, OUTPUT_SLOT)) {
			return;
		}

		balanceTable.balance(inventory, input);

		// Don't allow recipe to change (Keep at least one of each slot stocked, assuming it's actually a recipe)
		if (locked) {
			for (ItemStack stack : input.items()) {
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
			make(positioned, outputPreview, recipeReminder);
			if (inventoryCrafting.isEmpty()) {
				outputPreview = ItemStack.EMPTY;
			}
		} else {
			if (progress == 0) {
				maxProgress = Math.max((int) (RECIPE_TIME * (1.0 - getSpeedMultiplier())), 1);
				euTick = getEuPerTick(EU_TICK);
				if (getStored() < euTick) {
					return;
				}
			}
			progress++;
			if (!isMuffled()) {
				long time = world.getGameTime();
				if (time - lastSoundTime > RECIPE_TIME) {
					lastSoundTime = time;
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.AUTO_CRAFTING,
						SoundSource.BLOCKS, 0.3F, 0.8F);
				}
			}
			useEnergy(euTick);
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
	public void saveAdditional(ValueOutput view) {
		view.putBoolean("locked", locked);
		super.saveAdditional(view);
	}

	@Override
	public void loadAdditional(ValueInput view) {
		locked = view.getBooleanOr("locked", false);
		super.loadAdditional(view);
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(Player playerIn) {
		return TRContent.Machine.AUTO_CRAFTING_TABLE.getStack();
	}

	// InventoryProvider
	@Override
	public RebornInventory<AutoCraftingTableBlockEntity> getInventory() {
		return inventory;
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
		return new ScreenHandlerBuilder("autocraftingtable").player(player.getInventory()).inventory().hotbar().addInventory()
			.blockEntity(this)
			.slot(0, 28, 25).slot(1, 46, 25).slot(2, 64, 25)
			.slot(3, 28, 43).slot(4, 46, 43).slot(5, 64, 43)
			.slot(6, 28, 61).slot(7, 46, 61).slot(8, 64, 61)
			.outputSlot(OUTPUT_SLOT, 145, 42)
			.outputSlot(EXTRA_OUTPUT_SLOT, 145, 70)
			.syncEnergyValue().sync(ByteBufCodecs.INT, this::getProgress, this::setProgress)
			.sync(ByteBufCodecs.INT, this::getMaxProgress, this::setMaxProgress)
			.sync(ByteBufCodecs.INT, this::getLockedInt, this::setLockedInt)
			.sync(ItemStack.OPTIONAL_STREAM_CODEC, this::getOutputPreview, this::setOutputPreview)
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
		private java.util.Iterator<BalanceEntry> iterator = null;
		private boolean empty = true;
		private boolean needsMatch = false;

		public boolean updateLayout(CraftingInput input) {
			int size = input.size();
			ItemStack[] stacks = new ItemStack[size];
			Item[] items = new Item[size];

			boolean same = size == layout.length;
			for (int i = 0; i < size; i++) {
				stacks[i] = input.getItem(i);
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

		public void balance(RebornInventory<AutoCraftingTableBlockEntity> inventory, CraftingInput input) {
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
				itemStack = input.getItem(slot);
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
				maxStack.shrink(1);
				minStack.grow(1);
				inventory.resetHasChanged();
				inventory.setChanged();
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
		public DataComponentMap components = null;
		public boolean needsMatch = true;

		public void setItem(Item item) {
			this.item = item;
		}

		public void setComponents(DataComponentMap components) {
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
