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

import net.fabricmc.fabric.api.event.client.ItemTooltipCallback;
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
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import reborncore.api.IListInfoProvider;
import reborncore.common.BaseBlockEntityProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergySide;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.items.ItemUpgrade;
import techreborn.utils.DocumentAssistUtils;

import java.util.List;

public class StackToolTipHandler implements ItemTooltipCallback {

	public static void setup() {
		ItemTooltipCallback.EVENT.register(new StackToolTipHandler());
	}

	@Override
	public void getTooltip(ItemStack stack, TooltipContext tooltipContext, List<Text> components) {
		Item item = stack.getItem();


		// Machine info and upgrades helper section
		Block block = Block.getBlockFromItem(item);

		if(block instanceof BaseBlockEntityProvider){
			DocumentAssistUtils.addInfo(item.getTranslationKey(), components);
		}

		if(item instanceof ItemUpgrade){
			ItemUpgrade upgrade = (ItemUpgrade)item;

			DocumentAssistUtils.addInfo(item.getTranslationKey(), components, false);
			components.addAll(DocumentAssistUtils.getUpgradeStats(TRContent.Upgrades.valueOf(upgrade.name.toUpperCase()), stack.getCount(), Screen.hasShiftDown()));
		}


		// Other section
		if (item instanceof IListInfoProvider) {
			((IListInfoProvider) item).addInfo(components, false, false);
		} else if (stack.getItem() instanceof EnergyHolder) {
			LiteralText line1 = new LiteralText(PowerSystem.getLocaliszedPowerFormattedNoSuffix(Energy.of(stack).getEnergy()));
			line1.append("/");
			line1.append(PowerSystem.getLocaliszedPowerFormattedNoSuffix(Energy.of(stack).getMaxStored()));
			line1.append(" ");
			line1.append(PowerSystem.getDisplayPower().abbreviation);
			line1.formatted(Formatting.GOLD);

			components.add(1, line1);

			if (Screen.hasShiftDown()) {
				int percentage = percentage(Energy.of(stack).getMaxStored(), Energy.of(stack).getEnergy());
				Formatting color = StringUtils.getPercentageColour(percentage);
				components.add(2, new LiteralText(color + "" + percentage + "%" + Formatting.GRAY + " Charged"));
				// TODO: show both input and output rates
				components.add(3, new LiteralText(Formatting.GRAY + "I/O Rate: " + Formatting.GOLD + PowerSystem.getLocaliszedPowerFormatted(((EnergyHolder) item).getMaxInput(EnergySide.UNKNOWN))));
			}
		} else {
			try {
				if ((block instanceof BlockEntityProvider) && Registry.BLOCK.getId(block).getNamespace().contains("techreborn")) {
					BlockEntity blockEntity = ((BlockEntityProvider) block).createBlockEntity(MinecraftClient.getInstance().world);
					boolean hasData = false;
					if (stack.hasTag() && stack.getTag().contains("blockEntity_data")) {
						CompoundTag blockEntityData = stack.getTag().getCompound("blockEntity_data");
						blockEntity.fromTag(blockEntityData);
						hasData = true;
						components.add(new LiteralText("Block data contained").formatted(Formatting.DARK_GREEN));
					}
					if (blockEntity instanceof IListInfoProvider) {
						((IListInfoProvider) blockEntity).addInfo(components, false, hasData);
					}
				}
			} catch (NullPointerException e) {
				TechReborn.LOGGER.debug("Failed to load info for " + stack.getName());
			}
		}
	}

	public int percentage(int MaxValue, int CurrentValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}

	public int percentage(double MaxValue, double CurrentValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}

}
