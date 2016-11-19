package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import reborncore.client.gui.slots.BaseSlot;
import reborncore.client.gui.slots.SlotOutput;
import techreborn.tiles.multiblock.TileVacuumFreezer;

public class ContainerVacuumFreezer extends ContainerCrafting {

	EntityPlayer player;
	TileVacuumFreezer tile;

	public ContainerVacuumFreezer(TileVacuumFreezer tileAlloysmelter, EntityPlayer player) {
		super(tileAlloysmelter.crafter);
		tile = tileAlloysmelter;
		this.player = player;

		// input
		this.addSlotToContainer(new BaseSlot(tileAlloysmelter.inventory, 0, 56, 34));
		// outputs
		this.addSlotToContainer(new SlotOutput(tileAlloysmelter.inventory, 1, 116, 35));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new BaseSlot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new BaseSlot(player.inventory, i, 8 + i * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

}
