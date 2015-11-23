package techreborn.compat.nei.recipes;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import reborncore.client.gui.GuiUtil;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.client.gui.GuiIndustrialSawmill;
import techreborn.lib.Reference;

import java.awt.*;
import java.util.List;

public class IndustrialSawmillRecipeHandler extends GenericRecipeHander implements INeiBaseRecipe {
    @Override
    public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType) {
        int offset = 4;
        if (recipeType.getInputs().size() > 0) {
            PositionedStack pStack = new PositionedStack(recipeType.getInputs().get(0), 32 - offset, 26 - offset, false);
            input.add(pStack);
        }

        if (recipeType.getInputs().size() > 1) {
            PositionedStack pStack2 = new PositionedStack(recipeType.getInputs().get(1), 32 - offset, 44 - offset, false);
            input.add(pStack2);
        }

        if (recipeType.getOutputsSize() > 0) {
            PositionedStack pStack3 = new PositionedStack(recipeType.getOutput(0), 84 - offset, 35 - offset, false);
            outputs.add(pStack3);
        }

        if (recipeType.getOutputsSize() > 1) {
            PositionedStack pStack4 = new PositionedStack(recipeType.getOutput(1), 102 - offset, 35 - offset, false);
            outputs.add(pStack4);
        }

        if (recipeType.getOutputsSize() > 2) {
            PositionedStack pStack5 = new PositionedStack(recipeType.getOutput(2), 120 - offset, 35 - offset, false);
            outputs.add(pStack5);
        }
    }

    @Override
    public String getRecipeName() {
        return Reference.industrialSawmillRecipe;
    }

    @Override
    public String getGuiTexture() {
        return "techreborn:textures/gui/industrial_sawmill.png";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiIndustrialSawmill.class;
    }

    @Override
    public INeiBaseRecipe getNeiBaseRecipe() {
        return this;
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(
                new Rectangle(50, 20, 25, 20), getNeiBaseRecipe().getRecipeName(), new Object[0]));
    }

    @Override
    public void drawBackground(int recipeIndex) {
        super.drawBackground(recipeIndex);
        CachedRecipe recipe = arecipes.get(recipeIndex);
        if (recipe instanceof CachedGenericRecipe) {
            if (((CachedGenericRecipe) recipe).recipie instanceof IndustrialSawmillRecipe) {
                IndustrialSawmillRecipe grinderRecipe = (IndustrialSawmillRecipe) ((CachedGenericRecipe) recipe).recipie;
                if (grinderRecipe.fluidStack != null) {
                    IIcon fluidIcon = grinderRecipe.fluidStack.getFluid().getIcon();
                    if (fluidIcon != null) {

                        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                        int liquidHeight = grinderRecipe.fluidStack.amount * 100 / 16000;
                        GuiUtil.drawRepeated(fluidIcon, 7, 22 + 47 - liquidHeight, 14.0D, liquidHeight, GuiDraw.gui.getZLevel());

                    }
                    GuiDraw.drawString(grinderRecipe.fluidStack.amount + "mb of " + grinderRecipe.fluidStack.getLocalizedName(), 14, 135, -1);
                }
            }
        }

    }
}
