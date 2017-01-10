package techreborn.compat.jei.chemicalReactor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.api.recipe.machines.ChemicalReactorRecipe;
import techreborn.client.gui.TRBuilder;
import techreborn.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;

public class ChemicalReactorRecipeWrapper extends BaseRecipeWrapper<ChemicalReactorRecipe> {
	private final IDrawableAnimated progressright;
	private final IDrawableAnimated progressleft;

	public ChemicalReactorRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			ChemicalReactorRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressrightStatic = guiHelper.createDrawable(TRBuilder.GUI_SHEET, 100, 151, 16, 10);
		IDrawableStatic progressleftStatic = guiHelper.createDrawable(TRBuilder.GUI_SHEET, 84, 161, 16, 10);

		int ticksPerCycle = baseRecipe.tickTime(); // speed up the animation

		this.progressright = guiHelper.createAnimatedDrawable(progressrightStatic, ticksPerCycle, IDrawableAnimated.StartDirection.LEFT, false);
		this.progressleft = guiHelper.createAnimatedDrawable(progressleftStatic, ticksPerCycle, IDrawableAnimated.StartDirection.RIGHT, false);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
		progressright.draw(minecraft, 25, 12);
		progressleft.draw(minecraft, 75, 12);

		int y = 30  ;
		int lineHeight = minecraft.fontRendererObj.FONT_HEIGHT;

		minecraft.fontRendererObj.drawString(baseRecipe.tickTime / 20 + " seconds", (recipeWidth / 2 - minecraft.fontRendererObj.getStringWidth(baseRecipe.tickTime / 20 + " seconds") / 2), y, 0x444444);
		minecraft.fontRendererObj.drawString(PowerSystem.getLocaliszedPowerFormatted(baseRecipe.euPerTick * baseRecipe.tickTime), (recipeWidth / 2 - minecraft.fontRendererObj.getStringWidth(PowerSystem.getLocaliszedPowerFormatted(baseRecipe.euPerTick * baseRecipe.tickTime)) / 2), y + lineHeight, 0x444444);
	}
}
