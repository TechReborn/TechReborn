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
import org.jetbrains.annotations.Nullable;

public class IngredientFactory {
	public static RebornIngredient deserialize(@Nullable JsonElement jsonElement) {
		if (jsonElement == null || !jsonElement.isJsonObject()) {
			throw new JsonParseException("ingredient must be a json object");
		}

		final JsonObject json = jsonElement.getAsJsonObject();

		if (json.has("type")) {
			throw new UnsupportedOperationException("type is unsupported");
		}

		if (json.has("fluid")) {
			return FluidIngredient.deserialize(json);
		} else if (json.has("tag") || json.has("tag_server_sync")) {
			return TagIngredient.deserialize(json);
		} else if (json.has("item")) {
			return StackIngredient.deserialize(json);
		} else {
			throw new UnsupportedOperationException("Unable to determine ingredient: " + json);
		}
	}
}
