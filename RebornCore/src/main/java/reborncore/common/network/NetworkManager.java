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

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.Collection;
import java.util.Collections;

public class NetworkManager {
	public static void sendToAll(CustomPacketPayload payload, MinecraftServer server) {
		send(payload, PlayerLookup.all(server));
	}

	public static void sendToPlayer(CustomPacketPayload payload, ServerPlayer serverPlayerEntity) {
		send(payload, Collections.singletonList(serverPlayerEntity));
	}

	public static void sendToWorld(CustomPacketPayload payload, ServerLevel world) {
		send(payload, PlayerLookup.world(world));
	}


	public static void sendToTracking(CustomPacketPayload payload, BlockEntity blockEntity) {
		send(payload, PlayerLookup.tracking(blockEntity));
	}

	public static void send(CustomPacketPayload payload, Collection<ServerPlayer> players) {
		for (ServerPlayer player : players) {
			ServerPlayNetworking.send(player, payload);
		}
	}
}
