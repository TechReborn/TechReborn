package techreborn.compat.jei.vacuumFreezer;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;
import techreborn.client.gui.GuiVacuumFreezer;
import techreborn.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;

public class VacuumFreezerRecipeWrapper extends BaseRecipeWrapper<VacuumFreezerRecipe> {
	private final IDrawableAnimated progress;

	public VacuumFreezerRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			VacuumFreezerRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiVacuumFreezer.texture, 176, 14, 20, 11);

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
	}
}
