package techreborn.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import reborncore.common.packets.SimplePacket;
import techreborn.tiles.TileAesu;

public class PacketAesu extends SimplePacket {


    public PacketAesu() {
    }

    int buttonID;

    TileAesu aesu;

    public PacketAesu(int buttonID, TileAesu aesu) {
        this.aesu = aesu;
        this.buttonID = buttonID;
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {
        SimplePacket.writeTileEntity(aesu, out);
        out.writeInt(buttonID);
    }

    @Override
    public void readData(ByteBuf in) throws IOException {
        this.aesu = (TileAesu) SimplePacket.readTileEntity(in);
        buttonID = in.readInt();
    }

    @Override
    public void execute() {
        if (!aesu.getWorld().isRemote) {
            aesu.handleGuiInputFromClient(buttonID);
        }
    }
}
