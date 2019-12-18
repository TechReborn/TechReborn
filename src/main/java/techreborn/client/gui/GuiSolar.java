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
import reborncore.common.util.StringUtils;
import techreborn.blockentity.generator.SolarPanelBlockEntity;

public class GuiSolar extends GuiBase<BuiltContainer> {

    SolarPanelBlockEntity blockEntity;

    public GuiSolar(int syncID, PlayerEntity player, SolarPanelBlockEntity panel) {
        super(player, panel, panel.createContainer(syncID, player));
        this.blockEntity = panel;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected void drawBackground(float lastFrameDuration, int mouseX, int mouseY) {
        super.drawBackground(lastFrameDuration, mouseX, mouseY);
        final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        super.drawForeground(mouseX, mouseY);
        final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

        builder.drawMultiEnergyBar(this, 156, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxPower(), mouseX, mouseY, 0, layer);

        switch (blockEntity.getSunState()) {
            case SolarPanelBlockEntity.DAYGEN:
                builder.drawString(this, StringUtils.t("techreborn.message.daygen"), 10, 20, 15129632);
                break;
            case SolarPanelBlockEntity.NIGHTGEN:
                builder.drawString(this, StringUtils.t("techreborn.message.nightgen"), 10, 20, 7566195);
                break;
            case SolarPanelBlockEntity.ZEROGEN:
                builder.drawString(this, StringUtils.t("techreborn.message.zerogen"), 10, 20, 12066591);
                break;
        }

        builder.drawString(this, "Generating: " + blockEntity.getGenerationRate() + " E/t", 10, 30, 0);

    }
}
