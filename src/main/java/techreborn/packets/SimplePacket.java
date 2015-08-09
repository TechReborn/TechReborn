package techreborn.packets;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.io.IOException;

public abstract class SimplePacket {
    protected EntityPlayer player;
    protected byte mode;

    public SimplePacket(EntityPlayer _player) {
        player = _player;
    }

    @SuppressWarnings("unused")
    public SimplePacket() {
        player = null;
    }

    public static String readString(ByteBuf in) throws IOException {
        byte[] stringBytes = new byte[in.readInt()];
        in.readBytes(stringBytes);
        return new String(stringBytes, Charsets.UTF_8);
    }

    public static void writeString(String string, ByteBuf out)
            throws IOException {
        byte[] stringBytes;
        stringBytes = string.getBytes(Charsets.UTF_8);
        out.writeInt(stringBytes.length);
        out.writeBytes(stringBytes);
    }

    public static World readWorld(ByteBuf in) throws IOException {
        return DimensionManager.getWorld(in.readInt());
    }

    public static void writeWorld(World world, ByteBuf out) throws IOException {
        out.writeInt(world.provider.dimensionId);
    }

    public static EntityPlayer readPlayer(ByteBuf in) throws IOException {
        if (!in.readBoolean())
            return null;
        World playerWorld = readWorld(in);
        return playerWorld.getPlayerEntityByName(readString(in));
    }

    public static void writePlayer(EntityPlayer player, ByteBuf out)
            throws IOException {
        if (player == null) {
            out.writeBoolean(false);
            return;
        }
        out.writeBoolean(true);
        writeWorld(player.worldObj, out);
        writeString(player.getCommandSenderName(), out);
    }

    public static TileEntity readTileEntity(ByteBuf in) throws IOException {
        return readWorld(in).getTileEntity(in.readInt(), in.readInt(),
                in.readInt());
    }

    public static void writeTileEntity(TileEntity tileEntity, ByteBuf out)
            throws IOException {
        writeWorld(tileEntity.getWorldObj(), out);
        out.writeInt(tileEntity.xCoord);
        out.writeInt(tileEntity.yCoord);
        out.writeInt(tileEntity.zCoord);
    }

    public static Fluid readFluid(ByteBuf in) throws IOException {
        return FluidRegistry.getFluid(readString(in));
    }

    public static void writeFluid(Fluid fluid, ByteBuf out) throws IOException {
        if (fluid == null) {
            writeString("", out);
            return;
        }
        writeString(fluid.getName(), out);
    }

    public void writePacketData(ByteBuf out) throws IOException {
        out.writeByte(mode);
        writePlayer(player, out);
        writeData(out);
    }

    public abstract void writeData(ByteBuf out) throws IOException;

    public void readPacketData(ByteBuf in) throws IOException {
        mode = in.readByte();
        player = readPlayer(in);
        readData(in);
    }

    public abstract void readData(ByteBuf in) throws IOException;

    public abstract void execute();

    public void sendPacketToServer() {
        PacketHandler.sendPacketToServer(this);
    }

    public void sendPacketToPlayer(EntityPlayer player) {
        PacketHandler.sendPacketToPlayer(this, player);
    }

    public void sendPacketToAllPlayers() {
        PacketHandler.sendPacketToAllPlayers(this);
    }

}
