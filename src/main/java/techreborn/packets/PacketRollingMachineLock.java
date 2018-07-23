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
import techreborn.tiles.tier1.TileRollingMachine;

public class PacketRollingMachineLock implements INetworkPacket<PacketRollingMachineLock> {

	BlockPos machinePos;
	boolean locked;

	public PacketRollingMachineLock(TileRollingMachine machine, boolean locked) {
		this.machinePos = machine.getPos();
		this.locked = locked;
	}

	public PacketRollingMachineLock() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer buffer) {
		buffer.writeBlockPos(machinePos);
		buffer.writeBoolean(locked);
	}

	@Override
	public void readData(ExtendedPacketBuffer buffer) {
		machinePos = buffer.readBlockPos();
		locked = buffer.readBoolean();
	}

	@Override
	public void processData(PacketRollingMachineLock message, MessageContext context) {
		TileEntity tileEntity = context.getServerHandler().player.world.getTileEntity(machinePos);
		if(tileEntity instanceof TileRollingMachine){
			((TileRollingMachine) tileEntity).locked = locked;
		}
	}
}
