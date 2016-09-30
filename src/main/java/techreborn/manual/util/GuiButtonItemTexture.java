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
			boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
				&& mouseY < this.yPosition + this.height;
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			int u = textureU;
			int v = textureV;
			if (flag) {
				u += mc.fontRendererObj.getStringWidth(this.NAME) + 25;
				v += mc.fontRendererObj.getStringWidth(this.NAME) + 25;
				GL11.glPushMatrix();
				GL11.glColor4f(0f, 0f, 0f, 1f);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, mc.fontRendererObj.getStringWidth(this.NAME) + 25, height);
				GL11.glPopMatrix();
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(32826);
			RenderHelper.enableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
			itemRenderer.renderItemIntoGUI(itemstack, this.xPosition, this.yPosition);
			this.drawString(mc.fontRendererObj, this.NAME, this.xPosition + 20, this.yPosition + 3,
				Color.white.getRGB());
		}
	}

	public boolean getIsHovering() {
		return hovered;
	}

}
