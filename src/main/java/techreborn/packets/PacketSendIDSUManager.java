package techreborn.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import techreborn.tiles.idsu.ClientSideIDSUManager;

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
		json = buffer.readStringFromBuffer(99999999);
		player = readPlayer(in);
	}

	@Override
	public void execute() {
		ClientSideIDSUManager.CLIENT.loadFromString(json, player.worldObj);
	}
}
