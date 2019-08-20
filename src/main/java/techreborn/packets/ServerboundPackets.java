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
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.NetworkManager;
import techreborn.blockentity.fusionReactor.FusionControlComputerBlockEntity;
import techreborn.blockentity.machine.tier1.AutoCraftingTableBlockEntity;
import techreborn.blockentity.machine.tier1.RollingMachineBlockEntity;
import techreborn.blockentity.storage.AdjustableSUBlockEntity;
import techreborn.blockentity.storage.idsu.InterdimensionalSUBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;

import java.util.function.BiConsumer;

public class ServerboundPackets {

	public static final Identifier AESU = new Identifier("techreborn", "aesu");
	public static final Identifier AUTO_CRAFTING_LOCK = new Identifier("techreborn", "auto_crafting_lock");
	public static final Identifier ROLLING_MACHINE_LOCK = new Identifier("techreborn", "rolling_machine_lock");
	public static final Identifier FUSION_CONTROL_SIZE = new Identifier("techreborn", "fusion_control_size");
	public static final Identifier REFUND = new Identifier("techreborn", "refund");
	
	public static void init() {
		registerPacketHandler(AESU, (extendedPacketBuffer, context) -> {
			BlockPos pos = extendedPacketBuffer.readBlockPos();
			int buttonID = extendedPacketBuffer.readInt();
			boolean shift = extendedPacketBuffer.readBoolean();
			boolean ctrl = extendedPacketBuffer.readBoolean();

			context.getTaskQueue().execute(() -> {
				BlockEntity blockEntity = context.getPlayer().world.getBlockEntity(pos);
				if (blockEntity instanceof AdjustableSUBlockEntity) {
					((AdjustableSUBlockEntity) blockEntity).handleGuiInputFromClient(buttonID, shift, ctrl);
				}
			});
		});

		registerPacketHandler(AUTO_CRAFTING_LOCK, (extendedPacketBuffer, context) -> {
			BlockPos machinePos = extendedPacketBuffer.readBlockPos();
			boolean locked = extendedPacketBuffer.readBoolean();

			context.getTaskQueue().execute(() -> {
				BlockEntity BlockEntity = context.getPlayer().world.getBlockEntity(machinePos);
				if (BlockEntity instanceof AutoCraftingTableBlockEntity) {
					((AutoCraftingTableBlockEntity) BlockEntity).locked = locked;
				}
			});
		});

		registerPacketHandler(FUSION_CONTROL_SIZE, (extendedPacketBuffer, context) -> {
			int sizeDelta = extendedPacketBuffer.readInt();
			BlockPos pos = extendedPacketBuffer.readBlockPos();

			context.getTaskQueue().execute(() -> {
				BlockEntity blockEntity = context.getPlayer().world.getBlockEntity(pos);
				if (blockEntity instanceof FusionControlComputerBlockEntity) {
					((FusionControlComputerBlockEntity) blockEntity).changeSize(sizeDelta);
				}
			});
		});

		registerPacketHandler(ROLLING_MACHINE_LOCK, (extendedPacketBuffer, context) -> {
			BlockPos machinePos = extendedPacketBuffer.readBlockPos();
			boolean locked = extendedPacketBuffer.readBoolean();

			context.getTaskQueue().execute(() -> {
				BlockEntity BlockEntity = context.getPlayer().world.getBlockEntity(machinePos);
				if (BlockEntity instanceof RollingMachineBlockEntity) {
					((RollingMachineBlockEntity) BlockEntity).locked = locked;
				}
			});
		});

		registerPacketHandler(REFUND, (extendedPacketBuffer, context) -> {
			if(!TechRebornConfig.allowManualRefund){
				return;
			}
			context.getTaskQueue().execute(() -> {
				PlayerEntity playerMP = context.getPlayer();
				for (int i = 0; i < playerMP.inventory.getInvSize(); i++) {
					ItemStack stack = playerMP.inventory.getInvStack(i);
					if (stack.getItem() == TRContent.MANUAL) {
						playerMP.inventory.removeInvStack(i);
						playerMP.inventory.insertStack(new ItemStack(Items.BOOK));
						playerMP.inventory.insertStack(TRContent.Ingots.REFINED_IRON.getStack());
						return;
					}
				}
			});

		});
	}

	private static void registerPacketHandler(Identifier identifier, BiConsumer<ExtendedPacketBuffer, PacketContext> consumer){
		ServerSidePacketRegistry.INSTANCE.register(identifier, (packetContext, packetByteBuf) -> consumer.accept(new ExtendedPacketBuffer(packetByteBuf), packetContext));
	}

	public static Packet createPacketAesu(int buttonID, boolean shift, boolean ctrl, AdjustableSUBlockEntity blockEntity) {
		return NetworkManager.createServerBoundPacket(AESU, extendedPacketBuffer -> {
			extendedPacketBuffer.writeBlockPos(blockEntity.getPos());
			extendedPacketBuffer.writeInt(buttonID);
			extendedPacketBuffer.writeBoolean(shift);
			extendedPacketBuffer.writeBoolean(ctrl);
		});
	}

	public static Packet createPacketAutoCraftingTableLock(AutoCraftingTableBlockEntity machine, boolean locked) {
		return NetworkManager.createServerBoundPacket(AUTO_CRAFTING_LOCK, extendedPacketBuffer -> {
			extendedPacketBuffer.writeBlockPos(machine.getPos());
			extendedPacketBuffer.writeBoolean(locked);
		});
	}

	public static Packet createPacketFusionControlSize(int sizeDelta, BlockPos pos) {
		return NetworkManager.createServerBoundPacket(FUSION_CONTROL_SIZE, extendedPacketBuffer -> {
			extendedPacketBuffer.writeInt(sizeDelta);
			extendedPacketBuffer.writeBlockPos(pos);
		});
	}


	public static Packet createPacketRollingMachineLock(RollingMachineBlockEntity machine, boolean locked) {
		return NetworkManager.createServerBoundPacket(ROLLING_MACHINE_LOCK, extendedPacketBuffer -> {
			extendedPacketBuffer.writeBlockPos(machine.getPos());
			extendedPacketBuffer.writeBoolean(locked);
		});
	}

	public static Packet createRefundPacket(){
		return NetworkManager.createServerBoundPacket(REFUND, extendedPacketBuffer -> {

		});
	}

}
