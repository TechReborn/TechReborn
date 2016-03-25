package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import techreborn.client.container.ContainerIndustrialElectrolyzer;
import techreborn.tiles.TileIndustrialElectrolyzer;

public class GuiIndustrialElectrolyzer extends GuiContainer
{

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/industrial_electrolyzer.png");

	TileIndustrialElectrolyzer eletrolyzer;

	ContainerIndustrialElectrolyzer containerIndustrialElectrolyzer;

	public GuiIndustrialElectrolyzer(EntityPlayer player, TileIndustrialElectrolyzer tileeletrolyzer)
	{
		super(new ContainerIndustrialElectrolyzer(tileeletrolyzer, player));
		this.xSize = 176;
		this.ySize = 167;
		eletrolyzer = tileeletrolyzer;
		containerIndustrialElectrolyzer = (ContainerIndustrialElectrolyzer) this.inventorySlots;
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

		j = eletrolyzer.getProgressScaled(24);
		if (j > 0)
		{
			this.drawTexturedModalRect(k + 72, l + 38, 176, 14, j + 1, 16);
		}

		j = eletrolyzer.getEnergyScaled(12);
		if (j > 0)
		{
			this.drawTexturedModalRect(k + 134, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		String name = I18n.translateToLocal("tile.techreborn.industrialelectrolyzer.name");
		this.fontRendererObj.drawString(name, (this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2), 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}
}
