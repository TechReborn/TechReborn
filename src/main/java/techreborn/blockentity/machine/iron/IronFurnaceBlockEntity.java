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

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.network.ServerPlayerEntity;
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

	public RebornInventory<IronFurnaceBlockEntity> inventory = new RebornInventory<>(3, "IronFurnaceBlockEntity", 64, this, getInvetoryAccess());
	public int burnTime;
	public int totalBurnTime;
	public int progress;
	public int fuelScale = 160;
	int inputSlot = 0;
	int outputSlot = 1;
	int fuelSlot = 2;
	boolean active = false;
	public float experience;

	public IronFurnaceBlockEntity() {
		super(TRBlockEntities.IRON_FURNACE);
	}
	
	public static IInventoryAccess<IronFurnaceBlockEntity> getInvetoryAccess(){
		return (slotID, stack, face, direction, blockEntity) -> {
			if(direction == IInventoryAccess.AccessDirection.INSERT){
				boolean isFuel = AbstractFurnaceBlockEntity.canUseAsFuel(stack);
				if(isFuel){
					ItemStack fuelSlotStack = blockEntity.inventory.getInvStack(blockEntity.fuelSlot);
					if(fuelSlotStack.isEmpty() || ItemUtils.isItemEqual(stack, fuelSlotStack, true, true) && fuelSlotStack.getMaxCount() != fuelSlotStack.getCount()){
						return slotID == blockEntity.fuelSlot;
					}
				}
				return slotID != blockEntity.outputSlot;
			}
			return true;
		};
	}

	public int gaugeProgressScaled(int scale) {
		return progress * scale / fuelScale;
	}

	public int gaugeFuelScaled(int scale) {
		if (totalBurnTime == 0) {
			totalBurnTime = burnTime;
			if (totalBurnTime == 0) {
				totalBurnTime = fuelScale;
			}
		}
		return burnTime * scale / totalBurnTime;
	}
	
	private void cookItems() {
		if (!canSmelt()) {
			return;
		}
		ItemStack inputStack = inventory.getInvStack(inputSlot);
		ItemStack outputStack = getResultFor(inputStack);
		float recipeExp = getExperienceFor(inputStack);
		
		if (inventory.getInvStack(outputSlot).isEmpty()) {
			inventory.setInvStack(outputSlot, outputStack.copy());
		} else if (inventory.getInvStack(outputSlot).isItemEqualIgnoreDamage(outputStack)) {
			inventory.getInvStack(outputSlot).increment(outputStack.getCount());
		}
		if (inputStack.getCount() > 1) {
			inventory.shrinkSlot(inputSlot, 1);
		} else {
			inventory.setInvStack(inputSlot, ItemStack.EMPTY);
		}
		experience += recipeExp;
	}

	private boolean canSmelt() {
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

	public boolean isBurning() {
		return burnTime > 0;
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
	
	public void handleGuiInputFromClient(PlayerEntity playerIn) {
		if (playerIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) playerIn;
			int totalExperience = (int) experience;
			while (totalExperience > 0) {
				int expToDrop = ExperienceOrbEntity.roundToOrbSize(totalExperience);
				totalExperience -= expToDrop;
				player.world.spawnEntity(new ExperienceOrbEntity(player.world, player.x, player.y + 0.5D, player.z + 0.5D, expToDrop));
			}
		}
		experience = 0;
	}
	
	private void updateState() {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof BlockMachineBase) {
			BlockMachineBase blockMachineBase = (BlockMachineBase) state.getBlock();
			if (state.get(BlockMachineBase.ACTIVE) != burnTime > 0)
				blockMachineBase.setActive(burnTime > 0, world, pos);
		}
	}
		
	// MachineBaseBlockEntity
	@Override
	public void fromTag(CompoundTag compoundTag) {
		super.fromTag(compoundTag);
		experience = compoundTag.getFloat("Experience");
		burnTime = compoundTag.getInt("BurnTime");
		totalBurnTime = compoundTag.getInt("TotalBurnTime");
		progress = compoundTag.getInt("Progress");
	}
	
	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		 super.toTag(compoundTag);
		 compoundTag.putFloat("Experience", experience);
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
		
		if (burnTime <= 0 && canSmelt()) {
			burnTime = totalBurnTime = (int) (AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(inventory.getInvStack(fuelSlot).getItem(), 0) * 1.25);
			if (burnTime > 0) {
				// Fuel slot
				ItemStack fuelStack = inventory.getInvStack(fuelSlot);
				if (fuelStack.getItem().hasRecipeRemainder()) {
					inventory.setInvStack(fuelSlot, new ItemStack(fuelStack.getItem().getRecipeRemainder()));
				} else if (fuelStack.getCount() > 1) {
					inventory.shrinkSlot(fuelSlot, 1);
				} else if (fuelStack.getCount() == 1) {
					inventory.setInvStack(fuelSlot, ItemStack.EMPTY);
				}
				updateInventory = true;
			}
		}
		if (isBurning() && canSmelt()) {
			progress++;
			if (progress >= fuelScale) {
				progress = 0;
				cookItems();
				updateInventory = true;
			}
		} else {
			progress = 0;
		}
		if (isBurning != isBurning()) {
			updateInventory = true;
			updateState();
		}
		if (updateInventory) {
			markDirty();
		}
	}
	
	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// InventoryProvider
	@Override
	public RebornInventory<IronFurnaceBlockEntity> getInventory() {
		return this.inventory;
	}

	// IContainerProvider
	public int getBurnTime() {
		return this.burnTime;
	}

	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}

	public int getTotalBurnTime() {
		return this.totalBurnTime;
	}

	public void setTotalBurnTime(int totalBurnTime) {
		this.totalBurnTime = totalBurnTime;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	public float getExperience() {
		return experience;
	}
	
	public void setExperience(float experience) {
		this.experience = experience;
	}

	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("ironfurnace").player(player.inventory).inventory().hotbar()
				.addInventory().blockEntity(this).fuelSlot(2, 56, 53).slot(0, 56, 17).outputSlot(1, 116, 35)
				.syncIntegerValue(this::getBurnTime, this::setBurnTime)
				.syncIntegerValue(this::getProgress, this::setProgress)
				.syncIntegerValue(this::getTotalBurnTime, this::setTotalBurnTime)
				.sync(this::getExperience, this::setExperience)
				.addInventory().create(this, syncID);
	}
}
