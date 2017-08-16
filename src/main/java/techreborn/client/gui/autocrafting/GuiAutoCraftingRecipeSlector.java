package techreborn.client.gui.autocrafting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.GuiScreen;
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
		//recipeBook.setGuiOpen(true);
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	public void setContainerRecipe(IRecipe recipe) {
		guiAutoCrafting.setRecipe(recipe, false);
	}

	@Override
	public boolean mouseClicked(int p_191862_1_, int p_191862_2_, int p_191862_3_) {
		if (this.isVisible()) {
			if (this.recipeBookPage.mouseClicked(p_191862_1_, p_191862_2_, p_191862_3_, (this.width - 147) / 2 - this.xOffset, (this.height - 166) / 2, 147, 166)) {
				IRecipe irecipe = this.recipeBookPage.getLastClickedRecipe();
				RecipeList recipelist = this.recipeBookPage.getLastClickedRecipeList();

				if (irecipe != null && recipelist != null) {
					if (!recipelist.isCraftable(irecipe) && this.ghostRecipe.getRecipe() == irecipe) {
						return false;
					}
					this.ghostRecipe.clear();
					setContainerRecipe(irecipe);
				}
				return true;
			}
		}
		return super.mouseClicked(p_191862_1_, p_191862_2_, p_191862_3_);
	}

	public void setGuiAutoCrafting(GuiAutoCrafting guiAutoCrafting) {
		this.guiAutoCrafting = guiAutoCrafting;
	}
}
