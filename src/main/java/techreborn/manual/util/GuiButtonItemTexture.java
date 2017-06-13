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
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiButtonItemTexture extends GuiButtonExt {

	public int textureU;
	public int textureV;
	public ItemStack itemstack;
	public String LINKED_PAGE;
	public String NAME;

	public GuiButtonItemTexture(int id, int xPos, int yPos, int u, int v, int width, int height, ItemStack stack,
	                            String linkedPage, String name) {
		super(id, xPos, yPos, width, height, "_");
		textureU = u;
		textureV = v;
		itemstack = stack;
		NAME = name;
		this.LINKED_PAGE = linkedPage;
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
				&& mouseY < this.y + this.height;
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			int u = textureU;
			int v = textureV;
			if (flag) {
				u += mc.fontRenderer.getStringWidth(this.NAME) + 25;
				v += mc.fontRenderer.getStringWidth(this.NAME) + 25;
				GL11.glPushMatrix();
				GL11.glColor4f(0f, 0f, 0f, 1f);
				this.drawTexturedModalRect(this.x, this.y, u, v, mc.fontRenderer.getStringWidth(this.NAME) + 25, height);
				GL11.glPopMatrix();
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(32826);
			RenderHelper.enableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
			itemRenderer.renderItemIntoGUI(itemstack, this.x, this.y);
			this.drawString(mc.fontRenderer, this.NAME, this.x + 20, this.y + 3,
				Color.white.getRGB());
		}
	}

	public boolean getIsHovering() {
		return hovered;
	}

}
