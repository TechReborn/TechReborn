package techreborn.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import techreborn.packets.PacketHandler;

import java.util.List;

public class TileMachineBase extends TileEntity {

    @Override
    public void updateEntity() {
        super.updateEntity();
        //TODO make this happen less
        //syncWithAll();
    }

    @SideOnly(Side.CLIENT)
	public void addWailaInfo(List<String> info) {

	}

    public void syncWithAll() {
        if (!worldObj.isRemote) {
            PacketHandler.sendPacketToAllPlayers(getDescriptionPacket(), worldObj);
        }
    }
}
