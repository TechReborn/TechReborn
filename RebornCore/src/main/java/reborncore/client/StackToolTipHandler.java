/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.client;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import reborncore.RebornCore;
import reborncore.api.IListInfoProvider;
import reborncore.common.BaseBlockEntityProvider;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergySide;

import java.util.List;

public class StackToolTipHandler implements ItemTooltipCallback {

	@Override
	public void getTooltip(ItemStack itemStack, TooltipContext tooltipContext, List<Text> tooltipLines) {
		Item item = itemStack.getItem();
		Block block = Block.getBlockFromItem(item);

		if (item instanceof IListInfoProvider) {
			((IListInfoProvider) item).addInfo(tooltipLines, false, false);
		}
		else if (item instanceof EnergyHolder) {
			LiteralText line1 = new LiteralText(PowerSystem.getLocalizedPowerNoSuffix(Energy.of(itemStack).getEnergy()));
			line1.append("/");
			line1.append(PowerSystem.getLocalizedPower(Energy.of(itemStack).getMaxStored()));
			line1.formatted(Formatting.GOLD);

			tooltipLines.add(1, line1);

			if (Screen.hasShiftDown()) {
				int percentage = percentage(Energy.of(itemStack).getMaxStored(), Energy.of(itemStack).getEnergy());
				MutableText line2  = StringUtils.getPercentageText(percentage);
				line2.append(" ");
				line2.formatted(Formatting.GRAY);
				line2.append(I18n.translate("reborncore.gui.tooltip.power_charged"));
				tooltipLines.add(2, line2);

				double inputRate = ((EnergyHolder) item).getMaxInput(EnergySide.UNKNOWN);
				double outputRate = ((EnergyHolder) item).getMaxOutput(EnergySide.UNKNOWN);
				LiteralText line3 = new LiteralText("");
				if (inputRate != 0 && inputRate == outputRate){
					line3.append(I18n.translate("techreborn.tooltip.transferRate"));
					line3.append(" : ");
					line3.formatted(Formatting.GRAY);
					line3.append(PowerSystem.getLocalizedPower(inputRate));
					line3.formatted(Formatting.GOLD);
				}
				else if(inputRate != 0){
					line3.append(I18n.translate("reborncore.tooltip.energy.inputRate"));
					line3.append(" : ");
					line3.formatted(Formatting.GRAY);
					line3.append(PowerSystem.getLocalizedPower(inputRate));
					line3.formatted(Formatting.GOLD);
				}
				else if (outputRate !=0){
					line3.append(I18n.translate("reborncore.tooltip.energy.outputRate"));
					line3.append(" : ");
					line3.formatted(Formatting.GRAY);
					line3.append(PowerSystem.getLocalizedPower(outputRate));
					line3.formatted(Formatting.GOLD);
				}
				tooltipLines.add(3, line3);
			}
		}
		else {
			try {
				if ((block instanceof BaseBlockEntityProvider)) {
					BlockEntity blockEntity = ((BlockEntityProvider) block).createBlockEntity(BlockPos.ORIGIN, block.getDefaultState());
					boolean hasData = false;
					if (itemStack.hasTag() && itemStack.getOrCreateTag().contains("blockEntity_data")) {
						NbtCompound blockEntityData = itemStack.getOrCreateTag().getCompound("blockEntity_data");
						if (blockEntity != null) {
							blockEntity.readNbt(blockEntityData);
							hasData = true;
							tooltipLines.add(new LiteralText(I18n.translate("reborncore.tooltip.has_data")).formatted(Formatting.DARK_GREEN));
						}
					}
					if (blockEntity instanceof IListInfoProvider) {
						((IListInfoProvider) blockEntity).addInfo(tooltipLines, false, hasData);
					}
				}
			} catch (NullPointerException e) {
				RebornCore.LOGGER.debug("Failed to load info for " + itemStack.getName());
			}
		}
	}

	private int percentage(double CurrentValue, double MaxValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}
}
