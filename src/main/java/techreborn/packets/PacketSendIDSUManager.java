package techreborn.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import techreborn.client.gui.GuiIDSU;
import techreborn.cofhLib.gui.element.listbox.ListBoxElementText;
import techreborn.tiles.idsu.ClientSideIDSUManager;
import techreborn.tiles.idsu.IDSUManager;

import java.io.IOException;

public class PacketSendIDSUManager extends AbstractPacket {

	String json;

	public PacketSendIDSUManager() {
	}

	public PacketSendIDSUManager(String json) {
		this.json = json;
	}

	@Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf out) {
		PacketBuffer buffer = new PacketBuffer(out);
        try {
            buffer.writeStringToBuffer(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf in) {
		PacketBuffer buffer = new PacketBuffer(in);
        try {
            json = buffer.readStringFromBuffer(9999999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
    public void handleClientSide(EntityPlayer player) {
		ClientSideIDSUManager.CLIENT.loadFromString(json, player.worldObj);

		if(player.worldObj != null){
			GuiIDSU.listBox._elements.clear();
			for(World world : ClientSideIDSUManager.CLIENT.worldData.keySet()){
				IDSUManager.IDSUWorldSaveData saveData = ClientSideIDSUManager.CLIENT.getWorldDataFormWorld(world);
				for(Integer id : saveData.idsuValues.keySet()){
					IDSUManager.IDSUValueSaveData valueSaveData = saveData.idsuValues.get(id);
					System.out.println("added " + valueSaveData.name + " - " + id);
					if(!valueSaveData.name.isEmpty()){
						GuiIDSU.listBox._elements.add(new ListBoxElementText(valueSaveData.name + " - " + id));
					} else {
						GuiIDSU.listBox._elements.add(new ListBoxElementText(id.toString()));
					}
				}
			}
		}
	}

    @Override
    public void handleServerSide(EntityPlayer player) {

    }
}
