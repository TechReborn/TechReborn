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

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.element.MachineFaceState;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.MachineFacing;

import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractConfigPopupElement extends ElementBase {
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

	public AbstractConfigPopupElement(int x, int y, int height, Material sprite, int textureWidth, int textureHeight, String[] pencils) {
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
	public final void draw(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int mouseX, int mouseY) {
		drawContext.pose().pushMatrix();
		int x = adjustX(gui, getX() - 8);
		int y = adjustY(gui, getY() - 7);
		gui.builder.drawDefaultBackground(
			drawContext,
			x,
			y,
			84,
			height
		);
		drawContext.pose().popMatrix();

		super.draw(drawContext, gui, mouseX, mouseY);

		Minecraft client = Minecraft.getInstance();
		final MachineBaseBlockEntity machine = ((MachineBaseBlockEntity) gui.be);
		final BlockState state = machine.getBlockState();
		final BlockState defaultState = state.getBlock().defaultBlockState();
		final BlockRenderDispatcher dispatcher = client.getBlockRenderer();
		final BlockStateModel model = dispatcher.getBlockModelShaper().getBlockModel(defaultState);

		drawContext.guiRenderState.submitPicturesInPictureState(new MachineFaceState(
			new Matrix3x2f(drawContext.pose()),
			model,
			gui.getGuiLeft() + getX(),
			gui.getGuiTop() + getY()
		));

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

		drawSateColor(drawContext, gui, MachineFacing.UP.getFacing(machine), 22, -1);
		drawSateColor(drawContext, gui, MachineFacing.FRONT.getFacing(machine), 22, 18);
		drawSateColor(drawContext, gui, MachineFacing.DOWN.getFacing(machine), 22, 37);
		drawSateColor(drawContext, gui, MachineFacing.RIGHT.getFacing(machine), 41, 18);
		drawSateColor(drawContext, gui, MachineFacing.BACK.getFacing(machine), 41, 37);
		drawSateColor(drawContext, gui, MachineFacing.LEFT.getFacing(machine), 3, 18);

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

	protected abstract void drawSateColor(GuiGraphicsExtractor drawContext, GuiBase<?> gui, Direction side, int inx, int iny);

	protected boolean isInBox(int rectX, int rectY, int rectWidth, int rectHeight, double pointX, double pointY, GuiBase<?> guiBase) {
		rectX += getX();
		rectY += getY();
		return isInRect(guiBase, rectX, rectY, rectWidth, rectHeight, pointX, pointY);
		//return (pointX - guiBase.getGuiLeft()) >= rectX - 1 && (pointX - guiBase.getGuiLeft()) < rectX + rectWidth + 1 && (pointY - guiBase.getGuiTop()) >= rectY - 1 && (pointY - guiBase.getGuiTop()) < rectY + rectHeight + 1;
	}

	protected abstract int getPencilColor(String pencil);

	protected void drawPencil(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int mouseX, int mouseY, int x, int y) {
		int mx = mouseX - gui.getGuiLeft();
		int my = mouseY - gui.getGuiTop();
		x += 5;
		int color, x2, y2 = y + 13, x3, y3 = y + 3;
		Font textRenderer = gui.getFont();
		String pencil;
		Component letter;
		for (int i = 0, len = pencils.length; i < len; i++) {
			pencil = pencils[i];
			x2 = x + (i >= fixIndex ? pencilWidth - 1 : pencilWidth);
			if (pencil.equals(this.pencil)) {
				color = getPencilColor(pencil);
			} else if ((mx >= x && mx <= x2) && (my >= y && my < y2)) {
				drawContext.setTooltipForNextFrame(textRenderer, Component.translatable("reborncore.gui.slotconfig." + pencil), mouseX, mouseY + 10);
				color = mx != x2 ? 0xff8b8b8b : 0x668b8b8b;
			} else {
				color = 0x668b8b8b;
			}
			drawContext.fill(x, y, x2, y2, color);
			letter = Component.nullToEmpty(pencil.substring(0, 1));
			x3 = x + (pencilWidth - textRenderer.width(letter)) / 2;
			drawContext.drawString(textRenderer, letter, x3, y3, -1, false);
			x = x2 + 1;
		}
	}
}
