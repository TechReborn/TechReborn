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

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import reborncore.common.misc.TriConsumer;


public class GuiButtonExtended extends Button {

	private TriConsumer<GuiButtonExtended, Double, Double> clickHandler;

	public GuiButtonExtended(int x, int y, Component buttonText, Button.OnPress pressAction) {
		super(x, y, 20, 200, buttonText, pressAction, Button.DEFAULT_NARRATION);
	}

	public GuiButtonExtended(int x, int y, int widthIn, int heightIn, Component buttonText, Button.OnPress pressAction) {
		super(x, y, widthIn, heightIn, buttonText, pressAction, Button.DEFAULT_NARRATION);
	}

	public GuiButtonExtended clickHandler(TriConsumer<GuiButtonExtended, Double, Double> consumer) {
		clickHandler = consumer;
		return this;
	}

	@Override
	protected void renderContents(GuiGraphics guiGraphics, int i, int j, float f) {

	}

	@Override
	public void onClick(MouseButtonEvent mouse, boolean doubled) {
		if (clickHandler != null) {
			clickHandler.accept(this, mouse.x(), mouse.y());
		}
		super.onClick(mouse, doubled);
	}
}