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

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;

public class ExtendedPacketBuffer extends PacketByteBuf {
	public ExtendedPacketBuffer(ByteBuf wrapped) {
		super(wrapped);
	}

	public void writeObject(Object object) {
		ObjectBufferUtils.writeObject(object, this);
	}

	public Object readObject() {
		return ObjectBufferUtils.readObject(this);
	}

	// Supports reading and writing list codecs
	public <T> void writeCodec(Codec<T> codec, T object) {
		DataResult<NbtElement> dataResult = codec.encodeStart(NbtOps.INSTANCE, object);
		if (dataResult.error().isPresent()) {
			throw new RuntimeException("Failed to encode: " + dataResult.error().get().message() + " " + object);
		} else {
			NbtElement tag = dataResult.result().get();
			if (tag instanceof NbtCompound) {
				writeByte(0);
				writeNbt((NbtCompound) tag);
			} else if (tag instanceof NbtList) {
				writeByte(1);
				NbtCompound compoundTag = new NbtCompound();
				compoundTag.put("tag", tag);
				writeNbt(compoundTag);
			} else {
				throw new RuntimeException("Failed to write: " + tag);
			}
		}
	}

	public <T> T readCodec(Codec<T> codec) {
		byte type = readByte();
		NbtElement tag = null;

		if (type == 0) {
			tag = readNbt();
		} else if (type == 1) {
			tag = readNbt().get("tag");
		} else {
			throw new RuntimeException("Failed to read codec");
		}

		DataResult<T> dataResult = codec.parse(NbtOps.INSTANCE, tag);

		if (dataResult.error().isPresent()) {
			throw new RuntimeException("Failed to decode: " + dataResult.error().get().message() + " " + tag);
		} else {
			return dataResult.result().get();
		}
	}
}
