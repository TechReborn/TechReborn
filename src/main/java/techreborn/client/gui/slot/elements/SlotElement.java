/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.client.gui.slot.elements;

import net.minecraftforge.items.ItemStackHandler;

public class SlotElement extends ElementBase {
	protected ItemStackHandler slotInventory;
	protected SlotType type;
	int slotId, slotX, slotY;

	public SlotElement(ItemStackHandler slotInventory, int slotId, int slotX, int slotY, SlotType type, int x, int y) {
		super(x, y, type.getSprite());
		this.type = type;
		this.slotInventory = slotInventory;
		this.slotId = slotId;
		this.slotX = slotX;
		this.slotY = slotY;
	}

	public SlotType getType() {
		return type;
	}

	public ItemStackHandler getSlotInventory() {
		return slotInventory;
	}

	public int getSlotId() {
		return slotId;
	}

	public int getSlotX() {
		return slotX;
	}

	public int getSlotY() {
		return slotY;
	}
}