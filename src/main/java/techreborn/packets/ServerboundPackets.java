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

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.network.IdentifiedPacket;
import reborncore.common.network.NetworkManager;
import techreborn.TechReborn;
import techreborn.blockentity.machine.iron.IronFurnaceBlockEntity;
import techreborn.blockentity.machine.tier2.PumpBlockEntity;
import techreborn.blockentity.machine.multiblock.FusionControlComputerBlockEntity;
import techreborn.blockentity.machine.tier1.AutoCraftingTableBlockEntity;
import techreborn.blockentity.machine.tier1.ElevatorBlockEntity;
import techreborn.blockentity.machine.tier1.PlayerDetectorBlockEntity;
import techreborn.blockentity.machine.tier1.RollingMachineBlockEntity;
import techreborn.blockentity.machine.tier2.LaunchpadBlockEntity;
import techreborn.blockentity.machine.tier3.ChunkLoaderBlockEntity;
import techreborn.blockentity.storage.energy.AdjustableSUBlockEntity;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;
import techreborn.packets.serverbound.*;

public class ServerboundPackets {

	//public static final Identifier AESU = new Identifier(TechReborn.MOD_ID, "aesu");
	//public static final Identifier AUTO_CRAFTING_LOCK = new Identifier(TechReborn.MOD_ID, "auto_crafting_lock");
	// public static final Identifier ROLLING_MACHINE_LOCK = new Identifier(TechReborn.MOD_ID, "rolling_machine_lock");
	//public static final Identifier STORAGE_UNIT_LOCK = new Identifier(TechReborn.MOD_ID, "storage_unit_lock");
	//public static final Identifier FUSION_CONTROL_SIZE = new Identifier(TechReborn.MOD_ID, "fusion_control_size");
	//public static final Identifier REFUND = new Identifier(TechReborn.MOD_ID, "refund");
	//public static final Identifier CHUNKLOADER = new Identifier(TechReborn.MOD_ID, "chunkloader");
	// public static final Identifier EXPERIENCE = new Identifier(TechReborn.MOD_ID, "experience");
	// public static final Identifier DETECTOR_RADIUS = new Identifier(TechReborn.MOD_ID, "detector_radius");
	//public static final Identifier LAUNCH_SPEED = new Identifier(TechReborn.MOD_ID, "launch_speed");
	// public static final Identifier JUMP = new Identifier(TechReborn.MOD_ID, "jump");
	//public static final Identifier PUMP_DEPTH = new Identifier(TechReborn.MOD_ID, "pump_depth");
	//public static final Identifier PUMP_RANGE = new Identifier(TechReborn.MOD_ID, "pump_range");

