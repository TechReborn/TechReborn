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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.util.RebornInventory;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class IronAlloyFurnaceBlockEntity extends AbstractIronMachineBlockEntity	implements  IContainerProvider {

	int input1 = 0;
	int input2 = 1;
	int output = 2;

	public IronAlloyFurnaceBlockEntity() {
		super(TRBlockEntities.IRON_ALLOY_FURNACE, 3, TRContent.Machine.IRON_ALLOY_FURNACE.block);
		this.inventory = new RebornInventory<>(4, "IronAlloyFurnaceBlockEntity", 64, this);
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

	@Override
	protected boolean canSmelt() {
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

	@Override
	protected void smelt() {
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

	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("alloyfurnace").player(player.inventory).inventory().hotbar()
			.addInventory().blockEntity(this)
			.slot(0, 47, 17)
			.slot(1, 65, 17)
			.outputSlot(2, 116, 35).fuelSlot(3, 56, 53)
			.syncIntegerValue(this::getBurnTime, this::setBurnTime)
			.syncIntegerValue(this::getProgress, this::setProgress)
			.syncIntegerValue(this::getTotalBurnTime, this::setTotalBurnTime)
			.addInventory().create(this, syncID);
	}

	@Override
	public boolean isStackValid(int slotID, ItemStack stack) {
		return ModRecipes.ALLOY_SMELTER.getRecipes(world).stream()
			.anyMatch(rebornRecipe -> rebornRecipe.getRebornIngredients().stream()
				.anyMatch(rebornIngredient -> rebornIngredient.test(stack))
			);
	}

	@Override
	public int[] getInputSlots() {
		return new int[]{input1, input2};
	}
}
