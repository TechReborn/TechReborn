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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Loader;
import reborncore.api.tile.IUpgradeable;
import reborncore.client.guibuilder.GuiBuilder;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.lib.ModInfo;
import techreborn.proxies.ClientProxy;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.item.ItemStack.EMPTY;

/**
 * Created by Prospector
 */
public class TRBuilder extends GuiBuilder {
	public static final ResourceLocation GUI_SHEET = new ResourceLocation(ModInfo.MOD_ID.toLowerCase() + ":" + "textures/gui/gui_sheet.png");

	public TRBuilder() {
		super(GUI_SHEET);
	}

	public void drawMultiEnergyBar(GuiBase gui, int x, int y, int energyStored, int maxEnergyStored, int mouseX, int mouseY, int buttonID, GuiBase.Layer layer) {
		if(GuiBase.slotConfigType != GuiBase.SlotConfigType.NONE){
			return;
		}
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		if (layer == GuiBase.Layer.FOREGROUND) {
			mouseX -= gui.getGuiLeft();
			mouseY -= gui.getGuiTop();
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_SHEET);

		gui.drawTexturedModalRect(x, y, PowerSystem.getDisplayPower().xBar - 15, PowerSystem.getDisplayPower().yBar - 1, 14, 50);

		int draw = (int) ((double) energyStored / (double) maxEnergyStored * (48));
		if (energyStored > maxEnergyStored) {
			draw = (int) ((double) maxEnergyStored / (double) maxEnergyStored * (48));
		}
		gui.drawTexturedModalRect(x + 1, y + 49 - draw, PowerSystem.getDisplayPower().xBar, 48 + PowerSystem.getDisplayPower().yBar - draw, 12, draw);
		int percentage = percentage(maxEnergyStored, energyStored);
		if (isInRect(x + 1, y + 1, 11, 48, mouseX, mouseY)) {
			List<String> list = new ArrayList<>();
			TextFormatting powerColour = TextFormatting.GOLD;
			list.add(powerColour + PowerSystem.getLocaliszedPowerFormattedNoSuffix(energyStored) + "/" + PowerSystem.getLocaliszedPowerFormattedNoSuffix(maxEnergyStored) + " " + PowerSystem.getDisplayPower().abbreviation);
			list.add(getPercentageColour(percentage) + "" + percentage + "%" + TextFormatting.GRAY + " Charged");
			if(gui.tile instanceof TilePowerAcceptor && GuiScreen.isShiftKeyDown()){
				((TilePowerAcceptor) gui.tile).addInfo(list, true);
				list.add("");
				list.add(TextFormatting.BLUE + "Click to change display unit");
			} else {
				list.add("");
				list.add(TextFormatting.BLUE + "Shift" + TextFormatting.GRAY + " for more info");
			}
			net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(list, mouseX, mouseY, gui.width, gui.height, -1, gui.mc.fontRenderer);
			GlStateManager.disableLighting();
			GlStateManager.color(1, 1, 1, 1);
		}
		gui.addPowerButton(x, y, buttonID, layer);
	}

