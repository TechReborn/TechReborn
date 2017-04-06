package techreborn.compat.jei.industrialSawmill;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.util.ResourceLocation;
import techreborn.compat.jei.RecipeCategoryUids;

public class IndustrialSawmillRecipeCategory extends BlankRecipeCategory<IndustrialSawmillRecipeWrapper> {

	private final String title;
	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/industrial_sawmill.png");
	private final IDrawable background;
	
	
	public IndustrialSawmillRecipeCategory(IGuiHelper guiHelper){
		title = Translator.translateToLocal("tile.techreborn.industrialsawmill.name");
		background = guiHelper.createDrawable(texture, 7, 15, 141, 55);
		
	}
	
	
	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategoryUids.INDUSTRIAL_SAWMILL;
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
	public void setRecipe(IRecipeLayout recipeLayout, IndustrialSawmillRecipeWrapper recipeWrapper, IIngredients ingredients) {
		
	}
	}
