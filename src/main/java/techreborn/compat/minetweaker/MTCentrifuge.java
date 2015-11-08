package techreborn.compat.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import reborncore.common.util.ItemUtils;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.CentrifugeRecipe;
import techreborn.lib.Reference;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.techreborn.centrifuge")
public class MTCentrifuge {
    @ZenMethod
    public static void addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, IIngredient input1, IIngredient input2, int ticktime, int euTick) {
        ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input1);
        ItemStack oInput2 = (ItemStack) MinetweakerCompat.toObject(input2);

        CentrifugeRecipe r = new CentrifugeRecipe(oInput1, oInput2, MinetweakerCompat.toStack(output1), MinetweakerCompat.toStack(output2), MinetweakerCompat.toStack(output3), MinetweakerCompat.toStack(output4), ticktime, euTick);

        MineTweakerAPI.apply(new Add(r));
    }

    private static class Add implements IUndoableAction {
        private final CentrifugeRecipe recipe;

        public Add(CentrifugeRecipe recipe) {
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
            return "Adding Centrifuge Recipe for " + recipe.getOutput(0).getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Removing Centrifuge Recipe for " + recipe.getOutput(0).getDisplayName();
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

    @ZenMethod
    public static void removeInputRecipe(IItemStack output) {
        MineTweakerAPI.apply(new RemoveInput(MinetweakerCompat.toStack(output)));
    }

    private static class RemoveInput implements IUndoableAction {
        private final ItemStack output;
        List<CentrifugeRecipe> removedRecipes = new ArrayList<CentrifugeRecipe>();

        public RemoveInput(ItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            for (IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(Reference.centrifugeRecipe)) {
                for (ItemStack stack : recipeType.getInputs()) {
                    if (ItemUtils.isItemEqual(stack, output, true, false)) {
                        removedRecipes.add((CentrifugeRecipe) recipeType);
                        RecipeHandler.recipeList.remove(recipeType);
                        break;
                    }
                }
            }
        }

        @Override
        public void undo() {
            if (removedRecipes != null) {
                for (CentrifugeRecipe recipe : removedRecipes) {
                    if (recipe != null) {
                        RecipeHandler.addRecipe(recipe);
                    }
                }
            }

        }

        @Override
        public String describe() {
            return "Removing Centrifuge Recipe for " + output.getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Re-Adding Centrifuge Recipe for " + output.getDisplayName();
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

    private static class Remove implements IUndoableAction {
        private final ItemStack output;
        List<CentrifugeRecipe> removedRecipes = new ArrayList<CentrifugeRecipe>();

        public Remove(ItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            for (IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(Reference.centrifugeRecipe)) {
                for (ItemStack stack : recipeType.getOutputs()) {
                    if (ItemUtils.isItemEqual(stack, output, true, false)) {
                        removedRecipes.add((CentrifugeRecipe) recipeType);
                        RecipeHandler.recipeList.remove(recipeType);
                        break;
                    }
                }
            }
        }

        @Override
        public void undo() {
            if (removedRecipes != null) {
                for (CentrifugeRecipe recipe : removedRecipes) {
                    if (recipe != null) {
                        RecipeHandler.addRecipe(recipe);
                    }
                }
            }

        }

        @Override
        public String describe() {
            return "Removing Centrifuge Recipe for " + output.getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Re-Adding Centrifuge Recipe for " + output.getDisplayName();
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
