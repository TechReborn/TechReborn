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

package techreborn.client.gui.generator.lv;

import net.minecraft.entity.player.EntityPlayer;

import reborncore.client.gui.builder.GuiBase;
import reborncore.client.guibuilder.GuiBuilder;

import techreborn.tiles.generator.fluid.lv.TileGasTurbine;

/**
 * @author estebes
 */
public class GuiGasTurbine extends GuiBase {
    public GuiGasTurbine(final EntityPlayer player, final TileGasTurbine tile) {
        super(player, tile, tile.createContainer(player));

        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

        drawSlot(25, 35, layer);
        drawSlot(25, 55, layer);

        builder.drawJEIButton(this, 158, 5, layer);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

        builder.drawProgressBar(this, tile.getRemainingEnergyScaled(100), 100, 83, 48, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
        builder.drawMultiEnergyBar(this, 130, 28, (int) tile.getEnergy(), (int) tile.getMaxPower(), mouseX, mouseY, 0, layer);
        builder.drawTank(this, 44, 25, mouseX, mouseY, tile.tank.getFluid(), tile.tank.getCapacity(), tile.tank.isEmpty(), layer);
    }

    // Fields >>
    TileGasTurbine tile;
    // << Fields
}