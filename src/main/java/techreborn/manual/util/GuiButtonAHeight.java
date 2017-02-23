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

package techreborn.manual.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

public class GuiButtonAHeight extends GuiButton {

	public GuiButtonAHeight(int id, int xPos, int yPos, int width, int hight, String displayString) {
		super(id, xPos, yPos, width, hight, displayString);
	}

	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
		if (this.visible) {
			FontRenderer fontrenderer = minecraft.fontRendererObj;
			minecraft.getTextureManager().bindTexture(BUTTON_TEXTURES);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
				&& mouseY < this.yPosition + this.height;
			int k = this.getHoverState(this.hovered);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2,
				46 + k * 20, this.width / 2, this.height);

			if (this.height < 20) {
				this.drawTexturedModalRect(xPosition, yPosition + 3, 0, (46 + k * 20) + 20 - height + 3, width / 2,
					height - 3);
				this.drawTexturedModalRect(xPosition + width / 2, yPosition + 3, 200 - width / 2,
					(46 + k * 20) + 20 - height + 3, width / 2, height - 3);
			}

			this.mouseDragged(minecraft, mouseX, mouseY);
			int l = 14737632;

			if (packedFGColour != 0) {
				l = packedFGColour;
			} else if (!this.enabled) {
				l = 10526880;
			} else if (this.hovered) {
				l = 16777120;
			}
			this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2,
				this.yPosition + (this.height - 8) / 2, l);
		}
	}
}
