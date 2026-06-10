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

package techreborn.blockentity.generator.basic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class SolidFuelGeneratorBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

	public final RebornInventory<SolidFuelGeneratorBlockEntity> inventory = new RebornInventory<>(2, "SolidFuelGeneratorBlockEntity", 64, this);
	public final int fuelSlot = 0;
	public int burnTime;
	public int totalBurnTime = 0;
	public boolean isBurning;
	public boolean lastTickBurning;
	ItemStack burnItem;

	public SolidFuelGeneratorBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.SOLID_FUEL_GENERATOR, pos, state);
	}

	public static int getItemBurnTime(Level world, ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}
		return world.fuelValues().burnDuration(stack) / 4;
	}

	private void updateState() {
		assert level != null;
		final BlockState BlockStateContainer = level.getBlockState(worldPosition);
		if (BlockStateContainer.getBlock() instanceof final BlockMachineBase blockMachineBase) {
			boolean active = burnTime > 0 && getFreeSpace() > 0.0f;
			if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != active) {
				blockMachineBase.setActive(active, level, worldPosition);
			}
		}
	}


	// PowerAcceptorBlockEntity
	@Override
	public void tick(Level level, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(level, pos, state, blockEntity);
		if (!(level instanceof ServerLevel serverLevel)){
			return;
		}

		discharge(1);
		if (getFreeSpace() >= TechRebornConfig.solidFuelGeneratorOutputAmount.get()) {
			if (burnTime > 0) {
				burnTime--;
				addEnergy(TechRebornConfig.solidFuelGeneratorOutputAmount.get());
				isBurning = true;
			}
		} else {
			isBurning = false;
			updateState();
		}

		if (burnTime == 0) {
			updateState();
			burnTime = totalBurnTime = SolidFuelGeneratorBlockEntity.getItemBurnTime(serverLevel, inventory.getItem(fuelSlot));
			if (burnTime > 0) {
				updateState();
				burnItem = inventory.getItem(fuelSlot);
				if (inventory.getItem(fuelSlot).getCount() == 1) {
					if (inventory.getItem(fuelSlot).getItem() == Items.LAVA_BUCKET || inventory.getItem(fuelSlot).getItem() instanceof BucketItem) {
						inventory.setItem(fuelSlot, new ItemStack(Items.BUCKET));
					} else {
						inventory.setItem(fuelSlot, ItemStack.EMPTY);
					}

				} else {
					inventory.shrinkSlot(fuelSlot, 1);
				}
			}
		}

		lastTickBurning = isBurning;
	}

	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.solidFuelGeneratorMaxEnergy.get();
	}

	@Override
	public boolean canAcceptEnergy(@Nullable Direction side) {
		return false;
	}

	@Override
	public long getBaseMaxOutput() {
		return TechRebornConfig.solidFuelGeneratorMaxOutput.get();
	}

	@Override
	public long getBaseMaxInput() {
		return 0;
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		burnTime = view.getIntOr("BurnTime", 0);
		totalBurnTime = view.getIntOr("TotalBurnTime", 0);
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		view.putInt("BurnTime", burnTime);
		view.putInt("TotalBurnTime", totalBurnTime);
	}

	// MachineBaseBlockEntity
	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(Player playerIn) {
		return TRContent.Machine.SOLID_FUEL_GENERATOR.getStack();
	}

	// InventoryProvider
	@Override
	public RebornInventory<SolidFuelGeneratorBlockEntity> getInventory() {
		return inventory;
	}

	// BuiltScreenHandlerProvider
	public int getBurnTime() {
		return burnTime;
	}

	public void setBurnTime(final int burnTime) {
		this.burnTime = burnTime;
	}

	public int getTotalBurnTime() {
		return totalBurnTime;
	}

	public void setTotalBurnTime(final int totalBurnTime) {
		this.totalBurnTime = totalBurnTime;
	}

	public int getScaledBurnTime(final int i) {
		return (int) ((float) burnTime / (float) totalBurnTime * i);
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final Player player) {
		return new ScreenHandlerBuilder("generator").player(player.getInventory()).inventory().hotbar().addInventory()
				.blockEntity(this).fuelSlot(0, 80, 54).energySlot(1, 8, 72).syncEnergyValue()
				.sync(ByteBufCodecs.INT, this::getBurnTime, this::setBurnTime)
				.sync(ByteBufCodecs.INT, this::getTotalBurnTime, this::setTotalBurnTime).addInventory().create(this, syncID);
	}
}
