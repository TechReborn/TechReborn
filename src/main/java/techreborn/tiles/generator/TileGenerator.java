/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.ForgeModContainer;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;

public class TileGenerator extends TilePowerAcceptor implements IWrenchable, IInventoryProvider, IContainerProvider {
	public static int outputAmount = 10;
	public Inventory inventory = new Inventory(2, "TileGenerator", 64, this);
	public int fuelSlot = 0;
	public int burnTime;
	public int totalBurnTime = 0;
	// sould properly use the conversion
	// ratio here.
	public boolean isBurning;
	public boolean lastTickBurning;
	ItemStack burnItem;

	public TileGenerator() {
		super();
	}

	public static int getItemBurnTime(final ItemStack stack) {
		return TileEntityFurnace.getItemBurnTime(stack) / 4;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (this.world.isRemote) {
			return;
		}
		if (this.getEnergy() < this.getMaxPower()) {
			if (this.burnTime > 0) {
				this.burnTime--;
				this.addEnergy(TileGenerator.outputAmount);
				this.isBurning = true;
			}
		} else {
			this.isBurning = false;
		}

		if (this.burnTime == 0) {
			this.updateState();
			this.burnTime = this.totalBurnTime = TileGenerator.getItemBurnTime(this.getStackInSlot(this.fuelSlot));
			if (this.burnTime > 0) {
				this.updateState();
				this.burnItem = this.getStackInSlot(this.fuelSlot);
				if (this.getStackInSlot(this.fuelSlot).getCount() == 1) {
					if (this.getStackInSlot(this.fuelSlot).getItem() == Items.LAVA_BUCKET || this.getStackInSlot(this.fuelSlot).getItem() == ForgeModContainer.getInstance().universalBucket) {
						this.setInventorySlotContents(this.fuelSlot, new ItemStack(Items.BUCKET));
					} else {
						this.setInventorySlotContents(this.fuelSlot, ItemStack.EMPTY);
					}

				} else {
					this.decrStackSize(this.fuelSlot, 1);
				}
			}
		}

		this.lastTickBurning = this.isBurning;
	}

	public void updateState() {
		final IBlockState BlockStateContainer = this.world.getBlockState(this.pos);
		if (BlockStateContainer.getBlock() instanceof BlockMachineBase) {
			final BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
			if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != this.burnTime > 0)
				blockMachineBase.setActive(this.burnTime > 0, this.world, this.pos);
		}
	}

	@Override
	public boolean wrenchCanSetFacing(final EntityPlayer entityPlayer, final EnumFacing side) {
		return false;
	}

	@Override
	public EnumFacing getFacing() {
		return this.getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(final EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public double getBaseMaxPower() {
		return 10000;
	}

	@Override
	public boolean canAcceptEnergy(final EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
		return true;
	}

	@Override
	public double getBaseMaxOutput() {
		return 64;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer p0) {
		return new ItemStack(ModBlocks.SOLID_FUEL_GENEREATOR);
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	public int getBurnTime() {
		return this.burnTime;
	}

	public void setBurnTime(final int burnTime) {
		this.burnTime = burnTime;
	}

	public int getTotalBurnTime() {
		return this.totalBurnTime;
	}

	public void setTotalBurnTime(final int totalBurnTime) {
		this.totalBurnTime = totalBurnTime;
	}

	public int getScaledBurnTime(final int i) {
		return (int) ((float) this.burnTime / (float) this.totalBurnTime * i);
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("generator").player(player.inventory).inventory().hotbar().addInventory()
			.tile(this).fuelSlot(0, 80, 54).energySlot(1, 8, 72).syncEnergyValue()
			.syncIntegerValue(this::getBurnTime, this::setBurnTime)
			.syncIntegerValue(this::getTotalBurnTime, this::setTotalBurnTime).addInventory().create();
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return TileGenerator.getItemBurnTime(itemStackIn) != 0;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[]{fuelSlot};
	}
}
