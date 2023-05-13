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

package reborncore.client.gui.builder.slot.elements;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import reborncore.client.gui.builder.GuiBase;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.MachineFacing;

public abstract class AbstractConfigPopupElement extends ElementBase {
	public boolean filter = false;

	public AbstractConfigPopupElement(int x, int y, ISprite... sprites) {
		super(x, y, sprites);
	}

	@Override
	public final void draw(DrawContext drawContext, GuiBase<?> gui) {
		drawDefaultBackground(drawContext, gui, adjustX(gui, getX() - 8), adjustY(gui, getY() - 7), 84, 105 + (filter ? 15 : 0));
		super.draw(drawContext, gui);

		final MachineBaseBlockEntity machine = ((MachineBaseBlockEntity) gui.be);
		final BlockState state = machine.getCachedState();
		final BlockState defaultState = state.getBlock().getDefaultState();
		final BlockRenderManager dispatcher = MinecraftClient.getInstance().getBlockRenderManager();
		final BakedModel model = dispatcher.getModels().getModel(defaultState);

		drawState(drawContext, gui, model, defaultState, dispatcher, 4, 23, RotationAxis.POSITIVE_Y.rotationDegrees(90F)); //left
		drawState(drawContext, gui, model, defaultState, dispatcher, 23, 4, RotationAxis.NEGATIVE_X.rotationDegrees(90F)); //top
		drawState(drawContext, gui, model, defaultState, dispatcher, 23, 23, null); //centre
		drawState(drawContext, gui, model, defaultState, dispatcher, 23, 26, RotationAxis.POSITIVE_X.rotationDegrees(90F)); //bottom
		drawState(drawContext, gui, model, defaultState, dispatcher, 42, 23, RotationAxis.POSITIVE_Y.rotationDegrees(90F)); //right
		drawState(drawContext, gui, model, defaultState, dispatcher, 26, 42, RotationAxis.POSITIVE_Y.rotationDegrees(180F)); //back

		drawSateColor(drawContext, gui, MachineFacing.UP.getFacing(machine), 22, -1);
		drawSateColor(drawContext, gui, MachineFacing.FRONT.getFacing(machine), 22, 18);
		drawSateColor(drawContext, gui, MachineFacing.DOWN.getFacing(machine), 22, 37);
		drawSateColor(drawContext, gui, MachineFacing.RIGHT.getFacing(machine), 41, 18);
		drawSateColor(drawContext, gui, MachineFacing.BACK.getFacing(machine), 41, 37);
		drawSateColor(drawContext, gui, MachineFacing.LEFT.getFacing(machine), 3, 18);
	}

	@Override
	public final boolean onRelease(MachineBaseBlockEntity provider, GuiBase<?> gui, double mouseX, double mouseY) {
		if (isInBox(23, 4, 16, 16, mouseX, mouseY, gui)) {
			cycleConfig(MachineFacing.UP.getFacing(provider), gui);
		} else if (isInBox(23, 23, 16, 16, mouseX, mouseY, gui)) {
			cycleConfig(MachineFacing.FRONT.getFacing(provider), gui);
		} else if (isInBox(42, 23, 16, 16, mouseX, mouseY, gui)) {
			cycleConfig(MachineFacing.RIGHT.getFacing(provider), gui);
		} else if (isInBox(4, 23, 16, 16, mouseX, mouseY, gui)) {
			cycleConfig(MachineFacing.LEFT.getFacing(provider), gui);
		} else if (isInBox(23, 42, 16, 16, mouseX, mouseY, gui)) {
			cycleConfig(MachineFacing.DOWN.getFacing(provider), gui);
		} else if (isInBox(42, 42, 16, 16, mouseX, mouseY, gui)) {
			cycleConfig(MachineFacing.BACK.getFacing(provider), gui);
		} else {
			return false;
		}
		return true;
	}

	protected abstract void cycleConfig(Direction side, GuiBase<?> guiBase);

	protected abstract void drawSateColor(DrawContext drawContext, GuiBase<?> gui, Direction side, int inx, int iny);

	protected boolean isInBox(int rectX, int rectY, int rectWidth, int rectHeight, double pointX, double pointY, GuiBase<?> guiBase) {
		rectX += getX();
		rectY += getY();
		return isInRect(guiBase, rectX, rectY, rectWidth, rectHeight, pointX, pointY);
		//return (pointX - guiBase.getGuiLeft()) >= rectX - 1 && (pointX - guiBase.getGuiLeft()) < rectX + rectWidth + 1 && (pointY - guiBase.getGuiTop()) >= rectY - 1 && (pointY - guiBase.getGuiTop()) < rectY + rectHeight + 1;
	}

	protected void drawState(DrawContext drawContext,
						  GuiBase<?> gui,
						  BakedModel model,
						  BlockState actualState,
						  BlockRenderManager dispatcher,
						  int x,
						  int y,
						  Quaternionf quaternion) {
		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();
		matrixStack.translate(8 + gui.getGuiLeft() + this.x + x, 8 + gui.getGuiTop() + this.y + y, 0);
		matrixStack.scale(16F, 16F, 16F);
		matrixStack.translate(0.5F, 0.5F, 0);
		matrixStack.scale(-1, -1, 0);

		if (quaternion != null) {
			matrixStack.multiply(quaternion);
		}

		VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
		dispatcher.getModelRenderer().render(matrixStack.peek(), immediate.getBuffer(RenderLayer.getSolid()), actualState, model, 1F, 1F, 1F, OverlayTexture.getU(15F), OverlayTexture.DEFAULT_UV);
		immediate.draw();
		matrixStack.pop();
	}
}
