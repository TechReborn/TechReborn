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

import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.Torus;
import techreborn.blockentity.GuiType;
import techreborn.blockentity.machine.multiblock.FusionControlComputerBlockEntity;
import techreborn.init.TRBlockSettings;
import techreborn.init.TRContent;
import techreborn.init.TRDamageTypes;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockFusionControlComputer extends BlockMachineBase {
	public BlockFusionControlComputer(String name) {
		super(TRBlockSettings.fusionControlComputer(name));
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		final FusionControlComputerBlockEntity blockEntityFusionControlComputer = (FusionControlComputerBlockEntity) world.getBlockEntity(pos);
		if (!player.getItemInHand(hand).isEmpty() && (player.getItemInHand(hand).getItem() == TRContent.Machine.FUSION_COIL.asItem())) {
			List<BlockPos> coils = Torus.generate(blockEntityFusionControlComputer.getBlockPos(), blockEntityFusionControlComputer.size);
			boolean placed = false;
			for (BlockPos coil : coils) {
				if (player.getItemInHand(hand).isEmpty()) {
					return InteractionResult.SUCCESS;
				}
				if (world.getBlockState(coil).canBeReplaced(new BlockPlaceContext(new UseOnContext(player, hand, hit)))
					&& world.getBlockState(pos).getBlock() != TRContent.Machine.FUSION_COIL.block) {
					world.setBlockAndUpdate(coil, TRContent.Machine.FUSION_COIL.block.defaultBlockState());
					if (!player.isCreative()) {
						player.getItemInHand(hand).shrink(1);
					}
					placed = true;
				}
			}
			if (placed) {
				return InteractionResult.SUCCESS;
			}

		}
		return super.useItemOn(stack, state, world, pos, player, hand, hit);
	}

	@Override
	public IMachineGuiHandler getGui() {
		return GuiType.FUSION_CONTROLLER;
	}

	@Override
	public void stepOn(final Level worldIn, final BlockPos pos, final BlockState state,  final Entity entityIn) {
		super.stepOn(worldIn, pos, state, entityIn);
		if (!worldIn.isClientSide() && worldIn.getBlockEntity(pos) instanceof FusionControlComputerBlockEntity blockEntity) {
			if (blockEntity.craftingTickTime != 0 && blockEntity.isShapeValid()) {
				entityIn.hurtServer((ServerLevel) worldIn, TRDamageTypes.create(worldIn, TRDamageTypes.FUSION), 200F);
			}
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FusionControlComputerBlockEntity(pos, state);
	}

	@Override
	public boolean isAdvanced() {
		return true;
	}
}
