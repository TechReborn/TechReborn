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
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class StackIngredient extends RebornIngredient {
	private final ItemStack stack;

	private final Optional<Integer> count;
	private final ComponentChanges components;

	public StackIngredient(ItemStack stack, Optional<Integer> count, ComponentChanges components) {
		this.stack = stack;
		this.count = Objects.requireNonNull(count);
		this.components = Objects.requireNonNull(components);
		Validate.isTrue(!stack.isEmpty(), "ingredient must not empty");
	}

	public static RebornIngredient deserialize(JsonObject json) {
		Identifier identifier = Identifier.of(JsonHelper.getString(json, "item"));
		Item item = Registries.ITEM.getOrEmpty(identifier).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + identifier + "'"));

		Optional<Integer> stackSize = Optional.empty();
		if (json.has("count")) {
			stackSize = Optional.of(JsonHelper.getInt(json, "count"));
		}

		if (json.has("nbt")) {
			throw new JsonParseException("nbt is no longer supported");
		}

		ComponentChanges componentChanges = ComponentChanges.EMPTY;

		if (json.has("components")) {
			DataResult<ComponentChanges> result = ComponentChanges.CODEC.parse(DynamicRegistryManager.of(Registries.REGISTRIES).getOps(JsonOps.INSTANCE), json.get("components"));

			if (result.error().isPresent()) {
				throw new JsonParseException("Failed to parse components: " + result.error().get());
			}

			componentChanges = result.getOrThrow();
		}

		return new StackIngredient(new ItemStack(item), stackSize, componentChanges);
	}


	@Override
	public boolean test(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return false;
		}

		if (stack.getItem() != itemStack.getItem()) {
			return false;
		}

		if (count.isPresent() && count.get() > itemStack.getCount()) {
			return false;
		}

		for (Map.Entry<ComponentType<?>, Optional<?>> entry : components.entrySet()) {
			final ComponentType<?> type = entry.getKey();
			final Optional<?> value = entry.getValue();

			if (value.isPresent()) {
				// Expect the stack to contain a matching component
				if (!itemStack.contains(type)) {
					return false;
				}

				if (!Objects.equals(value.get(), itemStack.get(type))) {
					return false;
				}
			} else {
				// Expect the target stack to not contain this component
				if (itemStack.contains(type)) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public Ingredient getPreview() {
		return Ingredient.ofStacks(getPreviewStacks().stream());
	}

	@Override
	public List<ItemStack> getPreviewStacks() {
		ItemStack copy = stack.copy();
		copy.setCount(count.orElse(1));
		copy.applyChanges(components);
		return Collections.singletonList(copy);
	}

	@Override
	public JsonObject toJson(boolean networkSync) {
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("item", Registries.ITEM.getId(stack.getItem()).toString());
		count.ifPresent(integer -> jsonObject.addProperty("count", integer));

		if (!components.isEmpty()) {
			DataResult<JsonElement> result = ComponentChanges.CODEC.encodeStart(DynamicRegistryManager.of(Registries.REGISTRIES).getOps(JsonOps.INSTANCE), components);
			jsonObject.add("components", result.getOrThrow());
		}

		return jsonObject;
	}

	@Override
	public int getCount() {
		return count.orElse(1);
	}

	public ItemStack getStack() {
		return stack;
	}
}
