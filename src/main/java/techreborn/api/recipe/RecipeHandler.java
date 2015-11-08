package techreborn.api.recipe;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.time.StopWatch;
import reborncore.common.util.ItemUtils;
import scala.actors.threadpool.Arrays;
import techreborn.Core;
import techreborn.api.recipe.recipeConfig.RecipeConfigManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RecipeHandler {

    /**
     * This is the array list of all of the recipes for all of the machines
     */
    public static final ArrayList<IBaseRecipeType> recipeList = new ArrayList<IBaseRecipeType>();


    public static HashMap<IBaseRecipeType, String> stackMap = new HashMap<IBaseRecipeType, String>();
    /**
     * This is a list of all the registered machine names.
     */
    public static ArrayList<String> machineNames = new ArrayList<String>();

    /**
     * Use this to get all of the recipes form a recipe name
     *
     * @param name the name that the recipe was resisted as.
     * @return A list of all the recipes of a given name.
     */
    public static List<IBaseRecipeType> getRecipeClassFromName(String name) {
        List<IBaseRecipeType> baseRecipeList = new ArrayList<IBaseRecipeType>();
        for (IBaseRecipeType baseRecipe : recipeList) {
            if (baseRecipe.getRecipeName().equals(name)) {
                baseRecipeList.add(baseRecipe);
            }
        }
        return baseRecipeList;
    }

    public static String getUserFreindlyName(String name) {
        for (IBaseRecipeType baseRecipe : recipeList) {
            if (baseRecipe.getRecipeName().equals(name)) {
                return baseRecipe.getUserFreindlyName();
            }
        }
        return "";
    }

    /**
     * Add a recipe to the system
     *
     * @param recipe The recipe to add to the system.
     */
    public static void addRecipe(IBaseRecipeType recipe) {
        if (recipe == null) {
            return;
        }
        if (recipeList.contains(recipe)) {
            return;
        }
        if (!RecipeConfigManager.canLoadRecipe(recipe)) {
            return;
        }
        if (!machineNames.contains(recipe.getRecipeName())) {
            machineNames.add(recipe.getRecipeName());
        }
        recipeList.add(recipe);
        StringBuffer buffer = new StringBuffer();
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            buffer.append(ste);
        }
        stackMap.put(recipe, buffer.toString());
        for(ItemStack stack : recipe.getOutputs()){
            if(stack.getItem() == null){
                throw new NullPointerException("Null item in stack!");
            }
        }
        for(ItemStack stack : recipe.getInputs()){
            if(stack.getItem() == null){
                throw new NullPointerException("Null item in stack!");
            }
        }
    }


    public static void scanForDupeRecipes() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();
        for (IBaseRecipeType baseRecipeType : recipeList) {
            for (IBaseRecipeType recipe : recipeList) {
                if (baseRecipeType != recipe && baseRecipeType.getRecipeName().equals(recipe.getRecipeName())) {
                    for (int i = 0; i < baseRecipeType.getInputs().size(); i++) {
                        if (ItemUtils.isItemEqual(baseRecipeType.getInputs().get(i), recipe.getInputs().get(i), true, false, false)) {
                            StringBuffer itemInfo = new StringBuffer();
                            for (ItemStack inputs : baseRecipeType.getInputs()) {
                                itemInfo.append(":" + inputs.getItem().getUnlocalizedName() + "," + inputs.getDisplayName() + "," + inputs.stackSize);
                            }
                            Core.logHelper.all(stackMap.get(baseRecipeType));
                            // throw new Exception("Found a duplicate recipe for " + baseRecipeType.getRecipeName() + " with inputs " + itemInfo.toString());
                        }
                    }
                }
            }
        }
        Core.logHelper.all(watch + " : Scanning dupe recipes");
        watch.stop();

    }

}