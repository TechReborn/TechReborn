package techreborn.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import reborncore.common.packets.PacketHandler;

public class TileMachineBase extends TileEntity {

    int rotation;

    public void syncWithAll() {
        if (!worldObj.isRemote) {
            PacketHandler.sendPacketToAllPlayers(getDescriptionPacket(),
                    worldObj);
        }
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.getPos().getX(), this.getPos().getY(),
                this.getPos().getZ(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord,
                yCoord, zCoord);
        readFromNBT(packet.func_148857_g());
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
        syncWithAll();
        worldObj.notifyBlockChange(xCoord, yCoord, zCoord, blockType);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (tagCompound.hasKey("meta") && !tagCompound.hasKey("rotation")) {
            rotation = tagCompound.getInteger("meta");
        } else {
            rotation = tagCompound.getInteger("rotation");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("rotation", rotation);
    }
}
