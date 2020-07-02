package techreborn.blocks.machine.tier1;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import reborncore.api.blockentity.IMachineGuiHandler;
import techreborn.blocks.GenericMachineBlock;

import java.util.function.Supplier;

public class TapperBlock extends GenericMachineBlock {
	public TapperBlock(IMachineGuiHandler gui, Supplier<BlockEntity> blockEntityClass) {
		super(gui, blockEntityClass);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.cuboid(2/16f, 0, 2/16f,14/16f,8/16f,14/16f);
	}
}