	public void drawProgressBar(GuiBase gui, int progress, int maxProgress, int x, int y, int mouseX, int mouseY, ProgressDirection direction, GuiBase.Layer layer) {
		if(GuiBase.slotConfigType != GuiBase.SlotConfigType.NONE){
			return;
		}
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		if (layer == GuiBase.Layer.FOREGROUND) {
			mouseX -= gui.getGuiLeft();
			mouseY -= gui.getGuiTop();
		}

		gui.mc.getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(x, y, direction.x, direction.y, direction.width, direction.height);
		int j = (int) ((double) progress / (double) maxProgress * 16);
		if (j < 0) {
			j = 0;
		}

		switch (direction) {
			case RIGHT:
				gui.drawTexturedModalRect(x, y, direction.xActive, direction.yActive, j, 10);
				break;
			case LEFT:
				gui.drawTexturedModalRect(x + 16 - j, y, direction.xActive + 16 - j, direction.yActive, j, 10);
				break;
			case UP:
				gui.drawTexturedModalRect(x, y + 16 - j, direction.xActive, direction.yActive + 16 - j, 10, j);
				break;
			case DOWN:
				gui.drawTexturedModalRect(x, y, direction.xActive, direction.yActive, 10, j);
				break;
			default:
				return;
		}

		if (isInRect(x, y, direction.width, direction.height, mouseX, mouseY)) {
			int percentage = percentage(maxProgress, progress);
			List<String> list = new ArrayList<>();
			list.add(getPercentageColour(percentage) + "" + percentage + "%");
			net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(list, mouseX, mouseY, gui.width, gui.height, -1, gui.mc.fontRenderer);
			GlStateManager.disableLighting();
			GlStateManager.color(1, 1, 1, 1);
		}
	}

	public void drawTank(GuiBase gui, int x, int y, int mouseX, int mouseY, FluidStack fluid, int maxCapacity, boolean isTankEmpty, GuiBase.Layer layer) {
		if(GuiBase.slotConfigType != GuiBase.SlotConfigType.NONE){
			return;
		}
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		if (layer == GuiBase.Layer.FOREGROUND) {
			mouseX -= gui.getGuiLeft();
			mouseY -= gui.getGuiTop();
		}
		int percentage = 0;
		int amount = 0;
		boolean empty = true;
		if (!isTankEmpty) {
			amount = fluid.amount;
			percentage = percentage(maxCapacity, amount);
			empty = false;
		}
		gui.mc.getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(x, y, 228, 18, 22, 56);
		if (!empty)
			drawFluid(gui, fluid, x + 4, y + 4, 14, 48, maxCapacity);
		gui.drawTexturedModalRect(x + 3, y + 3, 231, 74, 16, 50);

		if (isInRect(x, y, 22, 56, mouseX, mouseY)) {
			List<String> list = new ArrayList<>();
			if (empty)
				list.add(TextFormatting.GOLD + "Empty Tank");
			else
				list.add(TextFormatting.GOLD + "" + amount + "mB/" + maxCapacity + "mB " + fluid.getLocalizedName());
			list.add(getPercentageColour(percentage) + "" + percentage + "%" + TextFormatting.GRAY + " Full");
			net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(list, mouseX, mouseY, gui.width, gui.height, -1, gui.mc.fontRenderer);
			GlStateManager.disableLighting();
			GlStateManager.color(1, 1, 1, 1);
		}
	}

	public void drawFluid(GuiBase gui, FluidStack fluid, int x, int y, int width, int height, int maxCapacity) {
		gui.mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		y += height;
		final ResourceLocation still = fluid.getFluid().getStill(fluid);
		final TextureAtlasSprite sprite = gui.mc.getTextureMapBlocks().getAtlasSprite(still.toString());

		final int drawHeight = (int) (fluid.amount / (maxCapacity * 1F) * height);
		final int iconHeight = sprite.getIconHeight();
		int offsetHeight = drawHeight;

		int iteration = 0;
		while (offsetHeight != 0) {
			final int curHeight = offsetHeight < iconHeight ? offsetHeight : iconHeight;
			gui.drawTexturedModalRect(x, y - offsetHeight, sprite, width, curHeight);
			offsetHeight -= curHeight;
			iteration++;
			if (iteration > 50)
				break;
		}
		gui.mc.getTextureManager().bindTexture(GUI_SHEET);
	}

	public void drawJEIButton(GuiBase gui, int x, int y, GuiBase.Layer layer) {
		if(GuiBase.slotConfigType != GuiBase.SlotConfigType.NONE){
			return;
		}
		if (Loader.isModLoaded("jei")) {
			if (layer == GuiBase.Layer.BACKGROUND) {
				x += gui.getGuiLeft();
				y += gui.getGuiTop();
			}
			gui.mc.getTextureManager().bindTexture(GUI_SHEET);
			gui.drawTexturedModalRect(x, y, 184, 70, 20, 12);
		}
	}

