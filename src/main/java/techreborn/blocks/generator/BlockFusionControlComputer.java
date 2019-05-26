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

package techreborn.blocks.generator;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.client.models.ModelCompound;
import reborncore.client.models.RebornModelRegistry;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.Torus;
import techreborn.TechReborn;
import techreborn.client.EGui;
import techreborn.init.TRContent;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;
import techreborn.utils.damageSources.FusionDamageSource;

import java.util.List;

public class BlockFusionControlComputer extends BlockMachineBase {

	public BlockFusionControlComputer() {
		super();
		RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, this, "machines/generators"));
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
			Hand hand, Direction side, float hitX, float hitY, float hitZ) {
		final TileFusionControlComputer tileFusionControlComputer = (TileFusionControlComputer) worldIn.getBlockEntity(pos);
		if(!playerIn.getStackInHand(hand).isEmpty() && (playerIn.getStackInHand(hand).getItem() == TRContent.Machine.FUSION_COIL.asItem())){
			List<BlockPos> coils = Torus.generate(tileFusionControlComputer.getPos(), tileFusionControlComputer.size);
			boolean placed = false;
			for(BlockPos coil : coils){
				if(playerIn.getStackInHand(hand).isEmpty()){
					return true;
				}
				if(worldIn.isAir(coil) && !tileFusionControlComputer.isCoil(coil)){
					worldIn.setBlockState(coil, TRContent.Machine.FUSION_COIL.block.getDefaultState());
					if(!playerIn.isCreative()){
						playerIn.getStackInHand(hand).subtractAmount(1);
					}
					placed = true;
				}
			}
			if(placed){
				return true;
			}

		}
		tileFusionControlComputer.checkCoils();
		return super.onBlockActivated(state, worldIn, pos, playerIn, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public IMachineGuiHandler getGui() {
		return EGui.FUSION_CONTROLLER;
	}

	@Override
	public void onSteppedOn(final World worldIn, final BlockPos pos, final Entity entityIn) {
		super.onSteppedOn(worldIn, pos, entityIn);
		if (worldIn.getBlockEntity(pos) instanceof TileFusionControlComputer) {
			if (((TileFusionControlComputer) worldIn.getBlockEntity(pos)).crafingTickTime != 0
					&& ((TileFusionControlComputer) worldIn.getBlockEntity(pos)).checkCoils()) {
				entityIn.damage(new FusionDamageSource(), 200F);
			}
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockView worldIn) {
		return new TileFusionControlComputer();
	}
	
	@Override
	public boolean isAdvanced() {
		return true;
	}
}
