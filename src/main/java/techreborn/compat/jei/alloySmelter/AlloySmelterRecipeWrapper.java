package techreborn.compat.jei.alloySmelter;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.client.gui.GuiAlloySmelter;
import techreborn.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;

public class AlloySmelterRecipeWrapper extends BaseRecipeWrapper<AlloySmelterRecipe> {
	private final IDrawableAnimated arrow;

	public AlloySmelterRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			AlloySmelterRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic arrowStatic = guiHelper.createDrawable(GuiAlloySmelter.texture, 176, 14, 24, 17);
		this.arrow = guiHelper.createAnimatedDrawable(arrowStatic, baseRecipe.tickTime(),
			IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void drawAnimations(
		@Nonnull
			Minecraft minecraft, int recipeWidth, int recipeHeight) {
		super.drawAnimations(minecraft, recipeWidth, recipeHeight);
		arrow.draw(minecraft, 33, 19);

		int x = recipeWidth / 2;
		int y = recipeHeight - recipeHeight / 4;
		int lineHeight = minecraft.fontRendererObj.FONT_HEIGHT;

		minecraft.fontRendererObj.drawString("Time: " + (baseRecipe.tickTime / 20) + " secs", x, y, 0x444444);
		minecraft.fontRendererObj.drawString("EU: " + baseRecipe.euPerTick + " EU/t", x, y += lineHeight, 0x444444);

	}
}
