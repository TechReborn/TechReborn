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

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.util.RebornInventory;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class IronAlloyFurnaceBlockEntity extends MachineBaseBlockEntity
	implements IToolDrop, InventoryProvider, IContainerProvider {

	public RebornInventory<IronAlloyFurnaceBlockEntity> inventory = new RebornInventory<>(4, "IronAlloyFurnaceBlockEntity", 64, this);
	public int burnTime;
	public int totalBurnTime;
	public int progress;
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
		}
		return AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(stack.getItem(), 0);
	}
	
	@Override
	public void fromTag(CompoundTag compoundTag) {
		super.fromTag(compoundTag);
		burnTime = compoundTag.getInt("BurnTime");
		totalBurnTime = compoundTag.getInt("TotalBurnTime");
		progress = compoundTag.getInt("Progress");
	}
	
	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		 super.toTag(compoundTag);
		 compoundTag.putInt("BurnTime", burnTime);
		 compoundTag.putInt("TotalBurnTime", totalBurnTime);
		 compoundTag.putInt("Progress", progress);
		 return compoundTag;
	}

	@Override
	public void tick() {
		super.tick();
		if(world.isClient){
			return;
		}
		
		boolean isBurning = isBurning();
		boolean updateInventory = false;
		if (isBurning) {
			--burnTime;
		}
		if (burnTime != 0 || !inventory.getInvStack(input1).isEmpty() && !inventory.getInvStack(fuel).isEmpty()) {
			if (burnTime == 0 && canSmelt()) {
				totalBurnTime = burnTime = getItemBurnTime(inventory.getInvStack(fuel));
				if (burnTime > 0) {
					updateInventory = true;
					if (!inventory.getInvStack(fuel).isEmpty()) {
						inventory.shrinkSlot(fuel, 1);
					}
				}
			}
			if (isBurning() && canSmelt()) {
				++progress;
				if (progress == 200) {
					progress = 0;
					smeltItem();
					updateInventory = true;
				}
			} else {
				progress = 0;
			}
		}
		if (isBurning != isBurning()) {
			updateInventory = true;
			updateState();
		}
		
		if (updateInventory) {
			markDirty();
		}
	}

	public boolean hasAllInputs(RebornRecipe recipeType) {
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
		if (inventory.getInvStack(input1).isEmpty() || inventory.getInvStack(input2).isEmpty()) {
			return false;
		}
		ItemStack itemstack = null;
		for (RebornRecipe recipeType : ModRecipes.ALLOY_SMELTER.getRecipes(world)) {
			if (hasAllInputs(recipeType)) {
				itemstack = recipeType.getOutputs().get(0);
				break;
			}
		}

		if (itemstack == null)
			return false;
		if (inventory.getInvStack(output).isEmpty())
			return true;
		if (!inventory.getInvStack(output).isItemEqualIgnoreDamage(itemstack))
			return false;
		int result = inventory.getInvStack(output).getCount() + itemstack.getCount();
		return result <= inventory.getStackLimit() && result <= inventory.getInvStack(output).getMaxCount(); 
	}

	/**
	 * Turn one item from the furnace source stack into the appropriate smelted
	 * item in the furnace result stack
	 */
	public void smeltItem() {
		if (!canSmelt()) {
			return;
		}

		ItemStack outputStack = ItemStack.EMPTY;
		RebornRecipe currentRecipe = null;
		for (RebornRecipe recipeType : ModRecipes.ALLOY_SMELTER.getRecipes(world)) {
			if (hasAllInputs(recipeType)) {
				currentRecipe = recipeType;
				break;
			}
		}
		if (currentRecipe == null) {
			return;
		}
		outputStack = currentRecipe.getOutputs().get(0);
		if (outputStack.isEmpty()) {
			return;
		}
		if (inventory.getInvStack(output).isEmpty()) {
			inventory.setInvStack(output, outputStack.copy());
		} else if (inventory.getInvStack(output).getItem() == outputStack.getItem()) {
			inventory.shrinkSlot(output, -outputStack.getCount());
		}

		for (RebornIngredient ingredient : currentRecipe.getRebornIngredients()) {
			for (int inputSlot = 0; inputSlot < 2; inputSlot++) {
				if (ingredient.test(inventory.getInvStack(inputSlot))) {
					inventory.shrinkSlot(inputSlot, ingredient.getCount());
					break;
				}
			}
		}	
	}

	/**
	 * Furnace isBurning
	 * @return Boolean True if furnace is burning
	 */
	public boolean isBurning() {
		return burnTime > 0;
	}

	public int getBurnTimeRemainingScaled(int scale) {
		if (totalBurnTime == 0) {
			totalBurnTime = 200;
		}

		return burnTime * scale / totalBurnTime;
	}

	public int getCookProgressScaled(int scale) {
		return progress * scale / 200;
	}
	
	public void updateState() {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof BlockMachineBase) {
			BlockMachineBase blockMachineBase = (BlockMachineBase) state.getBlock();
			if (state.get(BlockMachineBase.ACTIVE) != burnTime > 0)
				blockMachineBase.setActive(burnTime > 0, world, pos);
		}
	}

	@Override
	public Direction getFacing() {
		return getFacingEnum();
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return TRContent.Machine.IRON_ALLOY_FURNACE.getStack();
	}

	@Override
	public RebornInventory<IronAlloyFurnaceBlockEntity> getInventory() {
		return inventory;
	}

	public int getBurnTime() {
		return burnTime;
	}

	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}

	public int getTotalBurnTime() {
		return totalBurnTime;
	}

	public void setTotalBurnTime(int currentItemBurnTime) {
		this.totalBurnTime = currentItemBurnTime;
	}

	public int getCookTime() {
		return progress;
	}

	public void setCookTime(int cookTime) {
		this.progress = cookTime;
	}

	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("alloyfurnace").player(player.inventory).inventory().hotbar()
			.addInventory().blockEntity(this)
			.slot(0, 47, 17)
			.slot(1, 65, 17)
			.outputSlot(2, 116, 35).fuelSlot(3, 56, 53).syncIntegerValue(this::getBurnTime, this::setBurnTime)
			.syncIntegerValue(this::getCookTime, this::setCookTime)
			.syncIntegerValue(this::getTotalBurnTime, this::setTotalBurnTime).addInventory().create(this, syncID);
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}
