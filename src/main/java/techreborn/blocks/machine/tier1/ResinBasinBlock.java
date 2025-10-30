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

import reborncore.common.BaseBlockEntityProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.WorldUtils;
import techreborn.blockentity.machine.tier1.ResinBasinBlockEntity;
import techreborn.init.TRBlockSettings;
import techreborn.init.TRContent;

import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ResinBasinBlock extends BaseBlockEntityProvider {

	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty POURING = BooleanProperty.create("pouring");
	public static final BooleanProperty FULL = BooleanProperty.create("full");
	protected static final VoxelShape SHAPE = Block.box(0d,0d, 0d, 16d, 8d, 16d);
	final BiFunction<BlockPos, BlockState, BlockEntity> blockEntityClass;

	public ResinBasinBlock(BiFunction<BlockPos, BlockState, BlockEntity> blockEntityClass, String name) {
		super(TRBlockSettings.resinBasin(name));
		this.blockEntityClass = blockEntityClass;

		this.registerDefaultState(
				this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(POURING, false).setValue(FULL, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	public void setFacing(Direction facing, Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(FACING, facing));
	}

	// Block
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POURING, FULL);
	}

	public Direction getFacing(BlockState state) {
		return state.getValue(FACING);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (world == null || world.isClientSide() || player == null || pos == null || !(world.getBlockEntity(pos) instanceof ResinBasinBlockEntity basin))
			return InteractionResult.PASS;
		ItemStack sap = basin.empty();
		if (sap.isEmpty())
			return InteractionResult.PASS;
		player.getInventory().placeItemBackInInventory(sap);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(worldIn, pos, state, placer, stack);
		if (worldIn.isClientSide) return;

		Direction facing = placer.getDirection().getOpposite();
		setFacing(facing, worldIn, pos);

		// Drop item if not next to log and yell at user
		if (worldIn.getBlockState(pos.relative(facing.getOpposite())).getBlock() != TRContent.RUBBER_LOG) {
			worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			WorldUtils.dropItem(this.asItem(), worldIn, pos);
			if (placer instanceof ServerPlayer player) {
				player.sendSystemMessage(Component.translatable("techreborn.tooltip.invalid_basin_placement"));
			}
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		if (blockEntityClass == null) {
			return null;
		}
		return blockEntityClass.apply(pos, state);
	}

	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof MachineBaseBlockEntity) {
			((MachineBaseBlockEntity) blockEntity).onBreak(world, player, pos, state);
		}

		return super.playerWillDestroy(world, pos, state, player);
	}
}
