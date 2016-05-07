package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import reborncore.client.gui.BaseSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.container.RebornContainer;
import techreborn.tiles.TileAesu;

public class ContainerAESU extends RebornContainer {

	public int euOut;
	public int storedEu;
	public int euChange;
	EntityPlayer player;
	TileAesu tile;

	public ContainerAESU(TileAesu tileaesu, EntityPlayer player) {
		tile = tileaesu;
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
			ICrafting icrafting = this.listeners.get(i);
			if (this.euOut != tile.getMaxOutput()) {
				icrafting.sendProgressBarUpdate(this, 0, (int) tile.getMaxOutput());
			}
			if (this.storedEu != tile.getEnergy()) {
				icrafting.sendProgressBarUpdate(this, 1, (int) tile.getEnergy());
			}
			if (this.euChange != tile.getEuChange() && tile.getEuChange() != -1) {
				icrafting.sendProgressBarUpdate(this, 2, (int) tile.getEuChange());
			}
		}
	}

	@Override
	public void onCraftGuiOpened(ICrafting crafting) {
		super.onCraftGuiOpened(crafting);
		crafting.sendProgressBarUpdate(this, 0, (int) tile.getMaxOutput());
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
		}
	}

}
