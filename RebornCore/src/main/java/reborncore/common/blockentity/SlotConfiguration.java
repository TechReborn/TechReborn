/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.blockentity;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.NbtReadView;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import reborncore.common.util.NBTSerializable;
import reborncore.common.util.RebornInventory;

import java.util.*;
import java.util.stream.Collectors;

import static reborncore.RebornCore.LOGGER;

public class SlotConfiguration implements NBTSerializable {
	public static final PacketCodec<ByteBuf, SlotConfiguration> PACKET_CODEC = SlotConfigHolder.PACKET_CODEC
		.collect(PacketCodecs.toList())
		.xmap(SlotConfiguration::new, SlotConfiguration::getSlotDetails);

	List<SlotConfigHolder> slotDetails = new ArrayList<>();

	@Nullable
	Inventory inventory;

	public SlotConfiguration(RebornInventory<?> inventory) {
		this.inventory = inventory;

		for (int i = 0; i < inventory.size(); i++) {
			updateSlotDetails(new SlotConfigHolder(i));
		}
	}

	private SlotConfiguration(List<SlotConfigHolder> slotDetails) {
		this.slotDetails = slotDetails;
		this.inventory = null;
	}

	public void update(MachineBaseBlockEntity machineBase) {
		if (inventory == null && machineBase.getOptionalInventory().isPresent()) {
			inventory = machineBase.getOptionalInventory().get();
		}
		if (inventory != null && slotDetails.size() != inventory.size()) {
			for (int i = 0; i < inventory.size(); i++) {
				SlotConfigHolder holder = getSlotDetails(i);
				if (holder == null) {
					LOGGER.debug("Fixed slot " + i + " in " + machineBase);
					// hmm, something has gone wrong
					updateSlotDetails(new SlotConfigHolder(i));
				}
			}
		}
		if (!machineBase.getWorld().isClient && machineBase.getWorld().getTime() % machineBase.slotTransferSpeed() == 0) {
			getSlotDetails().forEach(slotConfigHolder -> slotConfigHolder.handleItemIO(machineBase));
		}
	}

	public SlotConfiguration(ReadView view) {
		read(view);
	}

	public List<SlotConfigHolder> getSlotDetails() {
		return slotDetails;
	}

	/**
	 * Replaces or adds a slot detail for the slot id
	 *
	 * @param slotConfigHolder {@link SlotConfigHolder}
	 * @return {@link SlotConfigHolder} Updated SlotConfigHolder
	 */
	public SlotConfigHolder updateSlotDetails(SlotConfigHolder slotConfigHolder) {
		SlotConfigHolder lookup = getSlotDetails(slotConfigHolder.slotID);
		if (lookup != null) {
			slotDetails.remove(lookup);
		}
		slotDetails.add(slotConfigHolder);
		return slotConfigHolder;
	}

	@Nullable
	public SlotConfigHolder getSlotDetails(int id) {
		for (SlotConfigHolder detail : slotDetails) {
			if (detail.slotID == id) {
				return detail;
			}
		}
		return null;
	}

	public List<SlotConfig> getSlotsForSide(Direction facing) {
		return slotDetails.stream().map(slotConfigHolder -> slotConfigHolder.getSideDetail(facing)).collect(Collectors.toList());
	}

	@Override
	public void write(WriteView view) {
		view.putInt("size", slotDetails.size());
		for (int i = 0; i < slotDetails.size(); i++) {
			slotDetails.get(i).write(view.get("slot_" + i));
		}
	}

	@Override
	public void read(ReadView view) {
		int size = view.getInt("size", 0);
		for (int i = 0; i < size; i++) {
			view.getOptionalReadView("slot_" + i).ifPresent(slot -> {
				updateSlotDetails(new SlotConfigHolder(slot));
			});
		}
	}

