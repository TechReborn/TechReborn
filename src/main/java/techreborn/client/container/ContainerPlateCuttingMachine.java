package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileAlloySmelter;
import techreborn.tiles.TileBlastFurnace;
import techreborn.tiles.TileLathe;
import techreborn.tiles.TilePlateCuttingMachine;

public class ContainerPlateCuttingMachine extends TechRebornContainer {

	EntityPlayer player;

	TilePlateCuttingMachine platecuttingmachine;

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	public int tickTime;

	public ContainerPlateCuttingMachine(TilePlateCuttingMachine tileplatecuttingmachine,
			EntityPlayer player)
	{
		platecuttingmachine = tileplatecuttingmachine;
		this.player = player;

		// input
		this.addSlotToContainer(new Slot(tileplatecuttingmachine.inventory, 0, 56, 17));
		// outputs
		this.addSlotToContainer(new SlotOutput(tileplatecuttingmachine.inventory, 2, 116, 35));
		// power
		this.addSlotToContainer(new Slot(tileplatecuttingmachine.inventory, 1, 56, 53));

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
