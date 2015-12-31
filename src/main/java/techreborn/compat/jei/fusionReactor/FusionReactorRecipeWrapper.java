package techreborn.compat.jei.fusionReactor;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.compat.jei.RecipeInfoUtil;

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
        RecipeInfoUtil.drawInfo(minecraft, 0, 67, baseRecipe.getStartEU(), baseRecipe.getEuTick(), baseRecipe.getTickTime());
    }
}
