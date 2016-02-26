package techreborn.tiles;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;


public class TilePlayerDectector extends TilePowerAcceptor {

    boolean redstone = false;
    public String owenerUdid = "";

    public TilePlayerDectector() {
        super(1);
    }

    @Override
    public double getMaxPower() {
        return 10000;
    }

    @Override
    public boolean canAcceptEnergy(EnumFacing direction) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(EnumFacing direction) {
        return false;
    }

    @Override
    public double getMaxOutput() {
        return 0;
    }

    @Override
    public double getMaxInput() {
        return 32;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(!worldObj.isRemote && worldObj.getWorldTime() % 20 == 0){
            boolean lastRedstone = redstone;
            redstone = false;
            if(canUseEnergy(50)){
                Iterator tIterator = super.worldObj.playerEntities.iterator();
                while (tIterator.hasNext()) {
                    EntityPlayer player = (EntityPlayer) tIterator.next();
                    if (player.getDistanceSq((double) super.getPos().getX() + 0.5D, (double) super.getPos().getY() + 0.5D, (double) super.getPos().getZ() + 0.5D) <= 256.0D) {
                        BlockMachineBase blockMachineBase = (BlockMachineBase) worldObj.getBlockState(pos).getBlock();
                        int meta = blockMachineBase.getMetaFromState(worldObj.getBlockState(pos));
                        if(meta == 0){//ALL
                            redstone = true;
                        } else if (meta == 1){//Others
                            if(!owenerUdid.isEmpty() && !owenerUdid.equals(player.getUniqueID().toString())){
                                redstone = true;
                            }
                        } else {//You
                            if(!owenerUdid.isEmpty() &&owenerUdid.equals(player.getUniqueID().toString())){
                                redstone = true;
                            }
                        }
                        redstone = true;
                    }
                }
                useEnergy(50);
            }
            if(lastRedstone != redstone){
                worldObj.markBlockForUpdate(getPos());
                worldObj.notifyNeighborsOfStateChange(getPos(), worldObj.getBlockState(getPos()).getBlock());
            }
        }
    }

    public boolean isProvidingPower(){
        return redstone;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        owenerUdid = tag.getString("ownerID");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("ownerID", owenerUdid);
    }
}
