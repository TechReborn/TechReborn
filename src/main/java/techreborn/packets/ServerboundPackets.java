/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.network.IdentifiedPacket;
import reborncore.common.network.NetworkManager;
import techreborn.TechReborn;
import techreborn.blockentity.machine.iron.IronFurnaceBlockEntity;
import techreborn.blockentity.machine.multiblock.FusionControlComputerBlockEntity;
import techreborn.blockentity.machine.tier1.AutoCraftingTableBlockEntity;
import techreborn.blockentity.machine.tier1.RollingMachineBlockEntity;
import techreborn.blockentity.machine.tier3.ChunkLoaderBlockEntity;
import techreborn.blockentity.storage.energy.AdjustableSUBlockEntity;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;

public class ServerboundPackets {

	public static final Identifier AESU = new Identifier(TechReborn.MOD_ID, "aesu");
	public static final Identifier AUTO_CRAFTING_LOCK = new Identifier(TechReborn.MOD_ID, "auto_crafting_lock");
	public static final Identifier ROLLING_MACHINE_LOCK = new Identifier(TechReborn.MOD_ID, "rolling_machine_lock");
	public static final Identifier STORAGE_UNIT_LOCK = new Identifier(TechReborn.MOD_ID, "storage_unit_lock");
	public static final Identifier FUSION_CONTROL_SIZE = new Identifier(TechReborn.MOD_ID, "fusion_control_size");
	public static final Identifier REFUND = new Identifier(TechReborn.MOD_ID, "refund");
	public static final Identifier CHUNKLOADER = new Identifier(TechReborn.MOD_ID, "chunkloader");
	public static final Identifier EXPERIENCE = new Identifier(TechReborn.MOD_ID, "experience");

	public static void init() {
		NetworkManager.registerServerBoundHandler(AESU, (server, player, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			int buttonID = buf.readInt();
			boolean shift = buf.readBoolean();
			boolean ctrl = buf.readBoolean();

			server.execute(() -> {
				BlockEntity blockEntity = player.world.getBlockEntity(pos);
				if (blockEntity instanceof AdjustableSUBlockEntity) {
					((AdjustableSUBlockEntity) blockEntity).handleGuiInputFromClient(buttonID, shift, ctrl);
				}
			});
		});

		NetworkManager.registerServerBoundHandler(AUTO_CRAFTING_LOCK, (server, player, handler, buf, responseSender) -> {
			BlockPos machinePos = buf.readBlockPos();
			boolean locked = buf.readBoolean();

			server.execute(() -> {
				BlockEntity BlockEntity = player.world.getBlockEntity(machinePos);
				if (BlockEntity instanceof AutoCraftingTableBlockEntity) {
					((AutoCraftingTableBlockEntity) BlockEntity).locked = locked;
				}
			});
		});

		NetworkManager.registerServerBoundHandler(FUSION_CONTROL_SIZE, (server, player, handler, buf, responseSender) -> {
			int sizeDelta = buf.readInt();
			BlockPos pos = buf.readBlockPos();

			server.execute(() -> {
				BlockEntity blockEntity = player.world.getBlockEntity(pos);
				if (blockEntity instanceof FusionControlComputerBlockEntity) {
					((FusionControlComputerBlockEntity) blockEntity).changeSize(sizeDelta);
				}
			});
		});

		NetworkManager.registerServerBoundHandler(ROLLING_MACHINE_LOCK, (server, player, handler, buf, responseSender) -> {
			BlockPos machinePos = buf.readBlockPos();
			boolean locked = buf.readBoolean();

			server.execute(() -> {
				BlockEntity BlockEntity = player.world.getBlockEntity(machinePos);
				if (BlockEntity instanceof RollingMachineBlockEntity) {
					((RollingMachineBlockEntity) BlockEntity).locked = locked;
				}
			});
		});

		NetworkManager.registerServerBoundHandler(STORAGE_UNIT_LOCK, (server, player, handler, buf, responseSender) -> {
			BlockPos machinePos = buf.readBlockPos();
			boolean locked = buf.readBoolean();

			server.execute(() -> {
				BlockEntity BlockEntity = player.world.getBlockEntity(machinePos);
				if (BlockEntity instanceof StorageUnitBaseBlockEntity) {
					((StorageUnitBaseBlockEntity) BlockEntity).setLocked(locked);
				}
			});
		});

		NetworkManager.registerServerBoundHandler(REFUND, (server, player, handler, buf, responseSender) -> {
			if (!TechRebornConfig.allowManualRefund) {
				return;
			}
			server.execute(() -> {
				for (int i = 0; i < player.inventory.size(); i++) {
					ItemStack stack = player.inventory.getStack(i);
					if (stack.getItem() == TRContent.MANUAL) {
						player.inventory.removeStack(i);
						player.inventory.insertStack(new ItemStack(Items.BOOK));
						player.inventory.insertStack(TRContent.Ingots.REFINED_IRON.getStack());
						return;
					}
				}
			});

		});

		NetworkManager.registerServerBoundHandler(CHUNKLOADER, (server, player, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			int buttonID = buf.readInt();
			boolean sync = buf.readBoolean();

			server.execute(() -> {
				BlockEntity blockEntity = player.world.getBlockEntity(pos);
				if (blockEntity instanceof ChunkLoaderBlockEntity) {
					((ChunkLoaderBlockEntity) blockEntity).handleGuiInputFromClient(buttonID, sync ? player : null);
				}
			});
		});

		NetworkManager.registerServerBoundHandler(EXPERIENCE, (server, player, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();

			server.execute(() -> {
				BlockEntity blockEntity = player.world.getBlockEntity(pos);
				if (blockEntity instanceof IronFurnaceBlockEntity) {
					((IronFurnaceBlockEntity) blockEntity).handleGuiInputFromClient(player);
				}
			});
		});
	}

