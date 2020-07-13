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

package techreborn.blocks.conduit;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import reborncore.common.blocks.BlockWrenchEventHandler;
import techreborn.blockentity.conduit.ItemConduitBlockEntity;
import techreborn.init.TRContent;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DimmerWorld on 12/07/2020.
 */
public class ConduitBlock extends BlockWithEntity {

	public static final BooleanProperty EAST = BooleanProperty.of("east");
	public static final BooleanProperty WEST = BooleanProperty.of("west");
	public static final BooleanProperty NORTH = BooleanProperty.of("north");
	public static final BooleanProperty SOUTH = BooleanProperty.of("south");
	public static final BooleanProperty UP = BooleanProperty.of("up");
	public static final BooleanProperty DOWN = BooleanProperty.of("down");

	public static final Map<Direction, BooleanProperty> PROPERTY_MAP = Util.make(new HashMap<>(), map -> {
		map.put(Direction.EAST, EAST);
		map.put(Direction.WEST, WEST);
		map.put(Direction.NORTH, NORTH);
		map.put(Direction.SOUTH, SOUTH);
		map.put(Direction.UP, UP);
		map.put(Direction.DOWN, DOWN);
	});

	private final ConduitShapeUtil conduitShapeUtil;

	public ConduitBlock() {
		super(Settings.of(Material.STONE).strength(1f, 8f).nonOpaque());

		setDefaultState(this.getStateManager().getDefaultState().with(EAST, false).with(WEST, false).with(NORTH, false)
				.with(SOUTH, false).with(UP, false).with(DOWN, false));

		BlockWrenchEventHandler.wrenableBlocks.add(this);

		conduitShapeUtil = new ConduitShapeUtil(this);
	}

	public BooleanProperty getProperty(Direction facing) {
		switch (facing) {
		case EAST:
			return EAST;
		case WEST:
			return WEST;
		case NORTH:
			return NORTH;
		case SOUTH:
			return SOUTH;
		case UP:
			return UP;
		case DOWN:
			return DOWN;
		default:
			return EAST;
		}
	}

	private boolean connectToConduit(@NotNull WorldAccess world, BlockPos ourPos, Direction direction) {
		BlockEntity ourBaseEntity = world.getBlockEntity(ourPos);
		BlockEntity otherBaseEntity = world.getBlockEntity(ourPos.offset(direction));

		if(!(ourBaseEntity instanceof ItemConduitBlockEntity)){
			return false;
		}

		// Cast to a variable our entity
		ItemConduitBlockEntity ourEntity = (ItemConduitBlockEntity)ourBaseEntity;

		if(!(otherBaseEntity instanceof ItemConduitBlockEntity)){
			ourEntity.removeItemConduit(direction);
			return false;
		}

		ItemConduitBlockEntity otherEntity = (ItemConduitBlockEntity)otherBaseEntity;

		// Can't connect according to entities.
		if(!ourEntity.canConnect(direction) || !otherEntity.canConnect(direction.getOpposite())){
			ourEntity.removeItemConduit(direction);
			return false;
		}

		// Add to entity as we've connected
		ourEntity.addItemConduit(direction, otherEntity);

		return true;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity entity = world.getBlockEntity(pos);
		if(world.isClient() || hand != Hand.MAIN_HAND || !(entity instanceof ItemConduitBlockEntity) || !player.isSneaking()) return super.onUse(state, world, pos, player, hand, hit);

		((ItemConduitBlockEntity) entity).changeMode(hit.getSide());

		return ActionResult.SUCCESS;
	}

	// BlockContainer
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView worldIn) {
		return new ItemConduitBlockEntity();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(EAST, WEST, NORTH, SOUTH, UP, DOWN);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState ourState, Direction ourFacing, BlockState otherState,
			WorldAccess worldIn, BlockPos ourPos, BlockPos otherPos) {

		if(worldIn.isClient()){
			return super.getStateForNeighborUpdate(ourState, ourFacing, otherState, worldIn, ourPos,otherPos);
		}

		return ourState.with(getProperty(ourFacing), connectToConduit(worldIn, ourPos, ourFacing));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext shapeContext) {
		return conduitShapeUtil.getShape(state);
	}


}
