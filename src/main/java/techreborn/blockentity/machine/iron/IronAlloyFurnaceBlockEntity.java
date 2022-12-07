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

package techreborn.blockentity.machine.iron;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.List;

public class IronAlloyFurnaceBlockEntity extends AbstractIronMachineBlockEntity implements BuiltScreenHandlerProvider {

	public final static int INPUT_SLOT_1 = 0;
	public final static int INPUT_SLOT_2 = 1;
	public final static int OUTPUT_SLOT = 2;
	public final static int FUEL_SLOT = 3;

	public IronAlloyFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.IRON_ALLOY_FURNACE, pos, state, FUEL_SLOT, TRContent.Machine.IRON_ALLOY_FURNACE.block);
		this.inventory = new RebornInventory<>(4, "IronAlloyFurnaceBlockEntity", 64, this);
	}

	public boolean hasAllInputs(RebornRecipe recipeType) {
		if (recipeType == null) {
			return false;
		}
		for (RebornIngredient ingredient : recipeType.getRebornIngredients()) {
			boolean hasItem = false;
			for (int inputSlot = 0; inputSlot < 2; inputSlot++) {
				if (ingredient.test(inventory.getStack(inputSlot))) {
					hasItem = true;
				}
			}
			if (!hasItem)
				return false;
		}
		return true;
	}

	private RebornRecipe getRecipe() {
		for (RebornRecipe recipeType : ModRecipes.ALLOY_SMELTER.getRecipes(world)) {
			if (hasAllInputs(recipeType)) {
				return recipeType;
			}
		}

		return null;
	}

	@Override
	protected boolean canSmelt() {
		if (inventory.getStack(INPUT_SLOT_1).isEmpty() || inventory.getStack(INPUT_SLOT_2).isEmpty()) {
			return false;
		}
		ItemStack itemstack = null;
		for (RebornRecipe recipeType : ModRecipes.ALLOY_SMELTER.getRecipes(world)) {
			if (hasAllInputs(recipeType)) {
				List<ItemStack> outputs = recipeType.getOutputs();

				if(outputs.isEmpty()){
					continue;
				}

				itemstack = outputs.get(0);

				break;
			}
		}

		if (itemstack == null)
			return false;
		if (inventory.getStack(OUTPUT_SLOT).isEmpty())
			return true;
		if (!inventory.getStack(OUTPUT_SLOT).isItemEqual(itemstack))
			return false;
		int result = inventory.getStack(OUTPUT_SLOT).getCount() + itemstack.getCount();
		return result <= inventory.getStackLimit() && result <= inventory.getStack(OUTPUT_SLOT).getMaxCount();
	}

	@Override
	protected void smelt() {
		if (!canSmelt()) {
			return;
		}

		RebornRecipe currentRecipe = getRecipe();
		if (currentRecipe == null) {
			return;
		}

		ItemStack outputStack = currentRecipe.getOutputs().get(0);
		if (outputStack.isEmpty()) {
			return;
		}
		if (inventory.getStack(OUTPUT_SLOT).isEmpty()) {
			inventory.setStack(OUTPUT_SLOT, outputStack.copy());
		} else if (inventory.getStack(OUTPUT_SLOT).getItem() == outputStack.getItem()) {
			inventory.shrinkSlot(OUTPUT_SLOT, -outputStack.getCount());
		}

		for (RebornIngredient ingredient : currentRecipe.getRebornIngredients()) {
			for (int inputSlot = 0; inputSlot < 2; inputSlot++) {
				if (ingredient.test(inventory.getStack(inputSlot))) {
					inventory.shrinkSlot(inputSlot, ingredient.getCount());
					break;
				}
			}
		}
	}

	@Override
	protected int cookingTime() {
		// default value for vanilla smelting recipes is 200
		int cookingTime = 200;

		RebornRecipe recipe = getRecipe();
		if (recipe != null) {
			cookingTime = recipe.getTime();
		}

		return (int) (cookingTime / TechRebornConfig.cookingScale);
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("alloyfurnace").player(player.getInventory()).inventory().hotbar()
				.addInventory().blockEntity(this)
				.slot(0, 47, 17)
				.slot(1, 65, 17)
				.outputSlot(2, 116, 35).fuelSlot(3, 56, 53)
				.sync(this::getBurnTime, this::setBurnTime)
				.sync(this::getProgress, this::setProgress)
				.sync(this::getTotalBurnTime, this::setTotalBurnTime)
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
		return new int[]{INPUT_SLOT_1, INPUT_SLOT_2};
	}
}
