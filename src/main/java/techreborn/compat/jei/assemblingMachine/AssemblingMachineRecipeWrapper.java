package techreborn.compat.jei.assemblingMachine;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;
import techreborn.client.gui.GuiAssemblingMachine;
import techreborn.compat.jei.BaseRecipeWrapper;
import techreborn.compat.jei.TechRebornJeiPlugin;

public class AssemblingMachineRecipeWrapper extends BaseRecipeWrapper {
	private final IDrawableAnimated progress;

	public AssemblingMachineRecipeWrapper(AssemblingMachineRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = TechRebornJeiPlugin.jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiAssemblingMachine.texture, 176, 14, 20, 18);
		this.progress = guiHelper.createAnimatedDrawable(progressStatic, baseRecipe.tickTime(), IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
		super.drawAnimations(minecraft, recipeWidth, recipeHeight);
		progress.draw(minecraft, 40, 18);
	}
}
