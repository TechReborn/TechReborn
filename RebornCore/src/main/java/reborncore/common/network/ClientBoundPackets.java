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
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.FluidConfiguration;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.chunkloading.ChunkLoaderManager;

import java.util.List;

public class ClientBoundPackets {

	public static IdentifiedPacket createCustomDescriptionPacket(BlockEntity blockEntity) {
		return createCustomDescriptionPacket(blockEntity.getPos(), blockEntity.createNbt());
	}

	public static IdentifiedPacket createCustomDescriptionPacket(BlockPos blockPos, NbtCompound nbt) {
		return NetworkManager.createClientBoundPacket(new Identifier("reborncore", "custom_description"), packetBuffer -> {
			packetBuffer.writeBlockPos(blockPos);
			packetBuffer.writeNbt(nbt);
		});
	}

	public static IdentifiedPacket createPacketFluidConfigSync(BlockPos pos, FluidConfiguration fluidConfiguration) {
		return NetworkManager.createClientBoundPacket(new Identifier("reborncore", "fluid_config_sync"), packetBuffer -> {
			packetBuffer.writeBlockPos(pos);
			packetBuffer.writeNbt(fluidConfiguration.write());
		});
	}

	public static IdentifiedPacket createPacketSlotSync(BlockPos pos, SlotConfiguration slotConfig) {
		return NetworkManager.createClientBoundPacket(new Identifier("reborncore", "slot_sync"), packetBuffer -> {
			packetBuffer.writeBlockPos(pos);
			packetBuffer.writeNbt(slotConfig.write());
		});
	}

	public static IdentifiedPacket createPacketSendObject(ScreenHandler screenHandler, Int2ObjectMap<Object> updatedValues) {
		return NetworkManager.createClientBoundPacket(new Identifier("reborncore", "send_object"), packetBuffer -> {
			packetBuffer.writeInt(updatedValues.size());
			updatedValues.forEach((integer, o) -> {
				packetBuffer.writeInt(integer);
				packetBuffer.writeObject(o);
			});
			packetBuffer.writeInt(screenHandler.getClass().getName().length());
			packetBuffer.writeString(screenHandler.getClass().getName());
		});
	}

	public static IdentifiedPacket createPacketSyncLoadedChunks(List<ChunkLoaderManager.LoadedChunk> chunks) {
		return NetworkManager.createClientBoundPacket(new Identifier("reborncore", "sync_chunks"), ChunkLoaderManager.CODEC, chunks);
	}

	public static IdentifiedPacket createPacketQueueItemStacksToRender(List<ItemStack> stacks) {
		return NetworkManager.createClientBoundPacket(new Identifier("reborncore", "stacks_to_render"), Codec.list(ItemStack.CODEC), stacks);
	}
}
