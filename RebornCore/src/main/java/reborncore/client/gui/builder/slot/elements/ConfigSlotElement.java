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

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.slot.SlotConfigGui;
import reborncore.client.gui.slots.BaseSlot;
import reborncore.common.blockentity.SlotConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConfigSlotElement extends ElementBase {
	SlotType type;
	Inventory inventory;
	int id;
	public List<ElementBase> elements = new ArrayList<>();
	boolean filter = false;

	public ConfigSlotElement(Inventory slotInventory, int slotId, SlotType type, int x, int y, GuiBase<?> gui) {
		super(x, y, type.getButtonSprite());
		this.type = type;
		this.inventory = slotInventory;
		this.id = slotId;

		SlotConfigPopupElement popupElement;

		boolean inputEnabled = gui.builtScreenHandler.slots.stream()
				.filter(Objects::nonNull)
				.filter(slot -> slot.inventory == inventory)
				.filter(slot -> slot instanceof BaseSlot)
				.map(slot -> (BaseSlot) slot)
				.filter(baseSlot -> baseSlot.getSlotID() == slotId)
				.allMatch(BaseSlot::canWorldBlockInsert);


		elements.add(popupElement = new SlotConfigPopupElement(this.id, x - 22, y - 22, this, inputEnabled));
		elements.add(new ButtonElement(x + 37, y - 25, Sprite.EXIT_BUTTON).addReleaseAction((element, gui1, provider, mouseX, mouseY) -> {
			SlotConfigGui.selectedSlot = -1;
			gui.closeSelectedTab();
			return true;
		}));

		if (inputEnabled) {
			elements.add(new CheckBoxElement(new TranslatableText("reborncore.gui.slotconfig.autoinput"), 0xFFFFFFFF, x - 26, y + 42, "input", slotId, Sprite.LIGHT_CHECK_BOX, gui.getMachine(),
					checkBoxElement -> checkBoxElement.machineBase.getSlotConfiguration().getSlotDetails(checkBoxElement.slotID).autoInput()).addPressAction((element, gui12, provider, mouseX, mouseY) -> {
				popupElement.updateCheckBox((CheckBoxElement) element, "input", gui12);
				return true;
			}));
		}

		elements.add(new CheckBoxElement(new TranslatableText("reborncore.gui.slotconfig.autooutput"), 0xFFFFFFFF, x - 26, y + 57, "output", slotId, Sprite.LIGHT_CHECK_BOX, gui.getMachine(),
				checkBoxElement -> checkBoxElement.machineBase.getSlotConfiguration().getSlotDetails(checkBoxElement.slotID).autoOutput()).addPressAction((element, gui13, provider, mouseX, mouseY) -> {
			popupElement.updateCheckBox((CheckBoxElement) element, "output", gui13);
			return true;
		}));

		if (gui.getMachine() instanceof SlotConfiguration.SlotFilter) {
			SlotConfiguration.SlotFilter slotFilter = (SlotConfiguration.SlotFilter) gui.getMachine();
			if (Arrays.stream(slotFilter.getInputSlots()).anyMatch(value -> value == slotId)) {
				elements.add(new CheckBoxElement(new TranslatableText("reborncore.gui.slotconfig.filter_input"), 0xFFFFFFFF, x - 26, y + 72, "filter", slotId, Sprite.LIGHT_CHECK_BOX, gui.getMachine(),
						checkBoxElement -> checkBoxElement.machineBase.getSlotConfiguration().getSlotDetails(checkBoxElement.slotID).filter()).addPressAction((element, gui13, provider, mouseX, mouseY) -> {
					popupElement.updateCheckBox((CheckBoxElement) element, "filter", gui13);
					return true;
				}));
				filter = true;
				popupElement.filter = true;
			}
		}
		setWidth(85);
		setHeight(105 + (filter ? 15 : 0));
	}

	@Override
	public void draw(MatrixStack matrixStack, GuiBase<?> gui) {
		super.draw(matrixStack, gui);
		ItemStack stack = inventory.getStack(id);
		int xPos = x + 1 + gui.getGuiLeft();
		int yPos = y + 1 + gui.getGuiTop();

		RenderSystem.enableDepthTest();
		matrixStack.push();
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
		ItemRenderer renderItem = MinecraftClient.getInstance().getItemRenderer();
		renderItem.renderInGuiWithOverrides(stack, xPos, yPos);
		renderItem.renderGuiItemOverlay(gui.getTextRenderer(), stack, xPos, yPos, null);
		RenderSystem.disableDepthTest();
		RenderSystem.disableLighting();
		matrixStack.pop();
		if (isHovering) {
			drawSprite(matrixStack, gui, type.getButtonHoverOverlay(), x, y);
		}
		elements.forEach(elementBase -> elementBase.draw(matrixStack, gui));
	}

	public SlotType getType() {
		return type;
	}

	public int getId() {
		return id;
	}
}