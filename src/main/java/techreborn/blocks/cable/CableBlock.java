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
import org.jspecify.annotations.Nullable;
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
import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Created by modmuss50 on 19/05/2017.
 */
public class CableBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;
	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty UP = BlockStateProperties.UP;
	public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty COVERED = BooleanProperty.create("covered");

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
		registerDefaultState(this.getStateDefinition().any().setValue(EAST, false).setValue(WEST, false).setValue(NORTH, false)
				.setValue(SOUTH, false).setValue(UP, false).setValue(DOWN, false).setValue(WATERLOGGED, false).setValue(COVERED, false));
		BlockWrenchEventHandler.wrenchableBlocks.add(this);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		throw new IllegalStateException("CableBlock does not support getCodec!");
	}

	// BlockWithEntity
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CableBlockEntity(pos, state, type);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return (world1, pos, state1, blockEntity) -> ((CableBlockEntity) blockEntity).tick(world1, pos, state1, (CableBlockEntity) blockEntity);
	}

	// Block
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn, BlockHitResult hitResult) {
		ItemStack stack = playerIn.getItemInHand(InteractionHand.MAIN_HAND);
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);

		// We should always have blockEntity entity. I hope.
		if (blockEntity == null) {
			return InteractionResult.FAIL;
		}

		if (stack.isEmpty()) {
			return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
		}

		if (ToolManager.INSTANCE.canHandleTool(stack)) {
			if (state.getValue(COVERED) && !playerIn.isShiftKeyDown()) {
				((CableBlockEntity) blockEntity).setCover(null);
				worldIn.setBlockAndUpdate(pos, state.setValue(COVERED, false));
				worldIn.playSound(playerIn, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 0.6F, 1.0F);
				if (!worldIn.isClientSide()) {
					Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), TRContent.Plates.WOOD.getStack());
				}
				return InteractionResult.SUCCESS;
			}

			if (WrenchUtils.handleWrench(stack, worldIn, pos, playerIn, hitResult.getDirection())) {
				return InteractionResult.SUCCESS;
			}
		}

		if (!state.getValue(COVERED) && !type.canKill
				&& stack.getItem() == TRContent.Plates.WOOD.asItem()) {
			worldIn.setBlockAndUpdate(pos, state.setValue(COVERED, true));
			worldIn.playSound(playerIn, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 0.6F, 1.0F);
			if (!worldIn.isClientSide() && !playerIn.isCreative()) {
				stack.shrink(1);
			}
			return InteractionResult.SUCCESS;
		}

		return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(EAST, WEST, NORTH, SOUTH, UP, DOWN, WATERLOGGED, COVERED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState()
				.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (state.getValue(WATERLOGGED) && world instanceof LevelAccessor worldIn) {
			worldIn.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return state;
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, @Nullable Orientation wireOrientation, boolean notify) {
		if (world.getBlockEntity(pos) instanceof CableBlockEntity cable) {
			cable.neighborUpdate();
		}
		super.neighborChanged(state, world, pos, block, wireOrientation, notify);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext shapeContext) {
		if (state.getValue(COVERED)) {
			return Shapes.block();
		}
		return CableShapeUtil.getShape(state);
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state) {
		return CableShapeUtil.getShape(state);
	}

	@Override
	protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl) {
		super.entityInside(state, world, pos, entity, handler, bl);
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

		if (TechRebornConfig.uninsulatedElectrocutionDamage.get()) {
			if (type == TRContent.Cables.HV) {
				entity.igniteForSeconds(1);
			}

			if (world instanceof ServerLevel serverWorld) {
				entity.hurtServer(serverWorld, TRDamageTypes.create(world, TRDamageTypes.ELECTRIC_SHOCK), 1F);
			}
			blockEntityCable.setEnergy(0);
		}
		if (TechRebornConfig.uninsulatedElectrocutionSound.get()) {
			world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.CABLE_SHOCK, SoundSource.BLOCKS,
					0.6F, 1F);
		}
		if (TechRebornConfig.uninsulatedElectrocutionParticles.get()) {
			world.addParticle(ParticleTypes.CRIT, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
		}
	}

	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
		return !state.getValue(COVERED) && SimpleWaterloggedBlock.super.placeLiquid(world, pos, state, fluidState);
	}

	@Override
	public boolean canPlaceLiquid(LivingEntity player, BlockGetter view, BlockPos pos, BlockState state, Fluid fluid) {
		return !state.getValue(COVERED) && SimpleWaterloggedBlock.super.canPlaceLiquid(player, view, pos, state, fluid);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getAppearance(BlockState state, BlockAndLightGetter renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
		if (state.getValue(COVERED)) {
			final BlockState cover;

			if (renderView.getBlockEntityRenderData(pos) instanceof BlockState blockState) {
				cover = blockState;
			} else {
				cover = Blocks.OAK_PLANKS.defaultBlockState();
			}

			return cover;
		}

		return super.getAppearance(state, renderView, pos, side, sourceState, sourcePos);
	}
}
