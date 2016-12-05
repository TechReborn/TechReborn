package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import reborncore.api.tile.IContainerLayout;
import reborncore.client.gui.slots.BaseSlot;
import reborncore.client.gui.slots.SlotCharge;
import reborncore.client.gui.slots.SlotInput;
import reborncore.client.gui.slots.SlotOutput;
import techreborn.api.gui.SlotUpgrade;
import techreborn.tiles.TileAlloySmelter;

import javax.annotation.Nullable;
import java.util.List;

public class ContainerAlloySmelter extends ContainerCrafting {

	public int tickTime;
	EntityPlayer player;
	TileAlloySmelter tile;

	public ContainerAlloySmelter(EntityPlayer player, TileAlloySmelter tile) {
		super(tile.crafter);
		this.player = player;
		this.tile = tile;
		addPlayerSlots();
		addInventorySlots();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}


	public void addInventorySlots() {

		// input
		this.addSlotToContainer(new SlotInput(tile.inventory, 0, 47, 17));
		this.addSlotToContainer(new SlotInput(tile.inventory, 1, 65, 17));
		// outputs
		this.addSlotToContainer(new SlotOutput(tile.inventory, 2, 116, 35));
		// battery
		this.addSlotToContainer(new SlotCharge(tile.inventory, 3, 56, 53));
		// upgrades
		this.addSlotToContainer(new SlotUpgrade(tile.inventory, 4, 152, 8));
		this.addSlotToContainer(new SlotUpgrade(tile.inventory, 5, 152, 26));
		this.addSlotToContainer(new SlotUpgrade(tile.inventory, 6, 152, 44));
		this.addSlotToContainer(new SlotUpgrade(tile.inventory, 7, 152, 62));
	}


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

}
