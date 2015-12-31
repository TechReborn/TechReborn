package techreborn.compat.jei.alloySmelter;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.client.gui.GuiAlloySmelter;
import techreborn.compat.jei.BaseRecipeWrapper;
import techreborn.compat.jei.TechRebornJeiPlugin;

public class AlloySmelterRecipeWrapper extends BaseRecipeWrapper<AlloySmelterRecipe> {
	private final IDrawableAnimated arrow;

	public AlloySmelterRecipeWrapper(AlloySmelterRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = TechRebornJeiPlugin.jeiHelpers.getGuiHelper();
		IDrawableStatic arrowStatic = guiHelper.createDrawable(GuiAlloySmelter.texture, 176, 14, 24, 17);
		this.arrow = guiHelper.createAnimatedDrawable(arrowStatic, baseRecipe.tickTime(), IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
		super.drawAnimations(minecraft, recipeWidth, recipeHeight);
		arrow.draw(minecraft, 33, 19);
	}
}
