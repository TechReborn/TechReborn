package techreborn.compat.jei.implosionCompressor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.translation.I18n;
import techreborn.client.gui.GuiImplosionCompressor;
import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.compat.jei.RecipeUtil;

import javax.annotation.Nonnull;

public class ImplosionCompressorRecipeCategory extends BlankRecipeCategory<ImplosionCompressorRecipeWrapper> {
	private static final int[] INPUT_SLOTS = { 0, 1 };
	private static final int[] OUTPUT_SLOTS = { 2, 3 };

	private final IDrawable background;
	private final IDrawable electricity;
	private final String title;

	public ImplosionCompressorRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiImplosionCompressor.texture, 16, 25, 116, 36);
		IDrawableStatic electricityDrawable = guiHelper.createDrawable(GuiImplosionCompressor.texture, 176, 0, 14, 14);
		electricity = guiHelper.createAnimatedDrawable(electricityDrawable, 300, IDrawableAnimated.StartDirection.TOP,
			true);
		title = I18n.translateToLocal("tile.techreborn.implosioncompressor.name");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.IMPLOSION_COMPRESSOR;
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
	public void drawAnimations(
		@Nonnull
			Minecraft minecraft) {
		electricity.draw(minecraft, 0, 12);
	}

	@Override
	public void setRecipe(
		@Nonnull
			IRecipeLayout recipeLayout,
		@Nonnull
			ImplosionCompressorRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 20, 0);
		guiItemStacks.init(INPUT_SLOTS[1], true, 20, 18);

		guiItemStacks.init(OUTPUT_SLOTS[0], false, 76, 9);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 94, 9);

		RecipeUtil.setRecipeItems(recipeLayout, recipeWrapper, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
	}

	@Override
	public void setRecipe(
		@Nonnull
			IRecipeLayout recipeLayout,
		@Nonnull
			ImplosionCompressorRecipeWrapper recipeWrapper,
		@Nonnull
			IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 20, 0);
		guiItemStacks.init(INPUT_SLOTS[1], true, 20, 18);

		guiItemStacks.init(OUTPUT_SLOTS[0], false, 76, 9);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 94, 9);

		RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
	}
}
