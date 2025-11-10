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

import net.minecraft.world.level.block.Block
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.renderer.block.model.Variant
import net.minecraft.client.renderer.block.model.VariantMutator
import net.minecraft.client.data.models.blockstates.ConditionBuilder
import net.minecraft.client.data.models.MultiVariant
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.random.WeightedList
import net.minecraft.core.Direction
import org.apache.commons.lang3.function.TriFunction
import org.apache.commons.lang3.tuple.Pair
import reborncore.common.blocks.BlockMachineBase
import techreborn.blocks.machine.tier1.PlayerDetectorBlock
import techreborn.blocks.machine.tier1.ResinBasinBlock
import techreborn.blocks.misc.BlockRubberLog

import java.util.function.BiFunction
import java.util.function.Function

import static net.minecraft.client.data.models.BlockModelGenerators.NOP;
import static net.minecraft.client.data.models.BlockModelGenerators.X_ROT_90;
import static net.minecraft.client.data.models.BlockModelGenerators.X_ROT_180;
import static net.minecraft.client.data.models.BlockModelGenerators.X_ROT_270;
import static net.minecraft.client.data.models.BlockModelGenerators.Y_ROT_90;
import static net.minecraft.client.data.models.BlockModelGenerators.Y_ROT_180;
import static net.minecraft.client.data.models.BlockModelGenerators.Y_ROT_270;

class TemplateState {
	static PropertyDispatch<VariantMutator> NORTH_DEFAULT_FACING = PropertyDispatch.modify(BlockStateProperties.FACING)
		.select(Direction.DOWN, X_ROT_90)
		.select(Direction.UP, X_ROT_270)
		.select(Direction.NORTH, NOP)
		.select(Direction.SOUTH, Y_ROT_180)
		.select(Direction.WEST, Y_ROT_270)
		.select(Direction.EAST, Y_ROT_90);
	static PropertyDispatch<VariantMutator> NORTH_DEFAULT_H_FACING = PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
		.select(Direction.EAST, Y_ROT_90)
		.select(Direction.SOUTH, Y_ROT_180)
		.select(Direction.WEST, Y_ROT_270)
		.select(Direction.NORTH, NOP);
	static PropertyDispatch<VariantMutator> UP_DEFAULT_FACING = PropertyDispatch.modify(BlockStateProperties.FACING)
		.select(Direction.DOWN, X_ROT_180)
		.select(Direction.UP, NOP)
		.select(Direction.NORTH, X_ROT_90)
		.select(Direction.SOUTH, X_ROT_270)
		.select(Direction.WEST, X_ROT_90.then(Y_ROT_270))
		.select(Direction.EAST, X_ROT_90.then(Y_ROT_90))

