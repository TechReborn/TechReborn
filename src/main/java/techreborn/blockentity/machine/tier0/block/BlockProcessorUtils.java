/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
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

package techreborn.blockentity.machine.tier0.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.common.blockentity.MachineBaseBlockEntity;

/**
 * <b>Common utilities for BlockProcessors</b>
 * <br>
 * Only exposes static methods
 *
 * @author SimonFlapse
 */
public class BlockProcessorUtils {
	private BlockProcessorUtils() {};

	/**
	 * <b>Get the hardness to break of a block</b>
	 * @param world {@link World} the world where the blockInFront is in
	 * @param blockInFront {@link BlockState} the block from which to get the hardness
	 * @param positionInFront {@link BlockPos} the position of the block from which to get the hardness
	 * @return the hardness to break of the supplied {@link BlockState}
	 */
	public static float getHardness(World world, BlockState blockInFront, BlockPos positionInFront) {
		float hardness = blockInFront.getHardness(world, positionInFront);
		return hardness;
	}


	/**
	 * <b>Get processing time adjusted to the hardness of a block and the overclocking speed</b>
	 * <br>
	 * Minimum value is 1 tick
	 * @param processable {@link BlockProcessable} the machine from which to get the speed multiplier of
	 * @param baseTickTime {@code int} the base processing time in ticks
	 * @param hardness {@code float} the hardness of the processed block
	 * @return time to process a block in ticks given the hardness, speed multiplier and base processing time
	 */
	public static int getProcessTimeWithHardness(BlockProcessable processable, int baseTickTime, float hardness) {
		int placeTime = (int) (baseTickTime * hardness * (1.0 - processable.getSpeedMultiplier()));
		return Math.max(placeTime, 1);
	}

	/**
	 * <b>Play a sound in a regular interval of 20 ticks</b>
	 * <br>
	 * Prevents spamming a new sound every tick, instead only allowing for a sound to be played every 20 tick.
	 * @param processable {@link BlockProcessable} the machine which should play the sound
	 * @param currentTick {@code int} the current tick of the processing
	 */
	public static void playSound(BlockProcessable processable, int currentTick) {
		if (currentTick == 1 || currentTick % 20 == 0) {
			processable.playSound();
		}
	}

	/**
	 * <b>Get the position of the block in front of the blockEntity</b>
	 * <br>
	 * @param blockEntity
	 * @param pos
	 * @return
	 */
	public static BlockPos getFrontBlockPosition(MachineBaseBlockEntity blockEntity, BlockPos pos) {
		return pos.offset(blockEntity.getFacing() == null? Direction.NORTH : blockEntity.getFacing());
	}
}
