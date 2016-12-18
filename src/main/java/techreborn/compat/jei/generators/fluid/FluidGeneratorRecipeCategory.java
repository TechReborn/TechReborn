package techreborn.compat.jei.generators.fluid;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import techreborn.api.generator.EFluidGenerator;
import techreborn.compat.jei.RecipeUtil;

public class FluidGeneratorRecipeCategory extends BlankRecipeCategory<FluidGeneratorRecipeWrapper> {
	public static ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/jei_fluid_generator.png");

	private static final int[] INPUT_TANKS = { 0 };

	private static final int[] INPUT_SLOTS = { 0 };
	private static final int[] OUTPUT_SLOTS = { 1 };

	private final IDrawable background;
	private final String title;

	private final IDrawable tankOverlay;

	private final EFluidGenerator generatorType;

	public FluidGeneratorRecipeCategory(EFluidGenerator generatorType, IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(texture, 42, 16, 102, 60);
		tankOverlay = guiHelper.createDrawable(texture, 176, 72, 12, 47);
		title = I18n.translateToLocal("techreborn.jei.category.generator." + generatorType.name().toLowerCase());

		this.generatorType = generatorType;
	}

	@Override
	public String getUid() {
		return this.generatorType.getRecipeID();
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, FluidGeneratorRecipeWrapper recipeWrapper,
			IIngredients ingredients) {

		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		guiFluidStacks.init(INPUT_TANKS[0], true, 4, 8, 12, 47, 10000, true, tankOverlay);

		RecipeUtil.setRecipeItems(recipeLayout, ingredients, INPUT_SLOTS, OUTPUT_SLOTS, INPUT_TANKS, null);
	}
}
