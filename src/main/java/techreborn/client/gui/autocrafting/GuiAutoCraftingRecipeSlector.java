package techreborn.client.gui.autocrafting;

import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.item.crafting.IRecipe;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class GuiAutoCraftingRecipeSlector extends GuiRecipeBook {

	@Override
	public void func_193945_a(IRecipe recipe, RecipeList recipes) {
		System.out.println(recipe.getRecipeOutput().getDisplayName());
	}
}
