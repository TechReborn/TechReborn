package techreborn.blocks.machine.tier1;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import reborncore.common.BaseBlockEntityProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.WorldUtils;
import techreborn.init.TRContent;

import java.util.UUID;
import java.util.function.Supplier;

public class ResinBasinBlock extends BaseBlockEntityProvider {

	public static DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static BooleanProperty POURING = BooleanProperty.of("pouring");
	public static BooleanProperty FULL = BooleanProperty.of("full");
	Supplier<BlockEntity> blockEntityClass;

	public ResinBasinBlock(Supplier<BlockEntity> blockEntityClass) {
		super(Block.Settings.of(Material.WOOD).strength(2F, 2F));
		this.blockEntityClass = blockEntityClass;

		this.setDefaultState(
				this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(POURING, false).with(FULL, false));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.cuboid(0, 0, 0, 1, 15 / 16f, 1);
	}

	public void setFacing(Direction facing, World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(FACING, facing));
	}

	// Block
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		FACING = DirectionProperty.of("facing", Direction.Type.HORIZONTAL);
		POURING = BooleanProperty.of("pouring");
		FULL = BooleanProperty.of("full");
		builder.add(FACING, POURING, FULL);
	}

	public Direction getFacing(BlockState state) {
		return state.get(FACING);
	}

	@Override
	public void onPlaced(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onPlaced(worldIn, pos, state, placer, stack);
		if (worldIn.isClient) return;

		Direction facing = placer.getHorizontalFacing().getOpposite();
		setFacing(facing, worldIn, pos);

		// Drop item if not next to log and yell at user
		if (worldIn.getBlockState(pos.offset(facing.getOpposite())).getBlock() != TRContent.RUBBER_LOG) {
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
			WorldUtils.dropItem(this.asItem(), worldIn,pos);
			placer.sendSystemMessage(new TranslatableText("techreborn.tooltip.invalid_basin_placement"), UUID.randomUUID());
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockView worldIn) {
		if (blockEntityClass == null) {
			return null;
		}
		return blockEntityClass.get();
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if(blockEntity instanceof MachineBaseBlockEntity){
			((MachineBaseBlockEntity) blockEntity).onBreak(world, player, pos, state);
		}

		super.onBreak(world, pos, state, player);
	}
}
