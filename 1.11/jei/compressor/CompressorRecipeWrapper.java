package techreborn.compat.jei.compressor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import techreborn.api.recipe.machines.CompressorRecipe;
import techreborn.client.gui.GuiCompressor;
import techreborn.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;

public class CompressorRecipeWrapper extends BaseRecipeWrapper<CompressorRecipe> {
	private final IDrawableAnimated progress;

	public CompressorRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			CompressorRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiCompressor.texture, 176, 14, 20, 11);

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
