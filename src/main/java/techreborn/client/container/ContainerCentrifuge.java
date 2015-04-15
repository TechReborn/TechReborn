package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileCentrifuge;

public class ContainerCentrifuge extends TechRebornContainer {

    EntityPlayer player;

    TileCentrifuge tile;

    public int tickTime;

    public ContainerCentrifuge(TileCentrifuge tileCentrifuge, EntityPlayer player) {
        tile = tileCentrifuge;
        this.player = player;

        //input
        this.addSlotToContainer(new Slot(tileCentrifuge.inventory, 0, 80, 35));
        //cells
        this.addSlotToContainer(new Slot(tileCentrifuge.inventory, 1, 50, 5));
        //outputs
        this.addSlotToContainer(new SlotOutput(tileCentrifuge.inventory, 2, 80, 5));
        this.addSlotToContainer(new SlotOutput(tileCentrifuge.inventory, 3, 110, 35));
        this.addSlotToContainer(new SlotOutput(tileCentrifuge.inventory, 4, 80, 65));
        this.addSlotToContainer(new SlotOutput(tileCentrifuge.inventory, 5, 50, 35));

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 0, tile.tickTime);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (this.tickTime != this.tile.tickTime) {
                icrafting.sendProgressBarUpdate(this, 0, this.tile.tickTime);
            }
        }
        this.tickTime = this.tile.tickTime;
    }
}
