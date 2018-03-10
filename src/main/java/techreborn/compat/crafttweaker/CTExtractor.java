package techreborn.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.Reference;
import techreborn.api.recipe.machines.ExtractorRecipe;

/**
 * mods.techreborn.extractor.addRecipe(<minecraft:gold_ingot>, <minecraft:iron_ingot>, 20, 100);
 */
@ZenClass("mods.techreborn.extractor")
public class CTExtractor extends CTGeneric {
	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient input1, int ticktime, int euTick) {
		Object oInput1 = CraftTweakerCompat.toObject(input1);

		ExtractorRecipe r = new ExtractorRecipe(oInput1, CraftTweakerCompat.toStack(output), ticktime, euTick);

		addRecipe(r);
	}

	@ZenMethod
	public static void removeInputRecipe(IIngredient iIngredient) {
		CraftTweakerAPI.apply(new RemoveInput(iIngredient, getMachineName()));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		CraftTweakerAPI.apply(new Remove(CraftTweakerCompat.toStack(output), getMachineName()));
	}

	@ZenMethod
	public static void removeAll(){
		CraftTweakerAPI.apply(new RemoveAll(getMachineName()));
	}

	public static String getMachineName() {
		return Reference.EXTRACTOR_RECIPE;
	}

}
