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

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reborncore.common.util.NBTSerializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FluidConfiguration implements NBTSerializable {

	HashMap<Direction, FluidConfig> sideMap;
	boolean input, output;

	public FluidConfiguration() {
		sideMap = new HashMap<>();
		Arrays.stream(Direction.values()).forEach(facing -> sideMap.put(facing, new FluidConfig(facing)));
	}

	public FluidConfiguration(NbtCompound tagCompound) {
		sideMap = new HashMap<>();
		read(tagCompound);
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

	public void updateFluidConfig(FluidConfig config) {
		FluidConfig toEdit = sideMap.get(config.side);
		toEdit.ioConfig = config.ioConfig;
	}

	public void update(MachineBaseBlockEntity machineBase) {
		if (!input && !output) {
			return;
		}
		if (machineBase.getTank() == null || machineBase.getWorld().getTime() % machineBase.slotTransferSpeed() != 0) {
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
		BlockPos pos = machine.getPos().offset(facing);
		return FluidStorage.SIDED.find(machine.getWorld(), pos, facing.getOpposite());
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

	@NotNull
	@Override
	public NbtCompound write() {
		NbtCompound compound = new NbtCompound();
		Arrays.stream(Direction.values()).forEach(facing -> compound.put("side_" + facing.ordinal(), sideMap.get(facing).write()));
		compound.putBoolean("input", input);
		compound.putBoolean("output", output);
		return compound;
	}

	@Override
	public void read(@NotNull NbtCompound nbt) {
		sideMap.clear();
		Arrays.stream(Direction.values()).forEach(facing -> {
			NbtCompound compound = nbt.getCompound("side_" + facing.ordinal());
			FluidConfig config = new FluidConfig(compound);
			sideMap.put(facing, config);
		});
		input = nbt.getBoolean("input");
		output = nbt.getBoolean("output");
	}

	public static class FluidConfig implements NBTSerializable {
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

		public FluidConfig(NbtCompound tagCompound) {
			read(tagCompound);
		}

		public Direction getSide() {
			return side;
		}

		public ExtractConfig getIoConfig() {
			return ioConfig;
		}

		@NotNull
		@Override
		public NbtCompound write() {
			NbtCompound tagCompound = new NbtCompound();
			tagCompound.putInt("side", side.ordinal());
			tagCompound.putInt("config", ioConfig.ordinal());
			return tagCompound;
		}

		@Override
		public void read(@NotNull NbtCompound nbt) {
			side = Direction.values()[nbt.getInt("side")];
			ioConfig = FluidConfiguration.ExtractConfig.values()[nbt.getInt("config")];
		}
	}

	public enum ExtractConfig {
		NONE(false, false),
		INPUT(false, true),
		OUTPUT(true, false),
		ALL(true, true);

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
