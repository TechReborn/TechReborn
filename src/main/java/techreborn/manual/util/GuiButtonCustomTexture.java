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
			boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
				&& mouseY < this.yPosition + this.height;
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			int u = textureU;
			int v = textureV;

			if (flag) {
				u += width;
				GL11.glPushMatrix();
				GL11.glColor4f(0f, 0f, 0f, 1f);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, width, height);
				GL11.glPopMatrix();
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(32826);
			RenderHelper.enableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			renderImage(this.xPosition, this.yPosition);
			this.drawString(mc.fontRendererObj, this.NAME, this.xPosition + 20, this.yPosition + 3,
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
