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

import net.minecraft.block.Block
import net.minecraft.client.data.TextureKey
import net.minecraft.client.data.TextureMap
import net.minecraft.client.render.model.MissingModel
import net.minecraft.client.render.model.json.ModelElement
import net.minecraft.client.render.model.json.ModelElementFace
import net.minecraft.client.render.model.json.ModelElementTexture
import net.minecraft.client.render.model.json.ModelRotation
import net.minecraft.client.render.model.json.Transformation
import net.minecraft.item.Item
import net.minecraft.item.ModelTransformationMode
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import org.apache.commons.lang3.function.TriFunction
import org.apache.commons.lang3.tuple.Pair
import org.joml.Vector3f

import java.util.function.BiFunction
import java.util.function.Function

class TemplateModel {
	static TextureKey KEY_ZERO = TextureKey.of("0")
	static TextureKey KEY_ONE = TextureKey.of("1")
	static TextureKey KEY_TWO = TextureKey.of("2")
	static TextureKey KEY_THREE = TextureKey.of("3")
	static TextureKey KEY_FOUR = TextureKey.of("4")
	static TextureKey KEY_MISSING = TextureKey.of("missing")
	static Identifier HANDHELD = Identifier.ofVanilla("item/handheld")
	static JsonModel GENERATED = new JsonModel().add(Identifier.ofVanilla("item/generated"))
	static JsonModel ORIENTABLE = new JsonModel().add(Identifier.ofVanilla("block/orientable"))
	static JsonModel CUBE_BOTTOM_TOP = new JsonModel().add(Identifier.ofVanilla("block/cube_bottom_top"))
	static JsonModel BLOCK = new JsonModel().add(Identifier.ofVanilla("block/block"))
	static Uploadable CUBE_ALL = (Block block) -> new JsonModel()
		.add(Identifier.ofVanilla("block/cube_all")).id(block).add(TextureMap.all(block), TextureKey.ALL)
	static JsonModel.DisplayMap CELL_DISPLAY = new JsonModel.DisplayMap()
		.put(ModelTransformationMode.GROUND, transformation(0, 0, 0, 0, 2, 0, 0.5, 0.5, 0.5))
		.put(ModelTransformationMode.HEAD, transformation(0, 180, 0, 0, 13, 7, 1, 1, 1))
		.put(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, transformation(0, 0, 0, 0, 3, 1, 0.55, 0.55, 0.55))
		.put(ModelTransformationMode.FIRST_PERSON_RIGHT_HAND, transformation(0, -90, 25, 1.13, 3.2, 1.13, 0.68, 0.68, 0.68))
		.put(ModelTransformationMode.FIXED, transformation(0, 180, 0, 0, 0, 0, 1, 1, 1))
	static JsonModel CELL_TEMPLATE = new JsonModel().add(CELL_DISPLAY).add(List.of(
		new ModelElement(new Vector3f(7, 4, 7.5), new Vector3f(10, 12, 8.5), Map.of(
			Direction.NORTH, face(TextureKey.TEXTURE, uvs(7, 4, 10, 12)),
			Direction.SOUTH, face(TextureKey.TEXTURE, uvs(7, 4, 10, 12)),
		))
	))
	static JsonModel BUCKET_TEMPLATE = new JsonModel().add(CELL_DISPLAY).add(List.of(
		new ModelElement(new Vector3f(4, 11, 7.5), new Vector3f(12, 13, 8.5), Map.of(
			Direction.NORTH, face(TextureKey.TEXTURE, uvs(4, 3, 12, 5)),
			Direction.SOUTH, face(TextureKey.TEXTURE, uvs(4, 3, 12, 5)),
		)),
		new ModelElement(new Vector3f(5, 10, 7.5), new Vector3f(11, 11, 8.5), Map.of(
			Direction.NORTH, face(TextureKey.TEXTURE, uvs(5, 5, 11, 6)),
			Direction.SOUTH, face(TextureKey.TEXTURE, uvs(5, 5, 11, 6)),
		)),
		new ModelElement(new Vector3f(3, 11, 7.5), new Vector3f(4, 12, 8.5), Map.of(
			Direction.NORTH, face(TextureKey.TEXTURE, uvs(12, 4, 13, 5)),
			Direction.SOUTH, face(TextureKey.TEXTURE, uvs(12, 4, 13, 5)),
		)),
		new ModelElement(new Vector3f(12, 11, 7.5), new Vector3f(13, 12, 8.5), Map.of(
			Direction.NORTH, face(TextureKey.TEXTURE, uvs(3, 4, 4, 5)),
			Direction.SOUTH, face(TextureKey.TEXTURE, uvs(3, 4, 4, 5)),
		)),
	))
	static ModelElementFace CABLE_FACE_1 = face(TextureKey.TEXTURE, uvs(1, 1, 5, 5))
	static ModelElementFace CABLE_FACE_2 = face(TextureKey.TEXTURE, uvs(0, 7, 6, 11))
	static ModelElementFace CABLE_FACE_3 = face(TextureKey.TEXTURE, uvs(0, 7, 6, 11), 90)
	static ModelElementFace CABLE_FACE_4 = face(TextureKey.TEXTURE, uvs(0, 0, 6, 6))
	static ModelElementFace CABLE_FACE_5 = face(TextureKey.TEXTURE, uvs(0, 6, 5, 12))
	static ModelElementFace CABLE_FACE_6 = face(TextureKey.TEXTURE, uvs(0, 6, 6, 12), 90)
	static List<ModelElement> CABLE_CORE_ELEMENT = List.of(
		new ModelElement(new Vector3f(6, 6, 6), new Vector3f(10, 10, 10), Map.of(
			Direction.NORTH, CABLE_FACE_1, Direction.EAST, CABLE_FACE_1, Direction.SOUTH, CABLE_FACE_1,
			Direction.WEST, CABLE_FACE_1, Direction.UP, CABLE_FACE_1, Direction.DOWN, CABLE_FACE_1
		))
	)
	static List<ModelElement> CABLE_SIDE_ELEMENT = List.of(
		new ModelElement(new Vector3f(6, 6, 0), new Vector3f(10, 10, 6), Map.of(
			Direction.NORTH, CABLE_FACE_1, Direction.EAST, CABLE_FACE_2,
			Direction.SOUTH, new ModelElementFace(
				Direction.SOUTH, -1, TextureKey.TEXTURE.toString(),
				new ModelElementTexture(uvs(1, 1, 5, 5), 0)
			),
			Direction.WEST, CABLE_FACE_2, Direction.UP, CABLE_FACE_3, Direction.DOWN, CABLE_FACE_3
		))
	)
	static List<ModelElement> CABLE_THICK_CORE_ELEMENT = List.of(
		new ModelElement(new Vector3f(5, 5, 5), new Vector3f(11, 11, 11), Map.of(
			Direction.NORTH, CABLE_FACE_4, Direction.EAST, CABLE_FACE_4, Direction.SOUTH, CABLE_FACE_4,
			Direction.WEST, CABLE_FACE_4, Direction.UP, CABLE_FACE_4, Direction.DOWN, CABLE_FACE_4
		))
	)
	static List<ModelElement> CABLE_THICK_SIDE_ELEMENT = List.of(
		new ModelElement(new Vector3f(5, 5, 0), new Vector3f(11, 11, 5), Map.of(
			Direction.NORTH, CABLE_FACE_4, Direction.EAST, CABLE_FACE_5,
			Direction.SOUTH, new ModelElementFace(
				Direction.SOUTH, -1, TextureKey.TEXTURE.toString(),
				new ModelElementTexture(uvs(0, 0, 6, 6), 0)
			),
			Direction.WEST, CABLE_FACE_5, Direction.UP, CABLE_FACE_6, Direction.DOWN, CABLE_FACE_6
		))
	)
	static JsonModel.DisplayMap LIGHT_DISPLAY_BASE = new JsonModel.DisplayMap()
		.put(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, transformation(75, 45, 0, 0, 2.5, 2, 0.375, 0.375, 0.375))
		.put(ModelTransformationMode.FIRST_PERSON_LEFT_HAND, transformation(0, 225, 0, 0, 4.2, 0, 0.40, 0.40, 0.40))
	static JsonModel.DisplayMap LIGHT_DISPLAY_1 = LIGHT_DISPLAY_BASE.create()
		.put(ModelTransformationMode.FIRST_PERSON_RIGHT_HAND, transformation(0, 35, 0 , 0, 5.5, 0, 0.60, 0.60, 0.60))
	static JsonModel.DisplayMap LIGHT_DISPLAY_2 = LIGHT_DISPLAY_BASE.create()
		.put(ModelTransformationMode.FIRST_PERSON_RIGHT_HAND, transformation(0, 35, 0 , 0, 4.3, 0, 0.60, 0.60, 0.60))
	static ModelElementFace LIGHT_FACE_1 = face(KEY_ZERO, uvs(0.0, 0.0, 1.0, 1.0))
	static ModelElementFace LIGHT_FACE_2 = face(KEY_ZERO, uvs(1.0,1.0,15.0,15.0))
	static ModelElementFace LIGHT_FACE_3 = face(KEY_ZERO, uvs(1.0,2.0,15.0,0.0))
	static Map<Direction, ModelElementFace> LIGHT_BASE_ELEMENT = Map.of(
		Direction.DOWN, LIGHT_FACE_1,
		Direction.UP, LIGHT_FACE_2,
		Direction.NORTH, LIGHT_FACE_3,
		Direction.SOUTH, LIGHT_FACE_3,
		Direction.WEST, LIGHT_FACE_3,
		Direction.EAST, LIGHT_FACE_3,
	)
	static ModelElementTexture EMPTY_TEXTURE_MODEL = new ModelElementTexture(null, 0)
	static List<ModelElement> CUBE_CTMH_BASE = List.of(
		new ModelElement(new Vector3f(0, 0, 0), new Vector3f(16, 16, 16), Map.of(
			Direction.DOWN, new ModelElementFace(Direction.DOWN, -1, TextureKey.DOWN.toString(), EMPTY_TEXTURE_MODEL),
			Direction.UP, new ModelElementFace(Direction.UP, -1, TextureKey.UP.toString(), EMPTY_TEXTURE_MODEL),
			Direction.NORTH, new ModelElementFace(Direction.NORTH, 0, TextureKey.NORTH.toString(), EMPTY_TEXTURE_MODEL),
			Direction.SOUTH, new ModelElementFace(Direction.SOUTH, 0, TextureKey.SOUTH.toString(), EMPTY_TEXTURE_MODEL),
			Direction.WEST, new ModelElementFace(Direction.WEST, 0, TextureKey.WEST.toString(), EMPTY_TEXTURE_MODEL),
			Direction.EAST, new ModelElementFace(Direction.EAST, 0, TextureKey.EAST.toString(), EMPTY_TEXTURE_MODEL),
		))
	)
	static JsonModel.DisplayMap RESIN_BASIN_DISPLAY = new JsonModel.DisplayMap()
		.put(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, transformation(50, 45, 0, 0, 1.7, 1.2, 0.325, 0.325, 0.325))
		.put(ModelTransformationMode.THIRD_PERSON_LEFT_HAND, transformation(50, -16, 0, 0, 1.7, 1.2, 0.325, 0.325, 0.325))
		.put(ModelTransformationMode.FIRST_PERSON_RIGHT_HAND, transformation(0, -225, 0, 0, 1.25, 0, 0.4, 0.4, 0.4))
		.put(ModelTransformationMode.FIRST_PERSON_LEFT_HAND, transformation(0, 135, 0, 0, 1.25, 0, 0.4, 0.4, 0.4))
		.put(ModelTransformationMode.GUI, transformation(30, 225, 0, 0, 0, 0, 0.625, 0.625, 0.625))
		.put(ModelTransformationMode.GROUND, transformation(0, 0, 0, 0, 3, 0, 0.25, 0.25, 0.25))
		.put(ModelTransformationMode.FIXED, transformation(0, -90, 0, 0, 0, 0, 0.5, 0.5, 0.5))
		.put(ModelTransformationMode.HEAD, transformation(0, 0, 0, 0, 11.75, 0, 1, 1, 1))
	static ModelElementFace RESIN_BASIN_FACE_1 = face(KEY_ONE, uvs(0, 8, 16, 16))
	static ModelElementFace RESIN_BASIN_FACE_2 = face(KEY_TWO, uvs(0, 0, 14, 1))
	static ModelElementFace RESIN_BASIN_FACE_3 = face(KEY_THREE, uvs(0, 0, 1, 6))
	static ModelElementFace RESIN_BASIN_FACE_4 = face(KEY_THREE, uvs(1, 9, 15, 15))
	static ModelElementFace RESIN_BASIN_FACE_5 = face(KEY_THREE, uvs(0, 0, 14, 6))
	static ModelElementFace RESIN_BASIN_FACE_6 = face(KEY_THREE, uvs(0, 0, 1, 14))
	static ModelElementFace RESIN_BASIN_FACE_7 = face(KEY_THREE, uvs(0, 0, 14, 1))
	static ModelElementFace RESIN_BASIN_FACE_8 = face(KEY_THREE, uvs(0, 0, 1, 3))
	static ModelElementFace RESIN_BASIN_FACE_9 = face(KEY_THREE, uvs(0, 0, 10, 3))
	static ModelElementFace RESIN_BASIN_FACE_10 = face(KEY_THREE, uvs(0, 0, 1, 10))
	static ModelElementFace RESIN_BASIN_FACE_11 = face(KEY_THREE, uvs(0, 0, 3, 1))
	static ModelElementFace RESIN_BASIN_FACE_12 = face(KEY_THREE, uvs(0, 0, 10, 1))
	static ModelElementFace RESIN_BASIN_FACE_13 = face(KEY_THREE, uvs(0, 0, 3, 10))
	static ModelElementFace RESIN_BASIN_FACE_14 = face(KEY_TWO, uvs(0, 0, 1, 4))
	static List<ModelElement> RESIN_BASIN_BASE_ELEMENT = List.of(
		new ModelElement(new Vector3f(0, 0, 0), new Vector3f(16, 8, 16), Map.of(
			Direction.NORTH, RESIN_BASIN_FACE_1, Direction.EAST, RESIN_BASIN_FACE_1,
			Direction.SOUTH, RESIN_BASIN_FACE_1, Direction.WEST, RESIN_BASIN_FACE_1,
			Direction.UP, face(KEY_ZERO, uvs(0, 0, 16, 16)), Direction.DOWN, face(KEY_TWO, uvs(0, 0, 16, 16)),
		)),
		new ModelElement(
			new Vector3f(1, 2, 1), new Vector3f(15, 3, 15),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_2, Direction.EAST, RESIN_BASIN_FACE_2,
				Direction.SOUTH, RESIN_BASIN_FACE_2, Direction.WEST, RESIN_BASIN_FACE_2,
				Direction.UP, face(KEY_TWO, uvs(1, 1, 15, 15)), Direction.DOWN, face(KEY_TWO, uvs(0, 0, 14, 14)),
			),
			new ModelRotation(new Vector3f(9, 10, 9), Direction.Axis.Y, 0, false), true, 0
		),
		new ModelElement(
			new Vector3f(2, 2, 1), new Vector3f(3, 8, 15),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_3, Direction.EAST, RESIN_BASIN_FACE_4, Direction.SOUTH, RESIN_BASIN_FACE_3,
				Direction.WEST, RESIN_BASIN_FACE_5, Direction.UP, RESIN_BASIN_FACE_6, Direction.DOWN, RESIN_BASIN_FACE_6
			),
			new ModelRotation(new Vector3f(2.5, 5, 8), Direction.Axis.Z, 22.5, false), true, 0
		),
		new ModelElement(
			new Vector3f(13, 2, 1), new Vector3f(14, 8, 15),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_3, Direction.EAST, RESIN_BASIN_FACE_4, Direction.SOUTH, RESIN_BASIN_FACE_3,
				Direction.WEST, RESIN_BASIN_FACE_4, Direction.UP, RESIN_BASIN_FACE_6, Direction.DOWN, RESIN_BASIN_FACE_6,
			),
			new ModelRotation(new Vector3f(13.5, 5, 8), Direction.Axis.Z, -22.5, false), true, 0
		),
		new ModelElement(
			new Vector3f(1, 2, 2), new Vector3f(15, 8, 3),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_5, Direction.EAST, RESIN_BASIN_FACE_3, Direction.SOUTH, RESIN_BASIN_FACE_4,
				Direction.WEST, RESIN_BASIN_FACE_3, Direction.UP, RESIN_BASIN_FACE_7, Direction.DOWN, RESIN_BASIN_FACE_7,
			),
			new ModelRotation(new Vector3f(8, 5, 2.5), Direction.Axis.X, -22.5, false), true, 0
		),
		new ModelElement(
			new Vector3f(1, 2, 13), new Vector3f(15, 8, 14),
			Map.of(
				Direction.NORTH, face(KEY_THREE, uvs(1, 8, 15, 14)), Direction.EAST, RESIN_BASIN_FACE_3,
				Direction.SOUTH, face(KEY_THREE, uvs(1, 9, 15, 15)), Direction.WEST, RESIN_BASIN_FACE_3,
				Direction.UP, RESIN_BASIN_FACE_7, Direction.DOWN, RESIN_BASIN_FACE_7,
			),
			new ModelRotation(new Vector3f(8, 5, 13.5), Direction.Axis.X, 22.5, false), true, 0
		),
		new ModelElement(
			new Vector3f(5.5, 11, 9), new Vector3f(6.5, 14, 19),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_8, Direction.EAST, RESIN_BASIN_FACE_9, Direction.SOUTH, RESIN_BASIN_FACE_8,
				Direction.WEST, RESIN_BASIN_FACE_9, Direction.UP, RESIN_BASIN_FACE_10, Direction.DOWN, RESIN_BASIN_FACE_10,
			),
			new ModelRotation(new Vector3f(8, 12.25, 14), Direction.Axis.X, -22.5, false), true, 0
		),
		new ModelElement(
			new Vector3f(9.5, 11, 9), new Vector3f(10.5, 14, 19),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_8, Direction.EAST, RESIN_BASIN_FACE_9, Direction.SOUTH, RESIN_BASIN_FACE_8,
				Direction.WEST, RESIN_BASIN_FACE_9, Direction.UP, RESIN_BASIN_FACE_10, Direction.DOWN, RESIN_BASIN_FACE_10,
			),
			new ModelRotation(new Vector3f(8, 12.25, 14), Direction.Axis.X, -22.5, false), true, 0
		),
		new ModelElement(
			new Vector3f(6.5, 11, 9), new Vector3f(9.5, 12, 19),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_11, Direction.EAST, RESIN_BASIN_FACE_12, Direction.SOUTH, RESIN_BASIN_FACE_11,
				Direction.WEST, RESIN_BASIN_FACE_12, Direction.UP, RESIN_BASIN_FACE_13, Direction.DOWN, RESIN_BASIN_FACE_13,
			),
			new ModelRotation(new Vector3f(8, 12.25, 14), Direction.Axis.X, -22.5, false), true, 0
		),
		new ModelElement(
			new Vector3f(6, 11, 15.99), new Vector3f(10, 15, 16.99),
			Map.of(
				Direction.NORTH, face(KEY_TWO, uvs(6, 6, 10, 10)), Direction.EAST, RESIN_BASIN_FACE_14,
				Direction.SOUTH, face(KEY_TWO, uvs(0, 0, 4, 4)), Direction.WEST, RESIN_BASIN_FACE_14,
				Direction.UP, face(KEY_TWO, uvs(6, 6, 10, 7)), Direction.DOWN, face(KEY_TWO, uvs(0, 0, 4, 1)),
			),
			new ModelRotation(new Vector3f(14, 9, 24), Direction.Axis.Y, 0, false), true, 0
		),
	)
	static JsonModel.DisplayMap FISHING_STATION_DISPLAY = new JsonModel.DisplayMap().put(
		ModelTransformationMode.GUI, transformation(30, 225, 0, -1, 0, 0, 0.5, 0.5, 0.5)
	)
	static ModelRotation FISHING_STATION_ROTATION_1 = new ModelRotation(new Vector3f(1, 0, 4), Direction.Axis.X, 22.5, false)
	static Map<Direction, ModelElementFace> FISHING_STATION_FACE_MAP_1 = Map.of(
		Direction.NORTH, face(KEY_ZERO, uvs(0, 0, 1, 1), 180),
		Direction.EAST, face(KEY_ZERO, uvs(0, 0, 1, 14), 90),
		Direction.SOUTH, face(KEY_ZERO, uvs(0, 0, 1, 1)),
		Direction.WEST, face(KEY_ZERO, uvs(0, 0, 1, 14), 270),
		Direction.UP, face(KEY_ZERO, uvs(0, 0, 1, 14)),
		Direction.DOWN, face(KEY_ZERO, uvs(0, 0, 1, 14), 180),
	)
	static ModelRotation FISHING_STATION_ROTATION_2 = new ModelRotation(new Vector3f(1, 0, 4), Direction.Axis.X, -22.5, false)
	static ModelElementFace MISSING_FACE = face(KEY_MISSING, uvs(0, 0, 0, 9))
	static Map<Direction, ModelElementFace> FISHING_STATION_FACE_MAP_2 = Map.of(
		Direction.NORTH, MISSING_FACE, Direction.EAST, face(KEY_ONE, uvs(7, 0, 16, 9)),
		Direction.SOUTH, MISSING_FACE, Direction.WEST, face(KEY_ONE, uvs(7, 0, 16, 9), 90),
		Direction.UP, MISSING_FACE, Direction.DOWN, MISSING_FACE,
	)
	static List<ModelElement> FISHING_STATION_ELEMENTS = List.of(
		new ModelElement(new Vector3f(0, 0, 0), new Vector3f(16, 16, 16), Map.of(
			Direction.NORTH, face(TextureKey.NORTH, uvs(0, 0, 16, 16)),
			Direction.EAST, face(TextureKey.EAST, uvs(0, 0, 16, 16)),
			Direction.SOUTH, face(TextureKey.SOUTH, uvs(0, 0, 16, 16)),
			Direction.WEST, face(TextureKey.WEST, uvs(0, 0, 16, 16)),
			Direction.UP, face(TextureKey.UP, uvs(0, 0, 16, 16)),
			Direction.DOWN, face(TextureKey.DOWN, uvs(0, 0, 16, 16)),
		)),
		new ModelElement(
			new Vector3f(1, 0, -10), new Vector3f(2, 1, 4),
			FISHING_STATION_FACE_MAP_1, FISHING_STATION_ROTATION_1, true, 0
		),
		new ModelElement(
			new Vector3f(14, 0, -10), new Vector3f(15, 1, 4),
			FISHING_STATION_FACE_MAP_1, FISHING_STATION_ROTATION_1, true, 0
		),
		new ModelElement(
			new Vector3f(2, 0.5, -9.5), new Vector3f(14, 0.5, 4.5),
			Map.of(
				Direction.NORTH, face(KEY_ZERO, uvs(0, 0, 12, 0), 180),
				Direction.EAST, face(KEY_ZERO, uvs(0, 0, 0, 14), 90),
				Direction.SOUTH, face(KEY_ZERO, uvs(0, 0, 12, 0)),
				Direction.WEST, face(KEY_ZERO, uvs(0, 0, 0, 14), 270),
				Direction.UP, face(KEY_ZERO, uvs(4, 0, 16, 14)),
				Direction.DOWN, face(KEY_ZERO, uvs(4, 0, 16, 14), 180),
			),
			FISHING_STATION_ROTATION_1, true, 0
		),
		new ModelElement(
			new Vector3f(14.5, 1, -5), new Vector3f(14.5, 10, 4),
			FISHING_STATION_FACE_MAP_2, FISHING_STATION_ROTATION_2, true, 0
		),
		new ModelElement(
			new Vector3f(1.5, 1, -5), new Vector3f(1.5, 10, 4),
			FISHING_STATION_FACE_MAP_2, FISHING_STATION_ROTATION_2, true, 0
		),
	)
	static JsonModel.DisplayMap NANOSABER_DISPLAY = new JsonModel.DisplayMap()
		.put(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, transformation(0, 0, 0, 0, 3, 0, 1, 1, 1))
		.put(ModelTransformationMode.THIRD_PERSON_LEFT_HAND, transformation(0, 0, 0, 0, 3, 0, 1, 1, 1))
		.put(ModelTransformationMode.FIRST_PERSON_RIGHT_HAND, transformation(0, 0, -5, 0, 4, 2, 0.5, 0.5, 0.5))
		.put(ModelTransformationMode.FIRST_PERSON_LEFT_HAND, transformation(0, 0, -5, 0, 4, 2, 0.5, 0.5, 0.5))
		.put(ModelTransformationMode.GUI, transformation(90, 45, -90, -3.75, -3.75, 0, 0.65, 0.65, 0.65))
		.put(ModelTransformationMode.GROUND, transformation(45, 0, 0, 0, 3, -2, 0.5, 0.5, 0.5))
		.put(ModelTransformationMode.FIXED, transformation(0, 90, 0, 0, -4, 0, 0.5, 0.5, 0.5))
	static List<ModelElement> NANOSABER_ELEMENT = List.of(
		new ModelElement(new Vector3f(7, 0, 7), new Vector3f(9, 5, 9), Map.of(
			Direction.NORTH, face(TextureKey.TEXTURE, uvs(5, 4, 6, 6.5)),
			Direction.EAST, face(TextureKey.TEXTURE, uvs(4, 4, 5, 6.5)),
			Direction.SOUTH, face(TextureKey.TEXTURE, uvs(7, 4, 8, 6.5)),
			Direction.WEST, face(TextureKey.TEXTURE, uvs(6, 4, 7, 6.5)),
			Direction.UP, face(TextureKey.TEXTURE, uvs(6, 4, 5, 3)),
			Direction.DOWN, face(TextureKey.TEXTURE, uvs(7, 3, 6, 4)),
		)),
		new ModelElement(new Vector3f(5.5, 5, 5.5), new Vector3f(10.5, 6, 10.5), Map.of(
			Direction.NORTH, face(TextureKey.TEXTURE, uvs(6.5, 2.5, 9, 3)),
			Direction.EAST, face(TextureKey.TEXTURE, uvs(4, 2.5, 6.5, 3)),
			Direction.SOUTH, face(TextureKey.TEXTURE, uvs(11.5, 2.5, 14, 3)),
			Direction.WEST, face(TextureKey.TEXTURE, uvs(9, 2.5, 11.5, 3)),
			Direction.UP, face(TextureKey.TEXTURE, uvs(9, 2.5, 6.5, 0)),
			Direction.DOWN, face(TextureKey.TEXTURE, uvs(11.5, 0, 9, 2.5)),
		)),
		new ModelElement(new Vector3f(7.5, 6, 7), new Vector3f(8.5, 30, 9), Map.of(
			Direction.NORTH, face(TextureKey.TEXTURE, uvs(1, 1, 1.5, 13)),
			Direction.EAST, face(TextureKey.TEXTURE, uvs(0, 1, 1, 13)),
			Direction.SOUTH, face(TextureKey.TEXTURE, uvs(2.5, 1, 3, 13)),
			Direction.WEST, face(TextureKey.TEXTURE, uvs(1.5, 1, 2.5, 13)),
			Direction.UP, face(TextureKey.TEXTURE, uvs(1.5, 1, 1, 0)),
			Direction.DOWN, face(TextureKey.TEXTURE, uvs(2, 0, 1.5, 1)),
		)),
		new ModelElement(new Vector3f(7, 6, 7.5), new Vector3f(9, 9, 10.5), Map.of(
			Direction.NORTH, face(TextureKey.TEXTURE, uvs(1.5, 14.5, 2.5, 16)),
			Direction.EAST, face(TextureKey.TEXTURE, uvs(0, 14.5, 1.5, 16)),
			Direction.SOUTH, face(TextureKey.TEXTURE, uvs(4, 14.5, 5, 16)),
			Direction.WEST, face(TextureKey.TEXTURE, uvs(2.5, 14.5, 4, 16)),
			Direction.UP, face(TextureKey.TEXTURE, uvs(2.5, 14.5, 1.5, 13)),
			Direction.DOWN, face(TextureKey.TEXTURE, uvs(3.5, 13, 2.5, 14.5)),
		)),
		new ModelElement(new Vector3f(8, 6, 6), new Vector3f(8, 30, 7), Map.of(
			Direction.NORTH, face(TextureKey.TEXTURE, uvs(3.5, 0.5, 3.5, 12.5)),
			Direction.EAST, face(TextureKey.TEXTURE, uvs(3, 0.5, 3.5, 12.5)),
			Direction.SOUTH, face(TextureKey.TEXTURE, uvs(4, 0.5, 4, 12.5)),
			Direction.WEST, face(TextureKey.TEXTURE, uvs(3.5, 0.5, 4, 12.5)),
			Direction.UP, face(TextureKey.TEXTURE, uvs(3.5, 0.5, 3.5, 0)),
			Direction.DOWN, face(TextureKey.TEXTURE, uvs(3.5, 0, 3.5, 0.5)),
		)),
		new ModelElement(new Vector3f(8, 30, 7), new Vector3f(8, 32, 8), Map.of(
			Direction.NORTH, face(TextureKey.TEXTURE, uvs(4.5, 1.5, 4.5, 2.5)),
			Direction.EAST, face(TextureKey.TEXTURE, uvs(4, 1.5, 4.5, 2.5)),
			Direction.SOUTH, face(TextureKey.TEXTURE, uvs(5, 1.5, 5, 2.5)),
			Direction.WEST, face(TextureKey.TEXTURE, uvs(4.5, 1.5, 5, 2.5)),
			Direction.UP, face(TextureKey.TEXTURE, uvs(4.5, 1.5, 4.5, 1)),
			Direction.DOWN, face(TextureKey.TEXTURE, uvs(4.5, 1, 4.5, 1.5)),
		)),
		new ModelElement(new Vector3f(7.5, 30, 8), new Vector3f(8.5, 32, 9), Map.of(
			Direction.NORTH, face(TextureKey.TEXTURE, uvs(4.5, 0.5, 5, 1.5)),
			Direction.EAST, face(TextureKey.TEXTURE, uvs(4, 0.5, 4.5, 1.5)),
			Direction.SOUTH, face(TextureKey.TEXTURE, uvs(5.5, 0.5, 6, 1.5)),
			Direction.WEST, face(TextureKey.TEXTURE, uvs(5, 0.5, 5.5, 1.5)),
			Direction.UP, face(TextureKey.TEXTURE, uvs(5, 0.5, 4.5, 0)),
			Direction.DOWN, face(TextureKey.TEXTURE, uvs(5.5, 0, 5, 0.5)),
		)),
	)

	@FunctionalInterface
	interface Uploadable<T> {
		JsonModel apply(T);
		default Identifier upload(T target) {
			apply(target).upload()
		}
	}
	@FunctionalInterface
	interface Active {
		Pair<Identifier, Identifier> upload(JsonModel model);
	}
	static Active ACTIVE = (JsonModel model) -> Pair.of(model.suffix("_off").upload(), model.suffix("_on").upload())
	static Uploadable<Item> ENERGY_ITEM = (Item item) -> GENERATED.create(item).add(TextureMap.layer0(item), TextureKey.LAYER0)
	static Uploadable<Item> ENERGY_ITEM_ACTIVE = (Item item) -> ENERGY_ITEM.apply(item).suffix("_active")
	static Uploadable<Item> ENERGY_ITEM_HANDHELD = (Item item) -> ENERGY_ITEM.apply(item).add(HANDHELD)
	static Uploadable<Item> ENERGY_ITEM_HANDHELD_ACTIVE = (Item item) -> ENERGY_ITEM_ACTIVE.apply(item).add(HANDHELD)
	static def SOLAR_PANEL = wrapperBlock { block, id ->
		CUBE_BOTTOM_TOP.create(block).add(
			cubeBottomTop(
				id.withSuffixedPath("_top"),
				TexturePaths.generatorBottom,
				TexturePaths.solarPanelSide,
			),
			TextureKey.SIDE
		)
	}
	static def QUANTUM_SOLAR_PANEL = wrapperBlock { block, id ->
		CUBE_BOTTOM_TOP.create(block).add(
			cubeBottomTop(
				id.withSuffixedPath("_top"),
				TexturePaths.quantumBottom,
				TexturePaths.quantumSolarPanelSide,
			),
			TextureKey.SIDE
		)
	}
	static Function<Block, JsonModel> CUBE_BOTTOM_TOP_SIDE = { Block block ->
		CUBE_BOTTOM_TOP.create(block).add(TextureMap.sideTopBottom(block), TextureKey.TOP, TextureKey.SIDE)
	}
	static TriFunction<Block, Identifier, Identifier, JsonModel> CUBE_BOTTOM_TOP_BASE = { Block block, Identifier id, Identifier bottom ->
		CUBE_BOTTOM_TOP.create(block).add(
			cubeBottomTop(
				id.withSuffixedPath("_top"),
				bottom,
				id.withSuffixedPath("_side"),
			),
			TextureKey.TOP, TextureKey.SIDE
		)
	}
	static def GENERATOR_CUBE_BOTTOM_TOP = wrapperBlock { block, id -> CUBE_BOTTOM_TOP_BASE.apply(block, id, TexturePaths.generatorBottom) }
	static def CHARGE_O_MAT = wrapperBlock { block, id -> CUBE_BOTTOM_TOP_BASE.apply(block, id, TexturePaths.machineTier2Bottom) }
	static def CHUNK_LOADER = wrapperBlock { block, id -> CUBE_BOTTOM_TOP_BASE.apply(block, id, TexturePaths.machineTier3Bottom) }
	static def ENERGY = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(orientable(
			id.withSuffixedPath("_side"),
			id.withSuffixedPath("_front"),
			id.withSuffixedPath("_side"),
		))
	}
	static Uploadable ORIENTABLE_SIDE_FRONT_TOP_BOTTOM = { Block block ->
		ORIENTABLE.create(block).add(TextureMap.sideFrontTopBottom(block))
	}
	static def GAS_TURBINE = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				id.withSuffixedPath("_top"),
				TexturePaths.generatorBottom,
				TexturePaths.generatorSide,
				TexturePaths.generatorSide,
			),
			TextureKey.TOP, TextureKey.FRONT
		)
	}
	static def FUSION_CONTROL_COMPUTER = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				TexturePaths.machineTier3Top,
				id.withSuffixedPath("_bottom"),
				id.withSuffixedPath("_west"),
				id.withSuffixedPath("_east"),
				TexturePaths.machineTier3Back,
				id.withSuffixedPath("_front"),
			),
			TextureKey.FRONT
		)
	}
	static def SOLID_FUEL_GENERATOR = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				TexturePaths.generatorTop,
				TexturePaths.generatorBottom,
				TexturePaths.generatorSide,
				TexturePaths.generatorSide,
			),
			TextureKey.FRONT
		)
	}
	static def WIND_MILL = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(orientable(
			id.withSuffixedPath("_top"),
			id.withSuffixedPath("_bottom"),
			id.withSuffixedPath("_west"),
			id.withSuffixedPath("_east"),
			TexturePaths.generatorBottom,
			id.withSuffixedPath("_front"),
		))
	}
	static TriFunction<Block, Identifier, Identifier, JsonModel> STORAGE_UNIT = { Block block, Identifier id, Identifier bottom ->
		ORIENTABLE.create(block).add(orientable(
			id.withSuffixedPath("_top"),
			bottom,
			id.withSuffixedPath("_side"),
			id.withSuffixedPath("_front"),
		))
	}
	static def BASIC_STORAGE_UNIT = wrapperBlock { block, id -> STORAGE_UNIT.apply(block, id, TexturePaths.basicUnitBottom) }
	static def ADVANCED_STORAGE_UNIT = wrapperBlock { block, id -> STORAGE_UNIT.apply(block, id, TexturePaths.advancedUnitBottom) }
	static def INDUSTRIAL_STORAGE_UNIT = wrapperBlock { block, id -> STORAGE_UNIT.apply(block, id, TexturePaths.industrialUnitBottom) }
	static TriFunction<Block, Identifier, Identifier, JsonModel> ORIENTABLE_TOP_SIDE_BOTTOM = { Block block, Identifier id, Identifier bottom ->
		ORIENTABLE.create(block).add(orientable(
			id.withSuffixedPath("_top"),
			bottom,
			id.withSuffixedPath("_side"),
			id.withSuffixedPath("_side"),
		))
	}
	static def QUANTUM_STORAGE_UNIT = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(orientable(
			TexturePaths.quantumTop,
			TexturePaths.quantumBottom,
			id.withSuffixedPath("_side"),
			id.withSuffixedPath("_front"),
		))
	}
	static def BASIC_TANK_UNIT = wrapperBlock { block, id -> ORIENTABLE_TOP_SIDE_BOTTOM.apply(block, id, TexturePaths.basicUnitBottom) }
	static def ADVANCED_TANK_UNIT = wrapperBlock { block, id -> ORIENTABLE_TOP_SIDE_BOTTOM.apply(block, id, TexturePaths.advancedUnitBottom) }
	static def INDUSTRIAL_TANK_UNIT = wrapperBlock { block, id -> ORIENTABLE_TOP_SIDE_BOTTOM.apply(block, id, TexturePaths.industrialUnitBottom) }
	static def QUANTUM_TANK_UNIT = wrapperBlock { block, id -> ORIENTABLE_TOP_SIDE_BOTTOM.apply(block, id, TexturePaths.quantumBottom) }
	static def LAUNCHPAD = wrapperBlock { block, id -> ORIENTABLE_TOP_SIDE_BOTTOM.apply(block, id, TexturePaths.machineTier2Bottom) }
	static def ELEVATOR = wrapperBlock { block, id -> ORIENTABLE_TOP_SIDE_BOTTOM.apply(block, id, id.withSuffixedPath("_top")) }
	static TriFunction<Block, Identifier, Identifier, JsonModel> MACHINE_BASE = { Block block, Identifier id, Identifier base ->
		ORIENTABLE.create(block).add(
			orientable(
				base.withSuffixedPath("machine_top"),
				id.withSuffixedPath("_front"),
				base.withSuffixedPath("machine_side"),
			),
			TextureKey.FRONT
		)
	}
	static def BASIC_MACHINE = wrapperBlock { block, id -> MACHINE_BASE.apply(block, id, TexturePaths.machineTier0Dir) }
	static def ADVANCED_MACHINE = wrapperBlock { block, id -> MACHINE_BASE.apply(block, id, TexturePaths.machineTier1Dir) }
	static def COMPUTER_CUBE = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(orientable(
			TexturePaths.machineTier2Top,
			id.withSuffixedPath("_front"),
			id.withSuffixedPath("_side"),
		))
	}
	static def PLAYER_DETECTOR_OTHERS = wrapperBlock { block, id -> CUBE_ALL.apply(block).suffix("_others") }
	static def PLAYER_DETECTOR_YOU = wrapperBlock { block, id -> CUBE_ALL.apply(block).suffix("_you") }
	static def AUTO_CRAFTING_TABLE = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(orientable(
			id.withSuffixedPath("_top"),
			TexturePaths.machineTier1Bottom,
			TexturePaths.machineTier1Side,
			id.withSuffixedPath("_front"),
		))
	}
	static def GRINDER = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				id.withSuffixedPath("_top"),
				id.withSuffixedPath("_front"),
				TexturePaths.machineTier1Side,
			),
			TextureKey.TOP, TextureKey.FRONT
		)
	}
	static TriFunction<Block, Identifier, Identifier, JsonModel> ACTIVE_FRONT_MACHINE_BASE = { Block block, Identifier id, Identifier base ->
		ORIENTABLE.create(block).add(
			orientable(
				base.withSuffixedPath("machine_top"),
				base.withSuffixedPath("machine_bottom"),
				base.withSuffixedPath("machine_west"),
				base.withSuffixedPath("machine_east"),
				base.withSuffixedPath("machine_back"),
				id.withSuffixedPath("_front"),
			),
			TextureKey.FRONT
		)
	}
	static def ACTIVE_FRONT_MACHINE = wrapperBlock { block, id -> ACTIVE_FRONT_MACHINE_BASE.apply(block, id, TexturePaths.machineTier2Dir) }
	static def ACTIVE_SIDE_MACHINE = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				TexturePaths.machineTier2Top,
				TexturePaths.machineTier2Bottom,
				id.withSuffixedPath("_west"),
				id.withSuffixedPath("_east"),
				TexturePaths.machineTier2Back,
				id.withSuffixedPath("_front"),
			),
			TextureKey.WEST, TextureKey.EAST, TextureKey.FRONT
		)
	}
	static def ACTIVE_TOP_FRONT_MACHINE = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				id.withSuffixedPath("_top"),
				TexturePaths.machineTier2Bottom,
				TexturePaths.machineTier2West,
				TexturePaths.machineTier2East,
				TexturePaths.machineTier2Back,
				id.withSuffixedPath("_front"),
			),
			TextureKey.UP, TextureKey.FRONT
		)
	}
	static def PUMP = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				id.withSuffixedPath("_top"),
				id.withSuffixedPath("_bottom"),
				id.withSuffixedPath("_side"),
				id.withSuffixedPath("_side"),
				id.withSuffixedPath("_front"),
				id.withSuffixedPath("_front"),
			),
			TextureKey.SOUTH, TextureKey.FRONT
		)
	}
	static def FLUID_REPLICATOR = wrapperBlock { block, id -> ACTIVE_FRONT_MACHINE_BASE.apply(block, id, TexturePaths.machineTier3Dir) }

	static BiFunction<Block, Identifier, JsonModel> LIGHT_BLOCK = (Block block, Identifier id) -> BLOCK.create(block).add(
		new TextureMap().put(TextureKey.PARTICLE, id).put(KEY_ZERO, id),
		TextureKey.PARTICLE, KEY_ZERO
	)
	static def ALARM_LIGHT = wrapperBlock { block, id ->
		LIGHT_BLOCK.apply(block, id).add(LIGHT_DISPLAY_1).add(List.of(
			new ModelElement(new Vector3f(3, 0, 3), new Vector3f(13, 3, 13), LIGHT_BASE_ELEMENT,)
		))
	}
	static def LAMP_LED_LIGHT = wrapperBlock { block, id ->
		LIGHT_BLOCK.apply(block, id).add(LIGHT_DISPLAY_1).add(List.of(
			new ModelElement(new Vector3f(2, 0, 2), new Vector3f(14, 1, 14), LIGHT_BASE_ELEMENT)
		))
	}
	static def LAMP_INCANDESCENT_LIGHT = wrapperBlock { block, id ->
		LIGHT_BLOCK.apply(block, id).add(LIGHT_DISPLAY_2).add(List.of(
			new ModelElement(
				new Vector3f(5, 0, 5), new Vector3f(11, 2, 11),
				Map.of(
					Direction.DOWN, LIGHT_FACE_1, Direction.UP, LIGHT_FACE_1, Direction.NORTH, LIGHT_FACE_1,
					Direction.SOUTH, LIGHT_FACE_1, Direction.WEST, LIGHT_FACE_1, Direction.EAST, LIGHT_FACE_1,
				)
			),
			new ModelElement(
				new Vector3f(4, 2, 4), new Vector3f(12, 10, 12),
				Map.of(
					Direction.DOWN, LIGHT_FACE_2, Direction.UP, LIGHT_FACE_2, Direction.NORTH, LIGHT_FACE_2,
					Direction.SOUTH, LIGHT_FACE_2, Direction.WEST, LIGHT_FACE_2, Direction.EAST, LIGHT_FACE_2,
				)
			),
		))
	}
	static def MACHINE_BLOCK = wrapperBlock { block, id ->
		CUBE_ALL.apply(block).add(CUBE_CTMH_BASE).add(
			new JsonModel.CtmMap().put(KEY_ZERO, id.withSuffixedPath("_ctmh"))
		)
	}
	static BiFunction<Block, Identifier, JsonModel> RESIN_BASIN_BASE = (Block block, Identifier id) -> new JsonModel().id(block).add(
		new TextureMap().put(TextureKey.PARTICLE, id.withSuffixedPath("_sap_flowing"))
			.put(KEY_ZERO, id.withSuffixedPath("_top"))
			.put(KEY_ONE, id.withSuffixedPath("_side"))
			.put(KEY_TWO, id.withSuffixedPath("_bottom"))
			.put(KEY_THREE, id.withSuffixedPath("_inner"))
	)
	static def RESIN_BASIN_EMPTY = wrapperBlock { block, id ->
		RESIN_BASIN_BASE.apply(block, id).add(RESIN_BASIN_BASE_ELEMENT).add(RESIN_BASIN_DISPLAY)
	}
	static def RESIN_BASIN_FLOWING = wrapperBlock { block, id ->
		List<ModelElement> elements = new ArrayList<>(RESIN_BASIN_BASE_ELEMENT)
		elements.add(new ModelElement(
			new Vector3f(6.5, 11, 9), new Vector3f(9.5, 12, 19),
			Map.of(
				Direction.NORTH, face(TextureKey.PARTICLE, uvs(0, 0, 3, 1)),
				Direction.EAST, face(TextureKey.PARTICLE, uvs(0, 0, 10, 1)),
				Direction.SOUTH, face(TextureKey.PARTICLE, uvs(0, 0, 3, 1)),
				Direction.WEST, face(TextureKey.PARTICLE, uvs(0, 0, 10, 1)),
				Direction.UP, face(TextureKey.PARTICLE, uvs(0, 6, 3, 16)),
				Direction.DOWN, face(TextureKey.PARTICLE, uvs(0, 0, 3, 10)),
			),
			new ModelRotation(new Vector3f(8, 11.25, 14), Direction.Axis.X, -22.5, false),
			true,
			0
		))
		elements.add(new ModelElement(
			new Vector3f(6.5, 3, 9.1), new Vector3f(9.5, 10, 9.6),
			Map.of(
				Direction.NORTH, face(TextureKey.PARTICLE, uvs(0, 0, 3, 7)),
				Direction.EAST, face(TextureKey.PARTICLE, uvs(0, 0, 0.5, 7)),
				Direction.SOUTH, face(TextureKey.PARTICLE, uvs(0, 0, 3, 7)),
				Direction.WEST, face(TextureKey.PARTICLE, uvs(0, 0, 0.5, 7)),
				Direction.UP, face(TextureKey.PARTICLE, uvs(0, 0, 3, 0.5)),
				Direction.DOWN, face(TextureKey.PARTICLE, uvs(0, 0, 3, 0.5)),
			),
			new ModelRotation(new Vector3f(13, 11, 17), Direction.Axis.X, 0, false),
			true,
			0
		))
		RESIN_BASIN_BASE.apply(block, id).add(elements).suffix("_flowing")
	}
	static def RESIN_BASIN_FULL = wrapperBlock { block, id ->
		JsonModel model = RESIN_BASIN_BASE.apply(block, id)
		model.textures = model.textures.copyAndAdd(KEY_FOUR, id.withSuffixedPath("_sap_still"))
		List<ModelElement> elements = new ArrayList<>(RESIN_BASIN_BASE_ELEMENT)
		elements.add(new ModelElement(
			new Vector3f(1, 1, 1), new Vector3f(15, 6, 15),
			Map.of(
				Direction.NORTH, face(KEY_FOUR, uvs(0, 0, 14, 5)),
				Direction.EAST, face(KEY_FOUR, uvs(0, 0, 14, 1)),
				Direction.SOUTH, face(KEY_FOUR, uvs(0, 0, 14, 1)),
				Direction.WEST, face(KEY_FOUR, uvs(0, 0, 14, 5)),
				Direction.UP, face(KEY_FOUR, uvs(1, 1, 15, 15)),
				Direction.DOWN, face(KEY_FOUR, uvs(0, 0, 14, 14)),
			),
			new ModelRotation(new Vector3f(9, 13, 9), Direction.Axis.Y, 0, false),
			true,
			0
		))
		model.add(elements).suffix("_full")
	}
	static def FISHING_STATION = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(FISHING_STATION_DISPLAY).add(FISHING_STATION_ELEMENTS).add(
			orientable(
				TexturePaths.machineTier2Top,
				TexturePaths.machineTier2Bottom,
				TexturePaths.machineTier2West,
				TexturePaths.machineTier2East,
				TexturePaths.machineTier2Back,
				id,
			).put(TextureKey.PARTICLE, TexturePaths.machineTier2Top)
			.put(KEY_ZERO, id.withSuffixedPath("_net"))
			.put(KEY_ONE, id.withSuffixedPath("_net_side"))
			.put(KEY_MISSING, Identifier.ofVanilla("missingno"))
		)
	}
	static def RUBBER_LOG_WITH_SAP = wrapperBlock { block, id ->
		ORIENTABLE.create(block).suffix("_with_sap").add(orientable(
			id.withSuffixedPath("_top"),
			id.withSuffixedPath("_sap"),
			id,
		))
	}
	static def FLUID = wrapperBlock { block, id ->
		new JsonModel().id(block).add(TextureMap.particle(id.withSuffixedPath("_still")))
	}
	static BiFunction<Block, Identifier, JsonModel> CABLE_CORE_BASE = { Block block, Identifier id ->
		new JsonModel().id(block).suffix("_core").add(texture(id))
	}
	static BiFunction<Block, Identifier, JsonModel> CABLE_SIDE_BASE = { Block block, Identifier id ->
		new JsonModel().id(block).suffix("_side").add(texture(id))
	}
	static def CABLE_CORE = wrapperBlock { block, id -> CABLE_CORE_BASE.apply(block, id).add(CABLE_CORE_ELEMENT) }
	static def CABLE_SIDE = wrapperBlock { block, id -> CABLE_SIDE_BASE.apply(block, id).add(CABLE_SIDE_ELEMENT) }
	static def CABLE_THICK_CORE = wrapperBlock { block, id -> CABLE_CORE_BASE.apply(block, id).add(CABLE_THICK_CORE_ELEMENT) }
	static def CABLE_THICK_SIDE = wrapperBlock { block, id -> CABLE_SIDE_BASE.apply(block, id).add(CABLE_THICK_SIDE_ELEMENT) }
	static def NANOSABER_BASE = wrapperItem { item, id ->
		new JsonModel().add(NANOSABER_DISPLAY).add(NANOSABER_ELEMENT).id(item).add(texture(id), TextureKey.TEXTURE, TextureKey.PARTICLE)
	}
	static Uploadable<Item> NANOSABER_OFF = { Item item -> NANOSABER_BASE.apply(item).suffix("_off") }
	static Uploadable<Item> NANOSABER_ON = { Item item -> NANOSABER_BASE.apply(item).suffix("_on") }
	static Uploadable<Item> NANOSABER_LOW = { Item item -> NANOSABER_BASE.apply(item).suffix("_low") }
	static def CELL_BASE = wrapperItem { item, id ->
		GENERATED.create(item).suffix("_base").add(TextureMap.layer0(id), TextureKey.LAYER0)
	}
	static def CELL_BACKGROUND = wrapperItem { item, id ->
		CELL_TEMPLATE.create(item).suffix("_background").add(texture(id.withSuffixedPath("_background")))
	}
	static def CELL_FLUID = wrapperItem { item, id ->
		CELL_TEMPLATE.create(item).suffix("_fluid").add(texture(id.withSuffixedPath("_background")))
	}
	static def CELL_GLASS = wrapperItem { item, id ->
		CELL_TEMPLATE.create(item).suffix("_glass").add(texture(id.withSuffixedPath("_glass")))
	}
	static Uploadable<Identifier> BUCKET_BASE = { Identifier id ->
		GENERATED.create(id).suffix("_base").add(TextureMap.layer0(id), TextureKey.LAYER0)
	}
	static Uploadable<Identifier> BUCKET_BACKGROUND = { Identifier id ->
		BUCKET_TEMPLATE.create(id).suffix("_background").add(texture(id.withSuffixedPath("_background")))
	}
	static Uploadable<Identifier> BUCKET_FLUID = { Identifier id ->
		BUCKET_TEMPLATE.create(id).suffix("_fluid").add(texture(id.withSuffixedPath("_background")))
	}

	static TextureMap orientable(Identifier top, Identifier front, Identifier side) {
		return new TextureMap()
			.put(TextureKey.TOP, top)
			.put(TextureKey.FRONT, front)
			.put(TextureKey.SIDE, side)
	}

	static TextureMap orientable(Identifier top, Identifier bottom, Identifier side, Identifier front) {
		return orientable(top, front, side).put(TextureKey.BOTTOM, bottom)
	}

	static TextureMap orientable(Identifier up, Identifier bottom, Identifier west, Identifier east, Identifier south, Identifier front) {
		return new TextureMap()
			.put(TextureKey.UP, up)
			.put(TextureKey.BOTTOM, bottom)
			.put(TextureKey.WEST, west)
			.put(TextureKey.EAST, east)
			.put(TextureKey.SOUTH, south)
			.put(TextureKey.FRONT, front)
	}

	static TextureMap cubeBottomTop(Identifier top, Identifier bottom, Identifier side) {
		return new TextureMap()
			.put(TextureKey.TOP, top)
			.put(TextureKey.BOTTOM, bottom)
			.put(TextureKey.SIDE, side)
	}

	static TextureMap texture(Identifier id) {
		return new TextureMap().put(TextureKey.TEXTURE, id).put(TextureKey.PARTICLE, id)
	}

	static float[] uvs(float x1, float y1, float x2, float y2) {
		return new float[]{ x1, y1, x2, y2 }
	}

	static ModelElementFace face(TextureKey texture, float[] uvs) {
		return new ModelElementFace(null, -1, texture.toString(), new ModelElementTexture(uvs, 0))
	}

	static ModelElementFace face(TextureKey texture, float[] uvs, int rotation) {
		return new ModelElementFace(null, -1, texture.toString(), new ModelElementTexture(uvs, rotation))
	}

	static Transformation transformation(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
		return new Transformation(new Vector3f(x1, y1, z1), new Vector3f(x2, y2, z2), new Vector3f(x3, y3, z3))
	}

	static Uploadable<Block> wrapperBlock(BiFunction<Block, Identifier, JsonModel> fun) {
		return (Block block) -> fun.apply(block, TextureMap.getId(block))
	}

	static Uploadable<Item> wrapperItem(BiFunction<Item, Identifier, JsonModel> fun) {
		return (Item item) -> fun.apply(item, TextureMap.getId(item))
	}
}
