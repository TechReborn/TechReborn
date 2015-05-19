package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileMatterFabricator;

public class ContainerMatterFabricator extends TechRebornContainer{
	
	EntityPlayer player;

	TileMatterFabricator tile;

	public int tickTime;

	public ContainerMatterFabricator(TileMatterFabricator tileMatterfab,
			EntityPlayer player)
	{
		tile = tileMatterfab;
		this.player = player;

		// input
		this.addSlotToContainer(new Slot(tileMatterfab.inventory, 0, 33, 17));
		this.addSlotToContainer(new Slot(tileMatterfab.inventory, 1, 33, 35));
		this.addSlotToContainer(new Slot(tileMatterfab.inventory, 2, 33, 53));
		this.addSlotToContainer(new Slot(tileMatterfab.inventory, 3, 51, 17));
		this.addSlotToContainer(new Slot(tileMatterfab.inventory, 4, 51, 35));
		this.addSlotToContainer(new Slot(tileMatterfab.inventory, 5, 51, 53));

		// outputs
		this.addSlotToContainer(new SlotOutput(tileMatterfab.inventory, 6,
				116, 35));

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9
						+ 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18,
					142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}

}
