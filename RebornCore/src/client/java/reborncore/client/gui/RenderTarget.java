package reborncore.client.gui;

import net.minecraft.client.gui.Font;

public interface RenderTarget {
	boolean hideGuiElements();

	int getGuiLeft();

	int getGuiTop();

	boolean isPointInRect(int rectX, int rectY, int rectWidth, int rectHeight, double pointX, double pointY);

	Font getFont();
}
