package techreborn.packets;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;

import java.io.IOException;

public class PacketFusionControlSize implements INetworkPacket<PacketFusionControlSize> {

	int sizeDelta;
	BlockPos pos;

	public PacketFusionControlSize(int sizeDelta, BlockPos pos) {
		this.sizeDelta = sizeDelta;
		this.pos = pos;
	}

	public PacketFusionControlSize() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer buffer) throws IOException {
		buffer.writeInt(sizeDelta);
		buffer.writeBlockPos(pos);
	}

	@Override
	public void readData(ExtendedPacketBuffer buffer) throws IOException {
		sizeDelta = buffer.readInt();
		pos = buffer.readBlockPos();
	}

	@Override
	public void processData(PacketFusionControlSize message, MessageContext context) {
		TileEntity tile = context.getServerHandler().player.world.getTileEntity(message.pos);
		if(tile instanceof TileFusionControlComputer){
			((TileFusionControlComputer) tile).changeSize(sizeDelta);
		}
	}
}
