package techreborn.compat.jei.implosionCompressor;

import javax.annotation.Nonnull;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.client.gui.GuiImplosionCompressor;
import techreborn.compat.jei.RecipeCategoryUids;

public class ImplosionCompressorRecipeCategory implements IRecipeCategory {
	private static final int INPUT_SLOT_0 = 0;
	private static final int INPUT_SLOT_1 = 1;
	private static final int OUTPUT_SLOT_0 = 2;
	private static final int OUTPUT_SLOT_1 = 3;

	private final IDrawable background;
	private final IDrawable electricity;
	private final String title;

	public ImplosionCompressorRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiImplosionCompressor.texture, 16, 25, 116, 36);
		IDrawableStatic electricityDrawable = guiHelper.createDrawable(GuiImplosionCompressor.texture, 176, 0, 14, 14);
		electricity = guiHelper.createAnimatedDrawable(electricityDrawable, 300, IDrawableAnimated.StartDirection.TOP, true);
		title = StatCollector.translateToLocal("tile.techreborn.implosioncompressor.name");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.IMPLOSION_COMPRESSOR;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return title;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {

	}

	@Override
	public void drawAnimations(Minecraft minecraft) {
		electricity.draw(minecraft, 0, 12);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOT_0, true, 20, 0);
		guiItemStacks.init(INPUT_SLOT_1, true, 20, 18);
		guiItemStacks.init(OUTPUT_SLOT_0, false, 76, 9);
		guiItemStacks.init(OUTPUT_SLOT_1, false, 94, 9);

		if (recipeWrapper instanceof ImplosionCompressorRecipeWrapper) {
			ImplosionCompressorRecipeWrapper recipe = (ImplosionCompressorRecipeWrapper) recipeWrapper;

			List<List<ItemStack>> inputs = recipe.getInputs();
			guiItemStacks.set(INPUT_SLOT_0, inputs.get(0));
			if (inputs.size() > 1) {
				guiItemStacks.set(INPUT_SLOT_1, inputs.get(1));
			}

			List<ItemStack> outputs = recipe.getOutputs();
			guiItemStacks.set(OUTPUT_SLOT_0, outputs.get(0));
			if (outputs.size() > 1) {
				guiItemStacks.set(OUTPUT_SLOT_1, outputs.get(1));
			}
		}
	}
}
