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

package techreborn.items.tools;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import reborncore.api.power.IEnergyInterfaceTile;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.items.ItemTR;

/**
 * Created by Mark on 20/03/2016.
 */
public class ItemDebugTool extends ItemTR {

	public ItemDebugTool() {
		setTranslationKey("techreborn.debug");
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos,
	                                  EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos); 
		Block block = state.getBlock();
		if (block == null) {
			return EnumActionResult.PASS;	
		}
		if (!worldIn.isRemote) {
			sendMessage(playerIn, "Block", block.getTranslationKey());
			sendMessage(playerIn, "State", state.toString());
		}
		
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile == null) {
			return EnumActionResult.SUCCESS;
		}
		if (tile instanceof IEnergyInterfaceTile) {
			if (!tile.getWorld().isRemote) {
				sendMessage(playerIn, "Power", PowerSystem.getLocaliszedPower(((IEnergyInterfaceTile) tile).getEnergy()));
			}
		} else if (tile.hasCapability(CapabilityEnergy.ENERGY, facing)) {
			if (!tile.getWorld().isRemote) {
				sendMessage(playerIn, "Power", PowerSystem.getLocaliszedPower(((IEnergyInterfaceTile) tile).getEnergy()) + "FE");
			}
		}
		return EnumActionResult.SUCCESS;	
	}
	
	private void sendMessage(EntityPlayer playerIn, String key, String value) {
		playerIn.sendMessage(new TextComponentString(TextFormatting.GREEN + key + ": " + TextFormatting.BLUE + value));
		
	}
}
