/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import java.util.function.BiPredicate;

/**
 * Writes a multiblock for either verification or hologram rendering
 *
 * @author ramidzkh
 * @see MultiblockVerifier
 */
public interface MultiblockWriter {

	/**
	 * Adds a block to the multiblock
	 *
	 * @param x         {@code int} X
	 * @param y         {@code int} Y
	 * @param z         {@code int} Z
	 * @param predicate {@link BiPredicate} Predicate of the position
	 * @param state     {@link BlockState} The state for the hologram
	 * @return {@link MultiblockWriter} This. Useful for chaining
	 */
	MultiblockWriter add(int x, int y, int z, BiPredicate<BlockView, BlockPos> predicate, BlockState state);

	/**
	 * Fills a section between (ax, ay, az) to (bx, by, bz)
	 *
	 * @param ax        {@code int} X of the first point
	 * @param ay        {@code int} Y of the first point
	 * @param az        {@code int} Z of the first point
	 * @param bx        {@code int} X of the second point
	 * @param by        {@code int} X of the second point
	 * @param bz        {@code int} Z of the second point
	 * @param predicate {@link BiPredicate} Predicate of the position
	 * @param state     {@link BlockState} The state for the hologram
	 * @return {@link MultiblockWriter} This. Useful for chaining
	 */
	default MultiblockWriter fill(int ax, int ay, int az, int bx, int by, int bz, BiPredicate<BlockView, BlockPos> predicate, BlockState state) {
		for (int x = ax; x < bx; x++) {
			for (int y = ay; y < by; y++) {
				for (int z = az; z < bz; z++) {
					add(x, y, z, predicate, state);
				}
			}
		}

		return this;
	}

	/**
	 * Fills the outer ring of (0, 0, 0) to (pX, pY, pZ) through the axis, using the <code>predicate</code> and
	 * <code>state</code>. The inside of the ring uses <code>holePredicate</code> and <code>holeHologramState</code>
	 *
	 * @param through           {@link Direction.Axis} The axis to go through
	 * @param pX                {@code int} Size on the X axis
	 * @param pY                {@code int} Size on the Y axis
	 * @param pZ                {@code int} Size on the Z axis
	 * @param predicate         {@link BiPredicate} Predicate for the ring
	 * @param state             {@link BlockState} The state for the hologram
	 * @param holePredicate     {@link BiPredicate} Predicate for the hole
	 * @param holeHologramState {@link BlockState} The hole state for the hologram
	 * @return {@link MultiblockWriter} This. Useful for chaining
	 */
	default MultiblockWriter ring(Direction.Axis through, int pX, int pY, int pZ, BiPredicate<BlockView, BlockPos> predicate, BlockState state, BiPredicate<BlockView, BlockPos> holePredicate, BlockState holeHologramState) {
		if (holePredicate == null) {
			holePredicate = predicate.negate();
		}

		if (holeHologramState == null) {
			holeHologramState = Blocks.AIR.getDefaultState();
		}

		if (through == Direction.Axis.X) {
			for (int y = 0; y < pY; y++) {
				for (int z = 0; z < pZ; z++) {
					if ((y == 0 || y == (pY - 1)) || (z == 0 || z == (pZ - 1))) {
						add(pX, y, z, predicate, state);
					} else {
						add(pX, y, z, holePredicate, holeHologramState);
					}
				}
			}
		} else if (through == Direction.Axis.Y) {
			for (int x = 0; x < pX; x++) {
				for (int z = 0; z < pZ; z++) {
					if ((x == 0 || x == (pX - 1)) || (z == 0 || z == (pZ - 1))) {
						add(x, pY, z, predicate, state);
					} else {
						add(x, pY, z, holePredicate, holeHologramState);
					}
				}
			}
		} else if (through == Direction.Axis.Z) {
			for (int x = 0; x < pX; x++) {
				for (int y = 0; y < pY; y++) {
					if ((x == 0 || x == (pX - 1)) || (y == 0 || y == (pY - 1))) {
						add(x, y, pZ, predicate, state);
					} else {
						add(x, y, pZ, holePredicate, holeHologramState);
					}
				}
			}
		}

		return this;
	}

	default MultiblockWriter ringWithAir(Direction.Axis through, int x, int y, int z, BiPredicate<BlockView, BlockPos> predicate, BlockState state) {
		return ring(through, x, y, z, predicate, state, (view, pos) -> view.getBlockState(pos).getBlock() == Blocks.AIR, Blocks.AIR.getDefaultState());
	}

	default MultiblockWriter add(int x, int y, int z, BlockState state) {
		return this.add(x, y, z, (view, pos) -> view.getBlockState(pos) == state, state);
	}

	default MultiblockWriter fill(int ax, int ay, int az, int bx, int by, int bz, BlockState state) {
		return fill(ax, ay, az, bx, by, bz, (view, pos) -> view.getBlockState(pos) == state, state);
	}

	default MultiblockWriter ring(Direction.Axis through, int x, int y, int z, BlockState state, BlockState holeState) {
		return ring(through, x, y, z, (view, pos) -> view.getBlockState(pos) == state, state, (view, pos) -> view.getBlockState(pos) == holeState, holeState);
	}

	default MultiblockWriter ringWithAir(Direction.Axis through, int x, int y, int z, BlockState state) {
		return ringWithAir(through, x, y, z, (view, pos) -> view.getBlockState(pos) == state, state);
	}

	default MultiblockWriter translate(int offsetX, int offsetY, int offsetZ) {
		return (x, y, z, predicate, state) -> add(offsetX + x, offsetY + y, offsetZ + z, predicate, state);
	}

	default MultiblockWriter rotate() {
		return (x, y, z, predicate, state) -> add(-z, y, x, predicate, state);
	}

	default MultiblockWriter rotate(Direction direction) {
		MultiblockWriter w = this;

		switch (direction) {
			case NORTH:
				w = w.rotate();
			case WEST:
				w = w.rotate();
			case SOUTH:
				w = w.rotate();
		}

		return w;
	}

	/**
	 * A writer which prints the hologram to {@link System#out}
	 */
	record DebugWriter(MultiblockWriter writer) implements MultiblockWriter {

		@Override
		public MultiblockWriter add(int x, int y, int z, BiPredicate<BlockView, BlockPos> predicate, BlockState state) {
			System.out.printf("\t%d\t%d\t%d\t%s\n", x, y, z, state.getBlock());

			if (writer != null) {
				writer.add(x, y, z, predicate, state);
			}

			return this;
		}
	}

	/**
	 * A writer which verifies the positions of each block
	 */
	class MultiblockVerifier implements MultiblockWriter {
		private final BlockPos relative;
		private final BlockView view;

		private boolean valid = true;

		public MultiblockVerifier(BlockPos relative, BlockView view) {
			this.relative = relative;
			this.view = view;
		}

		public boolean isValid() {
			return valid;
		}

		@Override
		public MultiblockWriter add(int x, int y, int z, BiPredicate<BlockView, BlockPos> predicate, BlockState state) {
			if (valid) {
				valid = predicate.test(view, relative.add(x, y, z));
			}

			return this;
		}
	}

}
