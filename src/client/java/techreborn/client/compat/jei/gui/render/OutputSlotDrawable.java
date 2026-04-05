/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.client.compat.jei.gui.render;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import reborncore.client.gui.GuiSprites;

public record OutputSlotDrawable(boolean left, boolean center, boolean right) implements IDrawable {

	public static final SpriteDrawable SLOT_BAR_LEFT = new SpriteDrawable(GuiSprites.SLOT_BAR_RIGHT, 3, 26);
	public static final SpriteDrawable SLOT_BAR_CENTER = new SpriteDrawable(GuiSprites.SLOT_BAR_CENTER, 20, 26);
	public static final SpriteDrawable SLOT_BAR_RIGHT = new SpriteDrawable(GuiSprites.SLOT_BAR_LEFT, 3, 26);

	public static final OutputSlotDrawable SINGLE = new OutputSlotDrawable(true, true, true);
	public static final OutputSlotDrawable LEFT = new OutputSlotDrawable(true, true, false);
	public static final OutputSlotDrawable CENTER = new OutputSlotDrawable(false, true, false);
	public static final OutputSlotDrawable RIGHT = new OutputSlotDrawable(false, true, true);

	@Override
	public int getWidth() {
		return 26;
	}

	@Override
	public int getHeight() {
		return 26;
	}

	@Override
	public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
		if(left) {
			SLOT_BAR_LEFT.draw(guiGraphics, xOffset, yOffset);
		}
		if(center) {
			SLOT_BAR_CENTER.draw(guiGraphics, xOffset + 3, yOffset);
		}
		if(right) {
			SLOT_BAR_RIGHT.draw(guiGraphics, xOffset + 23, yOffset);
		}
	}
}
