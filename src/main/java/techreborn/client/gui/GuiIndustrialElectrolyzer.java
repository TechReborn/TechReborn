package techreborn.client.gui;

import ic2.core.util.DrawUtil;
import codechicken.lib.gui.GuiDraw;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerIndustrialElectrolyzer;
import techreborn.lib.ModInfo;
import techreborn.tiles.TileIndustrialElectrolyzer;

public class GuiIndustrialElectrolyzer extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/industrial_electrolyzer.png");

	TileIndustrialElectrolyzer eletrolyzer;

	public GuiIndustrialElectrolyzer(EntityPlayer player,
			TileIndustrialElectrolyzer tileeletrolyzer)
	{
		super(new ContainerIndustrialElectrolyzer(tileeletrolyzer, player));
		this.xSize = 176;
		this.ySize = 167;
		eletrolyzer = tileeletrolyzer;
	}
	
    @Override
    public void initGui() {
//        this.buttonList.clear();
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
//        this.buttonList.add(new GuiButton(0, k + 4,  l + 4, 20, 20, "R"));
        super.initGui();
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		
		if (eletrolyzer.tank.getFluidAmount() != 0)
		{
			IIcon fluidIcon = eletrolyzer.tank.getFluid().getFluid().getIcon();
			if (fluidIcon != null)
			{
				drawTexturedModalRect(k + 7, l + 15, 176, 31, 20, 55);

				this.mc.renderEngine
						.bindTexture(TextureMap.locationBlocksTexture);
				int liquidHeight = eletrolyzer.tank.getFluidAmount() * 47
						/ eletrolyzer.tank.getCapacity();
				DrawUtil.drawRepeated(fluidIcon, k + 11, l + 19 + 47
						- liquidHeight, 12.0D, liquidHeight, this.zLevel);

				this.mc.renderEngine.bindTexture(texture);
				drawTexturedModalRect(k + 11, l + 19, 176, 86, 12, 47);
			}
		}
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_,
			int p_146979_2_)
	{
		String name = StatCollector.translateToLocal("tile.techreborn.industrialelectrolyzer.name");
		this.fontRendererObj.drawString(name, (this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2), 6, 4210752);
		this.fontRendererObj.drawString(
				I18n.format("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}
}
