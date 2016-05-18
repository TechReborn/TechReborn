package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import reborncore.client.gui.BaseSlot;
import reborncore.client.gui.SlotOutput;
import techreborn.api.gui.SlotUpgrade;
import techreborn.tiles.teir1.TileExtractor;

public class ContainerExtractor extends ContainerCrafting
{

	public int connectionStatus;
	EntityPlayer player;
	TileExtractor tile;

	public ContainerExtractor(TileExtractor tileGrinder, EntityPlayer player)
	{
		super(tileGrinder.crafter);
		tile = tileGrinder;
		this.player = player;

		// input
		this.addSlotToContainer(new BaseSlot(tileGrinder.inventory, 0, 56, 34));
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
				this.addSlotToContainer(new BaseSlot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new BaseSlot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}

	// @Override
	// public void addListener(IContainerListener crafting) {
	// super.addListener(crafting);
	// crafting.sendProgressBarUpdate(this, 0, (int) tile.getProgressScaled(0));
	// crafting.sendProgressBarUpdate(this, 2, (int) tile.getEnergy());
	// }
	//
	// @SideOnly(Side.CLIENT)
	// @Override
	// public void updateProgressBar(int id, int value) {
	// if (id == 0) {
	// this.progress = value;
	// }
	// else if (id == 2) {
	// this.energy = value;
	// }
	// this.tile.setEnergy(energy);
	// }

	// @SideOnly(Side.CLIENT)
	// @Override
	// public void updateProgressBar(int id, int value) {
	// if (id == 10) {
	// this.connectionStatus = value;
	// }
	// }
}
