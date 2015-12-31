package techreborn.compat.jei.fusionReactor;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import techreborn.api.reactor.FusionReactorRecipe;

public class FusionReactorRecipeWrapper extends BlankRecipeWrapper {
    private FusionReactorRecipe baseRecipe;

    public FusionReactorRecipeWrapper(FusionReactorRecipe baseRecipe) {
        this.baseRecipe = baseRecipe;
    }

    @Override
    public List<ItemStack> getInputs() {
        return Arrays.asList(baseRecipe.getTopInput(), baseRecipe.getBottomInput());
    }

    @Override
    public List<ItemStack> getOutputs() {
        return Collections.singletonList(baseRecipe.getOutput());
    }

    public ItemStack getTopInput() {
        return baseRecipe.getTopInput();
    }

    public ItemStack getBottomInput() {
        return baseRecipe.getBottomInput();
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
        FontRenderer fontRendererObj = minecraft.fontRendererObj;
        int color = Color.darkGray.getRGB();
        int x = 0;
        int y = 67;
        int lineSpacing = fontRendererObj.FONT_HEIGHT + 1;

        NumberFormat formatter = NumberFormat.getInstance();
        String startCostEU = formatter.format(baseRecipe.getStartEU());
        String startCostString = StatCollector.translateToLocalFormatted("techreborn.jei.recipe.start.cost", startCostEU);
        fontRendererObj.drawString(startCostString, x, y, color);
        y += lineSpacing;

        String runningCostString = StatCollector.translateToLocalFormatted("techreborn.jei.recipe.running.cost", baseRecipe.getEuTick());
        fontRendererObj.drawString(runningCostString, x, y, color);
        y += lineSpacing;

        int ticks = baseRecipe.getTickTime();
        String processingTimeString1 = StatCollector.translateToLocalFormatted("techreborn.jei.recipe.processing.time.1", ticks);
        fontRendererObj.drawString(processingTimeString1, x, y, color);
        y += lineSpacing;

        int seconds = ticks / 20;
        String processingTimeString2 = StatCollector.translateToLocalFormatted("techreborn.jei.recipe.processing.time.2", seconds);
        fontRendererObj.drawString(processingTimeString2, x + 10, y, color);
    }
}
