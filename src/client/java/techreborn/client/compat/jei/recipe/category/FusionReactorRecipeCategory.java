package techreborn.client.compat.jei.recipe.category;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import techreborn.client.compat.jei.gui.render.EnergyDisplayDrawable;
import techreborn.client.compat.jei.gui.render.ProgressBarDrawable;
import techreborn.recipe.recipes.FusionReactorRecipe;

public class FusionReactorRecipeCategory extends TwoItemToItemCenterRecipeCategory<FusionReactorRecipe> {

	public FusionReactorRecipeCategory(IRecipeHolderType<FusionReactorRecipe> recipeType) {
		super(recipeType);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, FusionReactorRecipe recipe, IFocusGroup focuses) {
		builder.addDrawable(new EnergyDisplayDrawable(recipe), 3, 3);
		builder.addDrawable(ProgressBarDrawable.right(recipe), 45, 23);
		builder.addDrawable(ProgressBarDrawable.left(recipe), 95, 23);
	}

	@Override
	public void draw(FusionReactorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
		Font font = font();
		Component component = getTimeComponent(recipe);
		guiGraphics.text(font, component, getWidth() - font.width(component), 0, 0xFF808080, false);
	}
}
