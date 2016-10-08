package techreborn.compat.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import reborncore.api.recipe.IBaseRecipeType;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.util.ItemUtils;
import techreborn.api.recipe.BaseRecipe;

import java.util.ArrayList;
import java.util.List;

public class MTGeneric {
	public static String getMachineName() {
		return null;
	}

	public static void addRecipe(BaseRecipe recipe) {
		MineTweakerAPI.apply(new Add(recipe));
	}

	private static class Add implements IUndoableAction {
		private final BaseRecipe recipe;

		public Add(BaseRecipe recipe) {
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
			return "Adding " + recipe.getRecipeName() + " recipe for " + recipe.getOutput(0).getDisplayName();
		}

		@Override
		public String describeUndo() {
			return "Removing " + recipe.getRecipeName() + " recipe for " + recipe.getOutput(0).getDisplayName();
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
	}

	public static class Remove implements IUndoableAction {
		private final ItemStack output;
		List<BaseRecipe> removedRecipes = new ArrayList<BaseRecipe>();
		private final String name;

		public Remove(ItemStack output, String machineName) {
			this.output = output;
			this.name = machineName;
		}

		@Override
		public void apply() {
			for (IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(name)) {
				for (ItemStack stack : recipeType.getOutputs()) {
					if (ItemUtils.isItemEqual(stack, output, true, false)) {
						removedRecipes.add((BaseRecipe) recipeType);
						RecipeHandler.recipeList.remove(recipeType);
						break;
					}
				}
			}
		}

		@Override
		public void undo() {
			if (removedRecipes != null) {
				for (BaseRecipe recipe : removedRecipes) {
					if (recipe != null) {
						RecipeHandler.addRecipe(recipe);
					}
				}
			}

		}

		@Override
		public String describe() {
			return "Removing " + name + " recipe for " + output.getDisplayName();
		}

		@Override
		public String describeUndo() {
			return "Re-Adding " + name + " recipe for " + output.getDisplayName();
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

	public static class RemoveInput implements IUndoableAction {
		private final IIngredient output;
		List<BaseRecipe> removedRecipes = new ArrayList<BaseRecipe>();
		private final String name;

		public RemoveInput(IIngredient output, String machineName) {
			this.output = output;
			this.name = machineName;
		}

		@Override
		public void apply() {
			for (IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(name)) {
				for (ItemStack stack : recipeType.getInputs()) {
					if (output.matches(MineTweakerMC.getIItemStack(stack))) {
						removedRecipes.add((BaseRecipe) recipeType);
						RecipeHandler.recipeList.remove(recipeType);
						break;
					}
				}
			}
		}

		@Override
		public void undo() {
			if (removedRecipes != null) {
				for (BaseRecipe recipe : removedRecipes) {
					if (recipe != null) {
						RecipeHandler.addRecipe(recipe);
					}
				}
			}

		}

		@Override
		public String describe() {
			return "Removing " + name + " recipe";
		}

		@Override
		public String describeUndo() {
			return "Re-Adding " + name + " recipe";
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
