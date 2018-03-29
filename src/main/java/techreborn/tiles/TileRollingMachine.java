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

package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.IToolDrop;
import reborncore.api.tile.IInventoryProvider;
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
	public void update() {
		super.update();
		this.charge(2);
		if (!this.world.isRemote) {
			this.currentRecipe = RollingMachineRecipe.instance.findMatchingRecipe(this.craftMatrix, this.world);
			if (!this.currentRecipe.isEmpty() && this.canMake()) {
				if (this.tickTime >= TileRollingMachine.runTime) {
					this.currentRecipe = RollingMachineRecipe.instance.findMatchingRecipe(this.craftMatrix, this.world);
					if (!this.currentRecipe.isEmpty()) {
						boolean hasCrafted = false;
						if (this.inventory.getStackInSlot(outputSlot) == ItemStack.EMPTY) {
							this.inventory.setInventorySlotContents(outputSlot, this.currentRecipe);
							this.tickTime = 0;
							hasCrafted = true;
						} else {
							if (this.inventory.getStackInSlot(outputSlot).getCount() + this.currentRecipe.getCount() <= this.currentRecipe
								.getMaxStackSize()) {
								final ItemStack stack = this.inventory.getStackInSlot(outputSlot);
								stack.setCount(stack.getCount() + this.currentRecipe.getCount());
								this.inventory.setInventorySlotContents(outputSlot, stack);
								this.tickTime = 0;
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
				if (this.canUseEnergy(energyPerTick) && this.tickTime < TileRollingMachine.runTime && canMake()) {
					this.useEnergy(energyPerTick);
					this.tickTime++;
				}
			}
			if (this.currentRecipe.isEmpty()) {
				this.tickTime = 0;
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
		ItemStack stack = RollingMachineRecipe.instance.findMatchingRecipe(this.craftMatrix, this.world);
		if(stack.isEmpty()){
			return false;
		}
		ItemStack output = getStackInSlot(outputSlot);
		if(output.isEmpty()){
			return true;
		}
		return ItemUtils.isItemEqual(stack, output, true, true);
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
		if (this.tickTime == 0 || TileRollingMachine.runTime == 0) {
			return 0;
		}
		return this.tickTime * scale / TileRollingMachine.runTime;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("rollingmachine").player(player.inventory)
			.inventory().hotbar()
			.addInventory().tile(this.craftMatrix)
			.slot(0, 30, 22).slot(1, 48, 22).slot(2, 66, 22)
			.slot(3, 30, 40).slot(4, 48, 40).slot(5, 66, 40)
			.slot(6, 30, 58).slot(7, 48, 58).slot(8, 66, 58)
			.onCraft(inv -> this.inventory.setInventorySlotContents(1, RollingMachineRecipe.instance.findMatchingRecipe(inv, this.world)))
			.addInventory().tile(this)
			.outputSlot(0, 124, 40)
			.energySlot(2, 8, 70)
			.syncEnergyValue().syncIntegerValue(this::getBurnTime, this::setBurnTime).addInventory().create(this);
	}

	public int getProgressScaled(final int scale) {
		if (tickTime != 0 && runTime != 0) {
			return tickTime * scale / runTime;
		}
		return 0;
	}

	private static class RollingTileContainer extends Container {

		@Override
		public boolean canInteractWith(final EntityPlayer entityplayer) {
			return true;
		}

	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}
