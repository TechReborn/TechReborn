package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import reborncore.client.gui.GuiUtil;
import techreborn.client.container.ContainerIndustrialSawmill;
import techreborn.tiles.TileIndustrialSawmill;

public class GuiIndustrialSawmill extends GuiContainer
{

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/industrial_sawmill.png");

	TileIndustrialSawmill sawmill;

	public GuiIndustrialSawmill(EntityPlayer player, TileIndustrialSawmill tilesawmill)
	{
		super(new ContainerIndustrialSawmill(tilesawmill, player));
		this.xSize = 176;
		this.ySize = 167;
		sawmill = tilesawmill;
	}

	@Override
	public void initGui()
	{
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = 0;

		j = sawmill.getProgressScaled(24);
		if (j > 0)
		{
			this.drawTexturedModalRect(k + 56, l + 38, 176, 14, j - 1, 11);
		}

		j = sawmill.getEnergyScaled(12);
		if (j > 0)
		{
			this.drawTexturedModalRect(k + 33, l + 65 + 12 - j, 176, 12 - j, 14, j + 2);
		}
		// TODO 1.8
		// if (sawmill.tank.getFluidAmount() != 0) {
		// IIcon fluidIcon = sawmill.tank.getFluid().getFluid().getIcon();
		// if (fluidIcon != null) {
		// this.mc.renderEngine.bindTexture(texture);
		//
		//
		// this.mc.renderEngine
		// .bindTexture(TextureMap.locationBlocksTexture);
		// int liquidHeight = sawmill.tank.getFluidAmount() * 47
		// / sawmill.tank.getCapacity();
		// GuiUtil.drawRepeated(fluidIcon, k + 11, l + 19 + 47
		// - liquidHeight, 12.0D, liquidHeight, this.zLevel);
		//
		// this.mc.renderEngine.bindTexture(texture);
		// // drawTexturedModalRect(k + 7, l + 15, 176, 31, 20, 55);
		// }
		// }
		drawTexturedModalRect(k + 11, l + 19, 176, 86, 12, 47);
		if (!sawmill.getMutliBlock())
		{
			GuiUtil.drawTooltipBox(k + 30, l + 50 + 12 - 0, 114, 10);
			this.fontRendererObj.drawString(I18n.translateToLocal("techreborn.message.missingmultiblock"), k + 38,
					l + 52 + 12 - 0, -1);
		}

	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		String name = I18n.translateToLocal("tile.techreborn.industrialsawmill.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 58,
				this.ySize - 96 + 2, 4210752);
	}
}
