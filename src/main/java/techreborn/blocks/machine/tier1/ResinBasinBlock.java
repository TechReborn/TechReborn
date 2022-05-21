/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.blocks.machine.tier1;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import reborncore.common.BaseBlockEntityProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.WorldUtils;
import techreborn.blockentity.machine.tier1.ResinBasinBlockEntity;
import techreborn.init.TRContent;

import java.util.function.BiFunction;

public class ResinBasinBlock extends BaseBlockEntityProvider {

	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty POURING = BooleanProperty.of("pouring");
	public static final BooleanProperty FULL = BooleanProperty.of("full");
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0d,0d, 0d, 16d, 8d, 16d);
	BiFunction<BlockPos, BlockState, BlockEntity> blockEntityClass;

	public ResinBasinBlock(BiFunction<BlockPos, BlockState, BlockEntity> blockEntityClass) {
		super(Block.Settings.of(Material.WOOD).strength(2F, 2F));
		this.blockEntityClass = blockEntityClass;

		this.setDefaultState(
				this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(POURING, false).with(FULL, false));
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	public void setFacing(Direction facing, World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(FACING, facing));
	}

	// Block
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, POURING, FULL);
	}

	public Direction getFacing(BlockState state) {
		return state.get(FACING);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world == null || world.isClient() || player == null || pos == null || !(world.getBlockEntity(pos) instanceof ResinBasinBlockEntity))
			return ActionResult.PASS;
		ResinBasinBlockEntity basin = (ResinBasinBlockEntity)world.getBlockEntity(pos);
		ItemStack sap = basin.empty();
		if (sap.isEmpty())
			return ActionResult.PASS;
		player.getInventory().offerOrDrop(sap);
		return ActionResult.SUCCESS;
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
			WorldUtils.dropItem(this.asItem(), worldIn, pos);
			placer.sendMessage(Text.translatable("techreborn.tooltip.invalid_basin_placement"));
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		if (blockEntityClass == null) {
			return null;
		}
		return blockEntityClass.apply(pos, state);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof MachineBaseBlockEntity) {
			((MachineBaseBlockEntity) blockEntity).onBreak(world, player, pos, state);
		}

		super.onBreak(world, pos, state, player);
	}
}
