package techreborn.tiles;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import techreborn.packets.PacketHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileMachineBase extends TileEntity {

	public boolean needsSync = false;
	public int ticksSinceLastSync = 0;

    @Override
    public void updateEntity() {
        super.updateEntity();
		//Force a sync evey 30 seconds
		if(needsSync && ticksSinceLastSync >= 10 || ticksSinceLastSync == 600){
			syncWithAll();
			needsSync = false;
			ticksSinceLastSync = 0;
		}
		ticksSinceLastSync ++;
    }

    @SideOnly(Side.CLIENT)
    public void addWailaInfo(List<String> info) {

    }

    public void syncWithAll() {
        if (!worldObj.isRemote) {
            PacketHandler.sendPacketToAllPlayers(getDescriptionPacket(),
                    worldObj);
        } else {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
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


}
