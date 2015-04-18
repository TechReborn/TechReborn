package techreborn.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiManuel extends GuiScreen{
	
	public final int guiWidth = 256;
    public final int guiHeight = 180;
    private int guiLeft, guiTop;
    
	private static final ResourceLocation tomeGuipages = new ResourceLocation("techreborn:" + "textures/gui/manuel.png");

	public GuiManuel(EntityPlayer player) {}

	@Override
	public void initGui() 
	{
        this.guiLeft = this.width / 2 - this.guiWidth / 2;
        this.guiTop = this.height / 2 - this.guiHeight / 2;	
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
//        this.fontRendererObj.setUnicodeFlag(true);
        this.mc.getTextureManager().bindTexture(tomeGuipages);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.guiWidth, this.guiHeight);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

}
