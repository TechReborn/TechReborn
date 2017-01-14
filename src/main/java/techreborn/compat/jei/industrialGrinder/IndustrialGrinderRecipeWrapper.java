package techreborn.compat.jei.industrialGrinder;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.recipe.machines.IndustrialGrinderRecipe;
import techreborn.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class IndustrialGrinderRecipeWrapper extends BaseRecipeWrapper<IndustrialGrinderRecipe> {
	public static final ResourceLocation texture = new ResourceLocation("techreborn",
		"textures/gui/industrial_grinder.png");
	private final IDrawableAnimated progress;

	public IndustrialGrinderRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			IndustrialGrinderRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(texture, 176, 14, 24, 17);

		int ticksPerCycle = baseRecipe.tickTime();
		this.progress = guiHelper.createAnimatedDrawable(progressStatic, ticksPerCycle,
			IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public void getIngredients(
		@Nonnull
		final IIngredients ingredients) {
		ingredients.setInput(FluidStack.class, this.baseRecipe.fluidStack);
		super.getIngredients(ingredients);
	}

	@Override
	@Nonnull
	public List<FluidStack> getFluidInputs() {
		if (baseRecipe.fluidStack != null) {
			return Collections.singletonList(baseRecipe.fluidStack);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
		progress.draw(minecraft, 44, 20);

		int x = 70;
		int y = 40;
		int lineHeight = minecraft.fontRendererObj.FONT_HEIGHT;

		minecraft.fontRendererObj.drawString("Time: " + baseRecipe.tickTime / 20 + " s", x, y, 0x444444);
		minecraft.fontRendererObj.drawString("EU: " + baseRecipe.euPerTick + " EU/t", x, y += lineHeight, 0x444444);
	}
}
