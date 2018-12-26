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

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import reborncore.api.IListInfoProvider;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.RebornCoreConfig;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.TechReborn;

public class StackToolTipEvent {

	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void handleItemTooltipEvent(ItemTooltipEvent event) {
		if (event.getEntityPlayer() == null) {
			return;
		}
		Item item = event.getItemStack().getItem();
		if (item instanceof IListInfoProvider) {
			((IListInfoProvider) item).addInfo(event.getToolTip(), false, false);
		} else if (event.getItemStack().getItem() instanceof IEnergyItemInfo) {
			IEnergyStorage capEnergy = event.getItemStack().getCapability(CapabilityEnergy.ENERGY, null);
			event.getToolTip().add(1,
					TextFormatting.GOLD + PowerSystem.getLocaliszedPowerFormattedNoSuffix(capEnergy.getEnergyStored() / RebornCoreConfig.euPerFU)
					+ "/" + PowerSystem.getLocaliszedPowerFormattedNoSuffix(capEnergy.getMaxEnergyStored() / RebornCoreConfig.euPerFU)
					+ " " + PowerSystem.getDisplayPower().abbreviation);
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				int percentage = percentage(capEnergy.getMaxEnergyStored(), capEnergy.getEnergyStored());
				TextFormatting color;
				if (percentage <= 10) {
					color = TextFormatting.RED;
				} else if (percentage >= 75) {
					color = TextFormatting.GREEN;
				} else {
					color = TextFormatting.YELLOW;
				}
				event.getToolTip().add(2, color + "" + percentage + "%" + TextFormatting.GRAY + " Charged");
// TODO: show both input and output rates
				event.getToolTip().add(3,
						TextFormatting.GRAY + "I/O Rate: " 
						+ TextFormatting.GOLD
						+ PowerSystem.getLocaliszedPowerFormatted((int) ((IEnergyItemInfo) item).getMaxInput()));
			}
		}
		else {
			try {
				Block block = Block.getBlockFromItem(item);
				if (block != null && (block instanceof BlockContainer || block instanceof ITileEntityProvider)
					&& block.getRegistryName().getNamespace().contains("techreborn")) {
					TileEntity tile = block.createTileEntity(Minecraft.getMinecraft().world,
						block.getStateFromMeta(event.getItemStack().getItemDamage()));
					boolean hasData = false;
					if(event.getItemStack().hasTagCompound() && event.getItemStack().getTagCompound().hasKey("tile_data")){
						NBTTagCompound tileData = event.getItemStack().getTagCompound().getCompoundTag("tile_data");
						tile.readFromNBT(tileData);
						hasData = true;
						event.getToolTip().add(TextFormatting.DARK_GREEN + "Block data contained");
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
