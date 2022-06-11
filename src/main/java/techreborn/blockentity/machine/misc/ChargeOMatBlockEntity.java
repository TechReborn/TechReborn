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

package techreborn.blockentity.machine.misc;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class ChargeOMatBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

	public RebornInventory<ChargeOMatBlockEntity> inventory = new RebornInventory<>(6, "ChargeOMatBlockEntity", 64, this);

	public ChargeOMatBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.CHARGE_O_MAT, pos, state);
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);

		if (world == null || world.isClient) {
			return;
		}
		for (int i = 0; i < 6; i++) {
			discharge(i);
		}
	}

	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.chargeOMatBMaxEnergy;
	}

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		// This allows to move energy from BE to chargeable item. #2264
		return side == null;
	}

	@Override
	public long getBaseMaxOutput() {
		return TechRebornConfig.chargeOMatBMaxOutput;
	}

	@Override
	public long getBaseMaxInput() {
		return TechRebornConfig.chargeOMatBMaxInput;
	}

	// MachineBaseBlockEntity
	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return TRContent.Machine.CHARGE_O_MAT.getStack();
	}

	// InventoryProvider
	@Override
	public RebornInventory<ChargeOMatBlockEntity> getInventory() {
		return inventory;
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("chargebench").player(player.getInventory()).inventory().hotbar().addInventory()
				.blockEntity(this).energySlot(0, 62, 25).energySlot(1, 98, 25).energySlot(2, 62, 45).energySlot(3, 98, 45)
				.energySlot(4, 62, 65).energySlot(5, 98, 65).syncEnergyValue().addInventory().create(this, syncID);
	}
}
