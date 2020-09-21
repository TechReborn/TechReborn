package techreborn.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.List;

public class WorldGenConfig {
	public static final Codec<WorldGenConfig> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.list(TechRebornOre.CODEC).fieldOf("ores").forGetter(WorldGenConfig::getOres),
					ConfiguredFeature.field_25833.fieldOf("rubberTree").forGetter(WorldGenConfig::getRubberTree)
			)
			.apply(instance, WorldGenConfig::new)
	);

	private final List<TechRebornOre> ores;
	private final ConfiguredFeature<?, ?> rubberTree;

	public WorldGenConfig(List<TechRebornOre> ores, ConfiguredFeature<?, ?> rubberTree) {
		this.ores = ores;
		this.rubberTree = rubberTree;
	}

	public ConfiguredFeature<?, ?> getRubberTree() {
		return rubberTree;
	}

	public List<TechRebornOre> getOres() {
		return ores;
	}
}
