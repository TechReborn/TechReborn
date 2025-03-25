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
import net.minecraft.client.data.BlockStateVariantMap
import net.minecraft.client.data.TextureMap
import net.minecraft.client.render.model.json.ModelVariant
import net.minecraft.client.render.model.json.ModelVariantOperator
import net.minecraft.client.render.model.json.MultipartModelConditionBuilder
import net.minecraft.client.render.model.json.WeightedVariant
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.collection.Pool
import net.minecraft.util.math.Direction
import org.apache.commons.lang3.function.TriFunction
import org.apache.commons.lang3.tuple.Pair
import reborncore.common.blocks.BlockMachineBase
import techreborn.blocks.machine.tier1.PlayerDetectorBlock
import techreborn.blocks.machine.tier1.ResinBasinBlock
import techreborn.blocks.misc.BlockRubberLog

import java.util.function.BiFunction
import java.util.function.Function

import static net.minecraft.client.data.BlockStateModelGenerator.NO_OP;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_X_90;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_X_180;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_X_270;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_90;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_180;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_270;

class TemplateState {
	static BlockStateVariantMap<ModelVariantOperator> NORTH_DEFAULT_FACING = BlockStateVariantMap.operations(Properties.FACING)
		.register(Direction.DOWN, ROTATE_X_90)
		.register(Direction.UP, ROTATE_X_270)
		.register(Direction.NORTH, NO_OP)
		.register(Direction.SOUTH, ROTATE_Y_180)
		.register(Direction.WEST, ROTATE_Y_270)
		.register(Direction.EAST, ROTATE_Y_90);
	static BlockStateVariantMap<ModelVariantOperator> NORTH_DEFAULT_H_FACING = BlockStateVariantMap.operations(Properties.HORIZONTAL_FACING)
		.register(Direction.EAST, ROTATE_Y_90)
		.register(Direction.SOUTH, ROTATE_Y_180)
		.register(Direction.WEST, ROTATE_Y_270)
		.register(Direction.NORTH, NO_OP);
	static BlockStateVariantMap<ModelVariantOperator> UP_DEFAULT_FACING = BlockStateVariantMap.operations(Properties.FACING)
		.register(Direction.DOWN, ROTATE_X_180)
		.register(Direction.UP, NO_OP)
		.register(Direction.NORTH, ROTATE_X_90)
		.register(Direction.SOUTH, ROTATE_X_270)
		.register(Direction.WEST, ROTATE_X_90.then(ROTATE_Y_270))
		.register(Direction.EAST, ROTATE_X_90.then(ROTATE_Y_90))

	@FunctionalInterface
	interface Uploadable {
		Identifier upload(Block block);
	}
	static Uploadable SINGLE = (Block block) -> new StateModel().add(model(block)).upload(block)
	static Uploadable SINGLE_NORTH_DEFAULT_FACING = (Block block) -> new StateModel().add(model(block)).add(NORTH_DEFAULT_FACING).upload(block)
	static Uploadable SINGLE_NORTH_DEFAULT_H_FACING = (Block block) -> new StateModel().add(model(block)).add(NORTH_DEFAULT_H_FACING).upload(block)
	static Function<Pair<Identifier, Identifier>, StateModel> ACTIVE = (Pair<Identifier, Identifier> pair) -> new StateModel().add(
		BlockStateVariantMap.models(BlockMachineBase.ACTIVE).register(false, model(pair.left)).register(true, model(pair.right))
	)
	static Function<Pair<Identifier, Identifier>, StateModel> ACTIVE_NORTH_DEFAULT_H_FACING = (Pair<Identifier, Identifier> pair) -> {
		ACTIVE.apply(pair).add(NORTH_DEFAULT_H_FACING)
	}
	static Function<Pair<Identifier, Identifier>, StateModel> ACTIVE_UP_DEFAULT_FACING = (Pair<Identifier, Identifier> pair) -> {
		ACTIVE.apply(pair).add(UP_DEFAULT_FACING)
	}
	static TriFunction<Identifier, Identifier, Identifier, StateModel> RESIN_BASIN = (Identifier empty, Identifier flowing, Identifier full) -> {
		new StateModel().add(
			BlockStateVariantMap.models(ResinBasinBlock.POURING, ResinBasinBlock.FULL)
				.register(false, false, model(empty))
				.register(true, false, model(flowing))
				.register(false, true, model(full))
				.register(true, true, model(full))
		).add(NORTH_DEFAULT_H_FACING)
	}
	static TriFunction<Identifier, Identifier, Identifier, StateModel> PLAYER_DETECTOR = (Identifier all, Identifier others, Identifier you) -> {
		new StateModel().add(
			BlockStateVariantMap.models(PlayerDetectorBlock.TYPE)
				.register(PlayerDetectorBlock.PlayerDetectorType.ALL, model(all))
				.register(PlayerDetectorBlock.PlayerDetectorType.OTHERS, model(others))
				.register(PlayerDetectorBlock.PlayerDetectorType.YOU, model(you))
		)
	}
	static TriFunction<Identifier, Identifier, Identifier, StateModel> RUBBER_LOG = (Identifier vertical, Identifier horizontal, Identifier with_sap) -> {
		BlockStateVariantMap.TripleProperty<WeightedVariant, Direction.Axis, Direction, Boolean> map = BlockStateVariantMap
			.models(Properties.AXIS, Properties.HORIZONTAL_FACING, BlockRubberLog.HAS_SAP)
		for (Direction.Axis axis : Direction.Axis.VALUES) {
			for (Direction direction : Direction.Type.HORIZONTAL) {
				for (boolean has_sap : [false, true]) {
					WeightedVariant variant
					if (axis == Direction.Axis.Y) {
						variant = model(has_sap ? with_sap : vertical)
						if (has_sap) {
							switch (direction) {
								case Direction.EAST:
									variant = variant.apply(ROTATE_Y_90)
									break
								case Direction.SOUTH:
									variant = variant.apply(ROTATE_Y_180)
									break
								case Direction.WEST:
									variant = variant.apply(ROTATE_Y_270)
									break
							}
						}
					} else {
						variant = model(horizontal).apply(ROTATE_X_90)
						if (axis == Direction.Axis.X) {
							variant = variant.apply(ROTATE_Y_90)
						}
					}
					map.register(axis, direction, has_sap, variant)
				}
			}
		}
		return new StateModel().add(map)
	}
	static BiFunction<Identifier, Identifier, StateModel> CABLE = (Identifier core, Identifier side) -> {
		new StateModel().multipart().add(model(core))
			.add(when().put(Properties.NORTH, true), model(side))
			.add(
				when().put(Properties.EAST, true),
				model(side).apply(ROTATE_Y_90)
			).add(
				when().put(Properties.SOUTH, true),
				model(side).apply(ROTATE_Y_180)
			).add(
				when().put(Properties.WEST, true),
				model(side).apply(ROTATE_Y_270)
			).add(
				when().put(Properties.UP, true),
				model(side).apply(ROTATE_X_270)
			).add(
				when().put(Properties.DOWN, true),
				model(side).apply(ROTATE_X_90)
			)
	}
	static MultipartModelConditionBuilder when() {
		return new MultipartModelConditionBuilder();
	}
	static WeightedVariant model(Identifier id) {
		return new WeightedVariant(Pool.of(new ModelVariant(id)));
	}
	static WeightedVariant model(Block block) {
		return model(TextureMap.getId(block))
	}
}
