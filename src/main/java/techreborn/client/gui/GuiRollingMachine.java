package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import techreborn.client.container.ContainerRollingMachine;
import techreborn.tiles.TileRollingMachine;

public class GuiRollingMachine extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation(
			"techreborn", "textures/gui/rollingmachine.png");
	TileRollingMachine rollingMachine;

	public GuiRollingMachine(EntityPlayer player,
			TileRollingMachine tileRollingmachine)
	{
		super(new ContainerRollingMachine(tileRollingmachine, player));
		this.xSize = 176;
		this.ySize = 167;
		rollingMachine = tileRollingmachine;
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

}
