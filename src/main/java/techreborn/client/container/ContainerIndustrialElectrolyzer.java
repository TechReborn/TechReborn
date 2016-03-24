package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import reborncore.client.gui.SlotOutput;
import techreborn.tiles.TileIndustrialElectrolyzer;

public class ContainerIndustrialElectrolyzer extends ContainerCrafting {

    EntityPlayer player;

    TileIndustrialElectrolyzer tile;

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public int tickTime;

    public ContainerIndustrialElectrolyzer(TileIndustrialElectrolyzer electrolyzer, EntityPlayer player) {
        super(electrolyzer.crafter);
        tile = electrolyzer;
        this.player = player;

        // input
        this.addSlotToContainer(new Slot(electrolyzer.inventory, 0, 80, 51));
        this.addSlotToContainer(new Slot(electrolyzer.inventory, 1, 50, 51));
        // outputs
        this.addSlotToContainer(new SlotOutput(electrolyzer.inventory, 2, 50, 19));
        this.addSlotToContainer(new SlotOutput(electrolyzer.inventory, 3, 70, 19));
        this.addSlotToContainer(new SlotOutput(electrolyzer.inventory, 4, 90, 19));
        this.addSlotToContainer(new SlotOutput(electrolyzer.inventory, 5, 110, 19));
        
        // battery
        this.addSlotToContainer(new Slot(electrolyzer.inventory, 6, 18, 51));

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
