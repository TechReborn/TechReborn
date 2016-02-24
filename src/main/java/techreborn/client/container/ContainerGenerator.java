package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import reborncore.common.container.RebornContainer;
import techreborn.tiles.generator.TileGenerator;

public class ContainerGenerator extends RebornContainer {

    EntityPlayer player;

    TileGenerator tile;

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public int tickTime;

    public ContainerGenerator(TileGenerator tile, EntityPlayer player) {
        super();
        this.tile = tile;
        this.player = player;

        // fuel
        this.addSlotToContainer(new Slot(tile.inventory, 0, 80, 53));
        // charge
        this.addSlotToContainer(new SlotFurnaceFuel(tile.inventory, 1, 80, 17));


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
