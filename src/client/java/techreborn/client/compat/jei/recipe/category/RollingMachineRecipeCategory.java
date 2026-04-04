package techreborn.client.compat.jei.recipe.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import techreborn.client.compat.jei.gui.render.OutputSlotDrawable;
import techreborn.client.compat.jei.gui.render.ProgressBarDrawable;
import techreborn.recipe.recipes.RollingMachineRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RollingMachineRecipeCategory extends AbstractRebornEnergyRecipeCategory<RollingMachineRecipe> {

	public RollingMachineRecipeCategory(IRecipeHolderType<RollingMachineRecipe> recipeType) {
		super(recipeType);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RollingMachineRecipe recipe, IFocusGroup focuses) {
		List<IRecipeSlotBuilder> slots = new ArrayList<>(9);
		for(int y = 0; y < 3; ++y) {
			for(int x = 0; x < 3; ++x) {
				slots.add(addItem(builder, RecipeIngredientRole.INPUT, 27 + x * 18, 2 + y * 18, SLOT));
			}
		}
		ShapedRecipe shapedRecipe = recipe.getShapedRecipe();
		int width = shapedRecipe.getWidth();
		int height = shapedRecipe.getHeight();
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				if(y * width + x < shapedRecipe.getIngredients().size()) {
					Optional<Ingredient> ing = shapedRecipe.getIngredients().get(y * width + x);
					if(ing.isPresent()) {
						slots.get(y * 3 + x).add(ing.get());
					}
				}
			}
		}
		addItem(builder, RecipeIngredientRole.OUTPUT, 112, 20, getOutput(recipe, 0), OutputSlotDrawable.SINGLE);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RollingMachineRecipe recipe, IFocusGroup focuses) {
		super.createRecipeExtras(builder, recipe, focuses);
		builder.addDrawable(ProgressBarDrawable.right(recipe), 85, 23);
	}

	@Override
	public void draw(RollingMachineRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
		Font font = font();
		Component component = getTimeComponent(recipe);
		guiGraphics.text(font, component, getWidth() - font.width(component), 0, 0xFF808080, false);
	}
}
