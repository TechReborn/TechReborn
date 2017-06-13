/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.api.tile.IUpgradeable;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;
import reborncore.common.util.ItemNBTHelper;

import java.io.IOException;

/**
 * Created by Mark on 12/04/2017.
 */
public class PacketSyncSideConfig implements INetworkPacket<PacketSyncSideConfig> {

	int slotID;
	int side;
	BlockPos pos;

	public PacketSyncSideConfig(int slotID, int side, BlockPos pos) {
		this.slotID = slotID;
		this.side = side;
		this.pos = pos;
	}

	public PacketSyncSideConfig() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer buffer) throws IOException {
		buffer.writeInt(slotID);
		buffer.writeInt(side);
		buffer.writeBlockPos(pos);
	}

	@Override
	public void readData(ExtendedPacketBuffer buffer) throws IOException {
		slotID = buffer.readInt();
		side = buffer.readInt();
		pos = buffer.readBlockPos();
	}

	@Override
	public void processData(PacketSyncSideConfig message, MessageContext context) {
		TileEntity tileEntity = context.getServerHandler().player.world.getTileEntity(message.pos);
		if (tileEntity instanceof IUpgradeable) {
			ItemStack stack = ((IUpgradeable) tileEntity).getUpgradeInvetory().getStackInSlot(message.slotID);
			ItemNBTHelper.setInt(stack, "side", message.side);
		}
	}
}