	public void drawLockButton(GuiBase gui, int x, int y, int mouseX, int mouseY, GuiBase.Layer layer, boolean locked) {
		if(GuiBase.slotConfigType != GuiBase.SlotConfigType.NONE){
			return;
		}
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		gui.mc.getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(x, y, 204, 70 + (locked ? 12 : 0) , 20, 12);
		if (isInRect(x, y, 20, 12, mouseX, mouseY)) {
			List<String> list = new ArrayList<>();
			if(locked){
				list.add("Unlock items");
			} else {
				list.add("Lock Items");
			}

			GlStateManager.pushMatrix();
			net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(list, mouseX, mouseY, gui.width, gui.height, 80, gui.mc.fontRenderer);
			GlStateManager.popMatrix();
		}
	}

	public void drawHologramButton(GuiBase gui, int x, int y, int mouseX, int mouseY, GuiBase.Layer layer) {
		if(GuiBase.slotConfigType != GuiBase.SlotConfigType.NONE){
			return;
		}
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		if (layer == GuiBase.Layer.FOREGROUND) {
			mouseX -= gui.getGuiLeft();
			mouseY -= gui.getGuiTop();
		}
		gui.mc.getTextureManager().bindTexture(GUI_SHEET);
		if (ClientProxy.multiblockRenderEvent.currentMultiblock == null) {
			gui.drawTexturedModalRect(x, y, 184, 94, 20, 12);
		} else {
			gui.drawTexturedModalRect(x, y, 184, 106, 20, 12);
		}
		if (isInRect(x, y, 20, 12, mouseX, mouseY)) {
			List<String> list = new ArrayList<>();
			list.add("Toggle Multiblock Hologram");
			GlStateManager.pushMatrix();
			net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(list, mouseX, mouseY, gui.width, gui.height, -1, gui.mc.fontRenderer);
			GlStateManager.popMatrix();
		}
	}
	
	public void drawUpDownButtons(GuiBase gui, int x, int y, GuiBase.Layer layer){
		if(GuiBase.slotConfigType != GuiBase.SlotConfigType.NONE){
			return;
		}
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		gui.mc.getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(x, y, 150, 70, 12, 12);
		gui.drawTexturedModalRect(x + 12, y, 150, 82, 12, 12);
		gui.drawTexturedModalRect(x + 24, y, 150, 94, 12, 12);
		gui.drawTexturedModalRect(x + 36, y, 150, 106, 12, 12);
	}

	public void drawUpDownButtonsSmall(GuiBase gui, int x, int y, GuiBase.Layer layer){
		if(GuiBase.slotConfigType != GuiBase.SlotConfigType.NONE){
			return;
		}
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		gui.mc.getTextureManager().bindTexture(GUI_SHEET);
		//gui.drawTexturedModalRect(x, y, 150, 70, 12, 12);
		gui.drawTexturedModalRect(x + 12, y, 150, 82, 12, 12);
		gui.drawTexturedModalRect(x + 24, y, 150, 94, 12, 12);
		//gui.drawTexturedModalRect(x + 36, y, 150, 106, 12, 12);
	}
	
	public void drawEnergyOutput(GuiBase gui, int right, int top, int maxOutput, GuiBase.Layer layer){
		if(GuiBase.slotConfigType != GuiBase.SlotConfigType.NONE){
			return;
		}
		String text = PowerSystem.getLocaliszedPowerFormattedNoSuffix(maxOutput) + " "
				+ PowerSystem.getDisplayPower().abbreviation + "/t";
		int width = gui.mc.fontRenderer.getStringWidth(text);
		gui.drawString(text, right - 17 - width, top + 5, 0, layer);
		
		if (layer == GuiBase.Layer.BACKGROUND) {
			right += gui.getGuiLeft();
			top += gui.getGuiTop();
		}
		gui.mc.getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(right - 16, top, 162, 101, 16, 17);
	}

