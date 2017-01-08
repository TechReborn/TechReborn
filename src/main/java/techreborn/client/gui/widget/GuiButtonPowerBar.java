package techreborn.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import reborncore.common.powerSystem.PowerSystem;

/**
 * Created by Prospector
 */
public class GuiButtonPowerBar extends GuiButton {
	public GuiButtonPowerBar(int buttonId, int x, int y) {
		super(buttonId, x, y, 12, 48, "");
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
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
