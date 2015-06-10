package techreborn.client.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import techreborn.tiles.TileAesu;

public class ContainerAesu extends TechRebornContainer {

	EntityPlayer player;

	TileAesu tile;

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	public int euOut;
	public int storedEu;

	public ContainerAesu(TileAesu tileaesu,
			EntityPlayer player)
	{
		tile = tileaesu;
		this.player = player;

		// input
		this.addSlotToContainer(new Slot(tileaesu.inventory, 0, 128, 14));
		this.addSlotToContainer(new Slot(tileaesu.inventory, 1, 128, 50));

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9
						+ 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18,
					142));
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < this.crafters.size(); i++) {
			ICrafting icrafting = (ICrafting)this.crafters.get(i);
			if(this.euOut != tile.output){
				icrafting.sendProgressBarUpdate(this, 0, tile.output);
			}
			if(this.storedEu != tile.energy){
				icrafting.sendProgressBarUpdate(this, 1, (int) tile.energy);
			}
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting crafting) {
		super.addCraftingToCrafters(crafting);
		crafting.sendProgressBarUpdate(this, 0, tile.output);
		crafting.sendProgressBarUpdate(this, 1, (int) tile.energy);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value) {
		if(id == 0){
			this.euOut = value;
		} else if(id == 1){
			this.storedEu = value;
		}
	}

}
