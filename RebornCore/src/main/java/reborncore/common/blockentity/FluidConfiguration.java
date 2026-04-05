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

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import reborncore.common.util.NBTSerializable;

import java.util.*;

public class FluidConfiguration implements NBTSerializable {
	private static final StreamCodec<ByteBuf, Map<Direction, FluidConfig>> SIDE_MAP_PACKET_CODEC = ByteBufCodecs.map(
		HashMap::new,
		Direction.STREAM_CODEC,
		FluidConfig.PACKET_CODEC
	);
	public static final StreamCodec<ByteBuf, FluidConfiguration> PACKET_CODEC = StreamCodec.composite(
		SIDE_MAP_PACKET_CODEC, FluidConfiguration::getSideMap,
		ByteBufCodecs.BOOL, FluidConfiguration::autoInput,
		ByteBufCodecs.BOOL, FluidConfiguration::autoOutput,
		FluidConfiguration::new
	);

	Map<Direction, FluidConfig> sideMap;
	boolean input, output;

	public FluidConfiguration() {
		sideMap = new HashMap<>();
		Arrays.stream(Direction.values()).forEach(facing -> sideMap.put(facing, new FluidConfig(facing)));
	}

	public FluidConfiguration(ValueInput view) {
		sideMap = new HashMap<>();
		read(view);
	}

	private FluidConfiguration(Map<Direction, FluidConfig> sideMap, boolean input, boolean output) {
		this.sideMap = sideMap;
		this.input = input;
		this.output = output;
	}

	public FluidConfig getSideDetail(Direction side) {
		if (side == null) {
			return sideMap.get(Direction.NORTH);
		}
		return sideMap.get(side);
	}

	public List<FluidConfig> getAllSides() {
		return new ArrayList<>(sideMap.values());
	}

	public Map<Direction, FluidConfig> getSideMap() {
		return sideMap;
	}

	public void updateFluidConfig(FluidConfig config) {
		FluidConfig toEdit = sideMap.get(config.side);
		toEdit.ioConfig = config.ioConfig;
	}

	public void update(MachineBaseBlockEntity machineBase) {
		if (!input && !output) {
			return;
		}
		if (machineBase.getTank() == null || machineBase.getLevel().getGameTime() % machineBase.slotTransferSpeed() != 0) {
			return;
		}
		for (Direction facing : Direction.values()) {
			FluidConfig fluidConfig = getSideDetail(facing);
			if (fluidConfig == null || !fluidConfig.getIoConfig().isEnabled()) {
				continue;
			}

			@Nullable
			Storage<FluidVariant> tank = getTank(machineBase, facing);
			if (autoInput() && fluidConfig.getIoConfig().isInsert()) {
				StorageUtil.move(tank, machineBase.getTank(), fv -> true, machineBase.fluidTransferAmount().getRawValue(), null);
			}
			if (autoOutput() && fluidConfig.getIoConfig().isExtract()) {
				StorageUtil.move(machineBase.getTank(), tank, fv -> true, machineBase.fluidTransferAmount().getRawValue(), null);
			}
		}
	}

	@Nullable
	private Storage<FluidVariant> getTank(MachineBaseBlockEntity machine, Direction facing) {
		BlockPos pos = machine.getBlockPos().relative(facing);
		return FluidStorage.SIDED.find(machine.getLevel(), pos, facing.getOpposite());
	}

	public boolean autoInput() {
		return input;
	}

	public boolean autoOutput() {
		return output;
	}

	public void setInput(boolean input) {
		this.input = input;
	}

	public void setOutput(boolean output) {
		this.output = output;
	}

	@Override
	public void write(ValueOutput view) {
		Arrays.stream(Direction.values()).forEach(facing -> sideMap.get(facing).write(view.child("side_" + facing.ordinal())));
		view.putBoolean("input", input);
		view.putBoolean("output", output);
	}

	@Override
	public void read(ValueInput view) {
		sideMap.clear();
		Arrays.stream(Direction.values()).forEach(facing -> {
			view.child("side_" + facing.ordinal()).ifPresent(config -> {
				sideMap.put(facing, new FluidConfig(config));
			});
		});
		input = view.getBooleanOr("input", false);
		output = view.getBooleanOr("output", false);
	}

	public static class FluidConfig implements NBTSerializable {
		public static final StreamCodec<ByteBuf, FluidConfig> PACKET_CODEC = StreamCodec.composite(
			Direction.STREAM_CODEC, FluidConfig::getSide,
			ExtractConfig.PACKET_CODEC, FluidConfig::getIoConfig,
			FluidConfig::new
		);

		Direction side;
		FluidConfiguration.ExtractConfig ioConfig;

		public FluidConfig(Direction side) {
			this.side = side;
			this.ioConfig = ExtractConfig.ALL;
		}

		public FluidConfig(Direction side, FluidConfiguration.ExtractConfig ioConfig) {
			this.side = side;
			this.ioConfig = ioConfig;
		}

		public FluidConfig(ValueInput view) {
			read(view);
		}

		public Direction getSide() {
			return side;
		}

		public ExtractConfig getIoConfig() {
			return ioConfig;
		}

		@Override
		public void write(ValueOutput view) {
			view.putInt("side", side.ordinal());
			view.putInt("config", ioConfig.ordinal());
		}

		@Override
		public void read(ValueInput view) {
			side = Direction.values()[view.getIntOr("side", 0)];
			ioConfig = FluidConfiguration.ExtractConfig.values()[view.getIntOr("config", 0)];
		}
	}

	public enum ExtractConfig {
		NONE(false, false),
		INPUT(false, true),
		OUTPUT(true, false),
		ALL(true, true);

		public static final StreamCodec<ByteBuf, ExtractConfig> PACKET_CODEC = ByteBufCodecs.INT
			.map(integer -> ExtractConfig.values()[integer], Enum::ordinal);

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

		public boolean isEnabled() {
			return extract || insert;
		}

		public FluidConfiguration.ExtractConfig getNext() {
			int i = this.ordinal() + 1;
			if (i >= FluidConfiguration.ExtractConfig.values().length) {
				i = 0;
			}
			return FluidConfiguration.ExtractConfig.values()[i];
		}
	}
}