	public static class SlotConfigHolder implements NBTSerializable {
		public static final PacketCodec<ByteBuf, SlotConfigHolder> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER, SlotConfigHolder::getSlotID,
			PacketCodecs.map(HashMap::new, Direction.PACKET_CODEC, SlotConfig.PACKET_CODEC), SlotConfigHolder::getSideMap,
			PacketCodecs.BOOLEAN, SlotConfigHolder::autoInput,
			PacketCodecs.BOOLEAN, SlotConfigHolder::autoOutput,
			PacketCodecs.BOOLEAN, SlotConfigHolder::filter,
			PacketCodecs.INTEGER, SlotConfigHolder::getPriority,
			SlotConfigHolder::new
		);

		int slotID;
		HashMap<Direction, SlotConfig> sideMap;
		boolean input, output, filter;
		@Nullable
		public Direction first, last;

		private SlotConfigHolder(int slotID, HashMap<Direction, SlotConfig> sideMap, boolean input, boolean output, boolean filter, int priority) {
			this.slotID = slotID;
			this.sideMap = sideMap;
			this.input = input;
			this.output = output;
			this.filter = filter;
			setPriority(priority);
		}

		public SlotConfigHolder(int slotID) {
			this.slotID = slotID;
			sideMap = new HashMap<>();
			Arrays.stream(Direction.values()).forEach(facing -> sideMap.put(facing, new SlotConfig(facing, slotID)));
		}

		public SlotConfigHolder(ReadView view) {
			sideMap = new HashMap<>();
			read(view);
			Validate.isTrue(Arrays.stream(Direction.values())
								.map(enumFacing -> sideMap.get(enumFacing))
								.noneMatch(Objects::isNull),
							"sideMap failed to load from nbt"
			);
		}

		public SlotConfig getSideDetail(Direction side) {
			Validate.notNull(side, "A none null side must be used");
			SlotConfig slotConfig = sideMap.get(side);
			Validate.notNull(slotConfig, "slotConfig was null for side " + side);
			return slotConfig;
		}

		public void updateSlotConfig(SlotConfig config) {
			SlotConfig toEdit = sideMap.get(config.side);
			toEdit.slotIO = config.slotIO;
		}

		private void handleItemIO(MachineBaseBlockEntity machineBase) {
			if (!input && !output) {
				return;
			}
			if (first != null) {
				handleItemSideIo(machineBase, sideMap.get(first));
			}
			sideMap.forEach((key, config) -> {
				if (key == first || key == last) return;
				handleItemSideIo(machineBase, config);
			});
			if (last != null) {
				handleItemSideIo(machineBase, sideMap.get(last));
			}
		}

		private void handleItemSideIo(MachineBaseBlockEntity machineBase, SlotConfig config) {
			switch (config.getSlotIO().getIoConfig()) {
				case INPUT -> {
					if (input) config.handleItemInput(machineBase);
				}
				case OUTPUT -> {
					if (output) config.handleItemOutput(machineBase);
				}
			}
		}

		public boolean autoInput() {
			return input;
		}

		public boolean autoOutput() {
			return output;
		}

		public boolean filter() {
			return filter;
		}

		public int getPriority() {
			int first = this.first == null ? 6 : this.first.ordinal();
			int last = this.last == null ? 6 : this.last.ordinal();
			return first * 10 + last;
		}

		public void setPriority(int priority) {
			int first = priority / 10;
			int last = priority % 10;
			Direction[] directions = Direction.values();
			this.first = first == 6 ? null : directions[first];
			this.last = last == 6 ? null : directions[last];
		}

		public void setInput(boolean input) {
			this.input = input;
		}

		public void setOutput(boolean output) {
			this.output = output;
		}

		public void setFilter(boolean filter) {
			this.filter = filter;
		}

		public int getSlotID() {
			return slotID;
		}

		public HashMap<Direction, SlotConfig> getSideMap() {
			return sideMap;
		}

		@Override
		public void write(WriteView view) {
			view.putInt("slotID", slotID);
			Arrays.stream(Direction.values()).forEach(facing -> sideMap.get(facing).write(view.get("side_" + facing.ordinal())));
			view.putBoolean("input", input);
			view.putBoolean("output", output);
			view.putBoolean("filter", filter);
			if (this.first != null || this.last != null) {
				view.putInt("priority", getPriority());
			}
		}

