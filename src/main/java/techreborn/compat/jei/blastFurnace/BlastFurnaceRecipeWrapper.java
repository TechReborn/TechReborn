package techreborn.compat.jei.blastFurnace;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import techreborn.api.recipe.machines.BlastFurnaceRecipe;
import techreborn.client.gui.GuiBlastFurnace;
import techreborn.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;

public class BlastFurnaceRecipeWrapper extends BaseRecipeWrapper<BlastFurnaceRecipe> {
	private final IDrawableAnimated progress;

	public BlastFurnaceRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			BlastFurnaceRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiBlastFurnace.texture, 176, 14, 20, 11);
		this.progress = guiHelper.createAnimatedDrawable(progressStatic, baseRecipe.tickTime(),
			IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void drawAnimations(
		@Nonnull
			Minecraft minecraft, int recipeWidth, int recipeHeight) {
		super.drawAnimations(minecraft, recipeWidth, recipeHeight);
		progress.draw(minecraft, 54 - 29, 13);

		int x = recipeWidth / 3;
		int y = (int) (recipeHeight - recipeHeight / 2.2F);
		int lineHeight = minecraft.fontRendererObj.FONT_HEIGHT;

		minecraft.fontRendererObj.drawString("Time: " + baseRecipe.tickTime / 20 + " secs", x, y, 0x444444);
		minecraft.fontRendererObj.drawString("EU: " + baseRecipe.euPerTick + " EU/t", x, y += lineHeight, 0x444444);
		minecraft.fontRendererObj.drawString("Heat capacity: " + baseRecipe.neededHeat, x, y += lineHeight, 0x444444);
	}
}
