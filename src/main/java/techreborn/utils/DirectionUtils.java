/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2025 TechReborn
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

package techreborn.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Map;
import java.util.function.Predicate;

public class DirectionUtils {
	public enum HORIZONTAL_PART {
		ALONE,
		START,
		MIDDLE,
		END,
	}

	private static final byte FLAG_NORTH = 1;
	private static final byte FLAG_SOUTH = 1 << 1;
	private static final byte FLAG_WEST = 1 << 2;
	private static final byte FLAG_EAST = 1 << 3;

	public static final Vec3i[] POSITIONS = new Vec3i[]{
		new Vec3i(0, 0, -1),
		new Vec3i(0, 0, 1),
		new Vec3i(-1, 0, 0),
		new Vec3i(1, 0, 0),
		new Vec3i(0, -1, 0),
		new Vec3i(0, 1, 0),
	};
	public static final int HORIZONTAL_LENGTH = 4;
	public static final int ALL_LENGTH = POSITIONS.length;
	public static final int[] FLAGS = new int[ALL_LENGTH];
	public static final int[] OPP_FLAGS = new int[ALL_LENGTH];
	static {
		for (int i = 0; i < ALL_LENGTH; i++) {
			FLAGS[i] = 1 << i;
			if (i % 2 == 1) {
				OPP_FLAGS[i] = FLAGS[i - 1];
				OPP_FLAGS[i - 1] = FLAGS[i];
			}
		}
	}
	public static final Map<Direction, byte[]> HORIZONTAL_MAP = Map.of(
		Direction.NORTH, new byte[]{FLAG_WEST, FLAG_WEST | FLAG_EAST, FLAG_EAST},
		Direction.SOUTH, new byte[]{FLAG_EAST, FLAG_WEST | FLAG_EAST, FLAG_WEST},
		Direction.WEST, new byte[]{FLAG_SOUTH, FLAG_SOUTH | FLAG_NORTH, FLAG_NORTH},
		Direction.EAST, new byte[]{FLAG_NORTH, FLAG_SOUTH | FLAG_NORTH, FLAG_SOUTH}
	);
	public static final IntProperty HORIZONTAL_NEIGHBORS = IntProperty.of(
		"neighbors", 0, FLAG_NORTH | FLAG_SOUTH | FLAG_WEST | FLAG_EAST
	);

	private static int addNeighbor(World world, BlockPos pos, IntProperty property, int length, Predicate<Block> predicate) {
		int neighbors = 0;
		BlockPos.Mutable neighborPos = new BlockPos.Mutable();
		for (int i = 0; i < length; i++) {
			neighborPos.set(pos, POSITIONS[i]);
			BlockState neighborState = world.getBlockState(neighborPos);
			if (predicate.test(neighborState.getBlock())) {
				int flags = neighborState.get(property) | OPP_FLAGS[i];
				world.setBlockState(neighborPos, neighborState.with(property, flags));
				neighbors |= FLAGS[i];
			}
		}
		return neighbors;
	}

	private static void removeNeighbor(World world, BlockPos pos, BlockState state, IntProperty property, int length, Predicate<Block> predicate) {
		int neighbors = state.get(property);
		if (neighbors != 0) {
			BlockPos.Mutable neighborPos = new BlockPos.Mutable();
			for (int i = 0; i < length; i++) {
				if ((neighbors & FLAGS[i]) != 0) {
					neighborPos.set(pos, POSITIONS[i]);
					BlockState neighborState = world.getBlockState(neighborPos);
					if (predicate.test(neighborState.getBlock())) {
						int flags = neighborState.get(property) & ~OPP_FLAGS[i];
						world.setBlockState(neighborPos, neighborState.with(property, flags));
					}
				}
			}
		}
	}

	private static void loadNeighbors(World world, BlockPos pos, BlockState state, IntProperty property, int length, Predicate<Block> predicate) {
		int neighbors = 0;
		BlockPos.Mutable neighborPos = new BlockPos.Mutable();
		for (int i = 0; i < length; i++) {
			neighborPos.set(pos, POSITIONS[i]);
			if (predicate.test(world.getBlockState(neighborPos).getBlock())) {
				neighbors |= FLAGS[i];
			}
		}
		world.setBlockState(pos, state.with(property, neighbors));
	}

	public static void addHorizontalNeighbor(World world, BlockPos pos, BlockState state, Predicate<Block> predicate) {
		world.setBlockState(pos, state.with(HORIZONTAL_NEIGHBORS, addNeighbor(world, pos, HORIZONTAL_NEIGHBORS, HORIZONTAL_LENGTH, predicate)));
	}

	public static void removeHorizontalNeighbor(World world, BlockPos pos, BlockState state, Predicate<Block> predicate) {
		removeNeighbor(world, pos, state, HORIZONTAL_NEIGHBORS, HORIZONTAL_LENGTH, predicate);
	}

	public static void loadHorizontalNeighbors(World world, BlockPos pos, BlockState state, Predicate<Block> predicate) {
		loadNeighbors(world, pos, state, HORIZONTAL_NEIGHBORS, HORIZONTAL_LENGTH, predicate);
	}

	public static HORIZONTAL_PART getHorizontalPart(Direction direction, int neighbors) {
		byte[] flags = HORIZONTAL_MAP.get(direction);
		if ((neighbors & flags[1]) == flags[1]) return HORIZONTAL_PART.MIDDLE;
		if ((neighbors & flags[0]) == flags[0]) return HORIZONTAL_PART.START;
		if ((neighbors & flags[2]) == flags[2]) return HORIZONTAL_PART.END;
		return HORIZONTAL_PART.ALONE;
	}
}
