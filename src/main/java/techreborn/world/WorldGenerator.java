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

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountExtraChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.Target;
import techreborn.init.TRContent;

/**
 * @author drcrazy
 *
 */
public class WorldGenerator {

//	public static final Predicate<IBlockState> IS_ENDSTONE = (state) -> {
//		return state != null && (state.getBlock() == Blocks.END_STONE);
//	};

	public static void initBiomeFeatures() {
		for (Biome biome : Registry.BIOME) {
			if (biome.getCategory() == Category.NETHER) {
				addOre(biome, OreFeatureConfig.Target.NETHERRACK, TRContent.Ores.CINNABAR);
				addOre(biome, OreFeatureConfig.Target.NETHERRACK, TRContent.Ores.PYRITE);
				addOre(biome, OreFeatureConfig.Target.NETHERRACK, TRContent.Ores.SPHALERITE);

			} else if (biome.getCategory() == Category.THEEND) {
				// addOre(biome, IS_ENDSTONE, TRContent.Ores.PERIDOT);
				// addOre(biome, IS_ENDSTONE, TRContent.Ores.SHELDONITE);
				// addOre(biome, IS_ENDSTONE, TRContent.Ores.SODALITE);
				// addOre(biome, IS_ENDSTONE, TRContent.Ores.TUNGSTEN);
			} else {
				addOre(biome, OreFeatureConfig.Target.NATURAL_STONE, TRContent.Ores.BAUXITE);
				addOre(biome, OreFeatureConfig.Target.NATURAL_STONE, TRContent.Ores.COPPER);
				addOre(biome, OreFeatureConfig.Target.NATURAL_STONE, TRContent.Ores.GALENA);
				addOre(biome, OreFeatureConfig.Target.NATURAL_STONE, TRContent.Ores.IRIDIUM);
				addOre(biome, OreFeatureConfig.Target.NATURAL_STONE, TRContent.Ores.LEAD);
				addOre(biome, OreFeatureConfig.Target.NATURAL_STONE, TRContent.Ores.RUBY);
				addOre(biome, OreFeatureConfig.Target.NATURAL_STONE, TRContent.Ores.SAPPHIRE);
				addOre(biome, OreFeatureConfig.Target.NATURAL_STONE, TRContent.Ores.SILVER);
				addOre(biome, OreFeatureConfig.Target.NATURAL_STONE, TRContent.Ores.TIN);
				if (biome.getCategory() == Category.FOREST || biome.getCategory() == Category.TAIGA) {
					biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
							Biome.configureFeature(new RubberTreeFeature(DefaultFeatureConfig::deserialize, false),
									FeatureConfig.DEFAULT, Decorator.COUNT_EXTRA_HEIGHTMAP,
									new CountExtraChanceDecoratorConfig(10, 0.1F, 1)));
				}
			}
		}
	}

	private static void addOre(Biome biome, Target canReplaceIn, TRContent.Ores ore) {
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Biome.configureFeature(Feature.ORE,
				new OreFeatureConfig(canReplaceIn, ore.block.getDefaultState(), ore.veinSize), Decorator.COUNT_RANGE,
				new RangeDecoratorConfig(ore.veinsPerChunk, ore.minY, ore.minY, ore.maxY)));
	}
}