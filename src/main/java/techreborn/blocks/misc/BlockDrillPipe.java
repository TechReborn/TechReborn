package techreborn.blocks.misc;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BlockDrillPipe extends FallingBlock {
	VoxelShape shape;

	public BlockDrillPipe() {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.WOOD).strength(2.0F, 3.0F).sounds(BlockSoundGroup.METAL));


		// Pipe shape for outline
		shape = VoxelShapes.cuboid(6 / 16D, 0, 6 / 16D, 10 / 16D, 1, 10 / 16D);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return shape;
	}
}
