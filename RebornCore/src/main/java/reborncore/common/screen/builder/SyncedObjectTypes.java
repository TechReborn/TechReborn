/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TeamReborn
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

package reborncore.common.screen.builder;

import io.netty.handler.codec.DecoderException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SyncedObjectTypes {
	private static final Map<Identifier, SyncedObjectType<?>> TYPES = new ConcurrentHashMap<>();

	public static final SyncedObjectType<Boolean> BOOL = register("bool", ByteBufCodecs.BOOL);
	public static final SyncedObjectType<Integer> INT = register("int", ByteBufCodecs.INT);
	public static final SyncedObjectType<Long> VAR_LONG = register("var_long", ByteBufCodecs.VAR_LONG);
	public static final SyncedObjectType<Float> FLOAT = register("float", ByteBufCodecs.FLOAT);
	public static final SyncedObjectType<CompoundTag> COMPOUND_TAG = register("compound_tag", ByteBufCodecs.COMPOUND_TAG);
	public static final SyncedObjectType<ItemStack> ITEM_STACK = register("item_stack", ItemStack.OPTIONAL_STREAM_CODEC);
	public static final SyncedObjectType<FluidValue> FLUID_VALUE = register("fluid_value", FluidValue.PACKET_CODEC);
	public static final SyncedObjectType<FluidInstance> FLUID_INSTANCE = register("fluid_instance", FluidInstance.PACKET_CODEC);
	public static final SyncedObjectType<RedstoneConfiguration> REDSTONE_CONFIGURATION = register("redstone_configuration", RedstoneConfiguration.PACKET_CODEC);

	public static final StreamCodec<RegistryFriendlyByteBuf, SyncedObjectType<?>> STREAM_CODEC = Identifier.STREAM_CODEC.<SyncedObjectType<?>>map(
		id -> {
			SyncedObjectType<?> type = TYPES.get(id);
			if (type == null) {
				throw new DecoderException("Unknown screen handler synced object type " + id);
			}
			return type;
		},
		SyncedObjectType::id
	).mapStream(buf -> buf);

	private SyncedObjectTypes() {
	}

	private static <T> SyncedObjectType<T> register(String path, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		return register(Identifier.fromNamespaceAndPath("reborncore", path), codec);
	}

	/**
	 * Registers a synchronized object type. The same identifier and codec must be registered on both sides before play begins.
	 */
	public static <T> SyncedObjectType<T> register(Identifier id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		SyncedObjectType<T> type = new SyncedObjectType<>(id, codec);
		if (TYPES.putIfAbsent(id, type) != null) {
			throw new IllegalArgumentException("Duplicate screen handler synced object type " + id);
		}
		return type;
	}
}
