package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.client.container.ContainerMatterFabricator;
import techreborn.tiles.TileMatterFabricator;

public class GuiMatterFabricator extends GuiContainer{
	
	private static final ResourceLocation texture = new ResourceLocation(
			"techreborn", "textures/gui/matterfabricator.png");

	TileMatterFabricator matterfab;

	ContainerMatterFabricator containerMatterFabricator;
	
	public GuiMatterFabricator(EntityPlayer player, TileMatterFabricator tilematterfab)
	{
		super(new ContainerMatterFabricator(tilematterfab, player));
		this.xSize = 176;
		this.ySize = 167;
		matterfab = tilematterfab;
		containerMatterFabricator = (ContainerMatterFabricator) this.inventorySlots;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_)
	{
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = containerMatterFabricator.getProgressScaled(24);
		if(j > 0) {
			this.drawTexturedModalRect(k + 79, l + 34, 176, 14, j + 1, 16);
		}
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_,
			int p_146979_2_)
	{
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		String name = StatCollector.translateToLocal("tile.techreborn.matterfabricator.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(
				I18n.format("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
		this.fontRendererObj.drawString(containerMatterFabricator.getProgressScaled(100) + "%", 80, 50, 4210752);
	}

}
