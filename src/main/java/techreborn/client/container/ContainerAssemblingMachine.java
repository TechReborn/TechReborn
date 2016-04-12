package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import reborncore.client.gui.BaseSlot;
import reborncore.client.gui.SlotOutput;
import techreborn.api.gui.SlotUpgrade;
import techreborn.tiles.TileAssemblingMachine;

public class ContainerAssemblingMachine extends ContainerCrafting
{

	public int tickTime;
	EntityPlayer player;
	TileAssemblingMachine tile;

	public ContainerAssemblingMachine(TileAssemblingMachine tileAssemblingMachine, EntityPlayer player)
	{
		super(tileAssemblingMachine.crafter);
		tile = tileAssemblingMachine;
		this.player = player;

		// input
		this.addSlotToContainer(new BaseSlot(tileAssemblingMachine.inventory, 0, 47, 17));
		this.addSlotToContainer(new BaseSlot(tileAssemblingMachine.inventory, 1, 65, 17));
		// outputs
		this.addSlotToContainer(new SlotOutput(tileAssemblingMachine.inventory, 2, 116, 35));
		// power
		this.addSlotToContainer(new BaseSlot(tileAssemblingMachine.inventory, 3, 56, 53));
		// upgrades
		this.addSlotToContainer(new SlotUpgrade(tileAssemblingMachine.inventory, 4, 152, 8));
		this.addSlotToContainer(new SlotUpgrade(tileAssemblingMachine.inventory, 5, 152, 26));
		this.addSlotToContainer(new SlotUpgrade(tileAssemblingMachine.inventory, 6, 152, 44));
		this.addSlotToContainer(new SlotUpgrade(tileAssemblingMachine.inventory, 7, 152, 62));

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new BaseSlot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new BaseSlot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

}
