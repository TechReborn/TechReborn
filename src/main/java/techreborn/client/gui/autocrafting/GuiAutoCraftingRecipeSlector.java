package techreborn.client.gui.autocrafting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class GuiAutoCraftingRecipeSlector extends GuiRecipeBook {

	GuiAutoCrafting guiAutoCrafting;


	@Override
	public void initVisuals(boolean p_193014_1_, InventoryCrafting p_193014_2_) {
		super.initVisuals(p_193014_1_, p_193014_2_);
		//Pulls the button off the screen as we dont need it
		toggleRecipesBtn = new GuiButtonToggle(0, -1000, -1000, 26, 16, false);
		toggleRecipesBtn.initTextureValues(152, 41, 28, 18, RECIPE_BOOK);
	}

	@Override
	public void init(int p_191856_1_, int p_191856_2_, Minecraft mc, boolean p_191856_4_, Container p_191856_5_, InventoryCrafting p_191856_6_) {
		super.init(p_191856_1_, p_191856_2_, mc, p_191856_4_, p_191856_5_, p_191856_6_);

	}

	@Override
	public void setContainerRecipe(IRecipe recipe, RecipeList recipes) {
		guiAutoCrafting.setRecipe(recipe, false);
	}

	public void setGuiAutoCrafting(GuiAutoCrafting guiAutoCrafting) {
		this.guiAutoCrafting = guiAutoCrafting;
	}
}
