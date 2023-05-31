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

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class ElementBase {
	private final int x;
	private final int y;
	public boolean isHovering = false;
	public boolean isPressing = false;
	public boolean isReleasing = false;
	public List<ElementBase.Action> pressActions = new ArrayList<>();
	public List<ElementBase.Action> releaseActions = new ArrayList<>();
	public SpriteContainer container;

	private int width;
	private int height;

	public static final Identifier MECH_ELEMENTS = new Identifier("reborncore", "textures/gui/elements.png");

	public ElementBase(int x, int y, ISprite... sprites) {
		this.container = new SpriteContainer();
		for (ISprite sprite : sprites) {
			container.addSprite(sprite);
		}
		this.x = x;
		this.y = y;
	}

	public SpriteContainer getSpriteContainer() {
		return container;
	}

	public void adjustDimensions(MachineBaseBlockEntity provider) {
		if (container.offsetSprites != null) {
			for (OffsetSprite offsetSprite : container.offsetSprites) {
				if (offsetSprite.getSprite().getSprite(provider).width + offsetSprite.getOffsetX(provider) > this.width) {
					this.width = offsetSprite.getSprite().getSprite(provider).width + offsetSprite.getOffsetX(provider);
				}
				if (offsetSprite.getSprite().getSprite(provider).height + offsetSprite.getOffsetY(provider) > this.height) {
					this.height = offsetSprite.getSprite().getSprite(provider).height + offsetSprite.getOffsetY(provider);
				}
			}
		}
	}

	public void draw(DrawContext drawContext, GuiBase<?> gui) {
		for (OffsetSprite sprite : getSpriteContainer().offsetSprites) {
			drawSprite(drawContext, gui, sprite.getSprite(), x + sprite.getOffsetX(gui.getMachine()), y + sprite.getOffsetY(gui.getMachine()));
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth(MachineBaseBlockEntity provider) {
		adjustDimensions(provider);
		return width;
	}

	public int getHeight(MachineBaseBlockEntity provider) {
		adjustDimensions(provider);
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ElementBase addPressAction(ElementBase.Action action) {
		this.pressActions.add(action);
		return this;
	}

	public ElementBase addReleaseAction(ElementBase.Action action) {
		this.releaseActions.add(action);
		return this;
	}

	public boolean onRelease(MachineBaseBlockEntity provider, GuiBase<?> gui, double mouseX, double mouseY) {
		for (ElementBase.Action action : releaseActions) {
			if (action.execute(this, gui, provider, mouseX, mouseY)) {
				return true;
			}
		}
		if (isPressing) {
			for (ElementBase.Action action : pressActions) {
				action.execute(this, gui, provider, mouseX, mouseY);
			}
		}
		return !releaseActions.isEmpty() || !pressActions.isEmpty();
	}

	public interface Action {
		boolean execute(ElementBase element, GuiBase<?> gui, MachineBaseBlockEntity provider, double mouseX, double mouseY);
	}

	public interface UpdateAction {
		void update(GuiBase<?> gui, ElementBase element);
	}

	public int adjustX(GuiBase<?> gui, int x) {
		return gui.getGuiLeft() + x;
	}

	public int adjustY(GuiBase<?> gui, int y) {
		return gui.getGuiTop() + y;
	}

	public boolean isInRect(GuiBase<?> gui, int x, int y, int xSize, int ySize, double mouseX, double mouseY) {
		return gui.isPointInRect(x + gui.getGuiLeft(), y + gui.getGuiTop(), xSize, ySize, mouseX, mouseY);
	}

	public void drawText(DrawContext drawContext, GuiBase<?> gui, Text text, int x, int y, int color) {
		x = adjustX(gui, x);
		y = adjustY(gui, y);
		drawContext.drawText(gui.getTextRenderer(), text, x, y, color, false);
	}

	public void drawSprite(DrawContext drawContext, GuiBase<?> gui, ISprite iSprite, int x, int y) {
		Sprite sprite = iSprite.getSprite(gui.getMachine());
		if (sprite != null) {
			if (sprite.hasTextureInfo()) {
				drawContext.drawTexture(sprite.textureLocation, x + gui.getGuiLeft(), y + gui.getGuiTop(), sprite.x, sprite.y, sprite.width, sprite.height);
			}
			if (sprite.hasStack()) {
				drawContext.drawItem(sprite.itemStack, x + gui.getGuiLeft(), y + gui.getGuiTop());
			}
		}
	}

	public void drawDefaultBackground(DrawContext drawContext, Screen gui, int x, int y, int width, int height) {
		drawContext.drawTexture(GuiBuilder.defaultTextureSheet, x, y, 0, 0, width / 2, height / 2);
		drawContext.drawTexture(GuiBuilder.defaultTextureSheet, x + width / 2, y, 150 - width / 2, 0, width / 2, height / 2);
		drawContext.drawTexture(GuiBuilder.defaultTextureSheet, x, y + height / 2, 0, 150 - height / 2, width / 2, height / 2);
		drawContext.drawTexture(GuiBuilder.defaultTextureSheet, x + width / 2, y + height / 2, 150 - width / 2, 150 - height / 2, width / 2, height / 2);
	}
}
