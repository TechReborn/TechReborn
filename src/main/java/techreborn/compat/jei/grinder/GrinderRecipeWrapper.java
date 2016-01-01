package techreborn.compat.jei.grinder;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;

import net.minecraftforge.fluids.FluidStack;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.client.gui.GuiGrinder;
import techreborn.compat.jei.BaseRecipeWrapper;
import techreborn.compat.jei.TechRebornJeiPlugin;

public class GrinderRecipeWrapper extends BaseRecipeWrapper<GrinderRecipe> {
	private final IDrawableAnimated progress;

	public GrinderRecipeWrapper(GrinderRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = TechRebornJeiPlugin.jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiGrinder.texture, 176, 14, 24, 17);

		int ticksPerCycle = baseRecipe.tickTime();
		this.progress = guiHelper.createAnimatedDrawable(progressStatic, ticksPerCycle, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public List<FluidStack> getFluidInputs() {
		if (baseRecipe.fluidStack != null) {
			return Collections.singletonList(baseRecipe.fluidStack);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
		super.drawAnimations(minecraft, recipeWidth, recipeHeight);
		progress.draw(minecraft, 44, 20);
	}
}
