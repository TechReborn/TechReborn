package techreborn.compat.libcd;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipeType;
import techreborn.api.recipe.recipes.IndustrialSawmillRecipe;

import java.util.List;

public class TweakedIndustrialSawmillRecipe extends IndustrialSawmillRecipe {
    public TweakedIndustrialSawmillRecipe(RebornRecipeType<?> type, Identifier name, List<Ingredient> input, List<ItemStack> output, int power, int time) {
        super(type, name);
        deserialize(TweakedRebornRecipe.hackConstruction(input, output, power, time));
    }
}
