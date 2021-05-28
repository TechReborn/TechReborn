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
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StackIngredient extends RebornIngredient {

	private final List<ItemStack> stacks;

	private final Optional<Integer> count;
	private final Optional<CompoundTag> tag;
	private final boolean requireEmptyTag;

	public StackIngredient(List<ItemStack> stacks, Optional<Integer> count, Optional<CompoundTag> tag, boolean requireEmptyTag) {
		super(IngredientManager.STACK_RECIPE_TYPE);
		this.stacks = stacks;
		this.count = count;
		this.tag = tag;
		this.requireEmptyTag = requireEmptyTag;
		Validate.isTrue(stacks.size() == 1, "stack size must 1");
	}

	public static RebornIngredient deserialize(JsonObject json) {
		if (!json.has("item")) {
			System.out.println("nope");
		}
		Identifier identifier = new Identifier(JsonHelper.getString(json, "item"));
		Item item = Registry.ITEM.getOrEmpty(identifier).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + identifier + "'"));

		Optional<Integer> stackSize = Optional.empty();
		if (json.has("count")) {
			stackSize = Optional.of(JsonHelper.getInt(json, "count"));
		}

		Optional<CompoundTag> tag = Optional.empty();
		boolean requireEmptyTag = false;

		if (json.has("nbt")) {
			if (!json.get("nbt").isJsonObject()) {
				if (json.get("nbt").getAsString().equals("empty")) {
					requireEmptyTag = true;
				}
			} else {
				tag = Optional.of((CompoundTag) Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, json.get("nbt")));
			}
		}

		return new StackIngredient(Collections.singletonList(new ItemStack(item)), stackSize, tag, requireEmptyTag);
	}


	@Override
	public boolean test(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return false;
		}
		if (stacks.stream().noneMatch(recipeStack -> recipeStack.getItem() == itemStack.getItem())) {
			return false;
		}
		if (count.isPresent() && count.get() > itemStack.getCount()) {
			return false;
		}
		if (tag.isPresent()) {
			if (!itemStack.hasTag()) {
				return false;
			}

			//Bit of a meme here, as DataFixer likes to use the most basic primative type over using an int.
			//So we have to go to json and back on the incoming stack to be sure its using types that match our input.

			CompoundTag compoundTag = itemStack.getTag();
			JsonElement jsonElement = Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, compoundTag);
			compoundTag = (CompoundTag) Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, jsonElement);

			if (!tag.get().equals(compoundTag)) {
				return false;
			}
		}
		return !requireEmptyTag || !itemStack.hasTag();
	}

	@Override
	public Ingredient getPreview() {
		return Ingredient.ofStacks(getPreviewStacks().stream());
	}

	@Override
	public List<ItemStack> getPreviewStacks() {
		return Collections.unmodifiableList(
				stacks.stream()
						.map(ItemStack::copy)
						.peek(itemStack -> itemStack.setCount(count.orElse(1)))
						.peek(itemStack -> itemStack.setTag(tag.orElse(null)))
						.collect(Collectors.toList()));
	}

	@Override
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("item", Registry.ITEM.getId(stacks.get(0).getItem()).toString());
		count.ifPresent(integer -> jsonObject.addProperty("count", integer));

		if (requireEmptyTag) {
			jsonObject.addProperty("nbt", "empty");
		} else {
			tag.ifPresent(compoundTag -> jsonObject.add("nbt", Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, compoundTag)));
		}

		return jsonObject;
	}

	@Override
	public int getCount() {
		return count.orElse(1);
	}
}
