package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileIndustrialSawmill;

public class ContainerIndustrialSawmill extends ContainerCrafting {

    EntityPlayer player;

    TileIndustrialSawmill tile;

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public int tickTime;

    public ContainerIndustrialSawmill(TileIndustrialSawmill tileIndustrialSawmill,
                                      EntityPlayer player) {
        super(tileIndustrialSawmill.crafter);
        tile = tileIndustrialSawmill;
        this.player = player;

        // input
        this.addSlotToContainer(new Slot(tileIndustrialSawmill.inventory, 0, 32, 26));
        this.addSlotToContainer(new Slot(tileIndustrialSawmill.inventory, 1, 32, 44));
        // outputs
        this.addSlotToContainer(new SlotOutput(tileIndustrialSawmill.inventory, 2, 84, 35));
        this.addSlotToContainer(new SlotOutput(tileIndustrialSawmill.inventory, 3, 102, 35));
        this.addSlotToContainer(new SlotOutput(tileIndustrialSawmill.inventory, 4, 120, 35));


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
