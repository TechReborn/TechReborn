package techreborn.compat.nei.recipes;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.RecipeHanderer;
import techreborn.config.ConfigTechReborn;
import techreborn.util.ItemUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericRecipeHander extends TemplateRecipeHandler {

	public INeiBaseRecipe getNeiBaseRecipe(){
		return null;
	}

	public class CachedGenericRecipe extends CachedRecipe {

		private List<PositionedStack> input = new ArrayList<PositionedStack>();
		private List<PositionedStack> outputs = new ArrayList<PositionedStack>();
		public Point focus;
		public IBaseRecipeType recipie;
		public INeiBaseRecipe neiBaseRecipe;

		public CachedGenericRecipe(IBaseRecipeType recipe, INeiBaseRecipe neiBaseRecipe)
		{
			this.recipie = recipe;
			this.neiBaseRecipe = neiBaseRecipe;
			neiBaseRecipe.addPositionedStacks(input, outputs, recipe);
		}

		@Override
		public List<PositionedStack> getIngredients()
		{
			return this.getCycledIngredients(cycleticks / 20, this.input);
		}

		@Override
		public List<PositionedStack> getOtherStacks()
		{
			return this.outputs;
		}

		@Override
		public PositionedStack getResult()
		{
			return null;
		}
	}

	@Override
	public String getRecipeName()
	{
		return getNeiBaseRecipe().getRecipeName();
	}

	@Override
	public String getGuiTexture()
	{
		return getNeiBaseRecipe().getGuiTexture();
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return getNeiBaseRecipe().getGuiClass();
	}

	@Override
	public void drawBackground(int recipeIndex)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 4, 4, 166, 78);
		GuiDraw.drawTooltipBox(10, 80, 145, 50);
		GuiDraw.drawString("Info:", 14, 84, -1);
		CachedRecipe recipe = arecipes.get(recipeIndex);
		if (recipe instanceof CachedGenericRecipe)
		{
			CachedGenericRecipe genericRecipe = (CachedGenericRecipe) recipe;
			GuiDraw.drawString(
					"EU needed: "
							+ (ConfigTechReborn.CentrifugeInputTick * genericRecipe.recipie
							.tickTime()), 14, 94, -1);
			GuiDraw.drawString("Ticks to smelt: "
							+ genericRecipe.recipie.tickTime(), 14,
					104, -1);
			GuiDraw.drawString("Time to smelt: "
					+ genericRecipe.recipie.tickTime() / 20
					+ " seconds", 14, 114, -1);
		}

	}

	@Override
	public int recipiesPerPage()
	{
		return 1;
	}

	public void loadTransferRects()
	{
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(
				new Rectangle(0, 0, 20, 20), getNeiBaseRecipe().getRecipeName(), new Object[0]));
	}

	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals(getNeiBaseRecipe().getRecipeName()))
		{
			for (IBaseRecipeType recipeType : RecipeHanderer.getRecipeClassFromName(getNeiBaseRecipe().getRecipeName()))
			{
				addCached(recipeType);
			}
		} else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		for (IBaseRecipeType recipeType : RecipeHanderer.getRecipeClassFromName(getNeiBaseRecipe().getRecipeName()))
		{
			for(ItemStack output : recipeType.getOutputs()){
				if (ItemUtils.isItemEqual(output, result, true, false, true))
				{
					addCached(recipeType);
				}
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for (IBaseRecipeType recipeType : RecipeHanderer.getRecipeClassFromName(getNeiBaseRecipe().getRecipeName()))
		{
			for(ItemStack input : recipeType.getInputs()){
				if (ItemUtils.isItemEqual(ingredient, input, true, false, true))
				{
					addCached(recipeType);
				}
			}
		}
	}

	private void addCached(IBaseRecipeType recipie)
	{
		this.arecipes.add(new CachedGenericRecipe(recipie, getNeiBaseRecipe()));
	}

}