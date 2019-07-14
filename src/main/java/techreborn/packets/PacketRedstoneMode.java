/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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


package techreborn.packets;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;
import techreborn.tiles.storage.TileEnergyStorage;

import java.io.IOException;

/**
 * @author estebes
 */
public class PacketRedstoneMode implements INetworkPacket<PacketRedstoneMode> {
	// Fields >>
	BlockPos machinePos;
	byte mode;
	// << Fields

	public PacketRedstoneMode(TileEnergyStorage machine, byte mode) {
		this.machinePos = machine.getPos();
		this.mode = mode;
	}

	public PacketRedstoneMode() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer buffer) throws IOException {
		buffer.writeBlockPos(machinePos);
		buffer.writeByte(mode);
	}

	@Override
	public void readData(ExtendedPacketBuffer buffer) throws IOException {
		machinePos = buffer.readBlockPos();
		mode = buffer.readByte();
	}

	@Override
	public void processData(PacketRedstoneMode message, MessageContext context) {
		TileEntity tileEntity = context.getServerHandler().player.world.getTileEntity(machinePos);

		if (tileEntity instanceof TileEnergyStorage) ((TileEnergyStorage) tileEntity).redstoneMode = mode;
	}
}

