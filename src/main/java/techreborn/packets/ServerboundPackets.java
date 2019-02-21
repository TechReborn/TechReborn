package techreborn.packets;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.NetworkPacket;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;
import techreborn.tiles.machine.tier1.TileAutoCraftingTable;
import techreborn.tiles.machine.tier1.TileRollingMachine;
import techreborn.tiles.storage.TileAdjustableSU;
import techreborn.tiles.storage.idsu.TileInterdimensionalSU;

public class ServerboundPackets {

	public static final ResourceLocation AESU = new ResourceLocation("techreborn", "aesu");
	public static final ResourceLocation AUTO_CRAFTING_LOCK = new ResourceLocation("techreborn", "auto_crafting_lock");
	public static final ResourceLocation ROLLING_MACHINE_LOCK = new ResourceLocation("techreborn", "rolling_machine_lock");
	public static final ResourceLocation FUSION_CONTROL_SIZE = new ResourceLocation("techreborn", "fusion_control_size");
	public static final ResourceLocation IDSU = new ResourceLocation("techreborn", "idsu");

	public static void init() {
		NetworkManager.registerPacketHandler(AESU, (extendedPacketBuffer, context) -> {
			BlockPos pos = extendedPacketBuffer.readBlockPos();
			int buttonID = extendedPacketBuffer.readInt();
			context.enqueueWork(() -> {
				TileEntity tile = context.getSender().world.getTileEntity(pos);
				if (tile instanceof TileAdjustableSU) {
					((TileAdjustableSU) tile).handleGuiInputFromClient(buttonID);
				}
			});
		});

		NetworkManager.registerPacketHandler(AUTO_CRAFTING_LOCK, (extendedPacketBuffer, context) -> {
			BlockPos machinePos = extendedPacketBuffer.readBlockPos();
			boolean locked = extendedPacketBuffer.readBoolean();
			context.enqueueWork(() -> {
				TileEntity tileEntity = context.getSender().world.getTileEntity(machinePos);
				if (tileEntity instanceof TileAutoCraftingTable) {
					((TileAutoCraftingTable) tileEntity).locked = locked;
				}
			});
		});

		NetworkManager.registerPacketHandler(FUSION_CONTROL_SIZE, (extendedPacketBuffer, context) -> {
			int sizeDelta = extendedPacketBuffer.readInt();
			BlockPos pos = extendedPacketBuffer.readBlockPos();
			context.enqueueWork(() -> {
				TileEntity tile = context.getSender().world.getTileEntity(pos);
				if (tile instanceof TileFusionControlComputer) {
					((TileFusionControlComputer) tile).changeSize(sizeDelta);
				}
			});
		});

		NetworkManager.registerPacketHandler(IDSU, (extendedPacketBuffer, context) -> {
			BlockPos pos = extendedPacketBuffer.readBlockPos();
			int buttonID = extendedPacketBuffer.readInt();
			context.enqueueWork(() -> {
				//TODO was commented out when I ported it, so ill leave it here, needs looking into tho
				//		if (!pos.getWorld().isRemote) {
				//			pos.handleGuiInputFromClient(buttonID);
				//		}
			});
		});

		NetworkManager.registerPacketHandler(ROLLING_MACHINE_LOCK, (extendedPacketBuffer, context) -> {
			BlockPos machinePos = extendedPacketBuffer.readBlockPos();
			boolean locked = extendedPacketBuffer.readBoolean();
			context.enqueueWork(() -> {
				TileEntity tileEntity = context.getSender().world.getTileEntity(machinePos);
				if (tileEntity instanceof TileRollingMachine) {
					((TileRollingMachine) tileEntity).locked = locked;
				}
			});
		});
	}

	public static NetworkPacket createPacketAesu(int buttonID, TileAdjustableSU tile) {
		return NetworkManager.createPacket(AESU, extendedPacketBuffer -> {
			extendedPacketBuffer.writeBlockPos(tile.getPos());
			extendedPacketBuffer.writeInt(buttonID);
		});
	}

	public static NetworkPacket createPacketAutoCraftingTableLock(TileAutoCraftingTable machine, boolean locked) {
		return NetworkManager.createPacket(AUTO_CRAFTING_LOCK, extendedPacketBuffer -> {
			extendedPacketBuffer.writeBlockPos(machine.getPos());
			extendedPacketBuffer.writeBoolean(locked);
		});
	}

	public static NetworkPacket createPacketFusionControlSize(int sizeDelta, BlockPos pos) {
		return NetworkManager.createPacket(FUSION_CONTROL_SIZE, extendedPacketBuffer -> {
			extendedPacketBuffer.writeInt(sizeDelta);
			extendedPacketBuffer.writeBlockPos(pos);
		});
	}

	public static NetworkPacket createPacketIdsu(int buttonID, TileInterdimensionalSU tile) {
		return NetworkManager.createPacket(IDSU, extendedPacketBuffer -> {
			extendedPacketBuffer.writeBlockPos(tile.getPos());
			extendedPacketBuffer.writeInt(buttonID);
		});
	}

	public static NetworkPacket createPacketRollingMachineLock(TileRollingMachine machine, boolean locked) {
		return NetworkManager.createPacket(ROLLING_MACHINE_LOCK, extendedPacketBuffer -> {
			extendedPacketBuffer.writeBlockPos(machine.getPos());
			extendedPacketBuffer.writeBoolean(locked);
		});
	}

}
