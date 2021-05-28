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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.PacketByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;

public class ExtendedPacketBuffer extends PacketByteBuf {
	public ExtendedPacketBuffer(ByteBuf wrapped) {
		super(wrapped);
	}

	protected void writeObject(Object object) {
		ObjectBufferUtils.writeObject(object, this);
	}

	protected Object readObject() {
		return ObjectBufferUtils.readObject(this);
	}

	public void writeBigInt(BigInteger bigInteger) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream outputStream = new ObjectOutputStream(baos);
			outputStream.writeObject(bigInteger);
			writeByteArray(baos.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException("Failed to write big int");
		}
	}

	public BigInteger readBigInt() {
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(readByteArray()));
			return (BigInteger) inputStream.readObject();
		} catch (Exception e) {
			throw new RuntimeException("Failed to read big int");
		}
	}

	// Supports reading and writing list codec's
	public <T> void writeCodec(Codec<T> codec, T object) {
		DataResult<Tag> dataResult = codec.encodeStart(NbtOps.INSTANCE, object);
		if (dataResult.error().isPresent()) {
			throw new RuntimeException("Failed to encode: " + dataResult.error().get().message() + " " + object);
		} else {
			Tag tag = dataResult.result().get();
			if (tag instanceof CompoundTag) {
				writeByte(0);
				writeCompoundTag((CompoundTag) tag);
			} else if (tag instanceof ListTag) {
				writeByte(1);
				CompoundTag compoundTag = new CompoundTag();
				compoundTag.put("tag", tag);
				writeCompoundTag(compoundTag);
			} else {
				throw new RuntimeException("Failed to write: " + tag);
			}
		}
	}

	public <T> T readCodec(Codec<T> codec) {
		byte type = readByte();
		Tag tag = null;

		if (type == 0) {
			tag = readCompoundTag();
		} else if (type == 1) {
			tag = readCompoundTag().get("tag");
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
