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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ColorHelper.Argb;
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

	public static boolean isShow() {
		return !loadedChunks.isEmpty();
	}

	public static boolean hasChunksForLoader(BlockPos pos) {
		return loadedChunks.stream()
				.filter(loadedChunk -> loadedChunk.chunkLoader().equals(pos))
				.anyMatch(loadedChunk -> loadedChunk.world().equals(ChunkLoaderManager.getWorldName(MinecraftClient.getInstance().world)));
	}

	public static void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double x, double y, double z) {
		int size = loadedChunks.size();
		if (size == 0) {
			return;
		}

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		Identifier worldName = ChunkLoaderManager.getWorldName(minecraftClient.world);
		int[] posX = new int[size], posZ = new int[size];
		int right = 0, startX, startZ, maxX = Integer.MIN_VALUE, minX = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE, minZ = Integer.MAX_VALUE;
		for (int i = 0; i < size; i++) {
			ChunkLoaderManager.LoadedChunk chunk = loadedChunks.get(i);
			if (chunk.world().equals(worldName)) {
				ChunkPos pos = chunk.chunk();
				startX = posX[right] = pos.getStartX();
				startZ = posZ[right] = pos.getStartZ();
				if (maxX < startX) maxX = startX;
				if (minX > startX) minX = startX;
				if (maxZ < startZ) maxZ = startZ;
				if (minZ > startZ) minZ = startZ;
				right++;
			}
		}
		if (right == 0) {
			return;
		}
		size = right;

		int bottom = minecraftClient.world.getBottomY();
		int top = minecraftClient.world.getTopY();
		DrawContext ctx = new DrawContext(
			vertexConsumers.getBuffer(RenderLayer.getDebugLineStrip(1.0)),
			matrices.peek().getPositionMatrix(),
			x, y, z, bottom, top
		);

		int chunkSize = 16, middle = chunkSize / 2, end = chunkSize;
		for (int i = 0; i < size; i++) {
			ctx.updatePos(posX[i], posZ[i]);
			ctx.drawVerticalRed(middle, middle);
			if (posX[i] == minX) {
				ctx.drawVerticalBlue(0, 0);
				for (int j = 2; j < chunkSize; j += 4) ctx.drawVerticalYellow(0, j);
				for (int j = 4; j < chunkSize; j += 4) ctx.drawVerticalCyan(0, j);
			}
			if (posX[i] == maxX) {
				ctx.drawVerticalBlue(end, end);
				for (int j = 2; j < chunkSize; j += 4) ctx.drawVerticalYellow(end, j);
				for (int j = 4; j < chunkSize; j += 4) ctx.drawVerticalCyan(end, j);
			}
			if (posZ[i] == minZ) {
				ctx.drawVerticalBlue(end, 0);
				for (int j = 2; j < chunkSize; j += 4) ctx.drawVerticalYellow(j, 0);
				for (int j = 4; j < chunkSize; j += 4) ctx.drawVerticalCyan(j, 0);
			}
			if (posZ[i] == maxZ) {
				ctx.drawVerticalBlue(0, end);
				for (int j = 2; j < chunkSize; j += 4) ctx.drawVerticalYellow(j, end);
				for (int j = 4; j < chunkSize; j += 4) ctx.drawVerticalCyan(j, end);
			}
		}

		ctx.updatePos(minX, minZ, maxX + chunkSize, maxZ + chunkSize);
		for (int i = bottom, j; i < top;) {
			ctx.drawHorizontalBlue(i);
			ctx.drawHorizontalCyan(j = i + middle);
			for (int stop = (i += 2) + 12; i <= stop; i += 2) {
				if (i != j) ctx.drawHorizontalYellow(i);
			}
		}
		ctx.drawHorizontalBlue(top);
	}

	static class DrawContext {
		private static final int NONE = Argb.getArgb(0, 0, 0, 0);
		private static final int RED = Argb.getArgb(127, 255, 0, 0);
		private static final int BLUE = Argb.getArgb(255, 63, 63, 255);
		private static final int DARK_CYAN = Argb.getArgb(255, 0, 155, 155);
		private static final int YELLOW = Argb.getArgb(255, 255, 255, 0);
		private final VertexConsumer vertexConsumer;
		private final Matrix4f matrix4f;
		private final double cameraX;
		private final double cameraY;
		private final double cameraZ;
		private final float bottom;
		private final float top;
		private float x1 = 0;
		private float z1 = 0;
		private float x2 = 0;
		private float z2 = 0;

		DrawContext(VertexConsumer vertexConsumer, Matrix4f matrix4f, double cameraX, double cameraY, double cameraZ, int bottom, int top) {
			this.vertexConsumer = vertexConsumer;
			this.matrix4f = matrix4f;
			this.cameraX = cameraX;
			this.cameraY = cameraY;
			this.cameraZ = cameraZ;
			this.bottom = (float) (bottom - cameraY);
			this.top = (float) (top - cameraY);
		}

		public void updatePos(float x, float z) {
			this.x1 = (float) (x - cameraX);
			this.z1 = (float) (z - cameraZ);
		}

		public void updatePos(float x1, float z1, float x2, float z2) {
			this.x1 = (float) (x1 - cameraX);
			this.z1 = (float) (z1 - cameraZ);
			this.x2 = (float) (x2 - cameraX);
			this.z2 = (float) (z2 - cameraZ);
		}

		public void drawVertical(int color, float x, float z) {
			vertexConsumer.vertex(matrix4f, x, bottom, z).color(NONE);
			vertexConsumer.vertex(matrix4f, x, bottom, z).color(color);
			vertexConsumer.vertex(matrix4f, x, top, z).color(color);
			vertexConsumer.vertex(matrix4f, x, top, z).color(NONE);
		}

		public void drawVerticalRed(int x, int z) {
			drawVertical(RED, this.x1 + x, this.z1 + z);
		}

		public void drawVerticalBlue(int x, int z) {
			drawVertical(BLUE, this.x1 + x, this.z1 + z);
		}

		public void drawVerticalYellow(int x, int z) {
			drawVertical(YELLOW, this.x1 + x, this.z1 + z);
		}

		public void drawVerticalCyan(int x, int z) {
			drawVertical(DARK_CYAN, this.x1 + x, this.z1 + z);
		}

		public void drawHorizontal(int color, float y) {
			vertexConsumer.vertex(matrix4f, x1, y, z1).color(NONE);
			vertexConsumer.vertex(matrix4f, x1, y, z1).color(color);
			vertexConsumer.vertex(matrix4f, x1, y, z2).color(color);
			vertexConsumer.vertex(matrix4f, x2, y, z2).color(color);
			vertexConsumer.vertex(matrix4f, x2, y, z1).color(color);
			vertexConsumer.vertex(matrix4f, x1, y, z1).color(color);
			vertexConsumer.vertex(matrix4f, x1, y, z1).color(NONE);
		}

		public void drawHorizontalBlue(int y) {
			drawHorizontal(BLUE, (float) (y - cameraY));
		}

		public void drawHorizontalYellow(int y) {
			drawHorizontal(YELLOW, (float) (y - cameraY));
		}

		public void drawHorizontalCyan(int y) {
			drawHorizontal(DARK_CYAN, (float) (y - cameraY));
		}
	}
}
