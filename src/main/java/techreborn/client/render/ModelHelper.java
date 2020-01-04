/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.client.render;

import com.google.common.base.Charsets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/*
 * Credits to JsonDestroyer
 */
@Environment(EnvType.CLIENT)
public class ModelHelper {

	public static final ModelTransformation DEFAULT_ITEM_TRANSFORMS = loadTransformFromJson(new Identifier("minecraft:models/item/generated"));
	public static final ModelTransformation HANDHELD_ITEM_TRANSFORMS = loadTransformFromJson(new Identifier("minecraft:models/item/handheld"));

	public static ModelTransformation loadTransformFromJson(Identifier location) {
		try {

			return JsonUnbakedModel.deserialize(getReaderForResource(location)).getTransformations();
		} catch (IOException exception) {
			TechReborn.LOGGER.warn("Can't load resource " + location);
			exception.printStackTrace();
			return null;
		}
	}

	public static Reader getReaderForResource(Identifier location) throws IOException {
		Identifier file = new Identifier(location.getNamespace(), location.getPath() + ".json");
		Resource iresource = MinecraftClient.getInstance().getResourceManager().getResource(file);
		return new BufferedReader(new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8));
	}

}
