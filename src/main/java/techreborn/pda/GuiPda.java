package techreborn.pda;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import techreborn.client.container.ContainerPda;
import techreborn.cofhLib.gui.GuiBase;
import techreborn.cofhLib.gui.GuiColor;
import techreborn.cofhLib.gui.element.ElementSlider;

public class GuiPda extends GuiBase {

	public final int guiHeight = 256;
	public final int guiWidth = 256;
	private int guiLeft, guiTop;
	private ElementSlider slider;

	private static final ResourceLocation pdaGuipages = new ResourceLocation("techreborn:" + "textures/gui/pda.png");

	public GuiPda(EntityPlayer player, ContainerPda container)
	{
		super(container);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.guiLeft = this.width / 2 - this.guiWidth / 2;
		this.guiTop = this.height / 2 - this.guiHeight / 2;
		//TODO fix
        slider = new ElementSlider(this, "scrollBar", 239, 36, 12, 201, 187, 0)
        {
            @Override
            protected void dragSlider(int x, int y)
            {
                if (y > _value)
                {
                    setValue(_value + 1);
                }
                else
                {
                    setValue(_value - 1);
                }
            }
        };
        
        slider.backgroundColor = new GuiColor(0, 0, 0, 0).getColor();
        slider.borderColor = new GuiColor(0, 0, 0, 0).getColor();
        slider.setSliderSize(12, 15);
        addElement(slider);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		mc.getTextureManager().bindTexture(pdaGuipages);
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.guiWidth,this.guiHeight);
//		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

}
