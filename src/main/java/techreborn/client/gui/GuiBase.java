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

package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.gui.widget.GuiButtonPowerBar;

/**
 * Created by Prospector
 */
public class GuiBase extends GuiContainer {

	public int xSize = 176;
	public int ySize = 176;
	public TRBuilder builder = new TRBuilder();
	public TileEntity tile;
	public BuiltContainer container;

	public GuiBase(EntityPlayer player, TileEntity tile, BuiltContainer container) {
		super(container);
		this.tile = tile;
		this.container = container;
	}

	protected void drawSlot(int x, int y, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += guiLeft;
			y += guiTop;
		}
		builder.drawSlot(this, x - 1, y - 1);
	}

	protected void drawArmourSlots(int x, int y, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += guiLeft;
			y += guiTop;
		}
		builder.drawSlot(this, x - 1, y - 1);
		builder.drawSlot(this, x - 1, y - 1 + 18);
		builder.drawSlot(this, x - 1, y - 1 + 18 + 18);
		builder.drawSlot(this, x - 1, y - 1 + 18 + 18 + 18);
	}

	protected void drawOutputSlot(int x, int y, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += guiLeft;
			y += guiTop;
		}
		builder.drawOutputSlot(this, x - 5, y - 5);
	}

	protected void drawSelectedStack(int x, int y, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += guiLeft;
			y += guiTop;
		}
		builder.drawSelectedStack(this, x, y);
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		builder.drawDefaultBackground(this, guiLeft, guiTop, xSize, ySize);
		builder.drawPlayerSlots(this, guiLeft + xSize / 2, guiTop + 93, true);
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		drawTitle();
	}

	protected void drawTitle() {
		drawCentredString(I18n.translateToLocal(tile.getBlockType().getUnlocalizedName() + ".name"), 6, 4210752, Layer.FOREGROUND);
	}

	protected void drawCentredString(String string, int y, int colour, Layer layer) {
		drawString(string, (xSize / 2 - mc.fontRendererObj.getStringWidth(string) / 2), y, colour, layer);
	}

	protected void drawCentredString(String string, int y, int colour, int modifier, Layer layer) {
		drawString(string, (xSize / 2 - (mc.fontRendererObj.getStringWidth(string)) / 2) + modifier, y, colour, layer);
	}

	protected void drawString(String string, int x, int y, int colour, Layer layer) {
		int factorX = 0;
		int factorY = 0;
		if (layer == Layer.BACKGROUND) {
			factorX = guiLeft;
			factorY = guiTop;
		}
		mc.fontRendererObj.drawString(string, x + factorX, y + factorY, colour);
		GlStateManager.color(1, 1, 1, 1);
	}

	public void addPowerButton(int x, int y, int id, Layer layer) {
		if (id == 0)
			buttonList.clear();
		int factorX = 0;
		int factorY = 0;
		if (layer == Layer.BACKGROUND) {
			factorX = guiLeft;
			factorY = guiTop;
		}
		buttonList.add(new GuiButtonPowerBar(id, x + factorX, y + factorY, this, layer));
	}

	//TODO
	public enum SlotRender {
		STANDARD, OUTPUT, NONE, SPRITE;

	}

	public enum Layer {
		BACKGROUND, FOREGROUND
	}
}
