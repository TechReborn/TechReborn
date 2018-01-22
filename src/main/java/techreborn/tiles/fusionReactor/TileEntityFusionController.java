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
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;

public class TileEntityFusionController extends TilePowerAcceptor
		implements IInventoryProvider, IContainerProvider, IWrenchable {

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
	
	public boolean checkCoils() {
		int posX = this.getPos().getX();
		int posY = this.getPos().getY();
		int posZ = this.getPos().getZ();

		if (isCoil(posX + 3, posY, posZ + 1) && isCoil(posX + 3, posY, posZ) && isCoil(posX + 3, posY, posZ - 1)
				&& isCoil(posX - 3, posY, posZ + 1) && isCoil(posX - 3, posY, posZ) && isCoil(posX - 3, posY, posZ - 1)
				&& isCoil(posX + 2, posY, posZ + 2) && isCoil(posX + 2, posY, posZ + 1)
				&& isCoil(posX + 2, posY, posZ - 1) && isCoil(posX + 2, posY, posZ - 2)
				&& isCoil(posX - 2, posY, posZ + 2) && isCoil(posX - 2, posY, posZ + 1)
				&& isCoil(posX - 2, posY, posZ - 1) && isCoil(posX - 2, posY, posZ - 2)
				&& isCoil(posX + 1, posY, posZ + 3) && isCoil(posX + 1, posY, posZ + 2)
				&& isCoil(posX + 1, posY, posZ - 2) && isCoil(posX + 1, posY, posZ - 3)
				&& isCoil(posX - 1, posY, posZ + 3) && isCoil(posX - 1, posY, posZ + 2)
				&& isCoil(posX - 1, posY, posZ - 2) && isCoil(posX - 1, posY, posZ - 3) && isCoil(posX, posY, posZ + 3)
				&& isCoil(posX, posY, posZ - 3)) {
			coilStatus = 1;
			return true;
		}
		coilStatus = 0;
		return false;
	}

	private boolean isCoil(final int x, final int y, final int z) {
		return this.world.getBlockState(new BlockPos(x, y, z)).getBlock() == ModBlocks.FUSION_COIL;
	}
	
	private void resetCrafter() {
		this.currentRecipe = null;
		this.crafingTickTime = 0;
		this.finalTickTime = 0;
		this.neededPower = 0;
		this.hasStartedCrafting = false;
	}

	/**
	 * Checks to see if it can fit given stack into given slot
	 * 
	 * @param stack Itemstack to fit
	 * @param slot Int slot ID to fit into
	 * @param oreDic boolean Use oreDictionary
	 * @return boolean true if stack will fit into slot
	 */
	public boolean canFitStack(final ItemStack stack, final int slot, final boolean oreDic) {
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
	
	public int getProgressScaled() {
		return Math.max(0, Math.min(24, (this.getCrafingTickTime() > 0 ? 1 : 0)
				+ this.getCrafingTickTime() * 24 / (this.finalTickTime < 1 ? 1 : this.finalTickTime)));
	}
	
	private void updateCurrentRecipe() {
		for (final FusionReactorRecipe reactorRecipe : FusionReactorRecipeHelper.reactorRecipes) {
			if (validateReactorRecipe(reactorRecipe)) {
				this.currentRecipe = reactorRecipe;
				this.crafingTickTime = 0;
				this.finalTickTime = this.currentRecipe.getTickTime();
				this.neededPower = (int) this.currentRecipe.getStartEU();
				this.hasStartedCrafting = false;
				break;				
			}
		}
	}
	
	private boolean validateReactorRecipe(FusionReactorRecipe recipe) {
		if (ItemUtils.isItemEqual(this.getStackInSlot(topStackSlot), recipe.getTopInput(), true, true, true)) {
			if (recipe.getBottomInput() != null) {
				if (!ItemUtils.isItemEqual(this.getStackInSlot(bottomStackSlot), recipe.getBottomInput(), true, true, true)) {
					return false;
				}
			}
			if (this.canFitStack(recipe.getOutput(), outputStackSlot, true)) {
				return true;
			}
		}
		return false;
	}

	// TilePowerAcceptor
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

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (this.world.isRemote) {
			return;
		}

		// Force check every second
		if (this.world.getTotalWorldTime() % 20 == 0) {
			checkCoils();
			this.inventory.hasChanged = true;
		}
		
		if (this.coilStatus == 0) {
			resetCrafter();
			return;
		}
		
		if (this.currentRecipe == null && this.inventory.hasChanged == true) {
			updateCurrentRecipe();
		}
		
		if (this.currentRecipe != null) {
			if (!this.hasStartedCrafting) {
				if (this.canUseEnergy(this.currentRecipe.getStartEU())) {
					this.useEnergy(this.currentRecipe.getStartEU());
					this.hasStartedCrafting = true;
				}
			}

			if (this.hasStartedCrafting && this.crafingTickTime < this.finalTickTime) {
				// Power gen
				if (this.currentRecipe.getEuTick() > 0) {
					this.addEnergy(this.currentRecipe.getEuTick());
					this.crafingTickTime++;
				}
				// Power user
				else {
					if (this.canUseEnergy(this.currentRecipe.getEuTick() * -1)) {
						this.setEnergy(this.getEnergy() - this.currentRecipe.getEuTick() * -1);
						this.crafingTickTime++;
					}
				}
			} else if (this.crafingTickTime >= this.finalTickTime) {
				if (this.canFitStack(this.currentRecipe.getOutput(), outputStackSlot, true)) {
					if (this.getStackInSlot(outputStackSlot) == null) {
						this.setInventorySlotContents(outputStackSlot, this.currentRecipe.getOutput().copy());
					} else {
						this.decrStackSize(outputStackSlot, -this.currentRecipe.getOutput().stackSize);
					}
					this.decrStackSize(topStackSlot, this.currentRecipe.getTopInput().stackSize);
					if (this.currentRecipe.getBottomInput() != null) {
						this.decrStackSize(bottomStackSlot, this.currentRecipe.getBottomInput().stackSize);
					}
					if (validateReactorRecipe(this.currentRecipe)) {
						this.crafingTickTime = 0;
					} else {
						resetCrafter();
					}
				} else {
					resetCrafter();
				}
			}
			this.markDirty();
		}
		
		if (this.inventory.hasChanged) {
			this.inventory.hasChanged = false;
		}
	}
	
	// TileLegacyMachineBase
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
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0, 1, 2 };
	}
	
    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction){
		if (index == 0 || index == 1) {
			return true;
		}
		return false;
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
	@Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction){
		return index == 2;
	}

	// IInventoryProvider
	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("fusionreactor").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(this).slot(0, 88, 17).slot(1, 88, 53).outputSlot(2, 148, 35).syncEnergyValue()
				.syncIntegerValue(this::getCoilStatus, this::setCoilStatus)
				.syncIntegerValue(this::getCrafingTickTime, this::setCrafingTickTime)
				.syncIntegerValue(this::getFinalTickTime, this::setFinalTickTime)
				.syncIntegerValue(this::getNeededPower, this::setNeededPower).addInventory().create();
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

	// IWrenchable
	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing enumFacing) {
		return !entityPlayer.isSneaking();
	}

	@Override
	public EnumFacing getFacing() {
		return this.getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return true;
	}

	@Override
	public float getWrenchDropRate() {
		return 1F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.FUSION_CONTROL_COMPUTER);
	}
}