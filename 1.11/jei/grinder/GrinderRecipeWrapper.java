package techreborn.compat.jei.grinder;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.client.gui.GuiGrinder;
import techreborn.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;

public class GrinderRecipeWrapper extends BaseRecipeWrapper<GrinderRecipe> {
	private final IDrawableAnimated progress;

	public GrinderRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			GrinderRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiGrinder.texture, 176, 14, 20, 11);

		int ticksPerCycle = baseRecipe.tickTime();
		this.progress = guiHelper.createAnimatedDrawable(progressStatic, ticksPerCycle,
			IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void drawAnimations(
		@Nonnull
			Minecraft minecraft, int recipeWidth, int recipeHeight) {
		super.drawAnimations(minecraft, recipeWidth, recipeHeight);
		progress.draw(minecraft, 25, 7);

		int x = -45;
		int y = 4;
		int lineHeight = minecraft.fontRendererObj.FONT_HEIGHT;

		minecraft.fontRendererObj.drawString("Time: " + baseRecipe.tickTime / 20 + " s", x, y, 0x444444);
		minecraft.fontRendererObj.drawString("EU: " + baseRecipe.euPerTick + " EU/t", x, y += lineHeight, 0x444444);
	}
}
