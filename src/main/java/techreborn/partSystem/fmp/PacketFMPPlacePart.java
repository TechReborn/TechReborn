package techreborn.partSystem.fmp;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import techreborn.packets.AbstractPacket;

public class PacketFMPPlacePart extends AbstractPacket {


    public PacketFMPPlacePart() {
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {

    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {

    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {
        CableConverter.place(player, player.worldObj);
    }
}
