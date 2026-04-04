/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2025 TeamReborn
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

package reborncore.client.gui.element;

import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.client.rendering.v1.PictureInPictureRendererRegistry;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.state.gui.BlitRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import org.joml.Matrix3x2f;
import org.joml.Quaternionfc;

import java.util.ArrayList;
import java.util.List;

public class MachineFaceElementRenderer extends PictureInPictureRenderer<MachineFaceState> {
	private static final Direction[] DIRECTIONS = Direction.values();
	private static final RenderHandler renderHandler = new RenderHandler();
	private static final RenderElementHandler elementHandler = new RenderElementHandler();
	public static final List<Identifier> BLACKLIST = new ArrayList<>();

	public MachineFaceElementRenderer(PictureInPictureRendererRegistry.Context context) {
		super(context.bufferSource());
	}

	@Override
	public Class<MachineFaceState> getRenderStateClass() {
		return MachineFaceState.class;
	}

	@Override
	protected String getTextureLabel() {
		return "machine face";
	}

	@Override
	protected void renderToTexture(MachineFaceState state, PoseStack matrices) {
		renderHandler.update(state, matrices, bufferSource);
		renderHandler.render(2, 0, Axis.YP.rotationDegrees(90F)); //left
		renderHandler.render(1, 0, Axis.XN.rotationDegrees(90F)); //top
		renderHandler.render(); //center
		renderHandler.render(-1, 1, Axis.XP.rotationDegrees(90F)); //bottom
		renderHandler.render(-2, 0, Axis.YP.rotationDegrees(90F)); //right
		renderHandler.render(-2, 0, Axis.YP.rotationDegrees(180F)); //back
		renderHandler.clear();
	}

	@Override
	protected void blitTexture(MachineFaceState element, GuiRenderState state) {
		elementHandler.update(element, state, textureView);
		elementHandler.render(4, 23); //left
		elementHandler.render(23, 4); //top
		elementHandler.render(23, 23); //center
		elementHandler.render(23, 42); //bottom
		elementHandler.render(42, 23); //right
		elementHandler.render(42, 42); //back
		elementHandler.clear();
	}

	static class RenderHandler {
		private VertexConsumer vertexConsumer;
		private BlockStateModel model;
		private PoseStack.Pose source;
		private int light;

		public void clear() {
			vertexConsumer = null;
			model = null;
			source = null;
			light = 0;
		}

		public void render(float x, float y, Quaternionfc quaternionfc) {
			PoseStack.Pose entry = source.copy();
			entry.translate(x, y, 0);
			entry.rotate(quaternionfc);
			render(entry);
		}

		public void render() {
			render(source);
		}

		private void render(PoseStack.Pose entry) {
			List<BlockStateModelPart> parts = new ArrayList<>();
			model.collectParts(RandomSource.create(42L), parts);
			for (BlockStateModelPart blockModelPart : parts) {
				for (Direction direction : DIRECTIONS) {
					renderQuads(entry, blockModelPart.getQuads(direction));
				}
				renderQuads(entry, blockModelPart.getQuads(null));
			}
		}

			private void renderQuads(PoseStack.Pose entry, List<BakedQuad> bakedQuads) {
			for (BakedQuad bakedQuad : bakedQuads) {
				if (BLACKLIST.contains(bakedQuad.materialInfo().sprite().contents().name())) {
					continue;
				}
				// Create QuadInstance with color, light, and overlay data
				QuadInstance quadInstance = new QuadInstance();
				quadInstance.setColor(ARGB.white(1.0F)); // Opaque white color
				quadInstance.setLightCoords(light);
				quadInstance.setOverlayCoords(OverlayTexture.NO_OVERLAY);
				
				vertexConsumer.putBakedQuad(entry, bakedQuad, quadInstance);
			}
		}

		public void update(MachineFaceState state, PoseStack matrices, MultiBufferSource.BufferSource vertexConsumers) {
			vertexConsumer = vertexConsumers.getBuffer(RenderTypes.solidMovingBlock());
			model = state.model();
			light = OverlayTexture.u(15F);
			source = matrices.last();
			source.scale(-16F, -16F, 0);
		}
	}

	static class RenderElementHandler {
		private GuiRenderState state;
		private TextureSetup texture;
		private Matrix3x2f pose;
		private int x1;
		private int y1;
		private int i;

		public void clear() {
			state = null;
			texture = null;
			pose = null;
			x1 = 0;
			y1 = 0;
			i = -1;
		}

		public void render(int x, int y) {
			i++;
			int left = x1 + x;
			int top = y1 + y;
			float u1 = i / 6F;
			float u2 = (i + 1) / 6F;
			state.addBlitToCurrentLayer(new BlitRenderState(
				RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA,
				texture,
				pose,
				left,
				top,
				left + 16,
				top + 16,
				u1,
				u2,
				1.0F,
				0.0F,
				-1,
				null,
				null
			));
		}

		public void update(MachineFaceState element, GuiRenderState guiRenderState, GpuTextureView textureView) {
			state = guiRenderState;
			texture = TextureSetup.singleTexture(textureView, RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST));
			pose = element.pose();
			x1 = element.x0();
			y1 = element.y0();
			i = -1;
		}
	}
}
