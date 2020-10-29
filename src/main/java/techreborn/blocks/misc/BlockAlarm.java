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

package techreborn.blocks.misc;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import reborncore.api.ToolManager;
import reborncore.common.BaseBlockEntityProvider;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.util.WrenchUtils;
import techreborn.blockentity.machine.misc.AlarmBlockEntity;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public class BlockAlarm extends BaseBlockEntityProvider {
	public static final DirectionProperty FACING = Properties.FACING;
	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
	protected final VoxelShape[] shape;

	public BlockAlarm() {
		super(Block.Settings.of(Material.STONE).strength(2f, 2f));
		this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(ACTIVE, false));
		this.shape = GenCuboidShapes(3, 10);
		BlockWrenchEventHandler.wrenableBlocks.add(this);
	}

	private VoxelShape[] GenCuboidShapes(double depth, double width) {
		double culling = (16.0D - width) / 2;
		VoxelShape[] shapes = {
				Block.createCuboidShape(culling, 16.0 - depth, culling, 16.0 - culling, 16.0D, 16.0 - culling),
				Block.createCuboidShape(culling, 0.0D, culling, 16.0D - culling, depth, 16.0 - culling),
				Block.createCuboidShape(culling, culling, 16.0 - depth, 16.0 - culling, 16.0 - culling, 16.0D),
				Block.createCuboidShape(culling, culling, 0.0D, 16.0 - culling, 16.0 - culling, depth),
				Block.createCuboidShape(16.0 - depth, culling, culling, 16.0D, 16.0 - culling, 16.0 - culling),
				Block.createCuboidShape(0.0D, culling, culling, depth, 16.0 - culling, 16.0 - culling)
		};
		return shapes;
	}

	public static boolean isActive(BlockState state) {
		return state.get(ACTIVE);
	}

	public static Direction getFacing(BlockState state) {
		return state.get(FACING);
	}

	public static void setFacing(Direction facing, World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(FACING, facing));
	}

	public static void setActive(boolean active, World world, BlockPos pos) {
		Direction facing = world.getBlockState(pos).get(FACING);
		BlockState state = world.getBlockState(pos).with(ACTIVE, active).with(FACING, facing);
		world.setBlockState(pos, state, 3);
	}

	// BaseTileBlock
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView worldIn) {
		return new AlarmBlockEntity();
	}

	// Block
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, ACTIVE);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		for (Direction enumfacing : context.getPlacementDirections()) {
			BlockState iblockstate = this.getDefaultState().with(FACING, enumfacing.getOpposite());
			if (iblockstate.canPlaceAt(context.getWorld(), context.getBlockPos())) {
				return iblockstate;
			}
		}
		return null;
	}

	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockHitResult hitResult) {
		ItemStack stack = playerIn.getStackInHand(Hand.MAIN_HAND);
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);

		// We extended BaseTileBlock. Thus we should always have blockEntity entity. I hope.
		if (blockEntity == null) {
			return ActionResult.FAIL;
		}

		if (!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack)) {
			if (WrenchUtils.handleWrench(stack, worldIn, pos, playerIn, hitResult.getSide())) {
				return ActionResult.SUCCESS;
			}
		}

		if (!worldIn.isClient && playerIn.isSneaking()) {
			((AlarmBlockEntity) blockEntity).rightClick();
			return ActionResult.SUCCESS;

		}

		return super.onUse(state, worldIn, pos, playerIn, hand, hitResult);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}


	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext shapeContext) {
		return shape[getFacing(state).ordinal()];
	}


	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView worldIn, List<Text> tooltip, TooltipContext flagIn) {
		tooltip.add(new TranslatableText("techreborn.tooltip.alarm").formatted(Formatting.GRAY));
	}

}
