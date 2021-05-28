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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import reborncore.mixin.common.AccessorIngredient;

import java.util.Arrays;
import java.util.List;

public class WrappedIngredient extends RebornIngredient {
	private Ingredient wrapped;

	public WrappedIngredient() {
		super(IngredientManager.WRAPPED_RECIPE_TYPE);
	}

	public WrappedIngredient(Ingredient wrapped) {
		this();
		this.wrapped = wrapped;
	}

	@Override
	public boolean test(ItemStack itemStack) {
		return wrapped.test(itemStack);
	}

	@Override
	public Ingredient getPreview() {
		return wrapped;
	}

	@Override
	public List<ItemStack> getPreviewStacks() {
		return Arrays.asList(((AccessorIngredient) (Object) wrapped).getMatchingStacks());
	}

	@Override
	protected JsonObject toJson() {
		if (wrapped.toJson() instanceof JsonObject) {
			return (JsonObject) wrapped.toJson();
		}
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("options", wrapped.toJson());
		return jsonObject;
	}

	@Override
	public int getCount() {
		return ((AccessorIngredient) (Object) wrapped).getMatchingStacks().length;
	}

	public static RebornIngredient deserialize(JsonObject jsonObject) {
		Ingredient underlying;
		if (jsonObject.has("options") && jsonObject.get("options") instanceof JsonArray) {
			underlying = Ingredient.fromJson(jsonObject.get("options"));
		} else {
			underlying = Ingredient.fromJson(jsonObject);
		}
		return new WrappedIngredient(underlying);
	}
}
