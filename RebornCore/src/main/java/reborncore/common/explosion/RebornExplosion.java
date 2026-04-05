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

package reborncore.common.explosion;

import org.apache.commons.lang3.time.StopWatch;
import org.jspecify.annotations.Nullable;
import reborncore.RebornCore;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created by modmuss50 on 12/03/2016.
 */
public class RebornExplosion extends ServerExplosion {
	final BlockPos center;
	final int radius;

	@Nullable
	LivingEntity livingBase;

	public RebornExplosion(BlockPos center, ServerLevel world, int radius) {
		super(world, null, null, null, center.getCenter(), radius, false, BlockInteraction.DESTROY);
		this.center = center;
		this.radius = radius;
	}

	public void setLivingBase(@Nullable LivingEntity livingBase) {
		this.livingBase = livingBase;
	}

	@Nullable
	public LivingEntity getLivingBase() {
		return livingBase;
	}

	@Override
	public int explode() {
		StopWatch watch = new StopWatch();
		watch.start();
		int i = 0;
		for (int tx = -radius; tx < radius + 1; tx++) {
			for (int ty = -radius; ty < radius + 1; ty++) {
				for (int tz = -radius; tz < radius + 1; tz++) {
					if (Math.sqrt(Math.pow(tx, 2) + Math.pow(ty, 2) + Math.pow(tz, 2)) <= radius - 2) {
						BlockPos pos = center.offset(tx, ty, tz);
						BlockState state = level().getBlockState(pos);
						Block block = state.getBlock();
						if (block != Blocks.BEDROCK && !state.isAir()) {
							block.wasExploded(level(), pos, this);
							level().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
							i++;
						}
					}
				}
			}
		}
		RebornCore.LOGGER.info("The explosion took" + watch + " to explode");
		return i;
	}

	@Nullable
	@Override
	public LivingEntity getIndirectSourceEntity() {
		return livingBase;
	}

	@Override
	public List<BlockPos> calculateExplodedPositions() {
		List<BlockPos> poses = new ArrayList<>();
		for (int tx = -radius; tx < radius + 1; tx++) {
			for (int ty = -radius; ty < radius + 1; ty++) {
				for (int tz = -radius; tz < radius + 1; tz++) {
					if (Math.sqrt(Math.pow(tx, 2) + Math.pow(ty, 2) + Math.pow(tz, 2)) <= radius - 2) {
						BlockPos pos = center.offset(tx, ty, tz);
						BlockState state = level().getBlockState(pos);
						Block block = state.getBlock();
						if (block != Blocks.BEDROCK && !state.isAir()) {
							poses.add(pos);
						}
					}
				}
			}
		}
		return poses;
	}
}
