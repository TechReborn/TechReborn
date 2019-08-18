package techreborn.compat.rei.rollingmachine;

import me.shedaniel.rei.plugin.crafting.DefaultShapedDisplay;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import techreborn.init.ModRecipes;

public class RollingMachineDisplay extends DefaultShapedDisplay {

	public RollingMachineDisplay(ShapedRecipe recipe) {
		super(recipe);
	}

	@Override
	public Identifier getRecipeCategory() {
		return ModRecipes.ROLLING_MACHINE.getName();
	}
}
