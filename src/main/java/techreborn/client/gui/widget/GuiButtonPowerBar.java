package techreborn.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.client.gui.GuiBase;

/**
 * Created by Prospector
 */
public class GuiButtonPowerBar extends GuiButton {

	GuiBase.Layer layer;
	GuiBase gui;

	public GuiButtonPowerBar(int buttonId, int x, int y, GuiBase gui, GuiBase.Layer layer) {
		super(buttonId, x, y, 12, 48, "");
		this.layer = layer;
		this.gui = gui;
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {

		if (layer == GuiBase.Layer.FOREGROUND) {
			mouseX -= gui.guiLeft;
			mouseY -= gui.guiTop;
		}

		if (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
			PowerSystem.bumpPowerConfig();
			return true;
		}
		return false;
	}

	@Override
	public void drawButtonForegroundLayer(int mouseX, int mouseY) {

	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {

	}
}
