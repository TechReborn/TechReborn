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

package techreborn.tiles.fusionReactor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;

import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;

public class TileEntityFusionController extends TilePowerAcceptor implements IInventoryProvider, IContainerProvider {

	public Inventory inventory = new Inventory(3, "TileEntityFusionController", 64, this);

	// 0= no coils, 1 = coils
	public int coilStatus = 0;
	public int crafingTickTime = 0;
	public int finalTickTime = 0;
	public int neededPower = 0;
	int topStackSlot = 0;
	int bottomStackSlot = 1;
	int outputStackSlot = 2;
	FusionReactorRecipe currentRecipe = null;
	boolean hasStartedCrafting = false;

	public TileEntityFusionController() {
		super(4);
	}

	@Override
	public double getMaxPower() {
		return 100000000;
	}

	@Override
	public boolean canAcceptEnergy(final EnumFacing direction) {
		return !(direction == EnumFacing.DOWN || direction == EnumFacing.UP);
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
		return direction == EnumFacing.DOWN || direction == EnumFacing.UP;
	}

	@Override
	public double getMaxOutput() {
		if (!this.hasStartedCrafting) {
			return 0;
		}
		return 1000000;
	}

	@Override
	public double getMaxInput() {
		if (this.hasStartedCrafting) {
			return 0;
		}
		return 8192;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.EXTREME;
	}

	@Override
	public void readFromNBT(final NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		this.crafingTickTime = tagCompound.getInteger("crafingTickTime");
		this.finalTickTime = tagCompound.getInteger("finalTickTime");
		this.neededPower = tagCompound.getInteger("neededPower");
		this.hasStartedCrafting = tagCompound.getBoolean("hasStartedCrafting");
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		if (this.crafingTickTime == -1) {
			this.crafingTickTime = 0;
		}
		if (this.finalTickTime == -1) {
			this.finalTickTime = 0;
		}
		if (this.neededPower == -1) {
			this.neededPower = 0;
		}
		tagCompound.setInteger("crafingTickTime", this.crafingTickTime);
		tagCompound.setInteger("finalTickTime", this.finalTickTime);
		tagCompound.setInteger("neededPower", this.neededPower);
		tagCompound.setBoolean("hasStartedCrafting", this.hasStartedCrafting);
		return tagCompound;
	}

	public boolean checkCoils() {
		if (this.isCoil(this.getPos().getX() + 3, this.getPos().getY(), this.getPos().getZ() + 1)
				&& this.isCoil(this.getPos().getX() + 3, this.getPos().getY(), this.getPos().getZ())
				&& this.isCoil(this.getPos().getX() + 3, this.getPos().getY(), this.getPos().getZ() - 1)
				&& this.isCoil(this.getPos().getX() - 3, this.getPos().getY(), this.getPos().getZ() + 1)
				&& this.isCoil(this.getPos().getX() - 3, this.getPos().getY(), this.getPos().getZ())
				&& this.isCoil(this.getPos().getX() - 3, this.getPos().getY(), this.getPos().getZ() - 1)
				&& this.isCoil(this.getPos().getX() + 2, this.getPos().getY(), this.getPos().getZ() + 2)
				&& this.isCoil(this.getPos().getX() + 2, this.getPos().getY(), this.getPos().getZ() + 1)
				&& this.isCoil(this.getPos().getX() + 2, this.getPos().getY(), this.getPos().getZ() - 1)
				&& this.isCoil(this.getPos().getX() + 2, this.getPos().getY(), this.getPos().getZ() - 2)
				&& this.isCoil(this.getPos().getX() - 2, this.getPos().getY(), this.getPos().getZ() + 2)
				&& this.isCoil(this.getPos().getX() - 2, this.getPos().getY(), this.getPos().getZ() + 1)
				&& this.isCoil(this.getPos().getX() - 2, this.getPos().getY(), this.getPos().getZ() - 1)
				&& this.isCoil(this.getPos().getX() - 2, this.getPos().getY(), this.getPos().getZ() - 2)
				&& this.isCoil(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() + 3)
				&& this.isCoil(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() + 2)
				&& this.isCoil(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() - 2)
				&& this.isCoil(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() - 3)
				&& this.isCoil(this.getPos().getX() - 1, this.getPos().getY(), this.getPos().getZ() + 3)
				&& this.isCoil(this.getPos().getX() - 1, this.getPos().getY(), this.getPos().getZ() + 2)
				&& this.isCoil(this.getPos().getX() - 1, this.getPos().getY(), this.getPos().getZ() - 2)
				&& this.isCoil(this.getPos().getX() - 1, this.getPos().getY(), this.getPos().getZ() - 3)
				&& this.isCoil(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ() + 3)
				&& this.isCoil(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ() - 3)) {
			this.coilStatus = 1;
			return true;
		}
		this.coilStatus = 0;
		return false;
	}

