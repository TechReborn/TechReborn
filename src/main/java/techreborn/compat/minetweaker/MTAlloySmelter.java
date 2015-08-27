package techreborn.compat.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.lib.Reference;
import techreborn.util.CraftingHelper;
import techreborn.util.ItemUtils;

import java.util.List;

/**
 * mods.techreborn.alloySmelter.addRecipe(<minecraft:gold_ingot>, <minecraft:iron_ingot>, <minecraft:diamond>, 20, 100);
 */

@ZenClass("mods.techreborn.alloySmelter")
public class MTAlloySmelter {


	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient input1, IIngredient input2, int ticktime, int euTick) {
		ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input1);
		if (oInput1 == null)
			return;

		ItemStack oInput2 = (ItemStack) MinetweakerCompat.toObject(input2);
		if (oInput2 == null)
			return;

		AlloySmelterRecipe r = new AlloySmelterRecipe(oInput1, oInput2, MinetweakerCompat.toStack(output), ticktime, euTick);

		MineTweakerAPI.apply(new Add(r));
	}

	private static class Add implements IUndoableAction {
		private final AlloySmelterRecipe recipe;

		public Add(AlloySmelterRecipe recipe) {
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
			return "Adding Alloy Smelter Recipe for " + recipe.getOutput(0).getDisplayName();
		}

		@Override
		public String describeUndo() {
			return "Removing Alloy Smelter Recipe for " + recipe.getOutput(0).getDisplayName();
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
		List<AlloySmelterRecipe> removedRecipes;
		public Remove(ItemStack output)
		{
			this.output = output;
		}
		@Override
		public void apply()
		{
			for(IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(Reference.alloySmelteRecipe)){
				for(ItemStack stack : recipeType.getOutputs()){
					if(ItemUtils.isItemEqual(stack, output, true, false)){
						removedRecipes.add((AlloySmelterRecipe) recipeType);
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
				for(AlloySmelterRecipe recipe : removedRecipes){
					if(recipe!=null){
						RecipeHandler.addRecipe(recipe);
					}
				}
			}

		}
		@Override
		public String describe()
		{
			return "Removing Alloy Smelter Recipe for " + output.getDisplayName();
		}
		@Override
		public String describeUndo()
		{
			return "Re-Adding Alloy Smelter Recipe for " + output.getDisplayName();
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
