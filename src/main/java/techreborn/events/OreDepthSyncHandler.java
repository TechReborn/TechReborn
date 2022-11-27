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

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import reborncore.common.network.NetworkManager;
import techreborn.packets.ClientboundPackets;
import techreborn.world.OreDepth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class OreDepthSyncHandler {
	private static Map<Block, OreDepth> oreDepthMap = new HashMap<>();

	private OreDepthSyncHandler() {
	}

	public static void setup() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
				NetworkManager.sendTo(ClientboundPackets.createPacketSyncOreDepth(OreDepth.create(server)), sender)
		);
	}

	public static void updateDepths(List<OreDepth> list) {
		synchronized (OreDepthSyncHandler.class) {
			oreDepthMap = list.stream()
				.collect(Collectors.toMap(oreDepth -> Registries.BLOCK.get(oreDepth.identifier()), Function.identity()));
		}
	}

	public static Map<Block, OreDepth> getOreDepthMap() {
		synchronized (OreDepthSyncHandler.class) {
			return oreDepthMap;
		}
	}
}
