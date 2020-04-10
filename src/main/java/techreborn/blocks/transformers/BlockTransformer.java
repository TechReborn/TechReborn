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

package techreborn.blocks.transformers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.common.blocks.RebornOrientableTileBlock;
import techreborn.lib.ModInfo;
import techreborn.utils.TechRebornCreativeTab;

/**
 * Created by Rushmead
 */
public abstract class BlockTransformer extends RebornOrientableTileBlock {
	public String name;

	public BlockTransformer(String name) {
		super();
		setCreativeTab(TechRebornCreativeTab.instance);
		this.name = name;
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/energy"));
	}

	// RebornOrientableTileBlock
	@Override
	protected EnumFacing getFacingForPlacement(World worldIn, BlockPos pos, EntityLivingBase placer) {
		if (worldIn.isRemote) {
			return EnumFacing.NORTH;
		}
			IBlockState state0 = worldIn.getBlockState(pos.north());
			IBlockState state1 = worldIn.getBlockState(pos.south());
			IBlockState state2 = worldIn.getBlockState(pos.west());
			IBlockState state3 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = getFacing(worldIn, pos);

			if (enumfacing == EnumFacing.NORTH && state0.isFullBlock() && !state1.isFullBlock()) {
				enumfacing = EnumFacing.SOUTH;
			} else if (enumfacing == EnumFacing.SOUTH && state1.isFullBlock() && !state0.isFullBlock()) {
				enumfacing = EnumFacing.NORTH;
			} else if (enumfacing == EnumFacing.WEST && state2.isFullBlock() && !state3.isFullBlock()) {
				enumfacing = EnumFacing.EAST;
			} else if (enumfacing == EnumFacing.EAST && state3.isFullBlock() && !state2.isFullBlock()) {
				enumfacing = EnumFacing.WEST;
			}
			
			return enumfacing;
	}

	public EnumFacing getSideFromint(int i) {
		if (i == 0) {
			return EnumFacing.NORTH;
		} else if (i == 1) {
			return EnumFacing.SOUTH;
		} else if (i == 2) {
			return EnumFacing.EAST;
		} else if (i == 3) {
			return EnumFacing.WEST;
		} else if (i == 4) {
			return EnumFacing.UP;
		} else if (i == 5) {
			return EnumFacing.DOWN;
		}
		return EnumFacing.NORTH;
	}

	public int getSideFromEnum(EnumFacing facing) {
		if (facing == EnumFacing.NORTH) {
			return 0;
		} else if (facing == EnumFacing.SOUTH) {
			return 1;
		} else if (facing == EnumFacing.EAST) {
			return 2;
		} else if (facing == EnumFacing.WEST) {
			return 3;
		} else if (facing == EnumFacing.UP) {
			return 4;
		} else if (facing == EnumFacing.DOWN) {
			return 5;
		}
		return 0;
	}
}
