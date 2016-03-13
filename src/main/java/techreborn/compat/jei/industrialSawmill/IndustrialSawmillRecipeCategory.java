package techreborn.compat.jei.industrialSawmill;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import techreborn.client.gui.GuiIndustrialSawmill;
import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.compat.jei.RecipeUtil;
import techreborn.tiles.TileIndustrialSawmill;

import javax.annotation.Nonnull;

public class IndustrialSawmillRecipeCategory extends BlankRecipeCategory {
	private static final int[] INPUT_SLOTS = {0, 1};
	private static final int[] OUTPUT_SLOTS = {2, 3, 4};
	private static final int[] INPUT_TANKS = {0};

	private final IDrawable background;
	private final IDrawable blankArea; // for covering the lightning power symbol
	private final IDrawable tankOverlay;
	private final String title;

	public IndustrialSawmillRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiIndustrialSawmill.texture, 7, 15, 141, 55);
		blankArea = guiHelper.createDrawable(GuiIndustrialSawmill.texture, 50, 45, 6, 6);
		tankOverlay = guiHelper.createDrawable(GuiIndustrialSawmill.texture, 176, 83, 12, 47);
		title = StatCollector.translateToLocal("tile.techreborn.industrialsawmill.name");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.INDUSTRIAL_SAWMILL;
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
	public void drawExtras(@Nonnull Minecraft minecraft) {
		blankArea.draw(minecraft, 31, 51);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 24, 10);
		guiItemStacks.init(INPUT_SLOTS[1], true, 24, 28);

		guiItemStacks.init(OUTPUT_SLOTS[0], false, 76, 19);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 94, 19);
		guiItemStacks.init(OUTPUT_SLOTS[2], false, 112, 19);

		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		guiFluidStacks.init(INPUT_TANKS[0], true, 4, 4, 12, 47, TileIndustrialSawmill.TANK_CAPACITY, true, tankOverlay);

		if (recipeWrapper instanceof IndustrialSawmillRecipeWrapper) {
			IndustrialSawmillRecipeWrapper recipe = (IndustrialSawmillRecipeWrapper) recipeWrapper;
			RecipeUtil.setRecipeItems(recipeLayout, recipe, INPUT_SLOTS, OUTPUT_SLOTS, INPUT_TANKS, null);
		}
	}
}
