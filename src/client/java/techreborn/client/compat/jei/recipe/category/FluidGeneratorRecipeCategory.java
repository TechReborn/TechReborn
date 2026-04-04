package techreborn.client.compat.jei.recipe.category;

import java.util.List;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.client.compat.jei.gui.render.EnergyDisplayDrawable;
import techreborn.client.compat.jei.gui.render.ProgressBarDrawable;
import techreborn.recipe.recipes.FluidGeneratorRecipe;

public class FluidGeneratorRecipeCategory extends AbstractRebornRecipeCategory<FluidGeneratorRecipe> {

	public FluidGeneratorRecipeCategory(IRecipeHolderType<FluidGeneratorRecipe> recipeType, Component title) {
		super(recipeType, title);
	}

	public FluidGeneratorRecipeCategory(IRecipeHolderType<FluidGeneratorRecipe> recipeType) {
		super(recipeType);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, FluidGeneratorRecipe recipe, IFocusGroup focuses) {
		addFluid(builder, RecipeIngredientRole.INPUT, 11, 3, new FluidInstance(recipe.fluid(), FluidValue.BUCKET));
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, FluidGeneratorRecipe recipe, IFocusGroup focuses) {
		builder.addDrawable(new EnergyDisplayDrawable(recipe), 114, 3);
		builder.addDrawable(ProgressBarDrawable.right(5000), 62, 23);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, FluidGeneratorRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if(EnergyDisplayDrawable.isMouseOver(114, 3, mouseX, mouseY)) {
			tooltip.addAll(List.of(
					Component.translatable("techreborn.jei.recipe.energy"),
					Component.translatable("techreborn.jei.recipe.generator.total", recipe.power() * 1000).withStyle(ChatFormatting.GRAY),
					Component.literal(jeiHelpers().getModIdHelper().getFormattedModNameForModId("techreborn"))));
		}
	}
}
