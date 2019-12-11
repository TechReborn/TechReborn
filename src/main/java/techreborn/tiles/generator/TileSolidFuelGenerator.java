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

package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.ForgeModContainer;
import reborncore.api.IToolDrop;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileSolidFuelGenerator extends TilePowerAcceptor implements IToolDrop, IInventoryProvider, IContainerProvider {

	@ConfigRegistry(config = "generators", category = "generator", key = "GeneratorMaxOutput", comment = "Solid Fuel Generator Max Output (Value in EU)")
	public static int maxOutput = 32;
	@ConfigRegistry(config = "generators", category = "generator", key = "GeneratorMaxEnergy", comment = "Solid Fuel Generator Max Energy (Value in EU)")
	public static int maxEnergy = 10_000;
	@ConfigRegistry(config = "generators", category = "generator", key = "GeneratorEnergyOutput", comment = "Solid Fuel Generator Energy Output Amount (Value in EU)")
	public static int outputAmount = 10;

	public Inventory inventory = new Inventory(2, "TileSolidFuelGenerator", 64, this);
	public int fuelSlot = 0;
	public int burnTime;
	public int totalBurnTime = 0;
	// sould properly use the conversion
	// ratio here.
	public boolean isBurning;
	public boolean lastTickBurning;
	ItemStack burnItem;

	public TileSolidFuelGenerator() {
		super();
	}

	public static int getItemBurnTime(ItemStack stack) {
		return TileEntityFurnace.getItemBurnTime(stack) / 4;
	}

	@Override
	public void update() {
		super.update();
		if (world.isRemote) {
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
			burnTime = totalBurnTime = TileSolidFuelGenerator.getItemBurnTime(getStackInSlot(fuelSlot));
			if (burnTime > 0) {
				updateState();
				burnItem = getStackInSlot(fuelSlot);
				if (getStackInSlot(fuelSlot).getCount() == 1) {
					if (getStackInSlot(fuelSlot).getItem() == Items.LAVA_BUCKET || getStackInSlot(fuelSlot).getItem() == ForgeModContainer.getInstance().universalBucket) {
						setInventorySlotContents(fuelSlot, new ItemStack(Items.BUCKET));
					} else {
						setInventorySlotContents(fuelSlot, ItemStack.EMPTY);
					}

				} else {
					decrStackSize(fuelSlot, 1);
				}
			}
		}

		lastTickBurning = isBurning;
	}

	public void updateState() {
		setActive(burnTime > 0);
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
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
	public ItemStack getToolDrop(EntityPlayer playerIn) {
		return new ItemStack(ModBlocks.SOLID_FUEL_GENEREATOR);
	}

	@Override
	public Inventory getInventory() {
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
	public BuiltContainer createContainer(final EntityPlayer player) {
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
