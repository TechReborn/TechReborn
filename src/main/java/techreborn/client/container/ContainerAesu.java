package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.container.RebornContainer;
import techreborn.tiles.TileAesu;

public class ContainerAesu extends RebornContainer {

    EntityPlayer player;

    TileAesu tile;
    
    private static final EntityEquipmentSlot[] equipmentSlots = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};


    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public int euOut;
    public int storedEu;
    public int euChange;

    public ContainerAesu(TileAesu tileaesu, EntityPlayer player) {
        tile = tileaesu;
        this.player = player;

        // charge
        this.addSlotToContainer(new Slot(tileaesu.inventory, 0, 152, 42));
        this.addSlotToContainer(new Slot(tileaesu.inventory, 1, 152, 58));
        this.addSlotToContainer(new Slot(tileaesu.inventory, 2, 152, 78));

        //Battery
        this.addSlotToContainer(new Slot(tileaesu.inventory, 3, 143, 5));

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9
                        + 9, 8 + j * 18, 115 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18,
                    173));
        }
        
		for (int k = 0; k < 4; k++) 
		{
			final EntityEquipmentSlot slot = equipmentSlots[k];
			addSlotToContainer(new Slot(player.inventory, player.inventory.getSizeInventory() - 2 - k, 134, 42 + k * 18) 
			{
				@Override
				public int getSlotStackLimit() { return 1; }
				@Override
				public boolean isItemValid(ItemStack stack) 
				{
					return stack != null && stack.getItem().isValidArmor(stack, slot, player);
				}
			});
		}
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
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
