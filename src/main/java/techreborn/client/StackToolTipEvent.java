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

package techreborn.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import reborncore.api.IListInfoProvider;
import reborncore.api.power.IEnergyInterfaceItem;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.Core;

public class StackToolTipEvent {

	@SubscribeEvent
	public void handleItemTooltipEvent(ItemTooltipEvent event) {
		if (event.getItemStack().getItem() instanceof IListInfoProvider) {
			((IListInfoProvider) event.getItemStack().getItem()).addInfo(event.getToolTip(), false);
		} else if (event.getItemStack().getItem() instanceof IEnergyInterfaceItem) {
			event.getToolTip().add(1, TextFormatting.GOLD + PowerSystem.getLocaliszedPowerFormattedNoSuffix(
				(int) ((IEnergyInterfaceItem) event.getItemStack().getItem()).getEnergy(event.getItemStack()))
				+ "/" + PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) ((IEnergyInterfaceItem) event.getItemStack().getItem()).getMaxPower(event.getItemStack()))
				+ " " + PowerSystem.getDisplayPower().abbreviation);
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				int percentage = percentage(
					(int) ((IEnergyInterfaceItem) event.getItemStack().getItem()).getMaxPower(event.getItemStack()),
					(int) ((IEnergyInterfaceItem) event.getItemStack().getItem()).getEnergy(event.getItemStack()));
				TextFormatting color;
				if (percentage <= 10) {
					color = TextFormatting.RED;
				} else if (percentage >= 75) {
					color = TextFormatting.GREEN;
				} else {
					color = TextFormatting.YELLOW;
				}
				event.getToolTip().add(2, color + "" + percentage + "%" + TextFormatting.GRAY + " Charged");
				event.getToolTip().add(3, TextFormatting.GRAY + "I/O Rate: " + TextFormatting.GOLD + PowerSystem.getLocaliszedPowerFormatted((int) ((IEnergyInterfaceItem) event.getItemStack().getItem()).getMaxTransfer(event.getItemStack())));
			}
		} else {
			try {
				Block block = Block.getBlockFromItem(event.getItemStack().getItem());
				if (block != null && (block instanceof BlockContainer || block instanceof ITileEntityProvider)
					&& block.getRegistryName().getResourceDomain().contains("techreborn")) {
					TileEntity tile = block.createTileEntity(Minecraft.getMinecraft().world,
						block.getStateFromMeta(event.getItemStack().getItemDamage()));
					if (tile instanceof IListInfoProvider) {
						((IListInfoProvider) tile).addInfo(event.getToolTip(), false);
					}
				}
			} catch (NullPointerException e) {
				Core.logHelper.debug("Failed to load info for " + event.getItemStack().getDisplayName());
			}
		}
	}

	public int percentage(int MaxValue, int CurrentValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}

}
