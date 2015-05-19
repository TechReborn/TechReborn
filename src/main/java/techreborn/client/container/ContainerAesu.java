package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.tiles.TileAesu;

public class ContainerAesu extends TechRebornContainer {

	EntityPlayer player;

	TileAesu tile;

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	public int tickTime;

	public ContainerAesu(TileAesu tileaesu,
			EntityPlayer player)
	{
		tile = tileaesu;
		this.player = player;

		// input
		this.addSlotToContainer(new Slot(tileaesu.inventory, 0, 128, 14));
		this.addSlotToContainer(new Slot(tileaesu.inventory, 1, 128, 50));

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

}
