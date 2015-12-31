package techreborn.compat.jei.alloySmelter;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.client.gui.GuiAlloySmelter;
import techreborn.compat.jei.RecipeCategoryUids;

public class AlloySmelterRecipeCategory implements IRecipeCategory {
	private static final int INPUT_SLOT_0 = 0;
	private static final int INPUT_SLOT_1 = 1;
	private static final int OUTPUT_SLOT = 2;

	private final IDrawable background;
	private final IDrawableAnimated electricity;
	private final String title;

	public AlloySmelterRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiAlloySmelter.texture, 46, 16, 91, 54);
		IDrawableStatic flameDrawable = guiHelper.createDrawable(GuiAlloySmelter.texture, 176, 0, 14, 14);
		electricity = guiHelper.createAnimatedDrawable(flameDrawable, 300, IDrawableAnimated.StartDirection.TOP, true);
		title = StatCollector.translateToLocal("techreborn.jei.category.alloy.furnace");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.ALLOY_SMELTER;
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
	public void drawExtras(Minecraft minecraft) {

	}

	@Override
	public void drawAnimations(Minecraft minecraft) {
		electricity.draw(minecraft, 10, 20);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOT_0, true, 0, 0);
		guiItemStacks.init(INPUT_SLOT_1, true, 18, 0);
		guiItemStacks.init(OUTPUT_SLOT, false, 69, 18);

		if (recipeWrapper instanceof AlloySmelterRecipeWrapper) {
			AlloySmelterRecipeWrapper alloySmelterRecipe = (AlloySmelterRecipeWrapper) recipeWrapper;
			guiItemStacks.set(INPUT_SLOT_0, alloySmelterRecipe.getInputs().get(0));
			guiItemStacks.set(INPUT_SLOT_1, alloySmelterRecipe.getInputs().get(1));
			guiItemStacks.set(OUTPUT_SLOT, alloySmelterRecipe.getOutputs());
		}
	}
}
