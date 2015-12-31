package techreborn.compat.jei.implosionCompressor;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import techreborn.api.recipe.machines.ImplosionCompressorRecipe;
import techreborn.client.gui.GuiImplosionCompressor;
import techreborn.compat.jei.BaseRecipeWrapper;
import techreborn.compat.jei.TechRebornJeiPlugin;

public class ImplosionCompressorRecipeWrapper extends BaseRecipeWrapper<ImplosionCompressorRecipe> {
	private final IDrawableAnimated progress;

	public ImplosionCompressorRecipeWrapper(ImplosionCompressorRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = TechRebornJeiPlugin.jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiImplosionCompressor.texture, 176, 14, 21, 11);
		this.progress = guiHelper.createAnimatedDrawable(progressStatic, baseRecipe.tickTime(), IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
		super.drawAnimations(minecraft, recipeWidth, recipeHeight);
		progress.draw(minecraft, 44, 13);
	}
}
