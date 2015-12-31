package techreborn.compat.jei.centrifuge;

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
import techreborn.client.gui.GuiCentrifuge;
import techreborn.compat.jei.RecipeCategoryUids;

public class CentrifugeRecipeCategory implements IRecipeCategory {
	private static final int INPUT_SLOT = 0;
	private static final int EMPTY_CELL_SLOT = 1;
	private static final int[] OUTPUT_SLOTS = {2, 3, 4, 5};

	private final IDrawable background;
	private final String title;

	public CentrifugeRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiCentrifuge.texture, 49, 4, 78, 78);
		title = StatCollector.translateToLocal("tile.techreborn.centrifuge.name");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.CENTRIFUGE;
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
		guiItemStacks.init(INPUT_SLOT, true, 30, 30);
		guiItemStacks.init(EMPTY_CELL_SLOT, true, 0, 0);

		guiItemStacks.init(OUTPUT_SLOTS[0], false, 30, 0);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 60, 30);
		guiItemStacks.init(OUTPUT_SLOTS[2], false, 30, 60);
		guiItemStacks.init(OUTPUT_SLOTS[3], false, 0, 30);

		if (recipeWrapper instanceof CentrifugeRecipeWrapper) {
			CentrifugeRecipeWrapper recipe = (CentrifugeRecipeWrapper) recipeWrapper;

			List<List<ItemStack>> inputs = recipe.getInputs();
			guiItemStacks.set(INPUT_SLOT, inputs.get(0));
			if (inputs.size() > 1) {
				guiItemStacks.set(EMPTY_CELL_SLOT, inputs.get(1));
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
