package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileAlloySmelter;

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
