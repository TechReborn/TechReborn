package techreborn.compat.jei.fusionReactor;

import mezz.jei.api.JEIManager;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidTanks;
import mezz.jei.api.gui.IGuiItemStacks;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import techreborn.client.gui.GuiFusionReactor;

import javax.annotation.Nonnull;
import java.util.Collection;

public class FusionReactorRecipeCategory implements IRecipeCategory {

    private static final int inputSlot1 = 0;
    private static final int inputSlot2 = 1;
    private static final int outputSlot = 2;

    @Nonnull
    private final IDrawable background;

    public FusionReactorRecipeCategory() {
        background = JEIManager.guiHelper.createDrawable(GuiFusionReactor.texture, 55, 16, 82, 54);
    }

    @Nonnull
    @Override
    public String getTitle() {
        return "Fustion Reactor";
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void init(@Nonnull IGuiItemStacks guiItemStacks, @Nonnull IGuiFluidTanks guiFluidTanks) {
        guiItemStacks.init(inputSlot1, 0, 0);
        guiItemStacks.init(inputSlot2, 0, 25);
        guiItemStacks.init(outputSlot, 60, 18);
    }

    @Override
    public void setRecipe(@Nonnull IGuiItemStacks guiItemStacks, @Nonnull IGuiFluidTanks guiFluidTanks, @Nonnull IRecipeWrapper recipeWrapper) {
        if(recipeWrapper instanceof FusionReactorRecipeWrapper){
            guiItemStacks.set(inputSlot1, (ItemStack) recipeWrapper.getInputs().get(0));
            guiItemStacks.set(inputSlot2, (ItemStack) recipeWrapper.getInputs().get(1));
            guiItemStacks.set(outputSlot, recipeWrapper.getOutputs());
        }
    }

}
