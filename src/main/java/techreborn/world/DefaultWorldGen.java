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

package techreborn.world;

import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockStateMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import org.apache.logging.log4j.util.TriConsumer;
import techreborn.blocks.misc.BlockRubberLog;
import techreborn.init.TRContent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class DefaultWorldGen {

	private static final RuleTest END_STONE = new BlockStateMatchRuleTest(Blocks.END_STONE.getDefaultState());

	private static ConfiguredFeature<?, ?> getRubberTree() {
		DataPool.Builder<BlockState> logPoolBuilder = DataPool.<BlockState>builder()
				.add(TRContent.RUBBER_LOG.getDefaultState(), 10);

		Arrays.stream(Direction.values())
				.filter(direction -> direction.getAxis().isHorizontal())
				.map(direction -> TRContent.RUBBER_LOG.getDefaultState()
						.with(BlockRubberLog.HAS_SAP, true)
						.with(BlockRubberLog.SAP_SIDE, direction)
				)
				.forEach(state -> logPoolBuilder.add(state, 1));

		BlockStateProvider logProvider = new WeightedBlockStateProvider(logPoolBuilder.build());


		TreeFeatureConfig treeFeatureConfig = new TreeFeatureConfig.Builder(
			logProvider,
			new StraightTrunkPlacer(6, 3, 0),
			new SimpleBlockStateProvider(TRContent.RUBBER_LEAVES.getDefaultState()),
			new SimpleBlockStateProvider(TRContent.RUBBER_SAPLING.getDefaultState()),
			new RubberTreeFeature.FoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3, 3, TRContent.RUBBER_LEAVES.getDefaultState()),
			new TwoLayersFeatureSize(1, 0, 1)
		).build();

		return WorldGenerator.RUBBER_TREE_FEATURE.configure(treeFeatureConfig)
				.decorate(WorldGenerator.RUBBER_TREE_DECORATOR
						.configure(new ChanceDecoratorConfig(50)
				));
	}

	public static List<DataDrivenFeature> getDefaultFeatures() {
		List<DataDrivenFeature> features = new ArrayList<>();
		TriConsumer<Predicate<BiomeSelectionContext>, RuleTest, TRContent.Ores> addOre = (worldTargetType, ruleTest, ore) ->
				features.add(ore.asNewOres(new Identifier("techreborn", Registry.BLOCK.getId(ore.block).getPath()), worldTargetType, ruleTest));

		addOre.accept(BiomeSelectors.foundInTheNether(), OreFeatureConfig.Rules.BASE_STONE_NETHER, TRContent.Ores.CINNABAR);
		addOre.accept(BiomeSelectors.foundInTheNether(), OreFeatureConfig.Rules.BASE_STONE_NETHER, TRContent.Ores.PYRITE);
		addOre.accept(BiomeSelectors.foundInTheNether(), OreFeatureConfig.Rules.BASE_STONE_NETHER, TRContent.Ores.SPHALERITE);

		addOre.accept(BiomeSelectors.foundInTheEnd(), END_STONE, TRContent.Ores.PERIDOT);
		addOre.accept(BiomeSelectors.foundInTheEnd(), END_STONE, TRContent.Ores.SHELDONITE);
		addOre.accept(BiomeSelectors.foundInTheEnd(), END_STONE, TRContent.Ores.SODALITE);
		addOre.accept(BiomeSelectors.foundInTheEnd(), END_STONE, TRContent.Ores.TUNGSTEN);

		addOre.accept(BiomeSelectors.foundInOverworld(), OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.BAUXITE);
		addOre.accept(BiomeSelectors.foundInOverworld(), OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.GALENA);
		addOre.accept(BiomeSelectors.foundInOverworld(), OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.IRIDIUM);
		addOre.accept(BiomeSelectors.foundInOverworld(), OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.LEAD);
		addOre.accept(BiomeSelectors.foundInOverworld(), OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.RUBY);
		addOre.accept(BiomeSelectors.foundInOverworld(), OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.SAPPHIRE);
		addOre.accept(BiomeSelectors.foundInOverworld(), OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.SILVER);
		addOre.accept(BiomeSelectors.foundInOverworld(), OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.TIN);


		features.add(new DataDrivenFeature(
			RubberSaplingGenerator.IDENTIFIER,
			BiomeSelectors.categories(Biome.Category.FOREST, Biome.Category.TAIGA, Biome.Category.SWAMP),
			getRubberTree(),
			GenerationStep.Feature.VEGETAL_DECORATION
		));

		return features;
	}

	// Used to export the worldgen jsons
	public static void export() {
		for (DataDrivenFeature defaultFeature : getDefaultFeatures()) {
			JsonElement jsonElement = defaultFeature.serialise();
			String json = jsonElement.toString();

			Path dir = Paths.get("..\\src\\main\\resources\\data\\techreborn\\techreborn\\features");
			try {
				Files.writeString(dir.resolve(defaultFeature.getIdentifier().getPath() + ".json"), json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
