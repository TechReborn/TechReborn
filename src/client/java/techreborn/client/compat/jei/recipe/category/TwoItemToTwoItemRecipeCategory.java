package techreborn.client.compat.jei.recipe.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import reborncore.common.crafting.RebornRecipe;
import techreborn.client.compat.jei.gui.render.OutputSlotDrawable;
import techreborn.client.compat.jei.gui.render.ProgressBarDrawable;

public class TwoItemToTwoItemRecipeCategory<R extends RebornRecipe> extends AbstractRebornEnergyRecipeCategory<R> {

	public TwoItemToTwoItemRecipeCategory(IRecipeHolderType<R> recipeType) {
		super(recipeType);
	}

	public TwoItemToTwoItemRecipeCategory(IRecipeHolderType<R> recipeType, Component title) {
		super(recipeType, title);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, R recipe, IFocusGroup focuses) {
		addItem(builder, RecipeIngredientRole.INPUT, 33, 10, getInput(recipe, 0), SLOT);
		addItem(builder, RecipeIngredientRole.INPUT, 33, 30, getInput(recipe, 1), SLOT);
		addItem(builder, RecipeIngredientRole.OUTPUT, 79, 20, getOutput(recipe, 0), OutputSlotDrawable.LEFT);
		addItem(builder, RecipeIngredientRole.OUTPUT, 99, 20, getOutput(recipe, 1), OutputSlotDrawable.RIGHT);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, R recipe, IFocusGroup focuses) {
		super.createRecipeExtras(builder, recipe, focuses);
		builder.addDrawable(ProgressBarDrawable.right(recipe), 54, 23);
	}

	@Override
	public void draw(R recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
		Font font = font();
		Component component = getTimeComponent(recipe);
		guiGraphics.text(font, component, getWidth() - font.width(component), 0, 0xFF808080, false);
	}
}
