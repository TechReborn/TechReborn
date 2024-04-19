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

package techreborn.blocks.generator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.ToolManager;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockSettings;

import java.util.List;

public class BlockFusionCoil extends Block {

	public BlockFusionCoil() {
		super(TRBlockSettings.fusionCoil());
	}

	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
							BlockHitResult hitResult) {

		ItemStack tool = playerIn.getStackInHand(Hand.MAIN_HAND);
		if (tool.isEmpty()) return ActionResult.PASS;
		if (!ToolManager.INSTANCE.canHandleTool(tool)) return ActionResult.PASS;

		if (ToolManager.INSTANCE.handleTool(tool, pos, worldIn, playerIn, hitResult.getSide(), false)) {
			if (!playerIn.isSneaking()) return ActionResult.PASS;
			ItemStack drop = new ItemStack(this);
			dropStack(worldIn, pos, drop);
			worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), ModSounds.BLOCK_DISMANTLE,
					SoundCategory.BLOCKS, 0.6F, 1F);
			if (!worldIn.isClient) {
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			}
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		super.appendTooltip(stack, context, tooltip, options);
		tooltip.add(Text.translatable("techreborn.tooltip.fusion_coil").formatted(Formatting.BLUE));
	}
}
