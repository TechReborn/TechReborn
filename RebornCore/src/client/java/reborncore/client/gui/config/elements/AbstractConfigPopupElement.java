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

package reborncore.client.gui.config.elements;

import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Matrix3x2fStack;
import reborncore.client.gui.GuiBase;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.MachineFacing;

import java.util.Arrays;

public abstract class AbstractConfigPopupElement extends ElementBase {
	private static final ProjectionMatrix2 guiProjectionMatrix = new ProjectionMatrix2("gui", 1000.0F, 11000.0F, true);
	private static final RenderLayer guiLayer = RenderLayer.of("gui", 786432, RenderPipelines.GUI, RenderLayer.MultiPhaseParameters.builder().build(false));
	private final int height;
	private final String[] pencils;
	private int pencilWidth;
	private int fixIndex = Integer.MAX_VALUE;
	private boolean mouseDown = false;
	private final boolean[] mouseIn = { false, false, false, false, false, false };
	private final MachineFacing[] mouseBoxFacingMap = {
		MachineFacing.UP,
		MachineFacing.FRONT,
		MachineFacing.RIGHT,
		MachineFacing.LEFT,
		MachineFacing.DOWN,
		MachineFacing.BACK
	};
	private final int[][] mouseBoxMap = {
		{ 23, 4 },
		{ 23, 23 },
		{ 42, 23 },
		{ 4, 23 },
		{ 23, 42 },
		{ 42, 42 }
	};

	@Nullable
	public String pencil;

	public AbstractConfigPopupElement(int x, int y, int height, SpriteIdentifier sprite, int textureWidth, int textureHeight, String[] pencils) {
		super(x, y, sprite, textureWidth, textureHeight);
		this.height = height;
		this.pencils = pencils;
		int space = 75 - pencils.length;
		this.pencilWidth = space / pencils.length;
		if (space % pencilWidth != 0) {
			this.pencilWidth++;
			this.fixIndex = space % pencils.length;
		}
	}

	@Override
	public boolean isMouseWithinRect(GuiBase<?> gui, double mouseX, double mouseY) {
		// sprite size is 62x62, expands to 84x79
		return isInRect(gui, getX() - 8, getY(), 84, 79, mouseX, mouseY);
	}

