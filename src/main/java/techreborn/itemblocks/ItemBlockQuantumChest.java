/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.tiles.TileQuantumChest;

import java.util.List;

public class ItemBlockQuantumChest extends ItemBlock {

	public ItemBlockQuantumChest(Block p_i45328_1_) {
		super(p_i45328_1_);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List list, ITooltipFlag flag) {
		if (!stack.isEmpty() && stack.hasTagCompound()) {
			if (stack.getTagCompound().getCompoundTag("tileEntity") != null)
				list.add(stack.getTagCompound().getCompoundTag("tileEntity").getInteger("storedQuantity") + " items");
		}
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
	                            float hitX, float hitY, float hitZ, IBlockState newState) {
		if (!world.setBlockState(pos, newState)) {
			return false;
		}
		if (world.getBlockState(pos).getBlock() == block) {
			world.getBlockState(pos).getBlock().onBlockPlacedBy(world, pos, newState, player, stack);
			// world.getBlockState(pos).getBlock().onPostBlockPlaced(world, x,
			// y, z, metadata);
		}
		if (!stack.isEmpty() && stack.hasTagCompound()) {
			((TileQuantumChest) world.getTileEntity(pos))
				.readFromNBTWithoutCoords(stack.getTagCompound().getCompoundTag("tileEntity"));
		}
		return true;
	}
}
