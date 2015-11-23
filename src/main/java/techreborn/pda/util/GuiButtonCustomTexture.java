package techreborn.pda.util;

import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiButtonCustomTexture extends GuiButtonExt {

    public int textureU;
    public int textureV;
    public ItemStack itemstack;
    public String LINKED_PAGE;
    public String NAME;

    public GuiButtonCustomTexture(int id, int xPos, int yPos, int u, int v, int width, int height, ItemStack stack, String linkedPage, String name) {
        super(id, xPos, yPos, width, height, "_");
        textureU = u;
        textureV = v;
        itemstack = stack;
        NAME = name;
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
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(32826);
            RenderHelper.enableStandardItemLighting();
            RenderHelper.enableGUIStandardItemLighting();
            //RenderItem.getInstance().renderItemIntoGUI(mc.fontRendererObj, mc.renderEngine, itemstack, this.xPosition, this.yPosition); //TODO 1.8
            this.drawString(mc.fontRendererObj, this.NAME, this.xPosition + 20, this.yPosition + 3, Color.white.getRGB());
        }
    }

    public boolean getIsHovering() {
        return hovered;
    }

}
