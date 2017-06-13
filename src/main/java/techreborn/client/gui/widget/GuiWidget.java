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

package techreborn.client.gui.widget;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.ArrayList;

public abstract class GuiWidget<T extends Container> extends GuiContainer {

	public static final LanguageMap translate = ObfuscationReflectionHelper.getPrivateValue(LanguageMap.class, null, 2);

	private final ArrayList<Widget> widgets = new ArrayList<>();
	private final ResourceLocation background;

	public GuiWidget(T inventorySlotsIn, ResourceLocation background, int xSize, int ySize) {
		super(inventorySlotsIn);
		this.xSize = xSize;
		this.ySize = ySize;
		this.background = background;
	}

	public T getContainer() {
		return (T) inventorySlots;
	}

	@Override
	public void initGui() {
		super.initGui();
		widgets.clear();
		initWidgets();
	}

	public void addWidget(Widget widget) {
		widgets.add(widget);
	}

	public void removeWidget(Widget widget) {
		widgets.remove(widget);
	}

	public abstract void initWidgets();

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(background);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		String name = translate.translateKey("tile.techreborn.industrialgrinder.name");

		fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 4210752);
		fontRenderer.drawString(translate.translateKey("container.inventory"), 8, ySize - 94, 4210752);

		for (Widget widget : widgets)
			widget.drawWidget(this, x, y, mouseX, mouseY);
	}

	public FontRenderer getFontRenderer() {
		return fontRenderer;
	}

}
