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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.HeightContext;
import org.jetbrains.annotations.Nullable;
import reborncore.common.BaseBlockEntityProvider;
import techreborn.init.TRContent;
import techreborn.items.DynamicCellItem;
import techreborn.items.UpgradeItem;
import techreborn.utils.ToolTipAssistUtils;
import techreborn.world.OreDistribution;
import techreborn.world.TargetDimension;

import java.util.List;
import java.util.Map;

public class StackToolTipHandler implements ItemTooltipCallback {

	public static final Map<Item, Boolean> ITEM_ID = Maps.newHashMap();
	private static final Map<Block, OreDistribution> ORE_DISTRIBUTION_MAP = Maps.newHashMap();
	private static final List<Block> UNOBTAINABLE_ORES = Lists.newLinkedList();

	public static void setup() {
		ItemTooltipCallback.EVENT.register(new StackToolTipHandler());

		for (TRContent.Ores ore : TRContent.Ores.values()) {
			if (ore.isDeepslate()) {
				TRContent.Ores normal = ore.getUnDeepslate();
				if (normal.distribution != null && normal.distribution.dimension != TargetDimension.OVERWORLD)
					UNOBTAINABLE_ORES.add(ore.block);
				continue;
			}

			if (ore.distribution != null) {
				ORE_DISTRIBUTION_MAP.put(ore.block, ore.distribution);

				if (ore.distribution.dimension != TargetDimension.OVERWORLD) {
					continue; // No Deepslate in other dims
				}

				TRContent.Ores deepslate = ore.getDeepslate();
				if (deepslate != null) {
					// Deepslate shares the same distribution as the stone version.
					ORE_DISTRIBUTION_MAP.put(deepslate.block, ore.distribution);
				}
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

		if (item instanceof UpgradeItem upgrade) {
			ToolTipAssistUtils.addInfo(item.getTranslationKey(), tooltipLines, false);
			tooltipLines.addAll(ToolTipAssistUtils.getUpgradeStats(TRContent.Upgrades.valueOf(upgrade.name.toUpperCase()), stack.getCount(), Screen.hasShiftDown()));
		}

		if (item instanceof DynamicCellItem cell) {
			Fluid fluid = cell.getFluid(stack);
			if (!(fluid instanceof FlowableFluid) && fluid != Fluids.EMPTY)
				ToolTipAssistUtils.addInfo("unplaceable_fluid", tooltipLines, false);
		}

		Text text = null;
		if (UNOBTAINABLE_ORES.contains(block))
			text = new TranslatableText("techreborn.tooltip.unobtainable");
		OreDistribution oreDistribution = ORE_DISTRIBUTION_MAP.get(block);
		if (oreDistribution != null && text == null) {
			text = switch (oreDistribution.dimension) {
				case OVERWORLD -> getOverworldOreText(oreDistribution);
				case END -> new TranslatableText("techreborn.tooltip.ores.end");
				case NETHER -> new TranslatableText("techreborn.tooltip.ores.nether");
			};
		}
		if (text != null)
			tooltipLines.add(text.copy().formatted(Formatting.AQUA));
	}

	private static boolean isTRItem(Item item) {
		return Registry.ITEM.getId(item).getNamespace().equals("techreborn");
	}

	@Nullable
	private static HeightContext getHeightContextSafely() {
		final IntegratedServer server = MinecraftClient.getInstance().getServer();

		if (server == null) {
			return null;
		}

		final ServerWorld world = server.getWorld(World.OVERWORLD);

		if (world == null) {
			return null;
		}

		return new HeightContext(world.getChunkManager().getChunkGenerator(), world);
	}

	@Nullable
	private static Text getOverworldOreText(OreDistribution oreDistribution) {
		final HeightContext heightContext = getHeightContextSafely();

		if (heightContext == null) {
			return null;
		}

		final int minY = oreDistribution.minOffset.getY(heightContext);
		final int maxY = oreDistribution.maxY;

		return new TranslatableText("techreborn.tooltip.ores.overworld",
				new LiteralText(String.valueOf(minY)).formatted(Formatting.YELLOW),
				new LiteralText(String.valueOf(maxY)).formatted(Formatting.YELLOW)
		);
	}
}
