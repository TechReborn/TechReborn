package techreborn.world;

import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockStateMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.math.Direction;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import org.apache.logging.log4j.util.TriConsumer;
import techreborn.blocks.misc.BlockRubberLog;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefaultWorldGen {

	private static final RuleTest END_STONE = new BlockStateMatchRuleTest(Blocks.END_STONE.getDefaultState());

	public static WorldGenConfig getDefaultWorldGen() {
		return new WorldGenConfig(getOres(), getRubberTree());
	}

	private static ConfiguredFeature<?, ?> getRubberTree() {
		WeightedBlockStateProvider logProvider = new WeightedBlockStateProvider();
		logProvider.addState(TRContent.RUBBER_LOG.getDefaultState(), 10);

		Arrays.stream(Direction.values())
				.filter(direction -> direction.getAxis().isHorizontal())
				.map(direction -> TRContent.RUBBER_LOG.getDefaultState()
						.with(BlockRubberLog.HAS_SAP, true)
						.with(BlockRubberLog.SAP_SIDE, direction)
				)
				.forEach(state -> logProvider.addState(state, 1));

		TreeFeatureConfig treeFeatureConfig = new TreeFeatureConfig.Builder(
			logProvider,
			new SimpleBlockStateProvider(TRContent.RUBBER_LEAVES.getDefaultState()),
			new RubberTreeFeature.FoliagePlacer(UniformIntDistribution.of(2, 0), UniformIntDistribution.of(0, 0), 3, 3, TRContent.RUBBER_LEAVES.getDefaultState()),
			new StraightTrunkPlacer(6, 3, 0),
			new TwoLayersFeatureSize(1, 0, 1)
		).build();


		return WorldGenerator.RUBBER_TREE_FEATURE.configure(treeFeatureConfig)
				.decorate(WorldGenerator.RUBBER_TREE_DECORATOR
						.configure(new ChanceDecoratorConfig(50)
				));
	}

	private static List<TechRebornOre> getOres() {
		List<TechRebornOre> ores = new ArrayList<>();
		TriConsumer<WorldTargetType, RuleTest, TRContent.Ores> addOre = (worldTargetType, ruleTest, ore) -> ores.add(ore.asNewOres(worldTargetType, ruleTest));

		addOre.accept(WorldTargetType.NETHER, OreFeatureConfig.Rules.BASE_STONE_NETHER, TRContent.Ores.CINNABAR);
		addOre.accept(WorldTargetType.NETHER, OreFeatureConfig.Rules.BASE_STONE_NETHER, TRContent.Ores.PYRITE);
		addOre.accept(WorldTargetType.NETHER, OreFeatureConfig.Rules.BASE_STONE_NETHER, TRContent.Ores.SPHALERITE);

		addOre.accept(WorldTargetType.END, END_STONE, TRContent.Ores.PERIDOT);
		addOre.accept(WorldTargetType.END, END_STONE, TRContent.Ores.SHELDONITE);
		addOre.accept(WorldTargetType.END, END_STONE, TRContent.Ores.SODALITE);
		addOre.accept(WorldTargetType.END, END_STONE, TRContent.Ores.TUNGSTEN);

		addOre.accept(WorldTargetType.DEFAULT, OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.BAUXITE);
		addOre.accept(WorldTargetType.DEFAULT, OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.COPPER);
		addOre.accept(WorldTargetType.DEFAULT, OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.GALENA);
		addOre.accept(WorldTargetType.DEFAULT, OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.IRIDIUM);
		addOre.accept(WorldTargetType.DEFAULT, OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.LEAD);
		addOre.accept(WorldTargetType.DEFAULT, OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.RUBY);
		addOre.accept(WorldTargetType.DEFAULT, OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.SAPPHIRE);
		addOre.accept(WorldTargetType.DEFAULT, OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.SILVER);
		addOre.accept(WorldTargetType.DEFAULT, OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, TRContent.Ores.TIN);

		return Collections.unmodifiableList(ores);
	}

}
