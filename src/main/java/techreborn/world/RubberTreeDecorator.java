package techreborn.world;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// Big thanks to SuperCoder7979 for this
public class RubberTreeDecorator extends Decorator<ChanceDecoratorConfig> {
    public RubberTreeDecorator(Function<Dynamic<?>, ? extends ChanceDecoratorConfig> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, ChanceDecoratorConfig config, BlockPos pos) {
        // Generate tree clusters randomly
        if (random.nextInt(config.chance) == 0) {
            // Generate 4 - 8 trees
            int treeCount = 4 + random.nextInt(5);
            return IntStream.range(0, treeCount).mapToObj((i) -> {
                int x = random.nextInt(16) + pos.getX();
                int z = random.nextInt(16) + pos.getZ();
                int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
                return new BlockPos(x, y, z);
            });
        }
        return Stream.empty();
    }
}