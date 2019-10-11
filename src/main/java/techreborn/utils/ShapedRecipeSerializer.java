package techreborn.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

//TODO: switch RebornRecipes over to proper packet-based serialization instead of JSON packets
public class ShapedRecipeSerializer {
    public static final String[] CHARACTERS = new String[]{ "#", "/", "_", "^", "&", "%", "@", ".", "$" };

    public static JsonObject writeToJson(ShapedRecipe recipe, JsonObject jsonObject) {

        jsonObject.addProperty("type", "minecraft:crafting_shaped");

        int rows = recipe.getHeight();
        int columns = recipe.getWidth();
        String[] representations = new String[rows];
        int charsUsed = 0;
        Map<Ingredient, String> ingMap = new HashMap<>();

        for (int i = 0; i < rows; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < columns; j++) {
                Ingredient ing = recipe.getPreviewInputs().get((i * columns) + j);
                if (ing == Ingredient.EMPTY) builder.append(" ");
                else {
                    if (ingMap.containsKey(ing)) {
                        builder.append(ingMap.get(ing));
                    } else {
                        String toUse = CHARACTERS[charsUsed];
                        builder.append(toUse);
                        ingMap.put(ing, toUse);
                        charsUsed++;
                    }
                }
            }
            representations[i] = builder.toString();
        }

        JsonArray array = new JsonArray();
        for (int i = 0; i < representations.length; i++) {
            array.add(representations[i]);
        }

        jsonObject.add("pattern", array);

        JsonObject dict = new JsonObject();
        for (Ingredient ing : ingMap.keySet()) {
            String toUse = ingMap.get(ing);
            dict.add(toUse, ing.toJson());
        }

        jsonObject.add("key", dict);

        ItemStack stack = recipe.getOutput();
        JsonObject stackObject = new JsonObject();
        stackObject.addProperty("item", Registry.ITEM.getId(stack.getItem()).toString());
        if (stack.getCount() > 1) {
            stackObject.addProperty("count", stack.getCount());
        }
        jsonObject.add("result", stackObject);

        return jsonObject;
    }
}
