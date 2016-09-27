package techreborn.compat.jei.extractor;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.text.translation.I18n;
import techreborn.client.gui.GuiExtractor;
import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.compat.jei.RecipeUtil;

public class ExtractorRecipeCategory extends BlankRecipeCategory<ExtractorRecipeWrapper>
{
	private static final int[] INPUT_SLOTS = { 0 };
	private static final int[] OUTPUT_SLOTS = { 1 };

	private final IDrawable background;
	private final String title;

	public ExtractorRecipeCategory(IGuiHelper guiHelper)
	{
		background = guiHelper.createDrawable(GuiExtractor.texture, 55, 30, 82, 26);
		title = I18n.translateToLocal("tile.techreborn.extractor.name");
	}

	@Nonnull
	@Override
	public String getUid()
	{
		return RecipeCategoryUids.EXTRACTOR;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		return title;
	}

	@Nonnull
	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull ExtractorRecipeWrapper recipeWrapper)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 0, 3);

		guiItemStacks.init(OUTPUT_SLOTS[0], false, 60, 4);

		RecipeUtil.setRecipeItems(recipeLayout, recipeWrapper, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull ExtractorRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 0, 3);

		guiItemStacks.init(OUTPUT_SLOTS[0], false, 60, 4);

		RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
	}
}
