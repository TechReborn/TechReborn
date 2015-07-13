package techreborn.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import techreborn.tiles.idsu.TileIDSU;

import java.io.IOException;

public class PacketIdsu extends AbstractPacket {
	public PacketIdsu() {
	}

	int buttonID, channel;

	String newName;

	TileIDSU idsu;

    int x, y, z;

	public PacketIdsu(int buttonID, TileIDSU idsu, int channel, String newName) {
		this.idsu = idsu;
		this.buttonID = buttonID;
		this.channel = channel;
		this.newName = newName;
	}


    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf out) {
        out.writeInt(idsu.xCoord);
        out.writeInt(idsu.yCoord);
        out.writeInt(idsu.zCoord);
        out.writeInt(buttonID);
        out.writeInt(channel);
        PacketBuffer buffer = new PacketBuffer(out);
        try {
            buffer.writeStringToBuffer(newName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf in) {
        this.x = in.readInt();
        this.y = in.readInt();
        this.z = in.readInt();
        buttonID = in.readInt();
        channel = in.readInt();
        PacketBuffer buffer = new PacketBuffer(in);
        try {
            newName = buffer.readStringFromBuffer(9999999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {
        idsu = (TileIDSU) player.getEntityWorld().getTileEntity(x, y, z);
        idsu.handleGuiInputFromClient(buttonID, channel, player, newName);
    }
}
