package techreborn.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class TechRebornOre {
	public static final Codec<TechRebornOre> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					WorldTargetType.CODEC.fieldOf("target").forGetter(TechRebornOre::getTargetType),
					RuleTest.field_25012.fieldOf("rule").forGetter(TechRebornOre::getRuleTest),
					BlockState.CODEC.fieldOf("blockState").forGetter(TechRebornOre::getBlockState),
					Codec.INT.fieldOf("maxY").forGetter(TechRebornOre::getMaxY),
					Codec.INT.fieldOf("veinSize").forGetter(TechRebornOre::getVeinCount),
					Codec.INT.fieldOf("veinCount").forGetter(TechRebornOre::getVeinCount)
			).apply(instance, TechRebornOre::new)
	);

	private final WorldTargetType targetType;
	private final RuleTest ruleTest;
	private final BlockState blockState;
	private final int maxY;
	private final int veinSize;
	private final int veinCount;
	private final ConfiguredFeature<?, ?> configuredFeature;

	public TechRebornOre(WorldTargetType targetType, RuleTest ruleTest, BlockState blockState, int maxY, int veinSize, int veinCount) {
		this.targetType = targetType;
		this.ruleTest = ruleTest;
		this.blockState = blockState;
		this.maxY = maxY;
		this.veinSize = veinSize;
		this.veinCount = veinCount;
		this.configuredFeature = Feature.ORE.configure(
									new OreFeatureConfig(ruleTest, blockState, veinSize)
								)
								.method_30377(maxY)
								.spreadHorizontally()
								.repeat(veinCount);
	}

	public ConfiguredFeature<?, ?> getConfiguredFeature() {
		return configuredFeature;
	}

	public Identifier getIdentifier() {
		return new Identifier("techreborn", "ore_" + Registry.BLOCK.getId(blockState.getBlock()).toString().replace(":", "_"));
	}

	public WorldTargetType getTargetType() {
		return targetType;
	}

	public RuleTest getRuleTest() {
		return ruleTest;
	}

	public BlockState getBlockState() {
		return blockState;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getVeinSize() {
		return veinSize;
	}

	public int getVeinCount() {
		return veinCount;
	}
}
