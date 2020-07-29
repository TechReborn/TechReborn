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

package techreborn.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import reborncore.api.blockentity.IUpgrade;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.recipes.IUpgradeHandler;
import techreborn.TechReborn;
import techreborn.init.TRContent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class UpgradeItem extends Item implements IUpgrade {

	public final String name;
	public final IUpgrade behavior;

	public UpgradeItem(String name, IUpgrade process) {
		super(new Item.Settings().group(TechReborn.ITEMGROUP).maxCount(16));
		this.name = name;
		this.behavior = process;
	}

	@Override
	public void process(
			@NotNull MachineBaseBlockEntity blockEntity,
			@Nullable
					IUpgradeHandler handler,
			@NotNull
					ItemStack stack) {
		behavior.process(blockEntity, handler, stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.getItem() == TRContent.Upgrades.SUPERCONDUCTOR.item) {
			if (Screen.hasControlDown()) {
				tooltip.add(new LiteralText(Formatting.GOLD + "Blame obstinate_3 for this"));
			}
		}
	}
}