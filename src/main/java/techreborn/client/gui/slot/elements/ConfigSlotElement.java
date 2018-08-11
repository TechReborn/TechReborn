/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.common.recipes.RecipeCrafter;
import techreborn.client.gui.GuiBase;
import techreborn.client.gui.slot.GuiSlotConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigSlotElement extends ElementBase {
	SlotType type;
	IInventory inventory;
	int id;
	public List<ElementBase> elements = new ArrayList<>();
	boolean filter = false;


	public ConfigSlotElement(IInventory slotInventory, int slotId, SlotType type, int x, int y, GuiBase gui) {
		super(x, y, type.getButtonSprite());
		this.type = type;
		this.inventory = slotInventory;
		this.id = slotId;

		SlotConfigPopupElement popupElement;

		elements.add(popupElement = new SlotConfigPopupElement(this.id, x - 22, y - 22, this));
		elements.add(new ButtonElement(x + 37, y - 25, Sprite.EXIT_BUTTON).addReleaseAction((element, gui1, provider, mouseX, mouseY) -> {
			GuiSlotConfiguration.slectedSlot = -1;
			GuiBase.slotConfigType = GuiBase.SlotConfigType.NONE;
			return true;
		}));

		elements.add(new CheckBoxElement("Auto Input", 0xFFFFFFFF, x - 26, y + 42, "input", slotId, Sprite.LIGHT_CHECK_BOX, gui.getMachine(),
			checkBoxElement -> checkBoxElement.machineBase.slotConfiguration.getSlotDetails(checkBoxElement.slotID).autoInput()).addPressAction((element, gui12, provider, mouseX, mouseY) -> {
			popupElement.updateCheckBox((CheckBoxElement) element, "input", gui12);
			return true;
		}));
		elements.add(new CheckBoxElement("Auto Output", 0xFFFFFFFF, x - 26, y + 57, "output", slotId, Sprite.LIGHT_CHECK_BOX, gui.getMachine(),
			checkBoxElement -> checkBoxElement.machineBase.slotConfiguration.getSlotDetails(checkBoxElement.slotID).autoOutput()).addPressAction((element, gui13, provider, mouseX, mouseY) -> {
			popupElement.updateCheckBox((CheckBoxElement) element, "output", gui13);
			return true;
		}));

		if(gui.getMachine() instanceof IRecipeCrafterProvider){
			RecipeCrafter recipeCrafter = ((IRecipeCrafterProvider) gui.getMachine()).getRecipeCrafter();
			if(Arrays.stream(recipeCrafter.inputSlots).anyMatch(value -> value == slotId)){
				elements.add(new CheckBoxElement("Filter Input", 0xFFFFFFFF, x - 26, y + 72, "filter", slotId, Sprite.LIGHT_CHECK_BOX, gui.getMachine(),
					checkBoxElement -> checkBoxElement.machineBase.slotConfiguration.getSlotDetails(checkBoxElement.slotID).filter()).addPressAction((element, gui13, provider, mouseX, mouseY) -> {
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
	public void draw(GuiBase gui) {
		super.draw(gui);
		ItemStack stack = inventory.getStackInSlot(id);
		int xPos = x + 1 + gui.guiLeft;
		int yPos = y + 1 + gui.guiTop;

		GlStateManager.enableDepth();
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableGUIStandardItemLighting();
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.renderItemAndEffectIntoGUI(gui.mc.player, stack, xPos, yPos);
		renderItem.renderItemOverlayIntoGUI(gui.mc.fontRenderer, stack, xPos, yPos, null);
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
		if (isHovering) {
			drawSprite(gui, type.getButtonHoverOverlay(), x, y);
		}
		elements.forEach(elementBase -> elementBase.draw(gui));
	}

	public SlotType getType() {
		return type;
	}

	public int getId() {
		return id;
	}
}