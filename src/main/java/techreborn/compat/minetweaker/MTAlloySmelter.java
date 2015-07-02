package techreborn.compat.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.util.ItemUtils;

@ZenClass("techreborn.crafting.AlloySmelter")
public class MTAlloySmelter {

	@ZenMethod
	public static void addRecipe(IItemStack output, IItemStack input1, IItemStack input2, int ticktime, int eutick) {
		MineTweakerAPI.apply(new AddRecipeAction(input1, input2, output, ticktime, eutick));
	}

	@ZenMethod
	public static void remove(IIngredient output) {
		for(IBaseRecipeType recipeType : RecipeHandler.recipeList){
			if(recipeType.getRecipeName().equals("alloySmelterRecipe")){
				for(ItemStack outputstack : recipeType.getOutputs()){
					if(ItemUtils.isItemEqual(MineTweakerMC.getItemStack(output), outputstack, true, false, false)){
						MineTweakerAPI.apply(new RemoveRecipeAction(recipeType));
					}
				}
			}
		}
	}

	private static class AddRecipeAction extends AlloySmelterRecipe implements IUndoableAction{

		public AddRecipeAction(IItemStack input1, IItemStack input2, IItemStack output1, int tickTime, int euPerTick) {
			super(MineTweakerMC.getItemStack(input1), MineTweakerMC.getItemStack(input2), MineTweakerMC.getItemStack(output1), tickTime, euPerTick);
		}

		@Override
		public void apply() {
			RecipeHandler.addRecipe(this);
		}

		@Override
		public boolean canUndo() {
			return false;
		}

		@Override
		public void undo() {

		}

		@Override
		public String describe() {
			return "Adding recipe to the alloy furnace";
		}

		@Override
		public String describeUndo() {
			return "Removing recipe to the alloy furnace";
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
	}

	private static class RemoveRecipeAction implements IUndoableAction {
		private final IBaseRecipeType recipe;

		public RemoveRecipeAction(IBaseRecipeType recipeType) {
			this.recipe = recipeType;
		}

		@Override
		public void apply() {
			RecipeHandler.recipeList.remove(recipe);
		}

		@Override
		public boolean canUndo() {
			return false;
		}

		@Override
		public void undo() {

		}

		@Override
		public String describe() {
			return "Removing" + recipe.getUserFreindlyName() + "recipe";
		}

		@Override
		public String describeUndo() {
			return "Restoring" + recipe.getUserFreindlyName() + "recipe";
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
	}
}
