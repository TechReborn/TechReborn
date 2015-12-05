package techreborn.compat.jei.fusionReactor;


import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.reactor.FusionReactorRecipe;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FusionReactorRecipeWrapper implements IRecipeWrapper {

    FusionReactorRecipe baseRecipe;

    public FusionReactorRecipeWrapper(FusionReactorRecipe baseRecipe) {
        this.baseRecipe = baseRecipe;
    }

    @Override
    public List getInputs() {
        List<ItemStack> inputs = new ArrayList<>();
        inputs.add(baseRecipe.getTopInput());
        inputs.add(baseRecipe.getBottomInput());
        return inputs;
    }

    @Override
    public List<FluidStack> getFluidInputs() {
        return Collections.emptyList();
    }

    @Override
    public List getOutputs() {
        List<ItemStack> outputs = new ArrayList<>();
        outputs.add(baseRecipe.getOutput());
        return outputs;
    }

    @Override
    public List<FluidStack> getFluidOutputs() {
        return Collections.emptyList();
    }

    @Override
    public void drawInfo(Minecraft minecraft) {
        FontRenderer fontRendererObj = minecraft.fontRendererObj;
        //  fontRendererObj.drawString(baseRecipe.getEuTick() + "", 69 - fontRendererObj.getStringWidth(baseRecipe.getEuTick() + "") / 2, 0, Color.gray.getRGB());

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        fontRendererObj.drawStringWithShadow("Start: " + formatter.format(baseRecipe.getStartEU()) + "EU", 0, 70, -1);
        fontRendererObj.drawStringWithShadow("EU/t: " + baseRecipe.getEuTick(), 0, 80, -1);
        fontRendererObj.drawStringWithShadow("Ticks to process: " + baseRecipe.getTickTime(), 0, 90, -1);
        fontRendererObj.drawStringWithShadow("Time to process: " + baseRecipe.getTickTime() / 20 + " seconds", 0, 100, -1);
    }

    @Override
    public boolean usesOreDictionaryComparison() {
        return true;
    }
}
