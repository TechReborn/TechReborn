package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import reborncore.client.gui.BaseSlot;
import reborncore.client.gui.SlotOutput;
import techreborn.tiles.TileBlastFurnace;

public class ContainerBlastFurnace extends ContainerCrafting
{

	public int heat;
	public int tickTime;
	EntityPlayer player;
	TileBlastFurnace tile;

	public ContainerBlastFurnace(TileBlastFurnace tileblastfurnace, EntityPlayer player)
	{
		super(tileblastfurnace.crafter);
		tile = tileblastfurnace;
		this.player = player;

		// input
		this.addSlotToContainer(new BaseSlot(tileblastfurnace.inventory, 0, 40, 25));// Input
																					// 1
		this.addSlotToContainer(new BaseSlot(tileblastfurnace.inventory, 1, 40, 43));// Input
																					// 2
		// outputs
		this.addSlotToContainer(new SlotOutput(tileblastfurnace.inventory, 2, 100, 35));// Output
																						// 1
		this.addSlotToContainer(new SlotOutput(tileblastfurnace.inventory, 3, 118, 35));// Output
																						// 2

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

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (int i = 0; i < this.listeners.size(); i++)
		{
			IContainerListener IContainerListener = this.listeners.get(i);
			if (this.heat != tile.getHeat())
			{
				IContainerListener.sendProgressBarUpdate(this, 10, tile.getHeat());
			}
		}
	}

	@Override
	public void addListener(IContainerListener crafting)
	{
		super.addListener(crafting);
		crafting.sendProgressBarUpdate(this, 10, tile.getHeat());
	}

	@Override
	public void updateProgressBar(int id, int value)
	{
		if (id == 10)
		{
			this.heat = value;
		}
		super.updateProgressBar(id, value);
	}
}
