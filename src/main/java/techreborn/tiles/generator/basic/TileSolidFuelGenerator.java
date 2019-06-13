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

package techreborn.tiles.generator.basic;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.api.tile.ItemHandlerProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.init.TRTileEntities;

@RebornRegister(TechReborn.MOD_ID)
public class TileSolidFuelGenerator extends TilePowerAcceptor implements IToolDrop, ItemHandlerProvider, IContainerProvider {

	@ConfigRegistry(config = "generators", category = "generator", key = "GeneratorMaxOutput", comment = "Solid Fuel Generator Max Output (Value in EU)")
	public static int maxOutput = 32;
	@ConfigRegistry(config = "generators", category = "generator", key = "GeneratorMaxEnergy", comment = "Solid Fuel Generator Max Energy (Value in EU)")
	public static int maxEnergy = 10_000;
	@ConfigRegistry(config = "generators", category = "generator", key = "GeneratorEnergyOutput", comment = "Solid Fuel Generator Energy Output Amount (Value in EU)")
	public static int outputAmount = 10;

	public Inventory<TileSolidFuelGenerator> inventory = new Inventory<>(2, "TileSolidFuelGenerator", 64, this).withConfiguredAccess();
	public int fuelSlot = 0;
	public int burnTime;
	public int totalBurnTime = 0;
	// sould properly use the conversion
	// ratio here.
	public boolean isBurning;
	public boolean lastTickBurning;
	ItemStack burnItem;

	public TileSolidFuelGenerator() {
		super(TRTileEntities.SOLID_FUEL_GENEREATOR);
	}

	public static int getItemBurnTime(ItemStack stack) {
		return FurnaceBlockEntity.createFuelTimeMap().get(stack) / 4;
	}

	@Override
	public void tick() {
		super.tick();
		if (world.isClient) {
			return;
		}
		if (getEnergy() < getMaxPower()) {
			if (burnTime > 0) {
				burnTime--;
				addEnergy(TileSolidFuelGenerator.outputAmount);
				isBurning = true;
			}
		} else {
			isBurning = false;
		}

		if (burnTime == 0) {
			updateState();
			burnTime = totalBurnTime = TileSolidFuelGenerator.getItemBurnTime(inventory.getInvStack(fuelSlot));
			if (burnTime > 0) {
				updateState();
				burnItem = inventory.getInvStack(fuelSlot);
				if (inventory.getInvStack(fuelSlot).getCount() == 1) {
					if (inventory.getInvStack(fuelSlot).getItem() == Items.LAVA_BUCKET || inventory.getInvStack(fuelSlot).getItem() instanceof BucketItem) {
						inventory.setStackInSlot(fuelSlot, new ItemStack(Items.BUCKET));
					} else {
						inventory.setStackInSlot(fuelSlot, ItemStack.EMPTY);
					}

				} else {
					inventory.shrinkSlot(fuelSlot, 1);
				}
			}
		}

		lastTickBurning = isBurning;
	}

	public void updateState() {
		final BlockState BlockStateContainer = world.getBlockState(pos);
		if (BlockStateContainer.getBlock() instanceof BlockMachineBase) {
			final BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
			if (BlockStateContainer.get(BlockMachineBase.ACTIVE) != burnTime > 0) {
				blockMachineBase.setActive(burnTime > 0, world, pos);
			}
		}
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(Direction direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(Direction direction) {
		return true;
	}

	@Override
	public double getBaseMaxOutput() {
		return maxOutput;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity playerIn) {
		return TRContent.Machine.SOLID_FUEL_GENERATOR.getStack();
	}

	@Override
	public Inventory<TileSolidFuelGenerator> getInventory() {
		return inventory;
	}

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
	public BuiltContainer createContainer(final PlayerEntity player) {
		return new ContainerBuilder("generator").player(player.inventory).inventory().hotbar().addInventory()
			.tile(this).fuelSlot(0, 80, 54).energySlot(1, 8, 72).syncEnergyValue()
			.syncIntegerValue(this::getBurnTime, this::setBurnTime)
			.syncIntegerValue(this::getTotalBurnTime, this::setTotalBurnTime).addInventory().create(this);
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}