	// public static final Identifier SUIT_NIGHT_VISION = new Identifier(TechReborn.MOD_ID, "suit_night_vision");
	//public static final Identifier QUANTUM_SUIT_SPRINT = new Identifier(TechReborn.MOD_ID, "quantum_suit_sprint");

	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(AESUConfigPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof AdjustableSUBlockEntity) {
				((AdjustableSUBlockEntity) legacyMachineBase).handleGuiInputFromClient(payload.buttonID(), payload.shift(), payload.ctrl());
			}
		});


		NetworkManager.registerServerBoundHandler(AutoCraftingLockPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof AutoCraftingTableBlockEntity) {
				((AutoCraftingTableBlockEntity) legacyMachineBase).locked = payload.locked();
			}
		});

		NetworkManager.registerServerBoundHandler(RollingMachineLockPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof RollingMachineBlockEntity) {
				((RollingMachineBlockEntity) legacyMachineBase).locked = payload.locked();
			}
		});

		NetworkManager.registerServerBoundHandler(StorageUnitLockPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof StorageUnitBaseBlockEntity) {
				((StorageUnitBaseBlockEntity) legacyMachineBase).setLocked(payload.locked());
			}
		});

		NetworkManager.registerServerBoundHandler(FusionControlSizePayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof FusionControlComputerBlockEntity) {
				((FusionControlComputerBlockEntity) legacyMachineBase).changeSize(payload.sizeDelta());
			}
		});

		NetworkManager.registerServerBoundHandler(RefundPayload.ID, (payload, context) -> {
			if (!TechRebornConfig.allowManualRefund) {
				return;
			}
			PlayerInventory inventory = context.player().getInventory();
			for (int i=0; i < inventory.size(); i++){
				ItemStack stack = inventory.getStack(i);
				if (stack.getItem() == TRContent.MANUAL) {
					inventory.removeStack(i);
					inventory.insertStack(new ItemStack(Items.BOOK));
					inventory.insertStack(TRContent.Ingots.REFINED_IRON.getStack());
					return;
				}
			}
		});

		NetworkManager.registerServerBoundHandler(ChunkloaderPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof ChunkLoaderBlockEntity) {
				((ChunkLoaderBlockEntity) legacyMachineBase).handleGuiInputFromClient(payload.buttonID(), payload.sync() ? context.player() : null);
			}
		});

		NetworkManager.registerServerBoundHandler(ExperiencePayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof IronFurnaceBlockEntity) {
				((IronFurnaceBlockEntity) legacyMachineBase).handleGuiInputFromClient(context.player());
			}
		});

		NetworkManager.registerServerBoundHandler(DetectorRadiusPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof PlayerDetectorBlockEntity) {
				((PlayerDetectorBlockEntity) legacyMachineBase).handleGuiInputFromClient(payload.buttonAmount());
			}
		});

		NetworkManager.registerServerBoundHandler(LaunchSpeedPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof LaunchpadBlockEntity) {
				((LaunchpadBlockEntity) legacyMachineBase).handleGuiInputFromClient(payload.buttonAmount());
			}
		});

		NetworkManager.registerServerBoundHandler(PumpDepthPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof PumpBlockEntity) {
				((PumpBlockEntity) legacyMachineBase).handleDepthGuiInputFromClient(payload.buttonAmount());
			}
		});

		NetworkManager.registerServerBoundHandler(PumpRangePayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof PumpBlockEntity) {
				((PumpBlockEntity) legacyMachineBase).handleRangeGuiInputFromClient(payload.buttonAmount());
			}
		});

		NetworkManager.registerServerBoundHandler(JumpPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof ElevatorBlockEntity) {
				((ElevatorBlockEntity) legacyMachineBase).teleportUp(context.player());
			}
		});

		NetworkManager.registerServerBoundHandler(SuitNightVisionPayload.ID, (payload, context) -> {
			for (ItemStack itemStack : context.player().getArmorItems()) {
				if (itemStack.isOf(TRContent.NANO_HELMET) || itemStack.isOf(TRContent.QUANTUM_HELMET)) {
					// TODO 1.20.5 Use component
					// itemStack.getOrCreateNbt().putBoolean("isActive", !itemStack.getOrCreateNbt().getBoolean("isActive"));
					break;
				}
			}
		});

		NetworkManager.registerServerBoundHandler(QuantumSuitSprintPayload.ID, (payload, context) -> {
			for (ItemStack itemStack : context.player().getArmorItems()) {
				if (itemStack.isOf(TRContent.QUANTUM_LEGGINGS)) {
					// TODO 1.20.5 Use component
					// itemStack.getOrCreateNbt().putBoolean("isActive", !itemStack.getOrCreateNbt().getBoolean("isActive"));
					break;
				}
			}
		});
	}

	public static IdentifiedPacket createPacketAesu(int buttonID, boolean shift, boolean ctrl, AdjustableSUBlockEntity blockEntity) {
		return NetworkManager.createServerBoundPacket(AESUConfigPayload.getId(), buf -> {
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

	public static IdentifiedPacket createPacketPlayerDetector(int buttonAmount, PlayerDetectorBlockEntity blockEntity) {
		return NetworkManager.createServerBoundPacket(DETECTOR_RADIUS, buf -> {
			buf.writeBlockPos(blockEntity.getPos());
			buf.writeInt(buttonAmount);
		});
	}

	public static IdentifiedPacket createPacketLaunchpad(int buttonAmount, LaunchpadBlockEntity blockEntity) {
		return NetworkManager.createServerBoundPacket(LAUNCH_SPEED, buf -> {
			buf.writeBlockPos(blockEntity.getPos());
			buf.writeInt(buttonAmount);
		});
	}

	public static IdentifiedPacket createPacketJump(BlockPos pos) {
		return NetworkManager.createServerBoundPacket(JUMP, packetBuffer -> {
			packetBuffer.writeBlockPos(pos);
		});
	}

	public static IdentifiedPacket createPacketPumpDepth(int buttonAmount, PumpBlockEntity blockEntity) {
		return NetworkManager.createServerBoundPacket(PUMP_DEPTH, buf -> {
			buf.writeBlockPos(blockEntity.getPos());
			buf.writeInt(buttonAmount);
		});
	}

	public static IdentifiedPacket createPacketPumpRange(int buttonAmount, PumpBlockEntity blockEntity) {
		return NetworkManager.createServerBoundPacket(PUMP_RANGE, buf -> {
			buf.writeBlockPos(blockEntity.getPos());
			buf.writeInt(buttonAmount);
		});
	}

	public static IdentifiedPacket createPacketToggleNV() {
		return NetworkManager.createServerBoundPacket(SUIT_NIGHT_VISION, buf -> {
		});
	}

	public static IdentifiedPacket createPacketToggleQuantumSprint() {
		return NetworkManager.createServerBoundPacket(QUANTUM_SUIT_SPRINT, buf -> { }); 
	}
}