	@FunctionalInterface
	interface Uploadable {
		ResourceLocation upload(Block block);
	}
	static Uploadable SINGLE = (Block block) -> new StateModel().add(model(block)).upload(block)
	static Uploadable SINGLE_NORTH_DEFAULT_FACING = (Block block) -> new StateModel().add(model(block)).add(NORTH_DEFAULT_FACING).upload(block)
	static Uploadable SINGLE_NORTH_DEFAULT_H_FACING = (Block block) -> new StateModel().add(model(block)).add(NORTH_DEFAULT_H_FACING).upload(block)
	static Function<Pair<ResourceLocation, ResourceLocation>, StateModel> ACTIVE = (Pair<ResourceLocation, ResourceLocation> pair) -> new StateModel().add(
		PropertyDispatch.initial(BlockMachineBase.ACTIVE).select(false, model(pair.left)).select(true, model(pair.right))
	)
	static Function<Pair<ResourceLocation, ResourceLocation>, StateModel> ACTIVE_NORTH_DEFAULT_H_FACING = (Pair<ResourceLocation, ResourceLocation> pair) -> {
		ACTIVE.apply(pair).add(NORTH_DEFAULT_H_FACING)
	}
	static Function<Pair<ResourceLocation, ResourceLocation>, StateModel> ACTIVE_UP_DEFAULT_FACING = (Pair<ResourceLocation, ResourceLocation> pair) -> {
		ACTIVE.apply(pair).add(UP_DEFAULT_FACING)
	}
	static TriFunction<ResourceLocation, ResourceLocation, ResourceLocation, StateModel> RESIN_BASIN = (ResourceLocation empty, ResourceLocation flowing, ResourceLocation full) -> {
		new StateModel().add(
			PropertyDispatch.initial(ResinBasinBlock.POURING, ResinBasinBlock.FULL)
				.select(false, false, model(empty))
				.select(true, false, model(flowing))
				.select(false, true, model(full))
				.select(true, true, model(full))
		).add(NORTH_DEFAULT_H_FACING)
	}
	static TriFunction<ResourceLocation, ResourceLocation, ResourceLocation, StateModel> PLAYER_DETECTOR = (ResourceLocation all, ResourceLocation others, ResourceLocation you) -> {
		new StateModel().add(
			PropertyDispatch.initial(PlayerDetectorBlock.TYPE)
				.select(PlayerDetectorBlock.PlayerDetectorType.ALL, model(all))
				.select(PlayerDetectorBlock.PlayerDetectorType.OTHERS, model(others))
				.select(PlayerDetectorBlock.PlayerDetectorType.YOU, model(you))
		)
	}
	static TriFunction<ResourceLocation, ResourceLocation, ResourceLocation, StateModel> RUBBER_LOG = (ResourceLocation vertical, ResourceLocation horizontal, ResourceLocation with_sap) -> {
		PropertyDispatch.C3<MultiVariant, Direction.Axis, Direction, Boolean> map = PropertyDispatch
			.initial(BlockStateProperties.AXIS, BlockStateProperties.HORIZONTAL_FACING, BlockRubberLog.HAS_SAP)
		for (Direction.Axis axis : Direction.Axis.VALUES) {
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				for (boolean has_sap : [false, true]) {
					MultiVariant variant
					if (axis == Direction.Axis.Y) {
						variant = model(has_sap ? with_sap : vertical)
						if (has_sap) {
							switch (direction) {
								case Direction.EAST:
									variant = variant.with(Y_ROT_90)
									break
								case Direction.SOUTH:
									variant = variant.with(Y_ROT_180)
									break
								case Direction.WEST:
									variant = variant.with(Y_ROT_270)
									break
							}
						}
					} else {
						variant = model(horizontal).with(X_ROT_90)
						if (axis == Direction.Axis.X) {
							variant = variant.with(Y_ROT_90)
						}
					}
					map.select(axis, direction, has_sap, variant)
				}
			}
		}
		return new StateModel().add(map)
	}
	static BiFunction<ResourceLocation, ResourceLocation, StateModel> CABLE = (ResourceLocation core, ResourceLocation side) -> {
		new StateModel().multipart().add(model(core))
			.add(when().term(BlockStateProperties.NORTH, true), model(side))
			.add(
				when().term(BlockStateProperties.EAST, true),
				model(side).with(Y_ROT_90)
			).add(
				when().term(BlockStateProperties.SOUTH, true),
				model(side).with(Y_ROT_180)
			).add(
				when().term(BlockStateProperties.WEST, true),
				model(side).with(Y_ROT_270)
			).add(
				when().term(BlockStateProperties.UP, true),
				model(side).with(X_ROT_270)
			).add(
				when().term(BlockStateProperties.DOWN, true),
				model(side).with(X_ROT_90)
			)
	}
	static ConditionBuilder when() {
		return new ConditionBuilder();
	}
	static MultiVariant model(ResourceLocation id) {
		return new MultiVariant(WeightedList.of(new Variant(id)));
	}
	static MultiVariant model(Block block) {
		return model(TextureMapping.getBlockTexture(block))
	}
}
