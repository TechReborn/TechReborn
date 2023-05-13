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

package reborncore.client.gui.builder.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import reborncore.client.gui.builder.GuiBase;

/**
 * @author drcrazy
 */
public class GuiButtonUpDown extends GuiButtonExtended {

	GuiBase<?> gui;
	UpDownButtonType type;

	public GuiButtonUpDown(int x, int y, GuiBase<?> gui, ButtonWidget.PressAction pressAction, UpDownButtonType type) {
		super(x, y, 12, 12, Text.empty(), pressAction);
		this.gui = gui;
		this.type = type;
	}

	@Override
	public void renderButton(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
		if (gui.hideGuiElements()) return;
		switch (type) {
			case FASTFORWARD -> drawContext.drawTexture(gui.builder.getResourceLocation(), getX(), getY(), 174, 74, 12, 12);
			case FORWARD -> drawContext.drawTexture(gui.builder.getResourceLocation(), getX(), getY(), 174, 86, 12, 12);
			case REWIND -> drawContext.drawTexture(gui.builder.getResourceLocation(), getX(), getY(), 174, 98, 12, 12);
			case FASTREWIND -> drawContext.drawTexture(gui.builder.getResourceLocation(), getX(), getY(), 174, 110, 12, 12);
		}
	}

	public enum UpDownButtonType {
		FASTFORWARD,
		FORWARD,
		REWIND,
		FASTREWIND
	}
}
