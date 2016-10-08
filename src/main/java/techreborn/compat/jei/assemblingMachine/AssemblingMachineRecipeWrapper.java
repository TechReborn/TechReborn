package techreborn.compat.jei.assemblingMachine;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;
import techreborn.client.gui.GuiAssemblingMachine;
import techreborn.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;

public class AssemblingMachineRecipeWrapper extends BaseRecipeWrapper<AssemblingMachineRecipe> {
	private final IDrawableAnimated progress;

	public AssemblingMachineRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			AssemblingMachineRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiAssemblingMachine.texture, 176, 14, 20, 18);
		this.progress = guiHelper.createAnimatedDrawable(progressStatic, baseRecipe.tickTime(),
			IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void drawAnimations(
		@Nonnull
			Minecraft minecraft, int recipeWidth, int recipeHeight) {
		super.drawAnimations(minecraft, recipeWidth, recipeHeight);
		progress.draw(minecraft, 40, 18);
	}
}
