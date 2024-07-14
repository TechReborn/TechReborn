package techreborn.client.compat.emi.machine;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.RecipeEntry;
import reborncore.common.crafting.RebornRecipe;

public class OneInputOneOutputRecipe<R extends RebornRecipe> extends AbstractEnergyConsumingMachineRecipe<R> {
	private final EmiRecipeCategory category;

	public OneInputOneOutputRecipe(RecipeEntry<R> rebornRecipeType, EmiRecipeCategory category) {
		super(rebornRecipeType);
		this.category = category;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return category;
	}

	@Override
	public int getDisplayWidth() {
		return 84;
	}

	@Override
	public int getDisplayHeight() {
		return 50;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addSlot(getInput(0), 16, (50 - 18) / 2);
		widgets.addSlot(getOutput(0), 16 + 18 + 24, (50 - 26) / 2).large(true).recipeContext(this);
	}
}
