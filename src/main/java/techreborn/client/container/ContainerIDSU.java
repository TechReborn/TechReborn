package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import reborncore.client.gui.BaseSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.container.RebornContainer;
import techreborn.tiles.idsu.TileIDSU;

public class ContainerIDSU extends RebornContainer {

	public int euOut;
	public int storedEu;
	public int euChange;
	public int channel;
	EntityPlayer player;
	TileIDSU tile;

	public ContainerIDSU(TileIDSU tileIDSU, EntityPlayer player) {
		tile = tileIDSU;
		this.player = player;

		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new BaseSlot(player.inventory, j + i * 9 + 9, 8 + j * 18, 115 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new BaseSlot(player.inventory, i, 8 + i * 18, 173));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < this.listeners.size(); i++) {
			IContainerListener IContainerListener = this.listeners.get(i);
			if (this.euOut != tile.output) {
				IContainerListener.sendProgressBarUpdate(this, 0, tile.output);
			}
			if (this.storedEu != (int) tile.getEnergy()) {
				IContainerListener.sendProgressBarUpdate(this, 1, (int) tile.getEnergy());
			}
			if (this.euChange != tile.getEuChange() && tile.getEuChange() != -1) {
				IContainerListener.sendProgressBarUpdate(this, 2, (int) tile.getEuChange());
			}
		}
	}

	@Override
	public void addListener(IContainerListener crafting) {
		super.addListener(crafting);
		crafting.sendProgressBarUpdate(this, 0, tile.output);
		crafting.sendProgressBarUpdate(this, 1, (int) tile.getEnergy());
		crafting.sendProgressBarUpdate(this, 2, (int) tile.getEuChange());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value) {
		if (id == 0) {
			this.euOut = value;
		} else if (id == 1) {
			this.storedEu = value;
		} else if (id == 2) {
			this.euChange = value;
		} else if (id == 3) {
			this.channel = value;
		}
	}

}
