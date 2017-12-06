package techreborn.client.gui.slot;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import reborncore.client.gui.GuiUtil;
import reborncore.client.gui.slots.SlotOutput;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.gui.GuiBase;

import java.awt.*;

public class GuiSlotConfiguration {

	public static void draw(GuiBase guiBase, int mouseX, int mouseY){
		BuiltContainer container = guiBase.container;

		boolean showSideConfig = false;
		Slot slotToConfigure = null;

		for(Slot slot : container.inventorySlots){
			if(guiBase.tile != slot.inventory){
				continue;
			}
			GlStateManager.color(255, 0, 0);
			Color color = new Color(255, 0, 0, 128);
			int extaSize = 0;
			if(slot instanceof SlotOutput){
				extaSize = 4;
				color = new Color(255, 165, 0, 128);
				slotToConfigure = slot;
				showSideConfig = true;
			}
			GuiUtil.drawGradientRect(slot.xPos - 1 - extaSize, slot.yPos - 1 - extaSize, 18 + (extaSize * 2), 18 + (extaSize * 2), color.getRGB(), color.getRGB());
			GlStateManager.color(255, 255, 255);
		}


		if(showSideConfig && slotToConfigure != null){
			Color color = Color.GRAY;
			int wPosX = slotToConfigure.xPos + 23;
			int wPosY = slotToConfigure.yPos;
			GuiUtil.drawGradientRect(wPosX , wPosY , 50 , 50 , color.getRGB(), color.getRGB());


		}
	}


}
