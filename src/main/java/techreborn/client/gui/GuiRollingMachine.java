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

import net.minecraft.entity.player.EntityPlayer;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.common.network.NetworkManager;
import techreborn.packets.ServerboundPackets;
import techreborn.tiles.machine.tier1.TileRollingMachine;

import java.io.IOException;

public class GuiRollingMachine extends GuiBase {

	TileRollingMachine rollingMachine;

	public GuiRollingMachine(final EntityPlayer player, final TileRollingMachine tileRollingmachine) {
		super(player, tileRollingmachine,  tileRollingmachine.createContainer(player));
		this.rollingMachine = tileRollingmachine;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		int gridYPos = 22;
		drawSlot(30, gridYPos, layer);
		drawSlot(48, gridYPos, layer);         
		drawSlot(66, gridYPos, layer);
		drawSlot(30, gridYPos + 18, layer);
		drawSlot(48, gridYPos + 18, layer); 
		drawSlot(66, gridYPos + 18, layer);
		drawSlot(30, gridYPos + 36, layer); 
		drawSlot(48, gridYPos + 36, layer); 
		drawSlot(66, gridYPos + 36, layer);

		drawSlot(8, 70, layer);
		drawOutputSlot(124, gridYPos + 18, layer);

		builder.drawJEIButton(this, 158, 5, layer);
		builder.drawLockButton(this, 130, 4, mouseX, mouseY, layer,rollingMachine.locked);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		builder.drawProgressBar(this, rollingMachine.getProgressScaled(100), 100, 92, 43, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		builder.drawMultiEnergyBar(this, 9, 17, (int) rollingMachine.getEnergy(), (int) rollingMachine.getMaxPower(), mouseX, mouseY, 0, layer);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(isPointInRect(130, 4, 20, 12, mouseX, mouseY)){
			NetworkManager.sendToServer(ServerboundPackets.createPacketRollingMachineLock(rollingMachine, !rollingMachine.locked));
			return;
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
