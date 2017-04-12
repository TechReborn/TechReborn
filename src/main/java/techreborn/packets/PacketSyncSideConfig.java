package techreborn.packets;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.api.tile.IUpgradeable;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;
import reborncore.common.util.ItemNBTHelper;

import java.io.IOException;

/**
 * Created by Mark on 12/04/2017.
 */
public class PacketSyncSideConfig implements INetworkPacket<PacketSyncSideConfig> {

	int slotID;
	int side;
	BlockPos pos;

	public PacketSyncSideConfig(int slotID, int side, BlockPos pos) {
		this.slotID = slotID;
		this.side = side;
		this.pos = pos;
	}

	public PacketSyncSideConfig() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer buffer) throws IOException {
		buffer.writeInt(slotID);
		buffer.writeInt(side);
		buffer.writeBlockPos(pos);
	}

	@Override
	public void readData(ExtendedPacketBuffer buffer) throws IOException {
		slotID = buffer.readInt();
		side = buffer.readInt();
		pos = buffer.readBlockPos();
	}

	@Override
	public void processData(PacketSyncSideConfig message, MessageContext context) {
		TileEntity tileEntity = context.getServerHandler().playerEntity.world.getTileEntity(message.pos);
		if(tileEntity instanceof IUpgradeable){
			ItemStack stack = ((IUpgradeable) tileEntity).getUpgradeInvetory().getStackInSlot(message.slotID);
			ItemNBTHelper.setInt(stack, "side", message.side);
		}
	}
}
