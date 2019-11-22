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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import reborncore.api.IToolDrop;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.fluids.RebornFluidTank;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;

import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.FluidGeneratorRecipe;
import techreborn.api.generator.FluidGeneratorRecipeList;
import techreborn.api.generator.GeneratorRecipeHelper;

import javax.annotation.Nullable;

public abstract class TileBaseFluidGenerator extends TilePowerAcceptor implements IToolDrop, IInventoryProvider {
	// Fields >>
	private final FluidGeneratorRecipeList recipes;
	private final int euTick;
	private FluidGeneratorRecipe currentRecipe;
	private int ticksSinceLastChange;
	public RebornFluidTank tank;
	public Inventory inventory;
	protected long lastOutput = 0;
	// << Fields

	/*
	 * We use this to keep track of fractional millibuckets, allowing us to hit
	 * our eu/bucket targets while still only ever removing integer millibucket
	 * amounts.
	 */
	double pendingWithdraw = 0.0;

	public TileBaseFluidGenerator(EFluidGenerator type, String tileName, int tankCapacity, int euTick) {
		super();
		recipes = GeneratorRecipeHelper.getFluidRecipesForGenerator(type);
		tank = new RebornFluidTank(tileName, tankCapacity, this);
		inventory = new Inventory(2, tileName, 64, this);
		this.euTick = euTick;
		this.ticksSinceLastChange = 0;
	}

	@Override
	public void update() {
		// Check cells input slot 2 times per second
		if (!world.isRemote && world.getTotalWorldTime() % 10 == 0) {
			if (!inventory.getStackInSlot(0).isEmpty()) {
				FluidUtils.drainContainers(tank, inventory, 0, 1);
				FluidUtils.fillContainers(tank, inventory, 0, 1, tank.getFluidType());
			}
		}

		if (!world.isRemote) super.update();

		ticksSinceLastChange++;

		// Check cells input slot 2s time per second
		// Please, keep ticks counting on client also to report progress to GUI
		if (ticksSinceLastChange >= 10) {
			ticksSinceLastChange = 0;
		}

		if (tank.getFluidAmount() > 0) {
			if (currentRecipe == null || !FluidUtils.fluidEquals(currentRecipe.getFluid(), tank.getFluidType()))
				currentRecipe = getRecipes().getRecipeForFluid(tank.getFluidType()).orElse(null);

			if (currentRecipe != null) {
				final Integer euPerBucket = currentRecipe.getEnergyPerMb() * 1000;
				final float millibucketsPerTick = euTick * 1000 / (float) euPerBucket;

				if (tryAddingEnergy(euTick)) {
					pendingWithdraw += millibucketsPerTick;
					final int currentWithdraw = (int) pendingWithdraw;
					pendingWithdraw -= currentWithdraw;
					tank.drain(currentWithdraw, true);
					lastOutput = world.getTotalWorldTime();
				}
			}
		}

		if (world.getTotalWorldTime() - lastOutput < 30 && !isActive())
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockMachineBase.ACTIVE, true));
		else if (world.getTotalWorldTime() - lastOutput > 30 && isActive())
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockMachineBase.ACTIVE, false));
	}
	
	public int getProgressScaled(int scale) {
		return isActive() ? ticksSinceLastChange * scale : 0;
	}

	protected boolean tryAddingEnergy(int amount) {
		if (getMaxPower() - getEnergy() >= amount) {
			addEnergy(amount);
			return true;
		} else if (getMaxPower() - getEnergy() > 0) {
			addEnergy(getMaxPower() - getEnergy());
			return true;
		}

		return false;
	}

	public FluidGeneratorRecipeList getRecipes() {
		return recipes;
	}

	@Override
	public double getBaseMaxOutput() {
		return euTick;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
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
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		tank.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tank.writeToNBT(tagCompound);
		return tagCompound;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	public int getTicksSinceLastChange() {
		return ticksSinceLastChange;
	}

	public void setTicksSinceLastChange(int ticksSinceLastChange) {
		this.ticksSinceLastChange = ticksSinceLastChange;
	}

	@Nullable
	@Override
	public RebornFluidTank getTank() {
		return tank;
	}
}
