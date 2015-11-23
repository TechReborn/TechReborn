package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.container.RebornContainer;
import techreborn.tiles.idsu.TileIDSU;


public class ContainerIDSU extends RebornContainer {

    EntityPlayer player;

    TileIDSU tile;

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public int euOut;
    public int storedEu;
    public int euChange;
    public int channel;

    public ContainerIDSU(TileIDSU tileIDSU,
                         EntityPlayer player) {
        tile = tileIDSU;
        this.player = player;

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
            if (this.euOut != tile.output) {
                icrafting.sendProgressBarUpdate(this, 0, tile.output);
            }
            if (this.storedEu != (int) tile.getEnergy()) {
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
        crafting.sendProgressBarUpdate(this, 0, tile.output);
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
        } else if (id == 3) {
            this.channel = value;
        }
    }

}
