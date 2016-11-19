package techreborn.client.container;

/**
 * Created by Rushmead
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.client.gui.slots.BaseSlot;
import reborncore.client.gui.slots.SlotCharge;
import reborncore.common.container.RebornContainer;
import techreborn.tiles.storage.TileMFE;

public class ContainerMFE extends RebornContainer {

	public int burnTime = 0;
	public int totalBurnTime = 0;
	public int energy;
	public int tickTime;
	EntityPlayer player;
	TileMFE tile;
	private static final EntityEquipmentSlot[] equipmentSlots = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };

	public ContainerMFE(TileMFE tile, EntityPlayer player) {
		super();
		this.tile = tile;
		this.player = player;

		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new BaseSlot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new BaseSlot(player.inventory, i, 8 + i * 18, 142));
		}
		for (int k = 0; k < 4; k++) {
			final EntityEquipmentSlot slot = equipmentSlots[k];
			addSlotToContainer(new BaseSlot(player.inventory, player.inventory.getSizeInventory() - 2 - k, 44, 6 + k * 19) {
				@Override
				public int getSlotStackLimit() { return 1; }

				@Override
				public boolean isItemValid(ItemStack stack) {
					return stack != null && stack.getItem().isValidArmor(stack, slot, player);
				}
			});
		}
		this.addSlotToContainer(new SlotCharge(tile.inventory, 0, 80, 17));
		this.addSlotToContainer(new SlotCharge(tile.inventory, 1, 80, 53));
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
			if (this.energy != (int) tile.getEnergy()) {
				IContainerListener.sendProgressBarUpdate(this, 2, (int) tile.getEnergy());
			}
		}
	}

	@Override
	public void addListener(IContainerListener crafting) {
		super.addListener(crafting);
		crafting.sendProgressBarUpdate(this, 2, (int) tile.getEnergy());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value) {
		if (id == 0) {
			this.burnTime = value;
		} else if (id == 1) {
			this.totalBurnTime = value;
		} else if (id == 2) {
			this.energy = value;
		}
		this.tile.setEnergy(energy);
	}

	public int getScaledBurnTime(int i) {
		return (int) (((float) burnTime / (float) totalBurnTime) * i);
	}
}
