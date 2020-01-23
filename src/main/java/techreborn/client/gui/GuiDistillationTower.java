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
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.widget.GuiButtonExtended;
import reborncore.client.gui.guibuilder.GuiBuilder;
import reborncore.client.multiblock.Multiblock;
import techreborn.blockentity.machine.multiblock.DistillationTowerBlockEntity;
import techreborn.init.TRContent;

public class GuiDistillationTower extends GuiBase<BuiltContainer> {
	
	public DistillationTowerBlockEntity blockEntity;

	public GuiDistillationTower(int syncID, final PlayerEntity player, final DistillationTowerBlockEntity blockEntity) {
		super(player, blockEntity, blockEntity.createContainer(syncID, player));
		this.blockEntity = blockEntity;
	}
	
	@Override
	protected void drawBackground(final float f, final int mouseX, final int mouseY) {
		super.drawBackground(f, mouseX, mouseY);

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		final GuiBase.Layer layer = Layer.BACKGROUND;
		// Battery slot
		drawSlot(8, 72, layer);
		// Input slots
		drawSlot(35, 27, layer);
		drawSlot(35, 47, layer);
		// Four output slots
		drawOutputSlotBar(78, 36, 4, layer);
		// JEI Button
		builder.drawJEIButton(this, 158, 5, layer);
	}
	
	@Override
	protected void drawForeground(final int mouseX, final int mouseY) {
		super.drawForeground(mouseX, mouseY);
		final GuiBase.Layer layer = Layer.FOREGROUND;

		builder.drawProgressBar(this, blockEntity.getProgressScaled(100), 100, 55, 40, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
		if (blockEntity.getMutliBlock()) {
			addHologramButton(6, 4, 212, layer).clickHandler(this::onClick);
			builder.drawHologramButton(this, 6, 4, mouseX, mouseY, layer);
		} else {
			builder.drawMultiblockMissingBar(this, layer);
			addHologramButton(76, 56, 212, layer).clickHandler(this::onClick);
			builder.drawHologramButton(this, 76, 56, mouseX, mouseY, layer);
		}
		builder.drawMultiEnergyBar(this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxPower(), mouseX, mouseY, 0, layer);

	}

	public void onClick(GuiButtonExtended button, Double x, Double y){
		if (hideGuiElements()) return;
		if (blockEntity.renderMultiblock == null) {
			{
				// This code here makes a basic multiblock and then sets to the selected one.
				final Multiblock multiblock = new Multiblock();
				BlockState industrialCasing = TRContent.MachineBlocks.INDUSTRIAL.getCasing().getDefaultState();
				BlockState standardCasing = TRContent.MachineBlocks.BASIC.getCasing().getDefaultState();

				addComponent(0, 0, 0, standardCasing, multiblock);
				addComponent(1, 0, 0, standardCasing, multiblock);
				addComponent(0, 0, 1, standardCasing, multiblock);
				addComponent(-1, 0, 0, standardCasing, multiblock);
				addComponent(0, 0, -1, standardCasing, multiblock);
				addComponent(-1, 0, -1, standardCasing, multiblock);
				addComponent(-1, 0, 1, standardCasing, multiblock);
				addComponent(1, 0, -1, standardCasing, multiblock);
				addComponent(1, 0, 1, standardCasing, multiblock);

				addComponent(1, 1, 0, industrialCasing, multiblock);
				addComponent(0, 1, 1, industrialCasing, multiblock);
				addComponent(-1, 1, 0, industrialCasing, multiblock);
				addComponent(0, 1, -1, industrialCasing, multiblock);
				addComponent(-1, 1, -1, industrialCasing, multiblock);
				addComponent(-1, 1, 1, industrialCasing, multiblock);
				addComponent(1, 1, -1, industrialCasing, multiblock);
				addComponent(1, 1, 1, industrialCasing, multiblock);

				addComponent(1, 2, 0, standardCasing, multiblock);
				addComponent(0, 2, 1, standardCasing, multiblock);
				addComponent(-1, 2, 0, standardCasing, multiblock);
				addComponent(0, 2, -1, standardCasing, multiblock);
				addComponent(-1, 2, -1, standardCasing, multiblock);
				addComponent(-1, 2, 1, standardCasing, multiblock);
				addComponent(1, 2, -1, standardCasing, multiblock);
				addComponent(1, 2, 1, standardCasing, multiblock);

				addComponent(0, 3, 0, industrialCasing, multiblock);
				addComponent(1, 3, 0, industrialCasing, multiblock);
				addComponent(0, 3, 1, industrialCasing, multiblock);
				addComponent(-1, 3, 0, industrialCasing, multiblock);
				addComponent(0, 3, -1, industrialCasing, multiblock);
				addComponent(-1, 3, -1, industrialCasing, multiblock);
				addComponent(-1, 3, 1, industrialCasing, multiblock);
				addComponent(1, 3, -1, industrialCasing, multiblock);
				addComponent(1, 3, 1, industrialCasing, multiblock);

				blockEntity.renderMultiblock = multiblock;
			}
		} else {
			blockEntity.renderMultiblock = null;
		}
	}

	public void addComponent(final int x, final int y, final int z, final BlockState blockState, final Multiblock multiblock) {
		multiblock.addComponent(new BlockPos(x - Direction.byId(blockEntity.getFacingInt()).getOffsetX() * 2, y, z - Direction.byId(blockEntity.getFacingInt()).getOffsetZ() * 2), blockState);
	}

}
