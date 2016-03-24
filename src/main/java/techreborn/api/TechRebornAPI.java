package techreborn.api;

import net.minecraft.item.ItemStack;
import techreborn.api.recipe.IRecipeCompact;

public final class TechRebornAPI {

    public static IRecipeCompact recipeCompact;

    public static void addRollingOreMachinceRecipe(ItemStack output,
                                                Object... components) {
        RollingMachineRecipe.instance.addShapedOreRecipe(output, components);
    }

    public static void addShapelessOreRollingMachinceRecipe(ItemStack output,
                                                         Object... components) {
        RollingMachineRecipe.instance.addShapelessOreRecipe(output, components);
    }

    public static void addRollingMachinceRecipe(ItemStack output,
                                                Object... components) {
        RollingMachineRecipe.instance.addRecipe(output, components);
    }

    public static void addShapelessRollingMachinceRecipe(ItemStack output,
                                                         Object... components) {
        RollingMachineRecipe.instance.addShapelessRecipe(output, components);
    }

}

class RegisteredItemRecipe extends Exception {
    public RegisteredItemRecipe(String message) {
        super(message);
    }
}
