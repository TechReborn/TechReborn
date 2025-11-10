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

package techreborn.client.events;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import reborncore.api.IListInfoProvider;
import reborncore.common.BaseBlockEntityProvider;
import techreborn.blocks.cable.CableBlock;
import techreborn.events.OreDepthSyncHandler;
import techreborn.init.TRContent;
import techreborn.items.DynamicCellItem;
import techreborn.items.UpgradeItem;
import techreborn.items.armor.NanoSuitItem;
import techreborn.items.armor.QuantumSuitItem;
import techreborn.world.OreDepth;
import techreborn.world.TargetDimension;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StackToolTipHandler implements ItemTooltipCallback {

	public static final Map<Item, Boolean> ITEM_ID = Maps.newHashMap();
	private static final List<Block> UNOBTAINABLE_ORES = Lists.newLinkedList();

	public static void setup() {
		ItemTooltipCallback.EVENT.register(new StackToolTipHandler());

		for (TRContent.Ores ore : TRContent.Ores.values()) {
			if (ore.isDeepslate()) {
				TRContent.Ores normal = ore.getUnDeepslate();
				if (normal.distribution != null && normal.distribution.dimension != TargetDimension.OVERWORLD)
					UNOBTAINABLE_ORES.add(ore.block);
			}
		}
	}

	@Override
	public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag tooltipType, List<Component> lines) {
		Item item = stack.getItem();

		// Can currently be executed by a ForkJoinPool.commonPool-worker when REI is in async search mode
		// We skip this method until a thread-safe solution is in place
		Minecraft mc = Minecraft.getInstance();
		if (!mc.isSameThread())
			return;

		if (!ITEM_ID.computeIfAbsent(item, StackToolTipHandler::isTRItem))
			return;

		// Machine info and upgrades helper section
		Block block = Block.byItem(item);

		if (block instanceof BaseBlockEntityProvider) {
			ToolTipAssistUtils.addInfo(item.getDescriptionId(), lines);
		}

		if (block instanceof CableBlock cable) {
			BlockEntity blockEntity = cable.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
			if (blockEntity != null) {
				((IListInfoProvider) blockEntity).addInfo(lines, false, false);
			}
		}

		if (item instanceof UpgradeItem upgrade) {
			ToolTipAssistUtils.addInfo(item.getDescriptionId(), lines, false);
			lines.addAll(ToolTipAssistUtils.getUpgradeStats(TRContent.Upgrades.fromItem(upgrade), stack.getCount(), mc.hasShiftDown()));
		}

		if (item instanceof DynamicCellItem cell) {
			Fluid fluid = cell.getFluid(stack);
			if (!(fluid instanceof FlowingFluid) && fluid != Fluids.EMPTY)
				ToolTipAssistUtils.addInfo("unplaceable_fluid", lines, false);
		}

		if (item == TRContent.Upgrades.SUPERCONDUCTOR.item && mc.hasControlDown()) {
			lines.add(Component.literal(ChatFormatting.GOLD + "Blame obstinate_3 for this"));
		}

		if (item == TRContent.OMNI_TOOL) {
			lines.add(Component.literal(ChatFormatting.YELLOW + I18n.get("techreborn.tooltip.omnitool_motto")));
		}

		if (block == TRContent.Machine.INDUSTRIAL_CENTRIFUGE.block && mc.hasControlDown()) {
			lines.add(Component.literal("Round and round it goes"));
		}

		if (UNOBTAINABLE_ORES.contains(block)) {
			lines.add(Component.translatable("techreborn.tooltip.unobtainable").withStyle(ChatFormatting.AQUA));
		} else if (OreDepthSyncHandler.getOreDepthMap().containsKey(block)) {
			OreDepth oreDepth = OreDepthSyncHandler.getOreDepthMap().get(block);
			Component text = getOreDepthText(oreDepth);
			lines.add(text.copy().withStyle(ChatFormatting.AQUA));
		}

		if (item instanceof NanoSuitItem suit) {
			suit.appendArmorTooltip(stack, lines, mc.hasShiftDown());
		} else if (item instanceof QuantumSuitItem suit) {
			suit.appendArmorTooltip(stack, lines, mc.hasShiftDown());
		}
	}

	private static boolean isTRItem(Item item) {
		return BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("techreborn");
	}

	private static Component getOreDepthText(OreDepth depth) {
		return Component.translatable("techreborn.tooltip.ores.%s".formatted(depth.dimension().name().toLowerCase(Locale.ROOT)),
				Component.literal(String.valueOf(depth.minY())).withStyle(ChatFormatting.YELLOW),
				Component.literal(String.valueOf(depth.maxY())).withStyle(ChatFormatting.YELLOW)
		);
	}
}
