//Copy and pasted from https://github.com/Chicken-Bones/NotEnoughItems/blob/master/src/codechicken/nei/recipe/ShapelessRecipeHandler.java
package techreborn.compat.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import techreborn.api.RollingMachineRecipe;
import techreborn.client.gui.GuiRollingMachine;

import java.awt.*;
import java.util.List;

public class ShapelessRollingMachineHandler extends ShapelessRecipeHandler {

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiRollingMachine.class;
    }

    public String getRecipeName() {
        return "Shapeless Rolling Machine";
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18),
                "rollingcraftingnoshape"));
    }

    @Override
    public String getOverlayIdentifier() {
        return "rollingcraftingnoshape";
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("rollingcraftingnoshape")
                && getClass() == ShapelessRollingMachineHandler.class) {
            List<IRecipe> allrecipes = RollingMachineRecipe.instance
                    .getRecipeList();
            for (IRecipe irecipe : allrecipes) {
                CachedShapelessRecipe recipe = null;
                if (irecipe instanceof ShapelessRecipes)
                    recipe = shapelessRecipe((ShapelessRecipes) irecipe);
                else if (irecipe instanceof ShapelessOreRecipe)
                    recipe = forgeShapelessRecipe((ShapelessOreRecipe) irecipe);

                if (recipe == null)
                    continue;

                arecipes.add(recipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        List<IRecipe> allrecipes = RollingMachineRecipe.instance
                .getRecipeList();
        for (IRecipe irecipe : allrecipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(
                    irecipe.getRecipeOutput(), result)) {
                CachedShapelessRecipe recipe = null;
                if (irecipe instanceof ShapelessRecipes)
                    recipe = shapelessRecipe((ShapelessRecipes) irecipe);
                else if (irecipe instanceof ShapelessOreRecipe)
                    recipe = forgeShapelessRecipe((ShapelessOreRecipe) irecipe);

                if (recipe == null)
                    continue;

                arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        List<IRecipe> allrecipes = RollingMachineRecipe.instance
                .getRecipeList();
        for (IRecipe irecipe : allrecipes) {
            CachedShapelessRecipe recipe = null;
            if (irecipe instanceof ShapelessRecipes)
                recipe = shapelessRecipe((ShapelessRecipes) irecipe);
            else if (irecipe instanceof ShapelessOreRecipe)
                recipe = forgeShapelessRecipe((ShapelessOreRecipe) irecipe);

            if (recipe == null)
                continue;

            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

    private CachedShapelessRecipe shapelessRecipe(ShapelessRecipes recipe) {
        if (recipe.recipeItems == null)
            return null;

        return new CachedShapelessRecipe(recipe.recipeItems,
                recipe.getRecipeOutput());
    }

    @Override
    public String getGuiTexture() {
        return "techreborn:textures/gui/rolling_machine.png";
    }
}
