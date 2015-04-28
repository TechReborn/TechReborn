package techreborn.client.gui;

import techreborn.client.container.ContainerGrinder;
import techreborn.client.container.ContainerImplosionCompressor;
import techreborn.tiles.TileImplosionCompressor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiImplosionCompressor extends GuiContainer{

	TileImplosionCompressor tilecompresser;
	
	public GuiImplosionCompressor(EntityPlayer player, TileImplosionCompressor tilecompresser)
	{
		super(new ContainerImplosionCompressor(tilecompresser, player));

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_)
	{
		// TODO Auto-generated method stub
		
	}

}
