/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import reborncore.common.BaseBlockEntityProvider;
import techreborn.init.TRContent;
import techreborn.items.UpgradeItem;
import techreborn.utils.ToolTipAssistUtils;

import java.util.List;
import java.util.Map;

public class StackToolTipHandler implements ItemTooltipCallback {

	public static final Map<Item, Boolean> ITEM_ID = Maps.newHashMap();

	public static void setup() {
		ItemTooltipCallback.EVENT.register(new StackToolTipHandler());
	}

	@Override
	public void getTooltip(ItemStack stack, TooltipContext tooltipContext, List<Text> tooltipLines) {
		Item item = stack.getItem();

		if (!ITEM_ID.computeIfAbsent(item, this::isTRItem))
			return;

		// Machine info and upgrades helper section
		Block block = Block.getBlockFromItem(item);

		if (block instanceof BaseBlockEntityProvider) {
			ToolTipAssistUtils.addInfo(item.getTranslationKey(), tooltipLines);
		}

		if (item instanceof UpgradeItem) {
			UpgradeItem upgrade = (UpgradeItem) item;

			ToolTipAssistUtils.addInfo(item.getTranslationKey(), tooltipLines, false);
			tooltipLines.addAll(ToolTipAssistUtils.getUpgradeStats(TRContent.Upgrades.valueOf(upgrade.name.toUpperCase()), stack.getCount(), Screen.hasShiftDown()));
		}
	}

	private boolean isTRItem(Item item) {
		return Registry.ITEM.getId(item).getNamespace().equals("techreborn");
	}
}
