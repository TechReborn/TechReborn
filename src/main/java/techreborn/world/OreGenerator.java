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

import java.util.function.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraftforge.registries.ForgeRegistries;
import techreborn.init.TRContent;

/**
 * @author drcrazy
 *
 */
public class OreGenerator {
	
	public static final Predicate<IBlockState> IS_NETHERRACK = (state) -> { return state != null && (state.getBlock() == Blocks.NETHERRACK); };
	public static final Predicate<IBlockState> IS_ENDSTONE = (state) -> { return state != null && (state.getBlock() == Blocks.END_STONE); };
	
	public static void init() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			if (biome.getCategory() == Category.NETHER) {
				addOre(biome, IS_NETHERRACK, TRContent.Ores.CINNABAR);
				addOre(biome, IS_NETHERRACK, TRContent.Ores.PYRITE);
				addOre(biome, IS_NETHERRACK, TRContent.Ores.SPHALERITE);

			} else if (biome.getCategory() == Category.THEEND) {
				addOre(biome, IS_ENDSTONE, TRContent.Ores.PERIDOT);
				addOre(biome, IS_ENDSTONE, TRContent.Ores.SHELDONITE);
				addOre(biome, IS_ENDSTONE, TRContent.Ores.SODALITE);
				addOre(biome, IS_ENDSTONE, TRContent.Ores.TUNGSTEN);
			} else {
				addOre(biome, MinableConfig.IS_ROCK, TRContent.Ores.BAUXITE);
				addOre(biome, MinableConfig.IS_ROCK, TRContent.Ores.COPPER);
				addOre(biome, MinableConfig.IS_ROCK, TRContent.Ores.GALENA);
				addOre(biome, MinableConfig.IS_ROCK, TRContent.Ores.IRIDIUM);
				addOre(biome, MinableConfig.IS_ROCK, TRContent.Ores.LEAD);
				addOre(biome, MinableConfig.IS_ROCK, TRContent.Ores.RUBY);
				addOre(biome, MinableConfig.IS_ROCK, TRContent.Ores.SAPPHIRE);
				addOre(biome, MinableConfig.IS_ROCK, TRContent.Ores.SILVER);
				addOre(biome, MinableConfig.IS_ROCK, TRContent.Ores.TIN);
			}
		}
	}
	
	private static void addOre(Biome biome, Predicate<IBlockState> canReplaceIn, TRContent.Ores ore) {
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
				Biome.createCompositeFeature(Feature.MINABLE,
						new MinableConfig(canReplaceIn, ore.block.getDefaultState(), ore.veinSize), Biome.COUNT_RANGE,
						new CountRangeConfig(ore.veinsPerChunk, ore.minY, ore.minY, ore.maxY)));
	}
}
