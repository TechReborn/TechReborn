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

package techreborn.blocks.cable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class CableShapeUtil {

	private final CableBlock cableBlock;
	private final HashMap<BlockState, VoxelShape> shapes;

	public CableShapeUtil(CableBlock cableBlock) {
		this.cableBlock = cableBlock;
		this.shapes = createStateShapeMap();
	}

	private HashMap<BlockState, VoxelShape> createStateShapeMap() {
		return Util.make(new HashMap<>(), map -> cableBlock.getStateManager().getStates()
				.forEach(state -> map.put(state, getStateShape(state)))
		);
	}

	private VoxelShape getStateShape(BlockState state) {
		final double size = cableBlock.type != null ? cableBlock.type.cableThickness : 6;
		final VoxelShape baseShape = Block.createCuboidShape(size, size, size, 16.0D - size, 16.0D - size, 16.0D - size);

		final List<VoxelShape> connections = new ArrayList<>();
		for (Direction dir : Direction.values()) {
			if (state.get(CableBlock.PROPERTY_MAP.get(dir))) {
				double x = dir == Direction.WEST ? 0 : dir == Direction.EAST ? 16D : size;
				double z = dir == Direction.NORTH ? 0 : dir == Direction.SOUTH ? 16D : size;
				double y = dir == Direction.DOWN ? 0 : dir == Direction.UP ? 16D : size;

				VoxelShape shape = Block.createCuboidShape(x, y, z, 16.0D - size, 16.0D - size, 16.0D - size);
				connections.add(shape);
			}
		}
		return VoxelShapes.union(baseShape, connections.toArray(new VoxelShape[]{}));
	}

	public VoxelShape getShape(BlockState state) {
		return shapes.get(state);
	}

}
