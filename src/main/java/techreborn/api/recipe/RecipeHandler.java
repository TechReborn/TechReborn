package techreborn.api.recipe;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.time.StopWatch;
import reborncore.common.util.ItemUtils;
import techreborn.Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeHandler {

    /**
     * This is the array list of all of the recipes for all of the machines
     */
    public static final ArrayList<IBaseRecipeType> recipeList = new ArrayList<>();

    public static HashMap<IBaseRecipeType, String> stackMap = new HashMap<>();
    /**
     * This is a list of all the registered machine names.
     */
    public static ArrayList<String> machineNames = new ArrayList<>();

    /**
     * Use this to get all of the recipes form a recipe name
     *
     * @param name the name that the recipe was resisted as.
     * @return A list of all the recipes of a given name.
     */
    public static List<IBaseRecipeType> getRecipeClassFromName(String name) {
        List<IBaseRecipeType> baseRecipeList = new ArrayList<>();
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
        // if (!RecipeConfigManager.canLoadRecipe(recipe)) {
        // return;
        // }
        if (!machineNames.contains(recipe.getRecipeName())) {
            machineNames.add(recipe.getRecipeName());
        }
        recipeList.add(recipe);
        StringBuilder buffer = new StringBuilder();
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            buffer.append(ste);
        }
        stackMap.put(recipe, buffer.toString());
    }

    public static void scanForDupeRecipes() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();
        for (IBaseRecipeType baseRecipeType : recipeList) {
            for (IBaseRecipeType recipe : recipeList) {
                if (baseRecipeType != recipe && baseRecipeType.getRecipeName().equals(recipe.getRecipeName())) {
                    for (int i = 0; i < baseRecipeType.getInputs().size(); i++) {
                        if (ItemUtils.isItemEqual(baseRecipeType.getInputs().get(i), recipe.getInputs().get(i), true,
                                false, false)) {
                            StringBuilder itemInfo = new StringBuilder();
                            for (ItemStack inputs : baseRecipeType.getInputs()) {
                                itemInfo.append(":").append(inputs.getItem().getUnlocalizedName()).append(",").append(inputs.getDisplayName()).append(",").append(inputs.stackSize);
                            }
                            Core.logHelper.all(stackMap.get(baseRecipeType));
                            // throw new Exception("Found a duplicate recipe for
                            // " + baseRecipeType.getRecipeName() + " with
                            // inputs " + itemInfo.toString());
                        }
                    }
                }
            }
        }
        Core.logHelper.all(watch + " : Scanning dupe recipes");
        watch.stop();

    }

}