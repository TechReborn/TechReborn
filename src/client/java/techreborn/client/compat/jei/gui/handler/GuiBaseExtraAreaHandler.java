package techreborn.client.compat.jei.gui.handler;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;
import reborncore.api.blockentity.IUpgradeable;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.config.GuiTab;
import reborncore.client.gui.config.SlotConfigGui;
import reborncore.client.gui.config.elements.ConfigSlotElement;

public class GuiBaseExtraAreaHandler implements IGuiContainerHandler<GuiBase<?>> {

	@Override
	public List<Rect2i> getGuiExtraAreas(GuiBase<?> guiBase) {
		List<Rect2i> extraAreas = new ArrayList<>(2);
		int height = 0;
		if(guiBase.tryAddUpgrades() && guiBase.be instanceof IUpgradeable upgradeable) {
			if(upgradeable.canBeUpgraded()) {
				height = 80;
			}
		}
		for(GuiTab slot : guiBase.getTabs()) {
			if(slot.enabled()) {
				height += 24;
			}
		}
		if(height > 0) {
			int width = 20;
			extraAreas.add(new Rect2i(guiBase.getGuiLeft() - width, guiBase.getGuiTop() + 8, width, height));
		}
		if(guiBase.getSelectedTab() instanceof SlotConfigGui slotConfigGui) {
			ConfigSlotElement element = slotConfigGui.getSelectedSlot();
			if(element != null) {
				int slotX = element.getX() + guiBase.getGuiLeft() - 50;
				if(element.getWidth() + slotX > guiBase.getScreenWidth()) {
					int slotY = element.getY() + guiBase.getGuiTop() + 25;
					int exclusionX = guiBase.getScreenWidth() + guiBase.getGuiLeft();
					int exclusionY = slotY + guiBase.getGuiTop() - (element.getHeight()/2);
					int exclusionWidth = element.getWidth() + slotX - guiBase.getScreenWidth() + 15;
					extraAreas.add(new Rect2i(exclusionX, exclusionY, exclusionWidth, element.getHeight()));
				}
			}
		}
		return extraAreas;
	}
}
