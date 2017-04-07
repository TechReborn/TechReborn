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

package techreborn.tiles.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.oredict.OreDictionary;

import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;

import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.items.ItemDusts;

import java.util.Random;

public class TileIndustrialSawmill extends TilePowerAcceptor
implements IWrenchable, IInventoryProvider, IContainerProvider {
	public static final int TANK_CAPACITY = 16000;

	public Inventory inventory = new Inventory(5, "Sawmill", 64, this);
	public Tank tank = new Tank("Sawmill", TileIndustrialSawmill.TANK_CAPACITY, this);

	public int tickTime;
	public MultiblockChecker multiblockChecker;

	public TileIndustrialSawmill() {
		super(2);
	}

	@Override
	public void update() {
		super.update();

		if (this.getMutliBlock()) {
			final ItemStack wood = this.inventory.getStackInSlot(0);
			if (this.tickTime == 0) {
				if (wood != null) {
					for (final int id : OreDictionary.getOreIDs(wood)) {
						final String name = OreDictionary.getOreName(id);
						if (name.equals("logWood") && this.canAddOutput(2, 10) && this.canAddOutput(3, 5)
								&& this.canAddOutput(4, 3) && this.canUseEnergy(128.0F) && !this.tank.isEmpty()
								&& this.tank.getFluid().amount >= 1000) {
							wood.stackSize -= (1);
							if (wood.stackSize == 0)
								this.setInventorySlotContents(0, null);
							this.tank.drain(1000, true);
							this.useEnergy(128.0F);
							this.syncWithAll();
							this.tickTime = 1;
						}
					}
				}
			} else if (++this.tickTime > 100) {
				final Random rnd = this.world.rand;
				this.addOutput(2, new ItemStack(Blocks.PLANKS, 6 + rnd.nextInt(4)));
				if (rnd.nextInt(4) != 0) {
					final ItemStack pulp = ItemDusts.getDustByName("sawDust", 2 + rnd.nextInt(3));
					this.addOutput(3, pulp);
				}
				if (rnd.nextInt(3) == 0) {
					final ItemStack paper = new ItemStack(Items.PAPER, 1 + rnd.nextInt(2));
					this.addOutput(4, paper);
				}
				this.tickTime = 0;
			}
		}
		FluidUtils.drainContainers(this.tank, this.inventory, 1, 4);
	}

	public void addOutput(final int slot, final ItemStack stack) {
		if (this.getStackInSlot(slot) == null)
			this.setInventorySlotContents(slot, stack);
		this.getStackInSlot(slot).stackSize += (stack.stackSize);
	}

	public boolean canAddOutput(final int slot, final int amount) {
		final ItemStack stack = this.getStackInSlot(slot);
		return stack == null || this.getInventoryStackLimit() - stack.stackSize >= amount;
	}

	@Override
	public void validate() {
		super.validate();
		this.multiblockChecker = new MultiblockChecker(this.world, this.getPos().down(3));
	}

	public boolean getMutliBlock() {
		final boolean down = this.multiblockChecker.checkRectY(1, 1, MultiblockChecker.CASING_NORMAL,
				MultiblockChecker.ZERO_OFFSET);
		final boolean up = this.multiblockChecker.checkRectY(1, 1, MultiblockChecker.CASING_NORMAL,
				new BlockPos(0, 2, 0));
		final boolean blade = this.multiblockChecker.checkRingY(1, 1, MultiblockChecker.CASING_REINFORCED,
				new BlockPos(0, 1, 0));
		final IBlockState centerBlock = this.multiblockChecker.getBlock(0, 1, 0);
		final boolean center = centerBlock.getBlock() == Blocks.WATER;
		return down && center && blade && up;
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
	public ItemStack getWrenchDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.INDUSTRIAL_SAWMILL, 1);
	}

	@Override
	public void readFromNBT(final NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		this.tank.readFromNBT(tagCompound);
		this.tickTime = tagCompound.getInteger("tickTime");
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		this.tank.writeToNBT(tagCompound);
		tagCompound.setInteger("tickTime", this.tickTime);
		return tagCompound;
	}

	@Override
	public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) this.tank;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		return new int[] { 0, 2, 3, 4, 5 };
	}

	@Override
	public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
		return index == 0;
	}

	@Override
	public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction) {
		return index >= 2;
	}

	public int getProgressScaled(final int scale) {
		if (this.tickTime != 0) {
			return this.tickTime * scale / 100;
		}
		return 0;
	}

	@Override
	public double getMaxPower() {
		return 64000;
	}

	@Override
	public boolean canAcceptEnergy(final EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
		return false;
	}

	@Override
	public double getMaxOutput() {
		return 0;
	}

	@Override
	public double getMaxInput() {
		return 64;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.MEDIUM;
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("industrialsawmill").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(this).slot(0, 32, 26).slot(1, 32, 44).outputSlot(2, 84, 35).outputSlot(3, 102, 35)
				.outputSlot(4, 120, 35).syncEnergyValue().addInventory().create();
	}
}
