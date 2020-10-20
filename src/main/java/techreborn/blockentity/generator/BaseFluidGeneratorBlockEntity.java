/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.blockentity.generator;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.ItemFluidInfo;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import team.reborn.energy.EnergySide;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.FluidGeneratorRecipe;
import techreborn.api.generator.FluidGeneratorRecipeList;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.utils.FluidUtils;

public abstract class BaseFluidGeneratorBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider {

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

	public BaseFluidGeneratorBlockEntity(BlockEntityType<?> blockEntityType, EFluidGenerator type, String blockEntityName, FluidValue tankCapacity, int euTick) {
		super(blockEntityType);
		recipes = GeneratorRecipeHelper.getFluidRecipesForGenerator(type);
		Validate.notNull(recipes, "null recipe list for " + type.getRecipeID());
		tank = new Tank(blockEntityName, tankCapacity, this);
		inventory = new RebornInventory<>(3, blockEntityName, 64, this);
		this.euTick = euTick;
		this.ticksSinceLastChange = 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick() {
		super.tick();

		if (world == null) {
			return;
		}

		ticksSinceLastChange++;

		if (world.isClient) {
			return;
		}

		// Check cells input slot 2 time per second
		if (ticksSinceLastChange >= 10) {
			ItemStack inputStack = inventory.getStack(0);
			if (!inputStack.isEmpty()) {
				if (FluidUtils.isContainerEmpty(inputStack) && !tank.getFluidAmount().isEmpty()) {
					FluidUtils.fillContainers(tank, inventory, 0, 1);
				} else if (inputStack.getItem() instanceof ItemFluidInfo && getRecipes().getRecipeForFluid(((ItemFluidInfo) inputStack.getItem()).getFluid(inputStack)).isPresent()) {
					FluidUtils.drainContainers(tank, inventory, 0, 1);
				}
			}

			ticksSinceLastChange = 0;
		}

		if (!tank.getFluidAmount().isEmpty()) {
			if (currentRecipe == null || !FluidUtils.fluidEquals(currentRecipe.getFluid(), tank.getFluid()))
				currentRecipe = getRecipes().getRecipeForFluid(tank.getFluid()).orElse(null);

			if (currentRecipe != null) {
				final int euPerBucket = currentRecipe.getEnergyPerBucket();
				final float millibucketsPerTick = euTick * 1000 / (float) euPerBucket;

				if (tryAddingEnergy(euTick)) {
					pendingWithdraw += millibucketsPerTick;
					final int currentWithdraw = (int) pendingWithdraw;
					pendingWithdraw -= currentWithdraw;
					tank.getFluidInstance().subtractAmount(FluidValue.fromRaw(currentWithdraw));
					lastOutput = world.getTime();
				}
			}
		}

		if (world.getTime() - lastOutput < 30 && !isActive()) {
			world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, true));
		} else if (world.getTime() - lastOutput > 30 && isActive()) {
			world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, false));
		}
	}

	public int getProgressScaled(int scale) {
		if (isActive()) {
			return ticksSinceLastChange * scale;
		}
		return 0;
	}

	protected boolean tryAddingEnergy(int amount) {
		 if (getFreeSpace() > 0) {
			addEnergy(amount);
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
	public boolean canAcceptEnergy(EnergySide side) {
		return false;
	}

	@Override
	public RebornInventory<?> getInventory() {
		return inventory;
	}

	@Override
	public void fromTag(BlockState blockState, CompoundTag tagCompound) {
		super.fromTag(blockState, tagCompound);
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

	public FluidValue getTankAmount() {
		return tank.getFluidAmount();
	}

	public void setTankAmount(FluidValue amount) {
		tank.setFluidAmount(amount);
	}

	@Nullable
	@Override
	public Tank getTank() {
		return tank;
	}
}
