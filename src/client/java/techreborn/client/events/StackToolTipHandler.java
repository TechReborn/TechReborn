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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import reborncore.api.IListInfoProvider;
import reborncore.common.BaseBlockEntityProvider;
import techreborn.blocks.cable.CableBlock;
import techreborn.events.OreDepthSyncHandler;
import techreborn.init.TRContent;
import techreborn.items.DynamicCellItem;
import techreborn.items.UpgradeItem;
import techreborn.world.OreDepth;
import techreborn.world.TargetDimension;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Environment(EnvType.CLIENT)
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
	public void getTooltip(ItemStack stack, TooltipContext tooltipContext, List<Text> tooltipLines) {
		Item item = stack.getItem();

		// Can currently be executed by a ForkJoinPool.commonPool-worker when REI is in async search mode
		// We skip this method until a thread-safe solution is in place
		if (!MinecraftClient.getInstance().isOnThread())
			return;

		if (!ITEM_ID.computeIfAbsent(item, StackToolTipHandler::isTRItem))
			return;

		// Machine info and upgrades helper section
		Block block = Block.getBlockFromItem(item);

		if (block instanceof BaseBlockEntityProvider) {
			ToolTipAssistUtils.addInfo(item.getTranslationKey(), tooltipLines);
		}

		if (block instanceof CableBlock cable) {
			BlockEntity blockEntity = cable.createBlockEntity(BlockPos.ORIGIN, block.getDefaultState());
			if (blockEntity != null) {
				((IListInfoProvider) blockEntity).addInfo(tooltipLines, false, false);
			}
		}

		if (item instanceof UpgradeItem upgrade) {
			ToolTipAssistUtils.addInfo(item.getTranslationKey(), tooltipLines, false);
			tooltipLines.addAll(ToolTipAssistUtils.getUpgradeStats(TRContent.Upgrades.valueOf(upgrade.name.toUpperCase()), stack.getCount(), Screen.hasShiftDown()));
		}

		if (item instanceof DynamicCellItem cell) {
			Fluid fluid = cell.getFluid(stack);
			if (!(fluid instanceof FlowableFluid) && fluid != Fluids.EMPTY)
				ToolTipAssistUtils.addInfo("unplaceable_fluid", tooltipLines, false);
		}

		if (item == TRContent.Upgrades.SUPERCONDUCTOR.item && Screen.hasControlDown()) {
			tooltipLines.add(Text.literal(Formatting.GOLD + "Blame obstinate_3 for this"));
		}

		if (item == TRContent.OMNI_TOOL) {
			tooltipLines.add(Text.literal(Formatting.YELLOW + I18n.translate("techreborn.tooltip.omnitool_motto")));
		}

		if (block == TRContent.Machine.INDUSTRIAL_CENTRIFUGE.block && Screen.hasControlDown()) {
			tooltipLines.add(Text.literal("Round and round it goes"));
		}

		if (UNOBTAINABLE_ORES.contains(block)) {
			tooltipLines.add(Text.translatable("techreborn.tooltip.unobtainable").formatted(Formatting.AQUA));
		} else if (OreDepthSyncHandler.getOreDepthMap().containsKey(block)) {
			OreDepth oreDepth = OreDepthSyncHandler.getOreDepthMap().get(block);
			Text text = getOreDepthText(oreDepth);
			tooltipLines.add(text.copy().formatted(Formatting.AQUA));
		}
	}

	private static boolean isTRItem(Item item) {
		return Registries.ITEM.getId(item).getNamespace().equals("techreborn");
	}

	private static Text getOreDepthText(OreDepth depth) {
		return Text.translatable("techreborn.tooltip.ores.%s".formatted(depth.dimension().name().toLowerCase(Locale.ROOT)),
				Text.literal(String.valueOf(depth.minY())).formatted(Formatting.YELLOW),
				Text.literal(String.valueOf(depth.maxY())).formatted(Formatting.YELLOW)
		);
	}
}
