package techreborn.pda;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.config.GuiButtonExt;

public class GuiButtonCustomTexture extends GuiButtonExt {

	public int textureU;
	public int textureV;
	public ResourceLocation texture;
	
	public GuiButtonCustomTexture(int id, int xPos, int yPos, int u, int v, int width,
			int height,  ResourceLocation loc) 
	{
		super(id, xPos, yPos, width, height, "_");
		textureU = u;
		textureV = v;
		texture = loc;
	}

     public void drawButton(Minecraft mc, int mouseX, int mouseY)
     {
         if (this.visible)
         {
        	 boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
             GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
             mc.getTextureManager().bindTexture(texture);
             int u = textureU;
             int v = textureV;

             if (flag)
             {
                 u += width;
             }

             this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, width, height);
         }
    }

}
