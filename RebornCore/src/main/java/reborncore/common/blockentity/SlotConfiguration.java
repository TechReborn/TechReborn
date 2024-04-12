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
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import reborncore.RebornCore;
import reborncore.common.util.NBTSerializable;
import reborncore.common.util.RebornInventory;

import java.util.*;
import java.util.stream.Collectors;

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
					RebornCore.LOGGER.debug("Fixed slot " + i + " in " + machineBase);
					// hmm, something has gone wrong
					updateSlotDetails(new SlotConfigHolder(i));
				}
			}
		}
		if (!machineBase.getWorld().isClient && machineBase.getWorld().getTime() % machineBase.slotTransferSpeed() == 0) {
			getSlotDetails().forEach(slotConfigHolder -> slotConfigHolder.handleItemIO(machineBase));
		}
	}

	public SlotConfiguration(NbtCompound tagCompound) {
		read(tagCompound);
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
	public NbtCompound write() {
		NbtCompound tagCompound = new NbtCompound();
		tagCompound.putInt("size", slotDetails.size());
		for (int i = 0; i < slotDetails.size(); i++) {
			tagCompound.put("slot_" + i, slotDetails.get(i).write());
		}
		return tagCompound;
	}

	@Override
	public void read(NbtCompound nbt) {
		int size = nbt.getInt("size");
		for (int i = 0; i < size; i++) {
			NbtCompound tagCompound = nbt.getCompound("slot_" + i);
			SlotConfigHolder slotConfigHolder = new SlotConfigHolder(tagCompound);
			updateSlotDetails(slotConfigHolder);
		}
	}

	public static class SlotConfigHolder implements NBTSerializable {
		public static final PacketCodec<ByteBuf, SlotConfigHolder> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER, SlotConfigHolder::getSlotID,
			PacketCodecs.map(HashMap::new, Direction.PACKET_CODEC, SlotConfig.PACKET_CODEC), SlotConfigHolder::getSideMap,
			PacketCodecs.BOOL, SlotConfigHolder::autoInput,
			PacketCodecs.BOOL, SlotConfigHolder::autoOutput,
			PacketCodecs.BOOL, SlotConfigHolder::filter,
			SlotConfigHolder::new
		);

		int slotID;
		HashMap<Direction, SlotConfig> sideMap;
		boolean input, output, filter;

		private SlotConfigHolder(int slotID, HashMap<Direction, SlotConfig> sideMap, boolean input, boolean output, boolean filter) {
			this.slotID = slotID;
			this.sideMap = sideMap;
			this.input = input;
			this.output = output;
			this.filter = filter;
		}

		public SlotConfigHolder(int slotID) {
			this.slotID = slotID;
			sideMap = new HashMap<>();
			Arrays.stream(Direction.values()).forEach(facing -> sideMap.put(facing, new SlotConfig(facing, slotID)));
		}

		public SlotConfigHolder(NbtCompound tagCompound) {
			sideMap = new HashMap<>();
			read(tagCompound);
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

		public List<SlotConfig> getAllSides() {
			return new ArrayList<>(sideMap.values());
		}

		public void updateSlotConfig(SlotConfig config) {
			SlotConfig toEdit = sideMap.get(config.side);
			toEdit.slotIO = config.slotIO;
		}

		private void handleItemIO(MachineBaseBlockEntity machineBase) {
			if (!input && !output) {
				return;
			}
			getAllSides().stream()
				.filter(config -> config.getSlotIO().getIoConfig() != ExtractConfig.NONE)
				.forEach(config -> {
					if (input && config.getSlotIO().getIoConfig() == ExtractConfig.INPUT) {
						config.handleItemInput(machineBase);
					}
					if (output && config.getSlotIO().getIoConfig() == ExtractConfig.OUTPUT) {
						config.handleItemOutput(machineBase);
					}
				});
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
		public NbtCompound write() {
			NbtCompound compound = new NbtCompound();
			compound.putInt("slotID", slotID);
			Arrays.stream(Direction.values()).forEach(facing -> compound.put("side_" + facing.ordinal(), sideMap.get(facing).write()));
			compound.putBoolean("input", input);
			compound.putBoolean("output", output);
			compound.putBoolean("filter", filter);
			return compound;
		}

		@Override
		public void read(NbtCompound nbt) {
			sideMap.clear();
			slotID = nbt.getInt("slotID");
			Arrays.stream(Direction.values()).forEach(facing -> {
				NbtCompound compound = nbt.getCompound("side_" + facing.ordinal());
				SlotConfig config = new SlotConfig(compound);
				sideMap.put(facing, config);
			});
			input = nbt.getBoolean("input");
			output = nbt.getBoolean("output");
			if (nbt.contains("filter")) { // Was added later, this allows old saves to be upgraded
				filter = nbt.getBoolean("filter");
			}
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

		public SlotConfig(NbtCompound tagCompound) {
			read(tagCompound);
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
		public NbtCompound write() {
			NbtCompound tagCompound = new NbtCompound();
			tagCompound.putInt("side", side.ordinal());
			tagCompound.put("config", slotIO.write());
			tagCompound.putInt("slot", slotID);
			return tagCompound;
		}

		@Override
		public void read(NbtCompound nbt) {
			side = Direction.values()[nbt.getInt("side")];
			slotIO = new SlotIO(nbt.getCompound("config"));
			slotID = nbt.getInt("slot");
		}
	}

	public static class SlotIO implements NBTSerializable {
		public static final PacketCodec<ByteBuf, SlotIO> PACKET_CODEC = PacketCodec.tuple(
			ExtractConfig.PACKET_CODEC, SlotIO::getIoConfig,
			SlotIO::new
		);

		ExtractConfig ioConfig;

		public SlotIO(NbtCompound tagCompound) {
			read(tagCompound);
		}

		public SlotIO(ExtractConfig ioConfig) {
			this.ioConfig = ioConfig;
		}

		public ExtractConfig getIoConfig() {
			return ioConfig;
		}

		@Override
		public NbtCompound write() {
			NbtCompound compound = new NbtCompound();
			compound.putInt("config", ioConfig.ordinal());
			return compound;
		}

		@Override
		public void read(NbtCompound nbt) {
			ioConfig = ExtractConfig.values()[nbt.getInt("config")];
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

	public String toJson(String machineIdent) {
		NbtCompound tagCompound = new NbtCompound();
		tagCompound.put("data", write());
		tagCompound.putString("machine", machineIdent);
		return tagCompound.toString();
	}

	public void readJson(String json, String machineIdent) throws UnsupportedOperationException {
		NbtCompound compound;
		try {
			compound = StringNbtReader.parse(json);
		} catch (CommandSyntaxException e) {
			throw new UnsupportedOperationException("Clipboard contents isn't a valid slot configuration");
		}
		if (!compound.contains("machine") || !compound.getString("machine").equals(machineIdent)) {
			throw new UnsupportedOperationException("Machine config is not for this machine.");
		}
		read(compound.getCompound("data"));
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
