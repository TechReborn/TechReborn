package techreborn.tiles;

import codechicken.microblock.FaceEdgeGrid;
import ic2.api.tile.IWrenchable;
import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;
import techreborn.util.LogHelper;

import java.lang.reflect.Field;

public class TileAesu extends TileEntityElectricBlock implements IWrenchable {

    public static final int MAX_OUTPUT = 8192;
    public static final int MAX_STORAGE = 1000000000; //One billion!
	public Inventory inventory = new Inventory(2, "TileAesu", 64);
	public int OUTPUT = 64; //The current output
	
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
		return true;
	}

	@Override
	public short getFacing()
	{
		return (short) getBlockMetadata();
	}

	@Override
	public void setFacing(short facing)
	{
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, facing, 2);
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

	public void handleGuiInputFromClient(int id){
		if(id == 0){
			OUTPUT += 256;
		}
		if(id == 1){
			OUTPUT += 64;
		}
		if(id == 2){
			OUTPUT -= 64;
		}
		if(id == 3){
			OUTPUT -= 256;
		}
		if(OUTPUT > MAX_OUTPUT){
			OUTPUT = MAX_OUTPUT;
		}
		if(OUTPUT < 0){
			OUTPUT = 0;
		}

		//TODO make a better way and not use reflection for this.
		try {
			Field field = getClass().getSuperclass().getDeclaredField("output");
			field.setAccessible(true);
			field.set(this, OUTPUT);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		LogHelper.info("Set output to " + getOutput());
	}

}