	@Override
	public final void draw(DrawContext drawContext, GuiBase<?> gui, int mouseX, int mouseY) {
		drawContext.getMatrices().pushMatrix();
		int x = adjustX(gui, getX() - 8);
		int y = adjustY(gui, getY() - 7);
		gui.builder.drawDefaultBackground(
			drawContext,
			x,
			y,
			84,
			height
		);
		drawContext.getMatrices().popMatrix();

		super.draw(drawContext, gui, mouseX, mouseY);

		MinecraftClient client = MinecraftClient.getInstance();
		final MachineBaseBlockEntity machine = ((MachineBaseBlockEntity) gui.be);
		final BlockState state = machine.getCachedState();
		final BlockState defaultState = state.getBlock().getDefaultState();
		final BlockRenderManager dispatcher = client.getBlockRenderManager();
		final BlockStateModel model = dispatcher.getModels().getModel(defaultState);

		RenderSystem.backupProjectionMatrix();
		Window window = client.getWindow();
		int height = window.getFramebufferHeight();
		int factor = window.getScaleFactor();
		RenderSystem.setProjectionMatrix(
			guiProjectionMatrix.set((float)window.getFramebufferWidth() / factor, (float) height / factor),
			ProjectionType.ORTHOGRAPHIC
		);
		Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
		matrix4fStack.pushMatrix();
		matrix4fStack.translate(0, 0, -11000);
		VertexConsumerProvider.Immediate vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();
		Matrix3x2fStack matrices = drawContext.getMatrices();
		MatrixStack matrixStack = new MatrixStack();
		matrixStack.translate(matrices.m20, matrices.m21, 900);
		RenderLayer layer = RenderLayer.getSolid();
		drawState(matrices, matrixStack, gui, model, vertexConsumers, layer, 4, 23, RotationAxis.POSITIVE_Y.rotationDegrees(90F), height, factor, 0, 0); //left
		drawState(matrices, matrixStack, gui, model, vertexConsumers, layer, 23, 4, RotationAxis.NEGATIVE_X.rotationDegrees(90F), height, factor, 0, 0); //top
		drawState(matrices, matrixStack, gui, model, vertexConsumers, layer, 23, 23, null, height, factor, 0, 0); //centre
		drawState(matrices, matrixStack, gui, model, vertexConsumers, layer, 23, 26, RotationAxis.POSITIVE_X.rotationDegrees(90F), height, factor, 0, 16); //bottom
		drawState(matrices, matrixStack, gui, model, vertexConsumers, layer, 42, 23, RotationAxis.POSITIVE_Y.rotationDegrees(90F), height, factor, 0, 0); //right
		drawState(matrices, matrixStack, gui, model, vertexConsumers, layer, 26, 42, RotationAxis.POSITIVE_Y.rotationDegrees(180F), height, factor, 16, 0); //back
		RenderSystem.disableScissorForRenderTypeDraws();

		if (mouseDown) {
			for (int i = 0; i < 6; i++) {
				if (isInBox(mouseBoxMap[i][0], mouseBoxMap[i][1], 16, 16, mouseX, mouseY, gui)) {
					if (!mouseIn[i]) {
						mouseIn[i] = true;
						cycleConfig(mouseBoxFacingMap[i].getFacing(gui.getMachine()), gui);
					}
					break;
				}
			}
		}

		drawSateColor(matrixStack, vertexConsumers, gui, MachineFacing.UP.getFacing(machine), 22, -1);
		drawSateColor(matrixStack, vertexConsumers, gui, MachineFacing.FRONT.getFacing(machine), 22, 18);
		drawSateColor(matrixStack, vertexConsumers, gui, MachineFacing.DOWN.getFacing(machine), 22, 37);
		drawSateColor(matrixStack, vertexConsumers, gui, MachineFacing.RIGHT.getFacing(machine), 41, 18);
		drawSateColor(matrixStack, vertexConsumers, gui, MachineFacing.BACK.getFacing(machine), 41, 37);
		drawSateColor(matrixStack, vertexConsumers, gui, MachineFacing.LEFT.getFacing(machine), 3, 18);
		vertexConsumers.draw();
		matrix4fStack.popMatrix();
		RenderSystem.restoreProjectionMatrix();

		drawPencil(drawContext, gui, mouseX, mouseY, x, y + 71);
	}

	@Override
	public final boolean onClick(GuiBase<?> gui, double mouseX, double mouseY) {
		mouseDown = true;
		for (int i = 0; i < 6; i++) {
			if (isInBox(mouseBoxMap[i][0], mouseBoxMap[i][1], 16, 16, mouseX, mouseY, gui)) {
				mouseIn[i] = true;
				cycleConfig(mouseBoxFacingMap[i].getFacing(gui.getMachine()), gui);
				return true;
			}
		}
		int rectX = -2, rectWidth = pencilWidth - 2;
		for (String pencil : pencils) {
			if (isInBox(rectX, 65, rectWidth, 11, mouseX, mouseY, gui)) {
				this.pencil = pencil.equals(this.pencil) ? null : pencil;
				return true;
			}
			rectX += pencilWidth + 1;
		}

		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int state) {
		mouseDown = false;
		Arrays.fill(mouseIn, false);
		return true;
	}

	protected abstract void cycleConfig(Direction side, GuiBase<?> guiBase);

