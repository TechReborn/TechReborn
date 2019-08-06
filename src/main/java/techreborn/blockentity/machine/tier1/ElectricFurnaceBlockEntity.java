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

package techreborn.blockentity.machine.tier1;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import reborncore.common.util.RebornInventory;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.utils.RecipeUtils;
import techreborn.init.TRBlockEntities;

@RebornRegister(TechReborn.MOD_ID)
public class ElectricFurnaceBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "electric_furnace", key = "ElectricFurnaceInput", comment = "Electric Furnace Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "electric_furnace", key = "ElectricFurnaceMaxEnergy", comment = "Electric Furnace Max Energy (Value in EU)")
	public static int maxEnergy = 1000;

	public RebornInventory<ElectricFurnaceBlockEntity> inventory = new RebornInventory<>(3, "ElectricFurnaceBlockEntity", 64, this);
	public int progress;
	public int fuelScale = 100;
	public int cost = 6;
	int input1 = 0;
	int output = 1;
	boolean wasBurning = false;

	public ElectricFurnaceBlockEntity() {
		super(TRBlockEntities.ELECTRIC_FURNACE );
	}

	public int gaugeProgressScaled(int scale) {
		return progress * scale / (int) (fuelScale * (1.0 - getSpeedMultiplier()));
	}

	public void cookItems() {
		if (canSmelt()) {
			final ItemStack itemstack = RecipeUtils.getMatchingRecipes(world, RecipeType.SMELTING, inventory.getInvStack(input1));

			if (inventory.getInvStack(output).isEmpty()) {
				inventory.setInvStack(output, itemstack.copy());
			} else if (inventory.getInvStack(output).isItemEqualIgnoreDamage(itemstack)) {
				inventory.getInvStack(output).increment(itemstack.getCount());
			}
			if (inventory.getInvStack(input1).getCount() > 1) {
				inventory.shrinkSlot(input1, 1);
			} else {
				inventory.setInvStack(input1, ItemStack.EMPTY);
			}
		}
	}

	public boolean canSmelt() {
		if (inventory.getInvStack(input1).isEmpty()) {
			return false;
		}
		final ItemStack itemstack = RecipeUtils.getMatchingRecipes(world, RecipeType.SMELTING, inventory.getInvStack(input1));
		if (itemstack.isEmpty()) {
			return false;
		}
		if (inventory.getInvStack(output).isEmpty()) {
			return true;
		}
		if (!inventory.getInvStack(output).isItemEqualIgnoreDamage(itemstack)) {
			return false;
		}
		final int result = inventory.getInvStack(output).getCount() + itemstack.getCount();
		return result <= this.inventory.getStackLimit() && result <= itemstack.getMaxCount();
	}

	public boolean isBurning() {
		return getEnergy() > getEuPerTick(cost);
	}

	public ItemStack getResultFor(ItemStack stack) {
		final ItemStack result = RecipeUtils.getMatchingRecipes(world, RecipeType.SMELTING, stack);
		if (!result.isEmpty()) {
			return result.copy();
		}
		return ItemStack.EMPTY;
	}

	public void updateState() {
		if (wasBurning != (progress > 0)) {
			// skips updating the block state for 1 tick, to prevent the machine from
			// turning on/off rapidly causing fps drops
			if (wasBurning && progress == 0 && canSmelt()) {
				wasBurning = true;
				return;
			}
			final BlockState BlockStateContainer = world.getBlockState(pos);
			if (BlockStateContainer.getBlock() instanceof BlockMachineBase) {
				final BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
				if (BlockStateContainer.get(BlockMachineBase.ACTIVE) != progress > 0)
					blockMachineBase.setActive(progress > 0, world, pos);
			}
			wasBurning = (progress > 0);
		}

	}

	public int getBurnTime() {
		return progress;
	}

	public void setBurnTime(final int burnTime) {
		this.progress = burnTime;
	}

	// TilePowerAcceptor
	@Override
	public void tick() {
		if (world.isClient) {
			return;
		}

		super.tick();
		charge(2);

		final boolean burning = isBurning();
		boolean updateInventory = false;
		if (isBurning() && canSmelt()) {
			updateState();
			if (canUseEnergy(getEuPerTick(cost))) {
				useEnergy(getEuPerTick(cost));
				progress++;
				if (progress >= Math.max((int) (fuelScale * (1.0 - getSpeedMultiplier())), 5)) {
					progress = 0;
					cookItems();
					updateInventory = true;
				}
			}
		} else {
			updateState();
		}
		if (burning != isBurning()) {
			updateInventory = true;
		}
		if (updateInventory) {
			markDirty();
		}
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(final Direction direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(final Direction direction) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return maxInput;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return TRContent.Machine.ELECTRIC_FURNACE.getStack();
	}

	// ItemHandlerProvider
	@Override
	public RebornInventory<ElectricFurnaceBlockEntity> getInventory() {
		return inventory;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("electricfurnace").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).slot(0, 55, 45).outputSlot(1, 101, 45).energySlot(2, 8, 72).syncEnergyValue()
				.syncIntegerValue(this::getBurnTime, this::setBurnTime).addInventory().create(this, syncID);
	}
}
