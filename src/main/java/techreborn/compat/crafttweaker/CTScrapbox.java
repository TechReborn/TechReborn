package techreborn.compat.crafttweaker;

import minetweaker.api.item.IIngredient;
import minetweaker.api.minecraft.MineTweakerMC;
import reborncore.api.recipe.RecipeHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.ScrapboxList;
import techreborn.api.recipe.ScrapboxRecipe;

/**
 * Created by Mark on 02/06/2017.
 */
@ZenClass("mods.techreborn.scrapbox")
public class CTScrapbox {

	@ZenMethod
	public static void addScrapboxDrop(IIngredient input) {
		ScrapboxList.stacks.add(MineTweakerMC.getItemStack(input));
		RecipeHandler.addRecipe(new ScrapboxRecipe(MineTweakerMC.getItemStack(input)));
	}

}
