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
import net.minecraft.client.data.BlockStateModelGenerator
import net.minecraft.client.data.BlockStateVariant
import net.minecraft.client.data.BlockStateVariantMap
import net.minecraft.client.data.TextureMap
import net.minecraft.client.data.VariantSettings
import net.minecraft.client.data.When
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import org.apache.commons.lang3.function.TriFunction
import org.apache.commons.lang3.tuple.Pair
import reborncore.common.blocks.BlockMachineBase
import techreborn.blocks.machine.tier1.PlayerDetectorBlock
import techreborn.blocks.machine.tier1.ResinBasinBlock
import techreborn.blocks.misc.BlockRubberLog

import java.util.function.BiFunction
import java.util.function.Function

class TemplateState {
	static BlockStateVariantMap NORTH_DEFAULT_FACING = BlockStateModelGenerator.createNorthDefaultRotationStates()
	static BlockStateVariantMap NORTH_DEFAULT_H_FACING = BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()
	static BlockStateVariantMap UP_DEFAULT_FACING = BlockStateVariantMap.create(Properties.FACING)
		.register(Direction.DOWN, BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R180))
		.register(Direction.UP, BlockStateVariant.create())
		.register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R90))
		.register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R270))
		.register(
			Direction.WEST,
			BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R270)
		)
		.register(
			Direction.EAST,
			BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R90)
		)

	@FunctionalInterface
	interface Uploadable {
		Identifier upload(Block block);
	}
	static Uploadable SINGLE = (Block block) -> new StateModel().add(model(block)).upload(block)
	static Uploadable SINGLE_NORTH_DEFAULT_FACING = (Block block) -> new StateModel().add(model(block)).add(NORTH_DEFAULT_FACING).upload(block)
	static Uploadable SINGLE_NORTH_DEFAULT_H_FACING = (Block block) -> new StateModel().add(model(block)).add(NORTH_DEFAULT_H_FACING).upload(block)
	static Function<Pair<Identifier, Identifier>, StateModel> ACTIVE = (Pair<Identifier, Identifier> pair) -> new StateModel().add(
		BlockStateVariantMap.create(BlockMachineBase.ACTIVE).register(false, model(pair.left)).register(true, model(pair.right))
	)
	static Function<Pair<Identifier, Identifier>, StateModel> ACTIVE_NORTH_DEFAULT_H_FACING = (Pair<Identifier, Identifier> pair) -> {
		ACTIVE.apply(pair).add(NORTH_DEFAULT_H_FACING)
	}
	static Function<Pair<Identifier, Identifier>, StateModel> ACTIVE_UP_DEFAULT_FACING = (Pair<Identifier, Identifier> pair) -> {
		ACTIVE.apply(pair).add(UP_DEFAULT_FACING)
	}
	static TriFunction<Identifier, Identifier, Identifier, StateModel> RESIN_BASIN = (Identifier empty, Identifier flowing, Identifier full) -> {
		new StateModel().add(NORTH_DEFAULT_H_FACING).add(
			BlockStateVariantMap.create(ResinBasinBlock.POURING)
				.register(false, BlockStateVariant.create().put(VariantSettings.MODEL, empty))
				.register(true, BlockStateVariant.create().put(VariantSettings.MODEL, flowing))
		).add(
			BlockStateVariantMap.create(ResinBasinBlock.FULL)
				.register(false, BlockStateVariant.create())
				.register(true, BlockStateVariant.create().put(VariantSettings.MODEL, full))
		)
	}
	static TriFunction<Identifier, Identifier, Identifier, StateModel> PLAYER_DETECTOR = (Identifier all, Identifier others, Identifier you) -> {
		new StateModel().add(
			BlockStateVariantMap.create(PlayerDetectorBlock.TYPE)
				.register(PlayerDetectorBlock.PlayerDetectorType.ALL, BlockStateVariant.create().put(VariantSettings.MODEL, all))
				.register(PlayerDetectorBlock.PlayerDetectorType.OTHERS, BlockStateVariant.create().put(VariantSettings.MODEL, others))
				.register(PlayerDetectorBlock.PlayerDetectorType.YOU, BlockStateVariant.create().put(VariantSettings.MODEL, you))
		)
	}
	static TriFunction<Identifier, Identifier, Identifier, StateModel> RUBBER_LOG = (Identifier vertical, Identifier horizontal, Identifier with_sap) -> {
		BlockStateVariantMap.TripleProperty<Direction.Axis, Direction, Boolean> map = BlockStateVariantMap
			.create(Properties.AXIS, Properties.HORIZONTAL_FACING, BlockRubberLog.HAS_SAP)
		for (Direction.Axis axis : Direction.Axis.VALUES) {
			for (Direction direction : Direction.Type.HORIZONTAL) {
				for (boolean has_sap : [false, true]) {
					BlockStateVariant variant = BlockStateVariant.create()
					if (axis == Direction.Axis.Y) {
						variant.put(VariantSettings.MODEL, has_sap ? with_sap : vertical)
						if (has_sap) {
							switch (direction) {
								case Direction.EAST:
									variant.put(VariantSettings.Y, VariantSettings.Rotation.R90)
									break
								case Direction.SOUTH:
									variant.put(VariantSettings.Y, VariantSettings.Rotation.R180)
									break
								case Direction.WEST:
									variant.put(VariantSettings.Y, VariantSettings.Rotation.R270)
									break
							}
						}
					} else {
						variant.put(VariantSettings.MODEL, horizontal).put(VariantSettings.X, VariantSettings.Rotation.R90)
						if (axis == Direction.Axis.X) {
							variant.put(VariantSettings.Y, VariantSettings.Rotation.R90)
						}
					}
					map.register(axis, direction, has_sap, variant)
				}
			}
		}
		return new StateModel().add(map)
	}
	static BiFunction<Identifier, Identifier, StateModel> CABLE = (Identifier core, Identifier side) -> {
		new StateModel().multipart().add(BlockStateVariant.create().put(VariantSettings.MODEL, core))
			.add(
				When.create().set(Properties.NORTH, true), BlockStateVariant.create().put(VariantSettings.MODEL, side)
			).add(
				When.create().set(Properties.EAST, true),
				BlockStateVariant.create().put(VariantSettings.MODEL, side).put(VariantSettings.Y, VariantSettings.Rotation.R90)
			).add(
				When.create().set(Properties.SOUTH, true),
				BlockStateVariant.create().put(VariantSettings.MODEL, side).put(VariantSettings.Y, VariantSettings.Rotation.R180)
			).add(
				When.create().set(Properties.WEST, true),
				BlockStateVariant.create().put(VariantSettings.MODEL, side).put(VariantSettings.Y, VariantSettings.Rotation.R270)
			).add(
				When.create().set(Properties.UP, true),
				BlockStateVariant.create().put(VariantSettings.MODEL, side).put(VariantSettings.X, VariantSettings.Rotation.R270)
			).add(
				When.create().set(Properties.DOWN, true),
				BlockStateVariant.create().put(VariantSettings.MODEL, side).put(VariantSettings.X, VariantSettings.Rotation.R90)
			)
	}
	static BlockStateVariant model(Identifier id) {
		return BlockStateVariant.create().put(VariantSettings.MODEL, id)
	}
	static BlockStateVariant model(Block block) {
		return model(TextureMap.getId(block))
	}
}
