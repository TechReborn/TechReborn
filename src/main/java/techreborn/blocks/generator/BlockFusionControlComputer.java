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

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.Torus;
import techreborn.blockentity.GuiType;
import techreborn.blockentity.machine.multiblock.FusionControlComputerBlockEntity;
import techreborn.init.TRBlockSettings;
import techreborn.init.TRContent;
import techreborn.init.TRDamageTypes;

import java.util.List;

public class BlockFusionControlComputer extends BlockMachineBase {
	public BlockFusionControlComputer() {
		super(TRBlockSettings.fusionControlComputer());
	}

	@Override
	protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		final FusionControlComputerBlockEntity blockEntityFusionControlComputer = (FusionControlComputerBlockEntity) world.getBlockEntity(pos);
		if (!player.getStackInHand(hand).isEmpty() && (player.getStackInHand(hand).getItem() == TRContent.Machine.FUSION_COIL.asItem())) {
			List<BlockPos> coils = Torus.generate(blockEntityFusionControlComputer.getPos(), blockEntityFusionControlComputer.size);
			boolean placed = false;
			for (BlockPos coil : coils) {
				if (player.getStackInHand(hand).isEmpty()) {
					return ItemActionResult.SUCCESS;
				}
				if (world.getBlockState(coil).canReplace(new ItemPlacementContext(new ItemUsageContext(player, hand, hit)))
					&& world.getBlockState(pos).getBlock() != TRContent.Machine.FUSION_COIL.block) {
					world.setBlockState(coil, TRContent.Machine.FUSION_COIL.block.getDefaultState());
					if (!player.isCreative()) {
						player.getStackInHand(hand).decrement(1);
					}
					placed = true;
				}
			}
			if (placed) {
				return ItemActionResult.SUCCESS;
			}

		}
		blockEntityFusionControlComputer.isMultiblockValid();
		return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
	}

	@Override
	public IMachineGuiHandler getGui() {
		return GuiType.FUSION_CONTROLLER;
	}

	@Override
	public void onSteppedOn(final World worldIn, final BlockPos pos, final BlockState state,  final Entity entityIn) {
		super.onSteppedOn(worldIn, pos, state, entityIn);
		if (worldIn.getBlockEntity(pos) instanceof FusionControlComputerBlockEntity) {
			if (((FusionControlComputerBlockEntity) worldIn.getBlockEntity(pos)).craftingTickTime != 0
					&& ((FusionControlComputerBlockEntity) worldIn.getBlockEntity(pos)).isMultiblockValid()) {
				entityIn.damage(TRDamageTypes.create(worldIn, TRDamageTypes.FUSION), 200F);
			}
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new FusionControlComputerBlockEntity(pos, state);
	}

	@Override
	public boolean isAdvanced() {
		return true;
	}
}
