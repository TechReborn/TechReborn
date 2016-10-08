package techreborn.compat.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.api.recipe.BaseRecipe;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class BaseRecipeWrapper<T extends BaseRecipe> extends BlankRecipeWrapper {
	protected final T baseRecipe;
	@Nonnull
	private final List<List<ItemStack>> inputs;

	public BaseRecipeWrapper(T baseRecipe) {
		this.baseRecipe = baseRecipe;

		inputs = new ArrayList<>();
		for (ItemStack input : baseRecipe.getInputs()) {
			if (baseRecipe.useOreDic()) {
				List<ItemStack> oreDictInputs = expandOreDict(input);
				inputs.add(oreDictInputs);
			} else {
				inputs.add(Collections.singletonList(input));
			}
		}
	}

	private static List<ItemStack> expandOreDict(ItemStack itemStack) {
		int[] oreIds = OreDictionary.getOreIDs(itemStack);
		if (oreIds.length == 0) {
			return Collections.singletonList(itemStack);
		}

		Set<ItemStack> itemStackSet = new HashSet<>();
		for (int oreId : oreIds) {
			String oreName = OreDictionary.getOreName(oreId);
			List<ItemStack> ores = OreDictionary.getOres(oreName);
			for (ItemStack ore : ores) {
				if (ore.stackSize != itemStack.stackSize) {
					ItemStack oreCopy = ore.copy();
					oreCopy.stackSize = itemStack.stackSize;
					itemStackSet.add(oreCopy);
				} else {
					itemStackSet.add(ore);
				}
			}
		}
		return new ArrayList<>(itemStackSet);
	}

	@Override
	public void getIngredients(
		@Nonnull
			IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutputs(ItemStack.class, baseRecipe.getOutputs());
	}

	@Nonnull
	@Override
	public List<List<ItemStack>> getInputs() {
		return inputs;
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		return baseRecipe.getOutputs();
	}
}
