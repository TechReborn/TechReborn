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
	public void func_193014_a(boolean p_193014_1_, InventoryCrafting p_193014_2_) {
		super.func_193014_a(p_193014_1_, p_193014_2_);
		//Pulls the button off the screen as we dont need it
		field_193960_m = new GuiButtonToggle(0, -1000, -1000, 26, 16, false);
		field_193960_m.func_191751_a(152, 41, 28, 18, RECIPE_BOOK);
	}

	@Override
	public void func_191856_a(int p_191856_1_, int p_191856_2_, Minecraft mc, boolean p_191856_4_, Container p_191856_5_, InventoryCrafting p_191856_6_) {
		super.func_191856_a(p_191856_1_, p_191856_2_, mc, p_191856_4_, p_191856_5_, p_191856_6_);

	}

	@Override
	public void func_193945_a(IRecipe recipe, RecipeList recipes) {
		guiAutoCrafting.setRecipe(recipe);
	}

	public void setGuiAutoCrafting(GuiAutoCrafting guiAutoCrafting) {
		this.guiAutoCrafting = guiAutoCrafting;
	}
}
