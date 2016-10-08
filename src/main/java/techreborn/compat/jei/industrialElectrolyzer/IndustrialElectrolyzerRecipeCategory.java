package techreborn.compat.jei.industrialElectrolyzer;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.util.text.translation.I18n;
import techreborn.client.gui.GuiIndustrialElectrolyzer;
import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.compat.jei.RecipeUtil;

import javax.annotation.Nonnull;

public class IndustrialElectrolyzerRecipeCategory extends BlankRecipeCategory<IndustrialElectrolyzerRecipeWrapper> {
	private static final int[] INPUT_SLOTS = { 0, 1 };
	private static final int[] OUTPUT_SLOTS = { 2, 3, 4, 5 };

	private final IDrawable background;
	private final String title;

	public IndustrialElectrolyzerRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiIndustrialElectrolyzer.texture, 49, 18, 78, 50);
		title = I18n.translateToLocal("tile.techreborn.industrialelectrolyzer.name");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return title;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(
		@Nonnull
			IRecipeLayout recipeLayout,
		@Nonnull
			IndustrialElectrolyzerRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 30, 32);
		guiItemStacks.init(INPUT_SLOTS[1], true, 0, 32);
		guiItemStacks.init(OUTPUT_SLOTS[0], false, 0, 0);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 20, 0);
		guiItemStacks.init(OUTPUT_SLOTS[2], false, 40, 0);
		guiItemStacks.init(OUTPUT_SLOTS[3], false, 60, 0);

		RecipeUtil.setRecipeItems(recipeLayout, recipeWrapper, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
	}

	@Override
	public void setRecipe(
		@Nonnull
			IRecipeLayout recipeLayout,
		@Nonnull
			IndustrialElectrolyzerRecipeWrapper recipeWrapper,
		@Nonnull
			IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 30, 32);
		guiItemStacks.init(INPUT_SLOTS[1], true, 0, 32);
		guiItemStacks.init(OUTPUT_SLOTS[0], false, 0, 0);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 20, 0);
		guiItemStacks.init(OUTPUT_SLOTS[2], false, 40, 0);
		guiItemStacks.init(OUTPUT_SLOTS[3], false, 60, 0);

		RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
	}
}
