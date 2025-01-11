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

package techreborn.blocks.cable;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;
import reborncore.api.ToolManager;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.util.WrenchUtils;
import techreborn.api.events.CableElectrocutionEvent;
import techreborn.blockentity.cable.CableBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockSettings;
import techreborn.init.TRContent;
import techreborn.init.TRDamageTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by modmuss50 on 19/05/2017.
 */
public class CableBlock extends BlockWithEntity implements Waterloggable {

	public static final BooleanProperty EAST = Properties.EAST;
	public static final BooleanProperty WEST = Properties.WEST;
	public static final BooleanProperty NORTH = Properties.NORTH;
	public static final BooleanProperty SOUTH = Properties.SOUTH;
	public static final BooleanProperty UP = Properties.UP;
	public static final BooleanProperty DOWN = Properties.DOWN;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final BooleanProperty COVERED = BooleanProperty.of("covered");

	public static final Map<Direction, BooleanProperty> PROPERTY_MAP = Util.make(new HashMap<>(), map -> {
		map.put(Direction.EAST, EAST);
		map.put(Direction.WEST, WEST);
		map.put(Direction.NORTH, NORTH);
		map.put(Direction.SOUTH, SOUTH);
		map.put(Direction.UP, UP);
		map.put(Direction.DOWN, DOWN);
	});

	public final TRContent.Cables type;

	public CableBlock(TRContent.Cables type, String name) {
		super(TRBlockSettings.cable(name));
		this.type = type;
		setDefaultState(this.getStateManager().getDefaultState().with(EAST, false).with(WEST, false).with(NORTH, false)
				.with(SOUTH, false).with(UP, false).with(DOWN, false).with(WATERLOGGED, false).with(COVERED, false));
		BlockWrenchEventHandler.wrenchableBlocks.add(this);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		throw new IllegalStateException("CableBlock does not support getCodec!");
	}

	// BlockWithEntity
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CableBlockEntity(pos, state, type);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (world1, pos, state1, blockEntity) -> ((CableBlockEntity) blockEntity).tick(world1, pos, state1, (CableBlockEntity) blockEntity);
	}

	// Block
	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, BlockHitResult hitResult) {
		ItemStack stack = playerIn.getStackInHand(Hand.MAIN_HAND);
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);

		// We should always have blockEntity entity. I hope.
		if (blockEntity == null) {
			return ActionResult.FAIL;
		}

		if (stack.isEmpty()) {
			return super.onUse(state, worldIn, pos, playerIn, hitResult);
		}

		if (ToolManager.INSTANCE.canHandleTool(stack)) {
			if (state.get(COVERED) && !playerIn.isSneaking()) {
				((CableBlockEntity) blockEntity).setCover(null);
				worldIn.setBlockState(pos, state.with(COVERED, false));
				worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 0.6F, 1.0F);
				if (!worldIn.isClient) {
					ItemScatterer.spawn(worldIn, pos.getX(), pos.getY(), pos.getZ(), TRContent.Plates.WOOD.getStack());
				}
				return ActionResult.SUCCESS;
			}

			if (WrenchUtils.handleWrench(stack, worldIn, pos, playerIn, hitResult.getSide())) {
				return ActionResult.SUCCESS;
			}
		}

		if (!state.get(COVERED) && !type.canKill
				&& stack.getItem() == TRContent.Plates.WOOD.asItem()) {
			worldIn.setBlockState(pos, state.with(COVERED, true));
			worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 0.6F, 1.0F);
			if (!worldIn.isClient && !playerIn.isCreative()) {
				stack.decrement(1);
			}
			return ActionResult.SUCCESS;
		}

		return super.onUse(state, worldIn, pos, playerIn, hitResult);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(EAST, WEST, NORTH, SOUTH, UP, DOWN, WATERLOGGED, COVERED);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return getDefaultState()
				.with(WATERLOGGED, context.getWorld().getFluidState(context.getBlockPos()).getFluid() == Fluids.WATER);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (state.get(WATERLOGGED) && world instanceof WorldAccess worldIn) {
			worldIn.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return state;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, @Nullable WireOrientation wireOrientation, boolean notify) {
		if (world.getBlockEntity(pos) instanceof CableBlockEntity cable) {
			cable.neighborUpdate();
		}
		super.neighborUpdate(state, world, pos, block, wireOrientation, notify);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext shapeContext) {
		if (state.get(COVERED)) {
			return VoxelShapes.fullCube();
		}
		return CableShapeUtil.getShape(state);
	}

	@Override
	public VoxelShape getCullingShape(BlockState state) {
		return CableShapeUtil.getShape(state);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		if (!type.canKill) {
			return;
		}
		if (!(entity instanceof LivingEntity)) {
			return;
		}

		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity == null) {
			return;
		}
		if (!(blockEntity instanceof CableBlockEntity blockEntityCable)) {
			return;
		}

		if (blockEntityCable.getEnergy() <= 0) {
			return;
		}

		if (!CableElectrocutionEvent.EVENT.invoker().electrocute((LivingEntity) entity, type, pos, world, blockEntityCable)) {
			return;
		}

		if (TechRebornConfig.uninsulatedElectrocutionDamage) {
			if (type == TRContent.Cables.HV) {
				entity.setOnFireFor(1);
			}

			if (world instanceof ServerWorld serverWorld) {
				entity.damage(serverWorld, TRDamageTypes.create(world, TRDamageTypes.ELECTRIC_SHOCK), 1F);
			}
			blockEntityCable.setEnergy(0);
		}
		if (TechRebornConfig.uninsulatedElectrocutionSound) {
			world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.CABLE_SHOCK, SoundCategory.BLOCKS,
					0.6F, 1F);
		}
		if (TechRebornConfig.uninsulatedElectrocutionParticles) {
			world.addParticle(ParticleTypes.CRIT, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
		}
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return !state.get(COVERED) && Waterloggable.super.tryFillWithFluid(world, pos, state, fluidState);
	}

	@Override
	public boolean canFillWithFluid(PlayerEntity player, BlockView view, BlockPos pos, BlockState state, Fluid fluid) {
		return !state.get(COVERED) && Waterloggable.super.canFillWithFluid(player, view, pos, state, fluid);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getAppearance(BlockState state, BlockRenderView renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
		if (state.get(COVERED)) {
			final BlockState cover;

			if (((RenderAttachedBlockView) renderView).getBlockEntityRenderAttachment(pos) instanceof BlockState blockState) {
				cover = blockState;
			} else {
				cover = Blocks.OAK_PLANKS.getDefaultState();
			}

			return cover;
		}

		return super.getAppearance(state, renderView, pos, side, sourceState, sourcePos);
	}
}
