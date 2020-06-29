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

package techreborn.packets;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.NetworkManager;
import techreborn.TechReborn;
import techreborn.blockentity.machine.multiblock.MiningRigBlockEntity;

import java.util.function.BiConsumer;

public class ClientboundPackets {

	public static final Identifier MINING_RIG_SYNC = new Identifier(TechReborn.MOD_ID, "mining_rig_sync");

	public static void init() {
		registerPacketHandler(MINING_RIG_SYNC, (extendedPacketBuffer, context) -> {
			BlockPos machinePos = extendedPacketBuffer.readBlockPos();
			boolean activeMining = extendedPacketBuffer.readBoolean();

			context.getTaskQueue().execute(() -> {
				BlockEntity BlockEntity = context.getPlayer().world.getBlockEntity(machinePos);
				if (BlockEntity instanceof MiningRigBlockEntity) {
					((MiningRigBlockEntity) BlockEntity).activeMining = activeMining;
				}
			});
		});
	}

	private static void registerPacketHandler(Identifier identifier, BiConsumer<ExtendedPacketBuffer, PacketContext> consumer){
		ClientSidePacketRegistry.INSTANCE.register(identifier, (packetContext, packetByteBuf) -> consumer.accept(new ExtendedPacketBuffer(packetByteBuf), packetContext));
	}

	public static Packet<ClientPlayPacketListener> createPacketMiningRigSync(boolean active, MiningRigBlockEntity blockEntity) {
		return NetworkManager.createClientBoundPacket(MINING_RIG_SYNC, extendedPacketBuffer -> {
			extendedPacketBuffer.writeBlockPos(blockEntity.getPos());
			extendedPacketBuffer.writeBoolean(active);
		});
	}


}
