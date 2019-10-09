package techreborn.compat.libcd;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipeType;
import techreborn.api.recipe.recipes.FusionReactorRecipe;

import java.util.List;

public class TweakedFusionReactorRecipe extends FusionReactorRecipe {
    public TweakedFusionReactorRecipe(RebornRecipeType<?> type, Identifier name, List<Ingredient> inputs, List<ItemStack> outputs, int power, int time, int startE, int minSize) {
        super(type, name);
        deserialize(hackConstruction(inputs, outputs, power, time, startE, minSize));
    }

    private static JsonObject hackConstruction(List<Ingredient> inputs, List<ItemStack> outputs, int power, int time, int startE, int minSize) {
        JsonObject jsonObject = TweakedRebornRecipe.hackConstruction(inputs, outputs, power, time);
        jsonObject.addProperty("start-power", startE);
        jsonObject.addProperty("min-size", minSize);
        return jsonObject;
    }
}
