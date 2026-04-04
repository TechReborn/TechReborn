package techreborn.client.compat.jei.gui.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import reborncore.client.gui.RenderTarget;

public class JeiRenderTarget implements RenderTarget {
	public static final RenderTarget INSTANCE = new JeiRenderTarget();

	private JeiRenderTarget() {
	}

	@Override
	public boolean hideGuiElements() {
		return false;
	}

	@Override
	public int getGuiLeft() {
		return 0;
	}

	@Override
	public int getGuiTop() {
		return 0;
	}

	@Override
	public boolean isPointInRect(int rectX, int rectY, int rectWidth, int rectHeight, double pointX, double pointY) {
		return false;
	}

	@Override
	public Font getFont() {
		return Minecraft.getInstance().font;
	}
}
