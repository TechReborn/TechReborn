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

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class RebornIngredient implements Predicate<ItemStack> {

	private final Identifier ingredientType;

	public RebornIngredient(Identifier ingredientType) {
		this.ingredientType = ingredientType;
	}

	@Override
	public abstract boolean test(ItemStack itemStack);

	public abstract Ingredient getPreview();

	public abstract List<ItemStack> getPreviewStacks();

	protected abstract JsonObject toJson();

	public abstract int getCount();

	//Same as above but adds the type
	public final JsonObject witeToJson() {
		JsonObject jsonObject = toJson();
		jsonObject.addProperty("type", ingredientType.toString());
		return jsonObject;
	}

	public <T extends RebornIngredient> void ifType(Class<T> clazz, Consumer<T> consumer) {
		if (this.getClass().isAssignableFrom(clazz)) {
			//noinspection unchecked
			consumer.accept((T) this);
		}
	}

}
