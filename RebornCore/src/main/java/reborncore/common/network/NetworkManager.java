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

package reborncore.common.network;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public class NetworkManager {


	public static IdentifiedPacket createServerBoundPacket(Identifier identifier, Consumer<ExtendedPacketBuffer> packetBufferConsumer) {
		PacketByteBuf buf = PacketByteBufs.create();
		packetBufferConsumer.accept(new ExtendedPacketBuffer(buf));
		return new IdentifiedPacket(identifier, buf);
	}

	public static <T extends CustomPayload> void registerServerBoundHandler(CustomPayload.Id<T> type, ServerPlayNetworking.PlayPayloadHandler<T> handler) {
		ServerPlayNetworking.registerGlobalReceiver(type, handler);
	}

	public static IdentifiedPacket createClientBoundPacket(Identifier identifier, Consumer<ExtendedPacketBuffer> packetBufferConsumer) {
		PacketByteBuf buf = PacketByteBufs.create();
		packetBufferConsumer.accept(new ExtendedPacketBuffer(buf));
		return new IdentifiedPacket(identifier, buf);
	}

	public static <T> IdentifiedPacket createClientBoundPacket(Identifier identifier, Codec<T> codec, T value) {
		return createClientBoundPacket(identifier, extendedPacketBuffer -> extendedPacketBuffer.encodeAsJson(codec, value));
	}

	public static void sendToAll(CustomPayload payload, MinecraftServer server) {
		send(payload, PlayerLookup.all(server));
	}

	public static void sendToPlayer(CustomPayload payload, ServerPlayerEntity serverPlayerEntity) {
		send(payload, Collections.singletonList(serverPlayerEntity));
	}

	public static void sendToWorld(CustomPayload payload, ServerWorld world) {
		send(payload, PlayerLookup.world(world));
	}


	public static void sendToTracking(CustomPayload payload, BlockEntity blockEntity) {
		send(payload, PlayerLookup.tracking(blockEntity));
	}

	public static void send(CustomPayload payload, Collection<ServerPlayerEntity> players) {
		for (ServerPlayerEntity player : players) {
			ServerPlayNetworking.send(player, payload);
		}
	}

	public static void sendTo(CustomPayload payload, PacketSender sender) {
		Packet<?> s2CPacket = ServerPlayNetworking.createS2CPacket(payload);
		sender.sendPacket(s2CPacket);
	}

}
