package techreborn.client.compat.jei.recipe.category;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import net.minecraft.network.chat.Component;
import reborncore.common.crafting.RebornRecipe;
import techreborn.client.compat.jei.gui.render.EnergyDisplayDrawable;

public abstract class AbstractRebornEnergyRecipeCategory<R extends RebornRecipe> extends AbstractRebornRecipeCategory<R> {

	public AbstractRebornEnergyRecipeCategory(IRecipeHolderType<R> recipeType) {
		super(recipeType);
	}

	public AbstractRebornEnergyRecipeCategory(IRecipeHolderType<R> recipeType, Component title) {
		super(recipeType, title);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, R recipe, IFocusGroup focuses) {
		builder.addDrawable(new EnergyDisplayDrawable(recipe), 3, 3);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, R recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		// TODO 26.1 can we just use drawMultiEnergyBar in EnergyDisplayDrawable?
//		if(EnergyDisplayDrawable.isMouseOver(3, 3, mouseX, mouseY)) {
//			tooltip.addAll(List.of(
//					Component.translatable("techreborn.jei.recipe.energy"),
//					Component.translatable("techreborn.jei.recipe.running.cost", "E", recipe.power()).withStyle(ChatFormatting.GRAY),
//					Component.translatable("techreborn.jei.recipe.generator.total", recipe.power() * recipe.time()).withStyle(ChatFormatting.GRAY),
//					Component.literal(jeiHelpers().getModIdHelper().getFormattedModNameForModId("techreborn"))));
//		}
	}
}
