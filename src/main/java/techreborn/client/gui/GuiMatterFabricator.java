package techreborn.client.gui;

import techreborn.client.container.ContainerImplosionCompressor;
import techreborn.client.container.ContainerMatterFabricator;
import techreborn.tiles.TileImplosionCompressor;
import techreborn.tiles.TileMatterFabricator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiMatterFabricator extends GuiContainer{
	
	private static final ResourceLocation texture = new ResourceLocation(
			"techreborn", "textures/gui/matterfabricator.png");

	TileMatterFabricator matterfab;
	
	public GuiMatterFabricator(EntityPlayer player, TileMatterFabricator tilematterfab)
	{
		super(new ContainerMatterFabricator(tilematterfab, player));
		this.xSize = 176;
		this.ySize = 167;
		matterfab = tilematterfab;

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_)
	{
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_,
			int p_146979_2_)
	{
		String name = StatCollector.translateToLocal("tile.techreborn.matterfabricator.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(
				I18n.format("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}

}
