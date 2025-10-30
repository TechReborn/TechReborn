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

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class CableShapeUtil {

	private static final Map<BlockState, VoxelShape> SHAPE_CACHE = new IdentityHashMap<>();

	private static VoxelShape getStateShape(BlockState state) {
		CableBlock cableBlock = (CableBlock) state.getBlock();

		final double size = cableBlock.type.cableThickness;
		final VoxelShape baseShape = Shapes.box(size, size, size, 1 - size, 1 - size, 1 - size);

		final List<VoxelShape> connections = new ArrayList<>();
		for (Direction dir : Direction.values()) {
			if (state.getValue(CableBlock.PROPERTY_MAP.get(dir))) {
				double[] mins = new double[] { size, size, size };
				double[] maxs = new double[] { 1 - size, 1 - size, 1 - size };
				int axis = dir.getAxis().ordinal();
				if (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
					maxs[axis] = 1;
				} else {
					mins[axis] = 0;
				}
				connections.add(Shapes.box(mins[0], mins[1], mins[2], maxs[0], maxs[1], maxs[2]));
			}
		}
		return Shapes.or(baseShape, connections.toArray(new VoxelShape[]{}));
	}

	public static VoxelShape getShape(BlockState state) {
		return SHAPE_CACHE.computeIfAbsent(state, CableShapeUtil::getStateShape);
	}

}
