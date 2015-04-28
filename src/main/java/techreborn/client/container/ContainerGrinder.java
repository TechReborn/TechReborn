package techreborn.client.container;

import techreborn.tiles.TileGrinder;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGrinder extends TechRebornContainer{

	public ContainerGrinder(TileGrinder tileEntity, EntityPlayer player)
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}

}
