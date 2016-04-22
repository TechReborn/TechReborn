package techreborn.compat.jei.rollingMachine;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import net.minecraft.item.crafting.IRecipe;
import techreborn.api.RollingMachineRecipe;

public class RollingMachineRecipeMaker
{
	private RollingMachineRecipeMaker()
	{

	}

	public static List<Object> getRecipes(@Nonnull IJeiHelpers jeiHelpers)
	{
		List<Object> recipes = new ArrayList<>();
		for (IRecipe recipe : RollingMachineRecipe.instance.getRecipeList())
		{
			RollingMachineRecipeWrapper recipeWrapper = RollingMachineRecipeWrapper.create(jeiHelpers, recipe);
			if (recipeWrapper != null)
			{
				recipes.add(recipeWrapper);
			}
		}
		return recipes;
	}
}
