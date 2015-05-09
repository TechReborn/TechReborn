package techreborn.api.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class RecipeHanderer {

	/**
	 * This is the array list of all of the recipes for all of the machines
	 */
	public static ArrayList<IBaseRecipeType> recipeList = new ArrayList<IBaseRecipeType>();

	/**
	 * This is a backedup clone of the master recipeList
	 */
	public static ArrayList<IBaseRecipeType> recipeListBackup = new ArrayList<IBaseRecipeType>();

	/**
	 * Use this to get all of the recipes form a recipe name
	 * @param name the name that the recipe was resisted as.
	 * @return A list of all the recipes of a given name.
	 */
	public static List<IBaseRecipeType> getRecipeClassFromName(String name){
		List<IBaseRecipeType> baseRecipeList = new ArrayList<IBaseRecipeType>();
		for(IBaseRecipeType baseRecipe : recipeList){
			if(baseRecipe.getRecipeName().equals(name)){
				baseRecipeList.add(baseRecipe);
			}
		}
		return baseRecipeList;
	}

	/**
	 * Add a recipe to the system
	 * @param recipe The recipe to add to the system.
	 */
	public static void addRecipe(IBaseRecipeType recipe){
		if(recipe == null){
			return;
		}
		if(recipeList.contains(recipe)){
			return;
		}
		recipeList.add(recipe);
	}


	public static void addOreDicRecipes(){
		recipeListBackup.clear();
		recipeListBackup.addAll(recipeList);
		ArrayList<IBaseRecipeType> newRecipes = new ArrayList<IBaseRecipeType>();
		for(IBaseRecipeType baseRecipe : recipeList){
			if(baseRecipe instanceof BaseRecipe){
				for (int i = 0; i < baseRecipe.getInputs().size(); i++) {
					BaseRecipe newRecipe = (BaseRecipe) baseRecipe;
					for(int oreId : OreDictionary.getOreIDs(((BaseRecipe) baseRecipe).inputs.get(i))){
						for(ItemStack itemStack : getInputs(OreDictionary.getOreName(oreId))){
							System.out.println(itemStack.getDisplayName());
							newRecipe.inputs.set(i, itemStack);
						}
					}
					newRecipes.add(newRecipe);
				}
				for (int i = 0; i < baseRecipe.getOutputs().size(); i++) {
					BaseRecipe newRecipe = (BaseRecipe) baseRecipe;
					for(int oreId : OreDictionary.getOreIDs(((BaseRecipe) baseRecipe).outputs.get(i))){
						for(ItemStack itemStack : OreDictionary.getOres(OreDictionary.getOreName(oreId))){
							newRecipe.outputs.set(i, itemStack);
						}
					}
					newRecipes.add(newRecipe);
				}

			}
		}
		recipeList.addAll(newRecipes);
	}

	public static List<ItemStack> getInputs(String name) {
		List ores = getOres(name);
		boolean hasInvalidEntries = false;
		Iterator ret = ores.iterator();

		while(ret.hasNext()) {
			ItemStack i$ = (ItemStack)ret.next();
			if(i$.getItem() == null) {
				hasInvalidEntries = true;
				break;
			}
		}

		if(!hasInvalidEntries) {
			return ores;
		} else {
			ArrayList ret1 = new ArrayList(ores.size());
			Iterator i$1 = ores.iterator();

			while(i$1.hasNext()) {
				ItemStack stack = (ItemStack)i$1.next();
				if(stack.getItem() != null) {
					ret1.add(stack);
				}
			}

			return Collections.unmodifiableList(ret1);
		}
	}

	private static List<ItemStack> getOres(String name) {
		ArrayList ret = OreDictionary.getOres(name);
		return ret;
	}

}