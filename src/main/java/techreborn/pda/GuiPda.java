package techreborn.pda;

import techreborn.init.ModBlocks;
import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiPda extends GuiScreen{
	
	public final int guiWidth = 256;
    public final int guiHeight = 180;
    private int guiLeft, guiTop;
    
	private static final ResourceLocation pdaGuipages = new ResourceLocation("techreborn:" + "textures/gui/pda.png");
	public static final ResourceLocation buttonOre = new ResourceLocation("techreborn:" + "textures/blocks/ore/book_of_revealing");

	public GuiPda(EntityPlayer player) {}

	@Override
	public void initGui() 
	{
		super.initGui();
        this.guiLeft = this.width / 2 - this.guiWidth / 2;
        this.guiTop = this.height / 2 - this.guiHeight / 2;	
		GuiButtonCustomTexture oresButton = new GuiButtonCustomTexture(1, guiLeft +20, guiTop +20, 0, 224, 16, 16, buttonOre);

        buttonList.add(oresButton);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		
        mc.getTextureManager().bindTexture(pdaGuipages);
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.guiWidth, this.guiHeight);
        mc.renderEngine.bindTexture(buttonOre);
        
		super.drawScreen(mouseX, mouseY, partialTicks);
	}


}
