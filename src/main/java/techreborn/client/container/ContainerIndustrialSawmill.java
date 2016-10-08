package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import reborncore.client.gui.BaseSlot;
import reborncore.client.gui.SlotOutput;
import reborncore.common.container.RebornContainer;
import techreborn.tiles.multiblock.TileIndustrialSawmill;

public class ContainerIndustrialSawmill extends RebornContainer {

	public int tickTime;
	EntityPlayer player;
	TileIndustrialSawmill tile;

	public ContainerIndustrialSawmill(TileIndustrialSawmill tileIndustrialSawmill, EntityPlayer player) {
		tile = tileIndustrialSawmill;
		this.player = player;

		// input
		this.addSlotToContainer(new BaseSlot(tileIndustrialSawmill.inventory, 0, 32, 26));
		this.addSlotToContainer(new BaseSlot(tileIndustrialSawmill.inventory, 1, 32, 44));
		// outputs
		this.addSlotToContainer(new SlotOutput(tileIndustrialSawmill.inventory, 2, 84, 35));
		this.addSlotToContainer(new SlotOutput(tileIndustrialSawmill.inventory, 3, 102, 35));
		this.addSlotToContainer(new SlotOutput(tileIndustrialSawmill.inventory, 4, 120, 35));

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
