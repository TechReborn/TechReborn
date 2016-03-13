package techreborn.compat.jei.rollingMachine;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import techreborn.client.gui.GuiRollingMachine;
import techreborn.compat.jei.RecipeCategoryUids;

import javax.annotation.Nonnull;

public class RollingMachineRecipeCategory extends BlankRecipeCategory {
	private static final int[] INPUT_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	private static final int[] OUTPUT_SLOTS = {10};

	private final IDrawable background;
	private final IDrawableAnimated progress;
	private final ICraftingGridHelper craftingGridHelper;
	private final String title;

	public RollingMachineRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiRollingMachine.texture, 29, 16, 116, 54);
		title = StatCollector.translateToLocal("tile.techreborn.rollingmachine.name");

		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiRollingMachine.texture, 176, 14, 20, 18);
		progress = guiHelper.createAnimatedDrawable(progressStatic, 250, IDrawableAnimated.StartDirection.LEFT, false);

		craftingGridHelper = guiHelper.createCraftingGridHelper(INPUT_SLOTS[0], OUTPUT_SLOTS[0]);
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.ROLLING_MACHINE;
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
		progress.draw(minecraft, 62, 18);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		for (int l = 0; l < 3; l++) {
			for (int k1 = 0; k1 < 3; k1++) {
				int i = k1 + l * 3;
				guiItemStacks.init(INPUT_SLOTS[i], true, k1 * 18, l * 18);
			}
		}

		guiItemStacks.init(OUTPUT_SLOTS[0], false, 94, 18);

		if (recipeWrapper instanceof RollingMachineRecipeWrapper) {
			RollingMachineRecipeWrapper recipe = (RollingMachineRecipeWrapper) recipeWrapper;
			craftingGridHelper.setInput(guiItemStacks, recipe.getInputs());
			craftingGridHelper.setOutput(guiItemStacks, recipe.getOutputs());
		}
	}
}
