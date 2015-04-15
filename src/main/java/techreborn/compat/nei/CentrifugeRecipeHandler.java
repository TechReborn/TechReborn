package techreborn.compat.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import ic2.api.item.IC2Items;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import techreborn.api.CentrifugeRecipie;
import techreborn.api.TechRebornAPI;
import techreborn.client.gui.GuiCentrifuge;
import techreborn.config.ConfigTechReborn;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class CentrifugeRecipeHandler extends TemplateRecipeHandler {

	public class CachedCentrifugeRecipe extends CachedRecipe {

		private List<PositionedStack> input = new ArrayList<PositionedStack>();
		private List<PositionedStack> outputs = new ArrayList<PositionedStack>();
		public Point focus;
		public CentrifugeRecipie centrifugeRecipie;


		public CachedCentrifugeRecipe(CentrifugeRecipie recipie) {
			this.centrifugeRecipie = recipie;
			int offset = 4;
			PositionedStack pStack = new PositionedStack(recipie.getInputItem(), 80 - offset, 35 - offset);
			pStack.setMaxSize(1);
			this.input.add(pStack);


			if(recipie.getOutput1() != null){
				this.outputs.add(new PositionedStack(recipie.getOutput1(), 80 - offset, 5 - offset));
			}
			if(recipie.getOutput2() != null){
				this.outputs.add(new PositionedStack(recipie.getOutput2(), 110 - offset, 35 - offset));
			}
			if(recipie.getOutput3() != null){
				this.outputs.add(new PositionedStack(recipie.getOutput3(), 80 - offset, 65 - offset));
			}
			if(recipie.getOutput4() != null){
				this.outputs.add(new PositionedStack(recipie.getOutput4(), 50 - offset, 35 - offset));
			}

			ItemStack cellStack = IC2Items.getItem("cell");
			cellStack.stackSize = recipie.getCells();
			this.outputs.add(new PositionedStack(cellStack, 50 - offset, 5 - offset));

		}

		@Override
		public List<PositionedStack> getIngredients() {
			return this.getCycledIngredients(cycleticks / 20, this.input);
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			return this.outputs;
		}

		@Override
		public PositionedStack getResult() {
			return null;
		}
	}

	@Override
	public String getRecipeName() {
		return "Centrifuge";
	}

	@Override
	public String getGuiTexture() {
		return "techreborn:textures/gui/centrifuge.png";
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiCentrifuge.class;
	}

	@Override
	public void drawBackground(int recipeIndex) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 4, 4, 166, 78);
		GuiDraw.drawTooltipBox(10, 80, 145, 50);
		GuiDraw.drawString("Info:", 14, 84, -1);
		CachedRecipe recipe = arecipes.get(recipeIndex);
		if(recipe instanceof CachedCentrifugeRecipe){
			CachedCentrifugeRecipe centrifugeRecipie = (CachedCentrifugeRecipe) recipe;
			GuiDraw.drawString("EU needed: " + (ConfigTechReborn.CentrifugeInputTick * centrifugeRecipie.centrifugeRecipie.getTickTime()), 14, 94, -1);
			GuiDraw.drawString("Ticks to smelt: " + centrifugeRecipie.centrifugeRecipie.getTickTime(), 14, 104, -1);
			GuiDraw.drawString("Time to smelt: " + centrifugeRecipie.centrifugeRecipie.getTickTime() / 20 + " seconds" , 14, 114, -1);
		}

	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public void loadTransferRects() {
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(75, 22, 15, 13), "tr.centrifuge", new Object[0]));
	}

	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("tr.centrifuge")) {
			for(CentrifugeRecipie centrifugeRecipie : TechRebornAPI.centrifugeRecipies){
				addCached(centrifugeRecipie);
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for(CentrifugeRecipie centrifugeRecipie : TechRebornAPI.centrifugeRecipies){
			if(NEIServerUtils.areStacksSameTypeCrafting(centrifugeRecipie.getOutput1(), result)){
				addCached(centrifugeRecipie);
			}
			if(NEIServerUtils.areStacksSameTypeCrafting(centrifugeRecipie.getOutput2(), result)){
				addCached(centrifugeRecipie);
			}
			if(NEIServerUtils.areStacksSameTypeCrafting(centrifugeRecipie.getOutput3(), result)){
				addCached(centrifugeRecipie);
			}
			if(NEIServerUtils.areStacksSameTypeCrafting(centrifugeRecipie.getOutput4(), result)){
				addCached(centrifugeRecipie);
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		for(CentrifugeRecipie centrifugeRecipie : TechRebornAPI.centrifugeRecipies){
			if(NEIServerUtils.areStacksSameTypeCrafting(centrifugeRecipie.getInputItem(), ingredient)){
				addCached(centrifugeRecipie);
			}
		}
	}

	private void addCached(CentrifugeRecipie recipie) {
		this.arecipes.add(new CachedCentrifugeRecipe(recipie));
	}

}