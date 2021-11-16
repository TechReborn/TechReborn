package techreborn.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class RubberTreeSpikeDecorator  extends TreeDecorator {
	private final int spireHeight;
	private final BlockState spireBlockState;

	public RubberTreeSpikeDecorator(int spireHeight, BlockState spireBlockState) {
		this.spireHeight = spireHeight;
		this.spireBlockState = spireBlockState;
	}

	@Override
	protected TreeDecoratorType<?> getType() {
		// TODO 1.18 really needs fabric api help here.
		throw new UnsupportedOperationException();
	}

	@Override
	public void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, List<BlockPos> logPositions, List<BlockPos> leavesPositions) {
		System.out.println("hi");
	}

	private void generateSpike(TestableWorld world, BlockPos pos, BiConsumer<BlockPos, BlockState> replacer) {
		final int startScan = pos.getY();
		BlockPos topPos = null;

		//Limit the scan to 15 blocks
		while (topPos == null && pos.getY() - startScan < 15) {
			pos = pos.up();
			if (world.testBlockState(pos, BlockState::isAir)) {
				topPos = pos;
			}
		}

		if (topPos == null) return;

		for (int i = 0; i < spireHeight; i++) {
			replacer.accept(pos.up(i), spireBlockState);
		}
	}
}
