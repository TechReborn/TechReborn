package techreborn.compat.jei.vacuumFreezer;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;
import techreborn.client.gui.GuiVacuumFreezer;
import techreborn.compat.jei.BaseRecipeWrapper;
import techreborn.compat.jei.TechRebornJeiPlugin;

public class VacuumFreezerRecipeWrapper extends BaseRecipeWrapper<VacuumFreezerRecipe> {
	private final IDrawableAnimated progress;

	public VacuumFreezerRecipeWrapper(VacuumFreezerRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = TechRebornJeiPlugin.jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiVacuumFreezer.texture, 176, 14, 20, 11);

		int ticksPerCycle = baseRecipe.tickTime();
		this.progress = guiHelper.createAnimatedDrawable(progressStatic, ticksPerCycle, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
		super.drawAnimations(minecraft, recipeWidth, recipeHeight);
		progress.draw(minecraft, 25, 7);
	}
}
