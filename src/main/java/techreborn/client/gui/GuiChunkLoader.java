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

import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonUpDown;
import reborncore.client.gui.builder.widget.GuiButtonUpDown.UpDownButtonType;
import reborncore.common.network.NetworkManager;
import techreborn.blockentity.machine.tier3.ChunkLoaderBlockEntity;
import techreborn.packets.ServerboundPackets;

public class GuiChunkLoader extends GuiBase<BuiltContainer> {

	ChunkLoaderBlockEntity blockEntity;

	public GuiChunkLoader(int syncID, PlayerEntity player, ChunkLoaderBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createContainer(syncID, player));
		this.blockEntity = blockEntity;
	}
	
	public void init() {
		addButton(new GuiButtonUpDown(left + 64, top + 40, this, b -> onClick(5), UpDownButtonType.FASTFORWARD));
		addButton(new GuiButtonUpDown(left + 64 + 12, top + 40, this, b -> onClick(1), UpDownButtonType.FORWARD));
		addButton(new GuiButtonUpDown(left + 64 + 24, top + 40, this, b -> onClick(-1), UpDownButtonType.REWIND));
		addButton(new GuiButtonUpDown(left + 64 + 36, top + 40, this, b -> onClick(-5), UpDownButtonType.FASTREWIND));
	}
	
	@Override
	protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
		super.drawBackground(partialTicks, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;
		
		// Battery slot
		drawSlot(8, 72, layer);
		if (GuiBase.slotConfigType != GuiBase.SlotConfigType.NONE) {
			return;
		}
		String text = "Raidus: " + String.valueOf(blockEntity.getRadius());
		drawCentredString(text, 25, 4210752, layer);
	}
	
	@Override
	protected void drawForeground(final int mouseX, final int mouseY) {
		super.drawForeground(mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		builder.drawMultiEnergyBar(this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxPower(), mouseX, mouseY, 0, layer);
	}
	
	public void onClick(int amount){
		NetworkManager.sendToServer(ServerboundPackets.createPacketChunkloader(amount, blockEntity));
	}	
}
