/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package reborncore.common.crafting.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.function.Function;

public class IngredientManager {

	public static final Identifier STACK_RECIPE_TYPE = new Identifier("reborncore", "stack");
	public static final Identifier FLUID_RECIPE_TYPE = new Identifier("reborncore", "fluid");
	public static final Identifier TAG_RECIPE_TYPE = new Identifier("reborncore", "tag");
	public static final Identifier WRAPPED_RECIPE_TYPE = new Identifier("reborncore", "wrapped");

	private static final HashMap<Identifier, Function<JsonObject, RebornIngredient>> recipeTypes = new HashMap<>();

	public static void setup() {
		recipeTypes.put(STACK_RECIPE_TYPE, StackIngredient::deserialize);
		recipeTypes.put(FLUID_RECIPE_TYPE, FluidIngredient::deserialize);
		recipeTypes.put(TAG_RECIPE_TYPE, TagIngredient::deserialize);
		recipeTypes.put(WRAPPED_RECIPE_TYPE, WrappedIngredient::deserialize);
	}

	public static RebornIngredient deserialize(@Nullable JsonElement jsonElement) {
		if (jsonElement == null || !jsonElement.isJsonObject()) {
			throw new JsonParseException("ingredient must be a json object");
		}

		JsonObject json = jsonElement.getAsJsonObject();

		Identifier recipeTypeIdent = STACK_RECIPE_TYPE;
		//TODO find a better way to do this.
		if (json.has("fluid")) {
			recipeTypeIdent = FLUID_RECIPE_TYPE;
		} else if (json.has("tag")) {
			recipeTypeIdent = TAG_RECIPE_TYPE;
		} else if (json.has("wrapped")) {
			recipeTypeIdent = WRAPPED_RECIPE_TYPE;
		}

		if (json.has("type")) {
			recipeTypeIdent = new Identifier(JsonHelper.getString(json, "type"));
		}

		Function<JsonObject, RebornIngredient> recipeTypeFunction = recipeTypes.get(recipeTypeIdent);
		if (recipeTypeFunction == null) {
			throw new JsonParseException("No recipe type found for " + recipeTypeIdent.toString());
		}
		return recipeTypeFunction.apply(json);
	}

}
