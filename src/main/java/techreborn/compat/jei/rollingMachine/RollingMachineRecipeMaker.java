package techreborn.compat.jei.rollingMachine;

import mezz.jei.api.IJeiHelpers;
import net.minecraft.item.crafting.IRecipe;
import techreborn.api.RollingMachineRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RollingMachineRecipeMaker {
	private RollingMachineRecipeMaker() {

	}

	public static List<Object> getRecipes(
		@Nonnull
			IJeiHelpers jeiHelpers) {
		List<Object> recipes = new ArrayList<>();
		for (IRecipe recipe : RollingMachineRecipe.instance.getRecipeList()) {
			RollingMachineRecipeWrapper recipeWrapper = RollingMachineRecipeWrapper.create(jeiHelpers, recipe);
			if (recipeWrapper != null) {
				recipes.add(recipeWrapper);
			}
		}
		return recipes;
	}
}
