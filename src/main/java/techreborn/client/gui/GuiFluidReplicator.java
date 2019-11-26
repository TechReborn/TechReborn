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

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonExtended;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.client.multiblock.Multiblock;
import techreborn.init.TRContent;
import techreborn.blockentity.machine.multiblock.FluidReplicatorBlockEntity;

/**
 * @author drcrazy
 *
 */
public class GuiFluidReplicator extends GuiBase<BuiltContainer> {

	FluidReplicatorBlockEntity blockEntity;

	public GuiFluidReplicator(int syncID, final PlayerEntity player, final FluidReplicatorBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createContainer(syncID, player));
		this.blockEntity = blockEntity;
	}

	@Override
	protected void drawBackground(final float f, final int mouseX, final int mouseY) {
		super.drawBackground(f, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;
		// Battery slot
		drawSlot(8, 72, layer);
		// Input slot
		drawSlot(55, 45, layer);
		// Liquid input slot
		drawSlot(124, 35, layer);
		// Liquid output slot
		drawSlot(124, 55, layer);
		// JEI button
		builder.drawJEIButton(this, 158, 5, layer);
		if (blockEntity.getMultiBlock()) {
			builder.drawHologramButton(this, 6, 4, mouseX, mouseY, layer);
		}
	}

	@Override
	protected void drawForeground(final int mouseX, final int mouseY) {
		super.drawForeground(mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		builder.drawTank(this, 99, 25, mouseX, mouseY, blockEntity.tank.getFluidInstance(), blockEntity.tank.getCapacity(), blockEntity.tank.isEmpty(), layer);
		builder.drawProgressBar(this, blockEntity.getProgressScaled(100), 100, 76, 48, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		if (blockEntity.getMultiBlock()) {
			addHologramButton(6, 4, 212, layer).clickHandler(this::onClick);
		} else {
			builder.drawMultiblockMissingBar(this, layer);
			addHologramButton(76, 56, 212, layer).clickHandler(this::onClick);
			builder.drawHologramButton(this, 76, 56, mouseX, mouseY, layer);
		}
		builder.drawMultiEnergyBar(this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxPower(), mouseX, mouseY, 0, layer);
	}

	// GuiScreen
	public void onClick(GuiButtonExtended button, Double x, Double y){
		if (GuiBase.slotConfigType == SlotConfigType.NONE) {
			if (blockEntity.renderMultiblock == null) {
				{
					// This code here makes a basic multiblock and then sets to the selected one.
					final Multiblock multiblock = new Multiblock();
					final BlockState reinforcedCasing = TRContent.MachineBlocks.ADVANCED.getCasing().getDefaultState();

					addComponent(1, 0, 0, reinforcedCasing, multiblock);
					addComponent(0, 0, 1, reinforcedCasing, multiblock);
					addComponent(-1, 0, 0, reinforcedCasing, multiblock);
					addComponent(0, 0, -1, reinforcedCasing, multiblock);
					addComponent(-1, 0, -1, reinforcedCasing, multiblock);
					addComponent(-1, 0, 1, reinforcedCasing, multiblock);
					addComponent(1, 0, -1, reinforcedCasing, multiblock);
					addComponent(1, 0, 1, reinforcedCasing, multiblock);

					blockEntity.renderMultiblock = multiblock;
				}
			} else {
				blockEntity.renderMultiblock = null;
			}
		}
	}

	public void addComponent(final int x, final int y, final int z, final BlockState blockState, final Multiblock multiblock) {
		multiblock.addComponent(new BlockPos(
				x - Direction.byId(this.blockEntity.getFacingInt()).getOffsetX() * 2,
				y,
				z - Direction.byId(this.blockEntity.getFacingInt()).getOffsetZ() * 2), blockState);
	}

}
