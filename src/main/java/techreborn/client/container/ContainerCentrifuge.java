package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import reborncore.client.gui.BaseSlot;
import reborncore.client.gui.SlotOutput;
import techreborn.api.gui.SlotUpgrade;
import techreborn.tiles.TileCentrifuge;

public class ContainerCentrifuge extends ContainerCrafting
{

	public int tickTime;
	EntityPlayer player;
	TileCentrifuge tile;

	public ContainerCentrifuge(TileCentrifuge tileCentrifuge, EntityPlayer player)
	{
		super(tileCentrifuge.crafter);
		tile = tileCentrifuge;
		this.player = player;

		// input
		this.addSlotToContainer(new BaseSlot(tileCentrifuge.inventory, 0, 80, 35));
		this.addSlotToContainer(new BaseSlot(tileCentrifuge.inventory, 1, 50, 5));
		// outputs
		this.addSlotToContainer(new SlotOutput(tileCentrifuge.inventory, 2, 80, 5));
		this.addSlotToContainer(new SlotOutput(tileCentrifuge.inventory, 3, 110, 35));
		this.addSlotToContainer(new SlotOutput(tileCentrifuge.inventory, 4, 80, 65));
		this.addSlotToContainer(new SlotOutput(tileCentrifuge.inventory, 5, 50, 35));
		// battery
		this.addSlotToContainer(new BaseSlot(tileCentrifuge.inventory, 6, 8, 51));
		// upgrades
		this.addSlotToContainer(new SlotUpgrade(tileCentrifuge.inventory, 7, 152, 8));
		this.addSlotToContainer(new SlotUpgrade(tileCentrifuge.inventory, 8, 152, 26));
		this.addSlotToContainer(new SlotUpgrade(tileCentrifuge.inventory, 9, 152, 44));
		this.addSlotToContainer(new SlotUpgrade(tileCentrifuge.inventory, 10, 152, 62));

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
