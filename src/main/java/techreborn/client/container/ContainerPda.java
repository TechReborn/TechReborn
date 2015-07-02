package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;

public class ContainerPda extends TechRebornContainer {

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
