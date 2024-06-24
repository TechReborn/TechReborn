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

package reborncore.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;
import reborncore.common.chunkloading.ChunkLoaderManager;
import reborncore.common.network.serverbound.ChunkLoaderRequestPayload;

import java.util.ArrayList;
import java.util.List;

public class ClientChunkManager {

	private static final List<ChunkLoaderManager.LoadedChunk> loadedChunks = new ArrayList<>();

	public static void setLoadedChunks(List<ChunkLoaderManager.LoadedChunk> chunks) {
		loadedChunks.clear();
		loadedChunks.addAll(chunks);
	}

	public static void toggleLoadedChunks(BlockPos chunkLoader) {
		if (loadedChunks.isEmpty()) {
			ClientPlayNetworking.send(new ChunkLoaderRequestPayload(chunkLoader));
		} else {
			loadedChunks.clear();
		}
	}

	public static boolean hasChunksForLoader(BlockPos pos) {
		return loadedChunks.stream()
				.filter(loadedChunk -> loadedChunk.chunkLoader().equals(pos))
				.anyMatch(loadedChunk -> loadedChunk.world().equals(ChunkLoaderManager.getWorldName(MinecraftClient.getInstance().world)));
	}

	public static void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double x, double y, double z) {
		if (loadedChunks.isEmpty()) {
			return;
		}
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getDebugLineStrip(1.0));
		Matrix4f matrix4f = matrices.peek().getPositionMatrix();

		loadedChunks.stream()
				.filter(loadedChunk -> loadedChunk.world().equals(ChunkLoaderManager.getWorldName(minecraftClient.world)))
				.forEach(loadedChunk -> {
					float chunkX = (float)((double) loadedChunk.chunk().getStartX() - x);
					float chunkY = (float)((double) loadedChunk.chunk().getStartZ() - z);

					vertexConsumer.vertex(matrix4f, chunkX + 8F, 0.0F - (float) y, chunkY + 8F).color(1.0F, 0.0F, 0.0F, 0.0F);
					vertexConsumer.vertex(matrix4f,chunkX + 8F, 0.0F - (float) y, chunkY + 8F).color(1.0F, 0.0F, 0.0F, 0.5F);
					vertexConsumer.vertex(matrix4f,chunkX + 8F, 256.0F - (float) y, chunkY + 8F).color(1.0F, 0.0F, 0.0F, 0.5F);
					vertexConsumer.vertex(matrix4f,chunkX + 8F, 256.0F - (float) y, chunkY + 8F).color(1.0F, 0.0F, 0.0F, 0.0F);
				});
	}

}
