package techreborn.world;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockStateMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.world.gen.feature.OreConfiguredFeatures;

import java.util.function.Predicate;

public enum TargetDimension {
	OVERWORLD(BiomeSelectors.foundInOverworld(), OreConfiguredFeatures.BASE_STONE_OVERWORLD),
	NETHER(BiomeSelectors.foundInTheNether(), OreConfiguredFeatures.BASE_STONE_NETHER),
	END(BiomeSelectors.foundInTheEnd(), new BlockStateMatchRuleTest(Blocks.END_STONE.getDefaultState()));

	public final Predicate<BiomeSelectionContext> biomeSelector;
	public final RuleTest ruleTest;

	TargetDimension(Predicate<BiomeSelectionContext> biomeSelector, RuleTest ruleTest) {
		this.biomeSelector = biomeSelector;
		this.ruleTest = ruleTest;
	}
}
