package techreborn.partSystem.fmp;


import io.netty.buffer.ByteBuf;
import reborncore.common.packets.SimplePacket;

import java.io.IOException;

public class PacketFMPPlacePart extends SimplePacket {


    public PacketFMPPlacePart() {
    }

    @Override
    public void writeData(ByteBuf out) throws IOException {

    }

    @Override
    public void readData(ByteBuf in) throws IOException {

    }

    @Override
    public void execute() {
        CableConverter.place(player, player.worldObj);
    }
}
