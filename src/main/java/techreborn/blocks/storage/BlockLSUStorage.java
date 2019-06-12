/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.blocks.storage;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import reborncore.api.ToolManager;
import reborncore.client.models.ModelCompound;
import reborncore.client.models.RebornModelRegistry;
import reborncore.common.BaseTileBlock;
import reborncore.common.RebornCoreConfig;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.util.WrenchUtils;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.tiles.storage.lesu.TileLSUStorage;

/**
 * Energy storage block for LESU
 */
public class BlockLSUStorage extends BaseTileBlock {

	public BlockLSUStorage() {
		super(FabricBlockSettings.of(Material.METAL).strength(2f, 2f).build());
		RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, this, "machines/energy"));
		BlockWrenchEventHandler.wrenableBlocks.add(this);
	}

	// BaseTileBlock
	@Override
	public void onBlockRemoved(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() == newState.getBlock()) {
			return;
		}
		if (worldIn.getBlockEntity(pos) instanceof TileLSUStorage) {
			TileLSUStorage tile = (TileLSUStorage) worldIn.getBlockEntity(pos);
			if (tile != null) {
				tile.removeFromNetwork();
			}
		}
		super.onBlockRemoved(state, worldIn, pos, newState, isMoving);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView worldIn) {
		return new TileLSUStorage();
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity player, ItemStack itemstack) {
		super.onPlaced(world, pos, state, player, itemstack);
		if (world.getBlockEntity(pos) instanceof TileLSUStorage) {
			TileLSUStorage tile = (TileLSUStorage) world.getBlockEntity(pos);
			if (tile != null) {
				tile.rebuildNetwork();
			}
		}
	}

	// Block
	@SuppressWarnings("deprecation")
	@Override
	public boolean activate(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockHitResult hitResult) {
		ItemStack stack = playerIn.getStackInHand(Hand.MAIN_HAND);
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);

		// We extended BaseTileBlock. Thus we should always have tile entity. I hope.
		if (tileEntity == null) {
			return false;
		}

		if (!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack)) {
			if (WrenchUtils.handleWrench(stack, worldIn, pos, playerIn, hitResult.getSide())) {
				return true;
			}
		}

		return super.activate(state, worldIn, pos, playerIn, hand, hitResult);
	}

	@Override
	public void getDrops(BlockState state, DefaultedList<ItemStack> drops, World world, BlockPos pos, int fortune) {
		if (RebornCoreConfig.wrenchRequired) {
			drops.add(new ItemStack(TRContent.MachineBlocks.BASIC.getFrame()));
		} else {
			super.getDrops(state, drops, world, pos, fortune);
		}
	}
}