	public static IdentifiedPacket createPacketAesu(int buttonID, boolean shift, boolean ctrl, AdjustableSUBlockEntity blockEntity) {
		return NetworkManager.createServerBoundPacket(AESU, buf -> {
			buf.writeBlockPos(blockEntity.getPos());
			buf.writeInt(buttonID);
			buf.writeBoolean(shift);
			buf.writeBoolean(ctrl);
		});
	}

	public static IdentifiedPacket createPacketAutoCraftingTableLock(AutoCraftingTableBlockEntity machine, boolean locked) {
		return NetworkManager.createServerBoundPacket(AUTO_CRAFTING_LOCK, buf -> {
			buf.writeBlockPos(machine.getPos());
			buf.writeBoolean(locked);
		});
	}

	public static IdentifiedPacket createPacketFusionControlSize(int sizeDelta, BlockPos pos) {
		return NetworkManager.createServerBoundPacket(FUSION_CONTROL_SIZE, buf -> {
			buf.writeInt(sizeDelta);
			buf.writeBlockPos(pos);
		});
	}


	public static IdentifiedPacket createPacketRollingMachineLock(RollingMachineBlockEntity machine, boolean locked) {
		return NetworkManager.createServerBoundPacket(ROLLING_MACHINE_LOCK, buf -> {
			buf.writeBlockPos(machine.getPos());
			buf.writeBoolean(locked);
		});
	}

	public static IdentifiedPacket createPacketStorageUnitLock(StorageUnitBaseBlockEntity machine, boolean locked) {
		return NetworkManager.createServerBoundPacket(STORAGE_UNIT_LOCK, buf -> {
			buf.writeBlockPos(machine.getPos());
			buf.writeBoolean(locked);
		});
	}

	public static IdentifiedPacket createRefundPacket() {
		return NetworkManager.createServerBoundPacket(REFUND, extendedPacketBuffer -> {

		});
	}

	public static IdentifiedPacket createPacketChunkloader(int buttonID, ChunkLoaderBlockEntity blockEntity, boolean sync) {
		return NetworkManager.createServerBoundPacket(CHUNKLOADER, buf -> {
			buf.writeBlockPos(blockEntity.getPos());
			buf.writeInt(buttonID);
			buf.writeBoolean(sync);
		});
	}

	public static IdentifiedPacket createPacketExperience(IronFurnaceBlockEntity blockEntity) {
		return NetworkManager.createServerBoundPacket(EXPERIENCE, extendedPacketBuffer -> extendedPacketBuffer.writeBlockPos(blockEntity.getPos()));
	}

}
