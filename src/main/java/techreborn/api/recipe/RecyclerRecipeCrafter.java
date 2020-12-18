package techreborn.api.recipe;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;

import java.util.List;
import java.util.Objects;

public class RecyclerRecipeCrafter extends RecipeCrafter {

	public RecyclerRecipeCrafter(BlockEntity blockEntity, RebornInventory<?> inventory, int[] inputSlots, int[] outputSlots) {
		super(ModRecipes.RECYCLER, blockEntity, 1, 1, inventory, inputSlots, outputSlots);
	}

	@Override
	public void updateCurrentRecipe() {
		currentTickTime = 0;
		List<RebornRecipe> recipeList = ModRecipes.RECYCLER.getRecipes(blockEntity.getWorld());
		if (recipeList.isEmpty() || !hasAllInputs()) {
			setCurrentRecipe(null);
			currentNeededTicks = 0;
			setIsActive();
			return;
		}
		setCurrentRecipe(recipeList.get(0));
		currentNeededTicks = Math.max((int) (currentRecipe.getTime() * (1.0 - getSpeedMultiplier())), 1);
		setIsActive();
	}

	@Override
	public boolean hasAllInputs() {
		boolean hasItem = false;
		// Check if we have at least something in input slots. Foreach input slot in case of several input slots
		for (int inputSlot : inputSlots) {
			if (inventory.getStack(inputSlot).isEmpty()) continue;
			hasItem = true;
			break;
		}
		return hasItem;
	}

	@Override
	public void useAllInputs() {
		if (currentRecipe == null) {
			return;
		}
		// Uses input. Foreach input slot in case of several input slots
		for (int inputSlot : inputSlots) {
			if (inventory.getStack(inputSlot).isEmpty()) continue;
			inventory.shrinkSlot(inputSlot, 1);
			break;
		}
	}

	@Override
	public void fitStack(ItemStack stack, int slot) {
		// Dirty hack for chance based crafting
		final int randomChance = Objects.requireNonNull(blockEntity.getWorld()).random.nextInt(TechRebornConfig.recyclerChance);
		if (randomChance == 1) {
			super.fitStack(stack, slot);
		}
	}
}
