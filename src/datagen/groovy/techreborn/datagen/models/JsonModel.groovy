/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TechReborn
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

package techreborn.datagen.models

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.block.Block
import net.minecraft.client.data.ModelIds
import net.minecraft.client.data.TextureKey
import net.minecraft.client.data.TextureMap
import net.minecraft.client.render.model.json.ModelElement
import net.minecraft.client.render.model.json.ModelElementFace
import net.minecraft.client.render.model.json.ModelElementTexture
import net.minecraft.client.render.model.json.ModelRotation
import net.minecraft.client.render.model.json.Transformation
import net.minecraft.item.Item
import net.minecraft.item.ModelTransformationMode
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import org.jetbrains.annotations.Nullable
import org.joml.Vector3f

class JsonModel {
	@Nullable
	Identifier parent
	@Nullable
	DisplayMap display
	@Nullable
	TextureMap textures
	@Nullable
	List<ModelElement> elements
	@Nullable
	CtmMap ctm

	@Nullable
	String variant
	@Nullable
	TextureKey[] variantKeys
	@Nullable
	Identifier id

	JsonModel id(Object target) {
		if (target instanceof Block) {
			id = ModelIds.getBlockModelId(target)
		} else if (target instanceof Item) {
			id = ModelIds.getItemModelId(target)
		} else if (target instanceof Identifier) {
			id = target
		} else {
			throw new IllegalArgumentException("Unknown target type: $target")
		}
		return this
	}

	JsonModel create(Object target) {
		return create().id(target)
	}

	JsonModel create() {
		JsonModel model = new JsonModel()
		model.parent = parent
		model.display = display
		model.textures = textures
		model.elements = elements
		model.variant = variant
		model.variantKeys = variantKeys
		model.id = id
		return model
	}

	JsonModel add(Identifier parent) {
		this.parent = parent
		return this
	}

	JsonModel add(DisplayMap display) {
		this.display = display
		return this
	}

	JsonModel add(CtmMap ctm) {
		this.ctm = ctm
		return this
	}

	JsonModel add(TextureMap textures, TextureKey... keys) {
		if (keys.length > 0) {
			this.variantKeys = keys
			this.textures = variant != null ? suffix(textures, keys, variant) : textures
		} else {
			this.textures = textures
		}
		return this
	}

	JsonModel add(List<ModelElement> elements) {
		this.elements = elements
		return this
	}

	JsonModel suffix(String variant) {
		if (textures != null && variantKeys != null) {
			JsonModel model = create()
			model.variant = variant
			model.textures = suffix(textures, variantKeys, variant)
			return model
		} else {
			this.variant = variant
			return this
		}
	}

	Identifier upload() {
		if (this.id == null) throw new IllegalStateException("No target specified")
		Identifier id = variant == null || variant == "_off" ? this.id : this.id.withSuffixedPath(variant)
		ModelProvider.modelCollector.accept(id, () -> toJson())
		return id
	}

	static TextureMap suffix(TextureMap textures, TextureKey[] keys, String variant) {
		TextureMap map = textures
		for (int i = 0, len = keys.length; i < len; i++) {
			Identifier texture = map.getTexture(keys[i]).withSuffixedPath(variant)
			if (i == 0) {
				textures = map.copyAndAdd(keys[0], texture)
			} else {
				textures.put(keys[i], texture)
			}
		}
		return textures
	}

	static class DisplayMap {
		final Map<ModelTransformationMode, Transformation> entries = new HashMap<>()
		DisplayMap create() {
			DisplayMap display = new DisplayMap()
			display.entries.putAll(entries)
			return display
		}
		DisplayMap put(ModelTransformationMode mode, Transformation transformation) {
			entries.put(mode, transformation)
			return this
		}
	}

	static class CtmMap {
		final int version = 1
		final TextureMap entries = new TextureMap()
		CtmMap put(TextureKey key, Identifier id) {
			entries.put(key, id)
			return this
		}
	}

	private JsonObject toJson() {
		JsonObject json = new JsonObject()
		if (parent != null) json.addProperty("parent", parent.toString())
		if (display != null) json.add("display", toJson(display))
		if (textures != null) json.add("textures", toJson(textures))
		if (elements != null) json.add("elements", toJson(elements))
		if (ctm != null) {
			json.addProperty("ctm_version", ctm.version)
			json.add("ctm_overrides", toJson(ctm.entries))
		}
		return json
	}

	private static JsonObject toJson(DisplayMap display) {
		JsonObject json = new JsonObject()
		display.entries.forEach((mode, transformation) -> {
			json.add(mode.asString(), toJson(transformation))
		})
		return json
	}

	private static JsonArray toJson(Vector3f vector) {
		JsonArray data = new JsonArray()
		data.add(vector.x)
		data.add(vector.y)
		data.add(vector.z)
		return data
	}

	private static JsonObject toJson(Transformation transformation) {
		JsonObject json = new JsonObject()
		if (transformation.rotation.x != 0 || transformation.rotation.y != 0 || transformation.rotation.z != 0) {
			json.add("rotation", toJson(transformation.rotation))
		}
		if (transformation.translation.x != 0 || transformation.translation.y != 0 || transformation.translation.z != 0) {
			json.add("translation", toJson(transformation.translation))
		}
		if (transformation.scale.x != 1 || transformation.translation.y != 1 || transformation.translation.z != 1) {
			json.add("scale", toJson(transformation.scale))
		}
		return json
	}

	private static JsonObject toJson(TextureMap texture) {
		JsonObject json = new JsonObject()
		texture.entries.forEach((key, value) -> {
			json.addProperty(key.getName(), value.toString())
		})
		return json
	}

	private static JsonObject toJson(ModelRotation rotation) {
		JsonObject json = new JsonObject()
		json.addProperty("angle", rotation.angle())
		json.addProperty("axis", rotation.axis().asString())
		json.add("origin", toJson(rotation.origin()))
		if (rotation.rescale()) {
			json.addProperty("rescale", true)
		}
		return json
	}

	private static JsonArray toJson(float[] uvs) {
		JsonArray json = new JsonArray()
		for (final float uv in uvs) {
			json.add(uv)
		}
		return json
	}

	private static JsonObject toJson(ModelElementFace face) {
		JsonObject json = new JsonObject()
		json.addProperty("texture", face.textureId())
		Direction cullFace = face.cullFace()
		if (cullFace != null) {
			json.addProperty("cullface", cullFace.asString())
		}
		int tintIndex = face.tintIndex()
		if (tintIndex != -1) {
			json.addProperty("tintindex", tintIndex)
		}
		ModelElementTexture textureData = face.textureData()
		if (textureData.uvs != null) {
			json.add("uv", toJson(textureData.uvs))
		}
		if (textureData.rotation != 0) {
			json.addProperty("rotation", textureData.rotation)
		}
		return json
	}

	private static JsonObject toJson(Map<Direction, ModelElementFace> faces) {
		JsonObject json = new JsonObject()
		faces.forEach((direction, face) -> {
			json.add(direction.asString(), toJson(face))
		})
		return json
	}

	private static JsonObject toJson(ModelElement element) {
		JsonObject json = new JsonObject()
		json.add("from", toJson(element.from))
		json.add("to", toJson(element.to))
		if (element.rotation != null) {
			json.add("rotation", toJson(element.rotation))
		}
		json.add("faces", toJson(element.faces))
		return json
	}

	private static JsonArray toJson(List<ModelElement> elements) {
		JsonArray json = new JsonArray()
		elements.forEach((element) -> {
			json.add(toJson(element))
		})
		return json
	}
}
