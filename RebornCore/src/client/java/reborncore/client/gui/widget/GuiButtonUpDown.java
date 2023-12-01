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

package reborncore.client.gui.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.text.Text;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiSprites;

import static reborncore.client.gui.GuiSprites.drawSprite;

/**
 * @author drcrazy
 */
public class GuiButtonUpDown extends GuiButtonExtended {
	private final GuiBase<?> gui;
	private final UpDownButtonType type;

	public GuiButtonUpDown(int x, int y, GuiBase<?> gui, ButtonWidget.PressAction pressAction, UpDownButtonType type) {
		super(x, y, 12, 12, Text.empty(), pressAction);
		this.gui = gui;
		this.type = type;
	}

	@Override
	public void renderWidget(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
		if (gui.hideGuiElements()) return;
		drawSprite(drawContext, type.spriteIdentifier, getX(), getY());
	}

	public enum UpDownButtonType {
		FASTFORWARD(GuiSprites.FAST_FORWARD),
		FORWARD(GuiSprites.FORWARD),
		REWIND(GuiSprites.REWIND),
		FASTREWIND(GuiSprites.FAST_REWIND);

		private final SpriteIdentifier spriteIdentifier;

		UpDownButtonType(SpriteIdentifier spriteIdentifier) {
			this.spriteIdentifier = spriteIdentifier;
		}
	}
}
