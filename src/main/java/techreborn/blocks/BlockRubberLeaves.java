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

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import techreborn.Core;
import techreborn.utils.TechRebornCreativeTab;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

import java.util.List;
import java.util.Random;

public class BlockRubberLeaves extends BlockLeaves {

	public BlockRubberLeaves() {
		super();
		setCreativeTab(TechRebornCreativeTab.instance);
		this.setDefaultState(this.getDefaultState().withProperty(CHECK_DECAY, true)
			.withProperty(DECAYABLE, true));
		Blocks.FIRE.setFireInfo(this, 30, 60);
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, CHECK_DECAY, DECAYABLE));
	}

	@Override
	public BlockPlanks.EnumType getWoodType(int meta) {
		return null;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		List<ItemStack> list = new java.util.ArrayList<>();
		list.add(new ItemStack(this, 1, 0));
		return list;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		if(!fancyLeaves()){
			return super.getRenderLayer();
		}
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		if(!fancyLeaves()){
			return super.isOpaqueCube(state);
		}
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { CHECK_DECAY, DECAYABLE });
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(DECAYABLE, (meta & 1) == 0)
			.withProperty(CHECK_DECAY, (meta & 2) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		if (!state.getValue(DECAYABLE)) {
			meta |= 1;
		}
		if (state.getValue(CHECK_DECAY)) {
			meta |= 2;
		}
		return meta;
	}

	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		return 16777215;
	}

	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state) {
		return 16777215;
	}

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		return 16777215;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(ModBlocks.RUBBER_SAPLING);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		if(!fancyLeaves()){
			return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
		}
		return true;
	}

	public boolean fancyLeaves(){
		return Core.proxy.fancyGraphics();
	}
}
