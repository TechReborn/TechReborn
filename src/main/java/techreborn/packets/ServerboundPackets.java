/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.packets;

import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.NetworkPacket;
import techreborn.init.TRContent;
import techreborn.items.ItemManual;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;
import techreborn.tiles.machine.tier1.TileAutoCraftingTable;
import techreborn.tiles.machine.tier1.TileRollingMachine;
import techreborn.tiles.storage.TileAdjustableSU;
import techreborn.tiles.storage.idsu.TileInterdimensionalSU;

import java.util.function.BiConsumer;

public class ServerboundPackets {

	public static final Identifier AESU = new Identifier("techreborn", "aesu");
	public static final Identifier AUTO_CRAFTING_LOCK = new Identifier("techreborn", "auto_crafting_lock");
	public static final Identifier ROLLING_MACHINE_LOCK = new Identifier("techreborn", "rolling_machine_lock");
	public static final Identifier FUSION_CONTROL_SIZE = new Identifier("techreborn", "fusion_control_size");
	public static final Identifier IDSU = new Identifier("techreborn", "idsu");
	public static final Identifier REFUND = new Identifier("techreborn", "refund");
	
	public static void init() {
		registerPacketHandler(AESU, (extendedPacketBuffer, context) -> {
			BlockPos pos = extendedPacketBuffer.readBlockPos();
			int buttonID = extendedPacketBuffer.readInt();
			context.getTaskQueue().execute(() -> {
				BlockEntity tile = context.getPlayer().world.getBlockEntity(pos);
				if (tile instanceof TileAdjustableSU) {
					((TileAdjustableSU) tile).handleGuiInputFromClient(buttonID, false, false);
				}
			});
		});

		registerPacketHandler(AUTO_CRAFTING_LOCK, (extendedPacketBuffer, context) -> {
			BlockPos machinePos = extendedPacketBuffer.readBlockPos();
			boolean locked = extendedPacketBuffer.readBoolean();
			context.getTaskQueue().execute(() -> {
				BlockEntity BlockEntity = context.getPlayer().world.getBlockEntity(machinePos);
				if (BlockEntity instanceof TileAutoCraftingTable) {
					((TileAutoCraftingTable) BlockEntity).locked = locked;
				}
			});
		});

		registerPacketHandler(FUSION_CONTROL_SIZE, (extendedPacketBuffer, context) -> {
			int sizeDelta = extendedPacketBuffer.readInt();
			BlockPos pos = extendedPacketBuffer.readBlockPos();
			context.getTaskQueue().execute(() -> {
				BlockEntity tile = context.getPlayer().world.getBlockEntity(pos);
				if (tile instanceof TileFusionControlComputer) {
					((TileFusionControlComputer) tile).changeSize(sizeDelta);
				}
			});
		});

		registerPacketHandler(IDSU, (extendedPacketBuffer, context) -> {
			BlockPos pos = extendedPacketBuffer.readBlockPos();
			int buttonID = extendedPacketBuffer.readInt();
			context.getTaskQueue().execute(() -> {
				//TODO was commented out when I ported it, so ill leave it here, needs looking into tho
				//		if (!pos.getWorld().isRemote) {
				//			pos.handleGuiInputFromClient(buttonID);
				//		}
			});
		});

		registerPacketHandler(ROLLING_MACHINE_LOCK, (extendedPacketBuffer, context) -> {
			BlockPos machinePos = extendedPacketBuffer.readBlockPos();
			boolean locked = extendedPacketBuffer.readBoolean();
			context.getTaskQueue().execute(() -> {
				BlockEntity BlockEntity = context.getPlayer().world.getBlockEntity(machinePos);
				if (BlockEntity instanceof TileRollingMachine) {
					((TileRollingMachine) BlockEntity).locked = locked;
				}
			});
		});

		registerPacketHandler(REFUND, (extendedPacketBuffer, context) -> {
			if(!ItemManual.allowRefund){
				return;
			}
			context.getTaskQueue().execute(() -> {
				PlayerEntity playerMP = context.getPlayer();
				for (int i = 0; i < playerMP.inventory.getInvSize(); i++) {
					ItemStack stack = playerMP.inventory.getInvStack(i);
					if (stack.getItem() == TRContent.MANUAL) {
						playerMP.inventory.removeInvStack(i);
						playerMP.inventory.addItemStackToInventory(new ItemStack(Items.BOOK));
						//TODO 1.13
						//playerMP.inventory.addItemStackToInventory(OreUtil.getStackFromName("ingotRefinedIron"));
						return;
					}
				}
			});

		});
	}

	private static void registerPacketHandler(Identifier identifier, BiConsumer<ExtendedPacketBuffer, PacketContext> consumer){
		ServerSidePacketRegistry.INSTANCE.register(identifier, (packetContext, packetByteBuf) -> consumer.accept(new ExtendedPacketBuffer(packetByteBuf), packetContext));
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

	public static NetworkPacket createRefundPacket(){
		return NetworkManager.createPacket(REFUND, extendedPacketBuffer -> {

		});
	}

}
