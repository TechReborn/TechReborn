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
import techreborn.api.recipe.machines.PlateCuttingMachineRecipe;
import techreborn.lib.Reference;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.techreborn.plateCuttingMachine")
public class MTPlateCuttingMachine {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input1, int ticktime, int euTick) {
        ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input1);
        PlateCuttingMachineRecipe r = new PlateCuttingMachineRecipe(oInput1, MinetweakerCompat.toStack(output), ticktime, euTick);

        MineTweakerAPI.apply(new Add(r));
    }

    private static class Add implements IUndoableAction {
        private final PlateCuttingMachineRecipe recipe;

        public Add(PlateCuttingMachineRecipe recipe) {
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
            return "Adding PlateCuttingMachineRecipe for " + recipe.getOutput(0).getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Removing PlateCuttingMachineRecipe for " + recipe.getOutput(0).getDisplayName();
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
        List<PlateCuttingMachineRecipe> removedRecipes = new ArrayList<PlateCuttingMachineRecipe>();

        public Remove(ItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            for (IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(Reference.plateCuttingMachineRecipe)) {
                for (ItemStack stack : recipeType.getOutputs()) {
                    if (ItemUtils.isItemEqual(stack, output, true, false)) {
                        removedRecipes.add((PlateCuttingMachineRecipe) recipeType);
                        RecipeHandler.recipeList.remove(recipeType);
                        break;
                    }
                }
            }
        }

        @Override
        public void undo() {
            if (removedRecipes != null) {
                for (PlateCuttingMachineRecipe recipe : removedRecipes) {
                    if (recipe != null) {
                        RecipeHandler.addRecipe(recipe);
                    }
                }
            }

        }

        @Override
        public String describe() {
            return "Removing PlateCuttingMachineRecipe for " + output.getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Re-Adding PlateCuttingMachineRecipe for " + output.getDisplayName();
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
