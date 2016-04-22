package techreborn.compat.jei.rollingMachine;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapedOreRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapedRecipesWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapelessOreRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipesWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import techreborn.Core;
import techreborn.compat.CompatManager;
import techreborn.compat.jei.TechRebornJeiPlugin;

public class RollingMachineRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper
{
	private final ICraftingRecipeWrapper baseRecipe;

	public RollingMachineRecipeWrapper(ICraftingRecipeWrapper baseRecipe)
	{
		this.baseRecipe = baseRecipe;
	}

	@Nullable
	public static RollingMachineRecipeWrapper create(@Nonnull IJeiHelpers jeiHelpers, IRecipe baseRecipe)
	{
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		ICraftingRecipeWrapper recipeWrapper;
		if (baseRecipe instanceof ShapelessRecipes)
		{
			recipeWrapper = new ShapelessRecipesWrapper(guiHelper, (ShapelessRecipes) baseRecipe);
		} else if (baseRecipe instanceof ShapedRecipes)
		{
			recipeWrapper = new ShapedRecipesWrapper((ShapedRecipes) baseRecipe);
		} else if (baseRecipe instanceof ShapedOreRecipe)
		{
			recipeWrapper = new ShapedOreRecipeWrapper((ShapedOreRecipe) baseRecipe);
		} else if (baseRecipe instanceof ShapelessOreRecipe)
		{
			recipeWrapper = new ShapelessOreRecipeWrapper(guiHelper, (ShapelessOreRecipe) baseRecipe);
		} else
		{
			return null;
		}

		return new RollingMachineRecipeWrapper(recipeWrapper);
	}

	@Override
	@Nonnull
	public List getInputs()
	{
		return baseRecipe.getInputs();
	}

	@Override
	@Nonnull
	public List<ItemStack> getOutputs()
	{
		return baseRecipe.getOutputs();
	}
}
