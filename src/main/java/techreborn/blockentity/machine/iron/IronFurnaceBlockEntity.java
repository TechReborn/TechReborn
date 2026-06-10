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

import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
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
	private RecipeHolder<SmeltingRecipe> lastRecipe = null;
	private int recipeCookingTime = 200;

	public IronFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.IRON_FURNACE, pos, state, FUEL_SLOT, TRContent.Machine.IRON_FURNACE.block);
		this.inventory = new RebornInventory<>(3, "IronFurnaceBlockEntity", 64, this);
	}

	public void handleGuiInputFromClient(Player playerIn) {
		if (playerIn instanceof ServerPlayer player) {
			int totalExperience = (int) experience;
			while (totalExperience > 0) {
				int expToDrop = ExperienceOrb.getExperienceValue(totalExperience);
				totalExperience -= expToDrop;
				player.level().addFreshEntity(new ExperienceOrb(player.level(), player.getX(), player.getY() + 0.5D, player.getZ() + 0.5D, expToDrop));
			}
		}
		experience = 0;
	}

	@Nullable
	private RecipeHolder<SmeltingRecipe> refreshRecipe(ItemStack stack) {
		if (level == null) return lastRecipe;
		// Check the previous recipe to see if it still applies to the current inv, saves rechecking the whole recipe list
		if (lastRecipe != null && lastRecipe.value().matches(new SingleRecipeInput(stack), level)) {
			return lastRecipe;
		} else {
			MinecraftServer server = level.getServer();
			if (server == null) return lastRecipe;

			// If the previous recipe does not apply anymore, reset the progress
			progress = 0;
			RecipeHolder<SmeltingRecipe> matchingRecipe = server.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level).orElse(null);
			if (matchingRecipe != null) {
				lastRecipe = matchingRecipe;
				recipeCookingTime = matchingRecipe.value().cookingTime();
			} else {
				// default value for vanilla smelting recipes is 200
				recipeCookingTime = 200;
			}
		}

		return lastRecipe;
	}


	private ItemStack getResultFor(ItemStack stack) {
		if (stack.isEmpty()) {
			// Fast fail if there is no input, no point checking the recipes if the machine is empty
			return ItemStack.EMPTY;
		}
		if (previousStack.is(stack.getItem()) && !previousValid){
			return ItemStack.EMPTY;
		}

		RecipeHolder<SmeltingRecipe> matchingRecipe = refreshRecipe(stack);

		if (matchingRecipe != null) {
			return matchingRecipe.value().assemble(new SingleRecipeInput(stack)).copy();
		}

		return ItemStack.EMPTY;
	}

	private float getExperienceFor() {
		if (level == null) return 0F;
		MinecraftServer server = level.getServer();
		if (server == null) return 0F;
		Optional<SmeltingRecipe> recipe = server.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(inventory.getItem(0)), level).map(RecipeHolder::value);
		return recipe.map(AbstractCookingRecipe::experience).orElse(0F);
	}

	// AbstractIronMachineBlockEntity
	@Override
	protected void smelt() {
		if (!canSmelt()) {
			return;
		}
		ItemStack inputStack = inventory.getItem(INPUT_SLOT);
		ItemStack resultStack = getResultFor(inputStack);

		if (inventory.getItem(OUTPUT_SLOT).isEmpty()) {
			inventory.setItem(OUTPUT_SLOT, resultStack.copy());
		} else if (inventory.getItem(OUTPUT_SLOT).is(resultStack.getItem())) {
			inventory.getItem(OUTPUT_SLOT).grow(resultStack.getCount());
		}
		experience += getExperienceFor();
		if (inputStack.getCount() > 1) {
			inventory.shrinkSlot(INPUT_SLOT, 1);
		} else {
			inventory.setItem(INPUT_SLOT, ItemStack.EMPTY);
		}
	}

	@Override
	protected boolean canSmelt() {
		ItemStack inputStack = inventory.getItem(INPUT_SLOT);
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
		ItemStack outputSlotStack = inventory.getItem(OUTPUT_SLOT);
		if (outputSlotStack.isEmpty())
			return true;
		if (!outputSlotStack.is(outputStack.getItem()))
			return false;
		int result = outputSlotStack.getCount() + outputStack.getCount();
		return result <= inventory.getStackLimit() && result <= outputStack.getMaxStackSize();
	}

	@Override
	protected int cookingTime() {
		return (int) (recipeCookingTime / TechRebornConfig.cookingScale.get());
	}

	@Override
	public boolean isStackValid(int slotID, ItemStack stack) {
		return !getResultFor(stack).isEmpty();
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		experience = view.getFloatOr("Experience", 0);
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		view.putFloat("Experience", experience);
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

	public int getRecipeCookingTime() {
		return recipeCookingTime;
	}

	public void setRecipeCookingTime(int recipeCookingTime) {
		this.recipeCookingTime = recipeCookingTime;
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final Player player) {
		return new ScreenHandlerBuilder("ironfurnace").player(player.getInventory()).inventory().hotbar()
				.addInventory().blockEntity(this)
				.fuelSlot(2, 56, 53).slot(0, 56, 17).outputSlot(1, 116, 35)
				.sync(ByteBufCodecs.INT, this::getBurnTime, this::setBurnTime)
				.sync(ByteBufCodecs.INT, this::getProgress, this::setProgress)
				.sync(ByteBufCodecs.INT, this::getTotalBurnTime, this::setTotalBurnTime)
				.sync(ByteBufCodecs.FLOAT, this::getExperience, this::setExperience)
				.sync(ByteBufCodecs.INT, this::getRecipeCookingTime, this::setRecipeCookingTime)
				.addInventory().create(this, syncID);
	}
}
