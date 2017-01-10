package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;

import techreborn.client.container.builder.BuiltContainer;

public interface IContainerProvider {
	BuiltContainer createContainer(EntityPlayer player);
}
