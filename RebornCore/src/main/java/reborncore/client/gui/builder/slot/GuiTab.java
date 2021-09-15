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

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.Validate;
import reborncore.client.gui.builder.GuiBase;
import reborncore.common.blockentity.MachineBaseBlockEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class GuiTab {

	private final Builder builder;
	private final MachineBaseBlockEntity machineBaseBlockEntity;
	private final GuiBase<?> guiBase;

	private GuiTab(Builder builder, MachineBaseBlockEntity machineBaseBlockEntity, GuiBase<?> guiBase) {
		this.builder = builder;
		this.machineBaseBlockEntity = machineBaseBlockEntity;
		this.guiBase = guiBase;
	}

	public String name() {
		return builder.name;
	}

	public boolean enabled() {
		return builder.enabled.apply(this);
	}

	public ItemStack stack() {
		return builder.stack.apply(this);
	}

	public MachineBaseBlockEntity machine() {
		return machineBaseBlockEntity;
	}

	public void draw(MatrixStack matrixStack, int x, int y) {
		builder.draw.draw(matrixStack, guiBase, x, y);
	}

	public boolean click(double mouseX, double mouseY, int mouseButton) {
		return builder.click.click(guiBase, mouseX, mouseY, mouseButton);
	}

	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		return builder.mouseReleased.mouseReleased(guiBase, mouseX, mouseY, mouseButton);
	}

	public boolean keyPress(int keyCode, int scanCode, int modifiers) {
		return builder.keyPressed.keyPress(guiBase, keyCode, scanCode, modifiers);
	}

	public List<String> getTips() {
		List<String> tips = new LinkedList<>();
		builder.tips.accept(tips);
		return tips;
	}

	public boolean hideGuiElements() {
		return builder.hideGuiElements;
	}

	public GuiBase<?> gui() {
		return guiBase;
	}

	public static class Builder {

		private String name;
		private Function<GuiTab, Boolean> enabled = (tab) -> true;
		private Function<GuiTab, ItemStack> stack = (tab) -> ItemStack.EMPTY;
		private Draw draw = (matrixStack, gui, x, y) -> {
		};
		private Click click = (guiBase, mouseX, mouseY, mouseButton) -> false;
		private MouseReleased mouseReleased = (guiBase, mouseX, mouseY, state) -> false;
		private KeyPressed keyPressed = (guiBase, keyCode, scanCode, modifiers) -> false;
		private Consumer<List<String>> tips = strings -> {
		};
		private boolean hideGuiElements = false;

		public static Builder builder() {
			return new Builder();
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder stack(Function<GuiTab, ItemStack> function) {
			this.stack = function;
			return this;
		}

		public Builder enabled(Function<GuiTab, Boolean> function) {
			this.enabled = function;
			return this;
		}

		public Builder draw(Draw draw) {
			this.draw = draw;
			return this;
		}

		public Builder click(Click click) {
			this.click = click;
			return this;
		}

		public Builder mouseReleased(MouseReleased mouseReleased) {
			this.mouseReleased = mouseReleased;
			return this;
		}

		public Builder keyPressed(KeyPressed keyPressed) {
			this.keyPressed = keyPressed;
			return this;
		}

		public Builder tips(Consumer<List<String>> listConsumer) {
			this.tips = listConsumer;
			return this;
		}

		public Builder hideGuiElements() {
			hideGuiElements = true;
			return this;
		}

		public GuiTab build(MachineBaseBlockEntity blockEntity, GuiBase<?> guiBase) {
			Validate.notBlank(name, "No name provided");
			return new GuiTab(this, blockEntity, guiBase);
		}

		public interface Draw {
			void draw(MatrixStack matrixStack, GuiBase<?> guiBase, int mouseX, int mouseY);
		}

		public interface Click {
			boolean click(GuiBase<?> guiBase, double mouseX, double mouseY, int mouseButton);
		}

		public interface MouseReleased {
			boolean mouseReleased(GuiBase<?> guiBase, double mouseX, double mouseY, int state);
		}

		public interface KeyPressed {
			boolean keyPress(GuiBase<?> guiBase, int keyCode, int scanCode, int modifiers);
		}

	}
}
