/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.fml.common.IWorldGenerator;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.TechReborn;
import techreborn.init.ModFluids;

import java.util.Random;

/**
 * Created by modmuss50 on 13/06/2017.
 */

@RebornRegister(modID = TechReborn.MOD_ID)
public class OilLakeGenerator implements IWorldGenerator {

	@ConfigRegistry(config = "world", category = "oil_lakes", comment = "Enable the generation of underground oil lakes")
	public static boolean enable = true;

	@ConfigRegistry(config = "world", category = "oil_lakes", comment = "Max height of underground lakes")
	public static int maxHeight = 48;

	@ConfigRegistry(config = "world", category = "oil_lakes", comment = "The chance to spawn in a chunk, smaller is more common")
	public static int rareity = 30;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (!enable) {
			return;
		}
		if (!world.provider.isSurfaceWorld()) {
			return;
		}
		if (random.nextInt(rareity) != 0) {
			return;
		}
		int y = random.nextInt(maxHeight);
		new WorldGenLakes(ModFluids.BLOCK_OIL).generate(world, random, new BlockPos((chunkX * 16) + 8, y, (chunkZ * 16) + 8));
	}
}
