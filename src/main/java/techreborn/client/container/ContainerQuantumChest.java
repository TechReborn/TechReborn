package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import reborncore.client.gui.BaseSlot;
import reborncore.client.gui.SlotFake;
import reborncore.client.gui.SlotOutput;
import reborncore.common.container.RebornContainer;
import techreborn.tiles.TileQuantumChest;

public class ContainerQuantumChest extends RebornContainer {
	public TileQuantumChest tileQuantumChest;
	public EntityPlayer player;

	public ContainerQuantumChest(TileQuantumChest tileQuantumChest, EntityPlayer player) {
		super();
		this.tileQuantumChest = tileQuantumChest;
		this.player = player;

		this.addSlotToContainer(new BaseSlot(tileQuantumChest.inventory, 0, 80, 17));
		this.addSlotToContainer(new SlotOutput(tileQuantumChest.inventory, 1, 80, 53));
		this.addSlotToContainer(new SlotFake(tileQuantumChest.inventory, 2, 59, 42, false, false, Integer.MAX_VALUE));

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
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

}
