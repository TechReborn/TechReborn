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

package reborncore.client.gui.builder.slot;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.slot.elements.ConfigFluidElement;
import reborncore.client.gui.builder.slot.elements.SlotType;

import java.util.Objects;

public class FluidConfigGui extends GuiTab {
	@Nullable
	private ConfigFluidElement fluidConfigElement;

	public FluidConfigGui(GuiBase<?> guiBase) {
		super(guiBase);
	}

	@Override
	public String name() {
		return "reborncore.gui.tooltip.config_fluids";
	}

	@Override
	public boolean enabled() {
		return machine.showTankConfig();
	}

	@Override
	public ItemStack stack() {
		return GuiBase.fluidCellProvider.provide(Fluids.LAVA);
	}

	@Override
	public void open() {
		fluidConfigElement = new ConfigFluidElement(SlotType.NORMAL, 35 - guiBase.getGuiLeft() + 50, 35 - guiBase.getGuiTop() - 25, guiBase);
	}

	@Override
	public void close() {
		fluidConfigElement = null;
	}

	@Override
	public void draw(DrawContext drawContext, int x, int y) {
		Objects.requireNonNull(fluidConfigElement).draw(drawContext, guiBase, x, y);
	}

	@Override
	public boolean click(double mouseX, double mouseY, int mouseButton) {
		if (mouseButton == 0 && fluidConfigElement != null) {
			return fluidConfigElement.onClick(guiBase, mouseX, mouseY);
		}

		return false;
	}
}
