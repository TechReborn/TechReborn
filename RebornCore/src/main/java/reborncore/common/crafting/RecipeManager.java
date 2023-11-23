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

import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import reborncore.common.crafting.serde.RebornRecipeSerde;
import reborncore.common.crafting.serde.RecipeSerde;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecipeManager {

	private static final Map<Identifier, RebornRecipeType<?>> recipeTypes = new HashMap<>();

	public static RebornRecipeType<reborncore.common.crafting.RebornRecipe> newRecipeType(Identifier name) {
		return newRecipeType(RebornRecipeSerde.BASIC, name);
	}

	public static <R extends RebornRecipe> RebornRecipeType<R> newRecipeType(RecipeSerde<R> recipeSerde, Identifier name) {
		if (recipeTypes.containsKey(name)) {
			throw new IllegalStateException("RebornRecipe type with this name already registered");
		}
		RebornRecipeType<R> type = new RebornRecipeType<>(recipeSerde, name);
		recipeTypes.put(name, type);

		Registry.register(Registries.RECIPE_TYPE, name, type);
		Registry.register(Registries.RECIPE_SERIALIZER, name, type);

		return type;
	}

	public static RebornRecipeType<?> getRecipeType(Identifier name) {
		if (!recipeTypes.containsKey(name)) {
			throw new IllegalStateException("RebornRecipe type " + name + " not found");
		}
		return recipeTypes.get(name);
	}

	public static List<RebornRecipeType<?>> getRecipeTypes(String namespace) {
		return recipeTypes.values().stream().filter(rebornRecipeType -> rebornRecipeType.name().getNamespace().equals(namespace)).collect(Collectors.toList());
	}
}
