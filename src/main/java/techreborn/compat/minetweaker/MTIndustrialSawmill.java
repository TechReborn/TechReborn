package techreborn.compat.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.lib.Reference;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.techreborn.industrialSawmill")
public class MTIndustrialSawmill {

    @ZenMethod
    public static void addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IIngredient input1, IIngredient input2, ILiquidStack fluid, int ticktime, int euTick) {
        addRecipe(output1, output2, output3, input1, input2, fluid, ticktime, euTick, true);
    }

    @ZenMethod
    public static void addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IIngredient input1, IIngredient input2, int ticktime, int euTick) {
        addRecipe(output1, output2, output3, input1, input2, null, ticktime, euTick, true);
    }

    @ZenMethod
    public static void addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IIngredient input1, IIngredient input2, int ticktime, int euTick, boolean useOreDic) {
        addRecipe(output1, output2, output3, input1, input2, null, ticktime, euTick, useOreDic);
    }

    @ZenMethod
    public static void addRecipe(IItemStack output1, IItemStack output2, IItemStack output3, IIngredient input1, IIngredient input2, ILiquidStack fluid, int ticktime, int euTick, boolean useOreDic) {
        ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input1);

        ItemStack oInput2 = (ItemStack) MinetweakerCompat.toObject(input2);

        FluidStack fluidStack = null;
        if (fluid != null) {
            fluidStack = MinetweakerCompat.toFluidStack(fluid);
        }

        IndustrialSawmillRecipe r = new IndustrialSawmillRecipe(oInput1, oInput2, fluidStack, MinetweakerCompat.toStack(output1), MinetweakerCompat.toStack(output2), MinetweakerCompat.toStack(output3), ticktime, euTick, useOreDic);

        MineTweakerAPI.apply(new Add(r));
    }

    private static class Add implements IUndoableAction {
        private final IndustrialSawmillRecipe recipe;

        public Add(IndustrialSawmillRecipe recipe) {
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
            return "Adding Sawmill Recipe for " + recipe.getOutput(0).getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Removing Sawmill Recipe for " + recipe.getOutput(0).getDisplayName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        for(IItemStack  itemStack : output.getItems()){
            MineTweakerAPI.apply(new Remove(MinetweakerCompat.toStack(itemStack)));
        }
    }

    private static class Remove implements IUndoableAction {
        private final ItemStack output;
        List<IndustrialSawmillRecipe> removedRecipes = new ArrayList<IndustrialSawmillRecipe>();

        public Remove(ItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            for (IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(Reference.industrialSawmillRecipe)) {
                for (ItemStack stack : recipeType.getOutputs()) {
                    if (ItemUtils.isItemEqual(stack, output, true, false)) {
                        removedRecipes.add((IndustrialSawmillRecipe) recipeType);
                        RecipeHandler.recipeList.remove(recipeType);
                        break;
                    }
                }
            }
        }

        @Override
        public void undo() {
            if (removedRecipes != null) {
                for (IndustrialSawmillRecipe recipe : removedRecipes) {
                    if (recipe != null) {
                        RecipeHandler.addRecipe(recipe);
                    }
                }
            }

        }

        @Override
        public String describe() {
            return "Removing Sawmill Recipe for " + output.getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Re-Adding Sawmill Recipe for " + output.getDisplayName();
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
