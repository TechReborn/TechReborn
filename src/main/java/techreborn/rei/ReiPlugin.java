package techreborn.rei;

import me.shedaniel.rei.api.REIPluginEntry;
import me.shedaniel.rei.api.RecipeHelper;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeManager;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ReiPlugin implements REIPluginEntry {

	public static final Identifier PLUGIN = new Identifier("techreborn", "techreborn_plugin");

	public static final Map<RebornRecipeType<?>, ItemConvertible> iconMap = new HashMap<>();

	public ReiPlugin() {
		iconMap.put(ModRecipes.ALLOY_SMELTER, TRContent.Machine.ALLOY_SMELTER);
		iconMap.put(ModRecipes.GRINDER, TRContent.Machine.GRINDER);
		iconMap.put(ModRecipes.BLAST_FURNACE, TRContent.Machine.INDUSTRIAL_BLAST_FURNACE);
		iconMap.put(ModRecipes.CENTRIFUGE, TRContent.Machine.INDUSTRIAL_CENTRIFUGE);
		//TODO add the others here
	}

	@Override
	public Identifier getPluginIdentifier() {
		return PLUGIN;
	}

	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		RecipeManager.getRecipeTypes("techreborn").forEach(rebornRecipeType -> recipeHelper.registerCategory(new MachineRecipeCategory(rebornRecipeType)));
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		RecipeManager.getRecipeTypes("techreborn").forEach(rebornRecipeType -> registerMachineRecipe(recipeHelper, rebornRecipeType));
	}
	
	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(ModRecipes.ALLOY_SMELTER.getName(), new ItemStack[]{new ItemStack(TRContent.Machine.ALLOY_SMELTER.asItem())});
	}

	private <R extends RebornRecipe> void registerMachineRecipe(RecipeHelper recipeHelper, RebornRecipeType<R> recipeType){
		recipeHelper.registerRecipes(recipeType.getName(), (Predicate<Recipe>) recipe -> {
			if (recipe instanceof RebornRecipe) {
				return ((RebornRecipe) recipe).getRebornRecipeType() == recipeType;
			}
			return false;
		}, recipe -> new MachineRecipeDisplay<RebornRecipe>((RebornRecipe) recipe));
	}
}
