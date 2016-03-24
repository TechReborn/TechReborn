package techreborn.compat.jei.industrialElectrolyzer;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import techreborn.api.recipe.machines.IndustrialElectrolyzerRecipe;
import techreborn.client.gui.GuiIndustrialElectrolyzer;
import techreborn.compat.jei.BaseRecipeWrapper;

public class IndustrialElectrolyzerRecipeWrapper extends BaseRecipeWrapper<IndustrialElectrolyzerRecipe>
{
	private final IDrawableAnimated progress;

	public IndustrialElectrolyzerRecipeWrapper(@Nonnull IJeiHelpers jeiHelpers,
			@Nonnull IndustrialElectrolyzerRecipe baseRecipe)
	{
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(GuiIndustrialElectrolyzer.texture, 176, 14, 30, 10);
		this.progress = guiHelper.createAnimatedDrawable(progressStatic, baseRecipe.tickTime(),
				IDrawableAnimated.StartDirection.BOTTOM, false);
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
	{
		super.drawAnimations(minecraft, recipeWidth, recipeHeight);
		progress.draw(minecraft, 24, 20);
	}
}
