package techreborn.compat.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.LatheRecipe;
import techreborn.lib.Reference;
import techreborn.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.techreborn.lathe")
public class MTLathe {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input1, int ticktime, int euTick) {
        ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input1);
        if (oInput1 == null)
            return;

        LatheRecipe r = new LatheRecipe(oInput1, MinetweakerCompat.toStack(output), ticktime, euTick);

        MineTweakerAPI.apply(new Add(r));
    }

    private static class Add implements IUndoableAction {
        private final LatheRecipe recipe;

        public Add(LatheRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            RecipeHandler.addRecipe(recipe);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            RecipeHandler.recipeList.remove(recipe);
        }

        @Override
        public String describe() {
            return "Adding Lathe Recipe for " + recipe.getOutput(0).getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Removing Lathe Recipe for " + recipe.getOutput(0).getDisplayName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(MinetweakerCompat.toStack(output)));
    }

    private static class Remove implements IUndoableAction {
        private final ItemStack output;
        List<LatheRecipe> removedRecipes = new ArrayList<LatheRecipe>();

        public Remove(ItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            for (IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(Reference.latheRecipe)) {
                for (ItemStack stack : recipeType.getOutputs()) {
                    if (ItemUtils.isItemEqual(stack, output, true, false)) {
                        removedRecipes.add((LatheRecipe) recipeType);
                        RecipeHandler.recipeList.remove(recipeType);
                        break;
                    }
                }
            }
        }

        @Override
        public void undo() {
            if (removedRecipes != null) {
                for (LatheRecipe recipe : removedRecipes) {
                    if (recipe != null) {
                        RecipeHandler.addRecipe(recipe);
                    }
                }
            }

        }

        @Override
        public String describe() {
            return "Removing Lathe Recipe for " + output.getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Re-Adding Lathe Recipe for " + output.getDisplayName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }

        @Override
        public boolean canUndo() {
            return true;
        }
    }
}
