package techreborn.compat.jei.wiremill;

import net.minecraft.client.Minecraft;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import reborncore.api.praescriptum.recipes.Recipe;
import reborncore.client.guibuilder.GuiBuilder;
import reborncore.common.powerSystem.PowerSystem;

public class WireMillRecipeWrapper extends RecipeWrapper {
	public WireMillRecipeWrapper(IJeiHelpers jeiHelpers, Recipe recipe) {
		super(recipe);

		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		GuiBuilder.ProgressDirection right = GuiBuilder.ProgressDirection.RIGHT;
		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiBuilder.defaultTextureSheet, right.xActive,
			right.yActive, right.width, right.height);

		int ticksPerCycle = recipe.getOperationDuration(); // speed up the animation
		this.progress = guiHelper.createAnimatedDrawable(progressStatic, ticksPerCycle,
			IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
		progress.draw(minecraft, 24, 12);

		int y = 31;
		int lineHeight = minecraft.fontRenderer.FONT_HEIGHT;

		minecraft.fontRenderer.drawString(recipe.getOperationDuration() / 20 + " seconds",
			(recipeWidth / 2 - minecraft.fontRenderer.getStringWidth(recipe.getOperationDuration() / 20 + " seconds") / 2), y, 0x444444);
		minecraft.fontRenderer.drawString(PowerSystem.getLocaliszedPowerFormatted(recipe.getEnergyCostPerTick() * recipe.getOperationDuration()),
			(recipeWidth / 2 - minecraft.fontRenderer.getStringWidth(PowerSystem.getLocaliszedPowerFormatted(recipe.getEnergyCostPerTick() * recipe.getOperationDuration())) / 2),
			y + lineHeight + 1, 0x444444);
	}
	
	// Fields >>
	private final IDrawableAnimated progress;
	// << Fields
}