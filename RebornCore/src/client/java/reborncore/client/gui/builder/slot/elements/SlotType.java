/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package reborncore.client.gui.builder.slot.elements;

import net.minecraft.client.util.SpriteIdentifier;

public enum SlotType {
	NORMAL(1, 1, GuiSprites.SLOT_NORMAL, GuiSprites.BUTTON_SLOT_NORMAL, GuiSprites.BUTTON_HOVER_OVERLAY_SLOT_NORMAL);

	int slotOffsetX;
	int slotOffsetY;
	SpriteIdentifier sprite;
	SpriteIdentifier buttonSprite;
	SpriteIdentifier buttonHoverOverlay;

	SlotType(int slotOffsetX, int slotOffsetY, SpriteIdentifier sprite, SpriteIdentifier buttonSprite, SpriteIdentifier buttonHoverOverlay) {
		this.slotOffsetX = slotOffsetX;
		this.slotOffsetY = slotOffsetY;
		this.sprite = sprite;
		this.buttonSprite = buttonSprite;
		this.buttonHoverOverlay = buttonHoverOverlay;
	}

	public SpriteIdentifier getSprite() {
		return sprite;
	}

	public SpriteIdentifier getButtonSprite() {
		return buttonSprite;
	}

	public SpriteIdentifier getButtonHoverOverlay() {
		return buttonHoverOverlay;
	}
}
