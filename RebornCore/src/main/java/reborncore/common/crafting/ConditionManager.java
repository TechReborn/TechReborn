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

package reborncore.common.crafting;

import java.util.HashMap;
import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import org.apache.commons.lang3.Validate;

import net.fabricmc.loader.api.FabricLoader;

public final class ConditionManager {

	private static final HashMap<Identifier, RecipeCondition<?>> RECIPE_CONDITIONS = new HashMap<>();
	private static final HashMap<Identifier, Class<?>> RECIPE_CONDITION_TYPES = new HashMap<>();

	static {
		//Only loads the recipe in a development env
		register("development", Boolean.class, (bool) -> bool == FabricLoader.getInstance().isDevelopmentEnvironment());

		//Only loads the recipe when the item is registered
		register("item", Identifier.class, (id) -> registryContains(Registry.ITEM, id));

		//Only loads the recipe when the fluid is registered
		register("fluid", Identifier.class, (id) -> registryContains(Registry.FLUID, id));

		//Only loads the recipe when the tag is loaded
		register("tag", Identifier.class, s -> ItemTags.getTagGroup().getTags().containsKey(s));

		//Only load the recipe if the provided mod is loaded
		register("mod", String.class, s -> FabricLoader.getInstance().isModLoaded(s));

		//Never load, just pass whatever in as the string
		register("never", String.class, s -> false);
	}

	private static boolean registryContains(SimpleRegistry<?> registry, Identifier ident) {
		return registry.containsId(ident);
	}

	public static <T> void register(String name, Class<T> type, RecipeCondition<T> recipeCondition){
		register(new Identifier("reborncore", name), type, recipeCondition);
	}

	public static <T> void register(Identifier identifier, Class<T> type, RecipeCondition<T> recipeCondition){
		Validate.isTrue(!RECIPE_CONDITIONS.containsKey(identifier), "Recipe condition already registered");
		RECIPE_CONDITIONS.put(identifier, recipeCondition);
		RECIPE_CONDITION_TYPES.put(identifier, type);
	}

	public static RecipeCondition<?> getRecipeCondition(Identifier identifier){
		RecipeCondition<?> condition = RECIPE_CONDITIONS.get(identifier);
		if(condition == null){
			throw new UnsupportedOperationException("Could not find recipe condition for " + identifier.toString());
		}
		return condition;
	}

	public static boolean shouldLoadRecipe(JsonObject jsonObject){
		if(!jsonObject.has("conditions")) return true;
		return jsonObject.get("conditions").getAsJsonObject().entrySet().stream()
			.allMatch(entry -> shouldLoad(entry.getKey(), entry.getValue()));
	}

	public static boolean shouldLoad(String ident, JsonElement jsonElement){
		Identifier identifier = parseIdent(ident);
		RecipeCondition<?> recipeCondition = getRecipeCondition(identifier);
		Class<?> type = RECIPE_CONDITION_TYPES.get(identifier);
		return shouldLoad(type, jsonElement, recipeCondition);
	}

	@SuppressWarnings("unchecked")
	private static boolean shouldLoad(Class type, JsonElement jsonElement, RecipeCondition recipeCondition){
		Object val = TypeHelper.getValue(type, jsonElement);
		return recipeCondition.shouldLoad(val);
	}

	private static Identifier parseIdent(String string) {
		if(string.contains(":")){
			return new Identifier(string);
		}
		return new Identifier("reborncore", string);
	}

	@FunctionalInterface
	public interface RecipeCondition<T> {
		boolean shouldLoad(T t);
	}

	private static class TypeHelper {

		private static final HashMap<Class<?>, Function<JsonElement, ?>> FUNCTIONS = new HashMap<>();

		static {
			register(String.class, JsonElement::getAsString);
			register(Boolean.class, JsonElement::getAsBoolean);

			register(Identifier.class, element -> new Identifier(element.getAsString()));
		}

		private static <T> void register(Class<T> type, Function<JsonElement, T> function){
			Validate.isTrue(!FUNCTIONS.containsKey(type), "Function for this class is already registered");
			FUNCTIONS.put(type, function);
		}

		public static <T> T getValue(Class<T> type, JsonElement jsonElement){
			Validate.isTrue(FUNCTIONS.containsKey(type), "Function for this class could not be found");
			//noinspection unchecked
			return (T) FUNCTIONS.get(type).apply(jsonElement);
		}

	}
}
