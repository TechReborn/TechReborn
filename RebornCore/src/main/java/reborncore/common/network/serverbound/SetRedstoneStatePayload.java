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

package reborncore.common.network.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.network.BlockPosPayload;

public record SetRedstoneStatePayload(BlockPos pos, RedstoneConfiguration.Element element, RedstoneConfiguration.State state) implements CustomPayload, BlockPosPayload {
	public static final CustomPayload.Id<SetRedstoneStatePayload> ID = new CustomPayload.Id<>(Identifier.of("reborncore:set_redstone_state"));
	public static final PacketCodec<RegistryByteBuf, SetRedstoneStatePayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, SetRedstoneStatePayload::pos,
		RedstoneConfiguration.Element.PACKET_CODEC, SetRedstoneStatePayload::element,
		RedstoneConfiguration.State.PACKET_CODEC, SetRedstoneStatePayload::state,
		SetRedstoneStatePayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
