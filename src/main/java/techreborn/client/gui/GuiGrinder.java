package techreborn.client.gui;

import techreborn.client.container.ContainerAlloySmelter;
import techreborn.client.container.ContainerGrinder;
import techreborn.tiles.TileAlloySmelter;
import techreborn.tiles.TileGrinder;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiGrinder extends GuiContainer{

	TileGrinder tilegrinder;
	
	public GuiGrinder(EntityPlayer player, TileGrinder tilegrinder)
	{
		super(new ContainerGrinder(tilegrinder, player));

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_)
	{
		// TODO Auto-generated method stub
		
	}

}
