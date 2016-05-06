package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import reborncore.client.gui.BaseSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.client.gui.SlotOutput;
import techreborn.tiles.TileVacuumFreezer;

public class ContainerVacuumFreezer extends ContainerCrafting
{

	public int tickTime;
	public int machineStatus;
	EntityPlayer player;
	TileVacuumFreezer tile;
	public ContainerVacuumFreezer(TileVacuumFreezer tileAlloysmelter, EntityPlayer player)
	{
		super(tileAlloysmelter.crafter);
		tile = tileAlloysmelter;
		this.player = player;

		// input
		this.addSlotToContainer(new BaseSlot(tileAlloysmelter.inventory, 0, 56, 34));
		// outputs
		this.addSlotToContainer(new SlotOutput(tileAlloysmelter.inventory, 1, 116, 35));

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
			ICrafting icrafting = this.listeners.get(i);
			if (this.machineStatus != tile.multiBlockStatus)
			{
				icrafting.sendProgressBarUpdate(this, 3, tile.multiBlockStatus);
			}
		}
	}

	@Override
	public void onCraftGuiOpened(ICrafting crafting)
	{
		super.onCraftGuiOpened(crafting);
		crafting.sendProgressBarUpdate(this, 3, tile.multiBlockStatus);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value)
	{
		super.updateProgressBar(id, value);
		if (id == 3)
		{
			machineStatus = value;
		}
	}

}
