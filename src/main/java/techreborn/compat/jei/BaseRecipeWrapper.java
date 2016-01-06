package techreborn.compat.jei;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import techreborn.api.recipe.BaseRecipe;

public abstract class BaseRecipeWrapper<T extends BaseRecipe> extends BlankRecipeWrapper {
	protected final T baseRecipe;
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
			itemStackSet.addAll(ores);
		}
		return new ArrayList<>(itemStackSet);
	}

	@Override
	public List<List<ItemStack>> getInputs() {
		return inputs;
	}

	@Override
	public List<ItemStack> getOutputs() {
		return baseRecipe.getOutputs();
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {

	}
}
