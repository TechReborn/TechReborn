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

package reborncore.client.gui;

import net.minecraft.client.util.math.MatrixStack;
import reborncore.client.RenderUtil;

public class GuiUtil {
	public static void drawTooltipBox(MatrixStack matrices, int x, int y, int w, int h) {
		int bg = 0xf0100010;
		drawGradientRect(matrices, x + 1, y, w - 1, 1, bg, bg);
		drawGradientRect(matrices, x + 1, y + h, w - 1, 1, bg, bg);
		drawGradientRect(matrices, x + 1, y + 1, w - 1, h - 1, bg, bg);// center
		drawGradientRect(matrices, x, y + 1, 1, h - 1, bg, bg);
		drawGradientRect(matrices, x + w, y + 1, 1, h - 1, bg, bg);
		int grad1 = 0x505000ff;
		int grad2 = 0x5028007F;
		drawGradientRect(matrices, x + 1, y + 2, 1, h - 3, grad1, grad2);
		drawGradientRect(matrices, x + w - 1, y + 2, 1, h - 3, grad1, grad2);

		drawGradientRect(matrices, x + 1, y + 1, w - 1, 1, grad1, grad1);
		drawGradientRect(matrices, x + 1, y + h - 1, w - 1, 1, grad2, grad2);
	}

	public static void drawGradientRect(MatrixStack matrices, int x, int y, int w, int h, int colour1, int colour2) {
		RenderUtil.drawGradientRect(matrices, 0, x, y, x + w, y + h, colour1, colour2);
	}

}
