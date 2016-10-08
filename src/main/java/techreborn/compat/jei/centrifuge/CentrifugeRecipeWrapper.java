package techreborn.compat.jei.centrifuge;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import techreborn.api.recipe.machines.CentrifugeRecipe;
import techreborn.client.gui.GuiCentrifuge;
import techreborn.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;

public class CentrifugeRecipeWrapper extends BaseRecipeWrapper<CentrifugeRecipe> {
	private final IDrawableAnimated progressUp;
	private final IDrawableAnimated progressLeft;
	private final IDrawableAnimated progressDown;
	private final IDrawableAnimated progressRight;

	public CentrifugeRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			CentrifugeRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressUpStatic = guiHelper.createDrawable(GuiCentrifuge.texture, 176, 14, 12, 12);
		IDrawableStatic progressLeftStatic = guiHelper.createDrawable(GuiCentrifuge.texture, 176, 26, 12, 12);
		IDrawableStatic progressDownStatic = guiHelper.createDrawable(GuiCentrifuge.texture, 176, 38, 12, 12);
		IDrawableStatic progressRightStatic = guiHelper.createDrawable(GuiCentrifuge.texture, 176, 50, 12, 12);

		int ticksPerCycle = baseRecipe.tickTime() / 4; // speed up the animation
		// a bit
		this.progressUp = guiHelper.createAnimatedDrawable(progressUpStatic, ticksPerCycle,
			IDrawableAnimated.StartDirection.BOTTOM, false);
		this.progressLeft = guiHelper.createAnimatedDrawable(progressLeftStatic, ticksPerCycle,
			IDrawableAnimated.StartDirection.RIGHT, false);
		this.progressDown = guiHelper.createAnimatedDrawable(progressDownStatic, ticksPerCycle,
			IDrawableAnimated.StartDirection.TOP, false);
		this.progressRight = guiHelper.createAnimatedDrawable(progressRightStatic, ticksPerCycle,
			IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void drawAnimations(
		@Nonnull
			Minecraft minecraft, int recipeWidth, int recipeHeight) {
		super.drawAnimations(minecraft, recipeWidth, recipeHeight);
		progressUp.draw(minecraft, 33, 18);
		progressLeft.draw(minecraft, 18, 33);
		progressDown.draw(minecraft, 33, 48);
		progressRight.draw(minecraft, 48, 33);

		int x = -45;
		int y = 60;
		int lineHeight = minecraft.fontRendererObj.FONT_HEIGHT;

		minecraft.fontRendererObj.drawString("Time: " + baseRecipe.tickTime / 20 + " secs", x, y, 0x444444);
		minecraft.fontRendererObj.drawString("EU: " + baseRecipe.euPerTick + " EU/t", x, y += lineHeight, 0x444444);

	}
}
