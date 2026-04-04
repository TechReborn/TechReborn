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

package techreborn.events;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techreborn.TechReborn;
import techreborn.world.OreDepth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class OreDepthSyncHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(OreDepthSyncHandler.class);
	private static Map<Block, OreDepth> oreDepthMap = new HashMap<>();

	private OreDepthSyncHandler() {
	}

	public static void setup() {
		PayloadTypeRegistry.clientboundConfiguration().register(OreDepthPayload.ID, OreDepthPayload.PACKET_CODEC);

		ServerConfigurationConnectionEvents.CONFIGURE.register((handler, server) -> {
			if (ServerConfigurationNetworking.canSend(handler, OreDepthPayload.ID)) {
				List<OreDepth> oreDepths = OreDepth.create(server);
				var packet = ServerConfigurationNetworking.createClientboundPacket(new OreDepthPayload(oreDepths));
				handler.send(packet, null);
			} else {
				LOGGER.error("Client cannot receive ore depth packet. This may mean that TechReborn is not installed on the client.");
				handler.disconnect(Component.literal("The TechReborn mod must be installed to play on this server."));
			}
		});
	}

	public static void updateDepths(List<OreDepth> list) {
		synchronized (OreDepthSyncHandler.class) {
			oreDepthMap = list.stream()
				.collect(Collectors.toMap(oreDepth -> BuiltInRegistries.BLOCK.getValue(oreDepth.identifier()), Function.identity()));
		}
	}

	public static Map<Block, OreDepth> getOreDepthMap() {
		synchronized (OreDepthSyncHandler.class) {
			return oreDepthMap;
		}
	}

	public record OreDepthPayload(List<OreDepth> oreDepths) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<OreDepthPayload> ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "ore_depth"));
		public static final StreamCodec<FriendlyByteBuf, OreDepthPayload> PACKET_CODEC = StreamCodec.composite(
			OreDepth.PACKET_CODEC.apply(ByteBufCodecs.list()), OreDepthPayload::oreDepths,
			OreDepthPayload::new
		);

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return ID;
		}
	}
}
