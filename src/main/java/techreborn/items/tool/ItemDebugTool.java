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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
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
		Block block = context.getWorld().getBlockState(context.getPos()).getBlock();
		if (block != null) {
			sendMessage(context, new TextComponentString(TextFormatting.GREEN + "Block Registry Name: "
					+ TextFormatting.BLUE + block.getRegistryName().toString()));
		} else {
			return EnumActionResult.FAIL;
		}

		TileEntity tile = context.getWorld().getTileEntity(context.getPos());
		if (tile != null) {
			sendMessage(context, new TextComponentString(
					TextFormatting.GREEN + "Tile Entity: " + TextFormatting.BLUE + tile.getType().toString()));
		}
		if (tile instanceof IEnergyInterfaceTile) {
			sendMessage(context, new TextComponentString(TextFormatting.GREEN + "Power: "
					+ TextFormatting.BLUE + PowerSystem.getLocaliszedPower(((IEnergyInterfaceTile) tile).getEnergy())));
		} else if (tile.getCapability(CapabilityEnergy.ENERGY, context.getPlacementHorizontalFacing()) != null) {
			sendMessage(context, new TextComponentString(TextFormatting.GREEN + "Power " + TextFormatting.RED
					+ ((IEnergyStorage) tile.getCapability(CapabilityEnergy.ENERGY, context.getPlacementHorizontalFacing())).getEnergyStored() + "FU"));
		}
		return EnumActionResult.SUCCESS;
	}

	private void sendMessage(ItemUseContext context, TextComponentString string) {
		if (!context.getWorld().isRemote) {
			context.getPlayer().sendMessage(string);
		}
	}
}
