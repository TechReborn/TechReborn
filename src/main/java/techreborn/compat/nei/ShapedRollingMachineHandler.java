//Copy and pasted from https://github.com/Chicken-Bones/NotEnoughItems/blob/master/src/codechicken/nei/recipe/ShapedRecipeHandler.java
package techreborn.compat.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.recipe.ShapedRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import techreborn.api.RollingMachineRecipe;
import techreborn.client.gui.GuiRollingMachine;

import java.awt.*;
import java.util.List;

public class ShapedRollingMachineHandler extends ShapedRecipeHandler {

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiRollingMachine.class;
	}

	@Override
	public void loadTransferRects() {
		this.transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24,
				18), "rollingcrafting", new Object[0]));
	}

	@Override
	public String getRecipeName() {
		return "rollingcrafting";
	}

	@Override
	public String getOverlayIdentifier() {
		return "rollingcrafting";
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("rollingcrafting")
				&& getClass() == ShapedRollingMachineHandler.class) {
			for (IRecipe irecipe : (List<IRecipe>) RollingMachineRecipe.instance
					.getRecipeList()) {
				CachedShapedRecipe recipe = null;
				if (irecipe instanceof ShapedRecipes)
					recipe = new CachedShapedRecipe((ShapedRecipes) irecipe);
				else if (irecipe instanceof ShapedOreRecipe)
					recipe = forgeShapedRecipe((ShapedOreRecipe) irecipe);

				if (recipe == null)
					continue;

				recipe.computeVisuals();
				arecipes.add(recipe);
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for (IRecipe irecipe : (List<IRecipe>) RollingMachineRecipe.instance
				.getRecipeList()) {
			if (NEIServerUtils.areStacksSameTypeCrafting(
					irecipe.getRecipeOutput(), result)) {
				CachedShapedRecipe recipe = null;
				if (irecipe instanceof ShapedRecipes)
					recipe = new CachedShapedRecipe((ShapedRecipes) irecipe);
				else if (irecipe instanceof ShapedOreRecipe)
					recipe = forgeShapedRecipe((ShapedOreRecipe) irecipe);

				if (recipe == null)
					continue;

				recipe.computeVisuals();
				arecipes.add(recipe);
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		for (IRecipe irecipe : (List<IRecipe>) RollingMachineRecipe.instance
				.getRecipeList()) {
			CachedShapedRecipe recipe = null;
			if (irecipe instanceof ShapedRecipes)
				recipe = new CachedShapedRecipe((ShapedRecipes) irecipe);
			else if (irecipe instanceof ShapedOreRecipe)
				recipe = forgeShapedRecipe((ShapedOreRecipe) irecipe);

			if (recipe == null
					|| !recipe.contains(recipe.ingredients,
					ingredient.getItem()))
				continue;

			recipe.computeVisuals();
			if (recipe.contains(recipe.ingredients, ingredient)) {
				recipe.setIngredientPermutation(recipe.ingredients, ingredient);
				arecipes.add(recipe);
			}
		}
	}
}
