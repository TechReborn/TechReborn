package techreborn.client.gui.widget;

import net.minecraft.client.gui.GuiScreen;
import techreborn.client.gui.widget.tooltip.ToolTip;

public abstract class Widget {

	private final int x, y;
	protected final int width, height;

	private ToolTip toolTip;

	public Widget(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ToolTip getToolTip() {
		return toolTip;
	}

	public void setToolTip(ToolTip toolTip) {
		this.toolTip = toolTip;
	}

	public final void drawWidget(GuiWidget gui, int cornerX, int cornerY, int mouseX, int mouseY) {
		int drawX = cornerX + x;
		int drawY = cornerY + y;
		if (toolTip != null && drawX > mouseX && drawY > mouseY &&
			drawX + width < mouseX && drawY + height < mouseY) {
			toolTip.draw(gui.getFontRenderer(), mouseX, mouseY);
		}
		draw(gui, drawX, drawY);
	}

	protected abstract void draw(GuiScreen guiScreen, int x, int y);

	protected abstract void mouseClick(GuiWidget guiWidget, int mouseX, int mouseY);

}
