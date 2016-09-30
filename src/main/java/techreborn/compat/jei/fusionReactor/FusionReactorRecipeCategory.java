package techreborn.compat.jei.fusionReactor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import techreborn.client.gui.GuiFusionReactor;
import techreborn.compat.jei.RecipeCategoryUids;

import javax.annotation.Nonnull;

public class FusionReactorRecipeCategory extends BlankRecipeCategory<FusionReactorRecipeWrapper> {

	private static final int inputSlotTop = 0;
	private static final int inputSlotBottom = 1;
	private static final int outputSlot = 2;

	@Nonnull
	private final IDrawable background;
	@Nonnull
	private final String title;

	public FusionReactorRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiFusionReactor.texture, 86, 16, 85, 64, 0, 40, 20, 20);
		title = I18n.translateToLocal("tile.techreborn.fusioncontrolcomputer.name");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.FUSION_REACTOR;
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
			FusionReactorRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		itemStacks.init(inputSlotTop, true, 21, 0);
		itemStacks.init(inputSlotBottom, true, 21, 36);
		itemStacks.init(outputSlot, false, 81, 18);

		itemStacks.set(inputSlotTop, recipeWrapper.getTopInput());
		itemStacks.set(inputSlotBottom, recipeWrapper.getBottomInput());
		itemStacks.set(outputSlot, recipeWrapper.getOutputs());
	}

	@Override
	public void setRecipe(
		@Nonnull
			IRecipeLayout recipeLayout,
		@Nonnull
			FusionReactorRecipeWrapper recipeWrapper,
		@Nonnull
			IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		itemStacks.init(inputSlotTop, true, 21, 0);
		itemStacks.init(inputSlotBottom, true, 21, 36);
		itemStacks.init(outputSlot, false, 81, 18);

		itemStacks.set(inputSlotTop, recipeWrapper.getTopInput());
		itemStacks.set(inputSlotBottom, recipeWrapper.getBottomInput());
		itemStacks.set(outputSlot, ingredients.getOutputs(ItemStack.class));
	}

}
