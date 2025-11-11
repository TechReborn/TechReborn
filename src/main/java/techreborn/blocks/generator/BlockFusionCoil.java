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

import reborncore.api.ToolManager;
import reborncore.common.BaseBlock;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockSettings;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockFusionCoil extends BaseBlock {

	public BlockFusionCoil(String name) {
		super(TRBlockSettings.fusionCoil(name));
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn,
							BlockHitResult hitResult) {

		ItemStack tool = playerIn.getItemInHand(InteractionHand.MAIN_HAND);
		if (tool.isEmpty()) return InteractionResult.PASS;
		if (!ToolManager.INSTANCE.canHandleTool(tool)) return InteractionResult.PASS;

		if (ToolManager.INSTANCE.handleTool(tool, pos, worldIn, playerIn, hitResult.getDirection(), false)) {
			if (!playerIn.isShiftKeyDown()) return InteractionResult.PASS;
			ItemStack drop = new ItemStack(this);
			popResource(worldIn, pos, drop);
			worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), ModSounds.BLOCK_DISMANTLE,
					SoundSource.BLOCKS, 0.6F, 1F);
			if (!worldIn.isClientSide) {
				worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
			}
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
		super.appendTooltip(stack, context, tooltip, options);
		tooltip.add(Component.translatable("techreborn.tooltip.fusion_coil").withStyle(ChatFormatting.BLUE));
	}
}
