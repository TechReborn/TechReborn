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
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiButtonTextOnly extends GuiButton {
	public String LINKED_PAGE;
	public int textColour;

	public GuiButtonTextOnly(int id, int xPos, int yPos, int width, int hight, String displayString, String linkedPage,
	                         int colour) {
		super(id, xPos, yPos, width, hight, displayString);
		this.LINKED_PAGE = linkedPage;
		this.textColour = colour;
	}

	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
		if (this.visible) {
			FontRenderer fontrenderer = minecraft.fontRendererObj;
			minecraft.getTextureManager().bindTexture(BUTTON_TEXTURES);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
				&& mouseY < this.yPosition + this.height;
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			this.mouseDragged(minecraft, mouseX, mouseY);

			String trimmedDisplayString = displayString;
			if (fontrenderer.getStringWidth(displayString) > width + 30 && !this.hovered) {
				int energencyBreak = 0;
				while (fontrenderer.getStringWidth(trimmedDisplayString) * 0.7 > width - 5) {
					trimmedDisplayString = trimmedDisplayString.substring(0, trimmedDisplayString.length() - 1);
					energencyBreak++;
					if (energencyBreak > 100)
						break;
				}
				trimmedDisplayString += "...";
			}

			if (this.hovered) {
				trimmedDisplayString = TextFormatting.BOLD + "" + TextFormatting.ITALIC + trimmedDisplayString;
				GL11.glPushMatrix();
				GL11.glColor4f(0f, 0f, 0f, 1f);
				drawTexturedModalRect(xPosition + (int) (xPosition * 0.01), yPosition + (int) (yPosition * 0.01), 0, 46,
					(int) (fontrenderer.getStringWidth(trimmedDisplayString) * 0.72) + 2, 8);
				GL11.glPopMatrix();
			}
			GL11.glPushMatrix();
			GL11.glScalef(0.7F, 0.7F, 1);
			fontrenderer.drawString(trimmedDisplayString, (int) (xPosition * 1.45),
				(int) ((yPosition + (height - 8) / 2) * 1.45), Color.WHITE.getRGB());
			GL11.glPopMatrix();
		}
	}

	public boolean getIsHovering() {
		return hovered;
	}
}
