package techreborn.pda.util;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiButtonCustomTexture extends GuiButtonExt {

    public int textureU;
    public int textureV;
    public ItemStack itemstack;
	public String LINKED_PAGE;
    
    public GuiButtonCustomTexture(int id, int xPos, int yPos, int u, int v, int width, int height, ItemStack stack, String linkedPage) {
        super(id, xPos, yPos, width, height, "_");
        textureU = u;
        textureV = v;
        itemstack = stack;
		this.LINKED_PAGE = linkedPage;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition
                    && mouseX < this.xPosition + this.width
                    && mouseY < this.yPosition + this.height;
			mc.getTextureManager().bindTexture(buttonTextures);
            int u = textureU;
            int v = textureV;

            if (flag) {
                u += width;
				GL11.glPushMatrix();
				GL11.glColor4f(0f, 0f, 0f, 1f);
                this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, width, height);
				GL11.glPopMatrix();
            }
            RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, this.xPosition, this.yPosition);
            this.drawString(mc.fontRenderer, itemstack.getDisplayName(), this.xPosition + 20, this.yPosition + 3, 515611);
        }
    }
}
