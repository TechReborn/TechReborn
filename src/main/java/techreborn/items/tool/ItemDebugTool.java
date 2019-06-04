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

package techreborn.items.tool;

import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.IProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import reborncore.api.power.IEnergyInterfaceTile;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.TechReborn;

/**
 * Created by Mark on 20/03/2016.
 */
public class ItemDebugTool extends Item {

	public ItemDebugTool() {
		super(new Item.Properties().group(TechReborn.ITEMGROUP));
	}

	@Override
	public EnumActionResult onItemUse(ItemUseContext context) {
		IBlockState blockState = context.getWorld().getBlockState(context.getPos());
		Block block = blockState.getBlock();
		if (block == null) {
			return EnumActionResult.FAIL;
		}
		sendMessage(context, new TextComponentString(getRegistryName(block)));
		for (Entry<IProperty<?>, Comparable<?>> entry : blockState.getValues().entrySet()) {
			sendMessage(context, new TextComponentString(getPropertyString(entry)));
		}
		TileEntity tile = context.getWorld().getTileEntity(context.getPos());
		if (tile != null) {
			sendMessage(context, new TextComponentString(getTileEntityType(tile)));
			if (tile instanceof IEnergyInterfaceTile) {
				sendMessage(context, new TextComponentString(getRCPower((IEnergyInterfaceTile) tile)));
			} else {
				if (tile.getCapability(CapabilityEnergy.ENERGY, context.getPlacementHorizontalFacing()).isPresent()) {
					tile.getCapability(CapabilityEnergy.ENERGY, context.getPlacementHorizontalFacing()).ifPresent(consumer -> {
						sendMessage(context, new TextComponentString(getForgePower(consumer)));	
					});
				}
			}
		}
		return EnumActionResult.SUCCESS;
	}

	private void sendMessage(ItemUseContext context, TextComponentString string) {
		if (!context.getWorld().isRemote) {
			context.getPlayer().sendMessage(string);
		}
	}

	private String getPropertyString(Entry<IProperty<?>, Comparable<?>> entryIn) {
		IProperty<?> iproperty = entryIn.getKey();
		Comparable<?> comparable = entryIn.getValue();
		String s = Util.getValueName(iproperty, comparable);
		if (Boolean.TRUE.equals(comparable)) {
			s = TextFormatting.GREEN + s;
		} else if (Boolean.FALSE.equals(comparable)) {
			s = TextFormatting.RED + s;
		}

		return iproperty.getName() + ": " + s;
	}

	private String getRegistryName(Block block) {
		String s = "" + TextFormatting.GREEN;
		s += "Block Registry Name: ";
		s += TextFormatting.BLUE;
		s += block.getRegistryName().toString();

		return s;
	}
	
	private String getTileEntityType(TileEntity tile) {
		String s = "" + TextFormatting.GREEN;
		s += "Tile Entity: ";
		s += TextFormatting.BLUE;
		s += tile.getType().toString();

		return s;
	}
	
	private String getRCPower(IEnergyInterfaceTile tile) {
		String s = "" + TextFormatting.GREEN;
		s += "Power: ";
		s += TextFormatting.BLUE;
		s += PowerSystem.getLocaliszedPower(tile.getEnergy());
		s += "/";
		s += PowerSystem.getLocaliszedPower(tile.getMaxPower());
		
		return s;
	}
	
	private String getForgePower(IEnergyStorage cap) {
		String s = "" + TextFormatting.GREEN;
		s += "Power: ";
		s += TextFormatting.RED;
		s += cap.getEnergyStored();
		s += "/";
		s += cap.getMaxEnergyStored();
		s += " RF";
		
		return s;
	}
}
