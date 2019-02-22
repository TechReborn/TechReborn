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

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import reborncore.ClientProxy;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.client.multiblock.Multiblock;
import reborncore.client.multiblock.MultiblockRenderEvent;
import reborncore.client.multiblock.MultiblockSet;
import techreborn.init.TRContent;
import techreborn.tiles.machine.multiblock.TileImplosionCompressor;
import techreborn.blocks.BlockMachineCasing;
import reborncore.client.gui.builder.widget.GuiButtonHologram;

import java.io.IOException;

public class GuiImplosionCompressor extends GuiBase {

	TileImplosionCompressor tile;

	public GuiImplosionCompressor(final EntityPlayer player, final TileImplosionCompressor tile) {
		super(player, tile, tile.createContainer(player));
		this.tile = tile;
	}

	@Override
	public void initGui() {
		super.initGui();
		ClientProxy.multiblockRenderEvent.setMultiblock(null);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		final GuiBase.Layer layer = Layer.BACKGROUND;

		drawSlot(8, 72, layer);
		
		drawSlot(50, 27, layer);
		drawSlot(50, 47, layer);
		drawSlot(92, 36, layer);
		drawSlot(110, 36, layer);

		if (tile.getMutliBlock()) {
			builder.drawHologramButton(this, 6, 4, mouseX, mouseY, layer);
		}

		builder.drawJEIButton(this, 158, 5, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final GuiBase.Layer layer = Layer.FOREGROUND;

		builder.drawProgressBar(this, tile.getProgressScaled(100), 100, 71, 40, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		if (tile.getMutliBlock()) {
			addHologramButton(6, 4, 212, layer);
		} else {
			builder.drawMultiblockMissingBar(this, layer);
			addHologramButton(76, 56, 212, layer);
			builder.drawHologramButton(this, 76, 56, mouseX, mouseY, layer);
		}
		builder.drawMultiEnergyBar(this, 9, 19, (int) tile.getEnergy(), (int) tile.getMaxPower(), mouseX, mouseY, 0, layer);

	}

	@Override
	public void actionPerformed(final GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 212 && GuiBase.slotConfigType == SlotConfigType.NONE) {
			if (ClientProxy.multiblockRenderEvent.currentMultiblock == null) {
				{
					// This code here makes a basic multiblock and then sets to the selected one.
					final Multiblock multiblock = new Multiblock();
					for (int x = -1; x <= 1; x++) {
						for (int y = -4; y <= -2; y++) {
							for (int z = -1; z <= 1; z++) {
								if (!((x == 0) && (y == -3) && (z == 0))) {
									this.addComponent(x, y, z, TRContent.MachineBlocks.ADVANCED.getCasing().getDefaultState(), multiblock);
								}
							}
						}
					}

					final MultiblockSet set = new MultiblockSet(multiblock);
					ClientProxy.multiblockRenderEvent.setMultiblock(set);
					ClientProxy.multiblockRenderEvent.parent = tile.getPos();
					MultiblockRenderEvent.anchor = new BlockPos(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
				}
			} else {
				ClientProxy.multiblockRenderEvent.setMultiblock(null);
			}
		}
	}

	public void addComponent(final int x, final int y, final int z, final IBlockState blockState, final Multiblock multiblock) {
		multiblock.addComponent(new BlockPos(x, y, z), blockState);
	}
}
