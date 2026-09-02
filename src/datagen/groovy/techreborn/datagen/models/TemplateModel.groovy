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

import net.minecraft.client.data.models.model.ModelLocationUtils
import net.minecraft.client.resources.model.cuboid.CuboidFace
import net.minecraft.client.resources.model.cuboid.CuboidModelElement
import net.minecraft.client.resources.model.cuboid.CuboidRotation
import net.minecraft.client.resources.model.cuboid.ItemTransform
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.world.level.block.Block
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.resources.Identifier
import com.mojang.math.Quadrant
import net.minecraft.core.Direction
import org.apache.commons.lang3.function.TriFunction
import org.apache.commons.lang3.tuple.Pair
import org.joml.Vector3f
import techreborn.utils.DirectionUtils
import techreborn.utils.DirectionUtils.HorizontalPart

import java.util.function.BiFunction
import java.util.function.Function

class TemplateModel {
	static TextureSlot KEY_ZERO = TextureSlot.create("0")
	static TextureSlot KEY_ONE = TextureSlot.create("1")
	static TextureSlot KEY_TWO = TextureSlot.create("2")
	static TextureSlot KEY_THREE = TextureSlot.create("3")
	static TextureSlot KEY_FOUR = TextureSlot.create("4")
	static TextureSlot KEY_ALONE = TextureSlot.create("alone")
	static TextureSlot KEY_START = TextureSlot.create("start")
	static TextureSlot KEY_MIDDLE = TextureSlot.create("middle")
	static TextureSlot KEY_END = TextureSlot.create("end")
	static TextureSlot KEY_MISSING = TextureSlot.create("missing")
	static Identifier HANDHELD = Identifier.withDefaultNamespace("item/handheld")
	static JsonModel GENERATED = new JsonModel().add(Identifier.withDefaultNamespace("item/generated"))
	static JsonModel ORIENTABLE = new JsonModel().add(Identifier.withDefaultNamespace("block/orientable"))
	static JsonModel CUBE_BOTTOM_TOP = new JsonModel().add(Identifier.withDefaultNamespace("block/cube_bottom_top"))
	static JsonModel BLOCK = new JsonModel().add(Identifier.withDefaultNamespace("block/block"))
	static Uploadable CUBE_ALL = (Block block) -> new JsonModel()
		.add(Identifier.withDefaultNamespace("block/cube_all")).id(block).add(TextureMapping.cube(block), TextureSlot.ALL)
	static JsonModel.DisplayMap CELL_DISPLAY = new JsonModel.DisplayMap()
		.put(ItemDisplayContext.GROUND, transformation(0, 0, 0, 0, 2, 0, 0.5, 0.5, 0.5))
		.put(ItemDisplayContext.HEAD, transformation(0, 180, 0, 0, 13, 7, 1, 1, 1))
		.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, transformation(0, 0, 0, 0, 3, 1, 0.55, 0.55, 0.55))
		.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, transformation(0, -90, 25, 1.13, 3.2, 1.13, 0.68, 0.68, 0.68))
		.put(ItemDisplayContext.FIXED, transformation(0, 180, 0, 0, 0, 0, 1, 1, 1))
	static JsonModel CELL_TEMPLATE = new JsonModel().add(List.of(
		new CuboidModelElement(new Vector3f(7, 4, 7.5), new Vector3f(10, 12, 8.5), Map.of(
			Direction.NORTH, face(TextureSlot.TEXTURE, uvs(7, 4, 10, 12)),
			Direction.SOUTH, face(TextureSlot.TEXTURE, uvs(7, 4, 10, 12)),
		))
	))
	static JsonModel BUCKET_TEMPLATE = new JsonModel().add(List.of(
		new CuboidModelElement(new Vector3f(4, 11, 7.5), new Vector3f(12, 13, 8.5), Map.of(
			Direction.NORTH, face(TextureSlot.TEXTURE, uvs(4, 3, 12, 5)),
			Direction.SOUTH, face(TextureSlot.TEXTURE, uvs(4, 3, 12, 5)),
		)),
		new CuboidModelElement(new Vector3f(5, 10, 7.5), new Vector3f(11, 11, 8.5), Map.of(
			Direction.NORTH, face(TextureSlot.TEXTURE, uvs(5, 5, 11, 6)),
			Direction.SOUTH, face(TextureSlot.TEXTURE, uvs(5, 5, 11, 6)),
		)),
		new CuboidModelElement(new Vector3f(3, 11, 7.5), new Vector3f(4, 12, 8.5), Map.of(
			Direction.NORTH, face(TextureSlot.TEXTURE, uvs(12, 4, 13, 5)),
			Direction.SOUTH, face(TextureSlot.TEXTURE, uvs(12, 4, 13, 5)),
		)),
		new CuboidModelElement(new Vector3f(12, 11, 7.5), new Vector3f(13, 12, 8.5), Map.of(
			Direction.NORTH, face(TextureSlot.TEXTURE, uvs(3, 4, 4, 5)),
			Direction.SOUTH, face(TextureSlot.TEXTURE, uvs(3, 4, 4, 5)),
		)),
	))
	static CuboidFace CABLE_FACE_1 = face(TextureSlot.TEXTURE, uvs(1, 1, 5, 5))
	static CuboidFace CABLE_FACE_2 = face(TextureSlot.TEXTURE, uvs(0, 7, 6, 11))
	static CuboidFace CABLE_FACE_3 = face(TextureSlot.TEXTURE, uvs(0, 7, 6, 11), Quadrant.R90)
	static CuboidFace CABLE_FACE_4 = face(TextureSlot.TEXTURE, uvs(0, 0, 6, 6))
	static CuboidFace CABLE_FACE_5 = face(TextureSlot.TEXTURE, uvs(0, 6, 5, 12))
	static CuboidFace CABLE_FACE_6 = face(TextureSlot.TEXTURE, uvs(0, 6, 5, 12), Quadrant.R90)
	static List<CuboidModelElement> CABLE_CORE_ELEMENT = List.of(
		new CuboidModelElement(new Vector3f(6, 6, 6), new Vector3f(10, 10, 10), Map.of(
			Direction.NORTH, CABLE_FACE_1, Direction.EAST, CABLE_FACE_1, Direction.SOUTH, CABLE_FACE_1,
			Direction.WEST, CABLE_FACE_1, Direction.UP, CABLE_FACE_1, Direction.DOWN, CABLE_FACE_1
		))
	)
	static List<CuboidModelElement> CABLE_SIDE_ELEMENT = List.of(
		new CuboidModelElement(new Vector3f(6, 6, 0), new Vector3f(10, 10, 6), Map.of(
			Direction.NORTH, CABLE_FACE_1, Direction.EAST, CABLE_FACE_2,
			Direction.SOUTH, new CuboidFace(
				Direction.SOUTH, -1, TextureSlot.TEXTURE.toString(),
				uvs(1, 1, 5, 5), Quadrant.R0
			),
			Direction.WEST, CABLE_FACE_2, Direction.UP, CABLE_FACE_3, Direction.DOWN, CABLE_FACE_3
		))
	)
	static List<CuboidModelElement> CABLE_THICK_CORE_ELEMENT = List.of(
		new CuboidModelElement(new Vector3f(5, 5, 5), new Vector3f(11, 11, 11), Map.of(
			Direction.NORTH, CABLE_FACE_4, Direction.EAST, CABLE_FACE_4, Direction.SOUTH, CABLE_FACE_4,
			Direction.WEST, CABLE_FACE_4, Direction.UP, CABLE_FACE_4, Direction.DOWN, CABLE_FACE_4
		))
	)
	static List<CuboidModelElement> CABLE_THICK_SIDE_ELEMENT = List.of(
		new CuboidModelElement(new Vector3f(5, 5, 0), new Vector3f(11, 11, 5), Map.of(
			Direction.NORTH, CABLE_FACE_4, Direction.EAST, CABLE_FACE_5,
			Direction.SOUTH, new CuboidFace(
				Direction.SOUTH, -1, TextureSlot.TEXTURE.toString(),
				uvs(0, 0, 6, 6), Quadrant.R0
			),
			Direction.WEST, CABLE_FACE_5, Direction.UP, CABLE_FACE_6, Direction.DOWN, CABLE_FACE_6
		))
	)
	static JsonModel.DisplayMap LIGHT_DISPLAY_BASE = new JsonModel.DisplayMap()
		.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, transformation(75, 45, 0, 0, 2.5, 2, 0.375, 0.375, 0.375))
		.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, transformation(0, 225, 0, 0, 4.2, 0, 0.40, 0.40, 0.40))
	static JsonModel.DisplayMap LIGHT_DISPLAY_1 = LIGHT_DISPLAY_BASE.create()
		.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, transformation(0, 35, 0 , 0, 5.5, 0, 0.60, 0.60, 0.60))
	static JsonModel.DisplayMap LIGHT_DISPLAY_2 = LIGHT_DISPLAY_BASE.create()
		.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, transformation(0, 35, 0 , 0, 4.3, 0, 0.60, 0.60, 0.60))
	static CuboidFace LIGHT_FACE_1 = face(KEY_ZERO, uvs(0.0, 0.0, 1.0, 1.0))
	static CuboidFace LIGHT_FACE_2 = face(KEY_ZERO, uvs(1.0,1.0,15.0,15.0))
	static CuboidFace LIGHT_FACE_3 = face(KEY_ZERO, uvs(1.0,2.0,15.0,0.0))
	static Map<Direction, CuboidFace> LIGHT_BASE_ELEMENT = Map.of(
		Direction.DOWN, LIGHT_FACE_1,
		Direction.UP, LIGHT_FACE_2,
		Direction.NORTH, LIGHT_FACE_3,
		Direction.SOUTH, LIGHT_FACE_3,
		Direction.WEST, LIGHT_FACE_3,
		Direction.EAST, LIGHT_FACE_3,
	)
	static JsonModel.DisplayMap RESIN_BASIN_DISPLAY = new JsonModel.DisplayMap()
		.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, transformation(50, 45, 0, 0, 1.7, 1.2, 0.325, 0.325, 0.325))
		.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, transformation(50, -16, 0, 0, 1.7, 1.2, 0.325, 0.325, 0.325))
		.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, transformation(0, -225, 0, 0, 1.25, 0, 0.4, 0.4, 0.4))
		.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, transformation(0, 135, 0, 0, 1.25, 0, 0.4, 0.4, 0.4))
		.put(ItemDisplayContext.GUI, transformation(30, 225, 0, 0, 0, 0, 0.625, 0.625, 0.625))
		.put(ItemDisplayContext.GROUND, transformation(0, 0, 0, 0, 3, 0, 0.25, 0.25, 0.25))
		.put(ItemDisplayContext.FIXED, transformation(0, -90, 0, 0, 0, 0, 0.5, 0.5, 0.5))
		.put(ItemDisplayContext.HEAD, transformation(0, 0, 0, 0, 11.75, 0, 1, 1, 1))
	static CuboidFace RESIN_BASIN_FACE_1 = face(KEY_ONE, uvs(0, 8, 16, 16))
	static CuboidFace RESIN_BASIN_FACE_2 = face(KEY_TWO, uvs(0, 0, 14, 1))
	static CuboidFace RESIN_BASIN_FACE_3 = face(KEY_THREE, uvs(0, 0, 1, 6))
	static CuboidFace RESIN_BASIN_FACE_4 = face(KEY_THREE, uvs(1, 9, 15, 15))
	static CuboidFace RESIN_BASIN_FACE_5 = face(KEY_THREE, uvs(0, 0, 14, 6))
	static CuboidFace RESIN_BASIN_FACE_6 = face(KEY_THREE, uvs(0, 0, 1, 14))
	static CuboidFace RESIN_BASIN_FACE_7 = face(KEY_THREE, uvs(0, 0, 14, 1))
	static CuboidFace RESIN_BASIN_FACE_8 = face(KEY_THREE, uvs(0, 0, 1, 3))
	static CuboidFace RESIN_BASIN_FACE_9 = face(KEY_THREE, uvs(0, 0, 10, 3))
	static CuboidFace RESIN_BASIN_FACE_10 = face(KEY_THREE, uvs(0, 0, 1, 10))
	static CuboidFace RESIN_BASIN_FACE_11 = face(KEY_THREE, uvs(0, 0, 3, 1))
	static CuboidFace RESIN_BASIN_FACE_12 = face(KEY_THREE, uvs(0, 0, 10, 1))
	static CuboidFace RESIN_BASIN_FACE_13 = face(KEY_THREE, uvs(0, 0, 3, 10))
	static CuboidFace RESIN_BASIN_FACE_14 = face(KEY_TWO, uvs(0, 0, 1, 4))
	static List<CuboidModelElement> RESIN_BASIN_BASE_ELEMENT = List.of(
		new CuboidModelElement(new Vector3f(0, 0, 0), new Vector3f(16, 8, 16), Map.of(
			Direction.NORTH, RESIN_BASIN_FACE_1, Direction.EAST, RESIN_BASIN_FACE_1,
			Direction.SOUTH, RESIN_BASIN_FACE_1, Direction.WEST, RESIN_BASIN_FACE_1,
			Direction.UP, face(KEY_ZERO, uvs(0, 0, 16, 16)), Direction.DOWN, face(KEY_TWO, uvs(0, 0, 16, 16)),
		)),
		new CuboidModelElement(
			new Vector3f(1, 2, 1), new Vector3f(15, 3, 15),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_2, Direction.EAST, RESIN_BASIN_FACE_2,
				Direction.SOUTH, RESIN_BASIN_FACE_2, Direction.WEST, RESIN_BASIN_FACE_2,
				Direction.UP, face(KEY_TWO, uvs(1, 1, 15, 15)), Direction.DOWN, face(KEY_TWO, uvs(0, 0, 14, 14)),
			),
			new CuboidRotation(new Vector3f(9, 10, 9), new CuboidRotation.SingleAxisRotation(Direction.Axis.Y, 0), false), null, 0
		),
		new CuboidModelElement(
			new Vector3f(2, 2, 1), new Vector3f(3, 8, 15),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_3, Direction.EAST, RESIN_BASIN_FACE_4, Direction.SOUTH, RESIN_BASIN_FACE_3,
				Direction.WEST, RESIN_BASIN_FACE_5, Direction.UP, RESIN_BASIN_FACE_6, Direction.DOWN, RESIN_BASIN_FACE_6
			),
			new CuboidRotation(new Vector3f(2.5, 5, 8), new CuboidRotation.SingleAxisRotation(Direction.Axis.Z, 22.5), false), null, 0
		),
		new CuboidModelElement(
			new Vector3f(13, 2, 1), new Vector3f(14, 8, 15),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_3, Direction.EAST, RESIN_BASIN_FACE_4, Direction.SOUTH, RESIN_BASIN_FACE_3,
				Direction.WEST, RESIN_BASIN_FACE_4, Direction.UP, RESIN_BASIN_FACE_6, Direction.DOWN, RESIN_BASIN_FACE_6,
			),
			new CuboidRotation(new Vector3f(13.5, 5, 8), new CuboidRotation.SingleAxisRotation(Direction.Axis.Z, -22.5), false), null, 0
		),
		new CuboidModelElement(
			new Vector3f(1, 2, 2), new Vector3f(15, 8, 3),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_5, Direction.EAST, RESIN_BASIN_FACE_3, Direction.SOUTH, RESIN_BASIN_FACE_4,
				Direction.WEST, RESIN_BASIN_FACE_3, Direction.UP, RESIN_BASIN_FACE_7, Direction.DOWN, RESIN_BASIN_FACE_7,
			),
			new CuboidRotation(new Vector3f(8, 5, 2.5), new CuboidRotation.SingleAxisRotation(Direction.Axis.X, -22.5), false), null, 0
		),
		new CuboidModelElement(
			new Vector3f(1, 2, 13), new Vector3f(15, 8, 14),
			Map.of(
				Direction.NORTH, face(KEY_THREE, uvs(1, 8, 15, 14)), Direction.EAST, RESIN_BASIN_FACE_3,
				Direction.SOUTH, face(KEY_THREE, uvs(1, 9, 15, 15)), Direction.WEST, RESIN_BASIN_FACE_3,
				Direction.UP, RESIN_BASIN_FACE_7, Direction.DOWN, RESIN_BASIN_FACE_7,
			),
			new CuboidRotation(new Vector3f(8, 5, 13.5), new CuboidRotation.SingleAxisRotation(Direction.Axis.X, 22.5), false), null, 0
		),
		new CuboidModelElement(
			new Vector3f(5.5, 11, 9), new Vector3f(6.5, 14, 19),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_8, Direction.EAST, RESIN_BASIN_FACE_9, Direction.SOUTH, RESIN_BASIN_FACE_8,
				Direction.WEST, RESIN_BASIN_FACE_9, Direction.UP, RESIN_BASIN_FACE_10, Direction.DOWN, RESIN_BASIN_FACE_10,
			),
			new CuboidRotation(new Vector3f(8, 12.25, 14), new CuboidRotation.SingleAxisRotation(Direction.Axis.X, -22.5), false), null, 0
		),
		new CuboidModelElement(
			new Vector3f(9.5, 11, 9), new Vector3f(10.5, 14, 19),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_8, Direction.EAST, RESIN_BASIN_FACE_9, Direction.SOUTH, RESIN_BASIN_FACE_8,
				Direction.WEST, RESIN_BASIN_FACE_9, Direction.UP, RESIN_BASIN_FACE_10, Direction.DOWN, RESIN_BASIN_FACE_10,
			),
			new CuboidRotation(new Vector3f(8, 12.25, 14), new CuboidRotation.SingleAxisRotation(Direction.Axis.X, -22.5), false), null, 0
		),
		new CuboidModelElement(
			new Vector3f(6.5, 11, 9), new Vector3f(9.5, 12, 19),
			Map.of(
				Direction.NORTH, RESIN_BASIN_FACE_11, Direction.EAST, RESIN_BASIN_FACE_12, Direction.SOUTH, RESIN_BASIN_FACE_11,
				Direction.WEST, RESIN_BASIN_FACE_12, Direction.UP, RESIN_BASIN_FACE_13, Direction.DOWN, RESIN_BASIN_FACE_13,
			),
			new CuboidRotation(new Vector3f(8, 12.25, 14), new CuboidRotation.SingleAxisRotation(Direction.Axis.X, -22.5), false), null, 0
		),
		new CuboidModelElement(
			new Vector3f(6, 11, 15.99), new Vector3f(10, 15, 16.99),
			Map.of(
				Direction.NORTH, face(KEY_TWO, uvs(6, 6, 10, 10)), Direction.EAST, RESIN_BASIN_FACE_14,
				Direction.SOUTH, face(KEY_TWO, uvs(0, 0, 4, 4)), Direction.WEST, RESIN_BASIN_FACE_14,
				Direction.UP, face(KEY_TWO, uvs(6, 6, 10, 7)), Direction.DOWN, face(KEY_TWO, uvs(0, 0, 4, 1)),
			),
			new CuboidRotation(new Vector3f(14, 9, 24), new CuboidRotation.SingleAxisRotation(Direction.Axis.Y, 0), false), null, 0
		),
	)
	static JsonModel.DisplayMap FISHING_STATION_DISPLAY = new JsonModel.DisplayMap().put(
		ItemDisplayContext.GUI, transformation(30, 225, 0, -1, 0, 0, 0.5, 0.5, 0.5)
	)
	static CuboidRotation FISHING_STATION_ROTATION_1 = new CuboidRotation(new Vector3f(1, 0, 4), new CuboidRotation.SingleAxisRotation(Direction.Axis.X, 22.5), false)
	static Map<Direction, CuboidFace> FISHING_STATION_FACE_MAP_1 = Map.of(
		Direction.NORTH, face(KEY_ZERO, uvs(0, 0, 1, 1), Quadrant.R180),
		Direction.EAST, face(KEY_ZERO, uvs(0, 0, 1, 14), Quadrant.R90),
		Direction.SOUTH, face(KEY_ZERO, uvs(0, 0, 1, 1)),
		Direction.WEST, face(KEY_ZERO, uvs(0, 0, 1, 14), Quadrant.R270),
		Direction.UP, face(KEY_ZERO, uvs(0, 0, 1, 14)),
		Direction.DOWN, face(KEY_ZERO, uvs(0, 0, 1, 14), Quadrant.R180),
	)
	static CuboidRotation FISHING_STATION_ROTATION_2 = new CuboidRotation(new Vector3f(1, 0, 4), new CuboidRotation.SingleAxisRotation(Direction.Axis.X, -22.5), false)
	static CuboidFace MISSING_FACE = face(KEY_MISSING, uvs(0, 0, 0, 9))
	static Map<Direction, CuboidFace> FISHING_STATION_FACE_MAP_2 = Map.of(
		Direction.NORTH, MISSING_FACE, Direction.EAST, face(KEY_ONE, uvs(7, 0, 16, 9)),
		Direction.SOUTH, MISSING_FACE, Direction.WEST, face(KEY_ONE, uvs(7, 0, 16, 9), Quadrant.R90),
		Direction.UP, MISSING_FACE, Direction.DOWN, MISSING_FACE,
	)
	static List<CuboidModelElement> FISHING_STATION_ELEMENTS = List.of(
		new CuboidModelElement(new Vector3f(0, 0, 0), new Vector3f(16, 16, 16), Map.of(
			Direction.NORTH, face(TextureSlot.NORTH, uvs(0, 0, 16, 16)),
			Direction.EAST, face(TextureSlot.EAST, uvs(0, 0, 16, 16)),
			Direction.SOUTH, face(TextureSlot.SOUTH, uvs(0, 0, 16, 16)),
			Direction.WEST, face(TextureSlot.WEST, uvs(0, 0, 16, 16)),
			Direction.UP, face(TextureSlot.UP, uvs(0, 0, 16, 16)),
			Direction.DOWN, face(TextureSlot.DOWN, uvs(0, 0, 16, 16)),
		)),
		new CuboidModelElement(
			new Vector3f(1, 0, -10), new Vector3f(2, 1, 4),
			FISHING_STATION_FACE_MAP_1, FISHING_STATION_ROTATION_1, null, 0
		),
		new CuboidModelElement(
			new Vector3f(14, 0, -10), new Vector3f(15, 1, 4),
			FISHING_STATION_FACE_MAP_1, FISHING_STATION_ROTATION_1, null, 0
		),
		new CuboidModelElement(
			new Vector3f(2, 0.5, -9.5), new Vector3f(14, 0.5, 4.5),
			Map.of(
				Direction.NORTH, face(KEY_ZERO, uvs(0, 0, 12, 0), Quadrant.R180),
				Direction.EAST, face(KEY_ZERO, uvs(0, 0, 0, 14), Quadrant.R90),
				Direction.SOUTH, face(KEY_ZERO, uvs(0, 0, 12, 0)),
				Direction.WEST, face(KEY_ZERO, uvs(0, 0, 0, 14), Quadrant.R270),
				Direction.UP, face(KEY_ZERO, uvs(4, 0, 16, 14)),
				Direction.DOWN, face(KEY_ZERO, uvs(4, 0, 16, 14), Quadrant.R180),
			),
			FISHING_STATION_ROTATION_1, null, 0
		),
		new CuboidModelElement(
			new Vector3f(14.5, 1, -5), new Vector3f(14.5, 10, 4),
			FISHING_STATION_FACE_MAP_2, FISHING_STATION_ROTATION_2, null, 0
		),
		new CuboidModelElement(
			new Vector3f(1.5, 1, -5), new Vector3f(1.5, 10, 4),
			FISHING_STATION_FACE_MAP_2, FISHING_STATION_ROTATION_2, null, 0
		),
	)
	static JsonModel.DisplayMap NANOSABER_DISPLAY = new JsonModel.DisplayMap()
		.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, transformation(0, 0, 0, 0, 3, 0, 1, 1, 1))
		.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, transformation(0, 0, 0, 0, 3, 0, 1, 1, 1))
		.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, transformation(0, 0, -5, 0, 4, 2, 0.5, 0.5, 0.5))
		.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, transformation(0, 0, -5, 0, 4, 2, 0.5, 0.5, 0.5))
		.put(ItemDisplayContext.GUI, transformation(90, 45, -90, -3.75, -3.75, 0, 0.65, 0.65, 0.65))
		.put(ItemDisplayContext.GROUND, transformation(45, 0, 0, 0, 3, -2, 0.5, 0.5, 0.5))
		.put(ItemDisplayContext.FIXED, transformation(0, 90, 0, 0, -4, 0, 0.5, 0.5, 0.5))
	static List<CuboidModelElement> NANOSABER_ELEMENT = List.of(
		new CuboidModelElement(new Vector3f(7, 0, 7), new Vector3f(9, 5, 9), Map.of(
			Direction.NORTH, face(TextureSlot.TEXTURE, uvs(5, 4, 6, 6.5)),
			Direction.EAST, face(TextureSlot.TEXTURE, uvs(4, 4, 5, 6.5)),
			Direction.SOUTH, face(TextureSlot.TEXTURE, uvs(7, 4, 8, 6.5)),
			Direction.WEST, face(TextureSlot.TEXTURE, uvs(6, 4, 7, 6.5)),
			Direction.UP, face(TextureSlot.TEXTURE, uvs(6, 4, 5, 3)),
			Direction.DOWN, face(TextureSlot.TEXTURE, uvs(7, 3, 6, 4)),
		)),
		new CuboidModelElement(new Vector3f(5.5, 5, 5.5), new Vector3f(10.5, 6, 10.5), Map.of(
			Direction.NORTH, face(TextureSlot.TEXTURE, uvs(6.5, 2.5, 9, 3)),
			Direction.EAST, face(TextureSlot.TEXTURE, uvs(4, 2.5, 6.5, 3)),
			Direction.SOUTH, face(TextureSlot.TEXTURE, uvs(11.5, 2.5, 14, 3)),
			Direction.WEST, face(TextureSlot.TEXTURE, uvs(9, 2.5, 11.5, 3)),
			Direction.UP, face(TextureSlot.TEXTURE, uvs(9, 2.5, 6.5, 0)),
			Direction.DOWN, face(TextureSlot.TEXTURE, uvs(11.5, 0, 9, 2.5)),
		)),
		new CuboidModelElement(new Vector3f(7.5, 6, 7), new Vector3f(8.5, 30, 9), Map.of(
			Direction.NORTH, face(TextureSlot.TEXTURE, uvs(1, 1, 1.5, 13)),
			Direction.EAST, face(TextureSlot.TEXTURE, uvs(0, 1, 1, 13)),
			Direction.SOUTH, face(TextureSlot.TEXTURE, uvs(2.5, 1, 3, 13)),
			Direction.WEST, face(TextureSlot.TEXTURE, uvs(1.5, 1, 2.5, 13)),
			Direction.UP, face(TextureSlot.TEXTURE, uvs(1.5, 1, 1, 0)),
			Direction.DOWN, face(TextureSlot.TEXTURE, uvs(2, 0, 1.5, 1)),
		)),
		new CuboidModelElement(new Vector3f(7, 6, 7.5), new Vector3f(9, 9, 10.5), Map.of(
			Direction.NORTH, face(TextureSlot.TEXTURE, uvs(1.5, 14.5, 2.5, 16)),
			Direction.EAST, face(TextureSlot.TEXTURE, uvs(0, 14.5, 1.5, 16)),
			Direction.SOUTH, face(TextureSlot.TEXTURE, uvs(4, 14.5, 5, 16)),
			Direction.WEST, face(TextureSlot.TEXTURE, uvs(2.5, 14.5, 4, 16)),
			Direction.UP, face(TextureSlot.TEXTURE, uvs(2.5, 14.5, 1.5, 13)),
			Direction.DOWN, face(TextureSlot.TEXTURE, uvs(3.5, 13, 2.5, 14.5)),
		)),
		new CuboidModelElement(new Vector3f(8, 6, 6), new Vector3f(8, 30, 7), Map.of(
			Direction.NORTH, face(TextureSlot.TEXTURE, uvs(3.5, 0.5, 3.5, 12.5)),
			Direction.EAST, face(TextureSlot.TEXTURE, uvs(3, 0.5, 3.5, 12.5)),
			Direction.SOUTH, face(TextureSlot.TEXTURE, uvs(4, 0.5, 4, 12.5)),
			Direction.WEST, face(TextureSlot.TEXTURE, uvs(3.5, 0.5, 4, 12.5)),
			Direction.UP, face(TextureSlot.TEXTURE, uvs(3.5, 0.5, 3.5, 0)),
			Direction.DOWN, face(TextureSlot.TEXTURE, uvs(3.5, 0, 3.5, 0.5)),
		)),
		new CuboidModelElement(new Vector3f(8, 30, 7), new Vector3f(8, 32, 8), Map.of(
			Direction.NORTH, face(TextureSlot.TEXTURE, uvs(4.5, 1.5, 4.5, 2.5)),
			Direction.EAST, face(TextureSlot.TEXTURE, uvs(4, 1.5, 4.5, 2.5)),
			Direction.SOUTH, face(TextureSlot.TEXTURE, uvs(5, 1.5, 5, 2.5)),
			Direction.WEST, face(TextureSlot.TEXTURE, uvs(4.5, 1.5, 5, 2.5)),
			Direction.UP, face(TextureSlot.TEXTURE, uvs(4.5, 1.5, 4.5, 1)),
			Direction.DOWN, face(TextureSlot.TEXTURE, uvs(4.5, 1, 4.5, 1.5)),
		)),
		new CuboidModelElement(new Vector3f(7.5, 30, 8), new Vector3f(8.5, 32, 9), Map.of(
			Direction.NORTH, face(TextureSlot.TEXTURE, uvs(4.5, 0.5, 5, 1.5)),
			Direction.EAST, face(TextureSlot.TEXTURE, uvs(4, 0.5, 4.5, 1.5)),
			Direction.SOUTH, face(TextureSlot.TEXTURE, uvs(5.5, 0.5, 6, 1.5)),
			Direction.WEST, face(TextureSlot.TEXTURE, uvs(5, 0.5, 5.5, 1.5)),
			Direction.UP, face(TextureSlot.TEXTURE, uvs(5, 0.5, 4.5, 0)),
			Direction.DOWN, face(TextureSlot.TEXTURE, uvs(5.5, 0, 5, 0.5)),
		)),
	)

	@FunctionalInterface
	interface Upload<V, R> {
		R upload(V target);
	}
	@FunctionalInterface
	interface Uploadable<T> extends Upload<T, Identifier> {
		JsonModel apply(T);
		@Override
		default Identifier upload(T target) {
			apply(target).upload()
		}
	}
	static Upload<JsonModel, Pair<Identifier, Identifier>> ACTIVE = (JsonModel model) ->
		Pair.of(model.suffix("_off").upload(), model.suffix("_on").upload())
	static Uploadable<Item> ENERGY_ITEM = (Item item) -> GENERATED.create(item).add(TextureMapping.layer0(item), TextureSlot.LAYER0)
	static Uploadable<Item> ENERGY_ITEM_ACTIVE = (Item item) -> ENERGY_ITEM.apply(item).suffix("_active")
	static Uploadable<Item> ENERGY_ITEM_HANDHELD = (Item item) -> ENERGY_ITEM.apply(item).add(HANDHELD)
	static Uploadable<Item> ENERGY_ITEM_HANDHELD_ACTIVE = (Item item) -> ENERGY_ITEM_ACTIVE.apply(item).add(HANDHELD)
	static def SOLAR_PANEL = wrapperBlock { block, id ->
		CUBE_BOTTOM_TOP.create(block).add(
			cubeBottomTop(
				withSuffix(id, "_top"),
				new Material(TexturePaths.generatorBottom),
				new Material(TexturePaths.solarPanelSide),
			),
			TextureSlot.SIDE
		)
	}
	static def QUANTUM_SOLAR_PANEL = wrapperBlock { block, id ->
		CUBE_BOTTOM_TOP.create(block).add(
			cubeBottomTop(
				withSuffix(id, "_top"),
				new Material(TexturePaths.quantumBottom),
				new Material(TexturePaths.quantumSolarPanelSide),
			),
			TextureSlot.SIDE
		)
	}
	static Function<Block, JsonModel> CUBE_BOTTOM_TOP_SIDE = { Block block ->
		CUBE_BOTTOM_TOP.create(block).add(TextureMapping.cubeBottomTop(block), TextureSlot.TOP, TextureSlot.SIDE)
	}
	static TriFunction<Block, Material, Material, JsonModel> CUBE_BOTTOM_TOP_BASE = { Block block, Material id, Material bottom ->
		CUBE_BOTTOM_TOP.create(block).add(
			cubeBottomTop(
				withSuffix(id, "_top"),
				bottom,
				withSuffix(id, "_side"),
			),
			TextureSlot.TOP, TextureSlot.SIDE
		)
	}
	static def GENERATOR_CUBE_BOTTOM_TOP = wrapperBlock { block, id -> CUBE_BOTTOM_TOP_BASE.apply(block, id, new Material(TexturePaths.generatorBottom)) }
	static def CHARGE_O_MAT = wrapperBlock { block, id -> CUBE_BOTTOM_TOP_BASE.apply(block, id, new Material(TexturePaths.machineTier2Bottom)) }
	static def CHUNK_LOADER = wrapperBlock { block, id -> CUBE_BOTTOM_TOP_BASE.apply(block, id, new Material(TexturePaths.machineTier3Bottom)) }
	static def ENERGY = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(orientable(
			withSuffix(id, "_side"),
			withSuffix(id, "_front"),
			withSuffix(id, "_side"),
		))
	}
	static Uploadable ORIENTABLE_SIDE_FRONT_TOP_BOTTOM = { Block block ->
		ORIENTABLE.create(block).add(TextureMapping.orientableCube(block))
	}
	static def GAS_TURBINE = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				withSuffix(id, "_top"),
				new Material(TexturePaths.generatorBottom),
				new Material(TexturePaths.generatorSide),
				new Material(TexturePaths.generatorSide),
			),
			TextureSlot.TOP, TextureSlot.FRONT
		)
	}
	static def FUSION_CONTROL_COMPUTER = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				new Material(TexturePaths.machineTier3Top),
				withSuffix(id, "_bottom"),
				withSuffix(id, "_west"),
				withSuffix(id, "_east"),
				new Material(TexturePaths.machineTier3Back),
				withSuffix(id, "_front"),
			),
			TextureSlot.FRONT
		)
	}
	static def SOLID_FUEL_GENERATOR = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				new Material(TexturePaths.generatorTop),
				new Material(TexturePaths.generatorBottom),
				new Material(TexturePaths.generatorSide),
				new Material(TexturePaths.generatorSide),
			),
			TextureSlot.FRONT
		)
	}
	static def WIND_MILL = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(orientable(
			withSuffix(id, "_top"),
			withSuffix(id, "_bottom"),
			withSuffix(id, "_west"),
			withSuffix(id, "_east"),
			new Material(TexturePaths.generatorBottom),
			withSuffix(id, "_front"),
		))
	}
	static TriFunction<Block, Material, Material, Material> STORAGE_UNIT = { Block block, Material id, Material bottom ->
		ORIENTABLE.create(block).add(orientable(
			withSuffix(id, "_top"),
			bottom,
			withSuffix(id, "_side"),
			withSuffix(id, "_front"),
		))
	}
	static def BASIC_STORAGE_UNIT = wrapperBlock { block, id -> STORAGE_UNIT.apply(block, id, new Material(TexturePaths.basicUnitBottom)) }
	static def ADVANCED_STORAGE_UNIT = wrapperBlock { block, id -> STORAGE_UNIT.apply(block, id, new Material(TexturePaths.advancedUnitBottom)) }
	static def INDUSTRIAL_STORAGE_UNIT = wrapperBlock { block, id -> STORAGE_UNIT.apply(block, id, new Material(TexturePaths.industrialUnitBottom)) }
	static TriFunction<Block, Material, Material, JsonModel> ORIENTABLE_TOP_SIDE_BOTTOM = { Block block, Material id, Material bottom ->
		ORIENTABLE.create(block).add(orientable(
			withSuffix(id, "_top"),
			bottom,
			withSuffix(id, "_side"),
			withSuffix(id, "_side"),
		))
	}
	static def QUANTUM_STORAGE_UNIT = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(orientable(
			new Material(TexturePaths.quantumTop),
			new Material(TexturePaths.quantumBottom),
			withSuffix(id, "_side"),
			withSuffix(id, "_front"),
		))
	}
	static def BASIC_TANK_UNIT = wrapperBlock { block, id -> ORIENTABLE_TOP_SIDE_BOTTOM.apply(block, id, new Material(TexturePaths.basicUnitBottom)) }
	static def ADVANCED_TANK_UNIT = wrapperBlock { block, id -> ORIENTABLE_TOP_SIDE_BOTTOM.apply(block, id, new Material(TexturePaths.advancedUnitBottom)) }
	static def INDUSTRIAL_TANK_UNIT = wrapperBlock { block, id -> ORIENTABLE_TOP_SIDE_BOTTOM.apply(block, id, new Material(TexturePaths.industrialUnitBottom)) }
	static def QUANTUM_TANK_UNIT = wrapperBlock { block, id -> ORIENTABLE_TOP_SIDE_BOTTOM.apply(block, id, new Material(TexturePaths.quantumBottom)) }
	static def LAUNCHPAD = wrapperBlock { block, id -> ORIENTABLE_TOP_SIDE_BOTTOM.apply(block, id, new Material(TexturePaths.machineTier2Bottom)) }
	static def ELEVATOR = wrapperBlock { block, id -> ORIENTABLE_TOP_SIDE_BOTTOM.apply(block, id, withSuffix(id, "_top")) }
	static def NUCLEAR_REACTOR = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
			withSuffix(id, "_top"),
			withSuffix(id, "_bottom"),
			withSuffix(id, "_side"),
			withSuffix(id, "_front"),
			),
			TextureSlot.FRONT
		)
	}
	static TriFunction<Block, Material, Material, JsonModel> MACHINE_BASE = { Block block, Material id, Material base ->
		ORIENTABLE.create(block).add(
			orientable(
				withSuffix(base, "machine_top"),
				withSuffix(base, "machine_bottom"),
				withSuffix(base, "machine_side"),
				withSuffix(id, "_front"),
			),
			TextureSlot.FRONT
		)
	}
	static def BASIC_MACHINE = wrapperBlock { block, id -> MACHINE_BASE.apply(block, id, new Material(TexturePaths.machineTier0Dir)) }
	static def ADVANCED_MACHINE = wrapperBlock { block, id -> MACHINE_BASE.apply(block, id, new Material(TexturePaths.machineTier1Dir)) }
	static def COMPUTER_CUBE = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(orientable(
			new Material(TexturePaths.machineTier2Top),
			new Material(TexturePaths.machineTier1Bottom),
			withSuffix(id, "_side"),
			withSuffix(id, "_front"),
		))
	}
	static def PLAYER_DETECTOR_OTHERS = wrapperBlock { block, id -> CUBE_ALL.apply(block).suffix("_others") }
	static def PLAYER_DETECTOR_YOU = wrapperBlock { block, id -> CUBE_ALL.apply(block).suffix("_you") }
	static def ADVANCED_MACHINE_WITH_TOP = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				withSuffix(id, "_top"),
				new Material(TexturePaths.machineTier1Bottom),
				new Material(TexturePaths.machineTier1Side),
				withSuffix(id, "_front")
			),
			TextureSlot.TOP, TextureSlot.FRONT
		)
	}
	static TriFunction<Block, Material, Material, JsonModel> ACTIVE_FRONT_MACHINE_BASE = { Block block, Material id, Material base ->
		ORIENTABLE.create(block).add(
			orientable(
				withSuffix(base, "machine_top"),
				withSuffix(base, "machine_bottom"),
				withSuffix(base, "machine_west"),
				withSuffix(base, "machine_east"),
				withSuffix(base, "machine_back"),
				withSuffix(id, "_front"),
			),
			TextureSlot.FRONT
		)
	}
	static def ACTIVE_FRONT_MACHINE = wrapperBlock { block, id -> ACTIVE_FRONT_MACHINE_BASE.apply(block, id, new Material(TexturePaths.machineTier2Dir)) }
	static def ACTIVE_SIDE_MACHINE = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				new Material(TexturePaths.machineTier2Top),
				new Material(TexturePaths.machineTier2Bottom),
				withSuffix(id, "_west"),
				withSuffix(id, "_east"),
				new Material(TexturePaths.machineTier2Back),
				withSuffix(id, "_front")
			),
			TextureSlot.WEST, TextureSlot.EAST, TextureSlot.FRONT
		)
	}
	static def ACTIVE_TOP_FRONT_MACHINE = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				withSuffix(id, "_top"),
				new Material(TexturePaths.machineTier2Bottom),
				new Material(TexturePaths.machineTier2West),
				new Material(TexturePaths.machineTier2East),
				new Material(TexturePaths.machineTier2Back),
				withSuffix(id, "_front"),
			),
			TextureSlot.UP, TextureSlot.FRONT
		)
	}
	static def PUMP = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(
			orientable(
				withSuffix(id, "_top"),
				withSuffix(id, "_bottom"),
				withSuffix(id, "_side"),
				withSuffix(id, "_side"),
				withSuffix(id, "_front"),
				withSuffix(id, "_front"),
			),
			TextureSlot.SOUTH, TextureSlot.FRONT
		)
	}
	static def FLUID_REPLICATOR = wrapperBlock { block, id -> ACTIVE_FRONT_MACHINE_BASE.apply(block, id, new Material(TexturePaths.machineTier3Dir)) }

	static BiFunction<Block, Material, JsonModel> LIGHT_BLOCK = (Block block, Material id) -> BLOCK.create(block).add(
		new TextureMapping().put(TextureSlot.PARTICLE, id).put(KEY_ZERO, id),
		TextureSlot.PARTICLE, KEY_ZERO
	)
	static def ALARM_LIGHT = wrapperBlock { block, id ->
		LIGHT_BLOCK.apply(block, id).add(LIGHT_DISPLAY_1).add(List.of(
			new CuboidModelElement(new Vector3f(3, 0, 3), new Vector3f(13, 3, 13), LIGHT_BASE_ELEMENT,)
		))
	}
	static def LAMP_LED_LIGHT = wrapperBlock { block, id ->
		LIGHT_BLOCK.apply(block, id).add(LIGHT_DISPLAY_1).add(List.of(
			new CuboidModelElement(new Vector3f(2, 0, 2), new Vector3f(14, 2, 14), LIGHT_BASE_ELEMENT)
		))
	}
	static def LAMP_INCANDESCENT_LIGHT = wrapperBlock { block, id ->
		LIGHT_BLOCK.apply(block, id).add(LIGHT_DISPLAY_2).add(List.of(
			new CuboidModelElement(
				new Vector3f(5, 0, 5), new Vector3f(11, 2, 11),
				Map.of(
					Direction.DOWN, LIGHT_FACE_1, Direction.UP, LIGHT_FACE_1, Direction.NORTH, LIGHT_FACE_1,
					Direction.SOUTH, LIGHT_FACE_1, Direction.WEST, LIGHT_FACE_1, Direction.EAST, LIGHT_FACE_1,
				)
			),
			new CuboidModelElement(
				new Vector3f(4, 2, 4), new Vector3f(12, 10, 12),
				Map.of(
					Direction.DOWN, LIGHT_FACE_2, Direction.UP, LIGHT_FACE_2, Direction.NORTH, LIGHT_FACE_2,
					Direction.SOUTH, LIGHT_FACE_2, Direction.WEST, LIGHT_FACE_2, Direction.EAST, LIGHT_FACE_2,
				)
			),
		))
	}
	static BiFunction<Block, Material, JsonModel> RESIN_BASIN_BASE = (Block block, Material id) -> new JsonModel().id(block).add(
		new TextureMapping().put(TextureSlot.PARTICLE, withSuffix(id, "_sap_flowing"))
			.put(KEY_ZERO, withSuffix(id, "_top"))
			.put(KEY_ONE, withSuffix(id, "_side"))
			.put(KEY_TWO, withSuffix(id, "_bottom"))
			.put(KEY_THREE, withSuffix(id, "_inner"))
	)
	static def RESIN_BASIN_EMPTY = wrapperBlock { block, id ->
		RESIN_BASIN_BASE.apply(block, id).add(RESIN_BASIN_BASE_ELEMENT).add(RESIN_BASIN_DISPLAY)
	}
	static def RESIN_BASIN_FLOWING = wrapperBlock { block, id ->
		List<CuboidModelElement> elements = new ArrayList<>(RESIN_BASIN_BASE_ELEMENT)
		elements.add(new CuboidModelElement(
			new Vector3f(6.5, 11, 9), new Vector3f(9.5, 12, 19),
			Map.of(
				Direction.NORTH, face(TextureSlot.PARTICLE, uvs(0, 0, 3, 1)),
				Direction.EAST, face(TextureSlot.PARTICLE, uvs(0, 0, 10, 1)),
				Direction.SOUTH, face(TextureSlot.PARTICLE, uvs(0, 0, 3, 1)),
				Direction.WEST, face(TextureSlot.PARTICLE, uvs(0, 0, 10, 1)),
				Direction.UP, face(TextureSlot.PARTICLE, uvs(0, 6, 3, 16)),
				Direction.DOWN, face(TextureSlot.PARTICLE, uvs(0, 0, 3, 10)),
			),
			new CuboidRotation(new Vector3f(8, 11.25, 14), new CuboidRotation.SingleAxisRotation(Direction.Axis.X, -22.5), false),
			null,
			0
		))
		elements.add(new CuboidModelElement(
			new Vector3f(6.5, 3, 9.1), new Vector3f(9.5, 10, 9.6),
			Map.of(
				Direction.NORTH, face(TextureSlot.PARTICLE, uvs(0, 0, 3, 7)),
				Direction.EAST, face(TextureSlot.PARTICLE, uvs(0, 0, 0.5, 7)),
				Direction.SOUTH, face(TextureSlot.PARTICLE, uvs(0, 0, 3, 7)),
				Direction.WEST, face(TextureSlot.PARTICLE, uvs(0, 0, 0.5, 7)),
				Direction.UP, face(TextureSlot.PARTICLE, uvs(0, 0, 3, 0.5)),
				Direction.DOWN, face(TextureSlot.PARTICLE, uvs(0, 0, 3, 0.5)),
			),
			new CuboidRotation(new Vector3f(13, 11, 17), new CuboidRotation.SingleAxisRotation(Direction.Axis.X, 0), false),
			null,
			0
		))
		RESIN_BASIN_BASE.apply(block, id).add(elements).suffix("_flowing")
	}
	static def RESIN_BASIN_FULL = wrapperBlock { block, id ->
		JsonModel model = RESIN_BASIN_BASE.apply(block, id)
		model.textures = model.textures.copyAndUpdate(KEY_FOUR, withSuffix(id, "_sap_still"))
		List<CuboidModelElement> elements = new ArrayList<>(RESIN_BASIN_BASE_ELEMENT)
		elements.add(new CuboidModelElement(
			new Vector3f(1, 1, 1), new Vector3f(15, 6, 15),
			Map.of(
				Direction.NORTH, face(KEY_FOUR, uvs(0, 0, 14, 5)),
				Direction.EAST, face(KEY_FOUR, uvs(0, 0, 14, 1)),
				Direction.SOUTH, face(KEY_FOUR, uvs(0, 0, 14, 1)),
				Direction.WEST, face(KEY_FOUR, uvs(0, 0, 14, 5)),
				Direction.UP, face(KEY_FOUR, uvs(1, 1, 15, 15)),
				Direction.DOWN, face(KEY_FOUR, uvs(0, 0, 14, 14)),
			),
			new CuboidRotation(new Vector3f(9, 13, 9), new CuboidRotation.SingleAxisRotation(Direction.Axis.Y, 0), false),
			null,
			0
		))
		model.add(elements).suffix("_full")
	}
	static def FISHING_STATION = wrapperBlock { block, id ->
		ORIENTABLE.create(block).add(FISHING_STATION_DISPLAY).add(FISHING_STATION_ELEMENTS).add(
			orientable(
				new Material(TexturePaths.machineTier2Top),
				new Material(TexturePaths.machineTier2Bottom),
				new Material(TexturePaths.machineTier2West),
				new Material(TexturePaths.machineTier2East),
				new Material(TexturePaths.machineTier2Back),
				id,
			).put(TextureSlot.PARTICLE, new Material(TexturePaths.machineTier2Top))
			.put(KEY_ZERO, withSuffix(id, "_net"))
			.put(KEY_ONE, withSuffix(id, "_net_side"))
			.put(KEY_MISSING, new Material(Identifier.withDefaultNamespace("missingno")))
		)
	}
	static def RUBBER_LOG_WITH_SAP = wrapperBlock { block, id ->
		ORIENTABLE.create(block).suffix("_with_sap").add(orientable(
			withSuffix(id, "_top"),
			withSuffix(id, "_sap"),
			id,
		))
	}
	static def FLUID = wrapperBlock { block, material ->
		new JsonModel().id(block).add(TextureMapping.particle(withSuffix(material, "_still")))
	}
	static BiFunction<Block, Material, JsonModel> CABLE_CORE_BASE = { Block block, Material id ->
		new JsonModel().id(block).suffix("_core").add(texture(id))
	}
	static BiFunction<Block, Material, JsonModel> CABLE_SIDE_BASE = { Block block, Material id ->
		new JsonModel().id(block).suffix("_side").add(texture(id))
	}
	static def CABLE_CORE = wrapperBlock { block, id -> CABLE_CORE_BASE.apply(block, id).add(CABLE_CORE_ELEMENT) }
	static def CABLE_SIDE = wrapperBlock { block, id -> CABLE_SIDE_BASE.apply(block, id).add(CABLE_SIDE_ELEMENT) }
	static def CABLE_THICK_CORE = wrapperBlock { block, id -> CABLE_CORE_BASE.apply(block, id).add(CABLE_THICK_CORE_ELEMENT) }
	static def CABLE_THICK_SIDE = wrapperBlock { block, id -> CABLE_SIDE_BASE.apply(block, id).add(CABLE_THICK_SIDE_ELEMENT) }
	static Upload<Block, List<Identifier>> MACHINE_CASING = { Block block ->
		Material texture = TextureMapping.getBlockTexture(block)
		Identifier id = ModelLocationUtils.getModelLocation(block)
		TextureMapping textures = new TextureMapping()
			.put(KEY_ALONE, texture)
			.put(KEY_START, withSuffix(texture, "_start"))
			.put(KEY_MIDDLE, withSuffix(texture, "_middle"))
			.put(KEY_END, withSuffix(texture, "_end"))
			.put(TextureSlot.create(TextureSlot.UP.getId(), KEY_ALONE), null)
			.put(TextureSlot.create(TextureSlot.DOWN.getId(), KEY_ALONE), null)
			.put(TextureSlot.create(TextureSlot.PARTICLE.getId(), KEY_ALONE), null)
		Identifier parent = new JsonModel().add(Identifier.withDefaultNamespace("block/cube"))
			.id(id.withSuffix("_textures")).add(textures).upload()
		int size = 16;
		List<Identifier> ids = new ArrayList<>(size)
		for (int i = 0; i < size; i++) {
			textures = new TextureMapping()
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				switch (DirectionUtils.getHorizontalPart(direction, i)) {
					case HorizontalPart.ALONE -> textures.put(TextureSlot.create(direction.getName(), KEY_ALONE), null)
					case HorizontalPart.START -> textures.put(TextureSlot.create(direction.getName(), KEY_START), null)
					case HorizontalPart.MIDDLE -> textures.put(TextureSlot.create(direction.getName(), KEY_MIDDLE), null)
					case HorizontalPart.END -> textures.put(TextureSlot.create(direction.getName(), KEY_END), null)
				}
			}
			ids.add(new JsonModel().add(parent).id(i > 0 ? id.withSuffix("_" + i) : id).add(textures).upload())
		}
		return ids
	}
	static def NANOSABER_BASE = wrapperItem { item, id ->
		new JsonModel().add(NANOSABER_DISPLAY).add(NANOSABER_ELEMENT).id(item).add(texture(id), TextureSlot.TEXTURE, TextureSlot.PARTICLE)
	}
	static Uploadable<Item> NANOSABER_OFF = { Item item -> NANOSABER_BASE.apply(item).suffix("_off") }
	static Uploadable<Item> NANOSABER_ON = { Item item -> NANOSABER_BASE.apply(item).suffix("_on") }
	static Uploadable<Item> NANOSABER_LOW = { Item item -> NANOSABER_BASE.apply(item).suffix("_low") }
	static def CELL_BASE = wrapperItem { item, id ->
		GENERATED.create(item).suffix("_base").add(CELL_DISPLAY).add(
			TextureMapping.layer0(id).put(TextureSlot.PARTICLE, id),
			TextureSlot.LAYER0, TextureSlot.PARTICLE
		)
	}
	static def CELL_BACKGROUND = wrapperItem { item, id ->
		CELL_TEMPLATE.create(item).suffix("_background").add(TextureMapping.defaultTexture(id), TextureSlot.TEXTURE)
	}
	static def CELL_GLASS = wrapperItem { item, id ->
		CELL_TEMPLATE.create(item).suffix("_glass").add(TextureMapping.defaultTexture(id), TextureSlot.TEXTURE)
	}
	static Uploadable<Identifier> BUCKET_BASE = { Identifier id ->
		GENERATED.create(id).suffix("_base").add(CELL_DISPLAY).add(
			TextureMapping.layer0(new Material(id)).put(TextureSlot.PARTICLE, new Material(id)),
			TextureSlot.LAYER0, TextureSlot.PARTICLE
		)
	}
	static Uploadable<Identifier> BUCKET_BACKGROUND = { Identifier id ->
		BUCKET_TEMPLATE.create(id).suffix("_background").add(TextureMapping.defaultTexture(new Material(id)), TextureSlot.TEXTURE)
	}

	static TextureMapping orientable(Material top, Material front, Material side) {
		return new TextureMapping()
			.put(TextureSlot.TOP, top)
			.put(TextureSlot.FRONT, front)
			.put(TextureSlot.SIDE, side)
	}

	static TextureMapping orientable(Material top, Material bottom, Material side, Material front) {
		return orientable(top, front, side).put(TextureSlot.BOTTOM, bottom)
	}

	static TextureMapping orientable(Material up, Material bottom, Material west, Material east, Material south, Material front) {
		return new TextureMapping()
			.put(TextureSlot.UP, up)
			.put(TextureSlot.BOTTOM, bottom)
			.put(TextureSlot.WEST, west)
			.put(TextureSlot.EAST, east)
			.put(TextureSlot.SOUTH, south)
			.put(TextureSlot.FRONT, front)
	}

	static TextureMapping cubeBottomTop(Material top, Material bottom, Material side) {
		return new TextureMapping()
			.put(TextureSlot.TOP, top)
			.put(TextureSlot.BOTTOM, bottom)
			.put(TextureSlot.SIDE, side)
	}

	static TextureMapping texture(Material id) {
		return new TextureMapping().put(TextureSlot.TEXTURE, id).put(TextureSlot.PARTICLE, id)
	}

	static CuboidFace.UVs uvs(float x1, float y1, float x2, float y2) {
		return new CuboidFace.UVs(x1, y1, x2, y2)
	}

	static CuboidFace face(TextureSlot texture, CuboidFace.UVs uvs) {
		return new CuboidFace(null, -1, texture.toString(), uvs, Quadrant.R0)
	}

	static CuboidFace face(TextureSlot texture, CuboidFace.UVs uvs, Quadrant rotation) {
		return new CuboidFace(null, -1, texture.toString(), uvs, rotation)
	}

	static ItemTransform transformation(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
		return new ItemTransform(new Vector3f(x1, y1, z1), new Vector3f(x2, y2, z2), new Vector3f(x3, y3, z3))
	}

	static Uploadable<Block> wrapperBlock(BiFunction<Block, Material, JsonModel> fun) {
		return (Block block) -> fun.apply(block, TextureMapping.getBlockTexture(block))
	}

	static Uploadable<Item> wrapperItem(BiFunction<Item, Material, JsonModel> fun) {
		return (Item item) -> fun.apply(item, TextureMapping.getItemTexture(item))
	}

	static Material withSuffix(Material material, String suffix) {
		return new Material(material.sprite().withSuffix(suffix), material.forceTranslucent())
	}
}
