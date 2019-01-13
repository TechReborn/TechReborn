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

package techreborn.utils;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import techreborn.init.TRContent;
import reborncore.common.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by Mark on 23/03/2016.
 */
public class StackWIPHandler {
	ArrayList<Block> wipBlocks = new ArrayList<>();
	public static ArrayList<ItemStack> devHeads = new ArrayList<>();

	public StackWIPHandler() {
		wipBlocks.add(TRContent.Machine.MAGIC_ENERGY_ABSORBER.block);
		wipBlocks.add(TRContent.Machine.CHUNK_LOADER.block);
		wipBlocks.add(TRContent.Machine.MAGIC_ENERGY_CONVERTER.block);

		addHead("modmuss50");
		addHead("Gigabit101");
		addHead("ProfProspector");
		addHead("Rushmead");
	}

	private void addHead(String name) {
		ItemStack head = new ItemStack(Items.SKULL, 1, 3);
		head.setTag(new NBTTagCompound());
		head.getTag().setTag("SkullOwner", new NBTTagString(name));
		devHeads.add(head);
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void toolTip(ItemTooltipEvent event) {
		Block block = Block.getBlockFromItem(event.getItemStack().getItem());
		if (block != null && wipBlocks.contains(block)) {
			event.getToolTip().add(TextFormatting.RED + StringUtils.t("techreborn.tooltip.wip"));
		}

		if (devHeads.contains(event.getItemStack())) {
			event.getToolTip().add(TextFormatting.GOLD + "TechReborn Developer");
		}
	}
}
