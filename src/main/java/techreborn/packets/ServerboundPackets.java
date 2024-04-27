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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import techreborn.blockentity.machine.iron.IronFurnaceBlockEntity;
import techreborn.blockentity.machine.multiblock.FusionControlComputerBlockEntity;
import techreborn.blockentity.machine.tier1.AutoCraftingTableBlockEntity;
import techreborn.blockentity.machine.tier1.ElevatorBlockEntity;
import techreborn.blockentity.machine.tier1.PlayerDetectorBlockEntity;
import techreborn.blockentity.machine.tier1.RollingMachineBlockEntity;
import techreborn.blockentity.machine.tier2.LaunchpadBlockEntity;
import techreborn.blockentity.machine.tier2.PumpBlockEntity;
import techreborn.blockentity.machine.tier3.ChunkLoaderBlockEntity;
import techreborn.blockentity.storage.energy.AdjustableSUBlockEntity;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;
import techreborn.packets.serverbound.AESUConfigPayload;
import techreborn.packets.serverbound.AutoCraftingLockPayload;
import techreborn.packets.serverbound.ChunkloaderPayload;
import techreborn.packets.serverbound.DetectorRadiusPayload;
import techreborn.packets.serverbound.ExperiencePayload;
import techreborn.packets.serverbound.FusionControlSizePayload;
import techreborn.packets.serverbound.JumpPayload;
import techreborn.packets.serverbound.LaunchSpeedPayload;
import techreborn.packets.serverbound.PumpDepthPayload;
import techreborn.packets.serverbound.PumpRangePayload;
import techreborn.packets.serverbound.QuantumSuitSprintPayload;
import techreborn.packets.serverbound.RefundPayload;
import techreborn.packets.serverbound.RollingMachineLockPayload;
import techreborn.packets.serverbound.StorageUnitLockPayload;
import techreborn.packets.serverbound.SuitNightVisionPayload;

public class ServerboundPackets {
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(AESUConfigPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof AdjustableSUBlockEntity) {
				((AdjustableSUBlockEntity) legacyMachineBase).handleGuiInputFromClient(payload.buttonID(), payload.shift(), payload.ctrl());
			}
		});


		ServerPlayNetworking.registerGlobalReceiver(AutoCraftingLockPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof AutoCraftingTableBlockEntity) {
				((AutoCraftingTableBlockEntity) legacyMachineBase).locked = payload.locked();
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(RollingMachineLockPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof RollingMachineBlockEntity) {
				((RollingMachineBlockEntity) legacyMachineBase).locked = payload.locked();
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(StorageUnitLockPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof StorageUnitBaseBlockEntity) {
				((StorageUnitBaseBlockEntity) legacyMachineBase).setLocked(payload.locked());
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(FusionControlSizePayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof FusionControlComputerBlockEntity) {
				((FusionControlComputerBlockEntity) legacyMachineBase).changeSize(payload.sizeDelta());
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(RefundPayload.ID, (payload, context) -> {
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

		ServerPlayNetworking.registerGlobalReceiver(ChunkloaderPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof ChunkLoaderBlockEntity) {
				((ChunkLoaderBlockEntity) legacyMachineBase).handleGuiInputFromClient(payload.buttonID(), payload.sync() ? context.player() : null);
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(ExperiencePayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof IronFurnaceBlockEntity) {
				((IronFurnaceBlockEntity) legacyMachineBase).handleGuiInputFromClient(context.player());
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(DetectorRadiusPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof PlayerDetectorBlockEntity) {
				((PlayerDetectorBlockEntity) legacyMachineBase).handleGuiInputFromClient(payload.buttonAmount());
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(LaunchSpeedPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof LaunchpadBlockEntity) {
				((LaunchpadBlockEntity) legacyMachineBase).handleGuiInputFromClient(payload.buttonAmount());
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(PumpDepthPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof PumpBlockEntity) {
				((PumpBlockEntity) legacyMachineBase).handleDepthGuiInputFromClient(payload.buttonAmount());
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(PumpRangePayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof PumpBlockEntity) {
				((PumpBlockEntity) legacyMachineBase).handleRangeGuiInputFromClient(payload.buttonAmount());
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(JumpPayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (legacyMachineBase instanceof ElevatorBlockEntity) {
				((ElevatorBlockEntity) legacyMachineBase).teleportUp(context.player());
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(SuitNightVisionPayload.ID, (payload, context) -> {
			for (ItemStack itemStack : context.player().getArmorItems()) {
				if (itemStack.isOf(TRContent.NANO_HELMET) || itemStack.isOf(TRContent.QUANTUM_HELMET)) {
					// TODO 1.20.5 Use component
					// itemStack.getOrCreateNbt().putBoolean("isActive", !itemStack.getOrCreateNbt().getBoolean("isActive"));
					break;
				}
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(QuantumSuitSprintPayload.ID, (payload, context) -> {
			for (ItemStack itemStack : context.player().getArmorItems()) {
				if (itemStack.isOf(TRContent.QUANTUM_LEGGINGS)) {
					// TODO 1.20.5 Use component
					// itemStack.getOrCreateNbt().putBoolean("isActive", !itemStack.getOrCreateNbt().getBoolean("isActive"));
					break;
				}
			}
		});
	}
}
