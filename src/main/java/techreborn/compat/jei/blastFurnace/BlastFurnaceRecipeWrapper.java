package techreborn.compat.jei.blastFurnace;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.api.recipe.machines.BlastFurnaceRecipe;
import techreborn.client.gui.TRBuilder;
import techreborn.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;

public class BlastFurnaceRecipeWrapper extends BaseRecipeWrapper<BlastFurnaceRecipe> {
	private static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/industrial_blast_furnace.png");
	private final IDrawableAnimated progress;
	private final IDrawable heat;

	public BlastFurnaceRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			BlastFurnaceRecipe baseRecipe) {
		super(baseRecipe);
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IDrawableStatic progressStatic = guiHelper.createDrawable(TRBuilder.GUI_SHEET, 100, 151, 16, 10);

		int ticksPerCycle = baseRecipe.tickTime() / 4; // speed up the animation

		this.progress = guiHelper.createAnimatedDrawable(progressStatic, ticksPerCycle, IDrawableAnimated.StartDirection.LEFT, false);
		int j = (int) ((double) baseRecipe.neededHeat / (double) 3230 * 106);
		if (j < 0)
			j = 0;

		this.heat = guiHelper.createDrawable(TRBuilder.GUI_SHEET, 0, 246, j, 10);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
		progress.draw(minecraft, 43, 17);
		heat.draw(minecraft, 7, 47);

		int y = 64;
		int lineHeight = minecraft.fontRendererObj.FONT_HEIGHT;

		minecraft.fontRendererObj.drawString(baseRecipe.neededHeat + " Heat", (recipeWidth / 2 - minecraft.fontRendererObj.getStringWidth(baseRecipe.neededHeat + " Heat") / 2), 48, 0xFFFFFF);
		minecraft.fontRendererObj.drawString(baseRecipe.tickTime / 20 + " seconds", (recipeWidth / 2 - minecraft.fontRendererObj.getStringWidth(baseRecipe.tickTime / 20 + " seconds") / 2), y, 0x444444);
		minecraft.fontRendererObj.drawString(PowerSystem.getLocaliszedPowerFormatted(baseRecipe.euPerTick * baseRecipe.tickTime), (recipeWidth / 2 - minecraft.fontRendererObj.getStringWidth(PowerSystem.getLocaliszedPowerFormatted(baseRecipe.euPerTick * baseRecipe.tickTime)) / 2), y + lineHeight, 0x444444);

	}
}
