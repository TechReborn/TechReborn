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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import reborncore.api.tile.IInventoryProvider;
import reborncore.api.IToolDrop;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.FluidGeneratorRecipe;
import techreborn.api.generator.FluidGeneratorRecipeList;
import techreborn.api.generator.GeneratorRecipeHelper;

public abstract class TileBaseFluidGenerator extends TilePowerAcceptor implements IToolDrop, IInventoryProvider {

	private final FluidGeneratorRecipeList recipes;
	private final int euTick;
	private FluidGeneratorRecipe currentRecipe;
	private int ticksSinceLastChange;
	public final Tank tank;
	public final Inventory inventory;
	protected long lastOutput = 0;

	/*
	 * We use this to keep track of fractional millibuckets, allowing us to hit
	 * our eu/bucket targets while still only ever removing integer millibucket
	 * amounts.
	 */
	double pendingWithdraw = 0.0;

	public TileBaseFluidGenerator(EFluidGenerator type, String tileName, int tankCapacity, int euTick) {
		super();
		recipes = GeneratorRecipeHelper.getFluidRecipesForGenerator(type);
		tank = new Tank(tileName, tankCapacity, this);
		inventory = new Inventory(3, tileName, 64, this);
		this.euTick = euTick;
		this.ticksSinceLastChange = 0;
	}

	@Override
	public void update() {
		super.update();
		this.ticksSinceLastChange++;

		// Check cells input slot 2 time per second
		if (this.ticksSinceLastChange >= 10) {
			if (!this.inventory.getStackInSlot(0).isEmpty()) {
				FluidUtils.drainContainers(this.tank, this.inventory, 0, 1);
				FluidUtils.fillContainers(this.tank, this.inventory, 0, 1, this.tank.getFluidType());
			}
			this.ticksSinceLastChange = 0;
		}

		if (this.tank.getFluidAmount() > 0) {
			if (this.currentRecipe == null || !this.currentRecipe.getFluid().equals(this.tank.getFluidType()))
				this.currentRecipe = this.getRecipes().getRecipeForFluid(this.tank.getFluidType()).orElse(null);

			if (this.currentRecipe != null) {
				final Integer euPerBucket = this.currentRecipe.getEnergyPerMb() * 1000;
				final float millibucketsPerTick = this.euTick * 1000 / (float) euPerBucket;

				if (this.tryAddingEnergy(this.euTick)) {
					this.pendingWithdraw += millibucketsPerTick;
					final int currentWithdraw = (int) this.pendingWithdraw;
					this.pendingWithdraw -= currentWithdraw;
					this.tank.drain(currentWithdraw, true);
					this.lastOutput = this.world.getTotalWorldTime();
				}
			}
		}
		
		if (!this.world.isRemote) {
			if (this.world.getTotalWorldTime() - this.lastOutput < 30 && !this.isActive())
				this.world.setBlockState(this.getPos(), this.world.getBlockState(this.getPos()).withProperty(BlockMachineBase.ACTIVE, true));
			else if (this.world.getTotalWorldTime() - this.lastOutput > 30 && this.isActive())
				this.world.setBlockState(this.getPos(), this.world.getBlockState(this.getPos()).withProperty(BlockMachineBase.ACTIVE, false));
		}

	}
	
	public int getProgressScaled(final int scale) {
		if (this.isActive()){
			return this.ticksSinceLastChange * scale;
		}
		return 0;
	}

	protected boolean tryAddingEnergy(int amount) {
		if (this.getMaxPower() - this.getEnergy() >= amount) {
			addEnergy(amount);
			return true;
		} else if (this.getMaxPower() - this.getEnergy() > 0) {
			addEnergy(this.getMaxPower() - this.getEnergy());
			return true;
		}
		return false;
	}

	protected boolean acceptFluid() {
		if (!this.getStackInSlot(0).isEmpty()) {
			FluidStack stack = FluidUtils.getFluidStackInContainer(this.getStackInSlot(0));
			if (stack != null)
				return recipes.getRecipeForFluid(stack.getFluid()).isPresent();
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) tank;
		}
		return super.getCapability(capability, facing);
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
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		world.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(),
			getPos().getY(), getPos().getZ());
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}
