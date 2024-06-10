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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.Optional;

public class IronFurnaceBlockEntity extends AbstractIronMachineBlockEntity implements BuiltScreenHandlerProvider {

	public final static int INPUT_SLOT = 0;
	public final static int OUTPUT_SLOT = 1;
	public final static int FUEL_SLOT = 2;

	public float experience;
	private boolean previousValid = false;
	private ItemStack previousStack = ItemStack.EMPTY;
	private RecipeEntry<SmeltingRecipe> lastRecipe = null;

	public IronFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.IRON_FURNACE, pos, state, FUEL_SLOT, TRContent.Machine.IRON_FURNACE.block);
		this.inventory = new RebornInventory<>(3, "IronFurnaceBlockEntity", 64, this);
	}

	public void handleGuiInputFromClient(PlayerEntity playerIn) {
		if (playerIn instanceof ServerPlayerEntity player) {
			int totalExperience = (int) experience;
			while (totalExperience > 0) {
				int expToDrop = ExperienceOrbEntity.roundToOrbSize(totalExperience);
				totalExperience -= expToDrop;
				player.getWorld().spawnEntity(new ExperienceOrbEntity(player.getWorld(), player.getX(), player.getY() + 0.5D, player.getZ() + 0.5D, expToDrop));
			}
		}
		experience = 0;
	}

	@Nullable
	private RecipeEntry<SmeltingRecipe> refreshRecipe(ItemStack stack) {
		// Check the previous recipe to see if it still applies to the current inv, saves rechecking the whole recipe list
		if (lastRecipe != null && lastRecipe.value().matches(new SingleStackRecipeInput(stack), world)) {
			return lastRecipe;
		} else {
			// If the previous recipe does not apply anymore, reset the progress
			progress = 0;
			RecipeEntry<SmeltingRecipe> matchingRecipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SingleStackRecipeInput(stack), world).orElse(null);
			if (matchingRecipe != null) {
				lastRecipe = matchingRecipe;
			}
		}

		return lastRecipe;
	}


	private ItemStack getResultFor(ItemStack stack) {
		if (stack.isEmpty()) {
			// Fast fail if there is no input, no point checking the recipes if the machine is empty
			return ItemStack.EMPTY;
		}
		if (previousStack.isOf(stack.getItem()) && !previousValid){
			return ItemStack.EMPTY;
		}

		RecipeEntry<SmeltingRecipe> matchingRecipe = refreshRecipe(stack);

		if (matchingRecipe != null) {
			return matchingRecipe.value().getResult(getWorld().getRegistryManager()).copy();
		}

		return ItemStack.EMPTY;
	}

	private float getExperienceFor() {
		Optional<SmeltingRecipe> recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SingleStackRecipeInput(inventory.getStack(0)), world).map(RecipeEntry::value);
		return recipe.map(AbstractCookingRecipe::getExperience).orElse(0F);
	}

	// AbstractIronMachineBlockEntity
	@Override
	protected void smelt() {
		if (!canSmelt()) {
			return;
		}
		ItemStack inputStack = inventory.getStack(INPUT_SLOT);
		ItemStack resultStack = getResultFor(inputStack);

		if (inventory.getStack(OUTPUT_SLOT).isEmpty()) {
			inventory.setStack(OUTPUT_SLOT, resultStack.copy());
		} else if (inventory.getStack(OUTPUT_SLOT).isOf(resultStack.getItem())) {
			inventory.getStack(OUTPUT_SLOT).increment(resultStack.getCount());
		}
		experience += getExperienceFor();
		if (inputStack.getCount() > 1) {
			inventory.shrinkSlot(INPUT_SLOT, 1);
		} else {
			inventory.setStack(INPUT_SLOT, ItemStack.EMPTY);
		}
	}

	@Override
	protected boolean canSmelt() {
		ItemStack inputStack = inventory.getStack(INPUT_SLOT);
		if (inputStack.isEmpty())
			return false;
		if (previousStack != inputStack) {
			previousStack = inputStack;
			previousValid = true;
		}
		ItemStack outputStack = getResultFor(inputStack);
		if (outputStack.isEmpty()) {
			previousValid = false;
			return false;
		}
		else {
			previousValid = true;
		}
		ItemStack outputSlotStack = inventory.getStack(OUTPUT_SLOT);
		if (outputSlotStack.isEmpty())
			return true;
		if (!outputSlotStack.isOf(outputStack.getItem()))
			return false;
		int result = outputSlotStack.getCount() + outputStack.getCount();
		return result <= inventory.getStackLimit() && result <= outputStack.getMaxCount();
	}

	@Override
	protected int cookingTime() {
		// default value for vanilla smelting recipes is 200
		int cookingTime = 200;

		RecipeEntry<SmeltingRecipe> recipe = refreshRecipe(inventory.getStack(INPUT_SLOT));

		if (recipe != null) {
			try {
				cookingTime = recipe.value().getCookingTime();
			} catch (ClassCastException ex) {
				// Intentionally ignored
				System.out.println("Not a smelting recipe!");
			}
		}

		return (int) (cookingTime / TechRebornConfig.cookingScale);
	}

	@Override
	public boolean isStackValid(int slotID, ItemStack stack) {
		return !getResultFor(stack).isEmpty();
	}

	@Override
	public void readNbt(NbtCompound compoundTag, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(compoundTag, registryLookup);
		experience = compoundTag.getFloat("Experience");
	}

	@Override
	public void writeNbt(NbtCompound compoundTag, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(compoundTag, registryLookup);
		compoundTag.putFloat("Experience", experience);
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
		return new int[]{INPUT_SLOT};
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("ironfurnace").player(player.getInventory()).inventory().hotbar()
				.addInventory().blockEntity(this)
				.fuelSlot(2, 56, 53).slot(0, 56, 17).outputSlot(1, 116, 35)
				.sync(PacketCodecs.INTEGER, this::getBurnTime, this::setBurnTime)
				.sync(PacketCodecs.INTEGER, this::getProgress, this::setProgress)
				.sync(PacketCodecs.INTEGER, this::getTotalBurnTime, this::setTotalBurnTime)
				.sync(PacketCodecs.FLOAT, this::getExperience, this::setExperience)
				.addInventory().create(this, syncID);
	}
}
