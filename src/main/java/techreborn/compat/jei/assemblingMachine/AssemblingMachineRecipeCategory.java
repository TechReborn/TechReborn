package techreborn.compat.jei.assemblingMachine;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.translation.I18n;
import techreborn.client.gui.GuiAssemblingMachine;
import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.compat.jei.RecipeUtil;

import javax.annotation.Nonnull;

public class AssemblingMachineRecipeCategory extends BlankRecipeCategory<AssemblingMachineRecipeWrapper> {
	private static final int[] INPUT_SLOTS = { 0, 1 };
	private static final int[] OUTPUT_SLOTS = { 2 };

	private final IDrawable background;
	private final IDrawableAnimated electricity;
	private final String title;

	public AssemblingMachineRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiAssemblingMachine.texture, 46, 16, 91, 54);
		IDrawableStatic electricityDrawable = guiHelper.createDrawable(GuiAssemblingMachine.texture, 176, 0, 14, 14);
		electricity = guiHelper.createAnimatedDrawable(electricityDrawable, 300, IDrawableAnimated.StartDirection.TOP,
			true);
		title = I18n.translateToLocal("tile.techreborn.assemblingmachine.name");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.ASSEMBLING_MACHINE;
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
		electricity.draw(minecraft, 10, 20);
	}

	@Override
	public void setRecipe(
		@Nonnull
			IRecipeLayout recipeLayout,
		@Nonnull
			AssemblingMachineRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 0, 0);
		guiItemStacks.init(INPUT_SLOTS[1], true, 18, 0);
		guiItemStacks.init(OUTPUT_SLOTS[0], false, 69, 18);

		RecipeUtil.setRecipeItems(recipeLayout, recipeWrapper, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
	}

	@Override
	public void setRecipe(
		@Nonnull
			IRecipeLayout recipeLayout,
		@Nonnull
			AssemblingMachineRecipeWrapper recipeWrapper,
		@Nonnull
			IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 0, 0);
		guiItemStacks.init(INPUT_SLOTS[1], true, 18, 0);
		guiItemStacks.init(OUTPUT_SLOTS[0], false, 69, 18);

		RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, OUTPUT_SLOTS, null, null);
	}
}