	public void drawBigBlueBar(GuiBase gui, int x, int y, int value, int max, int mouseX, int mouseY, String suffix, GuiBase.Layer layer) {
		if(GuiBase.slotConfigType != GuiBase.SlotConfigType.NONE){
			return;
		}
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		gui.mc.getTextureManager().bindTexture(GUI_SHEET);
		if (!suffix.equals("")) {
			suffix = " " + suffix;
		}
		gui.drawTexturedModalRect(x, y, 0, 218, 114, 18);
		int j = (int) ((double) value / (double) max * 106);
		if (j < 0)
			j = 0;
		gui.drawTexturedModalRect(x + 4, y + 4, 0, 236, j, 10);
		gui.drawCentredString(value + suffix, y + 5, 0xFFFFFF, layer);
		if (isInRect(x, y, 114, 18, mouseX, mouseY)) {
			int percentage = percentage(max, value);
			List<String> list = new ArrayList<>();
			list.add("" + TextFormatting.GOLD + value + "/" + max + suffix);
			list.add(getPercentageColour(percentage) + "" + percentage + "%" + TextFormatting.GRAY + " Full");

			if (value > max) {
				list.add(TextFormatting.GRAY + "Yo this is storing more than it should be able to");
				list.add(TextFormatting.GRAY + "prolly a bug");
				list.add(TextFormatting.GRAY + "pls report and tell how tf you did this");
			}
			net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(list, mouseX, mouseY, gui.width, gui.height, -1, gui.mc.fontRenderer);
			GlStateManager.disableLighting();
			GlStateManager.color(1, 1, 1, 1);
		}
	}

	public void drawBigHeatBar(GuiBase gui, int x, int y, int value, int max, GuiBase.Layer layer) {
		if(GuiBase.slotConfigType != GuiBase.SlotConfigType.NONE){
			return;
		}
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		gui.mc.getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(x, y, 0, 218, 114, 18);
		if (value != 0) {
			int j = (int) ((double) value / (double) max * 106);
			if (j < 0)
				j = 0;
			gui.drawTexturedModalRect(x + 4, y + 4, 0, 246, j, 10);
			gui.drawCentredString(value + " Heat", y + 5, 0xFFFFFF, layer);

		}
	}

	public void drawMultiblockMissingBar(GuiBase gui, GuiBase.Layer layer) {
		if(GuiBase.slotConfigType != GuiBase.SlotConfigType.NONE){
			return;
		}
		int x = 0;
		int y = 4;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		gui.mc.getTextureManager().bindTexture(GUI_SHEET);
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.colorMask(true, true, true, false);
		GuiUtils.drawGradientRect(0, x, y, x + 176, y + 20, 0x000000, 0xC0000000);
		GuiUtils.drawGradientRect(0, x, y + 20, x + 176, y + 20 + 48, 0xC0000000, 0xC0000000);
		GuiUtils.drawGradientRect(0, x, y + 68, x + 176, y + 70 + 20, 0xC0000000, 0x00000000);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.enableDepth();
		gui.drawCentredString(I18n.format("techreborn.message.missingmultiblock"), 43, 0xFFFFFF, layer);
	}

	public void drawBigBlueBar(GuiBase gui, int x, int y, int value, int max, int mouseX, int mouseY, GuiBase.Layer layer) {
		drawBigBlueBar(gui, x, y, value, max, mouseX, mouseY, "", layer);
	}

