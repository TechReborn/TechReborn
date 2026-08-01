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

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import reborncore.common.screen.builder.SyncedObjectType;
import reborncore.common.screen.builder.SyncedObjectTypes;

import java.util.List;

public record ScreenHandlerUpdatePayload(List<UpdatedValue<?>> updatedValues) implements CustomPacketPayload {
	public static final Type<ScreenHandlerUpdatePayload> ID = new Type<>(Identifier.parse("reborncore:screen_handler_update"));
	private static final StreamCodec<RegistryFriendlyByteBuf, UpdatedValue<?>> UPDATED_VALUE_CODEC = SyncedObjectTypes.STREAM_CODEC.dispatch(
		UpdatedValue::type,
		ScreenHandlerUpdatePayload::updatedValueCodec
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, ScreenHandlerUpdatePayload> PACKET_CODEC = UPDATED_VALUE_CODEC.apply(ByteBufCodecs.list()).map(
		ScreenHandlerUpdatePayload::new,
		ScreenHandlerUpdatePayload::updatedValues
	);

	public ScreenHandlerUpdatePayload {
		updatedValues = List.copyOf(updatedValues);
	}

	private static <T> StreamCodec<RegistryFriendlyByteBuf, UpdatedValue<T>> updatedValueCodec(SyncedObjectType<T> type) {
		return StreamCodec.composite(
			ByteBufCodecs.VAR_INT, UpdatedValue::id,
			type.codec(), UpdatedValue::value,
			(id, value) -> new UpdatedValue<>(type, id, value)
		);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public record UpdatedValue<T>(SyncedObjectType<T> type, int id, T value) {
	}
}
