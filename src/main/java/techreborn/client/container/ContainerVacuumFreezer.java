package techreborn.client.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import reborncore.client.gui.SlotOutput;
import techreborn.tiles.TileVacuumFreezer;

public class ContainerVacuumFreezer extends ContainerCrafting {

    EntityPlayer player;

    TileVacuumFreezer tile;

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public int tickTime;
    public int machineStatus;

    public ContainerVacuumFreezer(TileVacuumFreezer tileAlloysmelter,
                                 EntityPlayer player) {
        super(tileAlloysmelter.crafter);
        tile = tileAlloysmelter;
        this.player = player;

        // input
        this.addSlotToContainer(new Slot(tileAlloysmelter.inventory, 0, 56, 34));
        // outputs
        this.addSlotToContainer(new SlotOutput(tileAlloysmelter.inventory, 1, 116, 35));

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
            if (this.machineStatus != tile.multiBlockStatus) {
                icrafting.sendProgressBarUpdate(this, 3, tile.multiBlockStatus);
            }
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 3, tile.multiBlockStatus);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int value) {
        super.updateProgressBar(id, value);
         if (id == 3) {
            machineStatus = value;
        }
    }

}
