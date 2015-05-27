package techreborn.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import techreborn.packets.PacketHandler;
import techreborn.packets.PacketSync;
import techreborn.packets.SimplePacket;

import java.util.List;

public class TileMachineBase extends TileEntity {

	public boolean needsSync = false;
	public int ticksSinceLastSync = 0;

    @Override
    public void updateEntity() {
        super.updateEntity();
		//Force a sync evey 10 seconds
		if(needsSync && !worldObj.isRemote){
			syncWithAll();
		}
		ticksSinceLastSync ++;
    }

    @SideOnly(Side.CLIENT)
    public void addWailaInfo(List<String> info) {

    }

    public void syncWithAll() {
        if (!worldObj.isRemote) {
            //PacketHandler.sendPacketToAllPlayers(getSyncPacket());
            PacketHandler.sendPacketToAllPlayers(getDescriptionPacket2(),
                    worldObj);
        } else {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
        needsSync = false;
        ticksSinceLastSync = 0;
    }

    public Packet getDescriptionPacket2() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeSyncToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
                this.zCoord, 1, nbtTag);
    }

    public SimplePacket getSyncPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeSyncToNBT(nbtTag);
        return new PacketSync(nbtTag, xCoord, yCoord, zCoord);
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
                this.zCoord, 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord,
                yCoord, zCoord);
        readFromNBT(packet.func_148857_g());
    }

    public void readSyncFromNBT(NBTTagCompound tagCompound) {

    }

    public void writeSyncToNBT(NBTTagCompound tagCompound) {

    }


}
