package techreborn.compat.nei.recipes;

import codechicken.nei.PositionedStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import techreborn.api.recipe.IBaseRecipeType;

import java.util.List;

/**
 * Use this to make your neiHandler
 */
public interface INeiBaseRecipe {

    /**
     * Add the inputs and the outputs
     *
     * @param input   add the input stacks to this
     * @param outputs add this output stacks to this
     */
    public void addPositionedStacks(List<PositionedStack> input, List<PositionedStack> outputs, IBaseRecipeType recipeType);

    /**
     * @return the recipe name that is used for the recipe
     */
    public String getRecipeName();

    /**
     * @return the guiTexture location
     */
    public String getGuiTexture();

    /**
     * @return the gui class for the recipe
     */
    public Class<? extends GuiContainer> getGuiClass();
}
