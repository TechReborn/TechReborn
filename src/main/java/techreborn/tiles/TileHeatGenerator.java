package techreborn.tiles;

import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.powerSystem.TilePowerAcceptor;

public class TileHeatGenerator extends TilePowerAcceptor implements IWrenchable {

    public static final int euTick = ConfigTechReborn.heatGeneratorOutput;

    public TileHeatGenerator() {
        super(1);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!worldObj.isRemote) {
            if (worldObj.getBlock(xCoord + 1, yCoord, zCoord) == Blocks.lava) {
                addEnergy(euTick);
            } else if (worldObj.getBlock(xCoord, yCoord, zCoord + 1) == Blocks.lava) {
                addEnergy(euTick);
            } else if (worldObj.getBlock(xCoord, yCoord, zCoord - 1) == Blocks.lava) {
                addEnergy(euTick);
            } else if (worldObj.getBlock(xCoord - 1, yCoord, zCoord) == Blocks.lava) {
                addEnergy(euTick);
            } else if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) == Blocks.lava) {
                addEnergy(euTick);
            }

        }
    }

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
        return false;
    }

    @Override
    public short getFacing() {
        return 0;
    }

    @Override
    public void setFacing(short facing) {
    }

    @Override
    public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
        if (entityPlayer.isSneaking()) {
            return true;
        }
        return false;
    }

    @Override
    public float getWrenchDropRate() {
        return 1.0F;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(ModBlocks.heatGenerator, 1);
    }

    public boolean isComplete() {
        return false;
    }


    @Override
    public double getMaxPower() {
        return 10000;
    }

    @Override
    public boolean canAcceptEnergy(EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canProvideEnergy(EnumFacing direction) {
        return true;
    }

    @Override
    public double getMaxOutput() {
        return 64;
    }

    @Override
    public double getMaxInput() {
        return 0;
    }

//    @Override
//	public void addWailaInfo(List<String> info)
//	{
//		super.addWailaInfo(info);
//		info.add("Power Generarating " + euTick +" EU/t");
//
//	}

}
