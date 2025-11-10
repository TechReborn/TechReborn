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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import reborncore.RebornCore;
import reborncore.api.IListInfoProvider;
import reborncore.common.BaseBlock;
import reborncore.common.BaseBlockEntityProvider;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StackToolTipHandler implements ItemTooltipCallback {

	@Override
	public void getTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipFlag tooltipType, List<Component> tooltipLines) {
		Item item = itemStack.getItem();
		Block block = Block.byItem(item);
		if (block instanceof BaseBlock baseBlock) {
			List<Component> list = new ArrayList<>();
			baseBlock.appendTooltip(itemStack, tooltipContext, list, tooltipType);
			tooltipLines.addAll(1, list);
		}

		if (item instanceof IListInfoProvider) {
			((IListInfoProvider) item).addInfo(tooltipLines, false, false);
		}
		else if (item instanceof RcEnergyItem energyItem) {
			MutableComponent line1 = Component.literal(PowerSystem.getLocalizedPowerNoSuffix(energyItem.getStoredEnergy(itemStack)));
			line1.append("/");
			line1.append(PowerSystem.getLocalizedPower(energyItem.getEnergyCapacity(itemStack)));
			line1.withStyle(ChatFormatting.GOLD);

			tooltipLines.add(1, line1);

			if (Screen.hasShiftDown()) {
				int percentage = percentage(energyItem.getStoredEnergy(itemStack), energyItem.getEnergyCapacity(itemStack));
				MutableComponent line2  = StringUtils.getPercentageText(percentage);
				line2.append(" ");
				line2.withStyle(ChatFormatting.GRAY);
				line2.append(I18n.get("reborncore.gui.tooltip.power_charged"));
				tooltipLines.add(2, line2);

				double inputRate = energyItem.getEnergyMaxInput(itemStack);
				double outputRate = energyItem.getEnergyMaxOutput(itemStack);

				MutableComponent line3 = Component.literal("");
				if (inputRate != 0 && inputRate == outputRate){
					line3.append(I18n.get("techreborn.tooltip.transferRate"));
					line3.append(" : ");
					line3.withStyle(ChatFormatting.GRAY);
					line3.append(PowerSystem.getLocalizedPower(inputRate));
					line3.withStyle(ChatFormatting.GOLD);
				}
				else if(inputRate != 0){
					line3.append(I18n.get("reborncore.tooltip.energy.inputRate"));
					line3.append(" : ");
					line3.withStyle(ChatFormatting.GRAY);
					line3.append(PowerSystem.getLocalizedPower(inputRate));
					line3.withStyle(ChatFormatting.GOLD);
				}
				else if (outputRate !=0){
					line3.append(I18n.get("reborncore.tooltip.energy.outputRate"));
					line3.append(" : ");
					line3.withStyle(ChatFormatting.GRAY);
					line3.append(PowerSystem.getLocalizedPower(outputRate));
					line3.withStyle(ChatFormatting.GOLD);
				}
				tooltipLines.add(3, line3);
			}
		}
		else {
			try {
				if ((block instanceof BaseBlockEntityProvider)) {
					BlockEntity blockEntity = ((EntityBlock) block).newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
					boolean hasData = false;
					TypedEntityData<BlockEntityType<?>> nbtComponent = itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
					if (nbtComponent != null) {
						nbtComponent.loadInto(blockEntity, Minecraft.getInstance().level.registryAccess());
						hasData = true;
						tooltipLines.add(Component.literal(I18n.get("reborncore.tooltip.has_data")).withStyle(ChatFormatting.DARK_GREEN));
					}
					if (blockEntity instanceof IListInfoProvider) {
						((IListInfoProvider) blockEntity).addInfo(tooltipLines, false, hasData);
					}
				}
			} catch (NullPointerException e) {
				RebornCore.LOGGER.debug("Failed to load info for " + itemStack.getHoverName());
			}
		}
	}

	private int percentage(double CurrentValue, double MaxValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}
}
