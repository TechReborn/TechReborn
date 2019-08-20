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

package techreborn.blockentity.machine.iron;

import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.util.RebornInventory;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class IronAlloyFurnaceBlockEntity extends MachineBaseBlockEntity
	implements IToolDrop, InventoryProvider, IContainerProvider {

	public int tickTime;
	public RebornInventory<IronAlloyFurnaceBlockEntity> inventory = new RebornInventory<>(4, "IronAlloyFurnaceBlockEntity", 64, this);
	public int burnTime;
	public int currentItemBurnTime;
	public int cookTime;
	int input1 = 0;
	int input2 = 1;
	int output = 2;
	int fuel = 3;

	public IronAlloyFurnaceBlockEntity() {
		super(TRBlockEntities.IRON_ALLOY_FURNACE);
	}

	/**
	 * Returns the number of ticks that the supplied fuel item will keep the
	 * furnace burning, or 0 if the item isn't fuel
	 * @param stack Itemstack of fuel
	 * @return Integer Number of ticks
	 */
	public static int getItemBurnTime(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		} else {
			return FurnaceBlockEntity.createFuelTimeMap().getOrDefault(stack.getItem(), 0);
		}
	}

	@Override
	public void tick() {
		super.tick();
		final boolean flag = this.burnTime > 0;
		boolean flag1 = false;
		if (this.burnTime > 0) {
			--this.burnTime;
		}
		if (!this.world.isClient) {
			if (this.burnTime != 0 || !inventory.getInvStack(this.input1).isEmpty()&& !inventory.getInvStack(this.fuel).isEmpty()) {
				if (this.burnTime == 0 && this.canSmelt()) {
					this.currentItemBurnTime = this.burnTime = IronAlloyFurnaceBlockEntity.getItemBurnTime(inventory.getInvStack(this.fuel));
					if (this.burnTime > 0) {
						flag1 = true;
						if (!inventory.getInvStack(this.fuel).isEmpty()) {
							inventory.shrinkSlot(this.fuel, 1);
						}
					}
				}
				if (this.isBurning() && this.canSmelt()) {
					++this.cookTime;
					if (this.cookTime == 200) {
						this.cookTime = 0;
						this.smeltItem();
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
			}
			if (flag != this.burnTime > 0) {
				flag1 = true;
			}
		}
		if (flag1) {
			this.markDirty();
		}
	}

	public boolean hasAllInputs(final RebornRecipe recipeType) {
		if (recipeType == null) {
			return false;
		}
		for (RebornIngredient ingredient : recipeType.getRebornIngredients()) {
			boolean hasItem = false;
			for (int inputslot = 0; inputslot < 2; inputslot++) {
				if (ingredient.test(inventory.getInvStack(inputslot))) {
					hasItem = true;
				}
			}
			if (!hasItem)
				return false;
		}
		return true;
	}

	private boolean canSmelt() {
		if (inventory.getInvStack(this.input1).isEmpty() || inventory.getInvStack(this.input2).isEmpty()) {
			return false;
		} else {
			ItemStack itemstack = null;
			for (final RebornRecipe recipeType : ModRecipes.ALLOY_SMELTER.getRecipes(world)) {
				if (this.hasAllInputs(recipeType)) {
					itemstack = recipeType.getOutputs().get(0);
					break;
				}
			}

			if (itemstack == null)
				return false;
			if (inventory.getInvStack(this.output).isEmpty())
				return true;
			if (!inventory.getInvStack(this.output).isItemEqualIgnoreDamage(itemstack))
				return false;
			final int result = inventory.getInvStack(this.output).getCount() + itemstack.getCount();
			return result <= inventory.getStackLimit() && result <= inventory.getInvStack(this.output).getMaxCount(); // Forge
			// BugFix:
			// Make
			// it
			// respect
			// stack
			// sizes
			// properly.
		}
	}

	/**
	 * Turn one item from the furnace source stack into the appropriate smelted
	 * item in the furnace result stack
	 */
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = ItemStack.EMPTY;
			for (final RebornRecipe recipeType : ModRecipes.ALLOY_SMELTER.getRecipes(world)) {
				if (this.hasAllInputs(recipeType)) {
					itemstack = recipeType.getOutputs().get(0);
					break;
				}
				if (!itemstack.isEmpty()) {
					break;
				}
			}

			if (inventory.getInvStack(this.output).isEmpty()) {
				inventory.setInvStack(this.output, itemstack.copy());
			} else if (inventory.getInvStack(this.output).getItem() == itemstack.getItem()) {
				inventory.shrinkSlot(this.output, -itemstack.getCount());
			}

			for (final RebornRecipe recipeType : ModRecipes.ALLOY_SMELTER.getRecipes(world)) {
				boolean hasAllRecipes = true;
				if (this.hasAllInputs(recipeType)) {

				} else {
					hasAllRecipes = false;
				}
				if (hasAllRecipes) {
					for (RebornIngredient ingredient : recipeType.getRebornIngredients()) {
						for (int inputSlot = 0; inputSlot < 2; inputSlot++) {
							if (ingredient.test(this.inventory.getInvStack(inputSlot))) {
								inventory.shrinkSlot(inputSlot, ingredient.getCount());
								break;
							}
						}
					}
				}
			}

		}
	}

	/**
	 * Furnace isBurning
	 * @return Boolean True if furnace is burning
	 */
	public boolean isBurning() {
		return this.burnTime > 0;
	}

	public int getBurnTimeRemainingScaled(final int scale) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = 200;
		}

		return this.burnTime * scale / this.currentItemBurnTime;
	}

	public int getCookProgressScaled(final int scale) {
		return this.cookTime * scale / 200;
	}

	@Override
	public Direction getFacing() {
		return this.getFacingEnum();
	}

	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return TRContent.Machine.IRON_ALLOY_FURNACE.getStack();
	}

	public boolean isComplete() {
		return false;
	}

	@Override
	public RebornInventory<IronAlloyFurnaceBlockEntity> getInventory() {
		return this.inventory;
	}


	public int getBurnTime() {
		return this.burnTime;
	}

	public void setBurnTime(final int burnTime) {
		this.burnTime = burnTime;
	}

	public int getCurrentItemBurnTime() {
		return this.currentItemBurnTime;
	}

	public void setCurrentItemBurnTime(final int currentItemBurnTime) {
		this.currentItemBurnTime = currentItemBurnTime;
	}

	public int getCookTime() {
		return this.cookTime;
	}

	public void setCookTime(final int cookTime) {
		this.cookTime = cookTime;
	}

	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("alloyfurnace").player(player.inventory).inventory().hotbar()
			.addInventory().blockEntity(this)
			.slot(0, 47, 17)
			.slot(1, 65, 17)
			.outputSlot(2, 116, 35).fuelSlot(3, 56, 53).syncIntegerValue(this::getBurnTime, this::setBurnTime)
			.syncIntegerValue(this::getCookTime, this::setCookTime)
			.syncIntegerValue(this::getCurrentItemBurnTime, this::setCurrentItemBurnTime).addInventory().create(this, syncID);
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}
