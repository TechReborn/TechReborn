package techreborn.client.gui.slot.elements;

public enum SlotType {
	NORMAL(1, 1, Sprite.SLOT_NORMAL, Sprite.BUTTON_SLOT_NORMAL, Sprite.BUTTON_HOVER_OVERLAY_SLOT_NORMAL);

	int slotOffsetX;
	int slotOffsetY;
	Sprite sprite;
	Sprite buttonSprite;
	Sprite buttonHoverOverlay;

	SlotType(int slotOffsetX, int slotOffsetY, Sprite sprite, Sprite buttonSprite, Sprite buttonHoverOverlay) {
		this.slotOffsetX = slotOffsetX;
		this.slotOffsetY = slotOffsetY;
		this.sprite = sprite;
		this.buttonSprite = buttonSprite;
		this.buttonHoverOverlay = buttonHoverOverlay;
	}

	SlotType(int slotOffset, Sprite sprite) {
		this.slotOffsetX = slotOffset;
		this.slotOffsetY = slotOffset;
		this.sprite = sprite;
	}

	public int getSlotOffsetX() {
		return slotOffsetX;
	}

	public int getSlotOffsetY() {
		return slotOffsetY;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Sprite getButtonSprite() {
		return buttonSprite;
	}

	public Sprite getButtonHoverOverlay() {
		return buttonHoverOverlay;
	}
}