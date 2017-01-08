package techreborn.compat.jei.centrifuge;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import techreborn.api.recipe.machines.CentrifugeRecipe;
import techreborn.client.gui.TRBuilder;
import techreborn.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;

public class CentrifugeRecipeWrapper extends BaseRecipeWrapper<CentrifugeRecipe> {
	private final IDrawableAnimated progressRight;

	public CentrifugeRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			CentrifugeRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(TRBuilder.GUI_SHEET, 100, 151, 16, 10);

		int ticksPerCycle = baseRecipe.tickTime() / 4; // speed up the animation

		this.progressRight = guiHelper.createAnimatedDrawable(progressStatic, ticksPerCycle,
			IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
		progressRight.draw(minecraft, 25, 26);

		int x = -10;
		int y1 = 3;
		int y2 = 52;
		int lineHeight = minecraft.fontRendererObj.FONT_HEIGHT;

		minecraft.fontRendererObj.drawString("Time: " + baseRecipe.tickTime / 20 + " secs", (recipeWidth / 2 - minecraft.fontRendererObj.getStringWidth("Time: " + baseRecipe.tickTime / 20 + " secs") / 2) - 40, y1, 0x444444);
		minecraft.fontRendererObj.drawString("EU: " + baseRecipe.euPerTick + " EU/t", (recipeWidth / 2 - minecraft.fontRendererObj.getStringWidth("EU: " + baseRecipe.euPerTick + " EU/t") / 2) - 40, y2, 0x444444);

	}
}
