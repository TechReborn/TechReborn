package techreborn.compat.jei.industrialGrinder;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import techreborn.client.gui.GuiIndustrialGrinder;
import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.compat.jei.RecipeUtil;
import techreborn.tiles.multiblock.TileIndustrialGrinder;

import javax.annotation.Nonnull;

public class IndustrialGrinderRecipeCategory extends BlankRecipeCategory<IndustrialGrinderRecipeWrapper> {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
		"textures/gui/industrial_grinder.png");
	private static final int[] INPUT_SLOTS = { 0, 1 };
	private static final int[] OUTPUT_SLOTS = { 2, 3, 4, 5 };
	private static final int[] INPUT_TANKS = { 0 };
	private final IDrawable background;
	private final IDrawable blankArea; // for covering the lightning power
	// symbol
	private final IDrawable tankOverlay;
	private final String title;

	public IndustrialGrinderRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(texture, 7, 15, 141, 55);
		blankArea = guiHelper.createDrawable(texture, 50, 45, 6, 6);
		tankOverlay = guiHelper.createDrawable(texture, 176, 86, 12, 47);
		title = I18n.translateToLocal("tile.techreborn.industrialgrinder.name");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.INDUSTRIAL_GRINDER;
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
	public void drawExtras(
		@Nonnull
			Minecraft minecraft) {
		blankArea.draw(minecraft, 129, 49);
	}

	@Override
	public void setRecipe(
		@Nonnull
			IRecipeLayout recipeLayout,
		@Nonnull
			IndustrialGrinderRecipeWrapper recipeWrapper,
		@Nonnull
			IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 24, 10);
		guiItemStacks.init(INPUT_SLOTS[1], true, 24, 28);

		guiItemStacks.init(OUTPUT_SLOTS[0], false, 69, 19);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 87, 19);
		guiItemStacks.init(OUTPUT_SLOTS[2], false, 105, 19);
		guiItemStacks.init(OUTPUT_SLOTS[3], false, 123, 19);

		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		guiFluidStacks.init(INPUT_TANKS[0], true, 4, 4, 12, 47, TileIndustrialGrinder.TANK_CAPACITY, true, tankOverlay);

		RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, OUTPUT_SLOTS, INPUT_TANKS, null);
	}
}
