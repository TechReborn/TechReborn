package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import reborncore.api.tile.IContainerLayout;
import reborncore.client.gui.BaseSlot;
import reborncore.client.gui.SlotInput;
import reborncore.client.gui.SlotOutput;
import reborncore.common.recipes.RecipeCrafter;
import techreborn.api.gui.SlotUpgrade;
import techreborn.tiles.teir1.TileGrinder;

import javax.annotation.Nullable;
import java.util.List;

public class ContainerGrinder extends ContainerCrafting {

	public int connectionStatus;
	EntityPlayer player;
	TileGrinder tile;

	public ContainerGrinder(EntityPlayer player, TileGrinder tile) {
		super(tile.crafter);
		this.player = player;
		this.tile = tile;
		addInventorySlots();
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}


	public void addInventorySlots() {
		// input
		this.addSlotToContainer(new SlotInput(tile.inventory, 0, 56, 34));
		this.addSlotToContainer(new SlotOutput(tile.inventory, 1, 116, 34));

		// upgrades
		this.addSlotToContainer(new SlotUpgrade(tile.inventory, 2, 152, 8));
		this.addSlotToContainer(new SlotUpgrade(tile.inventory, 3, 152, 26));
		this.addSlotToContainer(new SlotUpgrade(tile.inventory, 4, 152, 44));
		this.addSlotToContainer(new SlotUpgrade(tile.inventory, 5, 152, 62));

		addPlayerSlots();
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
