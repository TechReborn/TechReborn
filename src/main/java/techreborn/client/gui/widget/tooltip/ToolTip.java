/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.client.gui.widget.tooltip;

import net.minecraft.client.gui.FontRenderer;
import reborncore.client.gui.GuiUtil;

import java.util.ArrayList;
import java.util.Collections;

public class ToolTip {

	protected ArrayList<ToolTipLine> lines = new ArrayList<>();

	public ToolTip(String... textLines) {
		for (String text : textLines)
			lines.add(new ToolTipLine(text));
	}

	public ToolTip(ToolTipLine... toolTipLines) {
		Collections.addAll(lines, toolTipLines);
	}

	public ToolTip(int linesSize) {
		for (int i = 0; i < linesSize; i++)
			lines.add(new ToolTipLine());
	}

	public void addLine(ToolTipLine toolTipLine) {
		lines.add(toolTipLine);
	}

	public void removeLine(int index) {
		lines.remove(index);
	}

	public ToolTipLine getLine(int index) {
		return lines.get(index);
	}

	public ArrayList<ToolTipLine> getLines() {
		return lines;
	}

	protected void refresh() {}

	public void draw(FontRenderer font, int mouseX, int mouseY) {
		refresh();
		int maxLineLength = 0;
		int textX = mouseX + 3;
		int textY = mouseY + 3;
		for (ToolTipLine toolTipLine : lines) {
			toolTipLine.draw(font, textX, textY);
			textY += (font.FONT_HEIGHT + 3);
			int lineWidth = toolTipLine.getWidth(font);
			if (lineWidth > maxLineLength)
				maxLineLength = lineWidth;
		}
		GuiUtil.drawTooltipBox(mouseX, mouseY, maxLineLength, textY + 3);
	}

}
