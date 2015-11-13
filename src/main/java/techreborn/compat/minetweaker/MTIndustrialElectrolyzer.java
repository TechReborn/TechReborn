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
import techreborn.api.recipe.machines.IndustrialElectrolyzerRecipe;
import techreborn.lib.Reference;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.techreborn.industrialElectrolyzer")
public class MTIndustrialElectrolyzer {
    @ZenMethod
    public static void addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, IIngredient cells, IIngredient input2, int ticktime, int euTick) {
        ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(cells);
        ItemStack oInput2 = (ItemStack) MinetweakerCompat.toObject(input2);

        IndustrialElectrolyzerRecipe r = new IndustrialElectrolyzerRecipe(oInput1, oInput2, MinetweakerCompat.toStack(output1), MinetweakerCompat.toStack(output2), MinetweakerCompat.toStack(output3), MinetweakerCompat.toStack(output4), ticktime, euTick);

        MineTweakerAPI.apply(new Add(r));
    }

    private static class Add implements IUndoableAction {
        private final IndustrialElectrolyzerRecipe recipe;

        public Add(IndustrialElectrolyzerRecipe recipe) {
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
            return "Adding IndustrialElectrolyzerRecipe for " + recipe.getOutput(0).getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Removing IndustrialElectrolyzerRecipe for " + recipe.getOutput(0).getDisplayName();
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
        List<IndustrialElectrolyzerRecipe> removedRecipes = new ArrayList<IndustrialElectrolyzerRecipe>();

        public Remove(ItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            for (IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(Reference.industrialElectrolyzerRecipe)) {
                for (ItemStack stack : recipeType.getOutputs()) {
                    if (ItemUtils.isItemEqual(stack, output, true, false)) {
                        removedRecipes.add((IndustrialElectrolyzerRecipe) recipeType);
                        RecipeHandler.recipeList.remove(recipeType);
                        break;
                    }
                }
            }
        }

        @Override
        public void undo() {
            if (removedRecipes != null) {
                for (IndustrialElectrolyzerRecipe recipe : removedRecipes) {
                    if (recipe != null) {
                        RecipeHandler.addRecipe(recipe);
                    }
                }
            }

        }

        @Override
        public String describe() {
            return "Removing IndustrialElectrolyzerRecipe for " + output.getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Re-Adding IndustrialElectrolyzerRecipe for " + output.getDisplayName();
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
