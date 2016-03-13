package techreborn.compat.jei.implosionCompressor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import techreborn.client.gui.GuiImplosionCompressor;
import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.compat.jei.RecipeUtil;

import javax.annotation.Nonnull;

public class ImplosionCompressorRecipeCategory extends BlankRecipeCategory {
	private static final int[] INPUT_SLOTS = {0, 1};
	private static final int[] OUTPUT_SLOTS = {2, 3};

	private final IDrawable background;
	private final IDrawable electricity;
	private final String title;

	public ImplosionCompressorRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiImplosionCompressor.texture, 16, 25, 116, 36);
		IDrawableStatic electricityDrawable = guiHelper.createDrawable(GuiImplosionCompressor.texture, 176, 0, 14, 14);
		electricity = guiHelper.createAnimatedDrawable(electricityDrawable, 300, IDrawableAnimated.StartDirection.TOP, true);
		title = StatCollector.translateToLocal("tile.techreborn.implosioncompressor.name");
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
	public void drawAnimations(@Nonnull Minecraft minecraft) {
		electricity.draw(minecraft, 0, 12);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 20, 0);
		guiItemStacks.init(INPUT_SLOTS[1], true, 20, 18);

		guiItemStacks.init(OUTPUT_SLOTS[0], false, 76, 9);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 94, 9);

		if (recipeWrapper instanceof ImplosionCompressorRecipeWrapper) {
			ImplosionCompressorRecipeWrapper recipe = (ImplosionCompressorRecipeWrapper) recipeWrapper;
			RecipeUtil.setRecipeItems(recipeLayout, recipe, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
		}
	}
}
