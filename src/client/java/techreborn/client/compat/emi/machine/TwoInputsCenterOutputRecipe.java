package techreborn.client.compat.emi.machine;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.RecipeEntry;
import reborncore.client.gui.GuiBuilder;
import reborncore.common.crafting.RebornRecipe;
import techreborn.client.compat.emi.RenderHelper;

public class TwoInputsCenterOutputRecipe<R extends RebornRecipe> extends AbstractEnergyConsumingMachineRecipe<R> {
	private final EmiRecipeCategory category;

	public TwoInputsCenterOutputRecipe(RecipeEntry<R> rebornRecipeType, EmiRecipeCategory category) {
		super(rebornRecipeType);
		this.category = category;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return category;
	}

	@Override
	public int getDisplayWidth() {
		return 124;
	}

	@Override
	public int getDisplayHeight() {
		return 50;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addSlot(getInput(0), 16, 32 / 2);
		widgets.addSlot(getOutput(0), 58, 24 / 2).large(true).recipeContext(this);
		widgets.addSlot(getInput(1), 108, 32 / 2);

		RenderHelper.createEnergyDisplay(widgets, 0,0, getEnergy(),5000);
		RenderHelper.createProgressBar(widgets, 38, 20, getTime() * 50, GuiBuilder.ProgressDirection.RIGHT);
		RenderHelper.createProgressBar(widgets, 88, 20, getTime() * 50, GuiBuilder.ProgressDirection.LEFT);
	}
}