package techreborn.compat.jei.rollingMachine;

import net.minecraft.item.crafting.IRecipe;
import techreborn.api.RollingMachineRecipe;

import java.util.ArrayList;
import java.util.List;

public class RollingMachineRecipeMaker {
	private RollingMachineRecipeMaker() {

	}

	public static List<Object> getRecipes() {
		List<Object> recipes = new ArrayList<>();
		for (IRecipe recipe : RollingMachineRecipe.instance.getRecipeList()) {
			RollingMachineRecipeWrapper recipeWrapper = RollingMachineRecipeWrapper.create(recipe);
			if (recipeWrapper != null) {
				recipes.add(recipeWrapper);
			}
		}
		return recipes;
	}
}
