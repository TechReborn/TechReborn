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
import net.minecraft.item.ItemStack;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.RedstoneConfigGui;
import reborncore.common.blockentity.MachineBaseBlockEntity;

import java.util.Collections;
import java.util.List;

public abstract class GuiTab {
	public static List<GuiTabFactory> TABS = List.of(
		SlotConfigGui::new,
		FluidConfigGui::new,
		RedstoneConfigGui::new
	);

	protected final MachineBaseBlockEntity machine;
	protected final GuiBase<?> guiBase;

	public GuiTab(GuiBase<?> guiBase) {
		this.machine = guiBase.getMachine();
		this.guiBase = guiBase;
	}

	public abstract String name();

	public abstract boolean enabled();

	public abstract ItemStack stack();

	public void open() {};

	public void close() {};

	public abstract void draw(DrawContext drawContext, int x, int y);

	public boolean click(double mouseX, double mouseY, int mouseButton) {
		return false;
	}

	public boolean keyPress(int keyCode, int scanCode, int modifiers) {
		return false;
	}

	public List<String> getTips() {
		return Collections.emptyList();
	}

	public boolean hideGuiElements() {
		return true;
	}

	public GuiBase<?> gui() {
		return guiBase;
	}

	public interface GuiTabFactory {
		GuiTab create(GuiBase<?> gui);
	}
}
