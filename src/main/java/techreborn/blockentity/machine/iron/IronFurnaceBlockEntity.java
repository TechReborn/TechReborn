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

import java.util.Optional;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.network.ServerPlayerEntity;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.utils.RecipeUtils;

public class IronFurnaceBlockEntity extends AbstractIronMachineBlockEntity implements IContainerProvider {

	int inputSlot = 0;
	int outputSlot = 1;
	public float experience;
	
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
		ItemStack result = RecipeUtils.getMatchingRecipes(world, RecipeType.SMELTING, stack);
		if (!result.isEmpty()) {
			return result.copy();
		}
		return ItemStack.EMPTY;
	}

	private float getExperienceFor(ItemStack stack) {
		Optional<SmeltingRecipe> recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, this, world);
		if (recipe.isPresent()) {
			return recipe.get().getExperience();
		}
		return 0;
	}
	
	// AbstractIronMachineBlockEntity
	@Override
	protected void smelt() {
		if (!canSmelt()) {
			return;
		}
		ItemStack inputStack = inventory.getInvStack(inputSlot);
		ItemStack resultStack = getResultFor(inputStack);
		
		if (inventory.getInvStack(outputSlot).isEmpty()) {
			inventory.setInvStack(outputSlot, resultStack.copy());
		} else if (inventory.getInvStack(outputSlot).isItemEqualIgnoreDamage(resultStack)) {
			inventory.getInvStack(outputSlot).increment(resultStack.getCount());
		}
		experience += getExperienceFor(inputStack);
		if (inputStack.getCount() > 1) {
			inventory.shrinkSlot(inputSlot, 1);
		} else {
			inventory.setInvStack(inputSlot, ItemStack.EMPTY);
		}
	}
	
	@Override
	protected boolean canSmelt() {
		if (inventory.getInvStack(inputSlot).isEmpty()) {
			return false;
		}		
		ItemStack outputStack = getResultFor(inventory.getInvStack(inputSlot));
		if (outputStack.isEmpty())
			return false;
		if (inventory.getInvStack(outputSlot).isEmpty())
			return true;
		if (!inventory.getInvStack(outputSlot).isItemEqualIgnoreDamage(outputStack))
			return false;
		int result = inventory.getInvStack(outputSlot).getCount() + outputStack.getCount();
		return result <= inventory.getStackLimit() && result <= outputStack.getMaxCount();
	}
	
	@Override
	public void fromTag(CompoundTag compoundTag) {
		super.fromTag(compoundTag);
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
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("ironfurnace").player(player.inventory).inventory().hotbar()
				.addInventory().blockEntity(this)
				.fuelSlot(2, 56, 53).slot(0, 56, 17).outputSlot(1, 116, 35)
				.syncIntegerValue(this::getBurnTime, this::setBurnTime)
				.syncIntegerValue(this::getProgress, this::setProgress)
				.syncIntegerValue(this::getTotalBurnTime, this::setTotalBurnTime)
				.sync(this::getExperience, this::setExperience)
				.addInventory().create(this, syncID);
	}
}
