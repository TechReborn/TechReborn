package techreborn.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import reborncore.common.packets.PacketHandler;
import techreborn.blocks.BlockMachineBase;

public class TileMachineBase extends TileEntity implements ITickable {

    public void syncWithAll() {
        if (!worldObj.isRemote) {
            PacketHandler.sendPacketToAllPlayers(getDescriptionPacket(),
                    worldObj);
        }
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        worldObj.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(),
                getPos().getY(), getPos().getZ());
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void update() {
        updateEntity();
    }

    @Deprecated
    public void updateEntity() {

    }

    public int getFacingInt() {
        Block block = worldObj.getBlockState(pos).getBlock();
        if(block instanceof BlockMachineBase){
            return ((BlockMachineBase) block).getFacing(worldObj.getBlockState(pos)).getIndex();
        }
        return 0;
    }

    public EnumFacing getFacingEnum() {
        Block block = worldObj.getBlockState(pos).getBlock();
        if(block instanceof BlockMachineBase){
            return ((BlockMachineBase) block).getFacing(worldObj.getBlockState(pos));
        }
        return null;
    }


    public void setFacing(EnumFacing enumFacing) {
        Block block = worldObj.getBlockState(pos).getBlock();
        if(block instanceof BlockMachineBase){
            ((BlockMachineBase) block).setFacing(enumFacing, worldObj, pos);
        }
    }


    //This stops the tile from getting cleared when the state is updated(rotation and on/off)
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        if(oldState.getBlock() != newSate.getBlock()){
            return true;
        }
        return false;
    }
}
