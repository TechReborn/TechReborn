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
import net.minecraft.recipe.RecipeType;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.IInventoryAccess;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.ItemUtils;
import techreborn.init.TRBlockEntities;
import techreborn.utils.RecipeUtils;

public class IronFurnaceBlockEntity extends MachineBaseBlockEntity
		implements InventoryProvider, IContainerProvider {

	public int tickTime;
	public RebornInventory<IronFurnaceBlockEntity> inventory = new RebornInventory<>(3, "IronFurnaceBlockEntity", 64, this, getInvetoryAccess());
	public int fuel;
	public int fuelGague;
	public int progress;
	public int fuelScale = 160;
	int input1 = 0;
	int output = 1;
	int fuelslot = 2;
	boolean active = false;

	public IronFurnaceBlockEntity() {
		super(TRBlockEntities.IRON_FURNACE);
	}

	public int gaugeProgressScaled(final int scale) {
		return this.progress * scale / this.fuelScale;
	}

	public int gaugeFuelScaled(final int scale) {
		if (this.fuelGague == 0) {
			this.fuelGague = this.fuel;
			if (this.fuelGague == 0) {
				this.fuelGague = this.fuelScale;
			}
		}
		return this.fuel * scale / this.fuelGague;
	}

	@Override
	public void tick() {
		super.tick();
		if(world.isClient){
			return;
		}
		final boolean burning = this.isBurning();
		boolean updateInventory = false;
		if (this.fuel > 0) {
			fuel--;
		}
		updateState();
		if (this.fuel <= 0 && this.canSmelt()) {
			this.fuel = this.fuelGague = (int) (AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(inventory.getInvStack(this.fuelslot).getItem(), 0) * 1.25);
			if (this.fuel > 0) {
				// Fuel slot
				ItemStack fuelStack = inventory.getInvStack(this.fuelslot);
				if (fuelStack.getItem().hasRecipeRemainder()) {
					inventory.setInvStack(this.fuelslot, new ItemStack(fuelStack.getItem().getRecipeRemainder()));
				} else if (fuelStack.getCount() > 1) {
					inventory.shrinkSlot(this.fuelslot, 1);
				} else if (fuelStack.getCount() == 1) {
					inventory.setInvStack(this.fuelslot, ItemStack.EMPTY);
				}
				updateInventory = true;
			}
		}
		if (this.isBurning() && this.canSmelt()) {
			this.progress++;
			if (this.progress >= this.fuelScale) {
				this.progress = 0;
				this.cookItems();
				updateInventory = true;
			}
		} else {
			this.progress = 0;
		}
		if (burning != this.isBurning()) {
			updateInventory = true;
		}
		if (updateInventory) {
			this.markDirty();
		}
	}

	public void cookItems() {
		if (this.canSmelt()) {
			final ItemStack itemstack = RecipeUtils.getMatchingRecipes(world, RecipeType.SMELTING, inventory.getInvStack(this.input1));

			if (inventory.getInvStack(this.output).isEmpty()) {
				inventory.setInvStack(this.output, itemstack.copy());
			} else if (inventory.getInvStack(this.output).isItemEqualIgnoreDamage(itemstack)) {
				inventory.getInvStack(this.output).increment(itemstack.getCount());
			}
			if (inventory.getInvStack(this.input1).getCount() > 1) {
				inventory.shrinkSlot(this.input1, 1);
			} else {
				inventory.setInvStack(this.input1, ItemStack.EMPTY);
			}
		}
	}

	public boolean canSmelt() {
		if (inventory.getInvStack(this.input1).isEmpty())
			return false;
		final ItemStack itemstack = RecipeUtils.getMatchingRecipes(world, RecipeType.SMELTING, inventory.getInvStack(this.input1));
		if (itemstack.isEmpty())
			return false;
		if (inventory.getInvStack(this.output).isEmpty())
			return true;
		if (!inventory.getInvStack(this.output).isItemEqualIgnoreDamage(itemstack))
			return false;
		final int result = inventory.getInvStack(this.output).getCount() + itemstack.getCount();
		return result <= inventory.getStackLimit() && result <= itemstack.getMaxCount();
	}

	public boolean isBurning() {
		return this.fuel > 0;
	}

	public ItemStack getResultFor(final ItemStack stack) {
		final ItemStack result = RecipeUtils.getMatchingRecipes(world, RecipeType.SMELTING, stack);
		if (!result.isEmpty()) {
			return result.copy();
		}
		return ItemStack.EMPTY;
	}

	public void updateState() {
		BlockState state = world.getBlockState(this.pos);
		if (state.getBlock() instanceof BlockMachineBase) {
			BlockMachineBase blockMachineBase = (BlockMachineBase) state.getBlock();
			if (state.get(BlockMachineBase.ACTIVE) != this.fuel > 0)
				blockMachineBase.setActive(this.fuel > 0, this.world, this.pos);
		}
	}

	@Override
	public RebornInventory<IronFurnaceBlockEntity> getInventory() {
		return this.inventory;
	}

	public static IInventoryAccess<IronFurnaceBlockEntity> getInvetoryAccess(){
		return (slotID, stack, face, direction, blockEntity) -> {
			if(direction == IInventoryAccess.AccessDirection.INSERT){
				boolean isFuel = AbstractFurnaceBlockEntity.canUseAsFuel(stack);
				if(isFuel){
					ItemStack fuelSlotStack = blockEntity.inventory.getInvStack(blockEntity.fuelslot);
					if(fuelSlotStack.isEmpty() || ItemUtils.isItemEqual(stack, fuelSlotStack, true, true) && fuelSlotStack.getMaxCount() != fuelSlotStack.getCount()){
						return slotID == blockEntity.fuelslot;
					}
				}
				return slotID != blockEntity.output;
			}
			return true;
		};
	}
	
	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	public int getBurnTime() {
		return this.fuel;
	}

	public void setBurnTime(final int burnTime) {
		this.fuel = burnTime;
	}

	public int getTotalBurnTime() {
		return this.fuelGague;
	}

	public void setTotalBurnTime(final int totalBurnTime) {
		this.fuelGague = totalBurnTime;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("ironfurnace").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().blockEntity(this).fuelSlot(2, 56, 53).slot(0, 56, 17).outputSlot(1, 116, 35)
				.syncIntegerValue(this::getBurnTime, this::setBurnTime)
				.syncIntegerValue(this::getProgress, this::setProgress)
				.syncIntegerValue(this::getTotalBurnTime, this::setTotalBurnTime).addInventory().create(this, syncID);
	}
}
