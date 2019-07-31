package techreborn.compat.jei.wiremill;

import net.minecraft.util.ResourceLocation;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import reborncore.common.util.StringUtils;

import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.compat.jei.RecipeUtil;
import techreborn.lib.ModInfo;

import javax.annotation.Nonnull;

public class WireMillRecipeCategory implements IRecipeCategory<RecipeWrapper> {
	public WireMillRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(texture, 0, 172, 74, 52);
		title = StringUtils.t("tile.techreborn.wire_mill.name");
	}

	@Override
	public String getModName() {
		return ModInfo.MOD_NAME;
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.WIRE_MILL;
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
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(INPUT_SLOTS[0], true, 3, 7);
		guiItemStacks.init(OUTPUT_SLOTS[0], false, 49, 7);

		RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
	}

	// Fields >>
	public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/jei.png");

	private static final int[] INPUT_SLOTS = { 0 };
	private static final int[] OUTPUT_SLOTS = { 1 };

	private final IDrawable background;
	private final String title;
	// << Fields
}
