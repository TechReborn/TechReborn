package techreborn.blocks.misc;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import reborncore.common.BaseBlock;
import techreborn.init.TRContent;

public class BlockDrillHead extends BaseBlock {
	VoxelShape shape;

	public BlockDrillHead() {
		super(FabricBlockSettings.of(Material.METAL).strength(-1, 40000000).sounds(BlockSoundGroup.METAL));

		shape = VoxelShapes.cuboid(3 / 16D, 0, 3 / 16D, 13 / 16D, 1, 13 / 16D);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return shape;
	}


	// Sanity check, if pipe is destroyed, so is drill
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		super.neighborUpdate(state, world, pos, block, fromPos, notify);

		// Pipe has been destroyed, I shall commit
		if(fromPos.getY() == pos.offset(Direction.UP).getY() && block == TRContent.DRILL_PIPE){
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}
}
