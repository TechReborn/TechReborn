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
import net.minecraft.client.resources.model.cuboid.CuboidFace
import net.minecraft.client.resources.model.cuboid.CuboidModelElement
import net.minecraft.client.resources.model.cuboid.CuboidRotation
import net.minecraft.client.resources.model.cuboid.ItemTransform
import net.minecraft.world.level.block.Block
import net.minecraft.client.data.models.model.ModelLocationUtils
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.resources.Identifier
import com.mojang.math.Quadrant
import net.minecraft.core.Direction
import org.jetbrains.annotations.Nullable
import org.joml.Vector3fc

class JsonModel {
	@Nullable
	Identifier parent
	@Nullable
	DisplayMap display
	@Nullable
	TextureMapping textures
	@Nullable
	List<CuboidModelElement> elements
	@Nullable
	CtmMap ctm

	@Nullable
	String variant
	@Nullable
	TextureSlot[] variantKeys
	@Nullable
	Identifier id

	JsonModel id(Object target) {
		if (target instanceof Block) {
			id = ModelLocationUtils.getModelLocation(target)
		} else if (target instanceof Item) {
			id = ModelLocationUtils.getModelLocation(target)
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

	JsonModel add(TextureMapping textures, TextureSlot... keys) {
		if (keys.length > 0) {
			this.variantKeys = keys
			this.textures = variant != null ? suffix(textures, keys, variant) : textures
		} else {
			this.textures = textures
		}
		return this
	}

	JsonModel add(List<CuboidModelElement> elements) {
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
		Identifier id = variant == null || variant == "_off" ? this.id : this.id.withSuffix(variant)
		ModelProvider.modelCollector.accept(id, () -> toJson())
		return id
	}

	static TextureMapping suffix(TextureMapping textures, TextureSlot[] keys, String variant) {
		TextureMapping map = textures
		for (int i = 0, len = keys.length; i < len; i++) {
			Identifier texture = map.get(keys[i]).withSuffix(variant)
			if (i == 0) {
				textures = map.copyAndUpdate(keys[0], texture)
			} else {
				textures.put(keys[i], texture)
			}
		}
		return textures
	}

	static class DisplayMap {
		final Map<ItemDisplayContext, ItemTransform> entries = new HashMap<>()
		DisplayMap create() {
			DisplayMap display = new DisplayMap()
			display.entries.putAll(entries)
			return display
		}
		DisplayMap put(ItemDisplayContext mode, ItemTransform transformation) {
			entries.put(mode, transformation)
			return this
		}
	}

	static class CtmMap {
		final int version = 1
		final TextureMapping entries = new TextureMapping()
		CtmMap put(TextureSlot key, Identifier id) {
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
			json.add(mode.getSerializedName(), toJson(transformation))
		})
		return json
	}

	private static JsonArray toJson(Vector3fc vector) {
		JsonArray data = new JsonArray()
		data.add(vector.x())
		data.add(vector.y())
		data.add(vector.z())
		return data
	}

	private static JsonObject toJson(ItemTransform transformation) {
		JsonObject json = new JsonObject()
		if (transformation.rotation().x() != 0 || transformation.rotation().y() != 0 || transformation.rotation().z() != 0) {
			json.add("rotation", toJson(transformation.rotation()))
		}
		if (transformation.translation().x() != 0 || transformation.translation().y() != 0 || transformation.translation().z() != 0) {
			json.add("translation", toJson(transformation.translation()))
		}
		if (transformation.scale().x() != 1 || transformation.translation().y() != 1 || transformation.translation().z() != 1) {
			json.add("scale", toJson(transformation.scale()))
		}
		return json
	}

	private static JsonObject toJson(TextureMapping texture) {
		JsonObject json = new JsonObject()
		texture.slots.forEach((key, value) -> {
			json.addProperty(key.getId(), value.toString())
		})
		return json
	}

	private static JsonObject toJson(CuboidRotation rotation) {
		JsonObject json = new JsonObject()
		json.addProperty("angle", rotation.angle())
		json.addProperty("axis", rotation.axis().getSerializedName())
		json.add("origin", toJson(rotation.origin()))
		if (rotation.rescale()) {
			json.addProperty("rescale", true)
		}
		return json
	}

	private static JsonArray toJson(CuboidFace.UVs uv) {
		JsonArray json = new JsonArray()
		json.add(uv.minU())
		json.add(uv.minV())
		json.add(uv.maxU())
		json.add(uv.maxV())
		return json
	}

	private static JsonObject toJson(CuboidFace face) {
		JsonObject json = new JsonObject()
		json.addProperty("texture", face.texture())
		Direction cullFace = face.cullForDirection()
		if (cullFace != null) {
			json.addProperty("cullface", cullFace.getSerializedName())
		}
		int tintIndex = face.tintIndex()
		if (tintIndex != -1) {
			json.addProperty("tintindex", tintIndex)
		}
		CuboidFace.UVs uv = face.uvs()
		if (uv != null) {
			json.add("uv", toJson(uv))
		}
		if (face.rotation() != Quadrant.R0) {
			json.addProperty("rotation", switch (face.rotation()) {
				case Quadrant.R90 -> 90
				case Quadrant.R180 -> 180
				case Quadrant.R270 -> 270
				default -> 0
			})
		}
		return json
	}

	private static JsonObject toJson(Map<Direction, CuboidFace> faces) {
		JsonObject json = new JsonObject()
		faces.forEach((direction, face) -> {
			json.add(direction.getSerializedName(), toJson(face))
		})
		return json
	}

	private static JsonObject toJson(CuboidModelElement element) {
		JsonObject json = new JsonObject()
		json.add("from", toJson(element.from()))
		json.add("to", toJson(element.to()))
		if (element.rotation() != null) {
			json.add("rotation", toJson(element.rotation()))
		}
		json.add("faces", toJson(element.faces()))
		return json
	}

	private static JsonArray toJson(List<CuboidModelElement> elements) {
		JsonArray json = new JsonArray()
		elements.forEach((element) -> {
			json.add(toJson(element))
		})
		return json
	}
}
