package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerGrinder;
import techreborn.tiles.TileGrinder;

public class GuiGrinder extends GuiContainer{
	
	private static final ResourceLocation texture = new ResourceLocation(
			"techreborn", "textures/gui/industrial_grinder.png");

	TileGrinder grinder;
	
	public GuiGrinder(EntityPlayer player, TileGrinder tilegrinder)
	{
		super(new ContainerGrinder(tilegrinder, player));
		this.xSize = 176;
		this.ySize = 167;
		grinder = tilegrinder;

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
		String name = StatCollector.translateToLocal("tile.techreborn.grinder.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(
				I18n.format("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}

}
