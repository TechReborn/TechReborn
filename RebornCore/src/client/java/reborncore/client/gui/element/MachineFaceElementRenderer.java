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
import net.fabricmc.fabric.api.client.rendering.v1.SpecialGuiElementRegistry;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.render.SpecialGuiElementRenderer;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.render.state.TexturedQuadGuiElementRenderState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix3x2f;
import org.joml.Quaternionfc;

import java.util.ArrayList;
import java.util.List;

public class MachineFaceElementRenderer extends SpecialGuiElementRenderer<MachineFaceState> {
	private static final Direction[] DIRECTIONS = Direction.values();
	private static final RenderHandler renderHandler = new RenderHandler();
	private static final RenderElementHandler elementHandler = new RenderElementHandler();
	public static final List<Identifier> BLACKLIST = new ArrayList<>();

	public MachineFaceElementRenderer(SpecialGuiElementRegistry.Context context) {
		super(context.vertexConsumers());
	}

	@Override
	public Class<MachineFaceState> getElementClass() {
		return MachineFaceState.class;
	}

	@Override
	protected String getName() {
		return "machine face";
	}

	@Override
	protected void render(MachineFaceState state, MatrixStack matrices) {
		renderHandler.update(state, matrices, vertexConsumers);
		renderHandler.render(2, 0, RotationAxis.POSITIVE_Y.rotationDegrees(90F)); //left
		renderHandler.render(1, 0, RotationAxis.NEGATIVE_X.rotationDegrees(90F)); //top
		renderHandler.render(); //center
		renderHandler.render(-1, 1, RotationAxis.POSITIVE_X.rotationDegrees(90F)); //bottom
		renderHandler.render(-2, 0, RotationAxis.POSITIVE_Y.rotationDegrees(90F)); //right
		renderHandler.render(-2, 0, RotationAxis.POSITIVE_Y.rotationDegrees(180F)); //back
		renderHandler.clear();
	}

	@Override
	protected void renderElement(MachineFaceState element, GuiRenderState state) {
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
		private MatrixStack.Entry source;
		private int light;

		public void clear() {
			vertexConsumer = null;
			model = null;
			source = null;
			light = 0;
		}

		public void render(float x, float y, Quaternionfc quaternionfc) {
			MatrixStack.Entry entry = source.copy();
			entry.translate(x, y, 0);
			entry.rotate(quaternionfc);
			render(entry);
		}

		public void render() {
			render(source);
		}

		private void render(MatrixStack.Entry entry) {
			for (BlockModelPart blockModelPart : model.getParts(Random.create(42L))) {
				for (Direction direction : DIRECTIONS) {
					renderQuads(entry, blockModelPart.getQuads(direction));
				}
				renderQuads(entry, blockModelPart.getQuads(null));
			}
		}

		private void renderQuads(MatrixStack.Entry entry, List<BakedQuad> bakedQuads) {
			for (BakedQuad bakedQuad : bakedQuads) {
				if (BLACKLIST.contains(bakedQuad.sprite().getContents().getId())) {
					continue;
				}
				vertexConsumer.quad(entry, bakedQuad, 1.0F, 1.0F, 1.0F, 1.0F, light, OverlayTexture.DEFAULT_UV);
			}
		}

		public void update(MachineFaceState state, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers) {
			vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getSolid());
			model = state.model();
			light = OverlayTexture.getU(15F);
			source = matrices.peek();
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
			state.addSimpleElementToCurrentLayer(new TexturedQuadGuiElementRenderState(
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
			texture = TextureSetup.withoutGlTexture(textureView);
			pose = element.pose();
			x1 = element.x1();
			y1 = element.y1();
			i = -1;
		}
	}
}
