package techreborn.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import techreborn.client.gui.GuiIDSU;
import techreborn.cofhLib.gui.element.listbox.ListBoxElementText;
import techreborn.tiles.idsu.ClientSideIDSUManager;
import techreborn.tiles.idsu.IDSUManager;

import java.io.IOException;

public class PacketSendIDSUManager extends SimplePacket {

	String json;

	public PacketSendIDSUManager() {
	}

	public PacketSendIDSUManager(String json, EntityPlayer player) {
		this.json = json;
		this.player = player;
	}

	@Override
	public void writeData(ByteBuf out) throws IOException {
		PacketBuffer buffer = new PacketBuffer(out);
		buffer.writeStringToBuffer(json);
		writePlayer(player, out);
	}

	@Override
	public void readData(ByteBuf in) throws IOException {
		PacketBuffer buffer = new PacketBuffer(in);
		json = buffer.readStringFromBuffer(Integer.MAX_VALUE / 4);
		player = readPlayer(in);
	}

	@Override
	public void execute() {
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
}
