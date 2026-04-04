package techreborn.client.compat.jei.gui.render;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.model.sprite.SpriteId;
import reborncore.client.gui.GuiSprites;

public record SpriteDrawable(SpriteId spriteId, int width, int height) implements IDrawable {
	@Override
	public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
		GuiSprites.drawSpriteStretched(guiGraphics, spriteId, xOffset, yOffset, width, height);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
}
