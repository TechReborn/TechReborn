package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import reborncore.client.gui.SlotOutput;
import techreborn.api.SlotUpgrade;
import techreborn.tiles.teir1.TileGrinder;

public class ContainerGrinder extends ContainerCrafting
{

	EntityPlayer player;

	TileGrinder tile;

	public int connectionStatus;

	public ContainerGrinder(TileGrinder tileGrinder, EntityPlayer player)
	{
		super(tileGrinder.crafter);
		tile = tileGrinder;
		this.player = player;

		// input
		this.addSlotToContainer(new Slot(tileGrinder.inventory, 0, 56, 34));
		this.addSlotToContainer(new SlotOutput(tileGrinder.inventory, 1, 116, 34));

		// upgrades
		this.addSlotToContainer(new SlotUpgrade(tileGrinder.inventory, 2, 152, 8));
		this.addSlotToContainer(new SlotUpgrade(tileGrinder.inventory, 3, 152, 26));
		this.addSlotToContainer(new SlotUpgrade(tileGrinder.inventory, 4, 152, 44));
		this.addSlotToContainer(new SlotUpgrade(tileGrinder.inventory, 5, 152, 62));

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}
}
