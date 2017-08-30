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

package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.tile.IInventoryProvider;
import reborncore.api.IToolDrop;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.RollingMachineRecipe;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

import javax.annotation.Nonnull;

//TODO add tick and power bars.

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileRollingMachine extends TilePowerAcceptor
	implements IToolDrop, IInventoryProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "rolling_machine", key = "RollingMachineMaxInput", comment = "Rolling Machine Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "rolling_machine", key = "RollingMachineEnergyPerTick", comment = "Rolling Machine Energy Per Tick (Value in EU)")
	public static int energyPerTick = 5;
	@ConfigRegistry(config = "machines", category = "rolling_machine", key = "RollingMachineEnergyRunTime", comment = "Rolling Machine Run Time")
	public static int runTime = 250;
	@ConfigRegistry(config = "machines", category = "rolling_machine", key = "RollingMachineMaxEnergy", comment = "Rolling Machine Max Energy (Value in EU)")
	public static int maxEnergy = 10000;
	//  @ConfigRegistry(config = "machines", category = "rolling_machine", key = "RollingMachineWrenchDropRate", comment = "Rolling Machi Wrench Drop Rate")
	public static float wrenchDropRate = 1.0F;

	public final InventoryCrafting craftMatrix = new InventoryCrafting(new RollingTileContainer(), 3, 3);
	public Inventory inventory = new Inventory(3, "TileRollingMachine", 64, this);
	public boolean isRunning;
	public int tickTime;
	@Nonnull
	public ItemStack currentRecipe;
	private int outputSlot;

	public TileRollingMachine() {
		super();
		outputSlot = 0;
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
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
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return maxInput;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		this.charge(2);
		if (!this.world.isRemote) {
			this.currentRecipe = RollingMachineRecipe.instance.findMatchingRecipe(this.craftMatrix, this.world);
			if (!this.currentRecipe.isEmpty() && this.canMake()) {
				if (this.tickTime >= this.runTime) {
					this.currentRecipe = RollingMachineRecipe.instance.findMatchingRecipe(this.craftMatrix, this.world);
					if (!this.currentRecipe.isEmpty()) {
						boolean hasCrafted = false;
						if (this.inventory.getStackInSlot(outputSlot) == ItemStack.EMPTY) {
							this.inventory.setInventorySlotContents(outputSlot, this.currentRecipe);
							this.tickTime = -1;
							hasCrafted = true;
						} else {
							if (this.inventory.getStackInSlot(outputSlot).getCount() + this.currentRecipe.getCount() <= this.currentRecipe
								.getMaxStackSize()) {
								final ItemStack stack = this.inventory.getStackInSlot(outputSlot);
								stack.setCount(stack.getCount() + this.currentRecipe.getCount());
								this.inventory.setInventorySlotContents(outputSlot, stack);
								this.tickTime = -1;
								hasCrafted = true;
							}
						}
						if (hasCrafted) {
							for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
								this.craftMatrix.decrStackSize(i, 1);
							}
							this.currentRecipe = ItemStack.EMPTY;
						}
					}
				}
			}
			if (!this.currentRecipe.isEmpty()) {
				if (this.canUseEnergy(energyPerTick) && this.tickTime < this.runTime) {
					this.useEnergy(energyPerTick);
					this.tickTime++;
				}
			}
			if (this.currentRecipe.isEmpty()) {
				this.tickTime = -1;
			}
		} else {
			this.currentRecipe = RollingMachineRecipe.instance.findMatchingRecipe(this.craftMatrix, this.world);
			if (!this.currentRecipe.isEmpty()) {
				this.inventory.setInventorySlotContents(1, this.currentRecipe);
			} else {
				this.inventory.setInventorySlotContents(1, ItemStack.EMPTY);
			}
		}
	}

	public boolean canMake() {
		return RollingMachineRecipe.instance.findMatchingRecipe(this.craftMatrix, this.world) != null;
	}

	@Override
	public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.ROLLING_MACHINE, 1);
	}

	@Override
	public void readFromNBT(final NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		ItemUtils.readInvFromNBT(this.craftMatrix, "Crafting", tagCompound);
		this.isRunning = tagCompound.getBoolean("isRunning");
		this.tickTime = tagCompound.getInteger("tickTime");
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		ItemUtils.writeInvToNBT(this.craftMatrix, "Crafting", tagCompound);
		this.writeUpdateToNBT(tagCompound);
		return tagCompound;
	}

	public void writeUpdateToNBT(final NBTTagCompound tagCompound) {
		tagCompound.setBoolean("isRunning", this.isRunning);
		tagCompound.setInteger("tickTime", this.tickTime);
	}

	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		if (side.equals(EnumFacing.DOWN))
			return new int[] { 0 };
		return new int[0];
	}

	@Override
	public boolean canInsertItem(final int Index, final ItemStack itemStack, final EnumFacing side) {
		return false;
	}

	@Override
	public boolean canExtractItem(final int Index, final ItemStack itemStack, final EnumFacing side) {
		return Index == outputSlot;
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	public int getBurnTime() {
		return this.tickTime;
	}

	public void setBurnTime(final int burnTime) {
		this.tickTime = burnTime;
	}

	public int getBurnTimeRemainingScaled(final int scale) {
		if (this.tickTime == 0 || this.runTime == 0) {
			return 0;
		}
		return this.tickTime * scale / this.runTime;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("rollingmachine").player(player.inventory).inventory(8, 84).hotbar(8, 142)
			.addInventory().tile(this.craftMatrix).slot(0, 30, 17).slot(1, 48, 17).slot(2, 66, 17).slot(3, 30, 35)
			.slot(4, 48, 35).slot(5, 66, 35).slot(6, 30, 53).slot(7, 48, 53).slot(8, 66, 53)
			.onCraft(inv -> this.inventory.setInventorySlotContents(1,
				RollingMachineRecipe.instance.findMatchingRecipe(inv, this.world)))
			.addInventory().tile(this).outputSlot(0, 124, 35).energySlot(2, 8, 51).syncEnergyValue()
			.syncIntegerValue(this::getBurnTime, this::setBurnTime).addInventory().create();
	}

	private static class RollingTileContainer extends Container {

		@Override
		public boolean canInteractWith(final EntityPlayer entityplayer) {
			return true;
		}

	}
}
