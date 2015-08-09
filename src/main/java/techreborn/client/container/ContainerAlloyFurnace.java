package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileAlloyFurnace;

public class ContainerAlloyFurnace extends TechRebornContainer {

    EntityPlayer player;

    TileAlloyFurnace tile;

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public int tickTime;

    public ContainerAlloyFurnace(TileAlloyFurnace tileAlloyfurnace,
                                 EntityPlayer player) {
        tile = tileAlloyfurnace;
        this.player = player;

        // input
        this.addSlotToContainer(new Slot(tileAlloyfurnace.inventory, 0, 47, 17));
        this.addSlotToContainer(new Slot(tileAlloyfurnace.inventory, 1, 65, 17));
        // outputs
        this.addSlotToContainer(new SlotOutput(tileAlloyfurnace.inventory, 2, 116, 35));
        // Fuel
        this.addSlotToContainer(new Slot(tileAlloyfurnace.inventory, 3, 56, 53));

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

}
