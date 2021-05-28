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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import reborncore.common.util.Color;

public class GuiButtonCustomTexture extends ButtonWidget {
	public int textureU;
	public int textureV;
	public String texturename;
	public String linkedPage;
	public Text name;
	public String imageprefix = "techreborn:textures/manual/elements/";
	public int buttonHeight;
	public int buttonWidth;
	public int buttonU;
	public int buttonV;
	public int textureH;
	public int textureW;

	public GuiButtonCustomTexture(int xPos, int yPos, int u, int v, int buttonWidth, int buttonHeight,
								  String texturename, String linkedPage, Text name, int buttonU, int buttonV, int textureH, int textureW, ButtonWidget.PressAction pressAction) {
		super(xPos, yPos, buttonWidth, buttonHeight, LiteralText.EMPTY, pressAction);
		this.textureU = u;
		this.textureV = v;
		this.texturename = texturename;
		this.name = name;
		this.linkedPage = linkedPage;
		this.buttonHeight = height;
		this.buttonWidth = width;
		this.buttonU = buttonU;
		this.buttonV = buttonV;
		this.textureH = textureH;
		this.textureW = textureW;
	}

	public void drawButton(MatrixStack matrixStack, MinecraftClient mc, int mouseX, int mouseY) {
		if (this.visible) {
			boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
					&& mouseY < this.y + this.height;
			mc.getTextureManager().bindTexture(WIDGETS_LOCATION);
			int u = textureU;
			int v = textureV;

			if (flag) {
				u += width;
				GL11.glPushMatrix();
				GL11.glColor4f(0f, 0f, 0f, 1f);
				this.drawTexture(matrixStack, this.x, this.y, u, v, width, height);
				GL11.glPopMatrix();
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(32826);
			DiffuseLighting.enable();
			renderImage(matrixStack, this.x, this.y);
			this.drawTextWithShadow(matrixStack, mc.textRenderer, this.name, this.x + 20, this.y + 3,
					Color.WHITE.getColor());
		}
	}

	public void renderImage(MatrixStack matrixStack, int offsetX, int offsetY) {
		TextureManager render = MinecraftClient.getInstance().getTextureManager();
		render.bindTexture(new Identifier(imageprefix + this.texturename + ".png"));

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexture(matrixStack, offsetX, offsetY, this.buttonU, this.buttonV, this.textureW, this.textureH);
		GL11.glDisable(GL11.GL_BLEND);
	}

}
