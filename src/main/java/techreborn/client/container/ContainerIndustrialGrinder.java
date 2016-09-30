package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import reborncore.client.gui.BaseSlot;
import reborncore.client.gui.SlotOutput;
import techreborn.tiles.multiblock.TileIndustrialGrinder;

public class ContainerIndustrialGrinder extends ContainerCrafting {

	EntityPlayer player;
	TileIndustrialGrinder tile;

	public ContainerIndustrialGrinder(TileIndustrialGrinder tileGrinder, EntityPlayer player) {
		super(tileGrinder.getRecipeCrafter());
		tile = tileGrinder;
		this.player = player;

		// input
		this.addSlotToContainer(new BaseSlot(tileGrinder.inventory, 0, 32, 26));
		this.addSlotToContainer(new BaseSlot(tileGrinder.inventory, 1, 32, 44));

		// outputs
		this.addSlotToContainer(new SlotOutput(tileGrinder.inventory, 2, 77, 35));
		this.addSlotToContainer(new SlotOutput(tileGrinder.inventory, 3, 95, 35));
		this.addSlotToContainer(new SlotOutput(tileGrinder.inventory, 4, 113, 35));
		this.addSlotToContainer(new SlotOutput(tileGrinder.inventory, 5, 131, 35));

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
