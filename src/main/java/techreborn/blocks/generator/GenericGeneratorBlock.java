package techreborn.blocks.generator;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import techreborn.blocks.GenericMachineBlock;
import techreborn.client.EGui;

import java.util.function.Supplier;

/**
 * An extension of {@link GenericMachineBlock} that provides utilities
 * for generators, like comparator output based on energy.
 */
public class GenericGeneratorBlock extends GenericMachineBlock {
    public GenericGeneratorBlock(EGui gui, Supplier<BlockEntity> blockEntityClass) {
        super(gui, blockEntityClass);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return PowerAcceptorBlockEntity.calculateComparatorOutputFromEnergy(world.getBlockEntity(pos));
    }
}
