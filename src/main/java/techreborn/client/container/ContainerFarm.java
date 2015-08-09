package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.tiles.TileFarm;

public class ContainerFarm extends TechRebornContainer {

    TileFarm farm;
    EntityPlayer player;

    public ContainerFarm(TileFarm farm, EntityPlayer player) {
        this.farm = farm;
        this.player = player;


        this.addSlotToContainer(new Slot(farm.inventory, 0, 35, 7));

        for (int i = 0; i < 3; i++) {
            this.addSlotToContainer(new Slot(farm.inventory, 1 + i, 71 + (i * 18), 52));
        }


        int p = 5;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlotToContainer(new Slot(farm.inventory, p, 143 + (i * 18), 16 + (j * 18)));
                p++;
            }
        }


        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9
                        + 9, 8 + j * 18 + 27, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18 + 27,
                    142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
