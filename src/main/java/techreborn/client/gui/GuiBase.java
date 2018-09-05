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

package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import reborncore.api.tile.IUpgradeable;
import reborncore.common.tile.TileLegacyMachineBase;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.gui.slot.GuiFluidConfiguration;
import techreborn.client.gui.slot.GuiSlotConfiguration;
import techreborn.client.gui.widget.GuiButtonPowerBar;
import techreborn.init.ModItems;
import techreborn.items.DynamicCell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prospector
 */
public class GuiBase extends GuiContainer {

	public int xSize = 176;
	public int ySize = 176;
	public TRBuilder builder = new TRBuilder();
	public TileEntity tile;
	public BuiltContainer container;
	public static SlotConfigType slotConfigType = SlotConfigType.NONE;

	public boolean upgrades;

	public GuiBase(EntityPlayer player, TileEntity tile, BuiltContainer container) {
		super(container);
		this.tile = tile;
		this.container = container;
		slotConfigType = SlotConfigType.NONE;
	}

	protected void drawSlot(int x, int y, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += guiLeft;
			y += guiTop;
		}
		builder.drawSlot(this, x - 1, y - 1);
	}

	protected void drawScrapSlot(int x, int y, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += guiLeft;
			y += guiTop;
		}
		builder.drawScrapSlot(this, x - 1, y - 1);
	}

	protected void drawOutputSlotBar(int x, int y, int count, Layer layer) {
		if (layer == Layer.BACKGROUND) {
			x += guiLeft;
			y += guiTop;
		}
		builder.drawOutputSlotBar(this, x - 4, y - 4, count);
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
		GuiSlotConfiguration.init(this);
		if(getMachine().getTank() != null && getMachine().showTankConfig()){
			GuiFluidConfiguration.init(this);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		builder.drawDefaultBackground(this, guiLeft, guiTop, xSize, ySize);
		if (drawPlayerSlots()) {
			builder.drawPlayerSlots(this, guiLeft + xSize / 2, guiTop + 93, true);
		}
		if (tryAddUpgrades() && tile instanceof IUpgradeable) {
			IUpgradeable upgradeable = (IUpgradeable) tile;
			if (upgradeable.canBeUpgraded()) {
				builder.drawUpgrades(this, upgradeable, guiLeft, guiTop);
				upgrades = true;
			}
		}
		if(getMachine().hasSlotConfig()){
			builder.drawSlotTab(this, guiLeft, guiTop, mouseX, mouseY, upgrades, new ItemStack(ModItems.WRENCH));
		}
		if(getMachine().showTankConfig()){
			builder.drawSlotTab(this, guiLeft, guiTop + 27, mouseX, mouseY, upgrades, DynamicCell.getCellWithFluid(FluidRegistry.LAVA));
		}
	}

	public boolean drawPlayerSlots() {
		return true;
	}

	public boolean tryAddUpgrades() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.buttonList.clear();
		drawTitle();
		if(slotConfigType == SlotConfigType.ITEMS && getMachine().hasSlotConfig()){
			GuiSlotConfiguration.draw(this, mouseX, mouseY);
		}

		if(slotConfigType == SlotConfigType.FLUIDS && getMachine().showTankConfig()){
			GuiFluidConfiguration.draw(this, mouseX, mouseY);
		}

		int offset = 0;
		if(!upgrades){
			offset = 80;
		}
		if (builder.isInRect(guiLeft - 19, guiTop + 92 - offset, 12, 12, mouseX, mouseY) && getMachine().hasSlotConfig()) {
			List<String> list = new ArrayList<>();
			list.add("Configure slots");
			GuiUtils.drawHoveringText(list, mouseX - guiLeft  ,  mouseY - guiTop , width, height, -1, mc.fontRenderer);
			GlStateManager.disableLighting();
			GlStateManager.color(1, 1, 1, 1);
		}
		if (builder.isInRect(guiLeft - 19, guiTop + 92 - offset + 27, 12, 12, mouseX, mouseY) && getMachine().hasSlotConfig()) {
			List<String> list = new ArrayList<>();
			list.add("Configure Fluids");
			GuiUtils.drawHoveringText(list, mouseX - guiLeft  ,  mouseY - guiTop , width, height, -1, mc.fontRenderer);
			GlStateManager.disableLighting();
			GlStateManager.color(1, 1, 1, 1);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	protected void drawTitle() {
		drawCentredString(I18n.format(tile.getBlockType().getTranslationKey() + ".name"), 6, 4210752, Layer.FOREGROUND);
	}

	protected void drawCentredString(String string, int y, int colour, Layer layer) {
		drawString(string, (xSize / 2 - mc.fontRenderer.getStringWidth(string) / 2), y, colour, layer);
	}

	protected void drawCentredString(String string, int y, int colour, int modifier, Layer layer) {
		drawString(string, (xSize / 2 - (mc.fontRenderer.getStringWidth(string)) / 2) + modifier, y, colour, layer);
	}

	protected void drawString(String string, int x, int y, int colour, Layer layer) {
		int factorX = 0;
		int factorY = 0;
		if (layer == Layer.BACKGROUND) {
			factorX = guiLeft;
			factorY = guiTop;
		}
		mc.fontRenderer.drawString(string, x + factorX, y + factorY, colour);
		GlStateManager.color(1, 1, 1, 1);
	}

	public void addPowerButton(int x, int y, int id, Layer layer) {
		int factorX = 0;
		int factorY = 0;
		if (layer == Layer.BACKGROUND) {
			factorX = guiLeft;
			factorY = guiTop;
		}
		buttonList.add(new GuiButtonPowerBar(id, x + factorX, y + factorY, this, layer));
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(slotConfigType == SlotConfigType.ITEMS && getMachine().hasSlotConfig()){
			if(GuiSlotConfiguration.mouseClicked(mouseX, mouseY, mouseButton, this)){
				return;
			}
		}
		if(slotConfigType == SlotConfigType.FLUIDS && getMachine().showTankConfig()){
			if(GuiFluidConfiguration.mouseClicked(mouseX, mouseY, mouseButton, this)){
				return;
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if(slotConfigType == SlotConfigType.ITEMS && getMachine().hasSlotConfig()){
			GuiSlotConfiguration.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick, this);
		}
		if(slotConfigType == SlotConfigType.FLUIDS && getMachine().showTankConfig()){
			GuiFluidConfiguration.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick, this);
		}
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		int offset = 0;
		if(!upgrades){
			offset = 80;
		}
		if(isPointInRegion(-26, 84 - offset, 30, 30, mouseX, mouseY) && getMachine().hasSlotConfig()){
			if(slotConfigType != SlotConfigType.ITEMS){
				slotConfigType = SlotConfigType.ITEMS;
			} else {
				slotConfigType = SlotConfigType.NONE;
			}
			if(slotConfigType == SlotConfigType.ITEMS){
				GuiSlotConfiguration.reset();
			}
		}
		if(isPointInRegion(-26, 84 - offset + 27, 30, 30, mouseX, mouseY) && getMachine().hasSlotConfig()){
			if(slotConfigType != SlotConfigType.FLUIDS){
				slotConfigType = SlotConfigType.FLUIDS;
			} else {
				slotConfigType = SlotConfigType.NONE;
			}
		}
		if(slotConfigType == SlotConfigType.ITEMS && getMachine().hasSlotConfig()){
			if(GuiSlotConfiguration.mouseReleased(mouseX, mouseY, state, this)){
				return;
			}
		}
		if(slotConfigType == SlotConfigType.FLUIDS && getMachine().showTankConfig()){
			if(GuiFluidConfiguration.mouseReleased(mouseX, mouseY, state, this)){
				return;
			}
		}
		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(slotConfigType == SlotConfigType.ITEMS){
			if(isCtrlKeyDown() && keyCode == Keyboard.KEY_C){
				GuiSlotConfiguration.copyToClipboard();
				return;
			} else if(isCtrlKeyDown() && keyCode == Keyboard.KEY_V){
				GuiSlotConfiguration.pasteFromClipboard();
				return;
			}
		}
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void onGuiClosed() {
		slotConfigType = SlotConfigType.NONE;
		super.onGuiClosed();
	}

	public boolean isPointInRect(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
		return super.isPointInRegion(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
	}

	public TileLegacyMachineBase getMachine(){
		return (TileLegacyMachineBase) tile;
	}


	public enum Layer {
		BACKGROUND, FOREGROUND
	}

	public enum SlotConfigType{
		NONE,
		ITEMS,
		FLUIDS
	}
}
