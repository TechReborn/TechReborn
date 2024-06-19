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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import reborncore.common.util.DefaultedListCollector;
import reborncore.common.util.serialization.SerializationUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class RecipeUtils {
	public static <T extends RebornRecipe> List<T> getRecipes(World world, RebornRecipeType<T> type) {
		return streamRecipeEntries(world, type).map(RecipeEntry::value).toList();
	}

	public static <T extends RebornRecipe> List<RecipeEntry<T>> getRecipeEntries(World world, RebornRecipeType<T> type) {
		return streamRecipeEntries(world, type).toList();
	}

	private static <T extends RebornRecipe> Stream<RecipeEntry<T>> streamRecipeEntries(World world, RebornRecipeType<T> type) {
		return world.getRecipeManager().getAllOfType(type).stream();
	}

	public static DefaultedList<ItemStack> deserializeItems(JsonElement jsonObject) {
		if (jsonObject.isJsonArray()) {
			return SerializationUtil.stream(jsonObject.getAsJsonArray()).map(entry -> deserializeItem(entry.getAsJsonObject())).collect(DefaultedListCollector.toList());
		} else {
			return DefaultedList.copyOf(deserializeItem(jsonObject.getAsJsonObject()));
		}
	}

	private static ItemStack deserializeItem(JsonObject jsonObject) {
		Identifier resourceLocation = Identifier.of(JsonHelper.getString(jsonObject, "item"));
		Item item = Registries.ITEM.get(resourceLocation);
		if (item == Items.AIR) {
			throw new IllegalStateException(resourceLocation + " did not exist");
		}
		int count = 1;
		if (jsonObject.has("count")) {
			count = JsonHelper.getInt(jsonObject, "count");
		}

		ItemStack stack = new ItemStack(item, count);

		if (jsonObject.has("nbt")) {
			throw new JsonParseException("nbt is no longer supported");
		}

		if (jsonObject.has("components")) {
			DataResult<ComponentChanges> result = ComponentChanges.CODEC.parse(DynamicRegistryManager.of(Registries.REGISTRIES).getOps(JsonOps.INSTANCE), jsonObject.get("components"));

			if (result.error().isPresent()) {
				throw new JsonParseException("Failed to parse components: " + result.error().get());
			}

			stack.applyChanges(result.getOrThrow());
		}

		return stack;
	}

	/**
	 * Adds the following toast/recipe defaults to an advancement builder:
	 * <ul>
	 *     <li>parent as "recipes/root"</li>
	 *     <li>criterion "has_the_recipe" via OR</li>
	 *     <li>reward: the specified recipe</li>
	 * </ul>
	 * @param builder the advancement task builder to expand
	 * @param recipeId the ID of the recipe
	 * @throws NullPointerException If any parameter refers to <code>null</code>.
	 */
	public static void addToastDefaults(@NotNull Advancement.Builder builder, @NotNull Identifier recipeId) {
		Objects.requireNonNull(builder);
		Objects.requireNonNull(recipeId);
		builder
			.criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
			.rewards(AdvancementRewards.Builder.recipe(recipeId))
			.criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
	}

}
