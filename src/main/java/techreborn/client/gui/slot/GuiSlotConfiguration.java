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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GuiSlotConfiguration {

	static HashMap<Integer, ConfigSlotElement> slotElementMap = new HashMap<>();

	public static void init(GuiBase guiBase){
		slotElementMap.clear();

		BuiltContainer container = guiBase.container;
		for(Slot slot : container.inventorySlots) {
			if (guiBase.tile != slot.inventory) {
				continue;
			}
			ConfigSlotElement slotElement = new ConfigSlotElement(guiBase.getMachine(), slot.getSlotIndex(), SlotType.NORMAL, slot.xPos - guiBase.guiLeft + 50, slot.yPos - guiBase.guiTop - 25, guiBase);
			slotElementMap.put(slot.getSlotIndex(), slotElement);
		}

	}

	public static void draw(GuiBase guiBase, int mouseX, int mouseY){
		BuiltContainer container = guiBase.container;

		boolean showSideConfig = false;
		Slot slotToConfigure = null;

		for(Slot slot : container.inventorySlots){
			if(guiBase.tile != slot.inventory){
				continue;
			}
			if(slot instanceof SlotOutput){
				slotToConfigure = slot;
				showSideConfig = true;
			}
		}


		if(showSideConfig && slotToConfigure != null){
			slotElementMap.get(slotToConfigure.getSlotIndex()).draw(guiBase);
		}
	}

	public static List<ConfigSlotElement> getVisibleElements(){
		return slotElementMap.values().stream().filter(configSlotElement -> {
			//TODO check that its visable
			return true;
		}).collect(Collectors.toList());
	}


	public static void mouseClicked(int mouseX, int mouseY, int mouseButton, GuiBase guiBase) throws IOException {
		if (mouseButton == 0) {
			for (ElementBase element : getVisibleElements()) {
				if (element.isInRect(guiBase, element.x, element.y, element.getWidth(guiBase.getMachine()), element.getHeight(guiBase.getMachine()), mouseX, mouseY)) {
					element.isPressing = true;
					element.onStartPress(guiBase.getMachine(), guiBase, mouseX, mouseY);
					for (ElementBase e : getVisibleElements()) {
						if (e != element) {
							e.isPressing = false;
						}
					}
					break;
				} else {
					element.isPressing = false;
				}
			}
		}
	}

	public static void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick, GuiBase guiBase) {
		mouseX -= guiBase.guiLeft - 50;
		mouseY -= guiBase.guiTop - 50;
		if (mouseButton == 0) {
			for (ElementBase element : getVisibleElements()) {
				if (element.isInRect(guiBase, element.x, element.y, element.getWidth(guiBase.getMachine()), element.getHeight(guiBase.getMachine()), mouseX, mouseY)) {
					element.isDragging = true;
					element.onDrag(guiBase.getMachine(), guiBase, mouseX, mouseY);
					for (ElementBase e : getVisibleElements()) {
						if (e != element) {
							e.isDragging = false;
						}
					}
					break;
				} else {
					element.isDragging = false;
				}
			}
		}
	}

	public static void mouseReleased(int mouseX, int mouseY, int mouseButton, GuiBase guiBase) {
		mouseX -= guiBase.guiLeft - 50;
		mouseY -= guiBase.guiTop - 50;
		if (mouseButton == 0) {
			for (ElementBase element : getVisibleElements()) {
				if (element.isInRect(guiBase, element.x, element.y, element.getWidth(guiBase.getMachine()), element.getHeight(guiBase.getMachine()), mouseX, mouseY)) {
					element.isReleasing = true;
					element.onRelease(guiBase.getMachine(), guiBase, mouseX, mouseY);
					for (ElementBase e : getVisibleElements()) {
						if (e != element) {
							e.isReleasing = false;
						}
					}
					break;
				} else {
					element.isReleasing = false;
				}
			}
		}
	}

}
