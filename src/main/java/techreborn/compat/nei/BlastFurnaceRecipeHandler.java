package techreborn.compat.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import techreborn.api.BlastFurnaceRecipe;
import techreborn.api.TechRebornAPI;
import techreborn.client.gui.GuiBlastFurnace;
import techreborn.client.gui.GuiCentrifuge;
import techreborn.config.ConfigTechReborn;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 29/04/15.
 */
public class BlastFurnaceRecipeHandler extends TemplateRecipeHandler {

	public class CachedBlastFurnaceRecipe extends CachedRecipe {

		private List<PositionedStack> input = new ArrayList<PositionedStack>();
		private List<PositionedStack> outputs = new ArrayList<PositionedStack>();
		public Point focus;
		public BlastFurnaceRecipe centrifugeRecipie;

		public CachedBlastFurnaceRecipe(BlastFurnaceRecipe recipie)
		{
			this.centrifugeRecipie = recipie;
			int offset = 4;
			PositionedStack pStack = new PositionedStack(
					recipie.getInput1(), 56 - offset, 25 - offset);

			pStack.setMaxSize(1);
			this.input.add(pStack);

			PositionedStack pStack2 = new PositionedStack(
					recipie.getInput2(), 56 - offset, 43 - offset);

			pStack.setMaxSize(1);
			this.input.add(pStack2);

			if (recipie.getOutput1() != null)
			{
				this.outputs.add(new PositionedStack(recipie.getOutput1(),
						116 - offset, 35 - offset));
			}
			if (recipie.getOutput2() != null)
			{
				this.outputs.add(new PositionedStack(recipie.getOutput2(),
						116 - offset, 53 - offset));
			}
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
		return "Blast Furnace";
	}

	@Override
	public String getGuiTexture()
	{
		return "techreborn:textures/gui/industrial_blast_furnace.png";
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GuiBlastFurnace.class;
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
		if (recipe instanceof CachedBlastFurnaceRecipe)
		{
			CachedBlastFurnaceRecipe centrifugeRecipie = (CachedBlastFurnaceRecipe) recipe;
			GuiDraw.drawString(
					"EU needed: "
							+ (ConfigTechReborn.CentrifugeInputTick * centrifugeRecipie.centrifugeRecipie
							.getTickTime()), 14, 94, -1);
			GuiDraw.drawString("Ticks to smelt: "
							+ centrifugeRecipie.centrifugeRecipie.getTickTime(), 14,
					104, -1);
			GuiDraw.drawString("Time to smelt: "
					+ centrifugeRecipie.centrifugeRecipie.getTickTime() / 20
					+ " seconds", 14, 114, -1);
		}
	}

	@Override
	public int recipiesPerPage()
	{
		return 1;
	}

	@Override
	public void loadTransferRects()
	{
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(
				new Rectangle(0, 0, 20, 20), "tr.blast", new Object[0]));
	}

	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals("tr.blast"))
		{
			for (BlastFurnaceRecipe centrifugeRecipie : TechRebornAPI.blastFurnaceRecipes)
			{
				addCached(centrifugeRecipie);
			}
		} else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		for (BlastFurnaceRecipe centrifugeRecipie : TechRebornAPI.blastFurnaceRecipes)
		{
			if (NEIServerUtils.areStacksSameTypeCrafting(
					centrifugeRecipie.getOutput1(), result))
			{
				addCached(centrifugeRecipie);
			}
			if (NEIServerUtils.areStacksSameTypeCrafting(
					centrifugeRecipie.getOutput2(), result))
			{
				addCached(centrifugeRecipie);
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for (BlastFurnaceRecipe centrifugeRecipie : TechRebornAPI.blastFurnaceRecipes)
		{
			if (NEIServerUtils.areStacksSameTypeCrafting(centrifugeRecipie.getInput1(), ingredient) || NEIServerUtils.areStacksSameTypeCrafting(centrifugeRecipie.getInput2(), ingredient))
			{
				addCached(centrifugeRecipie);
			}
		}
	}

	private void addCached(BlastFurnaceRecipe recipie)
	{
		this.arecipes.add(new CachedBlastFurnaceRecipe(recipie));
	}

}