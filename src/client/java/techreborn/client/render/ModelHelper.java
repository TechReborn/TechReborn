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
import techreborn.TechReborn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;

public class ModelHelper {

	public static final ItemTransforms DEFAULT_ITEM_TRANSFORMS = loadTransformFromJson(Identifier.parse("minecraft:models/item/generated"));
	public static final ItemTransforms HANDHELD_ITEM_TRANSFORMS = loadTransformFromJson(Identifier.parse("minecraft:models/item/handheld"));

	public static ItemTransforms loadTransformFromJson(Identifier location) {
		try {

			return BlockModel.fromStream(getReaderForResource(location)).transforms();
		} catch (IOException exception) {
			TechReborn.LOGGER.warn("Can't load resource " + location);
			exception.printStackTrace();
			return null;
		}
	}

	public static Reader getReaderForResource(Identifier location) throws IOException {
		Identifier file = Identifier.fromNamespaceAndPath(location.getNamespace(), location.getPath() + ".json");
		Resource resource = Minecraft.getInstance().getResourceManager().getResource(file).orElseThrow();
		return new BufferedReader(new InputStreamReader(resource.open(), Charsets.UTF_8));
	}

}
