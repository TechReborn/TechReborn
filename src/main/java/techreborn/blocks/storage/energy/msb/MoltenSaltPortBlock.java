package techreborn.blocks.storage.energy.msb;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techreborn.blockentity.storage.energy.msb.MoltenSaltPortBlockEntity;
import techreborn.items.tool.WrenchItem;

public class MoltenSaltPortBlock extends BlockWithEntity {

	public static final BooleanProperty CHARGING = BooleanProperty.of("charging");

	public MoltenSaltPortBlock() {
		super(FabricBlockSettings.of(Material.METAL).strength(2f, 2f));
		setDefaultState(getStateManager().getDefaultState().with(CHARGING, true));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(CHARGING);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new MoltenSaltPortBlockEntity(pos, state);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.getStackInHand(hand).getItem() instanceof WrenchItem) {
			// Invert charging state and update world
			boolean isCharging = !state.get(CHARGING);
			world.setBlockState(pos, state.with(CHARGING, isCharging));

			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof MoltenSaltPortBlockEntity) {
				((MoltenSaltPortBlockEntity) blockEntity).blockStateUpdate();
			}
			return ActionResult.SUCCESS;
		}

		return ActionResult.FAIL;
	}
}
