package techreborn.client.gui.slot;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import reborncore.client.gui.GuiUtil;
import reborncore.client.gui.slots.SlotOutput;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.gui.GuiBase;
import techreborn.client.gui.slot.elements.ConfigSlotElement;
import techreborn.client.gui.slot.elements.ElementBase;
import techreborn.client.gui.slot.elements.SlotType;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GuiSlotConfiguration {

	static HashMap<Integer, ConfigSlotElement> slotElementMap = new HashMap<>();

	public static int slectedSlot = 0;

	public static void reset() {
		slectedSlot = -1;
	}

	public static void init(GuiBase guiBase) {
		reset();
		slotElementMap.clear();

		BuiltContainer container = guiBase.container;
		for (Slot slot : container.inventorySlots) {
			if (guiBase.tile != slot.inventory) {
				continue;
			}
			ConfigSlotElement slotElement = new ConfigSlotElement(guiBase.getMachine(), slot.getSlotIndex(), SlotType.NORMAL, slot.xPos - guiBase.guiLeft + 50, slot.yPos - guiBase.guiTop - 25, guiBase);
			slotElementMap.put(slot.getSlotIndex(), slotElement);
		}

	}

	public static void draw(GuiBase guiBase, int mouseX, int mouseY) {
		BuiltContainer container = guiBase.container;

		for (Slot slot : container.inventorySlots) {
			if (guiBase.tile != slot.inventory) {
				continue;
			}

			GlStateManager.color(255, 0, 0);
			Color color = new Color(255, 0, 0, 128);
			GuiUtil.drawGradientRect(slot.xPos - 1, slot.yPos - 1, 18, 18, color.getRGB(), color.getRGB());
			GlStateManager.color(255, 255, 255);
		}

		if (slectedSlot != -1) {
			slotElementMap.get(slectedSlot).draw(guiBase);
		}
	}

	public static List<ConfigSlotElement> getVisibleElements() {
		return slotElementMap.values().stream()
			.filter(configSlotElement -> configSlotElement.getId() == slectedSlot)
			.collect(Collectors.toList());
	}

	public static void mouseClicked(int mouseX, int mouseY, int mouseButton, GuiBase guiBase) throws IOException {
		if (mouseButton == 0) {
			for (ConfigSlotElement configSlotElement : getVisibleElements()) {
				for (ElementBase element : configSlotElement.elements) {
					if (element.isInRect(guiBase, element.x, element.y, element.getWidth(guiBase.getMachine()), element.getHeight(guiBase.getMachine()), mouseX, mouseY)) {
						element.isPressing = true;
						boolean action = element.onStartPress(guiBase.getMachine(), guiBase, mouseX, mouseY);
						for (ElementBase e : getVisibleElements()) {
							if (e != element) {
								e.isPressing = false;
							}
						}
						if(action)
							break;
					} else {
						element.isPressing = false;
					}
				}
			}
		}
	}

	public static void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick, GuiBase guiBase) {
		if (mouseButton == 0) {
			for (ConfigSlotElement configSlotElement : getVisibleElements()) {
				for (ElementBase element : configSlotElement.elements) {
					if (element.isInRect(guiBase, element.x, element.y, element.getWidth(guiBase.getMachine()), element.getHeight(guiBase.getMachine()), mouseX, mouseY)) {
						element.isDragging = true;
						boolean action = element.onDrag(guiBase.getMachine(), guiBase, mouseX, mouseY);
						for (ElementBase e : getVisibleElements()) {
							if (e != element) {
								e.isDragging = false;
							}
						}
						if(action)
							break;
					} else {
						element.isDragging = false;
					}
				}
			}
		}
	}

	public static void mouseReleased(int mouseX, int mouseY, int mouseButton, GuiBase guiBase) {
		BuiltContainer container = guiBase.container;


		boolean clicked = false;
		if (mouseButton == 0) {
			for (ConfigSlotElement configSlotElement : getVisibleElements()) {
				if (configSlotElement.isInRect(guiBase, configSlotElement.x, configSlotElement.y, configSlotElement.getWidth(guiBase.getMachine()), configSlotElement.getHeight(guiBase.getMachine()), mouseX, mouseY)) {
					clicked = true;
				}
				for (ElementBase element : configSlotElement.elements) {
					if (element.isInRect(guiBase, element.x, element.y, element.getWidth(guiBase.getMachine()), element.getHeight(guiBase.getMachine()), mouseX, mouseY)) {
						element.isReleasing = true;
						boolean action = element.onRelease(guiBase.getMachine(), guiBase, mouseX, mouseY);
						for (ElementBase e : getVisibleElements()) {
							if (e != element) {
								e.isReleasing = false;
							}
						}
						if(action)
							break;
					} else {
						element.isReleasing = false;
					}
				}
			}
		}
		if(!clicked){
			for (Slot slot : container.inventorySlots) {
				if (guiBase.tile != slot.inventory) {
					continue;
				}
				if (guiBase.isPointInRect(slot.xPos, slot.yPos, 18, 18, mouseX, mouseY)) {
					slectedSlot = slot.getSlotIndex();
				}
			}
		}
	}

}
