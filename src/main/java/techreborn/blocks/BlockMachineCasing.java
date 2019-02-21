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

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import reborncore.common.multiblock.BlockMultiblockBase;
import techreborn.tiles.TileMachineCasing;

public class BlockMachineCasing extends BlockMultiblockBase {

	public final int heatCapacity;

	public BlockMachineCasing(int heatCapacity) {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(2f).sound(SoundType.METAL));
		this.heatCapacity = heatCapacity;
	}

	public static int getHeatFromState(IBlockState state) {
		Block block = state.getBlock();
		if (!(block instanceof BlockMachineCasing)) {
			return 0;
		}
		BlockMachineCasing casing = (BlockMachineCasing) block;
		return casing.heatCapacity;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileMachineCasing();
	}

}
