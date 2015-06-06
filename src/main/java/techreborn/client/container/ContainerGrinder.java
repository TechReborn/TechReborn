package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileGrinder;

public class ContainerGrinder extends ContainerCrafting{
	
	EntityPlayer player;

	TileGrinder tile;
	
	public int tickTime;

	public ContainerGrinder(TileGrinder tileGrinder,
			EntityPlayer player)
	{
		super(tileGrinder.crafter);
		tile = tileGrinder;
		this.player = player;

		// input
		this.addSlotToContainer(new Slot(tileGrinder.inventory, 0, 32, 26));
		this.addSlotToContainer(new Slot(tileGrinder.inventory, 1, 32, 44));


		// outputs
		this.addSlotToContainer(new SlotOutput(tileGrinder.inventory, 2, 77, 35));
		this.addSlotToContainer(new SlotOutput(tileGrinder.inventory, 3, 95, 35));
		this.addSlotToContainer(new SlotOutput(tileGrinder.inventory, 4, 113, 35));
		this.addSlotToContainer(new SlotOutput(tileGrinder.inventory, 5, 131, 35));

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
