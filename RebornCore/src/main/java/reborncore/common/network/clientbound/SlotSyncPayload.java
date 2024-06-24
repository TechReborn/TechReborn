/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TeamReborn
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

package reborncore.common.network.clientbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.SlotConfiguration;

public record SlotSyncPayload(BlockPos pos, SlotConfiguration slotConfig) implements CustomPayload {
	public static final Id<SlotSyncPayload> ID = new Id<>(Identifier.of("reborncore:slot_sync"));
	public static final PacketCodec<RegistryByteBuf, SlotSyncPayload> PACKET_CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, SlotSyncPayload::pos,
		SlotConfiguration.PACKET_CODEC, SlotSyncPayload::slotConfig,
		SlotSyncPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