		@Override
		public void read(ReadView view) {
			sideMap.clear();
			slotID = view.getInt("slotID", 0);
			Arrays.stream(Direction.values()).forEach(facing -> {
				view.getOptionalReadView("side_" + facing.ordinal()).ifPresent(config -> {
					sideMap.put(facing, new SlotConfig(config));
				});
			});
			input = view.getBoolean("input", false);
			output = view.getBoolean("output", false);
			filter = view.getBoolean("filter", false);
			view.getOptionalInt("priority").ifPresentOrElse(this::setPriority, () -> {
				first = null;
				last = null;
			});
		}
	}

	public static class SlotConfig implements NBTSerializable {
		public static final PacketCodec<ByteBuf, SlotConfig> PACKET_CODEC = PacketCodec.tuple(
			Direction.PACKET_CODEC, SlotConfig::getSide,
			SlotIO.PACKET_CODEC, SlotConfig::getSlotIO,
			PacketCodecs.INTEGER, SlotConfig::getSlotID,
			SlotConfig::new
		);

		private Direction side;
		private SlotIO slotIO;
		private int slotID;

		public SlotConfig(Direction side, int slotID) {
			this.side = side;
			this.slotID = slotID;
			this.slotIO = new SlotIO(ExtractConfig.NONE);
		}

		public SlotConfig(Direction side, SlotIO slotIO, int slotID) {
			this.side = side;
			this.slotIO = slotIO;
			this.slotID = slotID;
		}

		public SlotConfig(ReadView view) {
			read(view);
			Validate.notNull(side, "error when loading slot config");
			Validate.notNull(slotIO, "error when loading slot config");
		}

		public Direction getSide() {
			Validate.notNull(side, "side is null");
			return side;
		}

		public SlotIO getSlotIO() {
			Validate.notNull(slotIO, "error when loading slot config");
			return slotIO;
		}

		public int getSlotID() {
			return slotID;
		}

		private void handleItemInput(MachineBaseBlockEntity machineBase) {
			RebornInventory<?> inventory = machineBase.getOptionalInventory().get();
			ItemStack targetStack = inventory.getStack(slotID);
			if (targetStack.getMaxCount() == targetStack.getCount()) {
				return;
			}

			StorageUtil.move(
					ItemStorage.SIDED.find(machineBase.getWorld(), machineBase.getPos().offset(side), side.getOpposite()),
					InventoryStorage.of(machineBase, null).getSlot(slotID),
					iv -> true,
					4, // Move up to 4 per tick.
					null
			);
		}

		private void handleItemOutput(MachineBaseBlockEntity machineBase) {
			RebornInventory<?> inventory = machineBase.getOptionalInventory().get();
			ItemStack sourceStack = inventory.getStack(slotID);
			if (sourceStack.isEmpty()) {
				return;
			}

			StorageUtil.move(
					InventoryStorage.of(machineBase, null).getSlot(slotID),
					ItemStorage.SIDED.find(machineBase.getWorld(), machineBase.getPos().offset(side), side.getOpposite()),
					iv -> true,
					Long.MAX_VALUE,
					null
			);
		}

		@Override
		public void write(WriteView view) {
			view.putInt("side", side.ordinal());
			slotIO.write(view.get("config"));
			view.putInt("slot", slotID);
		}

		@Override
		public void read(ReadView view) {
			side = Direction.values()[view.getInt("side", 0)];
			view.getOptionalReadView("config").ifPresent(config -> {
				slotIO = new SlotIO(config);
			});
			slotID = view.getInt("slot", 0);
		}
	}

	public static class SlotIO implements NBTSerializable {
		public static final PacketCodec<ByteBuf, SlotIO> PACKET_CODEC = PacketCodec.tuple(
			ExtractConfig.PACKET_CODEC, SlotIO::getIoConfig,
			SlotIO::new
		);

		ExtractConfig ioConfig;

		public SlotIO(ReadView view) {
			read(view);
		}

		public SlotIO(ExtractConfig ioConfig) {
			this.ioConfig = ioConfig;
		}

		public ExtractConfig getIoConfig() {
			return ioConfig;
		}

		@Override
		public void write(WriteView view) {
			view.putInt("config", ioConfig.ordinal());
		}

		@Override
		public void read(ReadView view) {
			ioConfig = ExtractConfig.values()[view.getInt("config", 0)];
		}
	}

	public enum ExtractConfig {
		NONE(false, false),
		INPUT(false, true),
		OUTPUT(true, false);

		public static final PacketCodec<ByteBuf, ExtractConfig> PACKET_CODEC = PacketCodecs.INTEGER
			.xmap(integer -> ExtractConfig.values()[integer], Enum::ordinal);

		boolean extract;
		boolean insert;

		ExtractConfig(boolean extract, boolean insert) {
			this.extract = extract;
			this.insert = insert;
		}

		public boolean isExtract() {
			return extract;
		}

		public boolean isInsert() {
			return insert;
		}

		public ExtractConfig getNext() {
			int i = this.ordinal() + 1;
			if (i >= ExtractConfig.values().length) {
				i = 0;
			}
			return ExtractConfig.values()[i];
		}
	}

	public String toJson(String machineIdent, RegistryWrapper.WrapperLookup registryLookup) {
		try (ErrorReporter.Logging logging = new ErrorReporter.Logging(() -> "SlotConfiguration", LOGGER)) {
			NbtWriteView view = NbtWriteView.create(logging, registryLookup);
			write(view.get("data"));
			view.putString("machine", machineIdent);
			return view.getNbt().toString();
		}
	}

	public void readJson(String json, String machineIdent, RegistryWrapper.WrapperLookup registryLookup) throws UnsupportedOperationException {
		NbtCompound compound;
		try {
			compound = StringNbtReader.readCompound(json);
		} catch (CommandSyntaxException e) {
			throw new UnsupportedOperationException("Clipboard contents isn't a valid slot configuration");
		}
		if (!compound.contains("machine") || !compound.getString("machine").orElseThrow().equals(machineIdent)) {
			throw new UnsupportedOperationException("Machine config is not for this machine.");
		}
		try (ErrorReporter.Logging logging = new ErrorReporter.Logging(() -> "SlotConfiguration", LOGGER)) {
			read(NbtReadView.create(logging, registryLookup, compound.getCompoundOrEmpty("data")));
		}
	}

	// DO NOT CALL THIS, use the inventory access on the inventory
	public static boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction, MachineBaseBlockEntity blockEntity) {
		if(itemStackIn.isEmpty()){
			return false;
		}
		SlotConfiguration.SlotConfigHolder slotConfigHolder = blockEntity.getSlotConfiguration().getSlotDetails(index);
		SlotConfiguration.SlotConfig slotConfig = slotConfigHolder.getSideDetail(direction);
		if (slotConfig.getSlotIO().getIoConfig().isInsert()) {
			if (slotConfigHolder.filter()) {
				if(blockEntity instanceof SlotFilter){
					return ((SlotFilter) blockEntity).isStackValid(index, itemStackIn);
				}
			}
			return blockEntity.isValid(index, itemStackIn);
		}
		return false;
	}

	// DO NOT CALL THIS, use the inventory access on the inventory
	public static boolean canExtractItem(int index, ItemStack stack, Direction direction, MachineBaseBlockEntity blockEntity) {
		SlotConfiguration.SlotConfigHolder slotConfigHolder = blockEntity.getSlotConfiguration().getSlotDetails(index);
		SlotConfiguration.SlotConfig slotConfig = slotConfigHolder.getSideDetail(direction);
		if (slotConfig.getSlotIO().getIoConfig().isExtract()) {
			return true;
		}
		return false;
	}

	public interface SlotFilter {
		boolean isStackValid(int slotID, ItemStack stack);

		int[] getInputSlots();
	}

}
