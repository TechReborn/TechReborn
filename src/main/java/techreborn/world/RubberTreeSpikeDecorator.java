package techreborn.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class RubberTreeSpikeDecorator  extends TreeDecorator {
	public static final Codec<RubberTreeSpikeDecorator> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Codec.INT.fieldOf("spire_height").forGetter(RubberTreeSpikeDecorator::getSpireHeight),
			BlockStateProvider.TYPE_CODEC.fieldOf("provider").forGetter(RubberTreeSpikeDecorator::getProvider)
		).apply(instance, RubberTreeSpikeDecorator::new)
	);

	public static final TreeDecoratorType<RubberTreeSpikeDecorator> RUBBER_TREE_SPIKE = Registry.register(Registry.TREE_DECORATOR_TYPE, new Identifier("techreborn", "rubber_tree_spike"), new TreeDecoratorType<>(CODEC));

	private final int spireHeight;
	private final BlockStateProvider provider;

	public RubberTreeSpikeDecorator(int spireHeight, BlockStateProvider spireBlockState) {
		this.spireHeight = spireHeight;
		this.provider = spireBlockState;
	}

	@Override
	protected TreeDecoratorType<?> getType() {
		return RUBBER_TREE_SPIKE;
	}

	@Override
	public void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, List<BlockPos> logPositions, List<BlockPos> leavesPositions) {
		logPositions.stream()
			.max(Comparator.comparingInt(BlockPos::getY))
			.ifPresent(blockPos -> {
				for (int i = 0; i < spireHeight; i++) {
					BlockPos sPos = blockPos.up(i);
					replacer.accept(sPos, provider.getBlockState(random, sPos));
				}
			});
	}

	public int getSpireHeight() {
		return spireHeight;
	}

	public BlockStateProvider getProvider() {
		return provider;
	}
}
