package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
//import reborncore.client.gui.BaseSlot;
import reborncore.client.gui.BaseSlot;
import reborncore.common.container.RebornContainer;
import techreborn.tiles.TileChargeBench;

public class ContainerChargeBench extends RebornContainer
{

	public int tickTime;
	EntityPlayer player;
	TileChargeBench tile;

	public ContainerChargeBench(TileChargeBench tileChargeBench, EntityPlayer player)
	{
		tile = tileChargeBench;
		this.player = player;

		this.addSlotToContainer(new BaseSlot(tileChargeBench.inventory, 0, 62, 21));
		this.addSlotToContainer(new BaseSlot(tileChargeBench.inventory, 1, 80, 21));
		this.addSlotToContainer(new BaseSlot(tileChargeBench.inventory, 2, 98, 21));
		this.addSlotToContainer(new BaseSlot(tileChargeBench.inventory, 3, 62, 39));
		this.addSlotToContainer(new BaseSlot(tileChargeBench.inventory, 4, 80, 39));
		this.addSlotToContainer(new BaseSlot(tileChargeBench.inventory, 5, 98, 39));

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
