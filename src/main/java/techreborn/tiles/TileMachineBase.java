package techreborn.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import techreborn.packets.PacketHandler;

import java.util.List;

public abstract class TileMachineBase extends TileEntity {

	@Deprecated
	/**
	 * Try not to use this
	 */
	public void syncWithAll() {
		if (!worldObj.isRemote) {
			PacketHandler.sendPacketToAllPlayers(getDescriptionPacket(),
					worldObj);
		}
	}

    @SideOnly(Side.CLIENT)
    public void addWailaInfo(List<String> info) {

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
