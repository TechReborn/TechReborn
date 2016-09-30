package techreborn.client.gui.widget.tooltip;

import net.minecraft.client.gui.FontRenderer;

public class ToolTipLine {

	private String line;
	private int color;
	private boolean shadowed;

	public ToolTipLine(String line, int color, boolean shadowed) {
		this.line = line;
		this.color = color;
		this.shadowed = shadowed;
	}

	public ToolTipLine(String line, int color) {
		this(line, color, false);
	}

	public ToolTipLine(String line, boolean shadowed) {
		this(line, 0xFFFFFF, shadowed);
	}

	public ToolTipLine(String line) {
		this(line, 0xFFFFFF, false);
	}

	public ToolTipLine() {
		this("");
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean isShadowed() {
		return shadowed;
	}

	public void setShadowed(boolean shadowed) {
		this.shadowed = shadowed;
	}

	public int getWidth(FontRenderer fontRenderer) {
		return fontRenderer.getStringWidth(getLine());
	}

	public void draw(FontRenderer fontRenderer, int x, int y) {
		fontRenderer.drawString(getLine(), x, y, color, isShadowed());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ToolTipLine that = (ToolTipLine) o;
		return color == that.color &&
			shadowed == that.shadowed &&
			line.equals(that.line);

	}

	@Override
	public int hashCode() {
		int result = line.hashCode();
		result = 31 * result + color;
		result = 31 * result + (shadowed ? 1 : 0);
		return result;
	}

}
