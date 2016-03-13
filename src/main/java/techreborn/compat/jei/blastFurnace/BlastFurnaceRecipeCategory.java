package techreborn.compat.jei.blastFurnace;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.StatCollector;
import techreborn.client.gui.GuiBlastFurnace;
import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.compat.jei.RecipeUtil;

import javax.annotation.Nonnull;

public class BlastFurnaceRecipeCategory extends BlankRecipeCategory {
	private static final int[] INPUT_SLOTS = {0, 1};
	private static final int[] OUTPUT_SLOTS = {2, 3};

	private final IDrawable background;
	private final String title;

	public BlastFurnaceRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiBlastFurnace.texture, 39, 24, 100, 36);
		title = StatCollector.translateToLocal("tile.techreborn.blastfurnace.name");
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
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 0, 0);
		guiItemStacks.init(INPUT_SLOTS[1], true, 0, 18);
		guiItemStacks.init(OUTPUT_SLOTS[0], false, 60, 10);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 78, 10);

		if (recipeWrapper instanceof BlastFurnaceRecipeWrapper) {
			BlastFurnaceRecipeWrapper recipe = (BlastFurnaceRecipeWrapper) recipeWrapper;
			RecipeUtil.setRecipeItems(recipeLayout, recipe, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
		}
	}
}
