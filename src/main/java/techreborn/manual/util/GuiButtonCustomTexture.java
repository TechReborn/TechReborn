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
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiButtonCustomTexture extends GuiButtonExt {
	public int textureU;
	public int textureV;
	public String texturename;
	public String LINKED_PAGE;
	public String NAME;
	public String imageprefix = "techreborn:textures/manual/elements/";
	public int buttonHeight;
	public int buttonWidth;
	public int buttonU;
	public int buttonV;
	public int textureH;
	public int textureW;

	public GuiButtonCustomTexture(int id, int xPos, int yPos, int u, int v, int buttonWidth, int buttonHeight,
	                              String texturename, String linkedPage, String name, int buttonU, int buttonV, int textureH, int textureW) {
		super(id, xPos, yPos, buttonWidth, buttonHeight, "_");
		this.textureU = u;
		this.textureV = v;
		this.texturename = texturename;
		this.NAME = name;
		this.LINKED_PAGE = linkedPage;
		this.buttonHeight = height;
		this.buttonWidth = width;
		this.buttonU = buttonU;
		this.buttonV = buttonV;
		this.textureH = textureH;
		this.textureW = textureW;
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
				&& mouseY < this.y + this.height;
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			int u = textureU;
			int v = textureV;

			if (flag) {
				u += width;
				GL11.glPushMatrix();
				GL11.glColor4f(0f, 0f, 0f, 1f);
				this.drawTexturedModalRect(this.x, this.y, u, v, width, height);
				GL11.glPopMatrix();
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(32826);
			RenderHelper.enableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			renderImage(this.x, this.y);
			this.drawString(mc.fontRenderer, this.NAME, this.x + 20, this.y + 3,
				Color.white.getRGB());
		}
	}

	public void renderImage(int offsetX, int offsetY) {
		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(new ResourceLocation(imageprefix + this.texturename + ".png"));

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(offsetX, offsetY, this.buttonU, this.buttonV, this.textureW, this.textureH);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public boolean getIsHovering() {
		return hovered;
	}
}
