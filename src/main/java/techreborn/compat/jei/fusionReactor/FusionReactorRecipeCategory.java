package techreborn.compat.jei.fusionReactor;

import mezz.jei.api.JEIManager;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import techreborn.client.gui.GuiFusionReactor;

import javax.annotation.Nonnull;

public class FusionReactorRecipeCategory implements IRecipeCategory {

    private static final int inputSlot1 = 0;
    private static final int inputSlot2 = 1;
    private static final int outputSlot = 2;

    @Nonnull
    private final IDrawable background;

    public FusionReactorRecipeCategory() {
        background = JEIManager.guiHelper.createDrawable(GuiFusionReactor.texture, 86, 16, 85, 64, 0, 40, 20, 20);
    }

    @Nonnull
    @Override
    public String getUid() {
        return "FusionReactor";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return "Fusion Reactor";
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
        recipeLayout.getItemStacks().init(inputSlot1, true, 21, 0);
        recipeLayout.getItemStacks().init(inputSlot2, true, 21, 36);
        recipeLayout.getItemStacks().init(outputSlot, false, 80, 18);

        if (recipeWrapper instanceof FusionReactorRecipeWrapper) {
            recipeLayout.getItemStacks().set(inputSlot1, (ItemStack) recipeWrapper.getInputs().get(0));
            recipeLayout.getItemStacks().set(inputSlot2, (ItemStack) recipeWrapper.getInputs().get(1));
            recipeLayout.getItemStacks().set(outputSlot, recipeWrapper.getOutputs());
        }
    }

}
