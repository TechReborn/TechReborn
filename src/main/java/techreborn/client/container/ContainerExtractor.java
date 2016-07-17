package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import reborncore.api.tile.IContainerLayout;
import reborncore.client.gui.BaseSlot;
import reborncore.client.gui.SlotInput;
import reborncore.client.gui.SlotOutput;
import techreborn.api.gui.SlotUpgrade;
import techreborn.tiles.teir1.TileExtractor;

public class ContainerExtractor extends ContainerCrafting implements IContainerLayout<TileExtractor>
{

	public int connectionStatus;
	EntityPlayer player;
	TileExtractor tileExtractor;


	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}

	@Override
	public void addInventorySlots() {

		// input
		this.addSlotToContainer(new SlotInput(tileExtractor.inventory, 0, 56, 34));
		this.addSlotToContainer(new SlotOutput(tileExtractor.inventory, 1, 116, 34));

		// upgrades
		this.addSlotToContainer(new SlotUpgrade(tileExtractor.inventory, 2, 152, 8));
		this.addSlotToContainer(new SlotUpgrade(tileExtractor.inventory, 3, 152, 26));
		this.addSlotToContainer(new SlotUpgrade(tileExtractor.inventory, 4, 152, 44));
		this.addSlotToContainer(new SlotUpgrade(tileExtractor.inventory, 5, 152, 62));		
	}

	@Override
	public void addPlayerSlots() {
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
	public void setTile(TileExtractor tile) {
		this.tileExtractor = tile;
		setCrafter(tile.crafter);
	}

	@Override
	public TileExtractor getTile() {
		return tileExtractor;
	}

	@Override
	public void setPlayer(EntityPlayer player) {
		this.player = player;
		
	}

	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	@Nullable
	@Override
	public List<Integer> getSlotsForSide(EnumFacing facing) {
		return null;
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
