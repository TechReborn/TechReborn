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

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import reborncore.common.chunkloading.ChunkLoaderManager;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.ServerBoundPackets;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientChunkManager {

	private static final List<ChunkLoaderManager.LoadedChunk> loadedChunks = new ArrayList<>();

	public static void setLoadedChunks(List<ChunkLoaderManager.LoadedChunk> chunks) {
		loadedChunks.clear();
		loadedChunks.addAll(chunks);
	}

	public static void toggleLoadedChunks(BlockPos chunkLoader) {
		if (loadedChunks.size() == 0) {
			NetworkManager.sendToServer(ServerBoundPackets.requestChunkloaderChunks(chunkLoader));
		} else {
			loadedChunks.clear();
		}
	}

	public static boolean hasChunksForLoader(BlockPos pos) {
		return loadedChunks.stream()
				.filter(loadedChunk -> loadedChunk.getChunkLoader().equals(pos))
				.anyMatch(loadedChunk -> loadedChunk.getWorld().equals(ChunkLoaderManager.getWorldName(MinecraftClient.getInstance().world)));
	}

	public static void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double x, double y, double z) {
		if (loadedChunks.size() == 0) {
			return;
		}
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();

		RenderSystem.enableDepthTest();
		RenderSystem.shadeModel(7425);
		RenderSystem.enableAlphaTest();
		RenderSystem.defaultAlphaFunc();

		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferBuilder = tessellator.getBuffer();

		RenderSystem.disableTexture();
		RenderSystem.disableBlend();
		RenderSystem.lineWidth(5.0F);

		bufferBuilder.begin(3, VertexFormats.POSITION_COLOR);

		loadedChunks.stream()
				.filter(loadedChunk -> loadedChunk.getWorld().equals(ChunkLoaderManager.getWorldName(minecraftClient.world)))
				.forEach(loadedChunk -> {
					double chunkX = (double) loadedChunk.getChunk().getStartX() - x;
					double chunkY = (double) loadedChunk.getChunk().getStartZ() - z;

					bufferBuilder.vertex(chunkX + 8, 0.0D - y, chunkY + 8).color(1.0F, 0.0F, 0.0F, 0.0F).next();
					bufferBuilder.vertex(chunkX + 8, 0.0D - y, chunkY + 8).color(1.0F, 0.0F, 0.0F, 0.5F).next();
					bufferBuilder.vertex(chunkX + 8, 256.0D - y, chunkY + 8).color(1.0F, 0.0F, 0.0F, 0.5F).next();
					bufferBuilder.vertex(chunkX + 8, 256.0D - y, chunkY + 8).color(1.0F, 0.0F, 0.0F, 0.0F).next();
				});

		tessellator.draw();
		RenderSystem.lineWidth(1.0F);
		RenderSystem.enableBlend();
		RenderSystem.enableTexture();
	}

}
