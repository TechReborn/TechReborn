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

package techreborn.events;

import net.minecraft.ChatFormat;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.registry.Registry;
import reborncore.api.IListInfoProvider;
import reborncore.api.events.ItemTooltipCallback;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.api.power.ItemPowerManager;
import reborncore.common.RebornCoreConfig;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import techreborn.TechReborn;

import java.util.List;

public class StackToolTipHandler implements ItemTooltipCallback {

	public static void setup() {
		ItemTooltipCallback.EVENT.register(new StackToolTipHandler());
	}

	@Override
	public void getTooltip(ItemStack stack, TooltipContext tooltipContext, List<Component> components) {
		Item item = stack.getItem();
		if (item instanceof IListInfoProvider) {
			((IListInfoProvider) item).addInfo(components, false, false);
		} else if (stack.getItem() instanceof IEnergyItemInfo) {
			ItemPowerManager itemPowerManager = new ItemPowerManager(stack);
			TextComponent line1 = new TextComponent(PowerSystem.getLocaliszedPowerFormattedNoSuffix(itemPowerManager.getEnergyStored() / RebornCoreConfig.euPerFU));
			line1.append("/");
			line1.append(PowerSystem.getLocaliszedPowerFormattedNoSuffix(itemPowerManager.getMaxEnergyStored() / RebornCoreConfig.euPerFU));
			line1.append(" ");
			line1.append(PowerSystem.getDisplayPower().abbreviation);
			line1.applyFormat(ChatFormat.GOLD);

			components.add(1, line1);

			if (Screen.hasShiftDown()) {
				int percentage = percentage(itemPowerManager.getMaxEnergyStored(), itemPowerManager.getEnergyStored());
				ChatFormat color = StringUtils.getPercentageColour(percentage);
				components.add(2, new TextComponent(color + "" + percentage + "%" + ChatFormat.GRAY + " Charged"));
				// TODO: show both input and output rates
				components.add(3, new TextComponent(ChatFormat.GRAY + "I/O Rate: " + ChatFormat.GOLD + PowerSystem.getLocaliszedPowerFormatted(((IEnergyItemInfo) item).getMaxInput())));
			}
		} else {
			try {
				Block block = Block.getBlockFromItem(item);
				if (block != null && (block instanceof BlockWithEntity || block instanceof BlockEntityProvider) && Registry.BLOCK.getId(block).getNamespace().contains("techreborn")) {
					BlockEntity tile = ((BlockEntityProvider) block).createBlockEntity(MinecraftClient.getInstance().world);
					boolean hasData = false;
					if (stack.hasTag() && stack.getTag().containsKey("tile_data")) {
						CompoundTag tileData = stack.getTag().getCompound("tile_data");
						tile.fromTag(tileData);
						hasData = true;
						components.add(new TextComponent("Block data contained").applyFormat(ChatFormat.DARK_GREEN));
					}
					if (tile instanceof IListInfoProvider) {
						((IListInfoProvider) tile).addInfo(components, false, hasData);
					}
				}
			} catch (NullPointerException e) {
				TechReborn.LOGGER.debug("Failed to load info for " + stack.getCustomName());
			}
		}
	}

	public int percentage(int MaxValue, int CurrentValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}

}
