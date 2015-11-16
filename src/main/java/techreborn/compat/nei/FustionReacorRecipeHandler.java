package techreborn.compat.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import reborncore.common.util.ItemUtils;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.client.gui.GuiFusionReactor;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FustionReacorRecipeHandler  extends TemplateRecipeHandler {


    public class CachedReactorRecipe extends CachedRecipe {

        private List<PositionedStack> input = new ArrayList<PositionedStack>();
        private List<PositionedStack> outputs = new ArrayList<PositionedStack>();
        public Point focus;
        public FusionReactorRecipe recipe;

        public CachedReactorRecipe(FusionReactorRecipe recipe) {
            this.recipe = recipe;
            addPositionedStacks(input, outputs, recipe);
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
    public void drawBackground(int recipeIndex) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 4, 4, 166, 78);
        GuiDraw.drawTooltipBox(10, 80, 145, 50);
        GuiDraw.drawString("Info:", 14, 84, -1);
        CachedRecipe recipe = arecipes.get(recipeIndex);
        if (recipe instanceof CachedReactorRecipe) {
            CachedReactorRecipe genericRecipe = (CachedReactorRecipe) recipe;
            float scale = 0.9F;
            GL11.glScalef(scale, scale, scale);
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            formatter.setDecimalFormatSymbols(symbols);
            GuiDraw.drawString("Start: " + formatter.format(genericRecipe.recipe.getStartEU()) + "EU", 16, 105, -1);
            GuiDraw.drawString("EU/t: " + formatter.format(new Integer((int) (genericRecipe.recipe.getEuTick() * genericRecipe.recipe.getTickTime())).longValue()), 16, 115, -1);
            GuiDraw.drawString("Ticks to process: " + genericRecipe.recipe.getTickTime(), 14, 125, -1);
            GuiDraw.drawString("Time to process: " + genericRecipe.recipe.getTickTime() / 20 + " seconds", 14, 135, -1);
        }

    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }


    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getRecipeName())) {
            for (FusionReactorRecipe recipeType : FusionReactorRecipeHelper.reactorRecipes) {
                addCached(recipeType);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (FusionReactorRecipe recipeType : FusionReactorRecipeHelper.reactorRecipes) {
            if (ItemUtils.isItemEqual(recipeType.getOutput(), result, true, false, true)) {
                addCached(recipeType);
            }

        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (FusionReactorRecipe recipeType : FusionReactorRecipeHelper.reactorRecipes) {
            if (ItemUtils.isItemEqual(ingredient, recipeType.getTopInput(), true, false, true)) {
                addCached(recipeType);
            }
            if (ItemUtils.isItemEqual(ingredient, recipeType.getBottomInput(), true, false, true)) {
                addCached(recipeType);
            }
        }
    }

    private void addCached(FusionReactorRecipe recipie) {
        this.arecipes.add(new CachedReactorRecipe(recipie));
    }


    public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, FusionReactorRecipe recipeType) {
        int offset = 4;

        PositionedStack pStack = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getTopInput()), 88 - offset, 17 - offset, false);
        input.add(pStack);

        if(recipeType.getBottomInput() != null){
            PositionedStack pStack2 = new PositionedStack(ItemUtils.getStackWithAllOre(recipeType.getBottomInput()), 88 - offset, 53 - offset, false);
            input.add(pStack2);
        }

        PositionedStack pStack3 = new PositionedStack(recipeType.getOutput(), 148 - offset, 35 - offset, false);
        outputs.add(pStack3);
    }


    @Override
    public String getRecipeName() {
        return "Fustion Reactor";
    }

    @Override
    public String getGuiTexture() {
        return "techreborn:textures/gui/fusion_reactor.png";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiFusionReactor.class;
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(
                new Rectangle(82, 23, 52, 18), getRecipeName(), new Object[0]));
    }

}
