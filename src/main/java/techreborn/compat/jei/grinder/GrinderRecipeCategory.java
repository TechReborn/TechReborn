package techreborn.compat.jei.grinder;

import javax.annotation.Nonnull;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import net.minecraftforge.fluids.FluidStack;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.client.gui.GuiGrinder;
import techreborn.compat.jei.RecipeCategoryUids;
import techreborn.tiles.TileGrinder;

public class GrinderRecipeCategory implements IRecipeCategory {
	private static final int[] INPUT_SLOTS = {0, 1};
	private static final int[] OUTPUT_SLOTS = {2, 3, 4, 5};
	private static final int INPUT_TANK = 0;

	private final IDrawable background;
	private final IDrawable blankArea; // for covering the lightning power symbol
	private final IDrawable tankOverlay;
	private final String title;

	public GrinderRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(GuiGrinder.texture, 7, 15, 141, 55);
		blankArea = guiHelper.createDrawable(GuiGrinder.texture, 50, 45, 6, 6);
		tankOverlay = guiHelper.createDrawable(GuiGrinder.texture, 176, 86, 12, 47);
		title = StatCollector.translateToLocal("tile.techreborn.grinder.name");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.GRINDER;
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
		blankArea.draw(minecraft, 129, 49);
	}

	@Override
	public void drawAnimations(Minecraft minecraft) {

	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(INPUT_SLOTS[0], true, 24, 10);
		guiItemStacks.init(INPUT_SLOTS[1], true, 24, 28);

		guiItemStacks.init(OUTPUT_SLOTS[0], false, 69, 19);
		guiItemStacks.init(OUTPUT_SLOTS[1], false, 87, 19);
		guiItemStacks.init(OUTPUT_SLOTS[2], false, 105, 19);
		guiItemStacks.init(OUTPUT_SLOTS[3], false, 123, 19);

		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		guiFluidStacks.init(INPUT_TANK, true, 4, 4, 12, 47, TileGrinder.TANK_CAPACITY, tankOverlay);

		if (recipeWrapper instanceof GrinderRecipeWrapper) {
			GrinderRecipeWrapper recipe = (GrinderRecipeWrapper) recipeWrapper;

			List<ItemStack> inputs = recipe.getInputs();
			for (int i = 0; i < inputs.size() && i < INPUT_SLOTS.length; i++) {
				int inputSlot = INPUT_SLOTS[i];
				ItemStack input = inputs.get(i);
				guiItemStacks.set(inputSlot, input);
			}

			List<ItemStack> outputs = recipe.getOutputs();
			for (int i = 0; i < outputs.size() && i < OUTPUT_SLOTS.length; i++) {
				int outputSlot = OUTPUT_SLOTS[i];
				ItemStack output = outputs.get(i);
				guiItemStacks.set(outputSlot, output);
			}

			List<FluidStack> fluidInputs = recipe.getFluidInputs();
			if (fluidInputs.size() > 0) {
				guiFluidStacks.set(INPUT_TANK, fluidInputs.get(0));
			}
		}
	}
}