	private boolean isCoil(final int x, final int y, final int z) {
		return this.world.getBlockState(new BlockPos(x, y, z)).getBlock() == ModBlocks.FUSION_COIL;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		// TODO improve this code a lot

		if (this.world.getTotalWorldTime() % 20 == 0) {
			this.checkCoils();
		}

		if (!this.world.isRemote) {
			if (this.coilStatus == 1) {
				if (this.currentRecipe == null) {
					if (this.inventory.hasChanged || this.crafingTickTime != 0) {
						for (final FusionReactorRecipe reactorRecipe : FusionReactorRecipeHelper.reactorRecipes) {
							if (ItemUtils.isItemEqual(this.getStackInSlot(this.topStackSlot), reactorRecipe.getTopInput(), true,
									true, true)) {
								if (reactorRecipe.getBottomInput() != null) {
									if (!ItemUtils.isItemEqual(this.getStackInSlot(this.bottomStackSlot),
											reactorRecipe.getBottomInput(), true, true, true)) {
										break;
									}
								}
								if (this.canFitStack(reactorRecipe.getOutput(), this.outputStackSlot, true)) {
									this.currentRecipe = reactorRecipe;
									if (this.crafingTickTime != 0) {
										this.finalTickTime = this.currentRecipe.getTickTime();
										this.neededPower = (int) this.currentRecipe.getStartEU();
									}
									this.hasStartedCrafting = false;
									this.crafingTickTime = 0;
									this.finalTickTime = this.currentRecipe.getTickTime();
									this.neededPower = (int) this.currentRecipe.getStartEU();
									break;
								}
							}
						}
					}
				} else {
					if (this.inventory.hasChanged) {
						if (!this.validateRecipe()) {
							this.resetCrafter();
							return;
						}
					}
					if (!this.hasStartedCrafting) {
						if (this.canUseEnergy(this.currentRecipe.getStartEU() + 64)) {
							this.useEnergy(this.currentRecipe.getStartEU());
							this.hasStartedCrafting = true;
						}
					} else {
						if (this.crafingTickTime < this.currentRecipe.getTickTime()) {
							if (this.currentRecipe.getEuTick() > 0) { // Power gen
								this.addEnergy(this.currentRecipe.getEuTick()); // Waste
								// power
								// if it
								// has
								// no
								// where
								// to go
								this.crafingTickTime++;
							} else { // Power user
								if (this.canUseEnergy(this.currentRecipe.getEuTick() * -1)) {
									this.setEnergy(this.getEnergy() - this.currentRecipe.getEuTick() * -1);
									this.crafingTickTime++;
								}
							}
						} else {
							if (this.canFitStack(this.currentRecipe.getOutput(), this.outputStackSlot, true)) {
								if (this.getStackInSlot(this.outputStackSlot) == null) {
									this.setInventorySlotContents(this.outputStackSlot, this.currentRecipe.getOutput().copy());
								} else {
									this.decrStackSize(this.outputStackSlot, -this.currentRecipe.getOutput().stackSize);
								}
								this.decrStackSize(this.topStackSlot, this.currentRecipe.getTopInput().stackSize);
								if (this.currentRecipe.getBottomInput() != null) {
									this.decrStackSize(this.bottomStackSlot, this.currentRecipe.getBottomInput().stackSize);
								}
								this.resetCrafter();
							}
						}
					}
				}
			} else {
				if (this.currentRecipe != null) {
					this.resetCrafter();
				}
			}
		}

		if (this.inventory.hasChanged) {
			this.inventory.hasChanged = false;
		}
	}

	private boolean validateRecipe() {
		if (ItemUtils.isItemEqual(this.getStackInSlot(this.topStackSlot), this.currentRecipe.getTopInput(), true, true, true)) {
			if (this.currentRecipe.getBottomInput() != null) {
				if (!ItemUtils.isItemEqual(this.getStackInSlot(this.bottomStackSlot), this.currentRecipe.getBottomInput(), true, true,
						true)) {
					return false;
				}
			}
			if (this.canFitStack(this.currentRecipe.getOutput(), this.outputStackSlot, true)) {
				return true;
			}
		}
		return false;
	}

	private void resetCrafter() {
		this.currentRecipe = null;
		this.crafingTickTime = -1;
		this.finalTickTime = -1;
		this.neededPower = -1;
		this.hasStartedCrafting = false;
	}

	public boolean canFitStack(final ItemStack stack, final int slot, final boolean oreDic) {// Checks to see if it can fit the stack
		if (stack == null) {
			return true;
		}
		if (this.inventory.getStackInSlot(slot) == null) {
			return true;
		}
		if (ItemUtils.isItemEqual(this.inventory.getStackInSlot(slot), stack, true, true, oreDic)) {
			if (stack.stackSize + this.inventory.getStackInSlot(slot).stackSize <= stack.getMaxStackSize()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	public int getCoilStatus() {
		return this.coilStatus;
	}

	public void setCoilStatus(final int coilStatus) {
		this.coilStatus = coilStatus;
	}

	public int getCrafingTickTime() {
		return this.crafingTickTime;
	}

	public void setCrafingTickTime(final int crafingTickTime) {
		this.crafingTickTime = crafingTickTime;
	}

	public int getFinalTickTime() {
		return this.finalTickTime;
	}

	public void setFinalTickTime(final int finalTickTime) {
		this.finalTickTime = finalTickTime;
	}

	public int getNeededPower() {
		return this.neededPower;
	}

	public void setNeededPower(final int neededPower) {
		this.neededPower = neededPower;
	}

	public int getProgressScaled() {
		return Math.max(0, Math.min(24, (this.getCrafingTickTime() > 0 ? 1 : 0)
				+ this.getCrafingTickTime() * 24 / (this.finalTickTime < 1 ? 1 : this.finalTickTime)));
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("fusionreactor").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(this).slot(0, 88, 17).slot(1, 88, 53).outputSlot(2, 148, 35).syncEnergyValue()
				.syncIntegerValue(this::getCoilStatus, this::setCoilStatus)
				.syncIntegerValue(this::getCrafingTickTime, this::setCrafingTickTime)
				.syncIntegerValue(this::getFinalTickTime, this::setFinalTickTime)
				.syncIntegerValue(this::getNeededPower, this::setNeededPower).addInventory().create();
	}
}
