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
import net.minecraft.ChatFormat;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.SystemUtil;

import net.minecraft.util.registry.Registry;
import reborncore.api.power.IEnergyInterfaceTile;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.TechReborn;

/**
 * Created by Mark on 20/03/2016.
 */
public class ItemDebugTool extends Item {

	public ItemDebugTool() {
		super(new Item.Settings().itemGroup(TechReborn.ITEMGROUP));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockState blockState = context.getWorld().getBlockState(context.getBlockPos());
		Block block = blockState.getBlock();
		if (block == null) {
			return ActionResult.FAIL;
		}
		sendMessage(context, new TextComponent(getRegistryName(block)));
		for (Entry<Property<?>, Comparable<?>> entry : blockState.getEntries().entrySet()) {
			sendMessage(context, new TextComponent(getPropertyString(entry)));
		}
		BlockEntity tile = context.getWorld().getBlockEntity(context.getBlockPos());
		if (tile != null) {
			sendMessage(context, new TextComponent(getTileEntityType(tile)));
			if (tile instanceof IEnergyInterfaceTile) {
				sendMessage(context, new TextComponent(getRCPower((IEnergyInterfaceTile) tile)));
			}
		}
		return ActionResult.SUCCESS;
	}

	private void sendMessage(ItemUsageContext context, TextComponent string) {
		if (!context.getWorld().isClient) {
			context.getPlayer().sendMessage(string);
		}
	}

	private String getPropertyString(Entry<Property<?>, Comparable<?>> entryIn) {
		Property<?> iproperty = entryIn.getKey();
		Comparable<?> comparable = entryIn.getValue();
		String s = SystemUtil.getValueAsString(iproperty, comparable);
		if (Boolean.TRUE.equals(comparable)) {
			s = ChatFormat.GREEN + s;
		} else if (Boolean.FALSE.equals(comparable)) {
			s = ChatFormat.RED + s;
		}

		return iproperty.getName() + ": " + s;
	}

	private String getRegistryName(Block block) {
		String s = "" + ChatFormat.GREEN;
		s += "Block Registry Name: ";
		s += ChatFormat.BLUE;
		s += Registry.BLOCK.getId(block);

		return s;
	}
	
	private String getTileEntityType(BlockEntity tile) {
		String s = "" + ChatFormat.GREEN;
		s += "Tile Entity: ";
		s += ChatFormat.BLUE;
		s += tile.getType().toString();

		return s;
	}
	
	private String getRCPower(IEnergyInterfaceTile tile) {
		String s = "" + ChatFormat.GREEN;
		s += "Power: ";
		s += ChatFormat.BLUE;
		s += PowerSystem.getLocaliszedPower(tile.getEnergy());
		s += "/";
		s += PowerSystem.getLocaliszedPower(tile.getMaxPower());
		
		return s;
	}
}
