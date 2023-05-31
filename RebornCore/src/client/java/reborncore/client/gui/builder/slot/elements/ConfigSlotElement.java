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

import net.minecraft.client.gui.DrawContext;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import reborncore.client.gui.builder.GuiBase;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.screen.slot.BaseSlot;

import java.util.Arrays;
import java.util.Objects;

public class ConfigSlotElement extends ParentElement {
	private final SlotType type;
	private final Inventory inventory;
	private final int id;
	private final boolean filter;

	public ConfigSlotElement(Inventory slotInventory,
							int slotId,
							SlotType type,
							int x,
							int y,
							GuiBase<?> gui,
							Runnable closeConfig) {
		super(x, y, type.getButtonSprite());
		this.type = type;
		this.inventory = slotInventory;
		this.id = slotId;
		this.filter = gui.getMachine() instanceof SlotConfiguration.SlotFilter;

		SlotConfigPopupElement popupElement;

		boolean inputEnabled = gui.builtScreenHandler.slots.stream()
				.filter(Objects::nonNull)
				.filter(slot -> slot.inventory == inventory)
				.filter(slot -> slot instanceof BaseSlot)
				.map(slot -> (BaseSlot) slot)
				.filter(baseSlot -> baseSlot.getIndex() == slotId)
				.allMatch(BaseSlot::canWorldBlockInsert);


		elements.add(popupElement = new SlotConfigPopupElement(this.id, x - 22, y - 22, inputEnabled));
		elements.add(new ButtonElement(x + 37, y - 25, Sprite.EXIT_BUTTON, closeConfig));

		if (inputEnabled) {
			elements.add(new CheckBoxElement(Text.translatable("reborncore.gui.slotconfig.autoinput"), x - 26, y + 42,
				checkBoxElement ->  gui.getMachine().getSlotConfiguration().getSlotDetails(slotId).autoInput(),
				() -> popupElement.updateCheckBox("input", gui)));
		}

		elements.add(new CheckBoxElement(Text.translatable("reborncore.gui.slotconfig.autooutput"), x - 26, y + 57,
			checkBoxElement ->  gui.getMachine().getSlotConfiguration().getSlotDetails(slotId).autoOutput(),
			() -> popupElement.updateCheckBox("output", gui)));

		if (gui.getMachine() instanceof SlotConfiguration.SlotFilter slotFilter) {
			if (Arrays.stream(slotFilter.getInputSlots()).anyMatch(value -> value == slotId)) {
				elements.add(new CheckBoxElement(Text.translatable("reborncore.gui.slotconfig.filter_input"), x - 26, y + 72,
					checkBoxElement ->  gui.getMachine().getSlotConfiguration().getSlotDetails(slotId).filter(),
					() -> popupElement.updateCheckBox("filter", gui)));
				popupElement.filter = true;
			}
		}
	}

	@Override
	public int getWidth() {
		return 85;
	}

	@Override
	public int getHeight() {
		return 105 + (filter ? 15 : 0);
	}

	@Override
	public void draw(DrawContext drawContext, GuiBase<?> gui, int mouseX, int mouseY) {
		ItemStack stack = inventory.getStack(id);
		int xPos = getX() + 1 + gui.getGuiLeft();
		int yPos = getY() + 1 + gui.getGuiTop();

		drawContext.drawItemInSlot(gui.getTextRenderer(), stack, xPos, yPos);

		if (isMouseWithinRect(gui, mouseX, mouseY)) {
			drawSprite(drawContext, gui, type.getButtonHoverOverlay(), getX(), getY());
		}
		super.draw(drawContext, gui, mouseX, mouseY);
	}


	public int getId() {
		return id;
	}
}
