package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import reborncore.client.gui.BaseSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.client.gui.SlotOutput;
import techreborn.tiles.TileImplosionCompressor;

public class ContainerImplosionCompressor extends ContainerCrafting
{

	public int tickTime;
	public int multIBlockState = 0;
	EntityPlayer player;
	TileImplosionCompressor tile;

	public ContainerImplosionCompressor(TileImplosionCompressor tilecompressor, EntityPlayer player)
	{
		super(tilecompressor.crafter);
		tile = tilecompressor;
		this.player = player;

		// input
		this.addSlotToContainer(new BaseSlot(tilecompressor.inventory, 0, 37, 26));
		this.addSlotToContainer(new BaseSlot(tilecompressor.inventory, 1, 37, 44));
		// outputs
		this.addSlotToContainer(new SlotOutput(tilecompressor.inventory, 2, 93, 35));
		this.addSlotToContainer(new SlotOutput(tilecompressor.inventory, 3, 111, 35));

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new BaseSlot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new BaseSlot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (int i = 0; i < this.listeners.size(); i++)
		{
			ICrafting icrafting = this.listeners.get(i);
			if (this.multIBlockState != getMultIBlockStateint())
			{
				icrafting.sendProgressBarUpdate(this, 3, getMultIBlockStateint());
			}
		}
	}

	@Override
	public void onCraftGuiOpened(ICrafting crafting)
	{
		super.onCraftGuiOpened(crafting);
		crafting.sendProgressBarUpdate(this, 3, getMultIBlockStateint());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value)
	{
		if (id == 3)
		{
			this.multIBlockState = value;
		}
	}

	public int getMultIBlockStateint()
	{
		return tile.getMutliBlock() ? 1 : 0;
	}

}
