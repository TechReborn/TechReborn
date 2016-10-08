package techreborn.packets;

import io.netty.buffer.ByteBuf;
import reborncore.common.packets.SimplePacket;
import techreborn.tiles.idsu.TileIDSU;

import java.io.IOException;

public class PacketIdsu extends SimplePacket {

	int buttonID;
	TileIDSU idsu;

	public PacketIdsu() {
	}

	public PacketIdsu(int buttonID, TileIDSU aesu) {
		this.idsu = aesu;
		this.buttonID = buttonID;
	}

	@Override
	public void writeData(ByteBuf out) throws IOException {
		SimplePacket.writeTileEntity(idsu, out);
		out.writeInt(buttonID);
	}

	@Override
	public void readData(ByteBuf in) throws IOException {
		this.idsu = (TileIDSU) SimplePacket.readTileEntity(in);
		buttonID = in.readInt();
	}

	@Override
	public void execute() {
		if (!idsu.getWorld().isRemote) {
			idsu.handleGuiInputFromClient(buttonID);
		}
	}
}
