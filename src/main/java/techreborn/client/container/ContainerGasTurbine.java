package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import reborncore.client.gui.BaseSlot;
import reborncore.client.gui.SlotFake;
import reborncore.client.gui.SlotOutput;
import reborncore.common.container.RebornContainer;
import techreborn.tiles.generator.TileGasTurbine;

public class ContainerGasTurbine extends RebornContainer {
	public TileGasTurbine tileGasTurbine;
	public EntityPlayer player;

	public ContainerGasTurbine(TileGasTurbine tileGasTurbine, EntityPlayer player) {
		super();
		this.tileGasTurbine = tileGasTurbine;
		this.player = player;

		this.addSlotToContainer(new BaseSlot(tileGasTurbine.inventory, 0, 80, 17));
		this.addSlotToContainer(new SlotOutput(tileGasTurbine.inventory, 1, 80, 53));
		this.addSlotToContainer(new SlotFake(tileGasTurbine.inventory, 2, 59, 42, false, false, 1));

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
