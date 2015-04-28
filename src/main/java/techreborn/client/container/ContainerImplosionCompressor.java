package techreborn.client.container;

import techreborn.tiles.TileGrinder;
import techreborn.tiles.TileImplosionCompressor;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerImplosionCompressor extends TechRebornContainer{

	public ContainerImplosionCompressor(TileImplosionCompressor tilecompresser,
			EntityPlayer player)
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}

}
