package techreborn.compat.libcd;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipeType;
import techreborn.api.recipe.recipes.BlastFurnaceRecipe;

import java.util.List;

public class TweakedBlastFurnaceRecipe extends BlastFurnaceRecipe {
    public TweakedBlastFurnaceRecipe(RebornRecipeType<?> type, Identifier name, List<Ingredient> inputs, List<ItemStack> outputs, int power, int time, int heat) {
        super(type, name);
        deserialize(hackConstruction(inputs, outputs, power, time, heat));
    }

    private static JsonObject hackConstruction(List<Ingredient> inputs, List<ItemStack> outputs,  int power, int time, int heat) {
        JsonObject jsonObject = TweakedRebornRecipe.hackConstruction(inputs, outputs, power, time);
        jsonObject.addProperty("heat", heat);
        return jsonObject;
    }
}
