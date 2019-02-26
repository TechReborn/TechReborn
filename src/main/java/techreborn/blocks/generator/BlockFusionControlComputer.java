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

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
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
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn,
			EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		final TileFusionControlComputer tileFusionControlComputer = (TileFusionControlComputer) worldIn.getTileEntity(pos);
		if(!playerIn.getHeldItem(hand).isEmpty() && (playerIn.getHeldItem(hand).getItem() == TRContent.Machine.FUSION_COIL.asItem())){
			List<BlockPos> coils = Torus.generate(tileFusionControlComputer.getPos(), tileFusionControlComputer.size);
			boolean placed = false;
			for(BlockPos coil : coils){
				if(playerIn.getHeldItem(hand).isEmpty()){
					return true;
				}
				if(worldIn.isAirBlock(coil) && !tileFusionControlComputer.isCoil(coil)){
					worldIn.setBlockState(coil, TRContent.Machine.FUSION_COIL.block.getDefaultState());
					if(!playerIn.isCreative()){
						playerIn.getHeldItem(hand).shrink(1);
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
	public void onEntityWalk(final World worldIn, final BlockPos pos, final Entity entityIn) {
		super.onEntityWalk(worldIn, pos, entityIn);
		if (worldIn.getTileEntity(pos) instanceof TileFusionControlComputer) {
			if (((TileFusionControlComputer) worldIn.getTileEntity(pos)).crafingTickTime != 0
					&& ((TileFusionControlComputer) worldIn.getTileEntity(pos)).checkCoils()) {
				entityIn.attackEntityFrom(new FusionDamageSource(), 200F);
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileFusionControlComputer();
	}
	
	@Override
	public boolean isAdvanced() {
		return true;
	}
}
