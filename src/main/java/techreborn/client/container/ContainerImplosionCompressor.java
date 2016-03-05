package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.client.gui.SlotOutput;
import techreborn.tiles.TileImplosionCompressor;

public class ContainerImplosionCompressor extends ContainerCrafting {

    EntityPlayer player;

    TileImplosionCompressor tile;

    public int tickTime;
    public int multiblockstate = 0;

    public ContainerImplosionCompressor(TileImplosionCompressor tilecompressor,
                                        EntityPlayer player) {
        super(tilecompressor.crafter);
        tile = tilecompressor;
        this.player = player;

        // input
        this.addSlotToContainer(new Slot(tilecompressor.inventory, 0, 37, 26));
        this.addSlotToContainer(new Slot(tilecompressor.inventory, 1, 37, 44));
        // outputs
        this.addSlotToContainer(new SlotOutput(tilecompressor.inventory, 2, 93, 35));
        this.addSlotToContainer(new SlotOutput(tilecompressor.inventory, 3, 111, 35));


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
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (this.multiblockstate != getMultiblockstateint()) {
                icrafting.sendProgressBarUpdate(this, 3, getMultiblockstateint());
            }
        }
    }

    @Override
    public void onCraftGuiOpened(ICrafting crafting) {
        super.onCraftGuiOpened(crafting);
        crafting.sendProgressBarUpdate(this, 3, getMultiblockstateint());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int value) {
        if (id == 3) {
            this.multiblockstate = value;
        }
    }

    public int getMultiblockstateint(){
        return tile.getMutliBlock() ? 1 : 0;
    }


}
