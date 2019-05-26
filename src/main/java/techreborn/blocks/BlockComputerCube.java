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

package techreborn.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.api.ToolManager;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.client.models.ModelCompound;
import reborncore.client.models.RebornModelRegistry;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.TechReborn;
import techreborn.init.ModSounds;
import techreborn.init.TRContent;

public class BlockComputerCube extends BlockMachineBase {

	public BlockComputerCube() {
		super();
		RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, this, "machines/tier2_machines"));
	}

	@Override
	public IMachineGuiHandler getGui() {
		return null;
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, Direction side, float hitX, float hitY, float hitZ) {

		ItemStack tool = playerIn.getStackInHand(Hand.MAIN_HAND);
		if (!tool.isEmpty() && ToolManager.INSTANCE.canHandleTool(tool)) {
			if (ToolManager.INSTANCE.handleTool(tool, pos, worldIn, playerIn, side, false)) {
				if (playerIn.isSneaking()) {
					ItemStack drop = new ItemStack(TRContent.COMPUTER_CUBE, 1);
					dropStack(worldIn, pos, drop);
					worldIn.playSound(null, playerIn.x, playerIn.y, playerIn.z, ModSounds.BLOCK_DISMANTLE,
							SoundCategory.BLOCKS, 0.6F, 1F);
					if (!worldIn.isClient) {
						worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
					}
					return true;
				}
				else {
					rotate(worldIn.getBlockState(pos), worldIn, pos, BlockRotation.CLOCKWISE_90);
					return true;
				}
			}
		}
		return false;
	}
}
