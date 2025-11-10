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

package techreborn.blocks.storage.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import reborncore.api.ToolManager;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.BaseBlockEntityProvider;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.ItemHandlerUtils;
import reborncore.common.util.WrenchUtils;
import techreborn.init.TRBlockSettings;



/**
 * Created by Rushmead
 */
public abstract class EnergyStorageBlock extends BaseBlockEntityProvider {
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	public final IMachineGuiHandler gui;

	public EnergyStorageBlock(IMachineGuiHandler gui, String name) {
		super(TRBlockSettings.energyStorage(name));
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
		this.gui = gui;
		BlockWrenchEventHandler.wrenchableBlocks.add(this);
	}

	public void setFacing(Direction facing, Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(FACING, facing));
	}

	public Direction getFacing(BlockState state) {
		return state.getValue(FACING);
	}

	// BaseBlockEntityProvider
	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(worldIn, pos, state, placer, stack);
		Direction facing = placer.getDirection().getOpposite();
		if (placer.getXRot() < -50) {
			facing = Direction.DOWN;
		} else if (placer.getXRot() > 50) {
			facing = Direction.UP;
		}
		setFacing(facing, worldIn, pos);
	}

	// Block
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn, BlockHitResult hitResult) {
		ItemStack stack = playerIn.getItemInHand(InteractionHand.MAIN_HAND);
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);

		// We extended BlockTileBase. Thus, we should always have blockEntity entity. I hope.
		if (blockEntity == null) {
			return InteractionResult.FAIL;
		}

		if (!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack)) {
			if (WrenchUtils.handleWrench(stack, worldIn, pos, playerIn, hitResult.getDirection())) {
				return InteractionResult.SUCCESS;
			}
		}

		if (!playerIn.isShiftKeyDown() && gui != null) {
			gui.open(playerIn, pos, worldIn);
			return InteractionResult.SUCCESS;
		}

		return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel worldIn, BlockPos pos, boolean isMoving) {
		ItemHandlerUtils.dropContainedItems(worldIn, pos);
		super.affectNeighborsAfterRemoval(state, worldIn, pos, isMoving);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
		return PowerAcceptorBlockEntity.calculateComparatorOutputFromEnergy(world.getBlockEntity(pos));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}
}