	public void drawSelectedStack(GuiBase gui, int x, int y) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(x - 4, y - 4, 202, 44, 24, 24);
	}

	public void drawBurnBar(GuiBase gui, int progress, int maxProgress, int x, int y, int mouseX, int mouseY, GuiBase.Layer layer) {
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		if (layer == GuiBase.Layer.FOREGROUND) {
			mouseX -= gui.getGuiLeft();
			mouseY -= gui.getGuiTop();
		}

		gui.mc.getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(x, y, 171, 84, 13, 13);
		int j = 13 - (int) ((double) progress / (double) maxProgress * 13);
		if (j > 0) {
			gui.drawTexturedModalRect(x, y + j, 171, 70 + j, 13, 13 - j);

		}
		if (isInRect(x, y, 12, 12, mouseX, mouseY)) {
			int percentage = percentage(maxProgress, progress);
			List<String> list = new ArrayList<>();
			list.add(getPercentageColour(percentage) + "" + percentage + "%");
			net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(list, mouseX, mouseY, gui.width, gui.height, -1, gui.mc.fontRenderer);
			GlStateManager.disableLighting();
			GlStateManager.color(1, 1, 1, 1);
		}
	}

	@Override
	public void drawSlot(GuiScreen gui, int posX, int posY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(posX, posY, 150, 0, 18, 18);
	}

	public void drawUpgrades(GuiScreen gui, IUpgradeable upgradeable, int posX, int posY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(posX - 27, posY + 4, 126, 151, 30, 87);
	}

	public void drawSlotTab(GuiScreen gui, int posX, int posY, int mouseX, int mouseY, boolean upgrades, ItemStack stack){
		int offset = -1;
		if(!upgrades){
			offset = 80;
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(posX - 26, posY + 84 - offset, 157, 149, 30, 30);
		renderItemStack(stack, posX - 19, posY + 92 - offset);
	}

	public void renderItemStack(ItemStack stack, int x, int y) {
		if (stack != EMPTY) {
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			RenderHelper.enableGUIStandardItemLighting();

			RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
			itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);

			GlStateManager.disableLighting();
		}
	}

	public void drawScrapSlot(GuiScreen gui, int posX, int posY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(posX, posY, 150, 0, 18, 18);
	}

	public void drawOutputSlotBar(GuiScreen gui, int posX, int posY, int count) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_SHEET);
		for (int i = 1; i <= count; i++) {
			if (i == 1) {
				gui.drawTexturedModalRect(posX, posY, 125 + 39, 218, 22, 26);
				posX += 22;
				if (1 == count) {
					gui.drawTexturedModalRect(posX, posY, 147 + 39, 218, 4, 26);
				}
			} else if (i != 1 && i != count) {
				gui.drawTexturedModalRect(posX, posY, 127 + 39, 218, 20, 26);
				posX += 20;
			} else if (i == count) {
				gui.drawTexturedModalRect(posX, posY, 127 + 39, 218, 24, 26);
				posX += 24;
			}
		}
	}

	@Override
	public void drawOutputSlot(GuiScreen gui, int posX, int posY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(posX, posY, 150, 18, 26, 26);
	}

	public int getScaledBurnTime(int scale, int burnTime, int totalBurnTime) {
		return (int) (((float) burnTime / (float) totalBurnTime) * scale);
	}

	public TextFormatting getPercentageColour(int percentage) {
		if (percentage <= 10) {
			return TextFormatting.RED;
		} else if (percentage >= 75) {
			return TextFormatting.GREEN;
		} else {
			return TextFormatting.YELLOW;
		}
	}

	public int percentage(int MaxValue, int CurrentValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}

	public enum ProgressDirection {
		RIGHT(84, 151, 100, 151, 16, 10), LEFT(100, 161, 84, 161, 16, 10), DOWN(104, 171, 114, 171, 10, 16), UP(84, 171, 94, 171, 10, 16);
		public int x;
		public int y;
		public int xActive;
		public int yActive;
		public int width;
		public int height;

		ProgressDirection(int x, int y, int xActive, int yActive, int width, int height) {
			this.x = x;
			this.y = y;
			this.xActive = xActive;
			this.yActive = yActive;
			this.width = width;
			this.height = height;
		}
	}
}
