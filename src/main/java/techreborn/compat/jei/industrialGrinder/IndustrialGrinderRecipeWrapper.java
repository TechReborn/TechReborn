package techreborn.compat.jei.industrialGrinder;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.recipe.machines.IndustrialGrinderRecipe;
import techreborn.client.gui.GuiIndustrialGrinder;
import techreborn.compat.jei.BaseRecipeWrapper;

public class IndustrialGrinderRecipeWrapper extends BaseRecipeWrapper<IndustrialGrinderRecipe>
{
	private final IDrawableAnimated progress;

	public IndustrialGrinderRecipeWrapper(@Nonnull IJeiHelpers jeiHelpers, @Nonnull IndustrialGrinderRecipe baseRecipe)
	{
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiIndustrialGrinder.texture, 176, 14, 24, 17);

		int ticksPerCycle = baseRecipe.tickTime();
		this.progress = guiHelper.createAnimatedDrawable(progressStatic, ticksPerCycle,
				IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	@Nonnull
	public List<FluidStack> getFluidInputs()
	{
		if (baseRecipe.fluidStack != null)
		{
			return Collections.singletonList(baseRecipe.fluidStack);
		} else
		{
			return Collections.emptyList();
		}
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
	{
		super.drawAnimations(minecraft, recipeWidth, recipeHeight);
		progress.draw(minecraft, 44, 20);
	}
}
