package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileBlastFurnace;

public class ContainerBlastFurnace extends TechRebornContainer {

	EntityPlayer player;

	TileBlastFurnace tile;

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	public int tickTime;

	public ContainerBlastFurnace(TileBlastFurnace tileblastfurnace,
			EntityPlayer player)
	{
		tile = tileblastfurnace;
		this.player = player;

		// input
		this.addSlotToContainer(new Slot(tileblastfurnace.inventory, 0, 56, 25));
		this.addSlotToContainer(new Slot(tileblastfurnace.inventory, 1, 56, 43));
		// outputs
		this.addSlotToContainer(new SlotOutput(tileblastfurnace.inventory, 2,
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

}
