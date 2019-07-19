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

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import reborncore.client.gui.builder.GuiBase;
import reborncore.common.network.NetworkManager;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.client.gui.builder.widget.GuiButtonUpDown;
import techreborn.packets.PacketAesu;
import techreborn.packets.PacketRedstoneMode;
import techreborn.tiles.storage.TileAdjustableSU;
import techreborn.tiles.storage.TileEnergyStorage;

import java.io.IOException;

public class GuiAESU extends GuiBase {

	TileAdjustableSU tile;

	public GuiAESU(final EntityPlayer player, final TileAdjustableSU aesu) {
		super(player, aesu, aesu.createContainer(player));
		this.tile = aesu;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		drawSlot(62, 45, layer);
		drawSlot(98, 45, layer);
		drawArmourSlots(8, 18, layer);
		builder.drawEnergyOutput(this, 153, 61, tile.getCurrentOutput(), layer);
		builder.drawUpDownButtons(this, 121, 79, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		if(GuiBase.slotConfigType == SlotConfigType.NONE){
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.6, 0.6, 1);
			drawCentredString(PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) tile.getEnergy()) + "/"
				+ PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) tile.getMaxPower()) + " "
				+ PowerSystem.getDisplayPower().abbreviation, 35, 0, 58, layer);
			GlStateManager.popMatrix();
		}
	
		builder.drawMultiEnergyBar(this, 81, 28, (int) tile.getEnergy(), (int) tile.getMaxPower(), mouseX, mouseY, 0, layer);
		
		buttonList.add(new GuiButtonUpDown(300, 121, 79, this, layer));
		buttonList.add(new GuiButtonUpDown(301, 121 + 12, 79, this, layer));
		buttonList.add(new GuiButtonUpDown(302, 121 + 24, 79, this, layer));
		buttonList.add(new GuiButtonUpDown(303, 121 + 36, 79, this, layer));

		builder.drawEnergyStorageRedstoneModeButton(this, 154, 5, mouseX, mouseY, layer, tile.redstoneMode);
	}
	
	@Override
	public void actionPerformed(final GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id >= 300 && button.id <= 303) {
			boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
			boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
			NetworkManager.sendToServer(new PacketAesu(button.id, tile, shift, ctrl));
		} 
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (isPointInRect(154, 5, 16, 15, mouseX, mouseY)) {
			byte currentMode = tile.redstoneMode;
			NetworkManager.sendToServer(new PacketRedstoneMode(tile, (++currentMode >= TileEnergyStorage.redstoneModes ? 0 : currentMode)));
			return;
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
