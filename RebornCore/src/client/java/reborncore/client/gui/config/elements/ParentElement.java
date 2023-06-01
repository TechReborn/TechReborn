/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2023 TeamReborn
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

import com.google.common.collect.Lists;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.SpriteIdentifier;
import reborncore.client.gui.GuiBase;

import java.util.ArrayList;
import java.util.List;

public abstract class ParentElement extends ElementBase {
	protected final List<ElementBase> elements = new ArrayList<>();

	public ParentElement(int x, int y, SpriteIdentifier sprite) {
		super(x, y, sprite);
	}

	@Override
	public void draw(DrawContext drawContext, GuiBase<?> gui, int mouseX, int mouseY) {
		super.draw(drawContext, gui, mouseX, mouseY);
		elements.forEach(elementBase -> elementBase.draw(drawContext, gui, mouseX, mouseY));
	}

	@Override
	public boolean onClick(GuiBase<?> gui, double mouseX, double mouseY) {
		for (ElementBase element : Lists.reverse(elements)) {
			if (element.isMouseWithinRect(gui, mouseX, mouseY)) {
				if (element.onClick(gui, mouseX, mouseY)) {
					return true;
				}
			}
		}

		return super.onClick(gui, mouseX, mouseY);
	}
}
