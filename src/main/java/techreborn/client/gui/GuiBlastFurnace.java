package techreborn.client.gui;

import codechicken.lib.gui.GuiDraw;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerBlastFurnace;
import techreborn.lib.ModInfo;
import techreborn.tiles.TileBlastFurnace;


public class GuiBlastFurnace extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/industrial_blast_furnace.png");

    TileBlastFurnace blastfurnace;

    ContainerBlastFurnace containerBlastFurnace;

	public GuiBlastFurnace(EntityPlayer player, TileBlastFurnace tileblastfurnace)
	{
		super(new ContainerBlastFurnace(tileblastfurnace, player));
		this.xSize = 176;
		this.ySize = 167;
		blastfurnace = tileblastfurnace;
        this.containerBlastFurnace = (ContainerBlastFurnace) this.inventorySlots;
	}

    @Override
    public void initGui() {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        super.initGui();
    }

    @Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		
        if(containerBlastFurnace.heat == 0)
        {
    		GuiDraw.drawTooltipBox(k + 30, l + 50 + 12 - 0, 114, 10);
    		this.fontRendererObj.drawString(ModInfo.MISSING_MULTIBLOCK, k + 38, l + 52 + 12 - 0, -1);
        }

        int j = 0;
        this.mc.getTextureManager().bindTexture(texture);
        j = blastfurnace.getProgressScaled(24);
        if(j > 0) {
            this.drawTexturedModalRect(k + 64, l + 37, 176, 14, j + 1, 16);
        }

        j = blastfurnace.getEnergyScaled(12);
        if(j > 0) {
            this.drawTexturedModalRect(k + 9, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
        }

	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
        super.drawGuiContainerForegroundLayer(p_146979_1_, p_146979_2_);
		String name = StatCollector.translateToLocal("tile.techreborn.blastfurnace.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        if(containerBlastFurnace.heat != 0){
            this.fontRendererObj.drawString("Current Heat: " + containerBlastFurnace.heat, 40, 60, 4210752);
        }
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
	}
}
