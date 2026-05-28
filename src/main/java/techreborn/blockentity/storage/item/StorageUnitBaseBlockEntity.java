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

package techreborn.blockentity.storage.item;

import it.unimi.dsi.fastutil.ints.IntObjectPair;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedSlottedStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.WorldUtils;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.List;

import static techreborn.TechReborn.LOGGER;

public class StorageUnitBaseBlockEntity extends MachineBaseBlockEntity implements InventoryProvider, IToolDrop, IListInfoProvider, BuiltScreenHandlerProvider {

	// Inventory constants
	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;

	// Client sync variables for GUI, what and how much stored
	public int storedAmount = 0;

	protected final RebornInventory<StorageUnitBaseBlockEntity> inventory;
	private int maxCapacity;
	private int serverCapacity = -1;

	private ItemStack storeItemStack;
	// Fabric transfer API support for the internal stack (one per direction);
	private final SingleStackStorage[] internalStoreStorage = new SingleStackStorage[6];

	private TRContent.StorageUnit type;

	// A locked storage unit will continue behaving as if it contains
	// the locked-in item, even if the stored amount drops to zero.
	private ItemStack lockedItemStack = ItemStack.EMPTY;

	public StorageUnitBaseBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.STORAGE_UNIT, pos, state);
		inventory = new RebornInventory<>(2, "ItemInventory", 64, this);
	}

	public StorageUnitBaseBlockEntity(BlockPos pos, BlockState state, TRContent.StorageUnit type) {
		super(TRBlockEntities.STORAGE_UNIT, pos, state);
		inventory = new RebornInventory<>(2, "ItemInventory", 64, this);
		configureEntity(type);
	}
	private void configureEntity(TRContent.StorageUnit type) {
		// Set capacity to local config unless overridden by server
		if(serverCapacity == -1){
			this.maxCapacity = type.capacity;
		}
		storeItemStack = ItemStack.EMPTY;
		this.type = type;
	}

	public boolean isLocked() {
		return lockedItemStack != ItemStack.EMPTY;
	}

	public void setLocked(boolean value) {
		if (isLocked() == value) {
			return;
		}

		// Only set lockedItem in response to user input
		ItemStack stack = getStoredStack().copy();
		stack.setCount(1);
		lockedItemStack = value ? stack : ItemStack.EMPTY;
		syncWithAll();
	}

	public boolean canModifyLocking() {
		// Can always be unlocked
		if (isLocked()) {
			return true;
		}

		// Can only lock if there is an item to lock
		return !isEmpty();
	}

	private void populateOutput() {
		// Set to storeItemStack to get the stack type
		ItemStack output = storeItemStack.copy();

		int outputSlotCount = inventory.getItem(OUTPUT_SLOT).getCount();

		// Set to current outputSlot count
		output.setCount(outputSlotCount);

		// Calculate amount needed to fill stack in output slot
		int amountToFill = getStoredStack().getMaxStackSize() - outputSlotCount;

		if (storeItemStack.getCount() >= amountToFill) {
			storeItemStack.shrink(amountToFill);

			if (storeItemStack.isEmpty()) {
				storeItemStack = ItemStack.EMPTY;
			}

			output.grow(amountToFill);
		} else {
			output.grow(storeItemStack.getCount());
			storeItemStack = ItemStack.EMPTY;
		}

		inventory.setItem(OUTPUT_SLOT, output);
	}

	private void addStoredItemCount(int amount) {
		storeItemStack.grow(amount);
	}

	public ItemStack getStoredStack() {
		return storeItemStack.isEmpty() ? inventory.getItem(OUTPUT_SLOT) : storeItemStack;
	}

	// Returns the ItemStack to be displayed to the player via UI / model
	public ItemStack getDisplayedStack() {
		if (!isLocked()) {
			return getStoredStack();
		} else {
			// Render the locked stack even if the unit is empty
			return lockedItemStack;
		}
	}

	public ItemStack getAll() {
		ItemStack returnStack = ItemStack.EMPTY;

		if (!isEmpty()) {
			returnStack = getStoredStack().copy();
			returnStack.setCount(getCurrentCapacity());
		}

		return returnStack;
	}

	public ItemStack processInput(ItemStack inputStack) {
		if (!canPlaceItem(INPUT_SLOT, inputStack)){
			return inputStack;
		}

		// Amount of items that can be added before reaching capacity
		int reminder = maxCapacity - getCurrentCapacity();
		NonNullList<ItemStack> optionalShulkerStack = ItemUtils.getBlockEntityStacks(inputStack);
		if (isLocked() && ItemUtils.canExtractFromCachedShulker(optionalShulkerStack, lockedItemStack) > 0 ) {
			IntObjectPair<ItemStack> pair = ItemUtils.extractFromShulker(inputStack, optionalShulkerStack, lockedItemStack, reminder);
			if (pair.leftInt() != 0) {
				int amount = pair.leftInt();
				if (storeItemStack.isEmpty()) {
					storeItemStack = lockedItemStack.copy();
					amount = amount -1;
				}
				addStoredItemCount(amount);
				inputStack = pair.right().copy();
				inventory.setHasChanged();
			}
			return inputStack;
		}
		if (inputStack.getCount() <= reminder) {
			// Add full stack
			if (storeItemStack == ItemStack.EMPTY){
				// copy input stack into stored if everything is in OUTPUT_SLOT
				storeItemStack = inputStack.copy();
			}
			else {
				addStoredItemCount(inputStack.getCount());
			}

			inputStack = ItemStack.EMPTY;
		} else {
			// Add only what is needed to reach max capacity
			if (storeItemStack == ItemStack.EMPTY) {
				storeItemStack = inputStack.copy();
				storeItemStack.setCount(reminder);
			} else {
				addStoredItemCount(reminder);
			}
			inputStack.shrink(reminder);
		}

		inventory.setHasChanged();
		return inputStack;
	}

	// Creative function
	private void fillToCapacity() {
		storeItemStack = getStoredStack();
		storeItemStack.setCount(maxCapacity);

		inventory.setItem(OUTPUT_SLOT, ItemStack.EMPTY);
	}

	public boolean isFull() {
		return getCurrentCapacity() == maxCapacity;
	}

	public int getCurrentCapacity() {
		return storeItemStack.getCount() + inventory.getItem(OUTPUT_SLOT).getCount();
	}

	// MachineBaseBlockEntity
	@Override
	public void tick(Level level, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(level, pos, state, blockEntity);
		if (!(level instanceof ServerLevel)) {
			return;
		}
		// If there is an item in the input AND stored is less than max capacity
		if (!inventory.getItem(INPUT_SLOT).isEmpty() && !isFull()) {
			inventory.setItem(INPUT_SLOT, processInput(inventory.getItem(INPUT_SLOT)));
		}

		// Fill output slot with goodies when stored has items and output count is less than max stack size
		if (storeItemStack.getCount() > 0 && inventory.getItem(OUTPUT_SLOT).getCount() < getStoredStack().getMaxStackSize()) {
			populateOutput();
		}

		if (type == TRContent.StorageUnit.CREATIVE) {
			if (!isFull() && !isEmpty()) {
				fillToCapacity();
			}
			// void input items for creative storage (#2205)
			if (!inventory.getItem(INPUT_SLOT).isEmpty()){
				inventory.setItem(INPUT_SLOT, ItemStack.EMPTY);
			}
		}

		if (inventory.hasChanged()) {
			syncWithAll();
			inventory.resetHasChanged();
		}
	}

	@Override
	public boolean isEmpty() {
		return getCurrentCapacity() == 0;
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
		return super.canPlaceItemThroughFace(index, stack, direction) && canPlaceItem(INPUT_SLOT, stack);
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);

		view.getString("unitType").ifPresentOrElse(name -> {
			this.type = TRContent.StorageUnit.valueOf(name);
			configureEntity(type);
		}, () -> {
			this.type = TRContent.StorageUnit.QUANTUM;
		});

		storeItemStack = ItemStack.EMPTY;

		view.read("storedStack", ItemStack.CODEC).ifPresent(stack -> {
			storeItemStack = stack;
		});

		if (!storeItemStack.isEmpty()) {
			storeItemStack.setCount(Math.min(view.getIntOr("storedQuantity", 0), this.maxCapacity));
		}

		// Renderer only
		storedAmount = view.getIntOr("totalStoredAmount", 0);

		view.read("lockedItem", ItemStack.CODEC).ifPresent(stack -> {
			lockedItemStack = stack;
		});
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);

		view.putString("unitType", this.type.name());

		if (!storeItemStack.isEmpty()) {
			ItemStack temp = storeItemStack.copy();
			if (storeItemStack.getCount() > storeItemStack.getMaxStackSize()) {
				temp.setCount(storeItemStack.getMaxStackSize());
			}
			view.store("storedStack", ItemStack.CODEC, temp);
			view.putInt("storedQuantity", Math.min(storeItemStack.getCount(), maxCapacity));
		} else {
			view.putInt("storedQuantity", 0);
		}

		// Renderer only
		view.putInt("totalStoredAmount", getCurrentCapacity());

		if (isLocked()) {
			view.store("lockedItem", ItemStack.CODEC, lockedItemStack);
		}
	}

	@Override
	public void onBreak(Level world, Player playerEntity, BlockPos blockPos, BlockState blockState) {
		super.onBreak(world, playerEntity, blockPos, blockState);

		// No need to drop anything for creative peeps
		if (type == TRContent.StorageUnit.CREATIVE) {
			this.inventory.clearContent();
			return;
		}

		if (storeItemStack != ItemStack.EMPTY) {
			if (storeItemStack.getMaxStackSize() == 64) {
				// Drop stacks (In one clump, reduce lag)
				WorldUtils.dropItem(storeItemStack, world, worldPosition);
			} else {
				int size = storeItemStack.getMaxStackSize();

				for (int i = 0; i < storeItemStack.getCount() / size; i++) {
					ItemStack toDrop = storeItemStack.copy();
					toDrop.setCount(size);
					WorldUtils.dropItem(toDrop, world, worldPosition);
				}

				if (storeItemStack.getCount() % size != 0) {
					ItemStack toDrop = storeItemStack.copy();
					toDrop.setCount(storeItemStack.getCount() % size);
					WorldUtils.dropItem(toDrop, world, worldPosition);
				}

			}
		}

		// Inventory gets dropped automatically
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack inputStack) {
		if (slot != INPUT_SLOT) {
			return false;
		}
		if (inputStack == ItemStack.EMPTY) {
			return false;
		}
		// Do not allow player heads into storage due to lag. Fix #2888
		if (inputStack.getItem() instanceof PlayerHeadItem) {
			return false;
		}
		// do not allow other storage units to avoid NBT overflow. Fix #2580
		if (inputStack.is(TRContent.ItemTags.STORAGE_UNITS)) {
			return false;
		}

		if (isLocked()) {
			//allow shulker bundle extraction when locked
			if (ItemUtils.canExtractAnyFromShulker(inputStack, lockedItemStack)) {
				return true;
			}
			return ItemUtils.isItemEqual(lockedItemStack, inputStack, true, true);
		}

		if (isEmpty()){
			return true;
		}

		return ItemUtils.isItemEqual(getStoredStack(), inputStack, true, true);
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// InventoryProvider
	@Override
	public RebornInventory<StorageUnitBaseBlockEntity> getInventory() {
		return inventory;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(Player entityPlayer) {
		ItemStack dropStack = new ItemStack(getBlockType(), 1);
		if (level != null){
			try (ProblemReporter.ScopedCollector logging = new ProblemReporter.ScopedCollector(problemPath(), LOGGER)) {
				TagValueOutput view = TagValueOutput.createWithContext(logging, level.registryAccess());
				saveAdditional(view);
				dropStack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(getType(), view.buildResult()));
			}
		}

		return dropStack;
	}

	// IListInfoProvider
	@Override
	public void addInfo(final List<Component> info, final boolean isReal, boolean hasData) {
		if (isReal || hasData) {
			if (!this.isEmpty()) {
				info.add(
						Component.literal(String.valueOf(this.getCurrentCapacity()))
								.append(Component.translatable("techreborn.tooltip.unit.divider"))
								.append(this.getStoredStack().getHoverName())
				);
			} else {
				info.add(Component.translatable("techreborn.tooltip.unit.empty"));
			}
		}

		info.add(
				Component.translatable("techreborn.tooltip.unit.capacity")
						.withStyle(ChatFormatting.GRAY)
						.append(
								Component.literal(String.valueOf(this.getMaxCapacity()))
										.withStyle(ChatFormatting.GOLD)
										.append(" ")
										.append(Component.translatable("techreborn.tooltip.unit.items"))
										.append(" (")
										.append(String.valueOf(this.getMaxCapacity() / 64))
										.append(" ")
										.append(Component.translatable("techreborn.tooltip.unit.stacks"))
										.append(")")
						)
		);
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final Player playerEntity) {
		return new ScreenHandlerBuilder("chest").player(playerEntity.getInventory()).inventory().hotbar().addInventory()
				.blockEntity(this)
				.slot(INPUT_SLOT, 100, 53)
				.outputSlot(OUTPUT_SLOT, 140, 53)
				.sync(ByteBufCodecs.INT, this::isLockedInt, this::setLockedInt)
				.sync(ByteBufCodecs.COMPOUND_TAG, this::getStoredStackNBT, this::setStoredStackFromNBT)
				.sync(ByteBufCodecs.INT, this::getStoredAmount, this::setStoredAmount)
				.sync(ByteBufCodecs.INT, this::getMaxCapacity, this::setMaxCapacity)
				.addInventory().create(this, syncID);

		// Note that inventory is synced, and it gets the stack from that
	}

	// The int methods are only for ContainerBuilder.sync()
	private int isLockedInt() {
		return isLocked() ? 1 : 0;
	}

	private void setLockedInt(int lockedInt) {
		setLocked(lockedInt == 1);
	}

	public int getStoredAmount() {
		return this.getCurrentCapacity();
	}

	public void setStoredAmount(int storedAmount) {
		this.storedAmount = storedAmount;
	}

	// Sync between server/client if configs are mis-matched.
	public int getMaxCapacity() {
		return this.maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
		this.serverCapacity = maxCapacity;
	}

	public CompoundTag getStoredStackNBT() {
		CompoundTag tag = new CompoundTag();
		RegistryOps<Tag> ops = level.registryAccess().createSerializationContext(NbtOps.INSTANCE);
		ItemStack stack = getStoredStack();

		tag.putInt("count", stack.getCount());

		if (!stack.isEmpty()) {
			// We are not allowed to serialize empty or large stacks
			ItemStack singleStack = stack.copy();
			singleStack.setCount(1);
			tag.store("item", ItemStack.CODEC, ops, singleStack);
		}

		return tag;
	}

	public void setStoredStackFromNBT(CompoundTag tag) {
		if (!tag.contains("item")) {
			storeItemStack = ItemStack.EMPTY;
		} else {
			RegistryOps<Tag> ops = level.registryAccess().createSerializationContext(NbtOps.INSTANCE);
			storeItemStack = tag.read("item", ItemStack.CODEC, ops).orElse(ItemStack.EMPTY);
		}

		storeItemStack.setCount(tag.getInt("count").orElse(0));
	}

	private SlottedStorage<ItemVariant> getInternalStoreStorage(@Nullable Direction direction) {
		// Quick fix to handle null sides. https://github.com/TechReborn/TechReborn/issues/3175
		final Direction side = direction != null ? direction : Direction.DOWN;

		if (internalStoreStorage[side.get3DDataValue()] == null) {
			internalStoreStorage[side.get3DDataValue()] = new SingleStackStorage() {
				@Override
				protected ItemStack getStack() {
					return storeItemStack;
				}

				@Override
				protected void setStack(ItemStack stack) {
					if (stack.isEmpty()) {
						// Ensure we maintain reference equality to EMPTY
						storeItemStack = ItemStack.EMPTY;
					} else {
						storeItemStack = stack;
					}
				}

				@Override
				protected int getCapacity(ItemVariant itemVariant) {
					// subtract capacity of output slot (super capacity is the default capacity)
					return maxCapacity - super.getCapacity(itemVariant);
				}

				@Override
				protected boolean canInsert(ItemVariant itemVariant) {
					// Check insertion with the same rules as the input slot
					return StorageUnitBaseBlockEntity.this.canPlaceItemThroughFace(INPUT_SLOT, itemVariant.toStack(), side);
				}

				@Override
				protected boolean canExtract(ItemVariant itemVariant) {
					// Check extraction with the same rules as the output slot
					return StorageUnitBaseBlockEntity.this.canTakeItemThroughFace(OUTPUT_SLOT, itemVariant.toStack(), side);
				}

				@Override
				protected void onFinalCommit() {
					inventory.setHasChanged();
				}
			};
		}
		return internalStoreStorage[side.get3DDataValue()];
	}

	public Storage<ItemVariant> getExposedStorage(Direction side) {
		return new CombinedSlottedStorage<>(List.of(
				getInternalStoreStorage(side),
				ContainerStorage.of(this, side)
		));
	}
}
