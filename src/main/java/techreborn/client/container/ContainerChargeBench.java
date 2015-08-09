package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.tiles.TileChargeBench;

public class ContainerChargeBench extends TechRebornContainer {

    EntityPlayer player;

    TileChargeBench tile;

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public int tickTime;

    public ContainerChargeBench(TileChargeBench tileChargeBench, EntityPlayer player) {
        tile = tileChargeBench;
        this.player = player;

        this.addSlotToContainer(new Slot(tileChargeBench.inventory, 0, 62, 21));
        this.addSlotToContainer(new Slot(tileChargeBench.inventory, 1, 80, 21));
        this.addSlotToContainer(new Slot(tileChargeBench.inventory, 2, 98, 21));
        this.addSlotToContainer(new Slot(tileChargeBench.inventory, 3, 62, 39));
        this.addSlotToContainer(new Slot(tileChargeBench.inventory, 4, 80, 39));
        this.addSlotToContainer(new Slot(tileChargeBench.inventory, 5, 98, 39));

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

}