	protected abstract void drawSateColor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, GuiBase<?> gui, Direction side, int inx, int iny);

	protected boolean isInBox(int rectX, int rectY, int rectWidth, int rectHeight, double pointX, double pointY, GuiBase<?> guiBase) {
		rectX += getX();
		rectY += getY();
		return isInRect(guiBase, rectX, rectY, rectWidth, rectHeight, pointX, pointY);
		//return (pointX - guiBase.getGuiLeft()) >= rectX - 1 && (pointX - guiBase.getGuiLeft()) < rectX + rectWidth + 1 && (pointY - guiBase.getGuiTop()) >= rectY - 1 && (pointY - guiBase.getGuiTop()) < rectY + rectHeight + 1;
	}

	protected void drawState(Matrix3x2fStack matrix3x2fStack,
						MatrixStack matrixStack,
						GuiBase<?> gui,
						BlockStateModel model,
						VertexConsumerProvider.Immediate vertexConsumers,
						RenderLayer layer,
						int x,
						int y,
						Quaternionf quaternion,
						int height,
						int factor,
						int paddingLeft,
						int paddingTop) {
		int left = gui.getGuiLeft() + getX() + x;
		int top = gui.getGuiTop() + getY() + y;
		ScreenRect scissorArea = new ScreenRect(left + paddingLeft, top + paddingTop, 16, 16).transform(matrix3x2fStack);
		int scissorLeft = scissorArea.getLeft() * factor;
		int scissorTop = height - scissorArea.getBottom() * factor;
		int scissorWidth = Math.max(0, scissorArea.width() * factor);
		int scissorHeight = Math.max(0, scissorArea.height() * factor);
		RenderSystem.enableScissorForRenderTypeDraws(scissorLeft, scissorTop, scissorWidth, scissorHeight);

		matrixStack.push();
		matrixStack.translate(left + 8, top + 8, 0);
		matrixStack.scale(16F, 16F, 16F);
		matrixStack.translate(0.5F, 0.5F, 0);
		matrixStack.scale(-1, -1, 0);

		if (quaternion != null) {
			matrixStack.multiply(quaternion);
		}

		VertexConsumer buffer = vertexConsumers.getBuffer(layer);
		BlockModelRenderer.render(matrixStack.peek(), buffer, model, 1F, 1F, 1F, OverlayTexture.getU(15F), OverlayTexture.DEFAULT_UV);
		vertexConsumers.draw(layer);
		matrixStack.pop();
	}

	protected abstract int getPencilColor(String pencil);

	protected void drawPencil(DrawContext drawContext, GuiBase<?> gui, int mouseX, int mouseY, int x, int y) {
		int mx = mouseX - gui.getGuiLeft();
		int my = mouseY - gui.getGuiTop();
		x += 5;
		int color, x2, y2 = y + 13, x3, y3 = y + 3;
		TextRenderer textRenderer = gui.getTextRenderer();
		String pencil;
		Text letter;
		for (int i = 0, len = pencils.length; i < len; i++) {
			pencil = pencils[i];
			x2 = x + (i >= fixIndex ? pencilWidth - 1 : pencilWidth);
			if (pencil.equals(this.pencil)) {
				color = getPencilColor(pencil);
			} else if ((mx >= x && mx <= x2) && (my >= y && my < y2)) {
				drawContext.drawTooltip(textRenderer, Text.translatable("reborncore.gui.slotconfig." + pencil), mouseX, mouseY + 10);
				color = mx != x2 ? 0xff8b8b8b : 0x668b8b8b;
			} else {
				color = 0x668b8b8b;
			}
			drawContext.fill(x, y, x2, y2, color);
			letter = Text.of(pencil.substring(0, 1));
			x3 = x + (pencilWidth - textRenderer.getWidth(letter)) / 2;
			drawContext.drawText(textRenderer, letter, x3, y3, -1, false);
			x = x2 + 1;
		}
	}

	protected void fill(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, float x2, float y2, float x1, float y1, int color) {
		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
		float z = 0;
		VertexConsumer buffer = vertexConsumers.getBuffer(guiLayer);
		buffer.vertex(matrix4f, x1, y1, z).color(color);
		buffer.vertex(matrix4f, x1, y2, z).color(color);
		buffer.vertex(matrix4f, x2, y2, z).color(color);
		buffer.vertex(matrix4f, x2, y1, z).color(color);
	}

	protected void drawText(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, TextRenderer textRenderer, Text text, float x, float y, int color, boolean shadow) {
		textRenderer.draw(
			text.asOrderedText(), x, y, color, shadow, matrixStack.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 15728880
		);
	}
}
