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
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;



import org.lwjgl.glfw.GLFW;
import reborncore.api.IListInfoProvider;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.RebornCoreConfig;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.ItemPowerManager;
import reborncore.common.util.StringUtils;
import techreborn.TechReborn;

public class StackToolTipEvent {

	@SuppressWarnings("deprecation")
	@Environment(EnvType.CLIENT)
	@SubscribeEvent
	public void handleItemTooltipEvent(ItemTooltipEvent event) {
		if (event.getEntityPlayer() == null) {
			return;
		}
		Item item = event.getItemStack().getItem();
		if (item instanceof IListInfoProvider) {
			((IListInfoProvider) item).addInfo(event.getToolTip(), false, false);
		} else if (event.getItemStack().getItem() instanceof IEnergyItemInfo) {
			IEnergyStorage capEnergy = new ItemPowerManager(event.getItemStack());
			TextComponent line1 = new TextComponent(PowerSystem
					.getLocaliszedPowerFormattedNoSuffix(capEnergy.getEnergyStored() / RebornCoreConfig.euPerFU));
			line1.append("/");
			line1.append(PowerSystem
					.getLocaliszedPowerFormattedNoSuffix(capEnergy.getMaxEnergyStored() / RebornCoreConfig.euPerFU));
			line1.append(" ");
			line1.append(PowerSystem.getDisplayPower().abbreviation);
			line1.applyFormat(ChatFormat.GOLD);

			event.getToolTip().add(1, line1);

			if (InputUtil.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)
					|| InputUtil.isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
				int percentage = percentage(capEnergy.getMaxEnergyStored(), capEnergy.getEnergyStored());
				ChatFormat color = StringUtils.getPercentageColour(percentage);
				event.getToolTip().add(2,
						new TextComponent(color + "" + percentage + "%" + ChatFormat.GRAY + " Charged"));
// TODO: show both input and output rates
				event.getToolTip().add(3, new TextComponent(ChatFormat.GRAY + "I/O Rate: "
						+ ChatFormat.GOLD
						+ PowerSystem.getLocaliszedPowerFormatted((int) ((IEnergyItemInfo) item).getMaxInput())));
			}
		} else {
			try {
				Block block = Block.getBlockFromItem(item);
				if (block != null && (block instanceof BlockWithEntity || block instanceof BlockEntityProvider)
						&& block.getRegistryName().getNamespace().contains("techreborn")) {
					BlockEntity tile = block.createTileEntity(block.getDefaultState(), MinecraftClient.getInstance().world);
					boolean hasData = false;
					if (event.getItemStack().hasTag() && event.getItemStack().getTag().contains("tile_data")) {
						CompoundTag tileData = event.getItemStack().getTag().getCompound("tile_data");
						tile.fromTag(tileData);
						hasData = true;
						event.getToolTip()
								.add(new TextComponent("Block data contained").applyFormat(ChatFormat.DARK_GREEN));
					}
					if (tile instanceof IListInfoProvider) {
						((IListInfoProvider) tile).addInfo(event.getToolTip(), false, hasData);
					}
				}
			} catch (NullPointerException e) {
				TechReborn.LOGGER.debug("Failed to load info for " + event.getItemStack().getDisplayName());
			}
		}
	}

	public int percentage(int MaxValue, int CurrentValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}

}
