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
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.network.ServerPlayerEntity;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.utils.RecipeUtils;

import java.util.Optional;

public class IronFurnaceBlockEntity extends AbstractIronMachineBlockEntity implements BuiltScreenHandlerProvider {

	int inputSlot = 0;
	int outputSlot = 1;
	public float experience;

	private Recipe<?> lastRecipe = null;

	public IronFurnaceBlockEntity() {
		super(TRBlockEntities.IRON_FURNACE, 2, TRContent.Machine.IRON_FURNACE.block);
		this.inventory = new RebornInventory<>(3, "IronFurnaceBlockEntity", 64, this);
	}

	public void handleGuiInputFromClient(PlayerEntity playerIn) {
		if (playerIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) playerIn;
			int totalExperience = (int) experience;
			while (totalExperience > 0) {
				int expToDrop = ExperienceOrbEntity.roundToOrbSize(totalExperience);
				totalExperience -= expToDrop;
				player.world.spawnEntity(new ExperienceOrbEntity(player.world, player.getX(), player.getY() + 0.5D, player.getZ() + 0.5D, expToDrop));
			}
		}
		experience = 0;
	}

	private ItemStack getResultFor(ItemStack stack) {
		if (stack.isEmpty()) {
			// Fast fail if there is no input, no point checking the recipes if the machine is empty
			return ItemStack.EMPTY;
		}

		// Check the previous recipe to see if it still applies to the current inv, saves rechecking the whole recipe list
		if (lastRecipe != null && RecipeUtils.matchesSingleInput(lastRecipe, stack)) {
			return lastRecipe.getOutput();
		}

		Recipe<?> matchingRecipe = RecipeUtils.getMatchingRecipe(world, RecipeType.SMELTING, stack).orElse(null);

		if (matchingRecipe != null) {
			lastRecipe = matchingRecipe;
			return matchingRecipe.getOutput().copy();
		}

		return ItemStack.EMPTY;
	}

	private float getExperienceFor(ItemStack stack) {
		Optional<SmeltingRecipe> recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, this, world);
		return recipe.map(AbstractCookingRecipe::getExperience).orElse(0F);
	}

	// AbstractIronMachineBlockEntity
	@Override
	protected void smelt() {
		if (!canSmelt()) {
			return;
		}
		ItemStack inputStack = inventory.getStack(inputSlot);
		ItemStack resultStack = getResultFor(inputStack);

		if (inventory.getStack(outputSlot).isEmpty()) {
			inventory.setStack(outputSlot, resultStack.copy());
		} else if (inventory.getStack(outputSlot).isItemEqualIgnoreDamage(resultStack)) {
			inventory.getStack(outputSlot).increment(resultStack.getCount());
		}
		experience += getExperienceFor(inputStack);
		if (inputStack.getCount() > 1) {
			inventory.shrinkSlot(inputSlot, 1);
		} else {
			inventory.setStack(inputSlot, ItemStack.EMPTY);
		}
	}

	@Override
	protected boolean canSmelt() {
		ItemStack inputStack = inventory.getStack(inputSlot);
		if (inputStack.isEmpty())
			return false;

		ItemStack outputStack = getResultFor(inputStack);
		if (outputStack.isEmpty())
			return false;

		ItemStack outputSlotStack = inventory.getStack(outputSlot);
		if (outputSlotStack.isEmpty())
			return true;
		if (!outputSlotStack.isItemEqualIgnoreDamage(outputStack))
			return false;
		int result = outputSlotStack.getCount() + outputStack.getCount();
		return result <= inventory.getStackLimit() && result <= outputStack.getMaxCount();
	}

	@Override
	public boolean isStackValid(int slotID, ItemStack stack) {
		return !getResultFor(stack).isEmpty();
	}

	@Override
	public void fromTag(BlockState blockState, CompoundTag compoundTag) {
		super.fromTag(blockState, compoundTag);
		experience = compoundTag.getFloat("Experience");
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		super.toTag(compoundTag);
		compoundTag.putFloat("Experience", experience);
		return compoundTag;
	}

	// IContainerProvider
	public float getExperience() {
		return experience;
	}

	public void setExperience(float experience) {
		this.experience = experience;
	}

	@Override
	public int[] getInputSlots() {
		return new int[]{inputSlot};
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("ironfurnace").player(player.inventory).inventory().hotbar()
				.addInventory().blockEntity(this)
				.fuelSlot(2, 56, 53).slot(0, 56, 17).outputSlot(1, 116, 35)
				.sync(this::getBurnTime, this::setBurnTime)
				.sync(this::getProgress, this::setProgress)
				.sync(this::getTotalBurnTime, this::setTotalBurnTime)
				.sync(this::getExperience, this::setExperience)
				.addInventory().create(this, syncID);
	}
}
