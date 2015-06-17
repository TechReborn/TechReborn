package techreborn.tiles.idsu;

import net.minecraft.entity.player.EntityPlayer;
import techreborn.packets.PacketHandler;
import techreborn.tiles.TileMachineBase;

public class TileIDSU extends TileMachineBase {

	public int channelID = 0;

	public void handleGuiInputFromClient(int buttonID, int channel, EntityPlayer player, String name) {
		if(buttonID == 4){
			channelID = channel;
			IDSUManager.INSTANCE.getSaveDataForWorld(worldObj, channel);//Do this to init the save data :)
			if(name != "BLANK!!!"){
				IDSUManager.INSTANCE.getSaveDataForWorld(worldObj, channel).name = name;
			}
			if(worldObj.isRemote){
				PacketHandler.sendPacketToPlayer(IDSUManager.INSTANCE.getPacket(worldObj, player), player);
			}
		}
	}
}
