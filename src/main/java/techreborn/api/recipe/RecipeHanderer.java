package techreborn.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;


public class RecipeHanderer {

	/**
	 * This is the array list of all of the recipes for all of the machines
	 */
	public static ArrayList<IBaseRecipeType> recipeList = new ArrayList<IBaseRecipeType>();

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
		ArrayList<IBaseRecipeType> newRecipes = new ArrayList<IBaseRecipeType>();
		for(IBaseRecipeType baseRecipe : recipeList){
			if(baseRecipe instanceof BaseRecipe){
				int i = 0;
				for(ItemStack stack : baseRecipe.getInputs()){
					for(int oreId : OreDictionary.getOreIDs(stack)){
						for(ItemStack itemStack : OreDictionary.getOres(oreId)){
							OreDicRecipe recipe = new OreDicRecipe(baseRecipe.getRecipeName(), ((BaseRecipe) baseRecipe).tickTime, ((BaseRecipe) baseRecipe).tickTime);
							recipe.inputs.add(itemStack);
							newRecipes.add(recipe);
						}
					}
				}
			}
		}
	}

	public static class OreDicRecipe implements IBaseRecipeType{

		public ArrayList<ItemStack> inputs;

		public ArrayList<ItemStack> outputs;

		public String name;

		public int tickTime;

		public int euPerTick;

		public OreDicRecipe(String name, int tickTime, int euPerTick) {
			inputs = new ArrayList<ItemStack>();
			outputs = new ArrayList<ItemStack>();
			this.name = name;
			//This adds all new recipes
			this.tickTime = tickTime;
			this.euPerTick = euPerTick;
		}

		@Override
		public List<ItemStack> getOutputs() {
			return outputs;
		}

		@Override
		public List<ItemStack> getInputs() {
			return inputs;
		}

		@Override
		public String getRecipeName() {
			return name;
		}

		@Override
		public int tickTime() {
			return tickTime;
		}

		@Override
		public int euPerTick() {
			return euPerTick;
		}
	}

}