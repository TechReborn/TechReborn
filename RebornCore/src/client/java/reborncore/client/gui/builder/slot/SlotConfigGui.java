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
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import reborncore.client.ClientChatUtils;
import reborncore.client.ClientNetworkManager;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.slot.elements.ConfigSlotElement;
import reborncore.client.gui.builder.slot.elements.ElementBase;
import reborncore.client.gui.builder.slot.elements.SlotType;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.network.ServerBoundPackets;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.util.Color;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

// Why is all this static?
public class SlotConfigGui {

	public static HashMap<Integer, ConfigSlotElement> slotElementMap = new HashMap<>();

	public static int selectedSlot = 0;

	public static void reset() {
		selectedSlot = -1;
	}

	public static void init(GuiBase<?> guiBase) {
		reset();
		slotElementMap.clear();

		BuiltScreenHandler container = guiBase.builtScreenHandler;
		for (Slot slot : container.slots) {
			if (guiBase.be != slot.inventory) {
				continue;
			}

			ConfigSlotElement slotElement = new ConfigSlotElement(guiBase.getMachine().getOptionalInventory().get(), slot.getIndex(), SlotType.NORMAL, slot.x - guiBase.getGuiLeft() + 50, slot.y - guiBase.getGuiTop() - 25, guiBase);
			slotElementMap.put(slot.getIndex(), slotElement);
		}

	}

	public static void draw(DrawContext drawContext, GuiBase<?> guiBase, int mouseX, int mouseY) {
		BuiltScreenHandler container = guiBase.builtScreenHandler;
		for (Slot slot : container.slots) {
			if (guiBase.be != slot.inventory) {
				continue;
			}
			Color color = new Color(255, 0, 0, 128);

			drawContext.fill(slot.x -1, slot.y -1, slot.x + 17, slot.y + 17, color.getColor());
		}

		if (selectedSlot != -1) {
			slotElementMap.get(selectedSlot).draw(drawContext, guiBase, mouseX, mouseY);
		}
	}

	public static List<ConfigSlotElement> getVisibleElements() {
		if (selectedSlot == -1) {
			return Collections.emptyList();
		}
		return slotElementMap.values().stream()
				.filter(configSlotElement -> configSlotElement.getId() == selectedSlot)
				.collect(Collectors.toList());
	}

	public static void copyToClipboard() {
		MachineBaseBlockEntity machine = getMachine();
		if (machine == null || machine.getSlotConfiguration() == null) {
			return;
		}
		String json = machine.getSlotConfiguration().toJson(machine.getClass().getCanonicalName());
		MinecraftClient.getInstance().keyboard.setClipboard(json);
		ClientChatUtils.addHudMessage(Text.literal("Slot configuration copied to clipboard"));
	}

	public static void pasteFromClipboard() {
		MachineBaseBlockEntity machine = getMachine();
		if (machine == null || machine.getSlotConfiguration() == null) {
			return;
		}
		String json = MinecraftClient.getInstance().keyboard.getClipboard();
		try {
			machine.getSlotConfiguration().readJson(json, machine.getClass().getCanonicalName());
			ClientNetworkManager.sendToServer(ServerBoundPackets.createPacketConfigSave(machine.getPos(), machine.getSlotConfiguration()));
			ClientChatUtils.addHudMessage(Text.literal("Slot configuration loaded from clipboard"));
		} catch (UnsupportedOperationException e) {
			ClientChatUtils.addHudMessage(Text.literal(e.getMessage()));
		}
	}

	@Nullable
	private static MachineBaseBlockEntity getMachine() {
		if (!(MinecraftClient.getInstance().currentScreen instanceof GuiBase<?> base)) {
			return null;
		}
		if (base.be instanceof MachineBaseBlockEntity machineBase) {
			return machineBase;
		}
		return null;
	}

	public static boolean mouseClicked(GuiBase<?> guiBase, double mouseX, double mouseY, int mouseButton) {
		BuiltScreenHandler screenHandler = guiBase.builtScreenHandler;

		if (getVisibleElements().isEmpty()) {
			for (Slot slot : screenHandler.slots) {
				if (guiBase.be != slot.inventory) {
					continue;
				}
				if (guiBase.isPointInRect(slot.x, slot.y, 18, 18, mouseX, mouseY)) {
					selectedSlot = slot.getIndex();
					return true;
				}
			}
		}

		if (mouseButton == 0) {
			for (ConfigSlotElement configSlotElement : getVisibleElements()) {
				for (ElementBase element : Lists.reverse(configSlotElement.elements)) {
					if (element.isMouseWithinRect(guiBase, mouseX, mouseY)) {
						if (element.onClick(guiBase.getMachine(), guiBase, mouseX, mouseY)) {
							return true;
						}
					}
				}
			}
		}

		return !getVisibleElements().isEmpty();
	}
}
