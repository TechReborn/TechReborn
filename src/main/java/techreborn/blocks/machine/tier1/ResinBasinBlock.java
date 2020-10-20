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

	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty POURING = BooleanProperty.of("pouring");
	public static final BooleanProperty FULL = BooleanProperty.of("full");
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
			WorldUtils.dropItem(this.asItem(), worldIn, pos);
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
		if (blockEntity instanceof MachineBaseBlockEntity) {
			((MachineBaseBlockEntity) blockEntity).onBreak(world, player, pos, state);
		}

		super.onBreak(world, pos, state, player);
	}
}
