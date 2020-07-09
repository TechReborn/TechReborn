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

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import reborncore.client.ClientChunkManager;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonSimple;
import reborncore.client.gui.builder.widget.GuiButtonUpDown;
import reborncore.client.gui.builder.widget.GuiButtonUpDown.UpDownButtonType;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.common.network.NetworkManager;
import techreborn.blockentity.machine.tier3.ChunkLoaderBlockEntity;
import techreborn.packets.ServerboundPackets;

public class GuiChunkLoader extends GuiBase<BuiltScreenHandler> {

	ChunkLoaderBlockEntity blockEntity;

	public GuiChunkLoader(int syncID, PlayerEntity player, ChunkLoaderBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
		this.blockEntity = blockEntity;
	}

	public void init() {
		super.init();
		addButton(new GuiButtonUpDown(x + 64, y + 40, this, b -> onClick(5), UpDownButtonType.FASTFORWARD));
		addButton(new GuiButtonUpDown(x + 64 + 12, y + 40, this, b -> onClick(1), UpDownButtonType.FORWARD));
		addButton(new GuiButtonUpDown(x + 64 + 24, y + 40, this, b -> onClick(-1), UpDownButtonType.REWIND));
		addButton(new GuiButtonUpDown(x + 64 + 36, y + 40, this, b -> onClick(-5), UpDownButtonType.FASTREWIND));

		addButton(new GuiButtonSimple(x + 10, y + 70, 155, 20, new LiteralText("Toggle Loaded Chunks"), b -> ClientChunkManager.toggleLoadedChunks(blockEntity.getPos())));
	}

	@Override
	protected void drawBackground(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		super.drawBackground(matrixStack, partialTicks, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		if (hideGuiElements()) return;

		Text text = new LiteralText("Radius: ")
				.append(String.valueOf(blockEntity.getRadius()));
		drawCentredText(matrixStack, text, 25, 4210752, layer);
	}

	public void onClick(int amount) {
		NetworkManager.sendToServer(ServerboundPackets.createPacketChunkloader(amount, blockEntity, ClientChunkManager.hasChunksForLoader(blockEntity.getPos())));
	}
}
