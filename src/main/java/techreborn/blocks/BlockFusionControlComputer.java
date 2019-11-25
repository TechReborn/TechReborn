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

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.common.blocks.RebornMachineBlock;
import reborncore.common.util.Torus;
import techreborn.client.EGui;
import techreborn.utils.TechRebornCreativeTab;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;
import techreborn.utils.damageSources.FusionDamageSource;

import java.util.List;

public class BlockFusionControlComputer extends RebornMachineBlock {

	public BlockFusionControlComputer() {
		super();
		setCreativeTab(TechRebornCreativeTab.instance);
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/generators"));
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
									final EntityPlayer player, final EnumHand hand, final EnumFacing side,
									final float hitX, final float hitY, final float hitZ) {
		final TileFusionControlComputer tileFusionControlComputer = (TileFusionControlComputer) world.getTileEntity(pos);
		if(!player.getHeldItem(hand).isEmpty() && player.getHeldItem(hand).getItem() == Item.getItemFromBlock(ModBlocks.FUSION_COIL)){
			List<BlockPos> coils = Torus.generate(tileFusionControlComputer.getPos(), tileFusionControlComputer.size);
			boolean placed = false;
			for(BlockPos coil : coils){
				if(player.getHeldItem(hand).isEmpty()){
					return true;
				}
				if(world.isAirBlock(coil) && !tileFusionControlComputer.isCoil(coil)){
					world.setBlockState(coil, ModBlocks.FUSION_COIL.getDefaultState());
					if(!player.isCreative()){
						player.getHeldItem(hand).shrink(1);
					}
					placed = true;
				}
			}
			if(placed){
				return true;
			}

		}
		tileFusionControlComputer.checkCoils();
		return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
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
	public TileEntity createNewTileEntity(final World world, final int meta) {
		return new TileFusionControlComputer();
	}
	
	@Override
	public boolean isAdvanced() {
		return true;
	}
}
