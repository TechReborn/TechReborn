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

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.slot.elements.ConfigFluidElement;
import reborncore.client.gui.builder.slot.elements.ElementBase;
import reborncore.client.gui.builder.slot.elements.SlotType;
import reborncore.common.blockentity.MachineBaseBlockEntity;

import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

public class FluidConfigGui {

	static ConfigFluidElement fluidConfigElement;

	public static void init(GuiBase<?> guiBase) {
		fluidConfigElement = new ConfigFluidElement(guiBase.getMachine().getTank(), SlotType.NORMAL, 35 - guiBase.getGuiLeft() + 50, 35 - guiBase.getGuiTop() - 25, guiBase);
	}

	public static void draw(MatrixStack matrixStack, GuiBase<?> guiBase, int mouseX, int mouseY) {
		fluidConfigElement.draw(matrixStack, guiBase);
	}

	public static List<ConfigFluidElement> getVisibleElements() {
		return Collections.singletonList(fluidConfigElement);
	}

	public static boolean mouseClicked(GuiBase<?> guiBase, double mouseX, double mouseY, int mouseButton) {
		if (mouseButton == 0) {
			for (ConfigFluidElement configFluidElement : getVisibleElements()) {
				for (ElementBase element : configFluidElement.elements) {
					if (element.isInRect(guiBase, element.x, element.y, element.getWidth(guiBase.getMachine()), element.getHeight(guiBase.getMachine()), mouseX, mouseY)) {
						element.isPressing = true;
						boolean action = element.onStartPress(guiBase.getMachine(), guiBase, mouseX, mouseY);
						for (ElementBase e : getVisibleElements()) {
							if (e != element) {
								e.isPressing = false;
							}
						}
						if (action) {
							break;
						}
					} else {
						element.isPressing = false;
					}
				}
			}
		}
		return !getVisibleElements().isEmpty();
	}

	public static void mouseClickMove(double mouseX, double mouseY, int mouseButton, long timeSinceLastClick, GuiBase<?> guiBase) {
		if (mouseButton == 0) {
			for (ConfigFluidElement configFluidElement : getVisibleElements()) {
				for (ElementBase element : configFluidElement.elements) {
					if (element.isInRect(guiBase, element.x, element.y, element.getWidth(guiBase.getMachine()), element.getHeight(guiBase.getMachine()), mouseX, mouseY)) {
						element.isDragging = true;
						boolean action = element.onDrag(guiBase.getMachine(), guiBase, mouseX, mouseY);
						for (ElementBase e : getVisibleElements()) {
							if (e != element) {
								e.isDragging = false;
							}
						}
						if (action) {
							break;
						}
					} else {
						element.isDragging = false;
					}
				}
			}
		}
	}

	public static boolean mouseReleased(GuiBase<?> guiBase, double mouseX, double mouseY, int mouseButton) {
		boolean clicked = false;
		if (mouseButton == 0) {
			for (ConfigFluidElement configFluidElement : getVisibleElements()) {
				if (configFluidElement.isInRect(guiBase, configFluidElement.x, configFluidElement.y, configFluidElement.getWidth(guiBase.getMachine()), configFluidElement.getHeight(guiBase.getMachine()), mouseX, mouseY)) {
					clicked = true;
				}
				for (ElementBase element : Lists.reverse(configFluidElement.elements)) {
					if (element.isInRect(guiBase, element.x, element.y, element.getWidth(guiBase.getMachine()), element.getHeight(guiBase.getMachine()), mouseX, mouseY)) {
						element.isReleasing = true;
						boolean action = element.onRelease(guiBase.getMachine(), guiBase, mouseX, mouseY);
						for (ElementBase e : getVisibleElements()) {
							if (e != element) {
								e.isReleasing = false;
							}
						}
						if (action) {
							clicked = true;
						}
						break;
					} else {
						element.isReleasing = false;
					}
				}
			}
		}
		return clicked;
	}

	@Nullable
	private static MachineBaseBlockEntity getMachine() {
		if (!(MinecraftClient.getInstance().currentScreen instanceof GuiBase)) {
			return null;
		}
		GuiBase<?> base = (GuiBase<?>) MinecraftClient.getInstance().currentScreen;
		if (!(base.be instanceof MachineBaseBlockEntity)) {
			return null;
		}
		MachineBaseBlockEntity machineBase = (MachineBaseBlockEntity) base.be;
		return machineBase;
	}

}
