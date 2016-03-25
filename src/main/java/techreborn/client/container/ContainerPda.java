package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import reborncore.common.container.RebornContainer;

public class ContainerPda extends RebornContainer
{

	EntityPlayer player;

	public ContainerPda(EntityPlayer player)
	{

	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

}
