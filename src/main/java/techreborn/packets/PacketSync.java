package techreborn.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import techreborn.tiles.TileMachineBase;
import techreborn.util.ClientUtil;
import techreborn.util.LogHelper;

import java.io.IOException;


public class PacketSync extends SimplePacket {

    private NBTTagCompound nbttagcompound;
    private int x, y, z;

    public PacketSync() {
    }

    public PacketSync(NBTTagCompound nbttagcompound, int x, int y, int z) {
        this.nbttagcompound = nbttagcompound;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void writeData(ByteBuf data) throws IOException{

        try {
            byte[] compressed = CompressedStreamTools.compress(nbttagcompound);
            if (compressed.length > 65535) {
                LogHelper.error("NBT data is too large (" + compressed.length + " > 65535)! Please report!");
            }
            data.writeShort(compressed.length);
            data.writeBytes(compressed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readData(ByteBuf data) throws IOException{
        int length = data.readUnsignedShort();
        byte[] compressed = new byte[length];
        data.readBytes(compressed);

        try {
            this.nbttagcompound = CompressedStreamTools.func_152457_a(compressed, NBTSizeTracker.field_152451_a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NBTTagCompound getTagCompound() {
        return this.nbttagcompound;
    }

    @Override
    public void execute() {
        TileEntity tile = ClientUtil.getWorld().getTileEntity(x, y, z);
        if(tile instanceof TileMachineBase){
            ((TileMachineBase) tile).readSyncFromNBT(getTagCompound());
        }
    }
}
