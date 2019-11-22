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

package techreborn.compat.jei.praescriptum.plasmagenerator;

import net.minecraft.client.Minecraft;

import reborncore.api.praescriptum.fuels.Fuel;
import reborncore.client.guibuilder.GuiBuilder;
import reborncore.common.powerSystem.PowerSystem;

import techreborn.compat.jei.FuelWrapper;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;

/**
 * @author estebes
 */
public class PlasmaGeneratorFuelWrapper extends FuelWrapper {
    public PlasmaGeneratorFuelWrapper(IJeiHelpers jeiHelpers, Fuel fuel) {
        super(fuel);

        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        GuiBuilder.ProgressDirection progressDirection = GuiBuilder.ProgressDirection.RIGHT;
        IDrawableStatic progressStatic = guiHelper.createDrawable(GuiBuilder.defaultTextureSheet, progressDirection.xActive,
                progressDirection.yActive, progressDirection.width, progressDirection.height);

        IDrawableStatic energyGaugeStatic = guiHelper.createDrawable(GuiBuilder.defaultTextureSheet, 113, 151, 12, 48);

        this.progress = guiHelper.createAnimatedDrawable(progressStatic, 50,
                IDrawableAnimated.StartDirection.LEFT, false);

        this.energyGauge = guiHelper.createAnimatedDrawable(energyGaugeStatic, 200,
                IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);

        progress.draw(minecraft, 52, 23);
        energyGauge.draw(minecraft, 79, 4);

        int y = 58;
        int lineHeight = minecraft.fontRenderer.FONT_HEIGHT;

        String totalEnergy = PowerSystem.getLocaliszedPowerFormatted(fuel.getEnergyOutput());
        minecraft.fontRenderer.drawString(totalEnergy,
                (recipeWidth / 2 - minecraft.fontRenderer.getStringWidth(totalEnergy) / 2), y, 0x444444);

        String energyPerTick = PowerSystem.getLocaliszedPowerFormatted(fuel.getEnergyPerTick()) + "/t";
        minecraft.fontRenderer.drawString(energyPerTick,
                (recipeWidth / 2 - minecraft.fontRenderer.getStringWidth(energyPerTick) / 2), y + lineHeight + 1, 0x444444);
    }

    // Fields >>
    private final IDrawableAnimated progress;
    private final IDrawableAnimated energyGauge;
    // << Fields
}
