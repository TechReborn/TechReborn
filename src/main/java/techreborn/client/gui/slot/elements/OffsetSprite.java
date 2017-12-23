package techreborn.client.gui.slot.elements;

import reborncore.common.tile.TileLegacyMachineBase;

public class OffsetSprite {
	public ISprite sprite;
	public int offsetX = 0;
	public int offsetY = 0;

	public OffsetSprite(ISprite sprite, int offsetX, int offsetY) {
		this.sprite = sprite;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public OffsetSprite(ISprite sprite) {
		this.sprite = sprite;
	}

	public OffsetSprite(Sprite sprite, TileLegacyMachineBase provider) {
		this.sprite = sprite;
	}

	public ISprite getSprite() {
		return sprite;
	}

	public int getOffsetX(TileLegacyMachineBase provider) {
		return offsetX + sprite.getSprite(provider).offsetX;
	}

	public OffsetSprite setOffsetX(int offsetX) {
		this.offsetX = offsetX;
		return this;
	}

	public int getOffsetY(TileLegacyMachineBase provider) {
		return offsetY + sprite.getSprite(provider).offsetY;
	}

	public OffsetSprite setOffsetY(int offsetY) {
		this.offsetY = offsetY;
		return this;
	}
}
