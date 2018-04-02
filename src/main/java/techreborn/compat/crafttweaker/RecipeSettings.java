package techreborn.compat.crafttweaker;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.recipe.BaseRecipe;

@ZenClass("mods.techreborn.RecipeSettings")
public class RecipeSettings {

	private BaseRecipe baseRecipe;

	public RecipeSettings(BaseRecipe baseRecipe) {
		this.baseRecipe = baseRecipe;
	}

	@ZenMethod
	@ZenDocumentation("boolean useOreDict")
	public void setUseOreDict(boolean useOreDict){
		baseRecipe.setOreDict(useOreDict);
	}
}
