package techreborn.compat.jei.industrialSawmill;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.client.gui.GuiIndustrialSawmill;
import techreborn.compat.jei.BaseRecipeWrapper;

public class IndustrialSawmillRecipeWrapper extends BaseRecipeWrapper<IndustrialSawmillRecipe>
{
	private final IDrawableAnimated progress;

	public IndustrialSawmillRecipeWrapper(@Nonnull IJeiHelpers jeiHelpers, @Nonnull IndustrialSawmillRecipe baseRecipe)
	{
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiIndustrialSawmill.texture, 176, 14, 20, 12);

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
		progress.draw(minecraft, 49, 23);

		int x = 70;
		int y = 40;
		int lineHeight = minecraft.fontRendererObj.FONT_HEIGHT;

		minecraft.fontRendererObj.drawString("Time: " +  baseRecipe.tickTime / 20 + " s", x, y, 0x444444);
		minecraft.fontRendererObj.drawString("EU: " +  baseRecipe.euPerTick + " EU/t", x, y += lineHeight, 0x444444);
	}
}
