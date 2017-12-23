package techreborn.client.gui.slot.elements;

import java.util.ArrayList;
import java.util.List;

public class SpriteContainer {
	public List<OffsetSprite> offsetSprites = new ArrayList<>();

	public SpriteContainer setSprite(int index, OffsetSprite sprite) {
		offsetSprites.set(index, sprite);
		return this;
	}

	public SpriteContainer setSprite(int index, ISprite sprite, int offsetX, int offsetY) {
		if (sprite instanceof Sprite) {
			offsetSprites.set(index, new OffsetSprite(sprite).setOffsetX(((Sprite) sprite).offsetX + offsetX).setOffsetY(((Sprite) sprite).offsetY + offsetY));
		} else {
			offsetSprites.set(index, new OffsetSprite(sprite, offsetX, offsetY));
		}
		return this;
	}

	public SpriteContainer setSprite(int index, ISprite sprite) {
		if (sprite instanceof Sprite) {
			offsetSprites.set(index, new OffsetSprite(sprite).setOffsetX(((Sprite) sprite).offsetX).setOffsetY(((Sprite) sprite).offsetY));
		} else {
			offsetSprites.add(index, new OffsetSprite(sprite));
		}
		return this;
	}

	public SpriteContainer addSprite(OffsetSprite sprite) {
		offsetSprites.add(sprite);
		return this;
	}

	public SpriteContainer addSprite(ISprite sprite, int offsetX, int offsetY) {
		if (sprite instanceof Sprite) {
			offsetSprites.add(new OffsetSprite(sprite).setOffsetX(((Sprite) sprite).offsetX + offsetX).setOffsetY(((Sprite) sprite).offsetY + offsetY));
		} else {
			offsetSprites.add(new OffsetSprite(sprite, offsetX, offsetY));
		}
		return this;
	}

	public SpriteContainer addSprite(ISprite sprite) {
		if (sprite instanceof Sprite) {
			offsetSprites.add(new OffsetSprite(sprite).setOffsetX(((Sprite) sprite).offsetX).setOffsetY(((Sprite) sprite).offsetY));
		} else {
			offsetSprites.add(new OffsetSprite(sprite));
		}
		return this;
	}
}
