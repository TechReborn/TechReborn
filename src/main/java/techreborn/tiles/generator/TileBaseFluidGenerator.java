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

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.api.tile.InventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import reborncore.fluid.FluidStack;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.FluidGeneratorRecipe;
import techreborn.api.generator.FluidGeneratorRecipeList;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.utils.FluidUtils;

import javax.annotation.Nullable;

public abstract class TileBaseFluidGenerator extends TilePowerAcceptor implements IToolDrop, InventoryProvider {

	private final FluidGeneratorRecipeList recipes;
	private final int euTick;
	private FluidGeneratorRecipe currentRecipe;
	private int ticksSinceLastChange;
	public final Tank tank;
	public final RebornInventory<?> inventory;
	protected long lastOutput = 0;

	/*
	 * We use this to keep track of fractional millibuckets, allowing us to hit
	 * our eu/bucket targets while still only ever removing integer millibucket
	 * amounts.
	 */
	double pendingWithdraw = 0.0;

	public TileBaseFluidGenerator(BlockEntityType<?> tileEntityType, EFluidGenerator type, String tileName, int tankCapacity, int euTick) {
		super(tileEntityType);
		recipes = GeneratorRecipeHelper.getFluidRecipesForGenerator(type);
		tank = new Tank(tileName, tankCapacity, this);
		inventory = new RebornInventory<>(3, tileName, 64, this).withConfiguredAccess();
		this.euTick = euTick;
		this.ticksSinceLastChange = 0;
	}

	@Override
	public void tick() {
		super.tick();
		ticksSinceLastChange++;

		if(world.isClient){
			return;
		}

		// Check cells input slot 2 time per second
		// Please, keep ticks counting on client also to report progress to GUI
		if (ticksSinceLastChange >= 10) {
			if (!inventory.getInvStack(0).isEmpty()) {
				FluidUtils.drainContainers(tank, inventory, 0, 1);
				FluidUtils.fillContainers(tank, inventory, 0, 1, tank.getFluidType());
			}
			tank.setTileEntity(this);
			tank.compareAndUpdate();

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
					lastOutput = world.getTime();
				}
			}
		}

		if (world.getTime() - lastOutput < 30 && !isActive()) {
			world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, true));
		}
		else if (world.getTime() - lastOutput > 30 && isActive()) {
			world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, false));
		}
	}
	
	public int getProgressScaled(int scale) {
		if (isActive()){
			return ticksSinceLastChange * scale;
		}
		return 0;
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

	protected boolean acceptFluid() {
		if (!inventory.getInvStack(0).isEmpty()) {
			FluidStack stack = FluidUtils.getFluidStackInContainer(inventory.getInvStack(0));
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
	public boolean canAcceptEnergy(Direction direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(Direction direction) {
		return true;
	}

	@Override
	public RebornInventory<?> getInventory() {
		return inventory;
	}

	@Override
	public void fromTag(CompoundTag tagCompound) {
		super.fromTag(tagCompound);
		tank.read(tagCompound);
	}

	@Override
	public CompoundTag toTag(CompoundTag tagCompound) {
		super.toTag(tagCompound);
		tank.write(tagCompound);
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

	public int getTankAmount(){
		return tank.getFluidAmount();
	}

	public void setTankAmount(int amount){
		tank.setFluidAmount(amount);
	}

	@Nullable
	@Override
	public Tank getTank() {
		return tank;
	}
}
