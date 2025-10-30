/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import reborncore.api.IToolDrop;
import reborncore.api.ToolManager;
import reborncore.common.BaseBlockEntityProvider;
import reborncore.common.misc.ModSounds;

/**
 * @author drcrazy
 */
public class WrenchUtils {

	public static boolean handleWrench(ItemStack stack, Level worldIn, BlockPos pos, Player playerIn, Direction side) {
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);
		if (blockEntity == null) {
			return false;
		}

		if (ToolManager.INSTANCE.handleTool(stack, pos, worldIn, playerIn, side, true)) {
			if (playerIn.isShiftKeyDown()) {
				if (blockEntity instanceof IToolDrop) {
					ItemStack drop = ((IToolDrop) blockEntity).getToolDrop(playerIn);
					if (drop == null) {
						return false;
					}

					boolean dropContents = true;
					Block block = blockEntity.getBlockState().getBlock();
					if (block instanceof BaseBlockEntityProvider) {
						ItemStack blockEntityDrop = ((BaseBlockEntityProvider) block).getDropWithContents(worldIn, pos, drop).orElse(ItemStack.EMPTY);
						if (!blockEntityDrop.isEmpty()) {
							dropContents = false;
							drop = blockEntityDrop;
						}
					}

					if (!worldIn.isClientSide) {
						if (dropContents) {
							ItemHandlerUtils.dropContainedItems(worldIn, pos);
						}
						if (!drop.isEmpty()) {
							net.minecraft.world.Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), drop);
						}
						worldIn.removeBlockEntity(pos);
						worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
					}
					worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), ModSounds.BLOCK_DISMANTLE,
							SoundSource.BLOCKS, 0.6F, 1F);
				}
			} else {
				BlockState oldState = worldIn.getBlockState(pos);
				BlockState newState;
				if (oldState.hasProperty(BlockStateProperties.FACING)) {
					// Machine can face all 6 directions. Let's move face to hit side.
					newState = oldState.setValue(BlockStateProperties.FACING, side);
				} else {
					newState = oldState.rotate(Rotation.CLOCKWISE_90);
				}

				if (!newState.canSurvive(worldIn, pos)) {
					return false;
				}
				worldIn.setBlockAndUpdate(pos, newState);
				worldIn.neighborChanged(pos, newState.getBlock(), null);
			}
			return true;
		}
		return false;
	}
}
