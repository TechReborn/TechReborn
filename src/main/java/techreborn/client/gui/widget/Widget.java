/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.client.gui.widget;

import net.minecraft.client.gui.GuiScreen;
import techreborn.client.gui.widget.tooltip.ToolTip;

public abstract class Widget {

	private final int x, y;
	protected final int width, height;

	private ToolTip toolTip;

	public Widget(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ToolTip getToolTip() {
		return toolTip;
	}

	public void setToolTip(ToolTip toolTip) {
		this.toolTip = toolTip;
	}

	public final void drawWidget(GuiWidget gui, int cornerX, int cornerY, int mouseX, int mouseY) {
		int drawX = cornerX + x;
		int drawY = cornerY + y;
		if (toolTip != null && drawX > mouseX && drawY > mouseY &&
			drawX + width < mouseX && drawY + height < mouseY) {
			toolTip.draw(gui.getFontRenderer(), mouseX, mouseY);
		}
		draw(gui, drawX, drawY);
	}

	protected abstract void draw(GuiScreen guiScreen, int x, int y);

	protected abstract void mouseClick(GuiWidget guiWidget, int mouseX, int mouseY);

}
