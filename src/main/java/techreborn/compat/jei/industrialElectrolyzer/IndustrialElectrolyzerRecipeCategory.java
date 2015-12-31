package techreborn.compat.jei.industrialElectrolyzer;

import javax.annotation.Nonnull;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.client.gui.GuiIndustrialElectrolyzer;
import techreborn.compat.jei.RecipeCategoryUids;

public class IndustrialElectrolyzerRecipeCategory implements IRecipeCategory {
	private static final int[] INPUT_SLOTS = {0, 1};
	private static final int[] OUTPUT_SLOTS = {2, 3, 4, 5};

	private final IDrawable background;
	private final String title;

	public IndustrialElectrolyzerRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiIndustrialElectrolyzer.texture, 49, 18, 78, 50);
		title = StatCollector.translateToLocal("tile.techreborn.industrialelectrolyzer.name");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER;
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

	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 30, 32);
		guiItemStacks.init(INPUT_SLOTS[1], true, 0, 32);
		guiItemStacks.init(OUTPUT_SLOTS[0], false, 0, 0);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 20, 0);
		guiItemStacks.init(OUTPUT_SLOTS[2], false, 40, 0);
		guiItemStacks.init(OUTPUT_SLOTS[3], false, 60, 0);

		if (recipeWrapper instanceof IndustrialElectrolyzerRecipeWrapper) {
			IndustrialElectrolyzerRecipeWrapper recipe = (IndustrialElectrolyzerRecipeWrapper) recipeWrapper;

			List<List<ItemStack>> inputs = recipe.getInputs();
			for (int i = 0; i < inputs.size() && i < INPUT_SLOTS.length; i++) {
				int inputSlot = INPUT_SLOTS[i];
				guiItemStacks.set(inputSlot, inputs.get(i));
			}

			List<ItemStack> outputs = recipe.getOutputs();
			for (int i = 0; i < outputs.size() && i < OUTPUT_SLOTS.length; i++) {
				int outputSlot = OUTPUT_SLOTS[i];
				ItemStack output = outputs.get(i);
				guiItemStacks.set(outputSlot, output);
			}
		}
	}
}
