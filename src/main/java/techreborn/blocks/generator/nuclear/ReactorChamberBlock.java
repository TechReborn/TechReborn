/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.blocks.generator.nuclear;

import techreborn.blockentity.generator.nuclear.NuclearReactorBlockEntity;
import techreborn.blockentity.generator.nuclear.ReactorChamberBlockEntity;
import techreborn.init.TRBlockSettings;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import org.jspecify.annotations.Nullable;

/**
 * Reactor Chamber block that expands the Nuclear Reactor's available slots.
 * Each chamber placed adjacent to a reactor adds one column (6 slots) to the reactor grid.
 * The reactor starts with 3 columns (18 slots) and can expand up to 9 columns (54 slots) with 6 chambers.
 */
public class ReactorChamberBlock extends Block implements EntityBlock {

	public ReactorChamberBlock(String name) {
		super(TRBlockSettings.reactorChamber(name));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ReactorChamberBlockEntity(pos, state);
	}

	// Ticker for reactor chambers to push energy to external blocks each tick.
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return (world, blockPos, blockState, blockEntity) -> ReactorChamberBlockEntity.tick(world, blockPos, blockState, (ReactorChamberBlockEntity) blockEntity);
	}

	@Override
	protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(state, world, pos, oldState, isMoving);
		if (!world.isClientSide()) {
			linkToReactor(world, pos);
		}
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean isMoving) {
		unlinkFromReactor(world, pos);
		super.affectNeighborsAfterRemoval(state, world, pos, isMoving);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (world.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		// Open the connected reactor's GUI
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof ReactorChamberBlockEntity chamber) {
			BlockPos reactorPos = chamber.getLinkedReactorPos();
			if (reactorPos != null) {
				BlockEntity reactorBe = world.getBlockEntity(reactorPos);
				if (reactorBe instanceof NuclearReactorBlockEntity reactor) {
					BlockState reactorState = world.getBlockState(reactorPos);
					if (reactorState.getBlock() instanceof NuclearReactorBlock reactorBlock) {
						reactorBlock.getGui().open(player, reactorPos, world);
						return InteractionResult.CONSUME;
					}
				}
			}
		}

		return InteractionResult.PASS;
	}

	/**
	 * Link this chamber to an adjacent reactor.
	 */
	private void linkToReactor(Level world, BlockPos chamberPos) {
		for (Direction direction : Direction.values()) {
			BlockPos adjacent = chamberPos.relative(direction);
			BlockEntity be = world.getBlockEntity(adjacent);
			if (be instanceof NuclearReactorBlockEntity reactor) {
				// Link this chamber to the reactor
				BlockEntity chamberBe = world.getBlockEntity(chamberPos);
				if (chamberBe instanceof ReactorChamberBlockEntity chamber) {
					chamber.setLinkedReactorPos(adjacent);
					reactor.onChamberAdded(chamberPos);
					return;
				}
			}
		}
	}

	/**
	 * Unlink this chamber from its connected reactor.
	 */
	private void unlinkFromReactor(ServerLevel world, BlockPos chamberPos) {
		BlockEntity chamberBe = world.getBlockEntity(chamberPos);
		if (chamberBe instanceof ReactorChamberBlockEntity chamber) {
			BlockPos reactorPos = chamber.getLinkedReactorPos();
			if (reactorPos != null) {
				BlockEntity reactorBe = world.getBlockEntity(reactorPos);
				if (reactorBe instanceof NuclearReactorBlockEntity reactor) {
					reactor.onChamberRemoved(chamberPos);
				}
			}
		}
	}

	/**
	 * Find a connected reactor for this position.
	 */
	@Nullable
	public static NuclearReactorBlockEntity findConnectedReactor(LevelAccessor world, BlockPos chamberPos) {
		for (Direction direction : Direction.values()) {
			BlockPos adjacent = chamberPos.relative(direction);
			BlockEntity be = world.getBlockEntity(adjacent);
			if (be instanceof NuclearReactorBlockEntity reactor) {
				return reactor;
			}
		}
		return null;
	}
}
