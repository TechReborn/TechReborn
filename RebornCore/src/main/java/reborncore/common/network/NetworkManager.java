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

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
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

	public static void registerServerBoundHandler(Identifier identifier, ServerPlayNetworking.PlayChannelHandler handler) {
		ServerPlayNetworking.registerGlobalReceiver(identifier, handler);
	}

	public static IdentifiedPacket createClientBoundPacket(Identifier identifier, Consumer<ExtendedPacketBuffer> packetBufferConsumer) {
		PacketByteBuf buf = PacketByteBufs.create();
		packetBufferConsumer.accept(new ExtendedPacketBuffer(buf));
		return new IdentifiedPacket(identifier, buf);
	}

	public static void registerClientBoundHandler(Identifier identifier, ClientPlayNetworking.PlayChannelHandler handler) {
		ClientPlayNetworking.registerGlobalReceiver(identifier, handler);
	}


	public static void sendToServer(IdentifiedPacket packet) {
		ClientPlayNetworking.send(packet.getChannel(), packet.getPacketByteBuf());
	}

	public static void sendToAll(IdentifiedPacket packet, MinecraftServer server) {
		send(packet, PlayerLookup.all(server));
	}

	public static void sendToPlayer(IdentifiedPacket packet, ServerPlayerEntity serverPlayerEntity) {
		send(packet, Collections.singletonList(serverPlayerEntity));
	}

	public static void sendToWorld(IdentifiedPacket packet, ServerWorld world) {
		send(packet, PlayerLookup.world(world));
	}


	public static void sendToTracking(IdentifiedPacket packet, BlockEntity blockEntity) {
		send(packet, PlayerLookup.tracking(blockEntity));
	}

	public static void send(IdentifiedPacket packet, Collection<ServerPlayerEntity> players) {
		for (ServerPlayerEntity player : players) {
			ServerPlayNetworking.send(player, packet.getChannel(), packet.getPacketByteBuf());
		}
	}


}
