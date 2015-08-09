package techreborn.compat.nei.recipes;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.RecipeHandler;
import techreborn.util.ItemUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericRecipeHander extends TemplateRecipeHandler {

    public INeiBaseRecipe getNeiBaseRecipe() {
        return null;
    }

    public class CachedGenericRecipe extends CachedRecipe {

        private List<PositionedStack> input = new ArrayList<PositionedStack>();
        private List<PositionedStack> outputs = new ArrayList<PositionedStack>();
        public Point focus;
        public IBaseRecipeType recipie;
        public INeiBaseRecipe neiBaseRecipe;

        public CachedGenericRecipe(IBaseRecipeType recipe, INeiBaseRecipe neiBaseRecipe) {
            this.recipie = recipe;
            this.neiBaseRecipe = neiBaseRecipe;
            neiBaseRecipe.addPositionedStacks(input, outputs, recipe);
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
        return RecipeHandler.getUserFreindlyName(getNeiBaseRecipe().getRecipeName());
    }

    @Override
    public String getGuiTexture() {
        return getNeiBaseRecipe().getGuiTexture();
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return getNeiBaseRecipe().getGuiClass();
    }

    @Override
    public void drawBackground(int recipeIndex) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 4, 4, 166, 78);
        GuiDraw.drawTooltipBox(10, 80, 145, 50);
        GuiDraw.drawString("Info:", 14, 84, -1);
        CachedRecipe recipe = arecipes.get(recipeIndex);
        if (recipe instanceof CachedGenericRecipe) {
            CachedGenericRecipe genericRecipe = (CachedGenericRecipe) recipe;
            float scale = 0.9F;
            GL11.glScalef(scale, scale, scale);
            GuiDraw.drawString("EU needed: " + (String.format("%,d", genericRecipe.recipie.euPerTick() * genericRecipe.recipie.tickTime())), 16, 105, -1);
            GuiDraw.drawString("Ticks to process: " + genericRecipe.recipie.tickTime(), 14, 115, -1);
            GuiDraw.drawString("Time to process: " + genericRecipe.recipie.tickTime() / 20 + " seconds", 14, 125, -1);
        }

    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

//	public void loadTransferRects() {
//		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(
//				new Rectangle(0, 0, 20, 20), getNeiBaseRecipe().getRecipeName(), new Object[0]));
//	}

    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getNeiBaseRecipe().getRecipeName())) {
            for (IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(getNeiBaseRecipe().getRecipeName())) {
                addCached(recipeType);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(getNeiBaseRecipe().getRecipeName())) {
            for (int i = 0; i < recipeType.getOutputsSize(); i++) {
                if (ItemUtils.isItemEqual(recipeType.getOutput(i), result, true, false, true)) {
                    addCached(recipeType);
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(getNeiBaseRecipe().getRecipeName())) {
            for (ItemStack input : recipeType.getInputs()) {
                if (ItemUtils.isItemEqual(ingredient, input, true, false, true)) {
                    addCached(recipeType);
                }
            }
        }
    }

    private void addCached(IBaseRecipeType recipie) {
        this.arecipes.add(new CachedGenericRecipe(recipie, getNeiBaseRecipe()));
    }

}