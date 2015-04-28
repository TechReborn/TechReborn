package techreborn.client.container;

import techreborn.tiles.TileChunkLoader;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerChunkloader extends TechRebornContainer{

	public ContainerChunkloader(TileChunkLoader tilechunkloader, EntityPlayer player)
	{
		
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}

}
