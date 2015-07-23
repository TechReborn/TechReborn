package techreborn.tiles;

import ic2.api.energy.EnergyNet;
import ic2.api.tile.IWrenchable;
import ic2.core.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import techreborn.blocks.storage.EUStorageTile;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;
import techreborn.util.LogHelper;

public class TileAesu extends EUStorageTile implements IWrenchable {

    public static final int MAX_OUTPUT = ConfigTechReborn.aesuMaxOutput;
    public static final int MAX_STORAGE = ConfigTechReborn.aesuMaxStorage;
	public Inventory inventory = new Inventory(2, "TileAesu", 64);
	private int OUTPUT = 64; //The current output
    private double euLastTick = 0;
    private double euChange;
    private int ticks;
	
	public TileAesu()
	{
		super(5, TileAesu.MAX_OUTPUT, TileAesu.MAX_STORAGE);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
        if(ticks == ConfigTechReborn.aveargeEuOutTickTime){
            euChange = -1;
            ticks = 0;

        } else {
            ticks ++;
            euChange += energy - euLastTick;
            if(euLastTick == energy){
                euChange = 0;
            }
        }

        euLastTick = energy;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
        if(!entityPlayer.isSneaking()){
            return true;
        }
		return false;
	}

	@Override
	public short getFacing()
	{
		return super.getFacing();
	}

	@Override
	public void setFacing(short facing)
	{
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, facing, 2);
		super.setFacing(facing);
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
		return getDropWithNBT();
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
		if(OUTPUT <= -1){
			OUTPUT = 0;
		}
        output = OUTPUT;
		LogHelper.debug("Set output to " + getOutput());
	}

    public double getEuChange(){
        if(euChange == -1){
            return -1;
        }
        return (euChange / ticks);
    }

    public ItemStack getDropWithNBT()
    {
        NBTTagCompound tileEntity = new NBTTagCompound();
        ItemStack dropStack = new ItemStack(ModBlocks.Aesu, 1);
        writeToNBTWithoutCoords(tileEntity);
        dropStack.setTagCompound(new NBTTagCompound());
        dropStack.stackTagCompound.setTag("tileEntity", tileEntity);
        return dropStack;
    }

    public void writeToNBTWithoutCoords(NBTTagCompound tagCompound)
    {
        tagCompound.setDouble("energy", this.energy);
        tagCompound.setDouble("euChange", euChange);
        tagCompound.setDouble("euLastTick", euLastTick);
        tagCompound.setBoolean("active", this.getActive());
        tagCompound.setByte("redstoneMode", this.redstoneMode);
        inventory.writeToNBT(tagCompound);
    }

    public void readFromNBTWithoutCoords(NBTTagCompound nbttagcompound) {
        this.energy = Util.limit(nbttagcompound.getDouble("energy"), 0.0D, (double) this.maxStorage + EnergyNet.instance.getPowerFromTier(this.tier));
        this.redstoneMode = nbttagcompound.getByte("redstoneMode");
        this.euChange = nbttagcompound.getDouble("euChange");
        this.euLastTick = nbttagcompound.getDouble("euLastTick");
        inventory.readFromNBT(nbttagcompound);
    }


}
