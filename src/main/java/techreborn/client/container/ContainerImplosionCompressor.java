package techreborn.client.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import reborncore.client.gui.SlotOutput;
import techreborn.tiles.TileImplosionCompressor;

public class ContainerImplosionCompressor extends ContainerCrafting {

    EntityPlayer player;

    TileImplosionCompressor tile;
    public int multiblockmeta = 0;
    public int tickTime;

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
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < this.crafters.size(); i++) {
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			if (this.multiblockmeta != getMultiblockstateint()) {
				icrafting.sendProgressBarUpdate(this, 3, getMultiblockstateint());
			}
		}
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting crafting) {
		super.addCraftingToCrafters(crafting);
		crafting.sendProgressBarUpdate(this, 3, getMultiblockstateint());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value) {
		if (id == 3) {
			this.multiblockmeta = value;
		}
	}

	public int getMultiblockstateint() {
		return tile.getMutliBlock() ? 1 : 0;
	}

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
