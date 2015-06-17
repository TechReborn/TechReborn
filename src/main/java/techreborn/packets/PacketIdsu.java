package techreborn.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import techreborn.tiles.idsu.TileIDSU;

import java.io.IOException;

/**
 * Created by mark on 17/06/15.
 */
public class PacketIdsu extends SimplePacket {


	public PacketIdsu() {
	}

	int buttonID, channel;

	String newName;

	TileIDSU idsu;

	public PacketIdsu(int buttonID, TileIDSU idsu, int channel, String newName) {
		this.idsu = idsu;
		this.buttonID = buttonID;
		this.channel = channel;
		this.newName = newName;
		if(this.newName.equals("")){
			this.newName = "BLANK!!!";
		}
	}

	@Override
	public void writeData(ByteBuf out) throws IOException {
		SimplePacket.writeTileEntity(idsu, out);
		out.writeInt(buttonID);
		out.writeInt(channel);
		PacketBuffer buffer = new PacketBuffer(out);
		buffer.writeStringToBuffer(newName);
	}

	@Override
	public void readData(ByteBuf in) throws IOException {
		this.idsu = (TileIDSU) SimplePacket.readTileEntity(in);
		buttonID = in.readInt();
		channel = in.readInt();
		PacketBuffer buffer = new PacketBuffer(in);
		newName = buffer.readStringFromBuffer(9);
	}

	@Override
	public void execute() {
		if(!idsu.getWorldObj().isRemote){
			idsu.handleGuiInputFromClient(buttonID, channel, player, newName);
		}
	}
}
