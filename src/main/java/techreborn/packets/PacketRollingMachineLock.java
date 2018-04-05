package techreborn.packets;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;
import techreborn.tiles.TileRollingMachine;

import java.io.IOException;

public class PacketRollingMachineLock implements INetworkPacket<PacketRollingMachineLock> {

	BlockPos machinePos;
	boolean locked;

	public PacketRollingMachineLock(TileRollingMachine machine, boolean locked) {
		this.machinePos = machine.getPos();
		this.locked = locked;
	}

	public PacketRollingMachineLock() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer buffer) throws IOException {
		buffer.writeBlockPos(machinePos);
		buffer.writeBoolean(locked);
	}

	@Override
	public void readData(ExtendedPacketBuffer buffer) throws IOException {
		machinePos = buffer.readBlockPos();
		locked = buffer.readBoolean();
	}

	@Override
	public void processData(PacketRollingMachineLock message, MessageContext context) {
		TileEntity tileEntity = context.getServerHandler().player.world.getTileEntity(machinePos);
		if(tileEntity instanceof TileRollingMachine){
			((TileRollingMachine) tileEntity).locked = locked;
		}
	}
}
