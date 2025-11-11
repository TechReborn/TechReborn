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

package techreborn.blocks.lighting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import reborncore.api.ToolManager;
import reborncore.common.BaseBlockEntityProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.util.WrenchUtils;
import techreborn.blockentity.lighting.LampBlockEntity;
import techreborn.init.TRBlockSettings;

import java.util.function.ToIntFunction;

public class LampBlock extends BaseBlockEntityProvider {

	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	public static final BooleanProperty ACTIVE = BlockMachineBase.ACTIVE;
	protected final VoxelShape[] shape;

	private final int cost;
	private static final int brightness = 15;

	public LampBlock(int cost, double depth, double width, String name) {
		super(TRBlockSettings.lightBlock(name).lightLevel(createLightLevelFromBlockState()));
		this.shape = genCuboidShapes(depth, width);
		this.cost = cost;
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(ACTIVE, false));
		BlockWrenchEventHandler.wrenchableBlocks.add(this);
	}

	private static ToIntFunction<BlockState> createLightLevelFromBlockState() {
		return (blockState) -> isActive(blockState) ? brightness : 0;
	}

	private VoxelShape[] genCuboidShapes(double depth, double width) {
		double culling = (16.0D - width) / 2;
		return new VoxelShape[]{
				box(culling, 16.0 - depth, culling, 16.0 - culling, 16.0D, 16.0 - culling),
				box(culling, 0.0D, culling, 16.0D - culling, depth, 16.0 - culling),
				box(culling, culling, 16.0 - depth, 16.0 - culling, 16.0 - culling, 16.0D),
				box(culling, culling, 0.0D, 16.0 - culling, 16.0 - culling, depth),
				box(16.0 - depth, culling, culling, 16.0D, 16.0 - culling, 16.0 - culling),
				box(0.0D, culling, culling, depth, 16.0 - culling, 16.0 - culling)
		};
	}

	public static boolean isActive(BlockState state) {
		return state.hasProperty(ACTIVE) && state.getValue(ACTIVE);
	}

	public static Direction getFacing(BlockState state) {
		return state.getValue(FACING);
	}

	public static void setFacing(Direction facing, Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(FACING, facing));
	}

	public static void setActive(Boolean active, Level world, BlockPos pos) {
		Direction facing = world.getBlockState(pos).getValue(FACING);
		BlockState state = world.getBlockState(pos).setValue(ACTIVE, active).setValue(FACING, facing);
		world.setBlock(pos, state, 3);
	}

	public int getCost() {
		return cost;
	}

	// BaseTileBlock
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LampBlockEntity(pos, state);
	}

	// Block
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ACTIVE);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		for (Direction facing : context.getNearestLookingDirections()) {
			BlockState state = this.defaultBlockState().setValue(FACING, facing.getOpposite());
			if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
				return state;
			}
		}
		return null;
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockView, BlockPos blockPos, CollisionContext shapeContext) {
		return shape[blockState.getValue(FACING).ordinal()];
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn, BlockHitResult hitResult) {
		ItemStack stack = playerIn.getItemInHand(InteractionHand.MAIN_HAND);
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);

		// We extended BaseTileBlock. Thus, we should always have blockEntity entity. I hope.
		if (blockEntity == null) {
			return InteractionResult.FAIL;
		}

		if (!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack)) {
			if (WrenchUtils.handleWrench(stack, worldIn, pos, playerIn, hitResult.getDirection())) {
				return InteractionResult.SUCCESS;
			}
		}

		return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
	}
}
