package techreborn.blocks.storage.energy.msb;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techreborn.blockentity.storage.energy.msb.MoltenSaltPortBlockEntity;

public class MoltenSaltPortBlock extends BlockWithEntity {

	public MoltenSaltPortBlock() {
		super(FabricBlockSettings.of(Material.METAL).strength(2f, 2f));
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new MoltenSaltPortBlockEntity(pos, state);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state,
		  PlayerEntity player) {
		if (world.isClient) {
			return;
		}

		// TODO: Add code to unlink the port block entity from battery
		super.onBreak(world, pos, state, player);
	}
}
