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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import reborncore.client.multiblock.Multiblock;
import reborncore.client.multiblock.MultiblockRenderEvent;
import reborncore.client.multiblock.MultiblockSet;
import techreborn.client.gui.widget.GuiButtonHologram;
import techreborn.init.ModBlocks;
import techreborn.proxies.ClientProxy;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;

import java.io.IOException;

public class GuiFusionReactor extends GuiBase {
	TileFusionControlComputer tile;

	public GuiFusionReactor(final EntityPlayer player, final TileFusionControlComputer tile) {
		super(player, tile, tile.createContainer(player));
		this.tile = tile;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		this.drawSlot(34, 47, layer);
		this.drawSlot(126, 47, layer);
		this.drawOutputSlot(80, 47, layer);

		this.builder.drawJEIButton(this, 150, 4, layer);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		this.builder.drawProgressBar(this, this.tile.getProgressScaled(100), 100, 55, 51, mouseX, mouseY, TRBuilder.ProgressDirection.RIGHT, layer);
		this.builder.drawProgressBar(this, this.tile.getProgressScaled(100), 100, 105, 51, mouseX, mouseY, TRBuilder.ProgressDirection.LEFT, layer);
		this.builder.drawMultiEnergyBar(this, 9, 19, (int) this.tile.getEnergy(), (int) this.tile.getMaxPower(), mouseX, mouseY, 0, layer);
		if (tile.checkCoils()) {
			addHologramButton(6, 4, 212, layer);
			builder.drawHologramButton(this, 6, 4, mouseX, mouseY, layer);
		} else {
			builder.drawMultiblockMissingBar(this, layer);
			addHologramButton(76, 56, 212, layer);
			builder.drawHologramButton(this, 76, 56, mouseX, mouseY, layer);
		}
	}
	
	public void addHologramButton(int x, int y, int id, Layer layer) {
		if (id == 0)
			buttonList.clear();
		int factorX = 0;
		int factorY = 0;
		if (layer == Layer.BACKGROUND) {
			factorX = guiLeft;
			factorY = guiTop;
		}
		buttonList.add(new GuiButtonHologram(id, x + factorX, y + factorY, this, layer));
	}
	
	@Override
	public void actionPerformed(final GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 212 && !GuiBase.showSlotConfig) {
			if (ClientProxy.multiblockRenderEvent.currentMultiblock == null) {
				// This code here makes a basic multiblock and then sets to the selected one.
				final Multiblock multiblock = new Multiblock();
				IBlockState coil = ModBlocks.FUSION_COIL.getDefaultState();
				
				this.addComponent(3, 0, 1, coil, multiblock);
				this.addComponent(3, 0, 0, coil, multiblock);
				this.addComponent(3, 0, -1, coil, multiblock);
				this.addComponent(-3, 0, 1, coil, multiblock);
				this.addComponent(-3, 0, 0, coil, multiblock);
				this.addComponent(-3, 0, -1, coil, multiblock);
				this.addComponent(2, 0, 2, coil, multiblock);
				this.addComponent(2, 0, 1, coil, multiblock);
				this.addComponent(2, 0, -1, coil, multiblock);
				this.addComponent(2, 0, -2, coil, multiblock);
				this.addComponent(-2, 0, 2, coil, multiblock);
				this.addComponent(-2, 0, 1, coil, multiblock);
				this.addComponent(-2, 0, -1, coil, multiblock);
				this.addComponent(-2, 0, -2, coil, multiblock);
				this.addComponent(1, 0, 3, coil, multiblock);
				this.addComponent(1, 0, 2, coil, multiblock);
				this.addComponent(1, 0, -2, coil, multiblock);
				this.addComponent(1, 0, -3, coil, multiblock);
				this.addComponent(-1, 0, 3, coil, multiblock);
				this.addComponent(-1, 0, 2, coil, multiblock);
				this.addComponent(-1, 0, -2, coil, multiblock);
				this.addComponent(-1, 0, -3, coil, multiblock);
				this.addComponent(0, 0, 3, coil, multiblock);
				this.addComponent(0, 0, -3, coil, multiblock);
				
				final MultiblockSet set = new MultiblockSet(multiblock);
				ClientProxy.multiblockRenderEvent.setMultiblock(set);
				ClientProxy.multiblockRenderEvent.parent = this.tile.getPos();
				MultiblockRenderEvent.anchor = new BlockPos(this.tile.getPos().getX(), this.tile.getPos().getY() - 1,
						this.tile.getPos().getZ());
			} else {
				ClientProxy.multiblockRenderEvent.setMultiblock(null);
			}
		}
	}
	
	public void addComponent(final int x, final int y, final int z, final IBlockState blockState, final Multiblock multiblock) {
		multiblock.addComponent(new BlockPos(x, y, z), blockState);
	}
}
