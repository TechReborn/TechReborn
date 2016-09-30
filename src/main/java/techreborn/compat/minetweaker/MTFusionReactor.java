package techreborn.compat.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import reborncore.common.util.ItemUtils;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;

import java.util.ArrayList;
import java.util.List;

@ZenClass("mods.techreborn.fusionReactor")
public class MTFusionReactor {

	@ZenMethod
	public static void addRecipe(IIngredient topInput, IIngredient bottomInput, IItemStack output, int startEU, int euTick, int tickTime) {
		FusionReactorRecipe reactorRecipe = new FusionReactorRecipe((ItemStack) MinetweakerCompat.toObject(topInput), (ItemStack) MinetweakerCompat.toObject(bottomInput), MinetweakerCompat.toStack(output), startEU, euTick, tickTime);
		MineTweakerAPI.apply(new Add(reactorRecipe));
	}

	@ZenMethod
	public static void removeTopInputRecipe(IIngredient iIngredient) {
		MineTweakerAPI.apply(new RemoveTopInput(iIngredient));
	}

	@ZenMethod
	public static void removeBottomInputRecipe(IIngredient iIngredient) {
		MineTweakerAPI.apply(new RemoveTopInput(iIngredient));
	}

	private static class Add implements IUndoableAction {
		private final FusionReactorRecipe recipe;

		public Add(FusionReactorRecipe recipe) {
			this.recipe = recipe;
		}

		@Override
		public void apply() {
			FusionReactorRecipeHelper.registerRecipe(recipe);
		}

		@Override
		public boolean canUndo() {
			return true;
		}

		@Override
		public void undo() {
			FusionReactorRecipeHelper.reactorRecipes.remove(recipe);
		}

		@Override
		public String describe() {
			return "Adding Fusion Reactor recipe for " + recipe.getOutput().getDisplayName();
		}

		@Override
		public String describeUndo() {
			return "Removing Fusion Reactor recipe for " + recipe.getOutput().getDisplayName();
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
		List<FusionReactorRecipe> removedRecipes = new ArrayList<FusionReactorRecipe>();

		public Remove(ItemStack output) {
			this.output = output;
		}

		@Override
		public void apply() {
			for (FusionReactorRecipe recipeType : FusionReactorRecipeHelper.reactorRecipes) {
				if (ItemUtils.isItemEqual(recipeType.getOutput(), output, true, false)) {
					removedRecipes.add(recipeType);
					FusionReactorRecipeHelper.reactorRecipes.remove(recipeType);
					break;
				}

			}
		}

		@Override
		public void undo() {
			if (removedRecipes != null) {
				for (FusionReactorRecipe recipe : removedRecipes) {
					if (recipe != null) {
						FusionReactorRecipeHelper.registerRecipe(recipe);
					}
				}
			}

		}

		@Override
		public String describe() {
			return "Removing Fusion Reactor recipe for " + output.getDisplayName();
		}

		@Override
		public String describeUndo() {
			return "Re-Adding Fusion Reactor recipe for " + output.getDisplayName();
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

	private static class RemoveTopInput implements IUndoableAction {
		private final IIngredient output;
		List<FusionReactorRecipe> removedRecipes = new ArrayList<FusionReactorRecipe>();

		public RemoveTopInput(IIngredient output) {
			this.output = output;
		}

		@Override
		public void apply() {
			for (FusionReactorRecipe recipeType : FusionReactorRecipeHelper.reactorRecipes) {
				if (output.matches(MineTweakerMC.getIItemStack(recipeType.getTopInput()))) {
					removedRecipes.add(recipeType);
					FusionReactorRecipeHelper.reactorRecipes.remove(recipeType);
					break;
				}
			}
		}

		@Override
		public void undo() {
			if (removedRecipes != null) {
				for (FusionReactorRecipe recipe : removedRecipes) {
					if (recipe != null) {
						FusionReactorRecipeHelper.registerRecipe(recipe);
					}
				}
			}

		}

		@Override
		public String describe() {
			return "Removing Fusion Reactor recipe";
		}

		@Override
		public String describeUndo() {
			return "Re-Adding Fusion Reactor recipe";
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

	private static class RemoveBottomInput implements IUndoableAction {
		private final IIngredient output;
		List<FusionReactorRecipe> removedRecipes = new ArrayList<FusionReactorRecipe>();

		public RemoveBottomInput(IIngredient output) {
			this.output = output;
		}

		@Override
		public void apply() {
			for (FusionReactorRecipe recipeType : FusionReactorRecipeHelper.reactorRecipes) {
				if (output.matches(MineTweakerMC.getIItemStack(recipeType.getBottomInput()))) {
					removedRecipes.add(recipeType);
					FusionReactorRecipeHelper.reactorRecipes.remove(recipeType);
					break;
				}
			}
		}

		@Override
		public void undo() {
			if (removedRecipes != null) {
				for (FusionReactorRecipe recipe : removedRecipes) {
					if (recipe != null) {
						FusionReactorRecipeHelper.registerRecipe(recipe);
					}
				}
			}

		}

		@Override
		public String describe() {
			return "Removing Fusion Reactor recipe";
		}

		@Override
		public String describeUndo() {
			return "Re-Adding Fusion Reactor recipe";
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
