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

public class TwoItemToFourItemRecipeCategory<R extends RebornRecipe> extends AbstractRebornEnergyRecipeCategory<R> {

	public TwoItemToFourItemRecipeCategory(IRecipeHolderType<R> recipeType) {
		super(recipeType);
	}

	public TwoItemToFourItemRecipeCategory(IRecipeHolderType<R> recipeType, Component title) {
		super(recipeType, title);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, R recipe, IFocusGroup focuses) {
		addItem(builder, RecipeIngredientRole.INPUT, 30, 36, getInput(recipe, 0), SLOT);
		addItem(builder, RecipeIngredientRole.INPUT, 50, 36, getInput(recipe, 1), SLOT);
		addItem(builder, RecipeIngredientRole.OUTPUT, 38, 9, getOutput(recipe, 0), OutputSlotDrawable.LEFT);
		addItem(builder, RecipeIngredientRole.OUTPUT, 58, 9, getOutput(recipe, 1), OutputSlotDrawable.CENTER);
		addItem(builder, RecipeIngredientRole.OUTPUT, 78, 9, getOutput(recipe, 2), OutputSlotDrawable.CENTER);
		addItem(builder, RecipeIngredientRole.OUTPUT, 98, 9, getOutput(recipe, 3), OutputSlotDrawable.RIGHT);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, R recipe, IFocusGroup focuses) {
		super.createRecipeExtras(builder, recipe, focuses);
		builder.addDrawable(ProgressBarDrawable.up(recipe), 70, 35);
	}

	@Override
	public void draw(R recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
		Font font = font();
		Component component = getTimeComponent(recipe);
		guiGraphics.text(font, component, getWidth() - font.width(component), getHeight() - font.lineHeight, 0xFF808080, false);
	}
}
