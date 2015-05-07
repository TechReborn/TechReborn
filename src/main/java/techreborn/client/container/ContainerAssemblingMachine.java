package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileAlloySmelter;
import techreborn.tiles.TileAssemblingMachine;
import techreborn.tiles.TileBlastFurnace;

public class ContainerAssemblingMachine extends TechRebornContainer {

	EntityPlayer player;

	TileAssemblingMachine tile;

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	public int tickTime;

	public ContainerAssemblingMachine(TileAssemblingMachine tileAssemblingMachine,
			EntityPlayer player)
	{
		tile = tileAssemblingMachine;
		this.player = player;

		// input
		this.addSlotToContainer(new Slot(tileAssemblingMachine.inventory, 0, 47, 17));
		this.addSlotToContainer(new Slot(tileAssemblingMachine.inventory, 1, 65, 17));
		// outputs
		this.addSlotToContainer(new SlotOutput(tileAssemblingMachine.inventory, 2,116, 35));
		// power
		this.addSlotToContainer(new Slot(tileAssemblingMachine.inventory, 3, 56, 53));

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
