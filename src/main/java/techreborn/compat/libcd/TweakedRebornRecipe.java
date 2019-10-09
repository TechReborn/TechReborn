package techreborn.compat.libcd;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.datafixers.NbtOps;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TweakedRebornRecipe extends RebornRecipe {

    public TweakedRebornRecipe(RebornRecipeType<?> type, Identifier name, List<Ingredient> inputs, List<ItemStack> outputs, int power, int time) {
        super(type, name);
        //TODO: PR to RebornCore to not require all this
        deserialize(hackConstruction(inputs, outputs, power, time));
    }

    public static JsonObject hackConstruction(List<Ingredient> inputs, List<ItemStack> outputs, int power, int time) {
        JsonObject jsonObject = new JsonObject();
        List<RebornIngredient> wrapped = new ArrayList<>();
        for (Ingredient ing : inputs) {
            wrapped.add(new WrappedIngredient(ing));
        }
        jsonObject.addProperty("power", power);
        jsonObject.addProperty("time", time);
        JsonArray ingredientsArray = new JsonArray();
        wrapped.stream().map(RebornIngredient::witeToJson).forEach(ingredientsArray::add);
        jsonObject.add("ingredients", ingredientsArray);
        JsonArray resultsArray = new JsonArray();

        JsonObject stackObject;
        for(Iterator var4 = outputs.iterator(); var4.hasNext(); resultsArray.add(stackObject)) {
            ItemStack stack = (ItemStack)var4.next();
            stackObject = new JsonObject();
            stackObject.addProperty("item", Registry.ITEM.getId(stack.getItem()).toString());
            if (stack.getCount() > 1) {
                stackObject.addProperty("count", stack.getCount());
            }

            if (stack.hasTag()) {
                jsonObject.add("tag", Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, stack.getTag()));
            }
        }

        jsonObject.add("results", resultsArray);
        return jsonObject;
    }
}
