package techreborn.client.compat.rei;

import com.google.common.collect.Lists;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZonesProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.slot.SlotConfigGui;
import reborncore.client.gui.builder.slot.elements.ConfigSlotElement;

import java.util.Collection;
import java.util.Collections;

@Environment(EnvType.CLIENT)
public class SlotConfigExclusionZones implements ExclusionZonesProvider<GuiBase<?>> {

	@Override
	public Collection<Rectangle> provide(GuiBase screen) {
		if (!screen.hideGuiElements()) {
			return Collections.emptyList();
		}
		if (SlotConfigGui.selectedSlot == -1){
			return Collections.emptyList();
		}
		ConfigSlotElement element = SlotConfigGui.slotElementMap.get(SlotConfigGui.selectedSlot);
		int slotX = element.x + screen.getGuiLeft() -50;
		if (element.getWidth() + slotX > screen.getScreenWidth() ) {
			int slotY = element.y + screen.getGuiTop() + 25;
			int exclusionX = screen.getScreenWidth() + screen.getGuiLeft();
			int exclusionY = slotY + screen.getGuiTop() - (element.getHeight()/2);
			int exclusionWidth = element.getWidth() + slotX - screen.getScreenWidth() + 15;
			return Lists.newArrayList(new Rectangle(exclusionX, exclusionY, exclusionWidth, element.getHeight()));
		}

		return Collections.emptyList();
	}
}
