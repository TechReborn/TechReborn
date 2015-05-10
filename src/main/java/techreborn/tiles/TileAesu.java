package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.tile.IWrenchable;
import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;

public class TileAesu extends TileEntityElectricBlock implements IWrenchable {

    public static final int MAX_OUTPUT = 8192;
    public static final int MAX_STORAGE = 1000000000; //One billion!
	public Inventory inventory = new Inventory(2, "TileAesu", 64);
	
	public TileAesu()
	{
		super(5, TileAesu.MAX_OUTPUT, TileAesu.MAX_STORAGE);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();

	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
		return false;
	}

	@Override
	public short getFacing()
	{
		return 0;
	}

	@Override
	public void setFacing(short facing)
	{
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		if (entityPlayer.isSneaking())
		{
			return true;
		}
		return false;
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(ModBlocks.Aesu, 1);
	}

	public boolean isComplete()
	{
		return false;
	}

	@Override
	public String getInventoryName()
	{
		return "AESU";
	}

}
