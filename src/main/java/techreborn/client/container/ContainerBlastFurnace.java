package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileBlastFurnace;

public class ContainerBlastFurnace extends ContainerCrafting {

    EntityPlayer player;

    TileBlastFurnace tile;

    public int heat;

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public int tickTime;

    public ContainerBlastFurnace(TileBlastFurnace tileblastfurnace,
                                 EntityPlayer player) {
        super(tileblastfurnace.crafter);
        tile = tileblastfurnace;
        this.player = player;

        // input
        this.addSlotToContainer(new Slot(tileblastfurnace.inventory, 0, 40, 25));//Input 1
        this.addSlotToContainer(new Slot(tileblastfurnace.inventory, 1, 40, 43));//Input 2
        // outputs
        this.addSlotToContainer(new SlotOutput(tileblastfurnace.inventory, 2, 100, 35));//Output 1
        this.addSlotToContainer(new SlotOutput(tileblastfurnace.inventory, 3, 118, 35));//Output 2

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9
                        + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18,
                    142));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (this.heat != tile.getHeat()) {
                icrafting.sendProgressBarUpdate(this, 10, tile.getHeat());
            }
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 10, tile.getHeat());
    }

    @Override
    public void updateProgressBar(int id, int value) {
        if (id == 10) {
            this.heat = value;
        }
        super.updateProgressBar(id, value);
    }
}
