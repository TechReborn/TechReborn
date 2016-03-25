package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.client.gui.SlotOutput;
import reborncore.common.container.RebornContainer;
import techreborn.api.SlotUpgrade;
import techreborn.tiles.TileScrapboxinator;

public class ContainerScrapboxinator extends RebornContainer
{

	public int connectionStatus;
	EntityPlayer player;
	TileScrapboxinator tile;

	public ContainerScrapboxinator(TileScrapboxinator tileScrapboxinator, EntityPlayer player)
	{
		super();
		tile = tileScrapboxinator;
		this.player = player;

		// input
		this.addSlotToContainer(new SlotScrapbox(tileScrapboxinator.inventory, 0, 56, 34));
		this.addSlotToContainer(new SlotOutput(tileScrapboxinator.inventory, 1, 116, 34));

		// upgrades
		this.addSlotToContainer(new SlotUpgrade(tileScrapboxinator.inventory, 2, 152, 8));
		this.addSlotToContainer(new SlotUpgrade(tileScrapboxinator.inventory, 3, 152, 26));
		this.addSlotToContainer(new SlotUpgrade(tileScrapboxinator.inventory, 4, 152, 44));
		this.addSlotToContainer(new SlotUpgrade(tileScrapboxinator.inventory, 5, 152, 62));

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

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value)
	{
		if (id == 10)
		{
			this.connectionStatus = value;
		}
	}
}
