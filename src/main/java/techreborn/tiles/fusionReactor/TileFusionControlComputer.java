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
import reborncore.api.IToolDrop;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileFusionControlComputer extends TilePowerAcceptor
		implements IToolDrop, IInventoryProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "fusion_reactor", key = "FusionReactorMaxInput", comment = "Fusion Reactor Max Input (Value in EU)")
	public static int maxInput = 8192;
	@ConfigRegistry(config = "machines", category = "fusion_reactor", key = "FusionReactorMaxOutput", comment = "Fusion Reactor Max Output (Value in EU)")
	public static int maxOutput = 1000000;
	@ConfigRegistry(config = "machines", category = "fusion_reactor", key = "FusionReactorMaxEnergy", comment = "Fusion Reactor Max Energy (Value in EU)")
	public static int maxEnergy = 100000000;

	public Inventory inventory;

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

	public TileFusionControlComputer() {
		super();
		this.inventory = new Inventory(3, "TileFusionControlComputer", 64, this);
	}

	/**
	 * Check that reactor has all necessary coils in place
	 * 
	 * @return boolean Return true if coils are present
	 */
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

	/**
	 * Checks if block is fusion coil
	 * 
	 * @param x int X coordinate for block
	 * @param y int Y coordinate for block
	 * @param z int Z coordinate for block
	 * @return boolean Returns true if block is fusion coil
	 */
	private boolean isCoil(final int x, final int y, final int z) {
		return this.world.getBlockState(new BlockPos(x, y, z)).getBlock() == ModBlocks.FUSION_COIL;
	}

	/**
	 * Resets crafter progress and recipe
	 */
	private void resetCrafter() {
		this.currentRecipe = null;
		this.crafingTickTime = 0;
		this.finalTickTime = 0;
		this.neededPower = 0;
		this.hasStartedCrafting = false;
	}

	/**
	 * Checks that ItemStack could be inserted into slot provided, including check
	 * for existing item in slot and maximum stack size
	 * 
	 * @param stack ItemStack ItemStack to insert
	 * @param slot int Slot ID to check
	 * @param oreDic boolean Should we use ore dictionary
	 * @return boolean Returns true if ItemStack will fit into slot
	 */
	public boolean canFitStack(final ItemStack stack, final int slot, final boolean oreDic) {// Checks to see if it can
																								// fit the stack
		if (stack.isEmpty()) {
			return true;
		}
		if (this.inventory.getStackInSlot(slot).isEmpty()) {
			return true;
		}
		if (ItemUtils.isItemEqual(this.inventory.getStackInSlot(slot), stack, true, true, oreDic)) {
			if (stack.getCount() + this.inventory.getStackInSlot(slot).getCount() <= stack.getMaxStackSize()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns progress scaled to input value
	 * 
	 * @param scale int Maximum value for progress
	 * @return int Scale of progress
	 */
	public int getProgressScaled(int scale) {
		if (this.crafingTickTime != 0) {
			return this.crafingTickTime * scale / this.finalTickTime;
		}
		return 0;
	}

	/**
	 * Tries to set current recipe based in inputs in reactor
	 */
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

	/**
	 * Validates that reactor can execute recipe provided, e.g. has all inputs and can fit output
	 * 
	 * @param recipe FusionReactorRecipe Recipe to validate
	 * @return boolean True if reactor can execute recipe provided 
	 */
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
	public void update() {
		super.update();

		if (this.world.isRemote) {
			return;
		}

		// Force check every second
		if (this.world.getTotalWorldTime() % 20 == 0) {
			this.checkCoils();
			this.inventory.hasChanged = true;
		}

		if (this.coilStatus == 0) {
			this.resetCrafter();
			return;
		}

		if (this.currentRecipe == null && this.inventory.hasChanged == true) {
			updateCurrentRecipe();
		}

		if (this.currentRecipe != null) {
			if (this.inventory.hasChanged && !validateReactorRecipe(this.currentRecipe)) {
				resetCrafter();
				return;
			}

			if (!this.hasStartedCrafting) {
				// Ignition!
				if (this.canUseEnergy(this.currentRecipe.getStartEU())) {
					this.useEnergy(this.currentRecipe.getStartEU());
					this.hasStartedCrafting = true;
				}
			}
			if (hasStartedCrafting && this.crafingTickTime < this.finalTickTime) {
				this.crafingTickTime++;
				// Power gen
				if (this.currentRecipe.getEuTick() > 0) {
					// Waste power if it has no where to go
					this.addEnergy(this.currentRecipe.getEuTick());
				} else { // Power user
					if (this.canUseEnergy(this.currentRecipe.getEuTick() * -1)) {
						this.setEnergy(this.getEnergy() - this.currentRecipe.getEuTick() * -1);
					}
				}
			} else if (this.crafingTickTime >= this.finalTickTime) {
				if (this.canFitStack(this.currentRecipe.getOutput(), this.outputStackSlot, true)) {
					if (this.getStackInSlot(this.outputStackSlot).isEmpty()) {
						this.setInventorySlotContents(this.outputStackSlot, this.currentRecipe.getOutput().copy());
					} else {
						this.decrStackSize(this.outputStackSlot, -this.currentRecipe.getOutput().getCount());
					}
					this.decrStackSize(this.topStackSlot, this.currentRecipe.getTopInput().getCount());
					if (!this.currentRecipe.getBottomInput().isEmpty()) {
						this.decrStackSize(this.bottomStackSlot, this.currentRecipe.getBottomInput().getCount());
					}
					if (this.validateReactorRecipe(this.currentRecipe)) {
						this.crafingTickTime = 0;
					} else {
						this.resetCrafter();
					}
				}
			}
			this.markDirty();
		}

		if (this.inventory.hasChanged) {
			this.inventory.hasChanged = false;
		}
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
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
	public double getBaseMaxOutput() {
		if (!this.hasStartedCrafting) {
			return 0;
		}
		return maxOutput;
	}

	@Override
	public double getBaseMaxInput() {
		if (this.hasStartedCrafting) {
			return 0;
		}
		return maxInput;
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
		tagCompound.setInteger("crafingTickTime", this.crafingTickTime);
		tagCompound.setInteger("finalTickTime", this.finalTickTime);
		tagCompound.setInteger("neededPower", this.neededPower);
		tagCompound.setBoolean("hasStartedCrafting", this.hasStartedCrafting);
		return tagCompound;
	}

	// TileLegacyMachineBase
	@Override
	public void onLoad() {
		super.onLoad();
		this.checkCoils();
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// @Override
	// public String getName() {
	// return null;
	// }
	//
	// @Override
	// public boolean hasCustomName() {
	// return false;
	// }
	//
	// @Override
	// public ITextComponent getDisplayName() {
	// return null;
	// }

	// IToolDrop
	@Override
	public ItemStack getToolDrop(EntityPlayer p0) {
		return new ItemStack(ModBlocks.FUSION_CONTROL_COMPUTER, 1);
	}

	// IInventoryProvider
	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("fusionreactor").player(player.inventory).inventory().hotbar()
				.addInventory().tile(this).slot(0, 34, 47).slot(1, 126, 47).outputSlot(2, 80, 47).syncEnergyValue()
				.syncIntegerValue(this::getCoilStatus, this::setCoilStatus)
				.syncIntegerValue(this::getCrafingTickTime, this::setCrafingTickTime)
				.syncIntegerValue(this::getFinalTickTime, this::setFinalTickTime)
				.syncIntegerValue(this::getNeededPower, this::setNeededPower).addInventory().create(this);
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
}
