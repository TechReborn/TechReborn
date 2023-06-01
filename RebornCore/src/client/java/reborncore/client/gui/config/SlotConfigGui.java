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

package reborncore.client.gui.config;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import reborncore.client.ClientChatUtils;
import reborncore.client.ClientNetworkManager;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.config.elements.ConfigSlotElement;
import reborncore.client.gui.config.elements.SlotType;
import reborncore.common.network.ServerBoundPackets;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.util.Color;

import java.util.List;
import java.util.Objects;

public class SlotConfigGui extends GuiTab {
	private Int2ObjectMap<ConfigSlotElement> slotElementMap = new Int2ObjectOpenHashMap<>();

	@Nullable
	private ConfigSlotElement selectedSlot;

	public SlotConfigGui(GuiBase<?> guiBase) {
		super(guiBase);
	}

	@Override
	public String name() {
		return "reborncore.gui.tooltip.config_slots";
	}

	@Override
	public boolean enabled() {
		return machine.hasSlotConfig();
	}

	@Override
	public ItemStack stack() {
		return GuiBase.wrenchStack;
	}

	@Override
	public void open() {
		selectedSlot = null;
		slotElementMap.clear();

		BuiltScreenHandler container = guiBase.builtScreenHandler;
		for (Slot slot : container.slots) {
			if (guiBase.be != slot.inventory) {
				continue;
			}

			ConfigSlotElement slotElement = new ConfigSlotElement(
				guiBase.getMachine().getOptionalInventory().get(),
				slot.getIndex(),
				SlotType.NORMAL,
				slot.x - guiBase.getGuiLeft() + 50,
				slot.y - guiBase.getGuiTop() - 25,
				guiBase,
				this::close
			);
			slotElementMap.put(slot.getIndex(), slotElement);
		}

	}

	@Override
	public void close() {
		selectedSlot = null;
	}

	@Override
	public void draw(DrawContext drawContext, int x, int y) {
		BuiltScreenHandler container = guiBase.builtScreenHandler;
		for (Slot slot : container.slots) {
			if (guiBase.be != slot.inventory) {
				continue;
			}
			Color color = new Color(255, 0, 0, 128);

			drawContext.fill(slot.x -1, slot.y -1, slot.x + 17, slot.y + 17, color.getColor());
		}

		if (selectedSlot != null) {
			selectedSlot.draw(drawContext, guiBase, x, y);
		}
	}

	@Override
	public boolean click(double mouseX, double mouseY, int mouseButton) {
		final BuiltScreenHandler screenHandler = Objects.requireNonNull(guiBase.builtScreenHandler);

		if (selectedSlot != null) {
			return selectedSlot.onClick(guiBase, mouseX, mouseY);
		} else {
			for (Slot slot : screenHandler.slots) {
				if (guiBase.be != slot.inventory) {
					continue;
				}
				if (guiBase.isPointInRect(slot.x, slot.y, 18, 18, mouseX, mouseY)) {
					selectedSlot = slotElementMap.get(slot.getIndex());
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean keyPress(int keyCode, int scanCode, int modifiers) {
		if (Screen.hasControlDown() && keyCode == GLFW.GLFW_KEY_C) {
			copyToClipboard();
			return true;
		} else if (Screen.hasControlDown() && keyCode == GLFW.GLFW_KEY_V) {
			pasteFromClipboard();
			return true;
		} else if (keyCode == GLFW.GLFW_KEY_ESCAPE && selectedSlot != null) {
			selectedSlot = null;
			return true;
		}
		return false;
	}

	@Override
	public List<String> getTips() {
		return List.of(
			"reborncore.gui.slotconfigtip.slot",
			"reborncore.gui.slotconfigtip.side1",
			"reborncore.gui.slotconfigtip.side2",
			"reborncore.gui.slotconfigtip.side3",
			"reborncore.gui.slotconfigtip.copy1",
			"reborncore.gui.slotconfigtip.copy2"
		);
	}

	private void copyToClipboard() {
		machine.getSlotConfiguration();
		String json = machine.getSlotConfiguration().toJson(machine.getClass().getCanonicalName());
		MinecraftClient.getInstance().keyboard.setClipboard(json);
		ClientChatUtils.addHudMessage(Text.literal("Slot configuration copied to clipboard"));
	}

	private void pasteFromClipboard() {
		machine.getSlotConfiguration();

		String json = MinecraftClient.getInstance().keyboard.getClipboard();
		try {
			machine.getSlotConfiguration().readJson(json, machine.getClass().getCanonicalName());
			ClientNetworkManager.sendToServer(ServerBoundPackets.createPacketConfigSave(machine.getPos(), machine.getSlotConfiguration()));
			ClientChatUtils.addHudMessage(Text.literal("Slot configuration loaded from clipboard"));
		} catch (UnsupportedOperationException e) {
			ClientChatUtils.addHudMessage(Text.literal(e.getMessage()));
		}
	}

	public ConfigSlotElement getSelectedSlot() {
		return selectedSlot;
	}
}
