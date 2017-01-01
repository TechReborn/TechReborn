package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import reborncore.client.gui.slots.BaseSlot;
import reborncore.client.gui.slots.SlotFilteredVoid;
import reborncore.common.container.RebornContainer;
import reborncore.common.util.Inventory;
import techreborn.init.ModItems;

public class ContainerDestructoPack extends RebornContainer {

	private EntityPlayer player;
	private Inventory inv;

	public ContainerDestructoPack(EntityPlayer player) {
		this.player = player;
		inv = new Inventory(1, "destructopack", 64, null);
		buildContainer();
	}

	@Override
	public boolean canInteractWith(EntityPlayer arg0) {
		return true;
	}

	private void buildContainer() {
		this.addSlotToContainer(
			new SlotFilteredVoid(inv, 0, 80, 36, new ItemStack[] { new ItemStack(ModItems.PARTS, 1, 37) }));
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
