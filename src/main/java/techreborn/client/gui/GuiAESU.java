/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonUpDown;
import reborncore.client.gui.builder.widget.GuiButtonUpDown.UpDownButtonType;
import reborncore.common.network.NetworkManager;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.blockentity.storage.energy.AdjustableSUBlockEntity;
import techreborn.packets.ServerboundPackets;

public class GuiAESU extends GuiBase<BuiltContainer> {

	AdjustableSUBlockEntity blockEntity;

	public GuiAESU(int syncID, final PlayerEntity player, final AdjustableSUBlockEntity aesu) {
		super(player, aesu, aesu.createContainer(syncID, player));
		this.blockEntity = aesu;
	}
	
	@Override
	public void init() {
		super.init();
		addButton(new GuiButtonUpDown(x + 121, y + 79, this, b -> onClick(256), UpDownButtonType.FASTFORWARD));
		addButton(new GuiButtonUpDown(x + 121 + 12, y + 79, this, b -> onClick(64), UpDownButtonType.FORWARD));
		addButton(new GuiButtonUpDown(x + 121 + 24, y + 79, this, b -> onClick(-64), UpDownButtonType.REWIND));
		addButton(new GuiButtonUpDown(x + 121 + 36, y + 79, this, b -> onClick(-256), UpDownButtonType.FASTREWIND));
	}

	@Override
	protected void drawBackground(final float f, final int mouseX, final int mouseY) {
		super.drawBackground(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		this.drawSlot(62, 45, layer);
		this.drawSlot(98, 45, layer);
		this.drawArmourSlots(8, 18, layer);
		this.builder.drawEnergyOutput(this, 155, 61, this.blockEntity.getCurrentOutput(), layer);
	}

	@Override
	protected void drawForeground(final int mouseX, final int mouseY) {
		super.drawForeground(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		if(!isTabOpen()){
			RenderSystem.pushMatrix();
			RenderSystem.scaled(0.6, 0.6, 1);
			drawCentredString(PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) blockEntity.getEnergy()) + "/"
				+ PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) blockEntity.getMaxPower()) + " "
				+ PowerSystem.getDisplayPower().abbreviation, 35, 0, 58, layer);
			RenderSystem.popMatrix();
		}
	
		builder.drawMultiEnergyBar(this, 81, 28, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxPower(), mouseX, mouseY, 0, layer);
	}

	public void onClick(int amount){
		NetworkManager.sendToServer(ServerboundPackets.createPacketAesu(amount, Screen.hasShiftDown(), Screen.hasControlDown(), blockEntity));
	}
}
