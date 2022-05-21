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

import net.minecraft.util.Identifier;
import reborncore.common.network.IdentifiedPacket;
import reborncore.common.network.NetworkManager;
import techreborn.TechReborn;
import techreborn.world.OreDepth;

import java.util.List;

public class ClientboundPackets {
	public static final Identifier ORE_DEPTH = new Identifier(TechReborn.MOD_ID, "ore_depth");
	public static final Identifier OPEN_MANUAL = new Identifier(TechReborn.MOD_ID, "open_manual");

	public static IdentifiedPacket createPacketSyncOreDepth(List<OreDepth> oreDepths) {
		return NetworkManager.createClientBoundPacket(ORE_DEPTH, OreDepth.LIST_CODEC, oreDepths);
	}

	public static IdentifiedPacket createPacketOpenManual() {
		return NetworkManager.createClientBoundPacket(OPEN_MANUAL, packetBuffer -> {
		});
	}
}
