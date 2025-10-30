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

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.network.BlockPosPayload;

public record SetRedstoneStatePayload(BlockPos pos, RedstoneConfiguration.Element element, RedstoneConfiguration.State state) implements CustomPacketPayload, BlockPosPayload {
	public static final CustomPacketPayload.Type<SetRedstoneStatePayload> ID = new CustomPacketPayload.Type<>(ResourceLocation.parse("reborncore:set_redstone_state"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SetRedstoneStatePayload> CODEC = StreamCodec.composite(
		BlockPos.STREAM_CODEC, SetRedstoneStatePayload::pos,
		RedstoneConfiguration.Element.PACKET_CODEC, SetRedstoneStatePayload::element,
		RedstoneConfiguration.State.PACKET_CODEC, SetRedstoneStatePayload::state,
		SetRedstoneStatePayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
