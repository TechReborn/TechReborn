package techreborn.compat.jei.extractor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import techreborn.api.recipe.machines.ExtractorRecipe;
import techreborn.client.gui.TRBuilder;
import techreborn.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;

public class ExtractorRecipeWrapper extends BaseRecipeWrapper<ExtractorRecipe> {
	private final IDrawableAnimated progress;

	public ExtractorRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			ExtractorRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(TRBuilder.GUI_SHEET, 100, 151, 16, 10);

		int ticksPerCycle = baseRecipe.tickTime(); // speed up the animation

		this.progress = guiHelper.createAnimatedDrawable(progressStatic, ticksPerCycle,
			IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
		progress.draw(minecraft, 25, 11);
	}
}
