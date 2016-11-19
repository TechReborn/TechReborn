package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import reborncore.api.tile.IContainerLayout;
import reborncore.client.gui.slots.BaseSlot;
import reborncore.client.gui.slots.SlotOutput;
import techreborn.api.gui.SlotUpgrade;
import techreborn.tiles.TileCentrifuge;

import javax.annotation.Nullable;
import java.util.List;

public class ContainerCentrifuge extends ContainerCrafting implements IContainerLayout<TileCentrifuge> {

	public int tickTime;
	EntityPlayer player;
	TileCentrifuge tile;

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public void addInventorySlots() {
		// input
		this.addSlotToContainer(new BaseSlot(tile.inventory, 0, 80, 35));
		this.addSlotToContainer(new BaseSlot(tile.inventory, 1, 50, 5));
		// outputs
		this.addSlotToContainer(new SlotOutput(tile.inventory, 2, 80, 5));
		this.addSlotToContainer(new SlotOutput(tile.inventory, 3, 110, 35));
		this.addSlotToContainer(new SlotOutput(tile.inventory, 4, 80, 65));
		this.addSlotToContainer(new SlotOutput(tile.inventory, 5, 50, 35));
		// battery
		this.addSlotToContainer(new BaseSlot(tile.inventory, 6, 8, 51));
		// upgrades
		this.addSlotToContainer(new SlotUpgrade(tile.inventory, 7, 152, 8));
		this.addSlotToContainer(new SlotUpgrade(tile.inventory, 8, 152, 26));
		this.addSlotToContainer(new SlotUpgrade(tile.inventory, 9, 152, 44));
		this.addSlotToContainer(new SlotUpgrade(tile.inventory, 10, 152, 62));
	}

	@Override
	public void addPlayerSlots() {
		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new BaseSlot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new BaseSlot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void setTile(TileCentrifuge tile) {
		this.tile = tile;
		this.crafter = tile.crafter;
	}

	@Nullable
	@Override
	public TileCentrifuge getTile() {
		return tile;
	}

	@Override
	public void setPlayer(EntityPlayer player) {
		this.player = player;
	}

	@Nullable
	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	@Nullable
	@Override
	public List<Integer> getSlotsForSide(EnumFacing facing) {
		return null;
	}
}
