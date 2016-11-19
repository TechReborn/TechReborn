package techreborn.compat.jei.blastFurnace;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.util.text.translation.I18n;
import techreborn.client.gui.GuiBlastFurnace;
import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.compat.jei.RecipeUtil;

import javax.annotation.Nonnull;

public class BlastFurnaceRecipeCategory extends BlankRecipeCategory<BlastFurnaceRecipeWrapper> {
	private static final int[] INPUT_SLOTS = { 0, 1 };
	private static final int[] OUTPUT_SLOTS = { 2, 3 };

	private final IDrawable background;
	private final String title;

	public BlastFurnaceRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiBlastFurnace.texture, 39, 24, 90, 60);
		title = I18n.translateToLocal("tile.techreborn.blastfurnace.name");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.BLAST_FURNACE;
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
			BlastFurnaceRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 0, 0);
		guiItemStacks.init(INPUT_SLOTS[1], true, 0, 18);
		guiItemStacks.init(OUTPUT_SLOTS[0], false, 60, 10);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 78, 10);

		RecipeUtil.setRecipeItems(recipeLayout, recipeWrapper, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
	}

	@Override
	public void setRecipe(
		@Nonnull
			IRecipeLayout recipeLayout,
		@Nonnull
			BlastFurnaceRecipeWrapper recipeWrapper,
		@Nonnull
			IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 0, 0);
		guiItemStacks.init(INPUT_SLOTS[1], true, 0, 18);
		guiItemStacks.init(OUTPUT_SLOTS[0], false, 60, 10);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 78, 10);

		RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
	}
}
