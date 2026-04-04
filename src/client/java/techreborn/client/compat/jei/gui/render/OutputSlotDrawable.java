package techreborn.client.compat.jei.gui.render;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import reborncore.client.gui.GuiSprites;

public record OutputSlotDrawable(boolean left, boolean center, boolean right) implements IDrawable {

	public static final SpriteDrawable SLOT_BAR_LEFT = new SpriteDrawable(GuiSprites.SLOT_BAR_RIGHT, 3, 26);
	public static final SpriteDrawable SLOT_BAR_CENTER = new SpriteDrawable(GuiSprites.SLOT_BAR_CENTER, 20, 26);
	public static final SpriteDrawable SLOT_BAR_RIGHT = new SpriteDrawable(GuiSprites.SLOT_BAR_LEFT, 3, 26);

	public static final OutputSlotDrawable SINGLE = new OutputSlotDrawable(true, true, true);
	public static final OutputSlotDrawable LEFT = new OutputSlotDrawable(true, true, false);
	public static final OutputSlotDrawable CENTER = new OutputSlotDrawable(false, true, false);
	public static final OutputSlotDrawable RIGHT = new OutputSlotDrawable(false, true, true);

	@Override
	public int getWidth() {
		return 26;
	}

	@Override
	public int getHeight() {
		return 26;
	}

	@Override
	public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
		if(left) {
			SLOT_BAR_LEFT.draw(guiGraphics, xOffset, yOffset);
		}
		if(center) {
			SLOT_BAR_CENTER.draw(guiGraphics, xOffset + 3, yOffset);
		}
		if(right) {
			SLOT_BAR_RIGHT.draw(guiGraphics, xOffset + 23, yOffset);
		}
	}
}
