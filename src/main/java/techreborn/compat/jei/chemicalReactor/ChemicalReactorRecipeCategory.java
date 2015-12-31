package techreborn.compat.jei.chemicalReactor;

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
import techreborn.client.gui.GuiChemicalReactor;
import techreborn.compat.jei.RecipeCategoryUids;

public class ChemicalReactorRecipeCategory implements IRecipeCategory {
	private static final int INPUT_SLOT_0 = 0;
	private static final int INPUT_SLOT_1 = 1;
	private static final int OUTPUT_SLOT = 2;

	private final IDrawable background;
	private final String title;

	public ChemicalReactorRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiChemicalReactor.texture, 69, 20, 38, 48);
		title = StatCollector.translateToLocal("tile.techreborn.chemicalreactor.name");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.CHEMICAL_REACTOR;
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
		guiItemStacks.init(INPUT_SLOT_0, true, 0, 0);
		guiItemStacks.init(INPUT_SLOT_1, true, 20, 0);

		guiItemStacks.init(OUTPUT_SLOT, false, 10, 30);

		if (recipeWrapper instanceof ChemicalReactorRecipeWrapper) {
			ChemicalReactorRecipeWrapper recipe = (ChemicalReactorRecipeWrapper) recipeWrapper;

			List<List<ItemStack>> inputs = recipe.getInputs();
			guiItemStacks.set(INPUT_SLOT_0, inputs.get(0));
			if (inputs.size() > 1) {
				guiItemStacks.set(INPUT_SLOT_1, inputs.get(1));
			}

			guiItemStacks.set(OUTPUT_SLOT, recipe.getOutputs());
		}
	}
}
