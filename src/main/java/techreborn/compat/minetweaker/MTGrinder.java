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
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.lib.Reference;
import techreborn.util.ItemUtils;

import java.util.List;

@ZenClass("mods.techreborn.grinder")
public class MTGrinder {
    @ZenMethod
    public static void addRecipe(IItemStack output1 ,IItemStack output2 ,IItemStack output3,IItemStack output4, IIngredient input1, IIngredient input2, int ticktime, int euTick) {
        addRecipe(output1, output2, output3, output4, input1, input2, null,  ticktime, euTick);
    }

    @ZenMethod
    public static void addRecipe(IItemStack output1 ,IItemStack output2 ,IItemStack output3,IItemStack output4, IIngredient input1, IIngredient input2, ILiquidStack fluid, int ticktime, int euTick) {
        ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input1);
        if (oInput1 == null)
            return;

        ItemStack oInput2 = (ItemStack) MinetweakerCompat.toObject(input2);
        if (oInput2 == null)
            return;

        FluidStack fluidStack = null;
        if(fluid != null){
            fluidStack = MinetweakerCompat.toFluidStack(fluid);
        }

        GrinderRecipe r = new GrinderRecipe(oInput1, oInput2,fluidStack, MinetweakerCompat.toStack(output1), MinetweakerCompat.toStack(output2), MinetweakerCompat.toStack(output3), MinetweakerCompat.toStack(output4), ticktime, euTick);

        MineTweakerAPI.apply(new Add(r));
    }

    private static class Add implements IUndoableAction {
        private final GrinderRecipe recipe;

        public Add(GrinderRecipe recipe) {
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
            return "Adding Grinder Recipe for " + recipe.getOutput(0).getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Removing Grinder Recipe for " + recipe.getOutput(0).getDisplayName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output)
    {
        MineTweakerAPI.apply(new Remove(MinetweakerCompat.toStack(output)));
    }
    private static class Remove implements IUndoableAction
    {
        private final ItemStack output;
        List<GrinderRecipe> removedRecipes;
        public Remove(ItemStack output)
        {
            this.output = output;
        }
        @Override
        public void apply()
        {
            for(IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(Reference.grinderRecipe)){
                for(ItemStack stack : recipeType.getOutputs()){
                    if(ItemUtils.isItemEqual(stack, output, true, false)){
                        removedRecipes.add((GrinderRecipe) recipeType);
                        RecipeHandler.recipeList.remove(recipeType);
                        break;
                    }
                }
            }
        }
        @Override
        public void undo()
        {
            if(removedRecipes!=null){
                for(GrinderRecipe recipe : removedRecipes){
                    if(recipe!=null){
                        RecipeHandler.addRecipe(recipe);
                    }
                }
            }

        }
        @Override
        public String describe()
        {
            return "Removing Grinder Recipe for " + output.getDisplayName();
        }
        @Override
        public String describeUndo()
        {
            return "Re-Adding Grinder Recipe for " + output.getDisplayName();
        }
        @Override
        public Object getOverrideKey()
        {
            return null;
        }
        @Override
        public boolean canUndo()
        {
            return true;
        }
    }
}
